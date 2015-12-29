package co.com.comcel.vo;

import java.util.ArrayList;
import java.util.List;

public class BillingOffer {
	
	private int billOfferGenerics;
	private int tipoBolsa;
	private String name;
	private String effectiveDate;
	private String code;
	private String level;
	private String type;
	private String currency;
	private String saleEffectiveDate;
	private String saleExpirationdate;
	private String durationA;
	private Integer durationB;
	private String deployIndicator;
	private String descriptionA;
	private String descriptionB;
	private String productType;
	private String descripcion;
	private Plan plan;
	private Paquete paquete;
	private PaqueteLDI paqueteLDI;
	private List<Price> priceList;
	private List<OfertasDemanda> ofertasDemanda;
	private Long tmcode;
	private int contLink;
	private String propertiesSetName;
	private String DPI_ACT;
	private String DPI_Control;
	private String RC_ACT;
	private String RC_Control;
	private String BlackberryServiceCode;
	private String RequireInternetService;
	private String DataAdditionalPackage;
	private String PromotionCategoryforplanpenalty;
	private String GAMA_OCC;
	private String GAMA_COS;
	private String GAMA_ORI;
	private String WEB_QOS_ACT;
	private String WEB_QOS_Control;
	private String WAP_QOS_ACT;
	private String WAP_QOS_Control;
	private String BB_QOS_ACT;
	private String BB_QOS_Control;	
	private String MMS_QOS_ACT;
	private String MMS_QOS_Control;	
	private String IAPN_QOS_ACT;
	private String _IAPN_QOS_Control;	
	private String VVM_QOS_ACT;
	private String VVM_QOS_Control;	
	private String NML_QOS_ACT;
	private String NML_QOS_Control;	
	private String PAPN_QOS_ACT;
	private String PAPN_QOS_Control;	
	private String WLM_QOS_ACT;
	private String WLM_QOS_Control;	
	private String INALIAS_Prepaid;
	private String INPERFILPAGO_Prepaid;	
	/**
	 * Se adiciona atributo, para almacenar información
	 * dependiendo del PIT, para el excel de PO.
	 * Se almacena informacion de atributo: Atribute mapping
	 * para los pits:
	 * 				**Allowance duration recurring per period and call type with relative quota
	 				**Event discount per period for FnF
	 				**Allowance occurrence recurring per period for FnF
	 				**Allowance duration recurring per period for FNF
	 				**Allowance duration recurring per period for FNF- X first min free for a call
	 */
	private String ATRMAP_TOPO;	
	private Integer plan_basico = null;	
	private boolean demanda = false;
	private String BFNF_Pre2Post_Ind;
	private String BFNF_Voice_Serv_dur;
	private String tipo_oferta;
	private String BFNF_Max_Num_Friends;
	private String BFNF_Vdur_till_renew;
	private String BFNF_VDays_next_numchange;
	private String FNFV_MAXNUM;
	private String FNFV_VDURTILLRENEW_;
	private String FNFV_VDAYSNEXTNUMCHAN;
	private String FNFV_VOICESERVDUR;
	private String FNFVSMS_MAXNUM;
	private String FNFVSMS_VDURTILLRENEW;
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	private String FNFVSMS_VDAYSNEXTNUMCHAN;
	private String tipo;
	public String getSaleExpirationdate() {
		return saleExpirationdate;
	}
	public void setSaleExpirationdate(String saleExpirationdate) {
		this.saleExpirationdate = saleExpirationdate;
	}
	public String getFNFV_MAXNUM() {
		return FNFV_MAXNUM;
	}
	public void setFNFV_MAXNUM(String fNFV_MAXNUM) {
		FNFV_MAXNUM = fNFV_MAXNUM;
	}
	public String getFNFV_VDURTILLRENEW_() {
		return FNFV_VDURTILLRENEW_;
	}
	public void setFNFV_VDURTILLRENEW_(String fNFV_VDURTILLRENEW_) {
		FNFV_VDURTILLRENEW_ = fNFV_VDURTILLRENEW_;
	}
	public String getFNFV_VDAYSNEXTNUMCHAN() {
		return FNFV_VDAYSNEXTNUMCHAN;
	}
	public void setFNFV_VDAYSNEXTNUMCHAN(String fNFV_VDAYSNEXTNUMCHAN) {
		FNFV_VDAYSNEXTNUMCHAN = fNFV_VDAYSNEXTNUMCHAN;
	}
	public String getFNFV_VOICESERVDUR() {
		return FNFV_VOICESERVDUR;
	}
	public void setFNFV_VOICESERVDUR(String fNFV_VOICESERVDUR) {
		FNFV_VOICESERVDUR = fNFV_VOICESERVDUR;
	}
	public String getFNFVSMS_MAXNUM() {
		return FNFVSMS_MAXNUM;
	}
	public void setFNFVSMS_MAXNUM(String fNFVSMS_MAXNUM) {
		FNFVSMS_MAXNUM = fNFVSMS_MAXNUM;
	}
	public String getFNFVSMS_VDURTILLRENEW() {
		return FNFVSMS_VDURTILLRENEW;
	}
	public void setFNFVSMS_VDURTILLRENEW(String fNFVSMS_VDURTILLRENEW) {
		FNFVSMS_VDURTILLRENEW = fNFVSMS_VDURTILLRENEW;
	}
	public String getFNFVSMS_VDAYSNEXTNUMCHAN() {
		return FNFVSMS_VDAYSNEXTNUMCHAN;
	}
	public void setFNFVSMS_VDAYSNEXTNUMCHAN(String fNFVSMS_VDAYSNEXTNUMCHAN) {
		FNFVSMS_VDAYSNEXTNUMCHAN = fNFVSMS_VDAYSNEXTNUMCHAN;
	}
	public String getFNFVSMS_VOICESERVDUR() {
		return FNFVSMS_VOICESERVDUR;
	}
	public void setFNFVSMS_VOICESERVDUR(String fNFVSMS_VOICESERVDUR) {
		FNFVSMS_VOICESERVDUR = fNFVSMS_VOICESERVDUR;
	}


