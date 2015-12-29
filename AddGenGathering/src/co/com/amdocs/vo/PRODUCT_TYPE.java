package co.com.amdocs.vo;

public class PRODUCT_TYPE {
	
	public Long getID_PRODUCTYPE() {
		return ID_PRODUCTYPE;
	}
	public void setID_PRODUCTYPE(Long iD_PRODUCTYPE) {
		ID_PRODUCTYPE = iD_PRODUCTYPE;
	}
	public String getPRODUCT_TYPE() {
		return PRODUCT_TYPE;
	}
	public void setPRODUCT_TYPE(String pRODUCT_TYPE) {
		PRODUCT_TYPE = pRODUCT_TYPE;
	}
	public String getDESCRIPCION() {
		return DESCRIPCION;
	}
	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}
	private Long ID_PRODUCTYPE;
	private String PRODUCT_TYPE;
	private String DESCRIPCION;
	private String TECNOLOGIA;
	public String getTECNOLOGIA() {
		return TECNOLOGIA;
	}
	public void setTECNOLOGIA(String tECNOLOGIA) {
		TECNOLOGIA = tECNOLOGIA;
	}
	

}
