package co.com.amdocs.model;



import com.ibatis.dao.client.DaoManager;

import co.com.amdocs.dao.DaoConfig;
import co.com.amdocs.dao.IDataDAO;
import co.com.amdocs.vo.CcRequerimientos;
import co.com.amdocs.vo.Ofertas_demanda;
import co.com.amdocs.vo.PRODUCT_TYPE;
import co.com.amdocs.vo.Plan;
import co.com.amdocs.vo.PlanPrepago;
import co.com.amdocs.vo.Tipo_oferta;
import co.com.amdocs.vo.canales_venta;
import co.com.amdocs.vo.carac_elegidos;
import co.com.amdocs.vo.oferta_amdocs;
import co.com.amdocs.vo.usingSimcard;
import co.com.amdocs.vo.zona;

import java.util.List;

import org.apache.log4j.Logger;

public class DataModel implements IDataModel {
	
	private IDataDAO dao;
	private DaoManager daoManager;
	private static final Logger log = Logger.getLogger(DataModel.class.getName());
	public DataModel(){
		this.daoManager = DaoConfig.getDaoManager();
		this.dao = (IDataDAO)daoManager.getDao(IDataDAO.class);		
	}	
	public List<canales_venta> getCanalesVenta(){
		System.out.println("Data Model");
		return dao.getCanalesVenta();
	}
	public List<oferta_amdocs>  getPlanEquiv(String tmeq){
		return dao.getPlanEquiv(tmeq);
	}
	public List<oferta_amdocs> getPlanEquiv2(oferta_amdocs om){
		return dao.getPlanEquiv2(om);
	}
	public List<PRODUCT_TYPE> getProductType(){
		return dao.getProductType();
	}
	public List<PRODUCT_TYPE> getTecnologia(){
		return dao.getTecnologia();
	}
	public List<Tipo_oferta> getTipoOferta(){
		return dao.getTipoOferta();
	}
	public List<Ofertas_demanda> getOfertasDemanda(){
		return dao.getOfertasDemanda();
	}
	public List<PlanPrepago> getPlanPrepago(){
		return dao.getPlanPrepago();
	}
	public List<Plan> getPlan(String tmcode){
		return dao.getPlan(tmcode);
	}	
	public List<usingSimcard> getUsingSimcard(){		
		return dao.getUsingSimcard();
	}
	
	public void insertPlan(Plan plan){			
				 dao.insertPlan(plan);	}
	
	public void insertPlanTemp(Plan pl){
		 dao.insertPlanTemp(pl);
	}
	public void insertPrepagoPlat(PlanPrepago pl) {
		dao.insertPrepagoPlat(pl);
	}
	
	public List<carac_elegidos> getCaractFNF(){
		return dao.getCaractFNF();
	}
	public List<carac_elegidos> getCaractBFNF(){
		return dao.getCaractBFNF();
	}
	public List<zona> getZona(){
		return dao.getZona();
	}
	
	/*Info Buscar CCR*/
	public List<CcRequerimientos> getBuscarReq(CcRequerimientos b){
		return dao.getBuscarReq(b);
	}
	/*Info Eliminar CCR*/
	public void EliminarReq (Integer VALUE){
			  dao.EliminarReq(VALUE);		
	}
	/*Info Actualizar CCR*/
	public void UpdateReq(CcRequerimientos a){
		  dao.UpdateReq(a);		
}
	/*CCrequerimientos*/

	public void insertCC_req(CcRequerimientos ccr){			
				 dao.insertCC_req(ccr);	}
	/*Iniciacion de CCRequerimientos*/
	public List<CcRequerimientos> getNum_req(){
		return dao.getNum_req();
	}
}