	private String FNFVSMS_VOICESERVDUR;	
	
	public String getBFNF_Pre2Post_Ind() {
		return BFNF_Pre2Post_Ind;
	}
	public String getTipo_oferta() {
		return tipo_oferta;
	}
	public void setTipo_oferta(String tipo_oferta) {
		this.tipo_oferta = tipo_oferta;
	}
	public void setBFNF_Pre2Post_Ind(String bFNF_Pre2Post_Ind) {
		BFNF_Pre2Post_Ind = bFNF_Pre2Post_Ind;
	}


	public String getBFNF_Voice_Serv_dur() {
		return BFNF_Voice_Serv_dur;
	}


	public void setBFNF_Voice_Serv_dur(String bFNF_Voice_Serv_dur) {
		BFNF_Voice_Serv_dur = bFNF_Voice_Serv_dur;
	}


	public String getBFNF_Max_Num_Friends() {
		return BFNF_Max_Num_Friends;
	}


	public void setBFNF_Max_Num_Friends(String bFNF_Max_Num_Friends) {
		BFNF_Max_Num_Friends = bFNF_Max_Num_Friends;
	}

	public String getBFNF_Vdur_till_renew() {
		return BFNF_Vdur_till_renew;
	}

	public void setBFNF_Vdur_till_renew(String bFNF_Vdur_till_renew) {
		BFNF_Vdur_till_renew = bFNF_Vdur_till_renew;
	}
	public String getBFNF_VDays_next_numchange() {
		return BFNF_VDays_next_numchange;
	}

	public void setBFNF_VDays_next_numchange(String bFNF_VDays_next_numchange) {
		BFNF_VDays_next_numchange = bFNF_VDays_next_numchange;
	}
		
	public BillingOffer(){
		this.plan = new Plan();
		this.priceList = new ArrayList<Price>();
		this.demanda = false;
	}	
	
