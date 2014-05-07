/**
 * 
 */
package cisc_sim;


public class ALUResult {

	private int _param1;
	private int _param2;

	/**
	 * returns _param1
	 * @return 
	 */
	public int getHi() {
		return _param1;
	}

	/**
	 * returns _param2
	 * @return 
	 */
	public int getLo() {
		return _param2;
	}

	/**
	 * returns _param1
	 * @return 
	 */
	public int getQuotient() {
		return _param1;
	}

	/**
	 * returns _param2
	 * @return 
	 */
	public int getRemainder() {
		return _param2;
	}

	/**
	 * returns _param1
	 * @return 
	 */
	public int getResult() {
		return _param1;
	}

	/**
	 * 
	 * @param param1
	 * @param param2
	 * @return 
	 */
	public ALUResult(int param1, int param2) {
		this._param1=param1;
		this._param2=param2;
	}

	/**
	 * 
	 * @param param1
	 * @return 
	 */
	public ALUResult(int param1) {
		this._param1=param1;
		this._param2=0;
	}
	public ALUResult(){
		this._param1=0;
		this._param2=0;
	}

}