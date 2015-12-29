package co.com.amdocs.dao;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

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

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

public class DataDao extends SqlMapDaoTemplate implements IDataDAO {

	private static final Logger log = Logger.getLogger("alarm");
	public DataDao(DaoManager daoManager) {
		super(daoManager);
	}
		
	@SuppressWarnings("unchecked")
	public List<canales_venta> getCanalesVenta(){
		System.out.println("Data Dao");
		return queryForList("DATA.getCanalesVenta", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<oferta_amdocs> getPlanEquiv(String tmeq){		
		return queryForList("DATA.getPlanEquiv", tmeq);
	}
	@SuppressWarnings("unchecked")
	public List<oferta_amdocs> getPlanEquiv2(oferta_amdocs om){	
		System.out.println(om.getTmcode());
		System.out.println(om.getSpcode());
		
		return queryForList("DATA.getPlanEquiv2", om);
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PRODUCT_TYPE> getProductType(){		
		return queryForList("DATA.getProductType",null);
	}
	@SuppressWarnings("unchecked")
	public List<PRODUCT_TYPE> getTecnologia(){		
		return queryForList("DATA.getTecnologia" ,null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Tipo_oferta> getTipoOferta(){		
		return queryForList("DATA.getTipoOferta" ,null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Ofertas_demanda> getOfertasDemanda(){		
		return queryForList("DATA.getOfertasDemanda" ,null);
	}
	@SuppressWarnings("unchecked")
	public List<PlanPrepago> getPlanPrepago(){		
		return queryForList("DATA.getPlanPrepago" ,null);
	}
	
	@SuppressWarnings("unchecked")
	public List<Plan> getPlan(String tmcode){		
		return queryForList("DATA.getPlan" ,tmcode);
	}
	
	public void insertPlan(Plan pl) {
		// TODO Auto-generated method stub
		insert("DATA.insertPlan", pl);
		
	}
	
	public void insertPlanTemp(Plan pl) {
		insert("DATA.insertPlanTemp", pl);
	}	
	
	@SuppressWarnings("unchecked")
	public List<usingSimcard> getUsingSimcard() {
		// TODO Auto-generated method stub
		return queryForList("DATA.getUsingSimcard", null);		
	}
	
	public List<zona> getZona() {
		// TODO Auto-generated method stub
		return queryForList("DATA.getZona", null);		
	}
	
	
	public void insertPrepagoPlat(PlanPrepago pl) {
		insert("DATA.insertPrepagoPlat", pl);
	}	
	
	@SuppressWarnings("unchecked")
	public List<carac_elegidos> getCaractFNF() {
		return queryForList("DATA.getCaractFNF", null);		
	}
	
	@SuppressWarnings("unchecked")
	public List<carac_elegidos> getCaractBFNF() {
		return queryForList("DATA.getCaractBFNF", null);		
	}
	
	/*Iniciacion de metodos Formulario CCRequerimientos*/
	
	public void insertCC_req(CcRequerimientos ccr) {
		// TODO Auto-generated method stub
		insert("DATA.insertCC_req",ccr);		
	}	
	
	@SuppressWarnings("unchecked")
	public List<CcRequerimientos> getNum_req() {
		return queryForList("DATA.getNum_req", null);		
	}
	
	/* ELiminar CcRequerimientos*/
	public void EliminarReq (Integer VALUE){
		delete("DATA.EliminarReq",VALUE);		
	}
	
		/* Actualizar CcRequerimientos*/
	public void UpdateReq(CcRequerimientos a) {
		update("DATA.UpdateReq", a);		
	}
	
	@SuppressWarnings("unchecked")
	public List<CcRequerimientos> getBuscarReq(CcRequerimientos b){
		return queryForList("DATA.getNum_req", b);
	}
	
}
