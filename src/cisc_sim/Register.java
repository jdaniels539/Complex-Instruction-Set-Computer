/**
 * 
 */
package cisc_sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author cooniur
 * 
 */
public class Register {
	public static final int						REG_MAX		= 4;
	public static final int						CC_MAX		= 4;

	private int									_PC;
	private int[]								_register;
	private int[]								_cc;
	private int									_X0;
	private int									_IR;
	private int									_MSR;
	private int									_MFR;

	private List<RegisterEventListener>	_listeners	= new ArrayList<RegisterEventListener>();

	public synchronized void addEventListener(
			RegisterEventListener listener) {
		if (this._listeners.indexOf(listener) == -1)
			this._listeners.add(listener);
	}

	public synchronized void removeEventListener(
			RegisterEventListener listener) {
		this._listeners.remove(listener);
	}

	private synchronized void _invoke_registerUpdated() {
		Iterator<RegisterEventListener> i = this._listeners.iterator();
		while (i.hasNext()) {
			i.next().invokeRegisterUpdatedEvent(this);
		}
	}

	/**
	 * Get the content of the regNo-th register. <br>
	 * If regNo is negative or greater than the maximum # of registers, an
	 * IllegalMemoryAddressException will be thrown.
	 * 
	 * @param regNo
	 * @return
	 */
	public int getReg(int regNo) {
		return this._register[regNo];
	}

	/**
	 * Set the content of the regNo-th register. <br>
	 * If regNo is negative or greater than the maximum # of registers, an
	 * IllegalMemoryAddressException will be thrown.
	 * 
	 * @param value
	 * @param regNo
	 * @return
	 */
	public void setReg(int value, int regNo) {
		this._register[regNo] = value;
		this._invoke_registerUpdated();
	}

	/**
	 * Get the content of the index register.
	 * 
	 * @return
	 */
	public int getX0() {
		return this._X0;
	}

	/**
	 * Set the content of the index register.
	 * 
	 * @param value
	 * @return
	 */
	public void setX0(int value) {
		this._X0 = value;
		this._invoke_registerUpdated();
	}

	/**
	 * Get the content of the program counter, and PC++
	 * 
	 * @return
	 * 
	 */
	public int getPC() {
		int pc = this._PC;
		return pc;
	}

	/**
	 * Get the content of the instruction register.
	 * 
	 * @return
	 */
	public int getIR() {
		return this._IR;
	}

	/**
	 * Set the content of the instruction register.
	 * 
	 * @param binaryInstruction
	 * @return
	 */
	public void setIR(int binaryInstruction) {
		this._IR = binaryInstruction;
		this._invoke_registerUpdated();
	}

	/**
	 * Get the content of the machine status register.
	 * 
	 * @return
	 */
	public int getMSR() {
		return this._MSR;
	}

	/**
	 * Set the content of the machine status register.
	 * 
	 * @param machineStatus
	 * @return
	 */
	public void setMSR(int machineStatus) {
		this._MSR = machineStatus;
		this._invoke_registerUpdated();
	}

	/**
	 * Get the content of the machine fault register.
	 * 
	 * @return
	 */
	public int getMFR() {
		return this._MFR;
	}

	/**
	 * Set the content of the machine fault register.
	 * 
	 * @param machineFaultCode
	 * @return
	 */
	public void setMFR(int machineFaultCode) {
		this._MFR = machineFaultCode;
		this._invoke_registerUpdated();
	}

	/**
	 * Set the content of the program counter.
	 * 
	 * @param newPC
	 * @return
	 */
	public void setPC(int newPC) {
		this._PC = newPC;
		this._invoke_registerUpdated();
	}

	/**
	 * Get the condition code at the ccNo-th bit of the condition code register.
	 * 
	 * @param ccNo
	 * @return
	 */
	public int getCC(int ccNo) {
		return this._cc[ccNo];
	}

	/**
	 * Set the condition code at the ccNo-th bit of the condition code register.
	 * 
	 * @param value
	 * @param ccNo
	 * @return
	 */
	public void setCC(int value, int ccNo) {
		this._cc[ccNo] = value;
		this._invoke_registerUpdated();
	}

	/**
	 * Increase the program counter by 2 (because each instruction is 2-byte
	 * long)
	 * 
	 * @return
	 */
	public void incPC() {
		this.setPC(this._PC + 2);
	}

	/**
	 * Set the 1st bit of condition code, OVERFLOW, to 1.
	 * 
	 * @return
	 */
	public void setOverflow() {
		this.setCC(0, 1);
	}

	/**
	 * Set the 2nd bit of condition code, UNDERFLOW, to 1.
	 * 
	 * @return
	 */
	public void setUnderflow() {
		this.setCC(1, 1);
	}

	/**
	 * Set the 3rd bit of condition code, DIVZERO, to 1.
	 * 
	 */
	public void setDivByZero() {
		this.setCC(2, 1);
	}

	/**
	 * Set the 4th bit of condition code, EQUALORNOT, to 1.
	 * 
	 * @return
	 */
	public void setEqual() {
		this.setCC(3, 1);
	}

	/**
	 * Set the 4th bit of condition code, EQUALORNOT, to 0.
	 * 
	 * @return
	 */
	public void setEqualNot() {
		this.setCC(3, 0);
	}

	/**
	 * Initialize an instance of the object
	 * 
	 * @return
	 */
	public Register() {
		this._register = new int[REG_MAX];
		this._cc = new int[CC_MAX];
	}

	public void init() {
		for (int i=0; i<this._register.length; i++) {
			this.setReg(0, i);
		}
		
		for (int i=0; i<this._cc.length; i++) {
			this.setCC(0, i);
		}
		
		this.setIR(0);
		this.setMFR(0);
		this.setMSR(0);
		this.setPC(0);
		this.setX0(0);
	}
}