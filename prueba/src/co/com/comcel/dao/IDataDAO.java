package co.com.comcel.dao;

import java.util.Collection;
import java.util.List;

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

public interface IDataDAO {

	/**
	 * Obtiene la lista de planes migracion de la base de datos
	 * @return
	 * @throws DaoException
	 */
	public Collection<Plan> getPlanesMigracion()throws DaoException;
	
	/**
	 * Obtiene un price genérico por código
	 * @param code
	 * @return
	 * @throws DaoException
	 */
	public String getPriceGenerico(String code)throws DaoException;

	public String getPriceGenericoGPRS_PL(String code)throws DaoException;
	
	public String getPriceGenericoGPRS_PQ(String code)throws DaoException;
	
    public String getPriceGenericoBB_PL(String code)throws DaoException;
	
	public String getPriceGenericoBB_PQ(String code)throws DaoException;
	
	/**
	 * Obtiene la lista de prices genéricos de la base de datos
	 * @return
	 */
	public List<PriceGenerico> getPricesGenericos();
	
	/**
	 * Obtiene la lista de paquetes a migrar de la base de datos
	 * @return
	 */
	public List<Paquete> getPaquetesMigracion();
	
	/**
	 * Obtiene la lista de paquetes especiales de la base de datos
	 * @return
	 */
	public List<Paquete> getPaquetesEspeciales();
	
	/**
	 * Obtiene la lista de Ofertas de Demanda
	 * @return
	 */
	public List<OfertasDemanda> getOfertasDemanda() throws DaoException;
	
	/**
	 * Obtiene la lista de Ofertas para suspension y VIP
	 * @return
	 */
	public List<OfertasDemanda> getOfertasPrepago();
	public List<OfertasDemanda> getOfertasDemPrep();
	public List<OfertasDemanda> getOfertasDemandaSus();
	
	public  List<Plan> getPlanesPrepago( boolean incre);
	
	public  List<ValuesPrepago> getValuesPrepago();
	
	/**
	 * Inserta una Billing Offer generada en la base de datos
	 * @param bo
	 */
	public void insertBO(BillingOffer bo);
	
	public void insertBOSuspension(BillingOffer bo);
	
	public void insertBOVIP(BillingOffer bo);
	
	/**
	 * Inserta una Billing Offer generada de paquete adicional en la base de datos
	 * @param bo
	 */
	public void insertBOPaq(BillingOffer bo);
	
	/**
	 * Inserta una Billing Offer generada de paquete de LDI en la base de datos
	 * @param bo
	 */
	public void insertBOPaqLDI(BillingOffer bo);
	
	/**
	 * Limpia la tabla BOffer
	 */
	public void truncateBO();
	public void truncateTTS();
	
	public void updateBO_numEleg();
	
	public void updateBO_spCode();
	
	public void insertBOPanama(BillingOffer bo);
	
	public void insertBOPaqPanama(BillingOffer bo);
	
	public void truncateBOPanama();
	
	public Collection<Plan> getPlanesMigracionPanama()throws DaoException;
	
	public String getPriceGenericoPanama(String code)throws DaoException;
	
	public List<PriceGenerico> getPricesGenericosPanama();
	
	public List<Paquete> getPaquetesMigracionPanama();
	
	/**
	 * Obtiene la información de tablas de traducción Colombia
	 * @return
	 */
	public List<TablaTraduccion> getInfoTT();
	public List<TablaTraduccion> getInfoTTPre();
	
	public List<servicio_comp>  getInfoComp();
	
	public List<Integer> getTMCODE_NoMigrados();
	
	public List<TablaTraduccion> getInfoTTPanama();
	
	public List<Integer> getTmcodeAd(Long tmcode);
	
	public List<Integer> getTmcodeAdPanama(Long tmcode);
	
	/**
	 * Obtiene los paquetes LDI a migrar de la base de datos
	 * @return
	 */
	public List<PaqueteLDI> getPaquetesLDI();
	
	public List<Integer> getPlanesAsociados(Long tmcode);
	
	public List<ServiceFilterList> getServiceFilterList();
	
	public List<ServiceFilterToCharge> getServiceFilterToCharge();

	public List<RecurringCharge> getPriceOfertaDemandaRC(int idOfertaDemanda);
	
	public List<UsageCharge> getPriceOfertaDemandaUC(int idOfertaDemanda);

	public List<OneTimeCharge> getPriceOfertaDemandaOTC(int idOfertaDemanda);

	public List<Value> getValue(int idOfertaDemanda);
	
	public List<TablaTraduccion> getRefTT();
	public List<TablaTraduccion> getRefTTPre();

	public void insertBOREVBPaq(BORevision boR);

	public void insertBOREVBPlan(BORevision boR);

	public void insertBOREVBddem(BORevision boR);
	
	public void insertTTBO(TablaTraduccion  tt);
	
	public void insertTTPO(TablaTraduccion  tt);
	public List<TOTALES_SEG_CAL>  generaTotalesCalidad();
	public void insertCalidad();
	public List<CompararTTS_BO> comparPLANTTS_BO() ;
	public List<CompararTTS_BO> getcomparPAQTTS_BO();	
	public List<NewOffersAmdocs> getNewOffersAmdocs();
	public List<NewOffersAmdocs> getOfertasVig();
	public List<NewOffersAmdocs> getOfertasNoVig();
	public List<NewOffersAmdocs> getOferCambioPlanVig();
	public List<NewOffersAmdocs> getOferCambioPlanNoVig();	
	public List<MaterialSim> getMaterialSim();
	
	public List<NewOffersAmdocs> getOfertasPlan();	
	public List<OfertasDemanda> getOfertasServ();	
	public List<OfertasDemanda> getOfertasAdicional(); 	
	public List<OfertasDemanda> getOfertaMercado();	
	public List<NewOffersAmdocs> getOferElegFNF(); 	
	public List<NewOffersAmdocs> getOferEleg1BFNF(); 	
	public List<NewOffersAmdocs> getOferEleg2BFNF();	
	public List<NewOffersAmdocs> getOferEleg3BFNF(); 	
	public List<NewOffersAmdocs> getOferEleg0BFNF(); 
	public List<NewOffersAmdocs> getOferEleg0FNF();
	public List<Componentes> getCompMand();	
	public List<Componentes> getCompOpc(); 	
	public List<Componentes> getCompOpcxDef(); 
	public List<Componentes> getCompExcluidos(); 
	public List<Componentes> getCompPrepago(); 	
	public List<Componentes> getActCharge(); 	
	 
	
}
