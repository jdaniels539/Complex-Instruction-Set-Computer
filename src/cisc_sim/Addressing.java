package cisc_sim;

public class Addressing {

	/**
	 * Calculate effective address, and replace the address field in the
	 * Instruction object with an effective address.
	 * 
	 * @param instru
	 *            An object of Instruction class
	 * @param reg
	 *            Register
	 * @param mem
	 *            Memory
	 * @throws IllegalMemoryAddressException
	 *             Illegal memory address access detected
	 */
	public static void calculateEA(Instruction instru, Register reg, Memory mem)
			throws IllegalMemoryAddressException {

		switch (instru.getInstruction()) {
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
				// xxx x, address[,I]
			case I_LDX:
			case I_STX:
			case I_JMP:
			case I_JSR:
				int effectiveAddr = instru.getAddress();

				// If use indexing
				if (instru.getIX() == 1) {
					effectiveAddr = getIndexedAddr(reg.getX0(), effectiveAddr);
				}

				// If indirectly addressing
				if (instru.getI() == 1) {
					mem.setMAR(effectiveAddr);
					mem.getData();
					effectiveAddr = mem.getMBR();
				}
				instru.setAddress(effectiveAddr);
				break;
		}
	}

	private static int getIndexedAddr(int x0, int oldAddr) {
		return x0 + oldAddr;
	}
}
