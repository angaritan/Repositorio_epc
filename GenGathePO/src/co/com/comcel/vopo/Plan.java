package co.com.comcel.vopo;

import java.util.List;

public class Plan {
	private String OBSCOMERCIALES; 
	private String TIPOPLAN; 
	private String SEGMENTO; 	
	private String RETARIFICA; 
	private String FLIARETARIF; 
	private Long USUARIOS_ACTIVOS;
	private String DESACTIVACION; 
	private String OBSDESACTIVACION; 
	private String FECHA_INICIO; 
	private String FECHA_VENTA;
	private String EFFECTIVE_DATE;	
	private String PLANPREPAGO; 
	private Long OPMX;
	private String MIGRACION;
	private Long TMCODE; 
	private String PLAN; 
	private Long SPCODE; 
	private String PAQUETE; 
	private Double CFM; 
	private String DES12X12; 
	private String IVA; 
	private Long RICODE; 
	private String PROMOCION; 
	private Integer MESES_PROMO;
	private Integer PROMO_INTERCALACION;
	private String TIPO_ALLOWANCE_PROMO;
	private String INH; 
	private Long TMCODE_EQ; 
	private Long TMCODE_BASE_BUNDLE; 
	private Long CFM_BASE_BUNDLE; 	
	private String TIPOBUZON; 
	private String ROLLOVER; 
	private Long ALLW_ONNET; 
	private Long ALLW_FIJOS; 
	private Long ALLW_OFFNET; 
	private Long ALLW_TDESTINO; 
	private Long ALLW_ELEGONNET; 
	private Long ALLW_ELEGONNET1; 
	private Long ALLW_ELEGNIT; 
	private Long ALLW_LDI; 
	private Long ALLW_EFIJO;
	private String CARACT_PLAN; 
	private String CARACT_ELEG; 
	private String CODIGO_ELEG; 
	private String CARACT_ELEG_OLD; 
	private Long ALLW_PROMO; 
	private String HORARIO_PROMO; 
	private Long   ALLW_SMS; 
	private String CODIGO_SMS; 
	private Long   ALLW_MMS; 
	private String CODIGO_MMS; 
	private String ALLW_GPRS; 
	private String CODIGO_GPRS; 
	private String ALLW_BLACKBERRY; 
	private String CODIGO_BB; 
	private String CODIGO_GPRSBB;
	private String VALORADC_ONNET; 
	private String VALORADC_FIJO; 
	private String VALORADC_OFFNET; 
	private String VALORADC_EFIJO; 
	private String VALORADC_ENIT; 
	private String VALORADC_SMS; 
	private String VLR_SMSOFFNET; 
	private String VLR_MMS; 
	private String VALORADC_GPRS; 
	private String VALORADC_BB; 
	private String USO_JUSTO; 
	private String TIPO_BLACKBERRY; 
	private String NOMBRE_APROV; 
	private String TIPO_APROV; 
	private Long   QA_SERVICIO; 
	private String DPI_INICIAL; 
	private String DPI_CONTROL; 
	private Double VALORINC_ONNET; 
	private Double VALORINC_FIJO; 
	private Double VALORINC_OFFNET; 
	private String COD_DOC1; 
	private String CAMPO;
	private String DESCRIPCION1;
	private String DESCRIPCION2;
	private String DESCRIPCION3;
	private String DESCRIPCION4;
	private String DESCRIPCION5;
	private String DESCRIPCION6;
	private String DESCRIPCION7;
	private String DESCRIPCION8;	
	private String RC_ACT; 
	private String RC_CONTROL; 
	private String REQUIRE_IS; 
	private String DATA_ADD_PACK; 
	private String PROMO_CATEGORY_PLAN_PENALTY; 
	private String GAMA_OCC; 
	private String GAMA_COS; 
	private String GAMA_ORI; 
	private String WEB_QOS_ACT; 
	private String WEB_QOS_CONTROL; 
	private String WAP_QOS_ACT; 
	private String WAP_QOS_CONTROL; 
	private String BB_QOS_ACT; 
	private String BB_QOS_CONTROL; 
	private String MMS_QOS_ACT; 
	private String MMS_QOS_CONTROL; 
	private String IAPN_QOS_ACT; 
	private String IAPN_QOS_CONTROL; 
	private String VVM_QOS_ACT; 
	private String VVM_QOS_CONTROL; 
	private String NML_QOS_ACT; 
	private String NML_QOS_CONTROL; 
	private String PAPN_QOS_ACT; 
	private String PAPN_QOS_CONTROL; 
	private String WLM_QOS_ACT; 
	private String WLM_QOS_CONTROL;
	private String VALORADC_EONNET;
	private String PLAN_DOX;
	private String CODIGO_EFIJO;
	private String DESCRIPCION_COM;
	private int elegidos_fnf;
	private int elegidos_bfnf;
	private Long valor_onneteleg;
	private Long valor_offneteleg;
	private Long valor_fijoeleg;
	private String caract_elegfnf;
	private String caract_elegbfnf;
	private String ffsmsmaxnumele_on;
	private String ffsmsdur;
	private String ffsmsdurtillre;	 
	private String ffsmsdaynextnch;
	private String ffvzmaxnumele_td;
	private String ffvzdur;
	private String ffvzdurtillre;
	private String ffvzdaynextnch;
	private String BFNFVDURTD;
	private String BFNFVDURTILLRE;	
	private String BFNFVDAYNEXTNCH;
	private Long   UPLINK_SPEED;
	private Long   DOWNLINK_SPEED;
	private Long   LTE_QOS_ACT;
	private Long   LTE_QOS_CONTROL;
	private Long   PCRF_CODE;
	private Long   QCI;
	public String getCID() {
		return CID;
	}
	public void setCID(String cID) {
		CID = cID;
	}
	public String getBO_ID() {
		return BO_ID;
	}
	public void setBO_ID(String bO_ID) {
		BO_ID = bO_ID;
	}
	private String CID;
	private String BO_ID;
	
