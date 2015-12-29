package co.com.comcel.vopo;

import java.util.ArrayList;
import java.util.List;

public class Componetes {	
	private String COD_COMPONENTE;
	private String SEGMENTO;
	private String BLOQUEO;
	private String COMPONENT1;
	private String COMPONENT2;
	private String COMPONENT3;
	private String PRODUCT_SPEC;
	private String MAIN_COMP;
	private String DESCRIPCION;
	private String SNCODE;
	private String OBSERVACION;
	private String COMPONENT4;
	private String PREP_MIN;
	private String PREP_MAX;
	private String PREP_DEF;
	private int orden;
	private List<POCodNum> padres_comp;	
	private String TIPO_ALLOWANCE;
	private int   ELEGIDOS;	
	private String inplan;
	private String bfnf;
	public String getTIPO_ALLOWANCE() {
		return TIPO_ALLOWANCE;
	}
	public void setTIPO_ALLOWANCE(String tIPO_ALLOWANCE) {
		TIPO_ALLOWANCE = tIPO_ALLOWANCE;
	}

	public int getELEGIDOS() {
		return ELEGIDOS;
	}

	public void setELEGIDOS(int eLEGIDOS) {
		ELEGIDOS = eLEGIDOS;
	}

	public String getInplan() {
		return inplan;
	}

	public void setInplan(String inplan) {
		this.inplan = inplan;
	}

	public String getBfnf() {
		return bfnf;
	}

	public void setBfnf(String bfnf) {
		this.bfnf = bfnf;
	}
		public String getCOMPONENT4() {
		return COMPONENT4;
	}

	public void setCOMPONENT4(String cOMPONENT4) {
		COMPONENT4 = cOMPONENT4;
	}

	public String getPREP_MIN() {
		return PREP_MIN;
	}

	public void setPREP_MIN(String pREP_MIN) {
		PREP_MIN = pREP_MIN;
	}

	public String getPREP_MAX() {
		return PREP_MAX;
	}

	public void setPREP_MAX(String pREP_MAX) {
		PREP_MAX = pREP_MAX;
	}

	public String getPREP_DEF() {
		return PREP_DEF;
	}

	public void setPREP_DEF(String pREP_DEF) {
		PREP_DEF = pREP_DEF;
	}

	public Componetes(){
		this.padres_comp = new ArrayList<POCodNum>();
	}
	
	
	public List<POCodNum> getPadres_comp() {
		return padres_comp;
	}

	public void setPadres_comp(List<POCodNum> padres_comp) {
		this.padres_comp = padres_comp;
	}

	public int getOrden() {
		return orden;
	}
	public void setOrden(int orden) {
		this.orden = orden;
	}
	
	public String getCOD_COMPONENTE() {
		return COD_COMPONENTE;
	}
	public String getOBSERVACION() {
		return OBSERVACION;
	}
	public void setOBSERVACION(String oBSERVACION) {
		OBSERVACION = oBSERVACION;
	}
	public void setCOD_COMPONENTE(String cOD_COMPONENTE) {
		COD_COMPONENTE = cOD_COMPONENTE;
	}
	public String getSEGMENTO() {
		return SEGMENTO;
	}
	public void setSEGMENTO(String sEGMENTO) {
		SEGMENTO = sEGMENTO;
	}
	public String getBLOQUEO() {
		return BLOQUEO;
	}
	public void setBLOQUEO(String bLOQUEO) {
		BLOQUEO = bLOQUEO;
	}
	public String getCOMPONENT1() {
		return COMPONENT1;
	}
	public void setCOMPONENT1(String cOMPONENT1) {
		COMPONENT1 = cOMPONENT1;
	}
	public String getCOMPONENT2() {
		return COMPONENT2;
	}
	public void setCOMPONENT2(String cOMPONENT2) {
		COMPONENT2 = cOMPONENT2;
	}
	public String getCOMPONENT3() {
		return COMPONENT3;
	}
	public void setCOMPONENT3(String cOMPONENT3) {
		COMPONENT3 = cOMPONENT3;
	}
	public String getPRODUCT_SPEC() {
		return PRODUCT_SPEC;
	}
	public void setPRODUCT_SPEC(String pRODUCT_SPEC) {
		PRODUCT_SPEC = pRODUCT_SPEC;
	}
	public String getMAIN_COMP() {
		return MAIN_COMP;
	}
	public void setMAIN_COMP(String mAIN_COMP) {
		MAIN_COMP = mAIN_COMP;
	}
	public String getDESCRIPCION() {
		return DESCRIPCION;
	}
	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}
	public String getSNCODE() {
		return SNCODE;
	}
	public void setSNCODE(String sNCODE) {
		SNCODE = sNCODE;
	}

}
