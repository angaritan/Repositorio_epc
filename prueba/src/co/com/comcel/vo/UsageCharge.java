package co.com.comcel.vo;

import java.util.List;

public class UsageCharge {
	private Integer entryIndex;
	private String name;
	private String role;
	private String pit;
	private String defaultRollingId;
	private String periodSensitivityPolicy;
	private String QualificationCriterion;
	private String rollingPolicy;
	private String rollingPolicyAllowance;
	private String serviceFilter;
	private String roundingFactorMedida;
	private Integer roundingFactorCantidad;
	private String shouldProrate;
	private String roundingMethod;
	private Integer intercalatedPromotionCycles;
	private String utilizeZeroForRate;
	private Integer number_of_cycles_to_roll;
	private String minimum_unitA;
	private Integer minimum_unitB;
	private String special_day_setA;
	private String special_day_setB;
	private String period_setA;
	private String period_setB;
	private String service_filter_to_charge_codeA;
	private String service_filter_to_charge_codeB;
	private String service_filter_groupA;
	private String service_filter_groupB;
	private String charge_codes_objectA;
	private String charge_codes_objectB;
	private String zone_mapping;
	private String providers_group;
	private String zone_list_group;
	private String zones;
	private String RLC_Tax_change;
	private String Taxchange;	
	private Integer QPP_EntryIndex;
	private String QPP_Incremental;//Quota_per_period
	private String QPP_UOMForQuantity;
	private String QPP_PEComplexAttributeValue;
	private String QPP_PEComplexAttributeLink;
	private Integer QCTP_EntryIndex;
	private String QCTP_Incremental;//Quota_per_call_type_and_period
	private String QCTP_UOMForQuantity;
	private String QCTP_PEComplexAttributeValue;
	private String QCTP_PEComplexAttributeLink;
	private String QCTP_DimensionA;
	private String QCTP_DimensionB;
	private Integer RPP_EntryIndex;
	private String RPP_Incremental;//Rate_per_period_rate
	private String RPP_UOMForQuantity;
	private String RPP_PEComplexAttributeValue;
	private String RPP_PEComplexAttributeLink;
	private Integer URI_EntryIndex;
	private String URI_Incremental;//Unit_rate
	private String URI_UOMForQuantity;
	private String URI_PEComplexAttributeValue;
	private String URI_PEComplexAttributeLink;
	private String URI_DimensionA;
	private String URI_DimensionB;	
	private Integer RTR_EntryIndex;
	private String RTR_Incremental;//Rate_table_rate
	private String RTR_UOMForQuantity;	
	private Integer QCPP_EntryIndex;
	private String  QCPP_Incremental;
	private String  QCPP_UOMForQuantity;
	private String TAX_CHANGE_OBJECT_01;
	private String TAX_CHANGE_OBJECT_02;
	private Integer RSPR_EntryIndex;//Rate_based_on_scale_and_period_rate
	private String RSPR_Incremental;
	private String RSPR_UOMForQuantity;
	
	private Integer DS_EntryIndex;//Duration_Scale
	private String DS_Units;
	private Integer DSScale__EntryIndex;
	private Integer DSScale_From;
	private Integer DSScale_To;
	
	public Integer getRSPR_EntryIndex() {
		return RSPR_EntryIndex;
	}
	public void setRSPR_EntryIndex(Integer rSPR_EntryIndex) {
		RSPR_EntryIndex = rSPR_EntryIndex;
	}
	public String getRSPR_Incremental() {
		return RSPR_Incremental;
	}
	public void setRSPR_Incremental(String rSPR_Incremental) {
		RSPR_Incremental = rSPR_Incremental;
	}
	public String getRSPR_UOMForQuantity() {
		return RSPR_UOMForQuantity;
	}
	public void setRSPR_UOMForQuantity(String rSPR_UOMForQuantity) {
		RSPR_UOMForQuantity = rSPR_UOMForQuantity;
	}
	public Integer getDS_EntryIndex() {
		return DS_EntryIndex;
	}
	public void setDS_EntryIndex(Integer dS_EntryIndex) {
		DS_EntryIndex = dS_EntryIndex;
	}
	public String getDS_Units() {
		return DS_Units;
	}
	public void setDS_Units(String dS_Units) {
		DS_Units = dS_Units;
	}
	public Integer getDSScale__EntryIndex() {
		return DSScale__EntryIndex;
	}
	public void setDSScale__EntryIndex(Integer dSScale__EntryIndex) {
		DSScale__EntryIndex = dSScale__EntryIndex;
	}
	
	
	public Integer getDSScale_From() {
		return DSScale_From;
	}
	public void setDSScale_From(Integer dSScale_From) {
		DSScale_From = dSScale_From;
	}
	public Integer getDSScale_To() {
		return DSScale_To;
	}
	public void setDSScale_To(Integer dSScale_To) {
		DSScale_To = dSScale_To;
	}
	public String getRLC_Tax_change() {
		return RLC_Tax_change;
	}
	public void setRLC_Tax_change(String rLC_Tax_change) {
		RLC_Tax_change = rLC_Tax_change;
	}
	public String getTaxchange() {
		return Taxchange;
	}
	