	public Long getQCI() {
		return QCI;
	}
	public void setQCI(Long qCI) {
		QCI = qCI;
	}
	public Long getUPLINK_SPEED() {
		return UPLINK_SPEED;
	}
	public void setUPLINK_SPEED(Long uPLINK_SPEED) {
		UPLINK_SPEED = uPLINK_SPEED;
	}
	public Long getDOWNLINK_SPEED() {
		return DOWNLINK_SPEED;
	}
	public void setDOWNLINK_SPEED(Long dOWNLINK_SPEED) {
		DOWNLINK_SPEED = dOWNLINK_SPEED;
	}
	public Long getLTE_QOS_ACT() {
		return LTE_QOS_ACT;
	}
	public void setLTE_QOS_ACT(Long lTE_QOS_ACT) {
		LTE_QOS_ACT = lTE_QOS_ACT;
	}
	public Long getLTE_QOS_CONTROL() {
		return LTE_QOS_CONTROL;
	}
	public void setLTE_QOS_CONTROL(Long lTE_QOS_CONTROL) {
		LTE_QOS_CONTROL = lTE_QOS_CONTROL;
	}
	public Long getPCRF_CODE() {
		return PCRF_CODE;
	}
	public void setPCRF_CODE(Long pCRF_CODE) {
		PCRF_CODE = pCRF_CODE;
	}
	public String getBFNFVDURTD() {
		return BFNFVDURTD;
	}
	public void setBFNFVDURTD(String bFNFVDURTD) {
		BFNFVDURTD = bFNFVDURTD;
	}
	public String getBFNFVDURTILLRE() {
		return BFNFVDURTILLRE;
	}
	public void setBFNFVDURTILLRE(String bFNFVDURTILLRE) {
		BFNFVDURTILLRE = bFNFVDURTILLRE;
	}
	public String getBFNFVDAYNEXTNCH() {
		return BFNFVDAYNEXTNCH;
	}
	public void setBFNFVDAYNEXTNCH(String bFNFVDAYNEXTNCH) {
		BFNFVDAYNEXTNCH = bFNFVDAYNEXTNCH;
	}
	public String getFfsmsmaxnumele_on() {
		return ffsmsmaxnumele_on;
	}
	public void setFfsmsmaxnumele_on(String ffsmsmaxnumele_on) {
		this.ffsmsmaxnumele_on = ffsmsmaxnumele_on;
	}
	public String getFfsmsdur() {
		return ffsmsdur;
	}
	public void setFfsmsdur(String ffsmsdur) {
		this.ffsmsdur = ffsmsdur;
	}
	public String getFfsmsdurtillre() {
		return ffsmsdurtillre;
	}
	public void setFfsmsdurtillre(String ffsmsdurtillre) {
		this.ffsmsdurtillre = ffsmsdurtillre;
	}
	public String getFfsmsdaynextnch() {
		return ffsmsdaynextnch;
	}
	public void setFfsmsdaynextnch(String ffsmsdaynextnch) {
		this.ffsmsdaynextnch = ffsmsdaynextnch;
	}
	public String getFfvzmaxnumele_td() {
		return ffvzmaxnumele_td;
	}
	public void setFfvzmaxnumele_td(String ffvzmaxnumele_td) {
		this.ffvzmaxnumele_td = ffvzmaxnumele_td;
	}
	public String getFfvzdur() {
		return ffvzdur;
	}
	public void setFfvzdur(String ffvzdur) {
		this.ffvzdur = ffvzdur;
	}
	public String getFfvzdurtillre() {
		return ffvzdurtillre;
	}
	public void setFfvzdurtillre(String ffvzdurtillre) {
		this.ffvzdurtillre = ffvzdurtillre;
	}
	public String getFfvzdaynextnch() {
		return ffvzdaynextnch;
	}
	public void setFfvzdaynextnch(String ffvzdaynextnch) {
		this.ffvzdaynextnch = ffvzdaynextnch;
	}
		
