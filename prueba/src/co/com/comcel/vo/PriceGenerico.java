package co.com.comcel.vo;

public class PriceGenerico {

	private String ID_PRICE;
	private String DES_PRICE;
	private String TIPO_PRICE;
	private Long ALLOWANCE;
	private String RATE;
	
	public Long getALLOWANCE() {
		return ALLOWANCE;
	}
	public void setALLOWANCE(Long aLLOWANCE) {
		ALLOWANCE = aLLOWANCE;
	}
	public String getRATE() {
		return RATE;
	}
	public void setRATE(String rATE) {
		RATE = rATE;
	}
	public String getID_PRICE() {
		return ID_PRICE;
	}
	public void setID_PRICE(String iDPRICE) {
		ID_PRICE = iDPRICE;
	}
	public String getDES_PRICE() {
		return DES_PRICE;
	}
	public void setDES_PRICE(String dESPRICE) {
		DES_PRICE = dESPRICE;
	}
	public String getTIPO_PRICE() {
		return TIPO_PRICE;
	}
	public void setTIPO_PRICE(String tIPOPRICE) {
		TIPO_PRICE = tIPOPRICE;
	}
	
}
