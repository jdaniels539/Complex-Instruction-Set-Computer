package cisc_sim;
/**
 * Event listener for Controller class
 */
public interface ControllerEventListener {
	/**
	 * Invoke the step event when controller executes codes in debug mode
	 */
	void invokeControllerCurrentInstructionEvent(Instruction instr);
	
	/**
	 * Invoke the exception event when an exception is thrown during the execution.  
	 * @param e Exception
	 */
	void invokeControllerExceptionEvent(final Exception e);
	
	/**
	 * Invoke the execution finished event when the controller finished executing.
	 */
	void invokeControllerExecutionFinished();
}