	public int getElegidos_fnf() {
		return elegidos_fnf;
	}
	public void setElegidos_fnf(int elegidos_fnf) {
		this.elegidos_fnf = elegidos_fnf;
	}
	public int getElegidos_bfnf() {
		return elegidos_bfnf;
	}
	public void setElegidos_bfnf(int elegidos_bfnf) {
		this.elegidos_bfnf = elegidos_bfnf;
	}
	public Long getValor_onneteleg() {
		return valor_onneteleg;
	}
	public void setValor_onneteleg(Long valor_onneteleg) {
		this.valor_onneteleg = valor_onneteleg;
	}
	public Long getValor_offneteleg() {
		return valor_offneteleg;
	}
	public void setValor_offneteleg(Long valor_offneteleg) {
		this.valor_offneteleg = valor_offneteleg;
	}
	public Long getValor_fijoeleg() {
		return valor_fijoeleg;
	}
	public void setValor_fijoeleg(Long valor_fijoeleg) {
		this.valor_fijoeleg = valor_fijoeleg;
	}
	public String getCaract_elegfnf() {
		return caract_elegfnf;
	}
	public void setCaract_elegfnf(String caract_elegfnf) {
		this.caract_elegfnf = caract_elegfnf;
	}
	public String getCaract_elegbfnf() {
		return caract_elegbfnf;
	}
	public void setCaract_elegbfnf(String caract_elegbfnf) {
		this.caract_elegbfnf = caract_elegbfnf;
	}
		
	public String getFECHA_VENTA() {
		return FECHA_VENTA;
	}
	public void setFECHA_VENTA(String fECHA_VENTA) {
		FECHA_VENTA = fECHA_VENTA;
	}
	public String getPLAN_DOX() {
		return PLAN_DOX;
	}
	public void setPLAN_DOX(String pLAN_DOX) {
		PLAN_DOX = pLAN_DOX;
	}	
	public String getDESCRIPCION_COM() {
		return DESCRIPCION_COM;
	}
	public void setDESCRIPCION_COM(String dESCRIPCION_COM) {
		DESCRIPCION_COM = dESCRIPCION_COM;
	}
	public String getCODIGO_EFIJO() {
		return CODIGO_EFIJO;
	}
	public void setCODIGO_EFIJO(String cODIGO_EFIJO) {
		CODIGO_EFIJO = cODIGO_EFIJO;
	}
	public String getTIPO_PLAN() {
		return TIPO_PLAN;
	}
	public String getVALORADC_EONNET() {
		return VALORADC_EONNET;
	}
	public void setVALORADC_EONNET(String vALORADC_EONNET) {
		VALORADC_EONNET = vALORADC_EONNET;
	}
	public void setTIPO_PLAN(String tIPO_PLAN) {
		TIPO_PLAN = tIPO_PLAN;
	}
	private String PERCENTAGE_FOR_DOWNGRADE;
	private String TIPO_PLAN;
	
	public String getCODIGO_GPRSBB() {
		return CODIGO_GPRSBB;
	}
	public void setCODIGO_GPRSBB(String cODIGO_GPRSBB) {
		CODIGO_GPRSBB = cODIGO_GPRSBB;
	}
	
    public String getPERCENTAGE_FOR_DOWNGRADE() {
		return PERCENTAGE_FOR_DOWNGRADE;
	}
	public void setPERCENTAGE_FOR_DOWNGRADE(String pERCENTAGE_FOR_DOWNGRADE) {
		PERCENTAGE_FOR_DOWNGRADE = pERCENTAGE_FOR_DOWNGRADE;
	}
	private List<Integer> planesAsocList;
	
