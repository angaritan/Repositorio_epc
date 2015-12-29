package co.com.comcel.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.ibatis.dao.client.DaoException;

import co.com.comcel.vo.BillingOffer;
import co.com.comcel.vo.BORevision;
import co.com.comcel.vo.CompararTTS_BO;
import co.com.comcel.vo.Componentes;
import co.com.comcel.vo.MaterialSim;
import co.com.comcel.vo.NewOffersAmdocs;
import co.com.comcel.vo.OfertasDemanda;
import co.com.comcel.vo.PaqueteLDI;
import co.com.comcel.vo.Plan;
import co.com.comcel.vo.Price;
import co.com.comcel.vo.ServiceFilterList;
import co.com.comcel.vo.TOTALES_SEG_CAL;
import co.com.comcel.vo.TablaTraduccion;
import co.com.comcel.vo.ValuesPrepago;
import co.com.comcel.vo.servicio_comp;


public interface IDataModel {

	/**
	 * M�todo que obtiene el listado de planes a migrar
	 * @param panama
	 * @return
	 * @throws DaoException
	 */
	public Collection<Plan> getPlanesMigracion(boolean panama)throws DaoException;
	
	/**
	 * M�todo que obtiene el price gen�rico por c�digo, asociado a un plan
	 * @param code
	 * @param panama
	 * @return
	 * @throws DaoException
	 */
	public String getPriceGenerico(String code, boolean panama)throws DaoException;
	
	public String getPriceGenericoGPRS_PL(String code)throws DaoException;
	
	public String getPriceGenericoGPRS_PQ(String code)throws DaoException;
	
    public String getPriceGenericoBB_PL(String code)throws DaoException;
	
	public String getPriceGenericoBB_PQ(String code)throws DaoException;
	
	public List<ValuesPrepago>  getValuesPrepago() throws DaoException;
	/**
	 * M�todo que genera la Billing Offer
	 * @param plan: Plan al cual se le va a generar la BO
	 * @param promocion: Bandera que indica si la BO es promocional o no. 0 si no tiene promoci�n, 1 si tiene promoci�n.
	 * @param contLink: NO APLICA
	 * @param panama: Bandera para saber si la BO es de Colombia o Panam�
	 * @param isPlanBasico: Si el valor es uno indica que es el plan basico.
	 * @return
	 * @throws Exception
	 */
	public BillingOffer generarBO(Plan plan, int promocion, int contLink, boolean panama, int isPlanBasico) throws Exception;
	
	/**
	 * M�todo que genera una Billing Offer promocional 12x12
	 * @param plan
	 * @param panama
	 * @return
	 * @throws Exception
	 */
	public BillingOffer generarBOPromo12x12(Plan plan, boolean panama) throws Exception;
	
	/**
	 * M�todo que genera los prices gen�ricos
	 * @param contLink
	 * @param bo
	 * @param panama
	 * @return
	 */
	public List<Price> generarPriceGenerico(int contLink, BillingOffer bo, boolean panama);
	
	/**
	 * M�todo que genera las Billing Offers asociadas a paquetes adicionales
	 * @param panama
	 * @return
	 */
	public List<BillingOffer> generarBOPaquetes(boolean panama);
	
	/**
	 * M�todo que genera las Billing Offers asociadas a paquetes adicionales
	 * @param panama
	 * @return
	 */
	public List<BillingOffer> generarBOPaquetesEspecial();
	
	/**
	 * M�todo que genera las Billing Offers asociadas a las Ofertas por Demanda.
	 * @param panama
	 * @return
	 */
	public List<BillingOffer> generarBOOfertasDemanda();
		
	
	/**
	 * M�todo que inserta una Billing Offer generada en la base de datos
	 * @param boList
	 */
	public void insertBO(List<BillingOffer> boList);
	
	/**
	 * M�todo que inserta una Billing Offer generada de paquete adicional en la base de datos
	 * @param boList
	 */
	public void insertBOPaq(List<BillingOffer> boList);
	
	public void insertBOPaqLDI(List<BillingOffer> boList);
	