	public PaqueteLDI getPaqueteLDI() {
		return paqueteLDI;
	}
	public void setPaqueteLDI(PaqueteLDI paqueteLDI) {
		this.paqueteLDI = paqueteLDI;
	}
	public int getContLink() {
		return contLink;
	}
	public void setContLink(int contLink) {
		this.contLink = contLink;
	}
	public String getPropertiesSetName() {
		return propertiesSetName;
	}
	public void setPropertiesSetName(String propertiesSetName) {
		this.propertiesSetName = propertiesSetName;
	}
	public String getDPI_ACT() {
		return DPI_ACT;
	}
	public void setDPI_ACT(String dPIACT) {
		DPI_ACT = dPIACT;
	}
	public String getDPI_Control() {
		return DPI_Control;
	}
	public void setDPI_Control(String dPIControl) {
		DPI_Control = dPIControl;
	}
	public String getRC_ACT() {
		return RC_ACT;
	}
	public void setRC_ACT(String rCACT) {
		RC_ACT = rCACT;
	}
	public String getRC_Control() {
		return RC_Control;
	}
	public void setRC_Control(String rCControl) {
		RC_Control = rCControl;
	}
	public String getBlackberryServiceCode() {
		return BlackberryServiceCode;
	}
	public void setBlackberryServiceCode(String blackberryServiceCode) {
		BlackberryServiceCode = blackberryServiceCode;
	}
	public String getRequireInternetService() {
		return RequireInternetService;
	}
	public void setRequireInternetService(String requireInternetService) {
		RequireInternetService = requireInternetService;
	}
	public String getDataAdditionalPackage() {
		return DataAdditionalPackage;
	}
	public void setDataAdditionalPackage(String dataAdditionalPackage) {
		DataAdditionalPackage = dataAdditionalPackage;
	}
	public String getPromotionCategoryforplanpenalty() {
		return PromotionCategoryforplanpenalty;
	}
	public void setPromotionCategoryforplanpenalty(
			String promotionCategoryforplanpenalty) {
		PromotionCategoryforplanpenalty = promotionCategoryforplanpenalty;
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
	public void setGAMA_COS(String gAMACOSTA) {
		GAMA_COS = gAMACOSTA;
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
	public String getWEB_QOS_Control() {
		return WEB_QOS_Control;
	}
	public void setWEB_QOS_Control(String wEBQOSControl) {
		WEB_QOS_Control = wEBQOSControl;
	}
	public String getWAP_QOS_ACT() {
		return WAP_QOS_ACT;
	}
	public void setWAP_QOS_ACT(String wAPQOSACT) {
		WAP_QOS_ACT = wAPQOSACT;
	}
	public String getWAP_QOS_Control() {
		return WAP_QOS_Control;
	}
	public void setWAP_QOS_Control(String wAPQOSControl) {
		WAP_QOS_Control = wAPQOSControl;
	}
	public String getBB_QOS_ACT() {
		return BB_QOS_ACT;
	}
	public void setBB_QOS_ACT(String bBQOSACT) {
		BB_QOS_ACT = bBQOSACT;
	}
	public String getBB_QOS_Control() {
		return BB_QOS_Control;
	}
	public void setBB_QOS_Control(String bBQOSControl) {
		BB_QOS_Control = bBQOSControl;
	}
	public String getMMS_QOS_ACT() {
		return MMS_QOS_ACT;
	}
	public void setMMS_QOS_ACT(String mMSQOSACT) {
		MMS_QOS_ACT = mMSQOSACT;
	}
	public String getMMS_QOS_Control() {
		return MMS_QOS_Control;
	}
	public void setMMS_QOS_Control(String mMSQOSControl) {
		MMS_QOS_Control = mMSQOSControl;
	}
	public String getIAPN_QOS_ACT() {
		return IAPN_QOS_ACT;
	}
	public void setIAPN_QOS_ACT(String iAPNQOSACT) {
		IAPN_QOS_ACT = iAPNQOSACT;
	}
	public String get_IAPN_QOS_Control() {
		return _IAPN_QOS_Control;
	}
	public void set_IAPN_QOS_Control(String iapnQOSControl) {
		_IAPN_QOS_Control = iapnQOSControl;
	}
	public String getVVM_QOS_ACT() {
		return VVM_QOS_ACT;
	}
	public void setVVM_QOS_ACT(String vVMQOSACT) {
		VVM_QOS_ACT = vVMQOSACT;
	}
	public String getVVM_QOS_Control() {
		return VVM_QOS_Control;
	}
	public void setVVM_QOS_Control(String vVMQOSControl) {
		VVM_QOS_Control = vVMQOSControl;
	}
	public String getNML_QOS_ACT() {
		return NML_QOS_ACT;
	}
	public void setNML_QOS_ACT(String nMLQOSACT) {
		NML_QOS_ACT = nMLQOSACT;
	}
	public String getNML_QOS_Control() {
		return NML_QOS_Control;
	}
	public void setNML_QOS_Control(String nMLQOSControl) {
		NML_QOS_Control = nMLQOSControl;
	}
	public String getPAPN_QOS_ACT() {
		return PAPN_QOS_ACT;
	}
	public void setPAPN_QOS_ACT(String pAPNQOSACT) {
		PAPN_QOS_ACT = pAPNQOSACT;
	}
	public String getPAPN_QOS_Control() {
		return PAPN_QOS_Control;
	}
	public void setPAPN_QOS_Control(String pAPNQOSControl) {
		PAPN_QOS_Control = pAPNQOSControl;
	}
	public String getWLM_QOS_ACT() {
		return WLM_QOS_ACT;
	}
	public void setWLM_QOS_ACT(String wLMQOSACT) {
		WLM_QOS_ACT = wLMQOSACT;
	}
	public String getWLM_QOS_Control() {
		return WLM_QOS_Control;
	}
	public void setWLM_QOS_Control(String wLMQOSControl) {
		WLM_QOS_Control = wLMQOSControl;
	}
	public String getINALIAS_Prepaid() {
		return INALIAS_Prepaid;
	}
	public void setINALIAS_Prepaid(String iNALIASPrepaid) {
		INALIAS_Prepaid = iNALIASPrepaid;
	}
	public String getINPERFILPAGO_Prepaid() {
		return INPERFILPAGO_Prepaid;
	}
	public void setINPERFILPAGO_Prepaid(String iNPERFILPAGOPrepaid) {
		INPERFILPAGO_Prepaid = iNPERFILPAGOPrepaid;
	}
	public int getBillOfferGenerics() {
		return billOfferGenerics;
	}
	public void setBillOfferGenerics(int billOfferGenerics) {
		this.billOfferGenerics = billOfferGenerics;
	}
	public Paquete getPaquete() {
		return paquete;
	}
	public void setPaquete(Paquete paquete) {
		this.paquete = paquete;
	}
	public int getTipoBolsa() {
		return tipoBolsa;
	}
	public void setTipoBolsa(int tipoBolsa) {
		this.tipoBolsa = tipoBolsa;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	public Long getTmcode() {
		return tmcode;
	}
	public void setTmcode(Long tmcode) {
		this.tmcode = tmcode;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDurationA() {
		return durationA;
	}
	public void setDurationA(String durationA) {
		this.durationA = durationA;
	}
	public Integer getDurationB() {
		return durationB;
	}
	public void setDurationB(Integer durationB) {
		this.durationB = durationB;
	}
	public String getDeployIndicator() {
		return deployIndicator;
	}
	public void setDeployIndicator(String deployIndicator) {
		this.deployIndicator = deployIndicator;
	}
	public String getDescriptionA() {
		return descriptionA;
	}
	public void setDescriptionA(String descriptionA) {
		this.descriptionA = descriptionA;
	}
	public String getDescriptionB() {
		return descriptionB;
	}
	public void setDescriptionB(String descriptionB) {
		this.descriptionB = descriptionB;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public List<Price> getPriceList() {
		return priceList;
	}
	public void setPriceList(List<Price> priceList) {
		this.priceList = priceList;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getSaleEffectiveDate() {
		return saleEffectiveDate;
	}
	public void setSaleEffectiveDate(String saleEffectiveDate) {
		this.saleEffectiveDate = saleEffectiveDate;
	}


	/**
	 * @return the plan_basico
	 */
	public Integer getPlan_basico() {
		return plan_basico;
	}


	/**
	 * @param plan_basico the plan_basico to set
	 */
	public void setPlan_basico(Integer plan_basico) {
		this.plan_basico = plan_basico;
	}


	/**
	 * @return the aTRMAP_TOPO
	 */
	public String getATRMAP_TOPO() {
		return ATRMAP_TOPO;
	}


	/**
	 * @param aTRMAP_TOPO the aTRMAP_TOPO to set
	 */
	public void setATRMAP_TOPO(String aTRMAP_TOPO) {
		ATRMAP_TOPO = aTRMAP_TOPO;
	}


	/**
	 * @return the ofertasDemanda
	 */
	public List<OfertasDemanda> getOfertasDemanda() {
		return ofertasDemanda;
	}


	/**
	 * @param ofertasDemanda the ofertasDemanda to set
	 */
	public void setOfertasDemanda(List<OfertasDemanda> ofertasDemanda) {
		this.ofertasDemanda = ofertasDemanda;
	}


	/**
	 * @return the demanda
	 */
	public boolean isDemanda() {
		return demanda;
	}


	/**
	 * @param demanda the demanda to set
	 */
	public void setDemanda(boolean demanda) {
		this.demanda = demanda;
	}



}
