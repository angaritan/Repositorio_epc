package co.com.comcel.modelpo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;

import co.com.comcel.daopo.DaoConfig;
import co.com.comcel.daopo.IDataDAO;
import co.com.comcel.vopo.Componetes;
import co.com.comcel.vopo.DatosGenerales;
import co.com.comcel.vopo.POCodNum;
import co.com.comcel.vopo.paquetes;
import co.com.comcel.vopo.planBasico;
import co.com.comcel.vopo.productOffering;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;

public class DataModel implements IDataModel{
	
	private DaoManager daoManager;
	private IDataDAO dataDao;
	private static final Logger log = Logger.getLogger(DataModel.class.getName());
	public DataModel() {
		this.daoManager = DaoConfig.getDaoManager();
		this.dataDao = (IDataDAO)daoManager.getDao(IDataDAO.class);	
			}
	
	public Collection<planBasico> getPlanesBasicos()throws DaoException{	
	  return dataDao.getPlanesBasicos() ;
	}
	
	public List<planBasico> consultaPLanBasico(planBasico p) throws DaoException{
	    return dataDao.consultaPLanBasico(p);	
	}
	
	public Collection<productOffering> getProductOffering()throws DaoException {
		return dataDao.getProductOffering();
	}
	
	public List<Componetes> getComponentes()throws DaoException {
		/*** Se agrego el siguiente codigo...  	
		List<Componetes> comp = dataDao.getComponentes();
		List<Componetes> todos = new ArrayList<Componetes>();		
		for(Componetes comp_h:comp){
			for(Componetes comp_p:comp ){
					if(comp_h.getCOMPONENT1()!=null){ 
						if(comp_h.getCOMPONENT1().trim().equals(comp_p.getDESCRIPCION().trim())){
							comp_p.setOrden(1);
							comp_h.getCom_padres().add(comp_p);
					     }
					  }
					if(comp_h.getCOMPONENT2()!=null){ 
						if(comp_h.getCOMPONENT2().trim().equals(comp_p.getDESCRIPCION().trim())){
							comp_p.setOrden(2);
							comp_h.getCom_padres().add(comp_p);				
						}
					  }
					if(comp_h.getCOMPONENT3()!=null){ 
						if(comp_h.getCOMPONENT3().trim().equals(comp_p.getDESCRIPCION().trim())){
							comp_p.setOrden(3);
							comp_h.getCom_padres().add(comp_p);					
						}
					 }				
				}
			 todos.add(comp_h);
			} 	
		return 	todos;	 **/
    /*** hasta aca llega el codigo agragado para nueva consideracion....***/		
    return dataDao.getComponentes();
	}
		
  	public Boolean defExisCompPO(planBasico pb, String sncode){
    String  codPlan = ""; 
    int count =0;
    //String tmcode = PO;
  	//String sncode = comp.getSNCODE();
   	//planBasico pb = new planBasico();
   	//pb.setSncode(sncode);
    //pb.setTmcode(tmcode); 
   	Boolean flag = false;
  	
  if(sncode.trim().equals("1")){
	  
	  System.out.println("pasa por muchos "+ pb.getSncode());
    List<planBasico> lpb = dataDao.consultaPLanBasicoMuchos(pb);
	 if(lpb.isEmpty()){		 
  				flag = false;  		  	 
  		  	}
  		  	else {		flag = true;  		  		
		  		  		for(planBasico a: lpb){
		  		  		    codPlan =  a.getTmcode();
		  		  	  		count = count + 1;}  	  		  	 	
  		     	}
  		    }
  	else {	 System.out.println("pasa por uno"+ " snocode: "+ sncode);
	  		List<planBasico> lpb =  dataDao.consultaPLanBasico(pb);
	  			if(lpb.isEmpty()){
	  				flag = false;
		 		  	}
	 		  	else { 
	 		  		flag = true;
	 		  		for(planBasico a: lpb){
	 		  			codPlan = a.getTmcode();
	 		  	  		count = count + 1;
	 		  							  }  	 		  	 	
	 		     	} 	  	    
  		   }
  	 return flag;
   }
  	//**************************** Metodo utilizado hasta el momento******************************************************
  	public Boolean defExisCompPO(String tmcode, String sncode){
  		planBasico pb = new planBasico();
  	    @SuppressWarnings("unused")
		String  codPlan = ""; 
  	    int count =0;
  	    Boolean flag = false;
  	  	pb.setSncode(sncode);
  	  	pb.setTmcode(tmcode);
  	  if(sncode.trim().contains(",")){ 		  
  	  List<planBasico> lpb = dataDao.consultaPLanBasicoMuchos(pb);
  	  System.out.println(  "pasa por muchos,  sn: " + sncode + " tm: " + tmcode);
  		       if(lpb.isEmpty()){
  			   	  				flag = false;  		  	 
  	  		  					}
	  	  	   else {	
	  	  		   				flag = true;  	
	  	  		   			
	  			  		  		for(planBasico a: lpb){
	  			  		  		    codPlan =  a.getTmcode();
	  			  		  	  		count = count + 1;}  	  		  	 	
  	  		     	}
  		       System.out.println("Flag muchos: "+ flag);
  		     
  	  		    }
  	  	else {	
  	  	//System.out.println(  "pasa por uno,  sn: " + sncode + " tm: " + tmcode);
  		  		List<planBasico> lpb =  dataDao.consultaPLanBasico(pb);
  		  			if(lpb.isEmpty()){
  		  				flag = false;
  			 		  	}
  		 		  	else { 
  		 		  		flag = true;
  		 		  		for(planBasico a: lpb){
  		 		  			codPlan = a.getTmcode();
  		 		  	  		count = count + 1;
  		 		  							  }  	 		  	 	
  		 		     	} 
  		  		 //System.out.println("Flag uno: "+ flag);
  	  		   }
  	  	 return flag;
  	   } 
  	
