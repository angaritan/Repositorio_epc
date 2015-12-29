package co.com.comcel.vo;

public class servicio_comp {
	private long SNCODE;
	/**
	 * @return the sNCODE
	 */
	public long getSNCODE() {
		return SNCODE;
	}
	/**
	 * @param sNCODE the sNCODE to set
	 */
	public void setSNCODE(long sNCODE) {
		SNCODE = sNCODE;
	}
	/**
	 * @return the dESC_SNCODE
	 */
	public String getDESC_SNCODE() {
		return DESC_SNCODE;
	}
	/**
	 * @param dESC_SNCODE the dESC_SNCODE to set
	 */
	public void setDESC_SNCODE(String dESC_SNCODE) {
		DESC_SNCODE = dESC_SNCODE;
	}
	/**
	 * @return the cOMP_CODE
	 */
	public String getCOMP_CODE() {
		return COMP_CODE;
	}
	/**
	 * @param cOMP_CODE the cOMP_CODE to set
	 */
	public void setCOMP_CODE(String cOMP_CODE) {
		COMP_CODE = cOMP_CODE;
	}
	private String DESC_SNCODE;
	private String COMP_CODE;

}
