/**
 * 
 */
package cisc_sim;

/**
 * 
 * @version 1.1
 * @time Sunday, Mar 3 2013 7:55PM (EST)
 */
public final class Decoder {
	/**
	 * @param data
	 * @return
	 * @throws IllegalOpcodeException
	 * @throws IllegalMemoryAddressException
	 */
	public static Instruction decodeInstruction(int data)
			throws IllegalOpcodeException, IllegalMemoryAddressException {
		int opcode = getOpcode(data);
		ISNameEnum isname = getISName(opcode);
		Instruction instr = new Instruction(isname);

		// TODO: Add support of floating instructions
		switch (isname) {
		// xxx r, x, address[,I]
			case I_LDR:
			case I_STR:
			case I_LDA:
			case I_JZ:
			case I_JNE:
			case I_SOB:
			case I_ADD:
			case I_SUB:
			case I_LDX:
			case I_STX:
			case I_JMP:
			case I_JSR:
				instr.setI(getI(data));
				instr.setIX(getIX(data));
				instr.setR(getR(data));
				instr.setAddress(getAddress(data));
				break;

			case I_JCC:
				instr.setI(getI(data));
				instr.setIX(getIX(data));
				instr.setCC(getCC(data));
				instr.setAddress(getAddress(data));
				break;

			// xxx immed
			case I_RFS:
				instr.setImmed(getImmed(data));
				break;

			// xxx r, immed
			case I_AIR:
			case I_SIR:
				instr.setR(getR(data));
				instr.setImmed(getImmed(data));
				break;

			// xxx rx, ry
			case I_MUL:
			case I_DIV:
			case I_TST:
			case I_AND:
			case I_OR:
			case I_XOR:
			case I_NOT:
				instr.setRx(getRx(data));
				instr.setRy(getRy(data));
				break;

			// xxx r, count, L/R, A/L
			case I_SRC:
			case I_RRC:
				instr.setR(getShiftR(data));
				instr.setShiftCount(getShiftCount(data));
				instr.setShiftDirection(getShiftDirection(data));
				instr.setShiftType(getShiftType(data));
				break;

			// xxx, r, devid
			case I_IN:
			case I_OUT:
			case I_CHK:
				instr.setR(getDevR(data));
				instr.setDevId(getDevId(data));
				break;

			// TRAP r
			case I_TRAP:
				instr.setR(getR(data));
				break;

			// HLT
			case I_HLT:
				break;

			case I_NOT_EXISTS:
			default:
				throw new IllegalOpcodeException(opcode);
		}
		return instr;
	}

	private static int getOpcode(int data) {
		return (data & 0xFC00) >> 10;
	}

	private static ISNameEnum getISName(int opcode) {
		return ISNameEnum.getInstruction(opcode);
	}

	private static int getI(int data) {
		return (data & 0x200) >> 9;
	}

	private static int getIX(int data) {
		return (data & 0x100) >> 8;
	}

	private static int getR(int data) throws IllegalMemoryAddressException {
		return checkR((data & 0xC0) >> 6);
	}

	private static int getCC(int data) throws IllegalMemoryAddressException {
		return checkCC((data & 0xC0) >> 6);
	}

	private static int getAddress(int data) {
		return (data & 0x3F);
	}

	private static int getImmed(int data) {
		return (data & 0x3F);
	}

	private static int getRx(int data) throws IllegalMemoryAddressException {
		return checkR((data & 0x300) >> 8);
	}

	private static int getRy(int data) throws IllegalMemoryAddressException {
		return checkR((data & 0xC0) >> 6);
	}

	private static int getRBeforeImmed(int data)
			throws IllegalMemoryAddressException {
		return checkR((data & 0x300) >> 8);
	}

	private static int getImmedAfterR(int data)
			throws IllegalMemoryAddressException {
		return checkR((data & 0xFF));
	}

	private static int getShiftDirection(int data) {
		return (data & 0x200) >> 9;
	}

	private static int getShiftR(int data) throws IllegalMemoryAddressException {
		return checkR((data & 0x180) >> 7);
	}

	private static int getShiftType(int data) {
		return (data & 0x40) >> 6;
	}

	private static int getShiftCount(int data) {
		return (data & 0xF);
	}

	private static int getDevId(int data) throws IllegalMemoryAddressException {
		int devid = (data & 0x1F);
		if (devid < 0 || devid > Device.DEVID_MAX)
			throw new IllegalMemoryAddressException(String.format(
					"Illegal device id: %d.", devid));

		return devid;
	}

	private static int getDevR(int data) throws IllegalMemoryAddressException {
		return checkR((data & 0x180) >> 7);
	}

	private static int checkR(int r) throws IllegalMemoryAddressException {
		if (r < 0 || r > Register.REG_MAX - 1)
			throw new IllegalMemoryAddressException(String.format(
					"Illegal register#: %d.", r));

		return r;
	}

	private static int checkCC(int cc) throws IllegalMemoryAddressException {
		if (cc < 0 || cc > Register.CC_MAX)
			throw new IllegalMemoryAddressException(String.format(
					"Illegal condition code#: %d", cc));
		return cc;
	}

	public static void Test(int data) {
		Instruction instr;
		try {
			instr = Decoder.decodeInstruction(data);
			switch (instr.getInstruction()) {
			// xxx r, x, address[,I]
				case I_LDR:
				case I_STR:
				case I_LDA:
				case I_JZ:
				case I_JNE:
				case I_JCC:
				case I_SOB:
				case I_ADD:
				case I_SUB:
				case I_LDX:
				case I_STX:
				case I_JMP:
				case I_JSR:
					System.out.printf("%s %d, %d, %d, %d\n", instr
							.getInstruction().toString().substring(2),
							instr.getI(), instr.getIX(), instr.getR(),
							instr.getAddress());
					break;

				// xxx immed
				case I_RFS:
					System.out.printf("%s %d\n", instr.getInstruction()
							.toString().substring(2), instr.getImmed());
					break;

				// xxx r, immed
				case I_AIR:
				case I_SIR:
					System.out.printf("%s %d, %d\n", instr.getInstruction()
							.toString().substring(2), instr.getR(),
							instr.getImmed());
					break;

				// xxx rx, ry
				case I_MUL:
				case I_DIV:
				case I_TST:
				case I_AND:
				case I_OR:
				case I_NOT:
					System.out.printf("%s %d, %d\n", instr.getInstruction()
							.toString().substring(2), instr.getRx(),
							instr.getRy());
					break;

				// xxx r, count, L/R, A/L
				case I_SRC:
				case I_RRC:
					System.out.printf("%s %d, %d, %s, %s\n", instr
							.getInstruction().toString().substring(2),
							instr.getR(), instr.getShiftCount(),
							instr.getShiftDirection() == 1 ? "L" : "R",
							instr.getShiftType() == 1 ? "L" : "A");
					break;

				// xxx, r, devid
				case I_IN:
				case I_OUT:
				case I_CHK:
					System.out.printf("%s %d, %d\n", instr.getInstruction()
							.toString().substring(2), instr.getR(),
							instr.getDevId());
					break;

				case I_NOT_EXISTS:
				default:
					System.out.println("not exists");
			}
		} catch (IllegalOpcodeException e) {
			System.out.printf("Error: %s\n", e.getMessage());
		} catch (IllegalMemoryAddressException e) {
			System.out.printf("Error: %s\n", e.getMessage());
		}
	}
}