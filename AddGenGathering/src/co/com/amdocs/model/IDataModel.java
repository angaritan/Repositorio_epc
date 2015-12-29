package co.com.amdocs.model;

import java.util.List;

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

public interface IDataModel {
	
	public List<canales_venta> getCanalesVenta();
	public List<oferta_amdocs> getPlanEquiv(String tmeq);
	public List<oferta_amdocs> getPlanEquiv2(oferta_amdocs om);
	public List<PRODUCT_TYPE> getProductType();
	public List<PRODUCT_TYPE> getTecnologia();
	public List<Tipo_oferta> getTipoOferta();
	public List<Ofertas_demanda> getOfertasDemanda();
	public List<PlanPrepago> getPlanPrepago();
	public List<Plan> getPlan(String tmcode);
	public void insertPlan(Plan plan);
	public void insertPlanTemp(Plan pl);
	public List<usingSimcard> getUsingSimcard();
	public List<zona> getZona();
	public void insertPrepagoPlat(PlanPrepago pl);
	public List<carac_elegidos> getCaractFNF();
	public List<carac_elegidos> getCaractBFNF();
	/*Info Buscar CCR*/
	public List<CcRequerimientos> getBuscarReq(CcRequerimientos b);
	/*Info Eliminar CCR*/
	public void EliminarReq(Integer VALUE);
	/*Info Actualizar CCR*/
	public void UpdateReq(CcRequerimientos a);
	public List<CcRequerimientos> getNum_req();
	public void insertCC_req(CcRequerimientos ccr);
	
	
}
