package co.com.comcel.daopo;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;

import co.com.comcel.vopo.Componetes;
import co.com.comcel.vopo.DatosGenerales;
import co.com.comcel.vopo.POCodNum;
import co.com.comcel.vopo.paquetes;
import co.com.comcel.vopo.planBasico;
import co.com.comcel.vopo.productOffering;

public class DataDAO extends SqlMapDaoTemplate implements IDataDAO{
	private static final Logger log = Logger.getLogger("alarm");
	public DataDAO(DaoManager daoManager) {
		super(daoManager);
	}
	public Collection<planBasico> getPlanesBasicos()throws DaoException{
		try {
			@SuppressWarnings("unchecked")
			Collection<planBasico> list = queryForList("DATA.getPlanesBasicos", null);	
		return list;
		} catch(DaoException e) {
			log.error("Obteniendo registros por estado telefonia ",e);
			throw e;
		}
	}
	public Collection<productOffering> getProductOffering()throws DaoException{
		try {
		@SuppressWarnings("unchecked")
		Collection<productOffering> list1 = queryForList("DATA.getProductOffering", null);
	return list1;
		  } catch(DaoException e) {
			log.error("Obteniendo registros por estado telefonia ",e);
			throw e;
		  }
	  }
	
	public List<Componetes> getComponentes()throws DaoException{
		try {
	
		@SuppressWarnings("unchecked")
		List<Componetes> list2 = queryForList("DATA.getComponentes", null);
	return list2;} catch(DaoException e) {
		log.error("No se puede obtener informacion de Componentes ",e);
		throw e;
	   }
	}
	
	public List<paquetes> getPaquetes()throws DaoException{
		try {
		@SuppressWarnings("unchecked")
		List<paquetes> list3 = queryForList("DATA.getPaquetes", null);
		return list3;} catch(DaoException e) {
			log.error("No se puede obtener informacion de Consulta a Paquetes",e);
			throw e;
		}
	}
   public List<planBasico> consultaPLanBasico(planBasico planB) throws DaoException{
	   try {
	   @SuppressWarnings("unchecked")
	List<planBasico> resPlanB = queryForList("DATA.getConsultaPB", planB);
       return resPlanB;} catch(DaoException e) {
			log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
			throw e;
		}
   }
   public List<planBasico> consultaPLanBasicoMuchos(planBasico planB) throws DaoException{
	   try {
	   @SuppressWarnings("unchecked")
	List<planBasico> resPlanBM = queryForList("DATA.getConsultaPBMuchos", planB);
       return resPlanBM;} catch(DaoException e) {
			log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
			throw e;
		}
   }
   //--------------------------  Se agregó este codigo 27/03/2012 -----------------------------------------------
   public List<POCodNum> consultaPlanAdicional(String dato) throws DaoException{
	   try {
	   @SuppressWarnings("unchecked")
	List<POCodNum> resPlanB = queryForList("DATA.getConsultaPA", dato);
       return resPlanB;} catch(DaoException e) {
			log.error("No se puede obtener informacion de Consulta de bloqueo  a Planes - Adicionales ",e);
			throw e;
		}
   }      
   //............................... Termina codigoo adicionado  .............................................
   public List<planBasico> consultaPLanPaq(planBasico planB) throws DaoException{
	   try {
	   @SuppressWarnings("unchecked")
	List<planBasico> resPlanPaq = queryForList("DATA.getConsultaPaq", planB);
       return resPlanPaq;} catch(DaoException e){
			log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
			throw e;
			}
   }
   public List<POCodNum> consultaPLanPaq(POCodNum planB) throws DaoException{
	   try {
	   @SuppressWarnings("unchecked")
	List<POCodNum> resPlanPaq = queryForList("DATA.getConsultaPaq", planB);
       return resPlanPaq;} catch(DaoException e){
			log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
			throw e;
			}
   }
   
   
   public List<productOffering> getBOColumna() throws DaoException{
	   try {
		   @SuppressWarnings("unchecked")
		List<productOffering> BoCOL = queryForList("DATA.getBOColumna", null);
	       return BoCOL;} catch(DaoException e){
				log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
				throw e;
		}
	   
   }
   
   public List<productOffering> getBO() throws DaoException{
	   try {
		   @SuppressWarnings("unchecked")
		List<productOffering> BO = queryForList("DATA.getBO", null);
	       return BO;} catch(DaoException e){
				log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
				throw e;
		}
	   
   }
   
   public List<productOffering> getBO1() throws DaoException{
	   try {
		   @SuppressWarnings("unchecked")
		List<productOffering> BO = queryForList("DATA.getBO1", null);
	       return BO;} catch(DaoException e){
				log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
				throw e;
		}
	   
   }
   
   
   
   public List<paquetes> getPaqTodos() throws DaoException{
	   try {
		   @SuppressWarnings("unchecked")
		List<paquetes> BO = queryForList("DATA.getPaqTodos", null);
	       return BO;} catch(DaoException e){
				log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
				throw e;
		}
	   
   }
   
   public List<DatosGenerales> getDatosGenerales() throws DaoException{
	   try {
		   @SuppressWarnings("unchecked")
		List<DatosGenerales> BO = queryForList("DATA.getDatosGenerales", null);
	       return BO;} catch(DaoException e){
				log.error("No se puede obtener informacion de Consulta a Planes Basicos ",e);
				throw e;
		}
	   
   }
@SuppressWarnings("unchecked")
@Override
public List<String> existplanMaestra() throws DaoException {
	try {	
		   return  queryForList("DATA.existplanMaestra", null);} catch(DaoException e){
				log.error("No se puede obtener informacion de Consulta a planes en maestra ",e);
				throw e;
		}

	
}
@Override
public List<String> existpaqMaestra() throws DaoException {
	try {	
		   return  queryForList("DATA.existpaqMaestra", null);} catch(DaoException e){
				log.error("No se puede obtener informacion de Consulta a paquetes en maestra ",e);
				throw e;
		}
}

   
}
