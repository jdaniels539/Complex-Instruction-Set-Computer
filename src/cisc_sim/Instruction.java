/**
 * 
 */
package cisc_sim;

/**
 * Instruction class.<br>
 * Contains the decoded information of an instruction
 * 
 * @author cooniur
 * 
 */
public class Instruction {
	/**
	 * Opcode
	 */
	private ISNameEnum	_instruction;
	/**
	 * <ol>
	 * <li>For Load/Store/Transfer/ADD/SUB/Floating Point/Vector instructions,
	 * _param1 indicates whether using <strong>indirect addressing</strong>:
	 * 0=No, 1=Yes.</li>
	 * <li>For Shift/Rotate instructions, _param1 indicates shift direction:
	 * 0=Right, 1=Left.</li>
	 * <li>For MUL*, DIV**, TST, AND, OR, NOT instructions, _param1 is a
	 * register selector indicating the first register that is involved in the
	 * operation, of which the value is from 0 to 2.<br>
	 * <strong>))*</strong> For MUL, the higher bits of the result is stored in
	 * r(_param1), the lower in r(_param1 + 1).<br>
	 * <strong>))**</strong> For DIV, the quotient is stored in r(_param1), the
	 * remainder in r(_param1 + 1).</li>
	 * <li>For other instructions, _param1 is not used.</li>
	 * </ol>
	 */
	private int			_param1;
	/**
	 * <ol>
	 * <li>For Load/Store/Transfer/ADD/SUB/Floating Point/Vector instructions,
	 * _param2 indicates whether <strong>using index</strong>: 0=No, 1=Yes.</li>
	 * <li>For Shift/Rotate instructions, _param2 indicates shift type:
	 * 0=Arithmetically (i.e. reserving sign bit), 1=Logically</li>
	 * <li>For MUL, DIV, TST, AND, OR instructions, _param2 is a register
	 * selector indicating the second register that is involved in the
	 * operation, of which the value is from 0 to 2.</li>
	 * <li>For other instructions, _param2 is not used.</li>
	 * </ol>
	 */
	private int			_param2;
	/**
	 * <ol>
	 * <li>For Load/Store/Transfer/ADD/SUB/Shift/Rotate/Floating
	 * Point/Vector/I/O instructions, _param3 is a register selector (or
	 * condition code selector in JCC) indicating the register (or the condition
	 * code) that is involved in the operation, of which the value is from 0 to
	 * Register.REG_MAX (or Register.CC_MAX for condition code).</li>
	 * <li>For other instructions, _param3 is not used.</li>
	 */
	private int			_param3;
	/**
	 * <ol>
	 * <li>For Load/Store/Transfer/ADD/SUB/Floating Point/Vector instructions,
	 * _param4 is the address where the data used in the operation is stored.</li>
	 * <li>For Shift/Rotate instructions, _param4 is the shift count.</li>
	 * <li>For I/O instructions, _param4 is the device id, from 0 to
	 * Device.DEVID_MAX.</li>
	 * <li>For other instructions, _param4 is not used.</li>
	 */
	private int			_param4;

	/**
	 * Initialize an instance
	 * 
	 * @param isname
	 *            Instruction name
	 */
	public Instruction(ISNameEnum isname) {
		this._instruction = isname;
	}

	public ISNameEnum getInstruction() {
		return this._instruction;
	}

	public int getI() {
		return this._param1;
	}

	public void setI(int value) {
		if (value != 0)
			this._param1 = 1;
		else
			this._param1 = 0;
	}

	public int getIX() {
		return this._param2;
	}

	public void setIX(int value) {
		if (value != 0)
			this._param2 = 1;
		else
			this._param2 = 0;
	}

	public int getR() {
		return this._param3;
	}

	public void setR(int value) {
		this._param3 = value;
	}

	public int getCC() {
		return this._param3;
	}

	public void setCC(int cc) {
		this._param3 = cc;
	}

	public int getAddress() {
		return this._param4;
	}

	/**
	 * Set the value of the address in an instruction
	 * 
	 * @param value
	 * 
	 */
	public void setAddress(int value) {
		this._param4 = value;
	}

	public int getImmed() {
		return this._param4;
	}

	public void setImmed(int value) {
		this._param4 = value;
	}

	public int getShiftDirection() {
		return this._param1;
	}

	public void setShiftDirection(int value) {
		if (value != 0)
			this._param1 = 1;
		else
			this._param1 = 0;
	}

	public int getShiftType() {
		return this._param2;
	}

	public void setShiftType(int value) {
		if (value != 0)
			this._param2 = 1;
		else
			this._param2 = 0;
	}

	public int getShiftCount() {
		return this._param4;
	}

	public void setShiftCount(int value) {
		this._param4 = value;
	}

	public int getRx() {
		return this._param1;
	}

	public void setRx(int value) {
		this._param1 = value;
	}

	public int getRy() {
		return this._param2;
	}

	public void setRy(int value) {
		this._param2 = value;
	}

	public int getDevId() {
		return this._param4;
	}

	public void setDevId(int value) {
		this._param4 = value;
	}

	public String toString() {
		String instr = this._instruction.toString().substring(2) + " ";
		String params = "";
		switch (this._instruction) {
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
				params = String.format("%d, %d, 0x%X, %d", this.getR(),
						this.getIX(), this.getAddress(), this.getI());
				break;

			// xxx x, address[,I]
			case I_LDX:
			case I_STX:
			case I_JMP:
			case I_JSR:
				params = String.format("%d, 0x%X, %d", this.getIX(),
						this.getAddress(), this.getI());
				break;

			// xxx immed
			case I_RFS:
				params = String.format("%d", this.getImmed());
				break;

			// xxx r, immed
			case I_AIR:
			case I_SIR:
				params = String.format("%d, %d", this.getR(), this.getImmed());
				break;

			// xxx rx, ry
			case I_MUL:
			case I_DIV:
			case I_TST:
			case I_AND:
			case I_OR:
			case I_XOR:
				params = String.format("%d, %d", this.getRx(), this.getRy());
				break;

			// xxx rx
			case I_NOT:
				params = String.format("%d", this.getRx());
				break;

			// xxx r, count, L/R, A/L
			case I_SRC:
			case I_RRC:
				params = String.format("%d, %d, %d, %d", this.getR(),
						this.getShiftCount(), this.getShiftDirection(),
						this.getShiftType());
				break;

			// xxx, r, devid
			case I_IN:
			case I_OUT:
			case I_CHK:
				params = String.format("%d, %d", this.getR(), this.getDevId());
				break;

			// TRAP r
			case I_TRAP:
				params = String.format("%d", this.getR());
				break;

			// HLT
			case I_HLT:
				params = "";
				break;

			case I_NOT_EXISTS:
			default:
				instr = "NOT_EXISTS";
		}
		return instr + params;
	}
}