	public void setTaxchange(String taxchange) {
		Taxchange = taxchange;
	}
	public String getQualificationCriterion() {
		return QualificationCriterion;
	}
	public void setQualificationCriterion(String qualificationCriterion) {
		QualificationCriterion = qualificationCriterion;
	}
	
	/**
	 * @return the tAX_CHANGE_OBJECT_01
	 */
	public String getTAX_CHANGE_OBJECT_01() {
		return TAX_CHANGE_OBJECT_01;
	}
	/**
	 * @param tAX_CHANGE_OBJECT_01 the tAX_CHANGE_OBJECT_01 to set
	 */
	public void setTAX_CHANGE_OBJECT_01(String tAX_CHANGE_OBJECT_01) {
		TAX_CHANGE_OBJECT_01 = tAX_CHANGE_OBJECT_01;
	}
	/**
	 * @return the tAX_CHANGE_OBJECT_02
	 */
	public String getTAX_CHANGE_OBJECT_02() {
		return TAX_CHANGE_OBJECT_02;
	}
	/**
	 * @param tAX_CHANGE_OBJECT_02 the tAX_CHANGE_OBJECT_02 to set
	 */
	public void setTAX_CHANGE_OBJECT_02(String tAX_CHANGE_OBJECT_02) {
		TAX_CHANGE_OBJECT_02 = tAX_CHANGE_OBJECT_02;
	}
	
	/**
	 * @return the qCPP_EntryIndex
	 */
	public Integer getQCPP_EntryIndex() {
		return QCPP_EntryIndex;
	}
	/**
	 * @param qCPP_EntryIndex the qCPP_EntryIndex to set
	 */
	public void setQCPP_EntryIndex(Integer qCPP_EntryIndex) {
		QCPP_EntryIndex = qCPP_EntryIndex;
	}
	/**
	 * @return the qCPP_Incremental
	 */
	public String getQCPP_Incremental() {
		return QCPP_Incremental;
	}
	/**
	 * @param qCPP_Incremental the qCPP_Incremental to set
	 */
	public void setQCPP_Incremental(String qCPP_Incremental) {
		QCPP_Incremental = qCPP_Incremental;
	}
	/**
	 * @return the qCPP_UOMForQuantity
	 */
	public String getQCPP_UOMForQuantity() {
		return QCPP_UOMForQuantity;
	}
	/**
	 * @param qCPP_UOMForQuantity the qCPP_UOMForQuantity to set
	 */
	public void setQCPP_UOMForQuantity(String qCPP_UOMForQuantity) {
		QCPP_UOMForQuantity = qCPP_UOMForQuantity;
	}
	private String rateGroupList;
	
	private String value;
	
	private List<Value> lstValue;
	
