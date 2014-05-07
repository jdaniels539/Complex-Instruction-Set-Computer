package cisc_sim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Temporary Memory Class
 * 
 * @author jdaniels
 * 
 */

public class Memory {
	private static final int			NUM_OF_BANK			= 2;
	private static final int			BANK_SIZE			= 4096;
	public static final int				ROM_BASE_ADDR		= 2;
	private static final int			BOOT_BASE_ADDR		= 52;
	private static final int			PROGRAM_BASE_ADDR	= 102;
	private Cache cache;
	private byte[][]					bank;
	private int							_MAR;
	private int							_MBR;

	private List<MemoryEventListener>	_listeners			= new ArrayList<MemoryEventListener>();

	public synchronized void addEventListener(MemoryEventListener listener) {
		if (this._listeners.indexOf(listener) == -1)
			this._listeners.add(listener);
	}

	public synchronized void removeEventListener(MemoryEventListener listener) {
		this._listeners.remove(listener);
	}

	private synchronized void _invoke_memoryUpdated() {
		Iterator<MemoryEventListener> i = this._listeners.iterator();
		while (i.hasNext()) {
			i.next().invokeMemoryUpdatedEvent(this);
		}
	}

	public Memory() {
		this._MAR = 0;
		this._MBR = 0;
		this.cache=new Cache();
		this.bank = new byte[NUM_OF_BANK][BANK_SIZE];
		// this.randomizeMemory();
	}

	private void randomizeMemory() {
		Random rand = new Random();
		for (int i = 0; i < NUM_OF_BANK; i++)
			rand.nextBytes(this.bank[i]);
	}

	public void loadRom(byte[] data) {
		this.loadData(data, ROM_BASE_ADDR);
	}

	public void loadBoot(byte[] data) {
		this.loadData(data, BOOT_BASE_ADDR);
	}

	public void loadProgram(byte[] data) {
		this.loadData(data, PROGRAM_BASE_ADDR);
	}

	private void loadData(byte[] data, int startingAddr) {
		int romlength = data.length;
		int bankid = 0;
		int addr = startingAddr;
		for (int i = 0; i < romlength; i++) {
			if (romlength >= BANK_SIZE) {
				bankid = 1;
				addr = 0;
			}
			this.bank[bankid][addr++] = data[i];
		}
	}

	public int getMAR() {
		return this._MAR;
	}

	public void setMAR(int value) {
		this._MAR = value;
		this._invoke_memoryUpdated();
	}

	public int getMBR() {
		return this._MBR;
	}

	public void setMBR(int value) {
		this._MBR = value;
		this._invoke_memoryUpdated();
	}

	public void getData() throws IllegalMemoryAddressException {
		if (this.checkAddress()) {
			this._MBR = this.read(this._MAR);
			this._invoke_memoryUpdated();
		}
	}

	public void setData() throws IllegalMemoryAddressException {
		if (this.checkAddress()) {
			this.write(this._MAR, this._MBR);
			this._invoke_memoryUpdated();
		}
	}

	private int readdirectly(int addr) {
		int addrLo = addr;
		int addrHi = addrLo + 1;
		int bankLo = 0;
		int bankHi = 0;
		if (addrHi > BANK_SIZE - 1) {
			addrHi = addrHi - BANK_SIZE;
			bankHi = 1;
		}

		int hi = (this.bank[bankHi][addrHi] << 8) & 0xFFFF;
		int lo = this.bank[bankLo][addrLo] & 0xFF;
		return hi + lo;
	}
	private int read(int addr){
		if(cache.ReadCache(addr)){
			return cache.GetData();
		}
		else{
			int data=this.readdirectly(addr);
			cache.write(addr,data);//Update the cache if a cache miss happens
			return data;
		}
	}

	private void write(int addr, int data) {
		int addrLo = addr;
		int addrHi = addrLo + 1;
		int bankLo = 0;
		int bankHi = 0;
		if (addrHi > BANK_SIZE - 1) {
			addrHi = addrHi - BANK_SIZE;
			bankHi = 1;
		}

		this.bank[bankHi][addrHi] = (byte) ((data >> 8) & 0xFF);
		this.bank[bankLo][addrLo] = (byte) (data & 0xFF);
		if(cache.IsValid(addr)){
			cache.SetDirty(addr);
		}
		else{
			cache.write(addr, data);
		}
		this._invoke_memoryUpdated();
	}

	private boolean checkAddress() throws IllegalMemoryAddressException {
		boolean ret = false;
		if (this._MAR > BANK_SIZE * 2 - 1 || this._MAR < 0)
			throw new IllegalMemoryAddressException(String.format(
					"Illegal access address at 0x%X.", this._MAR));
		else
			ret = true;

		return ret;
	}

	public byte[][] getDump() {
		return this.bank.clone();
	}

	public void init() {
		for (int i = 0; i < this.bank.length; i++) {
			for (int j = 0; j < this.bank[i].length; j++) {
				this.bank[i][j] = 0;
			}
		}
	}
}