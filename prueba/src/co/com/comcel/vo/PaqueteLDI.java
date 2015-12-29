package co.com.comcel.vo;

import java.util.List;

public class PaqueteLDI {

	private Integer SPCODE;
	private String NOMBRE;
	private String NOMBRE_AMDOCS;
	private String CCTOCZ;
	private Double CFM;
	private String TIPO_BOLSA;
	private String CLASE;
	private List<AttrPaqLDI> attrPaqLDIList;
	private String DESCRIPCION;
	
	public List<AttrPaqLDI> getAttrPaqLDIList() {
		return attrPaqLDIList;
	}
	public void setAttrPaqLDIList(List<AttrPaqLDI> attrPaqLDIList) {
		this.attrPaqLDIList = attrPaqLDIList;
	}
	public Integer getSPCODE() {
		return SPCODE;
	}
	public void setSPCODE(Integer sPCODE) {
		SPCODE = sPCODE;
	}
	public String getNOMBRE() {
		return NOMBRE;
	}
	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}
	public String getNOMBRE_AMDOCS() {
		return NOMBRE_AMDOCS;
	}
	public void setNOMBRE_AMDOCS(String nOMBREAMDOCS) {
		NOMBRE_AMDOCS = nOMBREAMDOCS;
	}
	public String getCCTOCZ() {
		return CCTOCZ;
	}
	public void setCCTOCZ(String cCTOCZ) {
		CCTOCZ = cCTOCZ;
	}
	public Double getCFM() {
		return CFM;
	}
	public void setCFM(Double cFM) {
		CFM = cFM;
	}
	public String getTIPO_BOLSA() {
		return TIPO_BOLSA;
	}
	public void setTIPO_BOLSA(String tIPOBOLSA) {
		TIPO_BOLSA = tIPOBOLSA;
	}
	public String getCLASE() {
		return CLASE;
	}
	public void setCLASE(String cLASE) {
		CLASE = cLASE;
	}
	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}
	public String getDESCRIPCION() {
		return DESCRIPCION;
	}
	
	
}