	/**
	 * M�todo que limpia la tabla BOffer
	 */
	public void truncateBO();
	public void truncateTTS();
	
	public void updateBO_numEleg();
	
	public void updateBO_spCode();
	
	public void insertBOPanama(List<BillingOffer> boList);
	
	public void insertBOPaqPanama(List<BillingOffer> boList);
	
	public void truncateBOPanama();
	
	/**
	 * M�todo que obtiene la informaci�n de tablas de traducci�n de Billing Offers
	 * @param panama
	 * @return
	 */
	public List<TablaTraduccion> getInfoTT(boolean panama);
	
	public List<TablaTraduccion> getInfoTTPre(boolean panama);
	
	public List<servicio_comp>  getInfoComp();
	
	public List<Integer> getTMCODE_NoMigrados();
	
	public List<Integer> getTmcodeAd(Long tmcode);
	
	public List<Integer> getTmcodeAdPanama(Long tmcode);
	
	/**
	 * M�todo que optiene los paquetes adicionales de LDI
	 * @return
	 */
	public List<PaqueteLDI> getPaquetesLDI();
	
	/**
	 * M�todo que genera las Billing Offers adicionales para paquetes LDI
	 * @return
	 */
	public List<BillingOffer> generarBOPaquetesLDI();
	
	public List<Integer> getPlanesAsociados(Long tmcode);
	
	/**
	 * Obtiene el Filtro de las listas de Servicio
	 * @return HashMap
	 */
	public HashMap<Integer, ServiceFilterList> getServiceFilterList();
	
	public void segmentacioDesc(String archivo) throws Exception;
	
	public List<TablaTraduccion> getRefTT();
	
	public List<TablaTraduccion> getRefTTPre();
	/**
	 * metodo para insertar la informacion b�sica de BO generadas para seguiemiento y validaci�n
	 * 
	 */
	public void insertBOREVPaq(List<BORevision> boRList);
	
    public void insertBOREVplan(List<BORevision> boRList);
    
    public void insertBOREVddem(List<BORevision> boRList);
    
    public void insertTTBO(List<TablaTraduccion> ttList);
    
    public void insertTTPO(List<TablaTraduccion> ttList);
    
	public void segmentacioDesc(String archivo, String strShe) throws Exception;
	
	public List<BillingOffer> generarBOPlanPrepago(boolean incre);	
	public List<BillingOffer> generarBOOfertasPrepago();	
	public List<TOTALES_SEG_CAL> generaTotalesCalidad();
	public void insertCalidad();	
	public List<CompararTTS_BO> comparPLANTTS_BO() ;
	public List<CompararTTS_BO> getcomparPAQTTS_BO();	
	public List<NewOffersAmdocs> getNewOffersAmdocs();
	public List<NewOffersAmdocs> getOfertasVig();
	public List<NewOffersAmdocs> getOfertasNoVig();
	public List<NewOffersAmdocs> getOferCambioPlanVig();
	public List<NewOffersAmdocs> getOferCambioPlanNoVig();	
	public List<NewOffersAmdocs> getOfertasPlan();
	public List<OfertasDemanda> getOfertasServ();
	public List<OfertasDemanda> getOfertasAdicional();
	public List<OfertasDemanda> getOfertaMercado();	
	public List<NewOffersAmdocs> getOferElegFNF();	
	public List<NewOffersAmdocs> getOferEleg1BFNF();	
	public List<NewOffersAmdocs> getOferEleg2BFNF();	
	public List<NewOffersAmdocs> getOferEleg3BFNF();	
	public List<NewOffersAmdocs> getOferEleg0BFNF();	
	public List<Componentes> getCompMand();	
	public List<Componentes> getCompOpc();	
	public List<Componentes> getCompOpcxDef();	
	public List<Componentes> getCompExcluidos();	
	public List<Componentes> getCompPrepago();	
	public List<Componentes> getActCharge();	
	public List<NewOffersAmdocs> getOferEleg0FNF();
	public List<MaterialSim> getMaterialSim();	
	
}