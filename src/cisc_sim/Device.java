/**
 * 
 */
package cisc_sim;

import java.util.ArrayList;
import java.util.List;

/**
 * The Peripheral Device Class
 * 
 * @author cooniur
 * 
 */
public class Device {
	public static final int		DEVID_MAX	= 32;

	private DeviceEventListener	_listener	= null;

	public void addEventListener(DeviceEventListener listener) {
		this._listener = listener;
	}

	public void removeEventListener(DeviceEventListener listener) {
		this._listener = null;
	}

	public int getData(int devid) {
		return this.invokeInputEvent(devid);
	}

	public void setData(int data, int devid) {
		this.invokeOutputEvent(devid, data);
	}

	public int check(int devid) {
		return this.invokeStatusEvent(devid);
	}

	private int invokeInputEvent(int devid) {
		if (this._listener != null)
			return this._listener.invokeDeviceInputEvent(devid);
		else
			return 0;
	}

	private int invokeStatusEvent(int devid) {
		if (this._listener != null)
			return this._listener.invokeDeviceStatusEvent(devid);
		else
			return 0;
	}

	private void invokeOutputEvent(int devid, int data) {
		if (this._listener != null)
			this._listener.invokeDeviceOutputEvent(devid, data);
	}

}