  	/**  --------- Metodo Agregado 27/03/2012 ----------------      **/
  	public List<POCodNum> defExisCompPA(String sncode){
  		List<POCodNum> lpa =  dataDao.consultaPlanAdicional(sncode);
  		return lpa;
  	   }    	
  	//**********************************************************************************
  	public void llenarDatosDefCardComp(String PO, Componetes comp, Cell cell, Row row){
  		planBasico pb = new planBasico();
  		Boolean flagBloq = false;
  		Boolean flagB = false;  		
  		pb.setTmcode(PO); 
  	   	String sncode = comp.getSNCODE().trim();
  	   	if(sncode.trim().equals("1"))  pb.setSncode(comp.getOBSERVACION());
  	   	else pb.setSncode(comp.getSNCODE());
  	   //	System.out.println("Sncode en llenar datos : "+ pb.getSncode()+ " sncode valor: " + sncode );
  	   	if(comp.getBLOQUEO().trim().equals("0")){
  	   	
  	   	    flagB = defExisCompPO(pb,  sncode);
  	      	if(flagB == true) llenarCard( cell, row,  "0", "1", "1" );  	   		
	  	   	else llenarCard( cell, row,  "0", "1", "0" );   	   		
  	   	}        
  	   	else{
  	   	    flagB = defExisCompPO(pb,  sncode);
  	   		 pb.setSncode(comp.getBLOQUEO().trim());
  	   	    //Ojo con esta sentencia -----ya que para bloqueo es un solo servicio 
  	   		sncode = "0";
  	   		flagBloq = defExisCompPO(pb,  sncode);   	   	    
  	   		
  	   		if(flagBloq == true && flagB == false) llenarCard( cell, row,  "0", "0", "0" );   	
		  	
  	   		if(flagBloq == false){
		  	   			if(flagB == true) llenarCard( cell, row,  "0", "1", "1" );
		  	   			else 			  llenarCard( cell, row,  "0", "1", "0" ); 
		  	   				     }
  	   	    if(flagBloq == true && flagB == true) llenarCard( cell, row,  "?", "?", "?" );   		  	   		
  	   	}  		
  	}
  	public void llenarCard(Cell cell,Row row, String a, String b, String c ){
  		
  		int index = cell.getColumnIndex();
  		cell.setCellValue(a);
  		index++;
  		cell = row.getCell(index);
  		if(cell ==null) cell = row.createCell(index);
  		cell.setCellValue(b);
  		index++;
  		cell = row.getCell(index);
  		if(cell ==null) cell = row.createCell(index);
  		cell.setCellValue(c);
  	}
  		
