/**
 * 
 */
package cisc_sim;

/**
 * Instruction Name Enum
 * 
 * @author cooniur
 * 
 */
public enum ISNameEnum {
	/**
	 * Instruction not exists
	 */
	I_NOT_EXISTS(-1),
	// Miscellaneous Instructions
	I_HLT(0), I_TRAP(030),
	// Load/Store Instructions
	I_LDR(01), I_STR(02), I_LDA(03), I_LDX(041), I_STX(042),
	// Transfer Instructions
	I_JZ(010), I_JNE(011), I_JCC(012), I_JMP(013), I_JSR(014), I_RFS(015), I_SOB(
			016),
	// Arithmetic and Logical Instructions
	I_ADD(04), I_SUB(05), I_AIR(06), I_SIR(07), I_MUL(020), I_DIV(021), I_TST(
			022), I_AND(023), I_OR(024), I_NOT(025), I_XOR(026), I_SRC(031), I_RRC(
			032),
	// I/O Operations Instructions
	I_IN(061), I_OUT(062), I_CHK(063),
	// Floating Point Instructions/Vector Operations Instructions(Used in Part3)
	I_FADD(033), I_FSUB(034), I_VADD(035), I_VSUB(035), I_CNVRT(037);

	private final int	_opcode;

	private ISNameEnum(int opcode) {
		this._opcode = opcode;
	}

	/**
	 * Convert instruction enum obj to opcode
	 * 
	 * @return opcode
	 */
	public int getOpcode() {
		return this._opcode;
	}

	/**
	 * Convert opcode to instruction enum obj.
	 * 
	 * @opcode Binary instruction
	 * @return Instruction enum obj
	 */
	public static ISNameEnum getInstruction(int opcode) {
		switch (opcode) {
		// Miscellaneous Instructions
			case 00:
				return ISNameEnum.I_HLT;
			case 030:
				return ISNameEnum.I_TRAP;
				// Load/Store Instructions
			case 01:
				return ISNameEnum.I_LDR;
			case 02:
				return ISNameEnum.I_STR;
			case 03:
				return ISNameEnum.I_LDA;
			case 041:
				return ISNameEnum.I_LDX;
			case 042:
				return ISNameEnum.I_STX;
				// Transfer Instructions
			case 010:
				return ISNameEnum.I_JZ;
			case 011:
				return ISNameEnum.I_JNE;
			case 012:
				return ISNameEnum.I_JCC;
			case 013:
				return ISNameEnum.I_JMP;
			case 014:
				return ISNameEnum.I_JSR;
			case 015:
				return ISNameEnum.I_RFS;
			case 016:
				return ISNameEnum.I_SOB;
				// Arithmetic and Logical Instructions
			case 04:
				return ISNameEnum.I_ADD;
			case 05:
				return ISNameEnum.I_SUB;
			case 06:
				return ISNameEnum.I_AIR;
			case 07:
				return ISNameEnum.I_SIR;
			case 020:
				return ISNameEnum.I_MUL;
			case 021:
				return ISNameEnum.I_DIV;
			case 022:
				return ISNameEnum.I_TST;
			case 023:
				return ISNameEnum.I_AND;
			case 024:
				return ISNameEnum.I_OR;
			case 025:
				return ISNameEnum.I_NOT;
			case 026:
				return ISNameEnum.I_XOR;
			case 031:
				return ISNameEnum.I_SRC;
			case 032:
				return ISNameEnum.I_RRC;
				// I/O Operations Instructions
			case 061:
				return ISNameEnum.I_IN;
			case 062:
				return ISNameEnum.I_OUT;
			case 063:
				return ISNameEnum.I_CHK;
				// Floating Point Instructions/Vector Operations
				// Instructions(Used in Part3)
			case 033:
				return ISNameEnum.I_FADD;
			case 034:
				return ISNameEnum.I_FSUB;
			case 035:
				return ISNameEnum.I_VADD;
			case 036:
				return ISNameEnum.I_VSUB;
			case 037:
				return ISNameEnum.I_CNVRT;
			default:
				return ISNameEnum.I_NOT_EXISTS;
		}
	}

	public static ISNameEnum getInstruction(String opname) {
		opname = opname.toUpperCase();
		if (opname.equals("HLT"))
			return ISNameEnum.I_HLT;
		else if (opname.equals("TRAP"))
			return ISNameEnum.I_TRAP;

		// Load/Store Instructions
		else if (opname.equals("LDR"))
			return ISNameEnum.I_LDR;
		else if (opname.equals("STR"))
			return ISNameEnum.I_STR;
		else if (opname.equals("LDA"))
			return ISNameEnum.I_LDA;
		else if (opname.equals("LDX"))
			return ISNameEnum.I_LDX;
		else if (opname.equals("STX"))
			return ISNameEnum.I_STX;

		// Transfer Instructions
		else if (opname.equals("JZ"))
			return ISNameEnum.I_JZ;
		else if (opname.equals("JNE"))
			return ISNameEnum.I_JNE;
		else if (opname.equals("JCC"))
			return ISNameEnum.I_JCC;
		else if (opname.equals("JMP"))
			return ISNameEnum.I_JMP;
		else if (opname.equals("JSR"))
			return ISNameEnum.I_JSR;
		else if (opname.equals("RFS"))
			return ISNameEnum.I_RFS;
		else if (opname.equals("SOB"))
			return ISNameEnum.I_SOB;

		// Arithmetic and Logical Instructions
		else if (opname.equals("ADD"))
			return ISNameEnum.I_ADD;
		else if (opname.equals("SUB"))
			return ISNameEnum.I_SUB;
		else if (opname.equals("AIR"))
			return ISNameEnum.I_AIR;
		else if (opname.equals("SIR"))
			return ISNameEnum.I_SIR;
		else if (opname.equals("MUL"))
			return ISNameEnum.I_MUL;
		else if (opname.equals("DIV"))
			return ISNameEnum.I_DIV;
		else if (opname.equals("TST"))
			return ISNameEnum.I_TST;
		else if (opname.equals("AND"))
			return ISNameEnum.I_AND;
		else if (opname.equals("OR"))
			return ISNameEnum.I_OR;
		else if (opname.equals("NOT"))
			return ISNameEnum.I_NOT;
		else if (opname.equals("XOR"))
			return ISNameEnum.I_XOR;
		else if (opname.equals("SRC"))
			return ISNameEnum.I_SRC;
		else if (opname.equals("RRC"))
			return ISNameEnum.I_RRC;

		// I/O Operations Instructions
		else if (opname.equals("IN"))
			return ISNameEnum.I_IN;
		else if (opname.equals("OUT"))
			return ISNameEnum.I_OUT;
		else if (opname.equals("CHK"))
			return ISNameEnum.I_CHK;

		// Floating Point Instructions/Vector Operations Instructions(Used in
		// Part3)
		else if (opname.equals("FADD"))
			return ISNameEnum.I_FADD;
		else if (opname.equals("FSUB"))
			return ISNameEnum.I_FSUB;
		else if (opname.equals("VADD"))
			return ISNameEnum.I_VADD;
		else if (opname.equals("VSUB"))
			return ISNameEnum.I_VSUB;
		else if (opname.equals("CNVRT"))
			return ISNameEnum.I_CNVRT;
		else
			return ISNameEnum.I_NOT_EXISTS;
	}
}
