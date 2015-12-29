package co.com.comcel.modelpo;

import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.ibatis.dao.client.DaoException;

import co.com.comcel.vopo.Componetes;
import co.com.comcel.vopo.DatosGenerales;
import co.com.comcel.vopo.POCodNum;
import co.com.comcel.vopo.paquetes;
import co.com.comcel.vopo.planBasico;
import co.com.comcel.vopo.productOffering;


public interface IDataModel {

	public Collection<planBasico> getPlanesBasicos()throws DaoException;
	public Boolean defExisCompPO(planBasico pb, String sncode);
	public Boolean defExisCompPO(String tmcode, String sncode);
	
	public List<POCodNum>  defExisCompPA(String sncode);
	//se utiliza
	public List<DatosGenerales> getDatosGenerales() throws DaoException;
	//public Boolean defExistPaqPO(productOffering PO);
	public Boolean defExistPaqPO(POCodNum PO);
	public List<POCodNum> defExistPaqPo(POCodNum PO);
	//public Boolean generarPO(productOffering PO, int id_PO , Componetes comp );
	public List<planBasico> consultaPLanBasico(planBasico p) throws DaoException;
	public Collection<productOffering> getProductOffering()throws DaoException;
	
	//se utiliza
	public List<productOffering>  getBOColumna() throws DaoException;
	public List<Componetes> getComponentes()throws DaoException;
	public List<Componetes> getCompTotal()throws DaoException;
	//se utiliza
	public List<productOffering>  getBO() throws DaoException;
	public List<productOffering> getBO1() throws DaoException;
	public List<paquetes> getPaqTodos()throws DaoException ;
	public void llenarDatosDefCardComp(String PO, Componetes comp, Cell cell, Row row);
	public List<String> existplanMaestra() throws DaoException;
	public List<String> existpaqMaestra() throws DaoException;
	
}