  	public Boolean defExistPaqPO(POCodNum PO){
  		Boolean flag = false;
  		int  codPlan = 0; 
  	    int count =0;	
  	    int tmcode = PO.getTmcode();
  	    int spcode = PO.getSpcode();
  	    POCodNum pb1 =new  POCodNum();
  	    pb1.setSpcode(spcode);
     	pb1.setTmcode(tmcode); 
  	    List<POCodNum> lpb = dataDao.consultaPLanPaq(pb1);
  	    if(lpb.isEmpty()){
  	    	System.out.println("false");
			flag = false;  		  	 
	  	}
	  	else { System.out.println("true");
	  		flag = true;	  		
	  		for(POCodNum a: lpb){
	  		    codPlan =  a.getTmcode();
	  	  		count = count + 1;
	  		}  	  	  	 	
	  	}  	
  	    return flag;
  	}
  	////-----------------------------------NUEVO METODO PARA DEFINIR EXISTENCIA PAQ EN PLAN----------------
  	public List<POCodNum> defExistPaqPo(POCodNum PO){ 		
  	    POCodNum pb1 =new  POCodNum();
  	    pb1.setSpcode(PO.getSpcode());
     	pb1.setTmcode(PO.getTmcode()); 
  	    List<POCodNum> lpb = dataDao.consultaPLanPaq(pb1);
  	    return lpb;
  	}
  	
  	
  	public List<DatosGenerales> getDatosGenerales() throws DaoException{
  		return dataDao.getDatosGenerales();
  	}
  	
  	  	
  	public List<productOffering>  getBOColumna() throws DaoException {
		return dataDao.getBOColumna();
	}
  	public List<productOffering>  getBO() throws DaoException {
		return dataDao.getBO();
	}
  	public List<productOffering>  getBO1() throws DaoException {
		return dataDao.getBO1();
	}
  	public List<paquetes> getPaqTodos()throws DaoException {
  		return dataDao.getPaqTodos();
  	}
  	public List<Componetes> getCompTotal()throws DaoException{
  		IDataModel model = new DataModel();	 
  		List<Componetes>  total = new ArrayList<Componetes>();
  		List<Componetes>  comp= model.getComponentes();
  		for(Componetes inicio : comp ){
			POCodNum com_padre = new POCodNum();
				for(Componetes fin: comp){	
						    	if(inicio.getCOMPONENT1()!=null){								 		
								 	if(inicio.getCOMPONENT1().trim().equals(fin.getDESCRIPCION().trim())&& !fin.getBLOQUEO().trim().equals("0")){
								 			 	com_padre.setTipo("1");	
								 			 	com_padre.setSncode(Integer.parseInt(fin.getBLOQUEO()));
								 				inicio.getPadres_comp().add(com_padre);						 																
																									
										     }  }
								 	 if(inicio.getCOMPONENT2()!=null){ 
										     if(inicio.getCOMPONENT2().trim().equals(fin.getDESCRIPCION().trim())&& !fin.getBLOQUEO().trim().equals("0") ){
										    	com_padre.setTipo("2");	
									 			com_padre.setSncode(Integer.parseInt(fin.getBLOQUEO()));
									 		    inicio.getPadres_comp().add(com_padre);
									 		}  }
									if(inicio.getCOMPONENT3()!=null){    
										      if(inicio.getCOMPONENT3().trim().equals(fin.getDESCRIPCION().trim())&& !fin.getBLOQUEO().trim().equals("0") ){
											    com_padre.setSncode(Integer.parseInt(inicio.getBLOQUEO()));
											    com_padre.setTipo("3");	
											    inicio.getPadres_comp().add(com_padre);
									 		 }	}
								    if(inicio.getCOMPONENT1()==null && inicio.getCOMPONENT2()==null && inicio.getCOMPONENT3()==null ) {   
										       	com_padre.setSncode(0);
										    	com_padre.setTipo("N");	
										    	inicio.getPadres_comp().add(com_padre);									  						
									     }					          
							}
				    total.add(inicio);
						
			}		
  		
  		
  		//............................................................................
  		
  		return total;
  	}
  	public List<String> existplanMaestra() throws DaoException{  	  		
  			return dataDao.existplanMaestra();
  	}
  	public List<String> existpaqMaestra() throws DaoException{  	  		
			return dataDao.existpaqMaestra();
	}
  	
  	
  		
}