	public String getURI_DimensionA() {
		return URI_DimensionA;
	}
	public void setURI_DimensionA(String uRIDimensionA) {
		URI_DimensionA = uRIDimensionA;
	}
	public String getURI_DimensionB() {
		return URI_DimensionB;
	}
	public void setURI_DimensionB(String uRIDimensionB) {
		URI_DimensionB = uRIDimensionB;
	}
	public String getRollingPolicyAllowance() {
		return rollingPolicyAllowance;
	}
	public void setRollingPolicyAllowance(String rollingPolicyAllowance) {
		this.rollingPolicyAllowance = rollingPolicyAllowance;
	}
	public Integer getRTR_EntryIndex() {
		return RTR_EntryIndex;
	}
	public void setRTR_EntryIndex(Integer rTREntryIndex) {
		RTR_EntryIndex = rTREntryIndex;
	}
	public String getRTR_Incremental() {
		return RTR_Incremental;
	}
	public void setRTR_Incremental(String rTRIncremental) {
		RTR_Incremental = rTRIncremental;
	}
	public String getRTR_UOMForQuantity() {
		return RTR_UOMForQuantity;
	}
	public void setRTR_UOMForQuantity(String rTRUOMForQuantity) {
		RTR_UOMForQuantity = rTRUOMForQuantity;
	}
	public String getZones() {
		return zones;
	}
	public void setZones(String zones) {
		this.zones = zones;
	}
	public String getZone_mapping() {
		return zone_mapping;
	}
	public void setZone_mapping(String zoneMapping) {
		zone_mapping = zoneMapping;
	}
	public String getProviders_group() {
		return providers_group;
	}
	public void setProviders_group(String providersGroup) {
		providers_group = providersGroup;
	}
	public String getZone_list_group() {
		return zone_list_group;
	}
	public void setZone_list_group(String zoneListGroup) {
		zone_list_group = zoneListGroup;
	}
	public Integer getURI_EntryIndex() {
		return URI_EntryIndex;
	}
	public void setURI_EntryIndex(Integer uRIEntryIndex) {
		URI_EntryIndex = uRIEntryIndex;
	}
	public String getURI_Incremental() {
		return URI_Incremental;
	}
	public void setURI_Incremental(String uRIIncremental) {
		URI_Incremental = uRIIncremental;
	}
	public String getURI_UOMForQuantity() {
		return URI_UOMForQuantity;
	}
	public void setURI_UOMForQuantity(String uRIUOMForQuantity) {
		URI_UOMForQuantity = uRIUOMForQuantity;
	}
	public String getURI_PEComplexAttributeValue() {
		return URI_PEComplexAttributeValue;
	}
	public void setURI_PEComplexAttributeValue(String uRIPEComplexAttributeValue) {
		URI_PEComplexAttributeValue = uRIPEComplexAttributeValue;
	}
	public String getURI_PEComplexAttributeLink() {
		return URI_PEComplexAttributeLink;
	}
	public void setURI_PEComplexAttributeLink(String uRIPEComplexAttributeLink) {
		URI_PEComplexAttributeLink = uRIPEComplexAttributeLink;
	}
	public String getRPP_PEComplexAttributeLink() {
		return RPP_PEComplexAttributeLink;
	}
	public void setRPP_PEComplexAttributeLink(String rPPPEComplexAttributeLink) {
		RPP_PEComplexAttributeLink = rPPPEComplexAttributeLink;
	}
	public String getQPP_PEComplexAttributeLink() {
		return QPP_PEComplexAttributeLink;
	}
	public void setQPP_PEComplexAttributeLink(String qPPPEComplexAttributeLink) {
		QPP_PEComplexAttributeLink = qPPPEComplexAttributeLink;
	}
	public String getQCTP_PEComplexAttributeLink() {
		return QCTP_PEComplexAttributeLink;
	}
	public void setQCTP_PEComplexAttributeLink(String qCTPPEComplexAttributeLink) {
		QCTP_PEComplexAttributeLink = qCTPPEComplexAttributeLink;
	}
	public Integer getEntryIndex() {
		return entryIndex;
	}
	public void setEntryIndex(Integer entryIndex) {
		this.entryIndex = entryIndex;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPit() {
		return pit;
	}
	public void setPit(String pit) {
		this.pit = pit;
	}
	public String getDefaultRollingId() {
		return defaultRollingId;
	}
	public void setDefaultRollingId(String defaultRollingId) {
		this.defaultRollingId = defaultRollingId;
	}
	public String getPeriodSensitivityPolicy() {
		return periodSensitivityPolicy;
	}
	public void setPeriodSensitivityPolicy(String periodSensitivityPolicy) {
		this.periodSensitivityPolicy = periodSensitivityPolicy;
	}
	public String getRollingPolicy() {
		return rollingPolicy;
	}
	public void setRollingPolicy(String rollingPolicy) {
		this.rollingPolicy = rollingPolicy;
	}
	public String getServiceFilter() {
		return serviceFilter;
	}
	public void setServiceFilter(String serviceFilter) {
		this.serviceFilter = serviceFilter;
	}
	public String getRoundingFactorMedida() {
		return roundingFactorMedida;
	}
	public void setRoundingFactorMedida(String roundingFactorMedida) {
		this.roundingFactorMedida = roundingFactorMedida;
	}
	
	public Integer getRoundingFactorCantidad() {
		return roundingFactorCantidad;
	}
	public void setRoundingFactorCantidad(Integer roundingFactorCantidad) {
		this.roundingFactorCantidad = roundingFactorCantidad;
	}
	public String getShouldProrate() {
		return shouldProrate;
	}
	public void setShouldProrate(String shouldProrate) {
		this.shouldProrate = shouldProrate;
	}
	public String getRoundingMethod() {
		return roundingMethod;
	}
	public void setRoundingMethod(String roundingMethod) {
		this.roundingMethod = roundingMethod;
	}
	public Integer getIntercalatedPromotionCycles() {
		return intercalatedPromotionCycles;
	}
	public void setIntercalatedPromotionCycles(Integer intercalatedPromotionCycles) {
		this.intercalatedPromotionCycles = intercalatedPromotionCycles;
	}
	public String getUtilizeZeroForRate() {
		return utilizeZeroForRate;
	}
	public void setUtilizeZeroForRate(String utilizeZeroForRate) {
		this.utilizeZeroForRate = utilizeZeroForRate;
	}
	
	public Integer getNumber_of_cycles_to_roll() {
		return number_of_cycles_to_roll;
	}
	public void setNumber_of_cycles_to_roll(Integer numberOfCyclesToRoll) {
		number_of_cycles_to_roll = numberOfCyclesToRoll;
	}
	
	public String getMinimum_unitA() {
		return minimum_unitA;
	}
	public void setMinimum_unitA(String minimumUnitA) {
		minimum_unitA = minimumUnitA;
	}
	public Integer getMinimum_unitB() {
		return minimum_unitB;
	}
	public void setMinimum_unitB(Integer minimumUnitB) {
		minimum_unitB = minimumUnitB;
	}
	public String getSpecial_day_setA() {
		return special_day_setA;
	}
	public void setSpecial_day_setA(String specialDaySetA) {
		special_day_setA = specialDaySetA;
	}
	public String getSpecial_day_setB() {
		return special_day_setB;
	}
	public void setSpecial_day_setB(String specialDaySetB) {
		special_day_setB = specialDaySetB;
	}
	public String getPeriod_setA() {
		return period_setA;
	}
	public void setPeriod_setA(String periodSetA) {
		period_setA = periodSetA;
	}
	public String getPeriod_setB() {
		return period_setB;
	}
	public void setPeriod_setB(String periodSetB) {
		period_setB = periodSetB;
	}
	public String getService_filter_to_charge_codeA() {
		return service_filter_to_charge_codeA;
	}
	public void setService_filter_to_charge_codeA(String serviceFilterToChargeCodeA) {
		service_filter_to_charge_codeA = serviceFilterToChargeCodeA;
	}
	public String getService_filter_to_charge_codeB() {
		return service_filter_to_charge_codeB;
	}
	public void setService_filter_to_charge_codeB(String serviceFilterToChargeCodeB) {
		service_filter_to_charge_codeB = serviceFilterToChargeCodeB;
	}
	public String getService_filter_groupA() {
		return service_filter_groupA;
	}
	public void setService_filter_groupA(String serviceFilterGroupA) {
		service_filter_groupA = serviceFilterGroupA;
	}
	public String getService_filter_groupB() {
		return service_filter_groupB;
	}
	public void setService_filter_groupB(String serviceFilterGroupB) {
		service_filter_groupB = serviceFilterGroupB;
	}
	public String getCharge_codes_objectA() {
		return charge_codes_objectA;
	}
	public void setCharge_codes_objectA(String chargeCodesObjectA) {
		charge_codes_objectA = chargeCodesObjectA;
	}
	public String getCharge_codes_objectB() {
		return charge_codes_objectB;
	}
	public void setCharge_codes_objectB(String chargeCodesObjectB) {
		charge_codes_objectB = chargeCodesObjectB;
	}
	
	public Integer getQPP_EntryIndex() {
		return QPP_EntryIndex;
	}
	public void setQPP_EntryIndex(Integer qPPEntryIndex) {
		QPP_EntryIndex = qPPEntryIndex;
	}
	public String getQPP_Incremental() {
		return QPP_Incremental;
	}
	public void setQPP_Incremental(String qPPIncremental) {
		QPP_Incremental = qPPIncremental;
	}
	public String getQPP_UOMForQuantity() {
		return QPP_UOMForQuantity;
	}
	public void setQPP_UOMForQuantity(String qPPUOMForQuantity) {
		QPP_UOMForQuantity = qPPUOMForQuantity;
	}
	public String getQPP_PEComplexAttributeValue() {
		return QPP_PEComplexAttributeValue;
	}
	public void setQPP_PEComplexAttributeValue(String qPPPEComplexAttributeValue) {
		QPP_PEComplexAttributeValue = qPPPEComplexAttributeValue;
	}
	
	public Integer getQCTP_EntryIndex() {
		return QCTP_EntryIndex;
	}
	public void setQCTP_EntryIndex(Integer qCTPEntryIndex) {
		QCTP_EntryIndex = qCTPEntryIndex;
	}
	public String getQCTP_Incremental() {
		return QCTP_Incremental;
	}
	public void setQCTP_Incremental(String qCTPIncremental) {
		QCTP_Incremental = qCTPIncremental;
	}
	public String getQCTP_UOMForQuantity() {
		return QCTP_UOMForQuantity;
	}
	public void setQCTP_UOMForQuantity(String qCTPUOMForQuantity) {
		QCTP_UOMForQuantity = qCTPUOMForQuantity;
	}
	public String getQCTP_PEComplexAttributeValue() {
		return QCTP_PEComplexAttributeValue;
	}
	public void setQCTP_PEComplexAttributeValue(String qCTPPEComplexAttributeValue) {
		QCTP_PEComplexAttributeValue = qCTPPEComplexAttributeValue;
	}
	public String getQCTP_DimensionA() {
		return QCTP_DimensionA;
	}
	public void setQCTP_DimensionA(String qCTPDimensionA) {
		QCTP_DimensionA = qCTPDimensionA;
	}
	public String getQCTP_DimensionB() {
		return QCTP_DimensionB;
	}
	public void setQCTP_DimensionB(String qCTPDimensionB) {
		QCTP_DimensionB = qCTPDimensionB;
	}
	public Integer getRPP_EntryIndex() {
		return RPP_EntryIndex;
	}
	public void setRPP_EntryIndex(Integer rPPEntryIndex) {
		RPP_EntryIndex = rPPEntryIndex;
	}
	public String getRPP_Incremental() {
		return RPP_Incremental;
	}
	public void setRPP_Incremental(String rPPIncremental) {
		RPP_Incremental = rPPIncremental;
	}
	public String getRPP_UOMForQuantity() {
		return RPP_UOMForQuantity;
	}
	public void setRPP_UOMForQuantity(String rPPUOMForQuantity) {
		RPP_UOMForQuantity = rPPUOMForQuantity;
	}
	public String getRPP_PEComplexAttributeValue() {
		return RPP_PEComplexAttributeValue;
	}
	public void setRPP_PEComplexAttributeValue(String rPPPEComplexAttributeValue) {
		RPP_PEComplexAttributeValue = rPPPEComplexAttributeValue;
	}
	/**
	 * @return the rateGroupList
	 */
	public String getRateGroupList() {
		return rateGroupList;
	}
	/**
	 * @param rateGroupList the rateGroupList to set
	 */
	public void setRateGroupList(String rateGroupList) {
		this.rateGroupList = rateGroupList;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the lstValue
	 */
	public List<Value> getLstValue() {
		return lstValue;
	}
	/**
	 * @param lstValue the lstValue to set
	 */
	public void setLstValue(List<Value> lstValue) {
		this.lstValue = lstValue;
	}

	
}
