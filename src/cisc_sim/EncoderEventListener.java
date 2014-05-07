package cisc_sim;

public interface EncoderEventListener {
	void invokeEncoderSucceededEvent(String msg);
	void invokeEncoderFailedEvent(String msg);
}
