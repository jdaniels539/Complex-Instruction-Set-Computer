package cisc_sim;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Encoder {

	private static final int					INSTRUCTION_LENGTH	= 2;										// 2-byte
																												// instruction

	private List<String>						lineToCode			= new ArrayList<String>();
	private Hashtable<Integer, Integer>			lineToAddress		= new Hashtable<Integer, Integer>();
	private Hashtable<String, Integer>			labelToAddress		= new Hashtable<String, Integer>();
	private Hashtable<String, List<Integer>>	labelVarToLine		= new Hashtable<String, List<Integer>>();
	private String								filename;
	private int									baseAddr;

	private EncoderEventListener				_listener			= null;

	public void addEventListener(EncoderEventListener listener) {
		this._listener = listener;
	}

	public void removeEventListener(EncoderEventListener listener) {
		this._listener = null;
	}

	private void invokeSucceededEvent(String msg) {
		if (this._listener != null) {
			this._listener.invokeEncoderSucceededEvent(msg);
		}
	}

	private void invokeFailedEvent(String msg) {
		if (this._listener != null) {
			this._listener.invokeEncoderFailedEvent(msg);
		}
	}

	public Encoder(String filename, int baseAddr) {
		this.filename = filename;
		this.baseAddr = baseAddr;
	}

	public void Encode() {
		this.load();
		this.saveToBinary();
	}

	private void load() {
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line;
			int linenum = 0;
			int address = this.baseAddr;
			String label = null;

			while ((line = br.readLine()) != null) {
				line = line.trim();
				line = line.replaceAll("\\s+", " ");
				line = line.replaceAll(", ", ",");

				if (line.length() > 0) {
					if (!line.startsWith("@")) {
						lineToCode.add(line);
						lineToAddress.put(linenum, address);

						if (label != null) {
							labelToAddress.put(label, address);
							label = null;
						}

						int varidx = line.indexOf("@");
						if (varidx != -1) {
							String labelvar = line.substring(varidx);
							int labelvar_comma_idx = labelvar.indexOf(",");
							if (labelvar_comma_idx != -1) {
								labelvar = labelvar.substring(0,
										labelvar_comma_idx);
							}

							if (!labelVarToLine.containsKey(labelvar)) {
								List<Integer> labelVarLineList = new ArrayList<Integer>();
								labelVarLineList.add(linenum);
								labelVarToLine.put(labelvar, labelVarLineList);
							} else {
								List<Integer> labelVarLineList = labelVarToLine
										.get(labelvar);
								labelVarLineList.add(linenum);
							}
						}

						linenum++;
						address += INSTRUCTION_LENGTH;
					} else {
						label = line.substring(0, line.length() - 1);
					}
				}
			}
			br.close();
			fr.close();

			Enumeration<String> labelVarList = labelVarToLine.keys();
			while (labelVarList.hasMoreElements()) {
				String labelVar = labelVarList.nextElement();
				int labelAddress = labelToAddress.get(labelVar);

				List<Integer> labelVarAtLineList = labelVarToLine.get(labelVar);
				int len = labelVarAtLineList.size();
				for (int i = 0; i < len; i++) {
					int labelVarAtLine = labelVarAtLineList.get(i);
					if (labelVarAtLine < lineToCode.size()) {
						String code = lineToCode.get(labelVarAtLine);
						lineToCode.set(
								labelVarAtLine,
								code.replaceAll(labelVar,
										Integer.toString(labelAddress)));
					}
				}
			}
		} catch (FileNotFoundException e) {
			this.invokeFailedEvent(e.getMessage());
		} catch (IOException e) {
			this.invokeFailedEvent(e.getMessage());
		}
	}

	private void saveToBinary() {
		boolean failed = false;
		String binfile = this.getExetuableFile();
		DataOutputStream output = null;
		try {
			output = new DataOutputStream(new FileOutputStream(binfile));
			int size = this.lineToCode.size();
			for (int i = 0; i < size; i++) {
				String line = this.lineToCode.get(i);
				int bin = getBinary(line);
				if (bin != -1) {
					byte hi = (byte) ((bin & 0xFF00) >> 8);
					byte lo = (byte) (bin & 0xFF);
					output.writeByte(lo);
					output.writeByte(hi);
				} else {
					this.invokeFailedEvent(String.format(
							"Opcode not exists at line: %s", line));
					failed = true;
					break;
				}
			}
			if (!failed)
				this.invokeSucceededEvent(String
						.format("Compiled succeeded!\nExecutable file outputed to:\n%s",
								binfile));
		} catch (FileNotFoundException e) {
			this.invokeFailedEvent(e.getMessage());
		} catch (IOException e) {
			this.invokeFailedEvent(e.getMessage());
		}
	}

	private int getBinary(String code) {
		String line = code;
		String[] tokens = line.split("\\s|,");
		ISNameEnum instr = ISNameEnum.getInstruction(tokens[0]);
		int r, ix, addr, indr, immed, rx, ry;
		int opcode = instr.getOpcode();
		int bin = 0;
		bin = this.fillOpcode(bin, opcode);
		switch (instr) {
		// xxx r, x, address[,I]
			case I_LDR:
			case I_STR:
			case I_LDA:
			case I_JZ:
			case I_JNE:
			case I_SOB:
			case I_ADD:
			case I_SUB:
			case I_JCC:
				r = Integer.parseInt(tokens[1]);
				ix = Integer.parseInt(tokens[2]);
				addr = Integer.parseInt(tokens[3]);
				indr = tokens.length == 5 ? Integer.parseInt(tokens[4]) : 0;
				bin = this.fillR(bin, r);
				bin = this.fillIX(bin, ix);
				bin = this.fillIndr(bin, indr);
				bin = this.fillAddr(bin, addr);
				break;

			// xxx x, address[,I]
			case I_LDX:
			case I_STX:
			case I_JMP:
			case I_JSR:
				ix = Integer.parseInt(tokens[1]);
				addr = Integer.parseInt(tokens[2]);
				indr = tokens.length == 4 ? Integer.parseInt(tokens[3]) : 0;
				bin = this.fillIX(bin, ix);
				bin = this.fillIndr(bin, indr);
				bin = this.fillAddr(bin, addr);
				break;

			// xxx immed
			case I_RFS:
				immed = Integer.parseInt(tokens[1]);
				bin = this.fillImmed(bin, immed);
				break;

			// xxx r, immed
			case I_AIR:
			case I_SIR:
				r = Integer.parseInt(tokens[1]);
				immed = Integer.parseInt(tokens[2]);
				bin = this.fillR(bin, r);
				bin = this.fillImmed(bin, immed);
				break;

			// xxx rx, ry
			case I_MUL:
			case I_DIV:
			case I_TST:
			case I_AND:
			case I_OR:
			case I_XOR:
				rx = Integer.parseInt(tokens[1]);
				ry = Integer.parseInt(tokens[2]);
				bin = this.fillRx(bin, rx);
				bin = this.fillRy(bin, ry);
				break;

			// xxx rx
			case I_NOT:
				rx = Integer.parseInt(tokens[1]);
				bin = this.fillRx(bin, rx);
				break;

			// xxx r, count, L/R, A/L
			case I_SRC:
			case I_RRC:
				r = Integer.parseInt(tokens[1]);
				int count = Integer.parseInt(tokens[2]);
				int lr = Integer.parseInt(tokens[3]);
				int al = Integer.parseInt(tokens[4]);
				bin = this.fillShiftR(bin, r);
				bin = this.fillShiftLR(bin, lr);
				bin = this.fillShiftAL(bin, al);
				bin = this.fillShiftCount(bin, count);
				break;

			// xxx, r, devid
			case I_IN:
			case I_OUT:
			case I_CHK:
				r = Integer.parseInt(tokens[1]);
				int devid = Integer.parseInt(tokens[2]);
				bin = this.fillIOR(bin, r);
				bin = this.fillIODevid(bin, devid);
				break;

			// TRAP r
			case I_TRAP:
				r = Integer.parseInt(tokens[1]);
				bin = this.fillR(bin, r);
				break;

			// HLT
			case I_HLT:
				break;

			case I_NOT_EXISTS:
			default:
				bin = -1;
		}

		return bin;
	}

	public String getExetuableFile() {
		int dotPos = this.filename.lastIndexOf(".");
		if (dotPos != -1)
			return this.filename.substring(0, dotPos) + ".bin";
		else
			return this.filename + ".bin";
	}

	private int fillOpcode(int bin, int opcode) {
		return bin | (opcode << 10);
	}

	private int fillR(int bin, int r) {
		return bin | (r << 6);
	}

	private int fillIX(int bin, int ix) {
		return bin | (ix << 8);
	}

	private int fillIndr(int bin, int indr) {
		return bin | (indr << 9);
	}

	private int fillAddr(int bin, int addr) {
		return bin | addr;
	}

	private int fillImmed(int bin, int immed) {
		return bin | immed;
	}

	private int fillRx(int bin, int rx) {
		return bin | (rx << 8);
	}

	private int fillRy(int bin, int ry) {
		return bin | (ry << 6);
	}

	private int fillShiftLR(int bin, int lr) {
		return bin | (lr << 9);
	}

	private int fillShiftAL(int bin, int al) {
		return bin | (al << 6);
	}

	private int fillShiftR(int bin, int r) {
		return bin | (r << 7);
	}

	private int fillShiftCount(int bin, int count) {
		return bin | count;
	}

	private int fillIOR(int bin, int r) {
		return bin | (r << 7);
	}

	private int fillIODevid(int bin, int devid) {
		return bin | devid;
	}
}