	public List<Integer> getPlanesAsocList() {
		return planesAsocList;
	}
	public void setPlanesAsocList(List<Integer> planesAsocList) {
		this.planesAsocList = planesAsocList;
	}
	public String getRC_ACT() {
		return RC_ACT;
	}
	public void setRC_ACT(String rCACT) {
		RC_ACT = rCACT;
	}
	public String getRC_CONTROL() {
		return RC_CONTROL;
	}
	public void setRC_CONTROL(String rCCONTROL) {
		RC_CONTROL = rCCONTROL;
	}
	public String getREQUIRE_IS() {
		return REQUIRE_IS;
	}
	public void setREQUIRE_IS(String rEQUIREIS) {
		REQUIRE_IS = rEQUIREIS;
	}
	public String getDATA_ADD_PACK() {
		return DATA_ADD_PACK;
	}
	public void setDATA_ADD_PACK(String dATAADDPACK) {
		DATA_ADD_PACK = dATAADDPACK;
	}
	public String getPROMO_CATEGORY_PLAN_PENALTY() {
		return PROMO_CATEGORY_PLAN_PENALTY;
	}
	public void setPROMO_CATEGORY_PLAN_PENALTY(String pROMOCATEGORYPLANPENALTY) {
		PROMO_CATEGORY_PLAN_PENALTY = pROMOCATEGORYPLANPENALTY;
	}
	public String getGAMA_OCC() {
		return GAMA_OCC;
	}
	public void setGAMA_OCC(String gAMAOCCIDENTE) {
		GAMA_OCC = gAMAOCCIDENTE;
	}
	public String getGAMA_COS() {
		return GAMA_COS;
	}
	public void setGAMA_COS(String gAMA_COS) {
		GAMA_COS = gAMA_COS;
	}
	public String getGAMA_ORI() {
		return GAMA_ORI;
	}
	public void setGAMA_ORI(String gAMAORIENTE) {
		GAMA_ORI = gAMAORIENTE;
	}
	public String getWEB_QOS_ACT() {
		return WEB_QOS_ACT;
	}
	public void setWEB_QOS_ACT(String wEBQOSACT) {
		WEB_QOS_ACT = wEBQOSACT;
	}
	public String getWEB_QOS_CONTROL() {
		return WEB_QOS_CONTROL;
	}
	public void setWEB_QOS_CONTROL(String wEBQOSCONTROL) {
		WEB_QOS_CONTROL = wEBQOSCONTROL;
	}
	public String getWAP_QOS_ACT() {
		return WAP_QOS_ACT;
	}
	public void setWAP_QOS_ACT(String wAPQOSACT) {
		WAP_QOS_ACT = wAPQOSACT;
	}
	public String getWAP_QOS_CONTROL() {
		return WAP_QOS_CONTROL;
	}
	public void setWAP_QOS_CONTROL(String wAPQOSCONTROL) {
		WAP_QOS_CONTROL = wAPQOSCONTROL;
	}
	public String getBB_QOS_ACT() {
		return BB_QOS_ACT;
	}
	public void setBB_QOS_ACT(String bBQOSACT) {
		BB_QOS_ACT = bBQOSACT;
	}
	public String getBB_QOS_CONTROL() {
		return BB_QOS_CONTROL;
	}
	public void setBB_QOS_CONTROL(String bBQOSCONTROL) {
		BB_QOS_CONTROL = bBQOSCONTROL;
	}
	public String getMMS_QOS_ACT() {
		return MMS_QOS_ACT;
	}
	public void setMMS_QOS_ACT(String mMSQOSACT) {
		MMS_QOS_ACT = mMSQOSACT;
	}
	public String getMMS_QOS_CONTROL() {
		return MMS_QOS_CONTROL;
	}
	public void setMMS_QOS_CONTROL(String mMSQOSCONTROL) {
		MMS_QOS_CONTROL = mMSQOSCONTROL;
	}
	public String getIAPN_QOS_ACT() {
		return IAPN_QOS_ACT;
	}
	public void setIAPN_QOS_ACT(String iAPNQOSACT) {
		IAPN_QOS_ACT = iAPNQOSACT;
	}
	public String getIAPN_QOS_CONTROL() {
		return IAPN_QOS_CONTROL;
	}
	public void setIAPN_QOS_CONTROL(String iAPNQOSCONTROL) {
		IAPN_QOS_CONTROL = iAPNQOSCONTROL;
	}
	public String getVVM_QOS_ACT() {
		return VVM_QOS_ACT;
	}
	public void setVVM_QOS_ACT(String vVMQOSACT) {
		VVM_QOS_ACT = vVMQOSACT;
	}
	public String getVVM_QOS_CONTROL() {
		return VVM_QOS_CONTROL;
	}
	public void setVVM_QOS_CONTROL(String vVMQOSCONTROL) {
		VVM_QOS_CONTROL = vVMQOSCONTROL;
	}
	public String getNML_QOS_ACT() {
		return NML_QOS_ACT;
	}
	public void setNML_QOS_ACT(String nMLQOSACT) {
		NML_QOS_ACT = nMLQOSACT;
	}
	public String getNML_QOS_CONTROL() {
		return NML_QOS_CONTROL;
	}
	public void setNML_QOS_CONTROL(String nMLQOSCONTROL) {
		NML_QOS_CONTROL = nMLQOSCONTROL;
	}
	public String getPAPN_QOS_ACT() {
		return PAPN_QOS_ACT;
	}
	public void setPAPN_QOS_ACT(String pAPNQOSACT) {
		PAPN_QOS_ACT = pAPNQOSACT;
	}
	public String getPAPN_QOS_CONTROL() {
		return PAPN_QOS_CONTROL;
	}
	public void setPAPN_QOS_CONTROL(String pAPNQOSCONTROL) {
		PAPN_QOS_CONTROL = pAPNQOSCONTROL;
	}
	public String getWLM_QOS_ACT() {
		return WLM_QOS_ACT;
	}
	public void setWLM_QOS_ACT(String wLMQOSACT) {
		WLM_QOS_ACT = wLMQOSACT;
	}
	public String getWLM_QOS_CONTROL() {
		return WLM_QOS_CONTROL;
	}
	public void setWLM_QOS_CONTROL(String wLMQOSCONTROL) {
		WLM_QOS_CONTROL = wLMQOSCONTROL;
	}
	public String getMIGRACION() {
		return MIGRACION;
	}
	public void setMIGRACION(String mIGRACION) {
		MIGRACION = mIGRACION;
	}
	public String getTIPO_ALLOWANCE_PROMO() {
		return TIPO_ALLOWANCE_PROMO;
	}
	public void setTIPO_ALLOWANCE_PROMO(String tIPOALLOWANCEPROMO) {
		TIPO_ALLOWANCE_PROMO = tIPOALLOWANCEPROMO;
	}
	public Integer getMESES_PROMO() {
		return MESES_PROMO;
	}
	public void setMESES_PROMO(Integer mESESPROMO) {
		MESES_PROMO = mESESPROMO;
	}
	public Integer getPROMO_INTERCALACION() {
		return PROMO_INTERCALACION;
	}
	public void setPROMO_INTERCALACION(Integer pROMOINTERCALACION) {
		PROMO_INTERCALACION = pROMOINTERCALACION;
	}
	public String getDESCRIPCION1() {
		return DESCRIPCION1;
	}
	public void setDESCRIPCION1(String dESCRIPCION1) {
		DESCRIPCION1 = dESCRIPCION1;
	}
	public String getDESCRIPCION2() {
		return DESCRIPCION2;
	}
	public void setDESCRIPCION2(String dESCRIPCION2) {
		DESCRIPCION2 = dESCRIPCION2;
	}
	public String getDESCRIPCION3() {
		return DESCRIPCION3;
	}
	public void setDESCRIPCION3(String dESCRIPCION3) {
		DESCRIPCION3 = dESCRIPCION3;
	}
	public String getDESCRIPCION4() {
		return DESCRIPCION4;
	}
	public void setDESCRIPCION4(String dESCRIPCION4) {
		DESCRIPCION4 = dESCRIPCION4;
	}
	public String getDESCRIPCION5() {
		return DESCRIPCION5;
	}
	public void setDESCRIPCION5(String dESCRIPCION5) {
		DESCRIPCION5 = dESCRIPCION5;
	}
	public String getDESCRIPCION6() {
		return DESCRIPCION6;
	}
	public void setDESCRIPCION6(String dESCRIPCION6) {
		DESCRIPCION6 = dESCRIPCION6;
	}
	public String getDESCRIPCION7() {
		return DESCRIPCION7;
	}
	public void setDESCRIPCION7(String dESCRIPCION7) {
		DESCRIPCION7 = dESCRIPCION7;
	}
	public String getOBSCOMERCIALES() {
		return OBSCOMERCIALES;
	}
	public void setOBSCOMERCIALES(String oBSCOMERCIALES) {
		OBSCOMERCIALES = oBSCOMERCIALES;
	}
	public String getTIPOPLAN() {
		return TIPOPLAN;
	}
	public void setTIPOPLAN(String tIPOPLAN) {
		TIPOPLAN = tIPOPLAN;
	}
	public String getSEGMENTO() {
		return SEGMENTO;
	}
	public void setSEGMENTO(String sEGMENTO) {
		SEGMENTO = sEGMENTO;
	}
	public String getRETARIFICA() {
		return RETARIFICA;
	}
	public void setRETARIFICA(String rETARIFICA) {
		RETARIFICA = rETARIFICA;
	}
	public String getFLIARETARIF() {
		return FLIARETARIF;
	}
	public void setFLIARETARIF(String fLIARETARIF) {
		FLIARETARIF = fLIARETARIF;
	}
	public Long getUSUARIOS_ACTIVOS() {
		return USUARIOS_ACTIVOS;
	}
	public void setUSUARIOS_ACTIVOS(Long uSUARIOS_ACTIVOS) {
		USUARIOS_ACTIVOS = uSUARIOS_ACTIVOS;
	}
	public String getDESACTIVACION() {
		return DESACTIVACION;
	}
	public void setDESACTIVACION(String dESACTIVACION) {
		DESACTIVACION = dESACTIVACION;
	}
	public String getOBSDESACTIVACION() {
		return OBSDESACTIVACION;
	}
	public void setOBSDESACTIVACION(String oBSDESACTIVACION) {
		OBSDESACTIVACION = oBSDESACTIVACION;
	}
	public String getFECHA_INICIO() {
		return FECHA_INICIO;
	}
	public void setFECHA_INICIO(String fECHAINICIO) {
		FECHA_INICIO = fECHAINICIO;
	}
	public String getPLANPREPAGO() {
		return PLANPREPAGO;
	}
	public void setPLANPREPAGO(String pLANPREPAGO) {
		PLANPREPAGO = pLANPREPAGO;
	}
	public Long getOPMX() {
		return OPMX;
	}
	public void setOPMX(Long oPMX) {
		OPMX = oPMX;
	}
	public Long getTMCODE() {
		return TMCODE;
	}
	public void setTMCODE(Long tMCODE) {
		TMCODE = tMCODE;
	}
	public String getPLAN() {
		return PLAN;
	}
	public void setPLAN(String pLAN) {
		PLAN = pLAN;
	}
	public Long getSPCODE() {
		return SPCODE;
	}
	public void setSPCODE(Long sPCODE) {
		SPCODE = sPCODE;
	}
	public String getPAQUETE() {
		return PAQUETE;
	}
	public void setPAQUETE(String pAQUETE) {
		PAQUETE = pAQUETE;
	}
	public Double getCFM() {
		return CFM;
	}
	public void setCFM(Double cFM) {
		CFM = cFM;
	}
	public String getDES12X12() {
		return DES12X12;
	}
	public void setDES12X12(String dES12X12) {
		DES12X12 = dES12X12;
	}
	public String getIVA() {
		return IVA;
	}
	public void setIVA(String iVA) {
		IVA = iVA;
	}
	public Long getRICODE() {
		return RICODE;
	}
	public void setRICODE(Long rICODE) {
		RICODE = rICODE;
	}
	public String getPROMOCION() {
		return PROMOCION;
	}
	public void setPROMOCION(String pROMOCION) {
		PROMOCION = pROMOCION;
	}
	public String getINH() {
		return INH;
	}
	public void setINH(String iNH) {
		INH = iNH;
	}
	public Long getTMCODE_EQ() {
		return TMCODE_EQ;
	}
	public void setTMCODE_EQ(Long tMCODEEQ) {
		TMCODE_EQ = tMCODEEQ;
	}
	public Long getTMCODE_BASE_BUNDLE() {
		return TMCODE_BASE_BUNDLE;
	}
	public void setTMCODE_BASE_BUNDLE(Long tMCODEBASEBUNDLE) {
		TMCODE_BASE_BUNDLE = tMCODEBASEBUNDLE;
	}
	public Long getCFM_BASE_BUNDLE() {
		return CFM_BASE_BUNDLE;
	}
	public void setCFM_BASE_BUNDLE(Long cFMBASEBUNDLE) {
		CFM_BASE_BUNDLE = cFMBASEBUNDLE;
	}
	public String getTIPOBUZON() {
		return TIPOBUZON;
	}
	public void setTIPOBUZON(String tIPOBUZON) {
		TIPOBUZON = tIPOBUZON;
	}
	public String getROLLOVER() {
		return ROLLOVER;
	}
	public void setROLLOVER(String rOLLOVER) {
		ROLLOVER = rOLLOVER;
	}
	public Long getALLW_ONNET() {
		return ALLW_ONNET;
	}
	public void setALLW_ONNET(Long aLLWONNET) {
		ALLW_ONNET = aLLWONNET;
	}
	public Long getALLW_FIJOS() {
		return ALLW_FIJOS;
	}
	public void setALLW_FIJOS(Long aLLWFIJOS) {
		ALLW_FIJOS = aLLWFIJOS;
	}
	public Long getALLW_OFFNET() {
		return ALLW_OFFNET;
	}
	public void setALLW_OFFNET(Long aLLWOFFNET) {
		ALLW_OFFNET = aLLWOFFNET;
	}
	public Long getALLW_TDESTINO() {
		return ALLW_TDESTINO;
	}
	public void setALLW_TDESTINO(Long aLLWTDESTINO) {
		ALLW_TDESTINO = aLLWTDESTINO;
	}
	public Long getALLW_ELEGONNET() {
		return ALLW_ELEGONNET;
	}
	public void setALLW_ELEGONNET(Long aLLWELEGONNET) {
		ALLW_ELEGONNET = aLLWELEGONNET;
	}
	public Long getALLW_ELEGONNET1() {
		return ALLW_ELEGONNET1;
	}
	public void setALLW_ELEGONNET1(Long aLLWELEGONNET1) {
		ALLW_ELEGONNET1 = aLLWELEGONNET1;
	}
	public Long getALLW_ELEGNIT() {
		return ALLW_ELEGNIT;
	}
	public void setALLW_ELEGNIT(Long aLLWELEGNIT) {
		ALLW_ELEGNIT = aLLWELEGNIT;
	}
	public Long getALLW_LDI() {
		return ALLW_LDI;
	}
	public void setALLW_LDI(Long aLLWLDI) {
		ALLW_LDI = aLLWLDI;
	}
	public String getCARACT_PLAN() {
		return CARACT_PLAN;
	}
	public void setCARACT_PLAN(String cARACTPLAN) {
		CARACT_PLAN = cARACTPLAN;
	}
	public String getCARACT_ELEG() {
		return CARACT_ELEG;
	}
	public void setCARACT_ELEG(String cARACTELEG) {
		CARACT_ELEG = cARACTELEG;
	}
	public String getCODIGO_ELEG() {
		return CODIGO_ELEG;
	}
	public void setCODIGO_ELEG(String cODIGOELEG) {
		CODIGO_ELEG = cODIGOELEG;
	}
	public String getCARACT_ELEG_OLD() {
		return CARACT_ELEG_OLD;
	}
	public void setCARACT_ELEG_OLD(String cARACTELEGOLD) {
		CARACT_ELEG_OLD = cARACTELEGOLD;
	}
	public Long getALLW_PROMO() {
		return ALLW_PROMO;
	}
	public void setALLW_PROMO(Long aLLW_PROMO) {
		ALLW_PROMO = aLLW_PROMO;
	}
	public String getHORARIO_PROMO() {
		return HORARIO_PROMO;
	}
	public void setHORARIO_PROMO(String hORARIOPROMO) {
		HORARIO_PROMO = hORARIOPROMO;
	}
	public Long getALLW_SMS() {
		return ALLW_SMS;
	}
	public void setALLW_SMS(Long aLLWSMS) {
		ALLW_SMS = aLLWSMS;
	}
	public String getCODIGO_SMS() {
		return CODIGO_SMS;
	}
	public void setCODIGO_SMS(String cODIGOSMS) {
		CODIGO_SMS = cODIGOSMS;
	}
	public Long getALLW_MMS() {
		return ALLW_MMS;
	}
	public void setALLW_MMS(Long aLLWMMS) {
		ALLW_MMS = aLLWMMS;
	}
	public String getCODIGO_MMS() {
		return CODIGO_MMS;
	}
	public void setCODIGO_MMS(String cODIGOMMS) {
		CODIGO_MMS = cODIGOMMS;
	}
	public String getALLW_GPRS() {
		return ALLW_GPRS;
	}
	public void setALLW_GPRS(String aLLWGPRS) {
		ALLW_GPRS = aLLWGPRS;
	}
	public String getCODIGO_GPRS() {
		return CODIGO_GPRS;
	}
	public void setCODIGO_GPRS(String cODIGOGPRS) {
		CODIGO_GPRS = cODIGOGPRS;
	}
	
