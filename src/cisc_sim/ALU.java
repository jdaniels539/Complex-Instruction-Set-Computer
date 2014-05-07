/**
 * 
 */
package cisc_sim;

/**
 * 
 * 		 Note: Since registers are 16-bit and the int type has 32 bits, I
 *         ignore 15-30 bit in all int type variables and treate them as 16-bit
 *         data type.The values range from −32,768(-2^15) to 32,767(2^15-1).
 * 
 */

public class ALU {
	private Register	reg	= null;

	public ALU(Register reg) {
		this.reg = reg;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public ALUResult add(int a, int b) {
		/*
		 *  
		 */
		if ((a + b) < 32767)
			return new ALUResult(a + b);
		else
			reg.setOverflow();
		return new ALUResult();
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public ALUResult sub(int a, int b) {
		if ((a - b) > -32768)
			return new ALUResult(a - b);
		else
			reg.setUnderflow();
		return new ALUResult();

	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public void tst(int a, int b) {

		if (a == b)
			reg.setCC(1, 3);
		else
			reg.setCC(0, 3);

	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public ALUResult and(int a, int b) {
		return new ALUResult(a & b);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public ALUResult or(int a, int b) {
		return new ALUResult(a | b);
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public ALUResult not(int a) {
		return new ALUResult(~a);
	}

	public ALUResult xor(int a, int b) {
		return new ALUResult(a ^ b);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public ALUResult mul(int a, int b) {
		int temp = a * b;
		if (temp >= -32768 && temp <= 32767) {
			return new ALUResult(temp);
		} else {
			int high;
			int low;
			low = temp & 0xFFFF;
			high = (temp & 0xFFFF0000) >> 16 ;
			return new ALUResult(high, low);
		}

	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public ALUResult div(int a, int b) {
		if (b != 0) {
			int quotient = a / b;
			int remainder = a % b;
			return new ALUResult(quotient, remainder);
		} else
			reg.setDivByZero();
		return new ALUResult();
	}

	/**
	 * 
	 * @param a
	 * @param count
	 * @param direction
	 *            1:left,0:right
	 * @param type
	 *            1:logically,0:arithmetically
	 * @return
	 */
	public ALUResult src(int a, int count, int direction, int type) {
		int temp = 0;
		int sign = 0;
		a = this.gettrueform(a);
		switch (type) {
			case 0:
				if (direction == 0) {
					for (int loop = 0; loop < count; loop++) {
						a = a >> 1;
						temp = a & 0x40000000;
						temp = temp >> 16;
						a = a & 0x80007fff;
						a = a | temp;
					}
				}
				if (direction == 1) {
					sign = a & 0x80000000;
					if (sign == 0) {
						for (int loop = 0; loop < count; loop++) {
							a = a << 1;
							if (this.getcomplement(a) > 32767) {
								reg.setOverflow();
								a = 0;
								break;
							}
							a = a & 0x80007fff;
						}
					} else {
						for (int loop = 0; loop < count; loop++) {
							temp = a & 0x00004000;
							if (temp != 0) {
								reg.setUnderflow();
								a = 0;
								break;
							}
							a = a << 1;
							a = a | 0x80000000;
							;// keep the sign bit
							a = a & 0x80007fff;
						}
					}
				}
				break;
			case 1:
				if (direction == 0) {
					for (int loop = 0; loop < count; loop++) {
						a = a >>> 1;
						temp = a & 0x40000000;
						temp = temp >> 16;
						a = a & 0x80007fff;
						a = a | temp;
					}
				}
				if (direction == 1) {
					a = a << count;
					a = a & 0x80007fff;
				}
				break;
		}
		a = this.getcomplement(a);
		return new ALUResult(a);

	}

	/**
	 * 
	 * @param a
	 * @param count
	 * @param direction
	 *            1:left,0:right
	 * @param type
	 *            1:logically,0:arithmetically
	 * @return
	 */
	public ALUResult rrc(int a, int count, int direction, int type) {
		int temp = 0;
		int sign = 0;
		a = this.gettrueform(a);
		switch (type) {
			case 0:
				if (direction == 0) {
					for (int loop = 0; loop < count; loop++) {
						temp = a & 0x00000001;// get 0 bit
						temp = temp << 14;
						a = a >> 1;
						a = a | temp;// set 14 bit to be the former 0 bit
					}
				}
				if (direction == 1) {
					for (int loop = 0; loop < count; loop++) {
						sign = a & 0x80000000;
						temp = a & 0x00004000;
						temp = temp >> 14;
						a = a << 1;
						a = a & 0x8001ffff;
						a = a | temp;
					}
				}
				break;
			case 1:
				int extension;
				if (direction == 0) {
					for (int loop = 0; loop < count; loop++) {
						sign = a & 0x80000000;
						sign = sign >>> 17;
						temp = a & 0x00000001;
						temp = temp << 31;
						a = a >>> 1;
						a = a | temp;
						a = a | sign;
						a = a & 0x8001ffff;
					}
				}
				if (direction == 1) {
					sign = a & 0x80000000;
					sign = sign >>> 31;
					temp = a & 0x00004000;
					temp = temp << 17;
					a = a << 1;
					a = a | temp;
					a = a | sign;
					a = a & 0x8001ffff;
				}
				break;
			default:
				break;
		}
		a = this.getcomplement(a);
		return new ALUResult(a);
	}

	/**
	 * Get the true form of the code
	 * 
	 * @param obj
	 * @return
	 */
	private int gettrueform(int obj) {
		if (obj >= 0) {
			return obj;
		} else {
			obj = obj - 1;
			obj = ~obj;
			obj = obj | 0x80000000;
			return obj;
		}
	}

	/**
	 * Get the complement of the code
	 * 
	 * @param obj
	 * @return
	 */
	private int getcomplement(int obj) {
		if (obj >= 0) {
			return obj;
		} else {

			obj = ~obj;
			obj = obj | 0x80000000;
			obj = obj + 1;
			return obj;
		}
	}
}