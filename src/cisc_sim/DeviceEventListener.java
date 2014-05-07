package cisc_sim;

public interface DeviceEventListener {
	int invokeDeviceInputEvent(int devid);
	int invokeDeviceStatusEvent(int devid);
	void invokeDeviceOutputEvent(int devid, int data);
}