	public String getALLW_BLACKBERRY() {
		return ALLW_BLACKBERRY;
	}
	public void setALLW_BLACKBERRY(String aLLWBLACKBERRY) {
		ALLW_BLACKBERRY = aLLWBLACKBERRY;
	}
	public String getCODIGO_BB() {
		return CODIGO_BB;
	}
	public void setCODIGO_BB(String cODIGOBB) {
		CODIGO_BB = cODIGOBB;
	}
	public String getVALORADC_ONNET() {
		return VALORADC_ONNET;
	}
	public void setVALORADC_ONNET(String vALORADC_ONNET) {
		VALORADC_ONNET = vALORADC_ONNET;
	}
	public String getVALORADC_FIJO() {
		return VALORADC_FIJO;
	}
	public void setVALORADC_FIJO(String vALORADC_FIJO) {
		VALORADC_FIJO = vALORADC_FIJO;
	}
	public String getVALORADC_OFFNET() {
		return VALORADC_OFFNET;
	}
	public void setVALORADC_OFFNET(String vALORADC_OFFNET) {
		VALORADC_OFFNET = vALORADC_OFFNET;
	}
	public String getVALORADC_EFIJO() {
		return VALORADC_EFIJO;
	}
	public void setVALORADC_EFIJO(String vALORADC_EFIJO) {
		VALORADC_EFIJO = vALORADC_EFIJO;
	}
	public String getVALORADC_ENIT() {
		return VALORADC_ENIT;
	}
	public void setVALORADC_ENIT(String vALORADC_ENIT) {
		VALORADC_ENIT = vALORADC_ENIT;
	}
	public String getVALORADC_SMS() {
		return VALORADC_SMS;
	}
	public void setVALORADC_SMS(String vALORADC_SMS) {
		VALORADC_SMS = vALORADC_SMS;
	}
	public String getVLR_SMSOFFNET() {
		return VLR_SMSOFFNET;
	}
	public void setVLR_SMSOFFNET(String vLRSMSOFFNET) {
		VLR_SMSOFFNET = vLRSMSOFFNET;
	}
	public String getVLR_MMS() {
		return VLR_MMS;
	}
	public void setVLR_MMS(String vLRMMS) {
		VLR_MMS = vLRMMS;
	}
	public String getVALORADC_GPRS() {
		return VALORADC_GPRS;
	}
	public void setVALORADC_GPRS(String vALORADC_GPRS) {
		VALORADC_GPRS = vALORADC_GPRS;
	}
	public String getVALORADC_BB() {
		return VALORADC_BB;
	}
	public void setVALORADC_BB(String vALORADC_BB) {
		VALORADC_BB = vALORADC_BB;
	}
	public String getUSO_JUSTO() {
		return USO_JUSTO;
	}
	public void setUSO_JUSTO(String uSOJUSTO) {
		USO_JUSTO = uSOJUSTO;
	}
	public String getTIPO_BLACKBERRY() {
		return TIPO_BLACKBERRY;
	}
	public void setTIPO_BLACKBERRY(String tIPOBLACKBERRY) {
		TIPO_BLACKBERRY = tIPOBLACKBERRY;
	}
	public String getNOMBRE_APROV() {
		return NOMBRE_APROV;
	}
	public void setNOMBRE_APROV(String nOMBREAPROV) {
		NOMBRE_APROV = nOMBREAPROV;
	}
	public String getTIPO_APROV() {
		return TIPO_APROV;
	}
	public void setTIPO_APROV(String tIPOAPROV) {
		TIPO_APROV = tIPOAPROV;
	}
	public Long getQA_SERVICIO() {
		return QA_SERVICIO;
	}
	public void setQA_SERVICIO(Long qASERVICIO) {
		QA_SERVICIO = qASERVICIO;
	}
	public String getDPI_INICIAL() {
		return DPI_INICIAL;
	}
	public void setDPI_INICIAL(String dPIINICIAL) {
		DPI_INICIAL = dPIINICIAL;
	}
	public String getDPI_CONTROL() {
		return DPI_CONTROL;
	}
	public void setDPI_CONTROL(String dPICONTROL) {
		DPI_CONTROL = dPICONTROL;
	}
	public Double getVALORINC_ONNET() {
		return VALORINC_ONNET;
	}
	public void setVALORINC_ONNET(Double rIHCOMCEL) {
		VALORINC_ONNET = rIHCOMCEL;
	}
	public Double getVALORINC_FIJO() {
		return VALORINC_FIJO;
	}
	public void setVALORINC_FIJO(Double rIHFIJOS) {
		VALORINC_FIJO = rIHFIJOS;
	}
	public Double getVALORINC_OFFNET() {
		return VALORINC_OFFNET;
	}
	public void setVALORINC_OFFNET(Double rIHOTROSOP) {
		VALORINC_OFFNET = rIHOTROSOP;
	}
	public String getCOD_DOC1() {
		return COD_DOC1;
	}
	public void setCOD_DOC1(String cODDOC1) {
		COD_DOC1 = cODDOC1;
	}
	public String getCAMPO() {
		return CAMPO;
	}
	public void setCAMPO(String cAMPO) {
		CAMPO = cAMPO;
	}
	public String getDESCRIPCION8() {
		return DESCRIPCION8;
	}
	public void setDESCRIPCION8(String dESCRIPCION8) {
		DESCRIPCION8 = dESCRIPCION8;
	}
	public Long getALLW_EFIJO() {
		return ALLW_EFIJO;
	}
	public void setALLW_EFIJO(Long aLLW_EFIJO) {
		ALLW_EFIJO = aLLW_EFIJO;
	}
	public String getEFFECTIVE_DATE() {
		return EFFECTIVE_DATE;
	}
	public void setEFFECTIVE_DATE(String eFFECTIVE_DATE) {
		EFFECTIVE_DATE = eFFECTIVE_DATE;
	}
}
