package cisc_sim;

public class IllegalOpcodeException extends Exception {
	/**
	 * Initialize an instance of IllegalOpcodeException
	 * 
	 * @param opcode
	 *            The non-exist opcode
	 */
	public IllegalOpcodeException(int opcode) {
		super(String.format("Opcode not exist: %o (in octal) or 0x%X.\n",
				opcode, opcode));
	}
}