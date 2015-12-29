package co.com.comcel.dao;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import co.com.comcel.vo.AttrPaqLDI;
import co.com.comcel.vo.BillingOffer;
import co.com.comcel.vo.CompararTTS_BO;
import co.com.comcel.vo.Componentes;
import co.com.comcel.vo.MaterialSim;
import co.com.comcel.vo.NewOffersAmdocs;
import co.com.comcel.vo.OfertasDemanda;
import co.com.comcel.vo.OneTimeCharge;
import co.com.comcel.vo.Paquete;
import co.com.comcel.vo.PaqueteLDI;
import co.com.comcel.vo.Plan;
import co.com.comcel.vo.PriceGenerico;
import co.com.comcel.vo.RecurringCharge;
import co.com.comcel.vo.ServiceFilterList;
import co.com.comcel.vo.ServiceFilterToCharge;
import co.com.comcel.vo.TOTALES_SEG_CAL;
import co.com.comcel.vo.TablaTraduccion;
import co.com.comcel.vo.UsageCharge;
import co.com.comcel.vo.Value;
import co.com.comcel.vo.BORevision;
import co.com.comcel.vo.ValuesPrepago;
import co.com.comcel.vo.servicio_comp;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class DataDAO extends SqlMapDaoTemplate implements IDataDAO{
	private static final Logger log = Logger.getLogger("alarm");
	public DataDAO(DaoManager daoManager) {
		super(daoManager);
	}

	/**
	 * 
	 */
	public Collection<Plan> getPlanesMigracion()throws DaoException{
		try {
			Collection<Plan> list = queryForList("DATA.getPlanesMigracion", null);
			for(Plan plan:list){
				plan.setPLAN(plan.getPLAN_DOX().replaceAll("Plan ", ""));
				plan.setPLAN(plan.getPLAN_DOX().replaceAll("PLAN ", ""));
			}
			return list;
		} catch(DaoException e) {
			throw e;
		}
	}
	
	public String getPriceGenerico(String code)throws DaoException{
		try {
			return (String)queryForObject("DATA.getPriceGenerico", code);
		} catch(DaoException e) {
			throw e;
		}
	}

	public String getPriceGenericoGPRS_PL(String code)throws DaoException{
		try {
			return (String)queryForObject("DATA.getPriceGenericoGPRS_PL", code);
		} catch(DaoException e) {
			throw e;
		}
	}
	
	public String getPriceGenericoGPRS_PQ(String code)throws DaoException{
		try {
			return (String)queryForObject("DATA.getPriceGenericoGPRS_PQ", code);
		} catch(DaoException e) {
			throw e;
		}
	}
	
	public String getPriceGenericoBB_PL(String code)throws DaoException{
		try {
			return (String)queryForObject("DATA.getPriceGenericoBB_PL", code);
		} catch(DaoException e) {
			throw e;
		}
	}
	
	public String getPriceGenericoBB_PQ(String code)throws DaoException{
		try {
			return (String)queryForObject("DATA.getPriceGenericoBB_PQ", code);
		} catch(DaoException e) {
			throw e;
		}
	}
	
	public List<PriceGenerico> getPricesGenericos(){
		return queryForList("DATA.getPricesGenericos", null);
	}
	
	public List<Paquete> getPaquetesMigracion(){
		return queryForList("DATA.getPaquetesMigracion", null);
	}
	
	public List<Paquete> getPaquetesEspeciales(){
		return queryForList("DATA.getPaquetesEspeciales", null);
	}
	
	public List<OfertasDemanda> getOfertasDemanda(){
		return queryForList("DATA.getOfertasDemanda", null);
	}
	public  List<Plan> getPlanesPrepago( boolean increm){
		if(increm)return queryForList("DATA.getPlanesPrepagoInc", null);
			
		else return queryForList("DATA.getPlanesPrepago", null);
	}
	public  List<ValuesPrepago> getValuesPrepago(){
		return queryForList("DATA.getValuesPrepago", null);
	}
	
	public void insertBO(BillingOffer bo){
		if(bo.getPlan().getTIPOPLAN().equalsIgnoreCase("Prepago")&&bo.getPlan().getCARACT_PLAN().trim().equalsIgnoreCase("Bolsa Unica")){
			bo.getPlan().setCARACT_PLAN("Prepago");			
		}
		
		insert("DATA.insertBO", bo);
	}
	
	public void insertBOSuspension(BillingOffer bo){
		insert("DATA.insertBOSuspension", bo);
	}
	
	
	public void insertTTBO(TablaTraduccion tt){
		insert("DATA.insertTTBO", tt);
	}
	
	public void insertTTPO(TablaTraduccion tt){
		insert("DATA.insertTTPO", tt);
	}
	
	public void insertBOVIP(BillingOffer bo){
		insert("DATA.insertBOVIP", bo);
	}
	
	public void insertBOPaq(BillingOffer bo){
		bo.getPaquete().setTIPO_ALLOWANCE(bo.getPaquete().getTIPO_ALLOWANCE().trim());
		if (bo.getPaquete().getTIPO_ALLOWANCE().trim().contains("FnF") ){
			bo.getPaquete().setALLOWANCE(bo.getPaquete().getALLOWANCE()==null?"0":bo.getPaquete().getALLOWANCE().trim());
		}else{
			bo.getPaquete().setALLOWANCE("0");
		}
		if (bo.getPaquete().getTIPO_ALLOWANCE().trim().contains("WindowsMobile") ){
			bo.getPaquete().setTIPO_ALLOWANCE("Internet");
		}
		
		insert("DATA.insertBOPaq", bo);
	}
	
	public void insertBOPaqLDI(BillingOffer bo){
		insert("DATA.insertBOPaqLDI", bo);
	}
	
	public void truncateBO(){
		delete("DATA.truncateBO", null);
	}
	
	public void updateBO_numEleg(){
		update("DATA.updateBO_numEleg", null);
	}
	
	
	public void truncateTTS(){
		delete("DATA.truncateTTS", null);
	}
	public void updateBO_spCode(){
		update("DATA.updateBO_spCode", null);
	}
	
	public void insertBOPanama(BillingOffer bo){
		insert("DATA.insertBOPanama", bo);
	}
	
	public void insertBOPaqPanama(BillingOffer bo){
		bo.getPaquete().setTIPO_ALLOWANCE(bo.getPaquete().getTIPO_ALLOWANCE().trim());
		insert("DATA.insertBOPaqPanama", bo);
	}
	
	public void truncateBOPanama(){
		delete("DATA.truncateBOPanama", null);
	}
	
	public Collection<Plan> getPlanesMigracionPanama()throws DaoException{
		try {
			Collection<Plan> list = queryForList("DATA.getPlanesMigracionPanama", null);
			for(Plan plan:list){
				plan.setPLAN(plan.getPLAN_DOX().replaceAll("Plan ", ""));
				plan.setPLAN(plan.getPLAN_DOX().replaceAll("PLAN ", ""));
			}
			return list;
		} catch(DaoException e) {
			throw e;
		}
	}
	
	public String getPriceGenericoPanama(String code)throws DaoException{
		try {
			return (String)queryForObject("DATA.getPriceGenericoPanama", code);
		} catch(DaoException e) {
			throw e;
		}
	}
	
	public List<PriceGenerico> getPricesGenericosPanama(){
		return queryForList("DATA.getPricesGenericosPanama", null);
	}
	
	public List<Paquete> getPaquetesMigracionPanama(){
		return queryForList("DATA.getPaquetesMigracionPanama", null);
	}
	
	public List<TablaTraduccion> getInfoTT(){
		return queryForList("DATA.getInfoTT", null);
	}
	
	public List<TablaTraduccion> getInfoTTPre(){
		return queryForList("DATA.getInfoTTPre", null);
	}
	
	public List<Integer> getTMCODE_NoMigrados(){
		return queryForList("DATA.getTMCODE_NoMigrados", null);
	}
	
	public List<TablaTraduccion> getInfoTTPanama(){
		return queryForList("DATA.getInfoTTPanama", null);
	}
	
	
	public List<servicio_comp>  getInfoComp(){
		return queryForList("DATA.getInfoComp", null);
	}
	
	public List<Integer> getTmcodeAd(Long tmcode){
		return queryForList("DATA.getTmcodeAd", tmcode);
	}
	
	public List<Integer> getTmcodeAdPanama(Long tmcode){
		return queryForList("DATA.getTmcodeAdPanama", tmcode);
	}
	
	public List<PaqueteLDI> getPaquetesLDI(){
		List<PaqueteLDI> paquetesLDIList = queryForList("DATA.getPaquetesLDI", null);
		for(PaqueteLDI paqueteLDI:paquetesLDIList){
			List<AttrPaqLDI> attrPaqLDIList = queryForList("DATA.getAttrPaqLDI", paqueteLDI.getSPCODE());
			paqueteLDI.setAttrPaqLDIList(attrPaqLDIList);
		}
		return paquetesLDIList;
	}
	
	public List<Integer> getPlanesAsociados(Long tmcode){
		return queryForList("DATA.getPlanesAsociados", tmcode);
	}
	
	public List<ServiceFilterList> getServiceFilterList(){
		return queryForList("DATA.getServiceFilterList", null);
	}
	
	public List<ServiceFilterToCharge> getServiceFilterToCharge(){
		return queryForList("DATA.getServiceFilterToCharge", null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RecurringCharge> getPriceOfertaDemandaRC(int idOfertaDemanda){
		
		List<RecurringCharge> lstRecurringCharge = queryForList("DATA.getPriceOfertasDemandaRC", idOfertaDemanda);	
	
		return lstRecurringCharge;
	}

	@Override
	public List<UsageCharge> getPriceOfertaDemandaUC(int idOfertaDemanda) {
		List<UsageCharge> lstUsageCharge = queryForList("DATA.getPriceOfertasDemandaUC", idOfertaDemanda);	
		
		return lstUsageCharge;
	}
	
	@Override
	public List<OneTimeCharge> getPriceOfertaDemandaOTC(int idOfertaDemanda) {
		List<OneTimeCharge> lstUsageCharge = queryForList("DATA.getPriceOfertasDemandaOTC", idOfertaDemanda);	
		
		return lstUsageCharge;
	}
	
	@Override
	public List<Value> getValue(int idOfertaDemanda) {
		List<Value> lstValue = queryForList("DATA.getValue", idOfertaDemanda);	
		
		return lstValue;
	}

	@Override
	public List<TablaTraduccion> getRefTT() {
		// TODO Auto-generated method stub
		List<TablaTraduccion> lsttref = queryForList("DATA.getRefTT",null);
	return lsttref;
	}
	public List<TablaTraduccion> getRefTTPre() {
		// TODO Auto-generated method stub
		List<TablaTraduccion> lsttref = queryForList("DATA.getRefTTPre",null);
	return lsttref;
	}

	@Override
	public void insertBOREVBPaq(BORevision boR) {
		// TODO Auto-generated method stub
		insert("DATA.insertBOREVPaq", boR);
	}

	@Override
	public void insertBOREVBPlan(BORevision boR) {
		// TODO Auto-generated method stub
		insert("DATA.insertBOREVPlan", boR);
		
	}

	@Override
	public void insertBOREVBddem(BORevision boR) {
		// TODO Auto-generated method stub
		insert("DATA.insertBOREVddem", boR);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OfertasDemanda> getOfertasDemandaSus() {
		// TODO Auto-generated method stub
		return queryForList("DATA.getOfertasDemandaSus", null);
	}
	@SuppressWarnings("unchecked")
	public List<OfertasDemanda> getOfertasPrepago() {
		// TODO Auto-generated method stub
		return queryForList("DATA.getOfertasPrepago", null);
	}
  //DATA.getOfertasDemPrep
	@SuppressWarnings("unchecked")
	public List<OfertasDemanda> getOfertasDemPrep() {
		// TODO Auto-generated method stub
		return queryForList("DATA.getOfertasDemPrep", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<TOTALES_SEG_CAL> generaTotalesCalidad(){
		return queryForList("DATA.querycalidadTotales",null);	
	}	
	public void  insertCalidad(){		
	     queryForObject("DATA.calidadTotales2",null);	 
     }
	@SuppressWarnings("unchecked")
	public List<CompararTTS_BO> getcomparPAQTTS_BO() {
		return queryForList("DATA.comparPAQTTS_BO",null);
	}
	
	@SuppressWarnings("unchecked")
	public List<CompararTTS_BO> comparPLANTTS_BO() {
		return queryForList("DATA.comparPLANTTS_BO",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getNewOffersAmdocs() {
		return queryForList("DATA.getNewOffersAmdocs",null);
	}
	//-------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOfertasVig() {
		return queryForList("DATA.getOfertasVig",null);
	}	
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOfertasNoVig() {
		return queryForList("DATA.getOfertasNoVig",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferCambioPlanVig() {
		return queryForList("DATA.getOferCambioPlanVig",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferCambioPlanNoVig() {
		return queryForList("DATA.getOferCambioPlanNoVig",null);
	}	
	//-------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public List<MaterialSim> getMaterialSim(){
		return queryForList("DATA.getMaterialSim",null);
	}
	//-------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOfertasPlan() {
		return queryForList("DATA.getOfertasPlan",null);
	}
	@SuppressWarnings("unchecked")
	public List<OfertasDemanda> getOfertasServ() {
		return queryForList("DATA.getOfertasServ",null);
	}
	@SuppressWarnings("unchecked")
	public List<OfertasDemanda> getOfertasAdicional() {
		return queryForList("DATA.getOfertasAdicional",null);
	}
	@SuppressWarnings("unchecked")
	public List<OfertasDemanda> getOfertaMercado() {
		return queryForList("DATA.getOfertaMercado",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferElegFNF() {
		return queryForList("DATA.getOferElegFNF",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferEleg1BFNF() {
		return queryForList("DATA.getOferEleg1BFNF",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferEleg2BFNF() {
		return queryForList("DATA.getOferEleg2BFNF",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferEleg3BFNF() {
		return queryForList("DATA.getOferEleg3BFNF",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferEleg0BFNF() {
		return queryForList("DATA.getOferEleg0BFNF",null);
	}
	@SuppressWarnings("unchecked")
	public List<Componentes> getCompMand() {
		return queryForList("DATA.getCompMand",null);
	}
	@SuppressWarnings("unchecked")
	public List<Componentes> getCompOpc() {
		return queryForList("DATA.getCompOpc",null);
	}
	@SuppressWarnings("unchecked")
	public List<Componentes> getCompOpcxDef() {
		return queryForList("DATA.getCompOpcxDef",null);
	}
	@SuppressWarnings("unchecked")
	public List<Componentes> getCompExcluidos() {
		return queryForList("DATA.getCompExcluidos",null);
	}
	@SuppressWarnings("unchecked")
	public List<Componentes> getCompPrepago() {
		return queryForList("DATA.getCompPrepago",null);
	}
	@SuppressWarnings("unchecked")
	public List<Componentes> getActCharge() {
		return queryForList("DATA.getActCharge",null);
	}
	@SuppressWarnings("unchecked")
	public List<NewOffersAmdocs> getOferEleg0FNF() {
		return queryForList("DATA.getOferEleg0FNF",null);
	}
	
	
	
}
