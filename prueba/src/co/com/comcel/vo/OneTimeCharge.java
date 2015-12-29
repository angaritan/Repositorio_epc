package co.com.comcel.vo;

public class OneTimeCharge {
	
	private Integer entryIndex;
	private String name;
	private String role; //BD:price_ofertasdemanda.ONE_TIME_ROLE
	private String pit;
	private Integer priceRate;
	private String charCodesObject;
	private String charCodesObjecTemp;
	private String oneTimeCC; //BD:price_ofertasdemanda.one_time_char_code	
	private String priceRateTable;
	private String TaxServiceType;
	private String Paidfrombalance;
	private String Immediatepaymentrequired;
	private String ALLOWOVERRIDETYPE;
	private String OC_LEVEL;
    private String DISCOUNT;
    private String DISCOUNT_SCHEME;
    private String VALUE;
    private Integer QUAl_CC_INDEX;
    private String  QUAL_CC_INCRE;
    private String  CC_AlQUEDESCONT;
	
	public String getCC_AlQUEDESCONT() {
		return CC_AlQUEDESCONT;
	}

	public void setCC_AlQUEDESCONT(String cC_AlQUEDESCONT) {
		CC_AlQUEDESCONT = cC_AlQUEDESCONT;
	}

	public Integer getQUAl_CC_INDEX() {
		return QUAl_CC_INDEX;
	}

	public void setQUAl_CC_INDEX(Integer qUAl_CC_INDEX) {
		QUAl_CC_INDEX = qUAl_CC_INDEX;
	}

	public String getQUAL_CC_INCRE() {
		return QUAL_CC_INCRE;
	}

	public void setQUAL_CC_INCRE(String qUAL_CC_INCRE) {
		QUAL_CC_INCRE = qUAL_CC_INCRE;
	}

	

	public String getVALUE() {
		return VALUE;
	}

	public void setVALUE(String vALUE) {
		VALUE = vALUE;
	}

	public String getDISCOUNT() {
		return DISCOUNT;
	}

	public void setDISCOUNT(String dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}

	public String getDISCOUNT_SCHEME() {
		return DISCOUNT_SCHEME;
	}

	public void setDISCOUNT_SCHEME(String dISCOUNT_SCHEME) {
		DISCOUNT_SCHEME = dISCOUNT_SCHEME;
	}

	public String getALLOWOVERRIDETYPE() {
		return ALLOWOVERRIDETYPE;
	}

	public void setALLOWOVERRIDETYPE(String aLLOWOVERRIDETYPE) {
		ALLOWOVERRIDETYPE = aLLOWOVERRIDETYPE;
	}
	public String getOC_LEVEL() {
		return OC_LEVEL;
	}

	public void setOC_LEVEL(String oC_LEVEL) {
		OC_LEVEL = oC_LEVEL;
	}

	public String getPaidfrombalance() {
		return Paidfrombalance;
	}

	public String getImmediatepaymentrequired() {
		return Immediatepaymentrequired;
	}

	public void setImmediatepaymentrequired(String immediatepaymentrequired) {
		Immediatepaymentrequired = immediatepaymentrequired;
	}

	public void setPaidfrombalance(String paidfrombalance) {
		Paidfrombalance = paidfrombalance;
	}

	public String getTaxServiceType() {
		return TaxServiceType;
	}

	public void setTaxServiceType(String taxServiceType) {
		TaxServiceType = taxServiceType;
	}

	public String getCharCodesObjecTemp() {
		return charCodesObjecTemp;
	}
	
	public void setCharCodesObjecTemp(String charCodesObjecTemp) {
		this.charCodesObjecTemp = charCodesObjecTemp;
	}
		public String getPriceRateTable() {
		return priceRateTable;
	}
	
	public void setPriceRateTable(String priceRateTable) {
		this.priceRateTable = priceRateTable;
	}
	
	public Integer getEntryIndex() {
		return entryIndex;
	}
	/**
	 * @param entryIndex the entryIndex to set
	 */
	public void setEntryIndex(Integer entryIndex) {
		this.entryIndex = entryIndex;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the pit
	 */
	public String getPit() {
		return pit;
	}
	/**
	 * @param pit the pit to set
	 */
	public void setPit(String pit) {
		this.pit = pit;
	}
	/**
	 * @return the priceRate
	 */
	public Integer getPriceRate() {
		return priceRate;
	}
	/**
	 * @param priceRate the priceRate to set
	 */
	public void setPriceRate(Integer priceRate) {
		this.priceRate = priceRate;
	}
	/**
	 * @return the charCodesObject
	 */
	public String getCharCodesObject() {
		return charCodesObject;
	}
	/**
	 * @param charCodesObject the charCodesObject to set
	 */
	public void setCharCodesObject(String charCodesObject) {
		this.charCodesObject = charCodesObject;
	}
	/**
	 * @return the oneTimeCC
	 */
	public String getOneTimeCC() {
		return oneTimeCC;
	}
	/**
	 * @param oneTimeCC the oneTimeCC to set
	 */
	public void setOneTimeCC(String oneTimeCC) {
		this.oneTimeCC = oneTimeCC;
	}

}
