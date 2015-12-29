package co.com.comcel.daopo;

import java.util.Collection;
import java.util.List;

import com.ibatis.dao.client.DaoException;

import co.com.comcel.vopo.Componetes;
import co.com.comcel.vopo.DatosGenerales;
import co.com.comcel.vopo.POCodNum;
import co.com.comcel.vopo.paquetes;
import co.com.comcel.vopo.planBasico;
import co.com.comcel.vopo.productOffering;


public interface IDataDAO {

	public Collection<planBasico> getPlanesBasicos() throws DaoException;
	public Collection<productOffering> getProductOffering() throws DaoException;
	public List<Componetes> getComponentes() throws DaoException;
	public List<paquetes> getPaquetes() throws DaoException;
	
	public List<planBasico> consultaPLanBasico(planBasico planB);
	public List<planBasico> consultaPLanBasicoMuchos(planBasico planB);
	
	//-----------   Sea agregó codigo-- 27/03/2012---------------------------------------
	public List<POCodNum> consultaPlanAdicional(String dato) throws DaoException;
	
	//-----------   Termina codigo-- 27/03/2012---------------------------------------
	
	public List<planBasico> consultaPLanPaq(planBasico planB);
	public List<POCodNum> consultaPLanPaq(POCodNum planB) throws DaoException;
    
	public List<productOffering> getBOColumna() throws DaoException;
    public List<productOffering> getBO() throws DaoException;
    public List<productOffering> getBO1() throws DaoException;
    public List<paquetes> getPaqTodos() throws DaoException;
    public List<DatosGenerales> getDatosGenerales() throws DaoException;
    public List<String> existplanMaestra() throws DaoException;
	public List<String> existpaqMaestra();
    
}
