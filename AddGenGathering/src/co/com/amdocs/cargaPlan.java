package co.com.amdocs;

import java.util.ArrayList;
import java.util.List;

import co.com.amdocs.model.DataModel;
import co.com.amdocs.model.IDataModel;
import co.com.amdocs.vo.Plan;
import co.com.amdocs.vo.oferta_amdocs;

public class cargaPlan {
	
	private oferta_amdocs OfertAmdocs;
	private String tmcode;
	IDataModel mod = new DataModel();
	private Plan pp;
	private Plan ppequ;
	private List<Plan> p = new ArrayList<Plan>();
	
	
	public  cargaPlan(String tmcodeq, oferta_amdocs of, boolean plancop){	
		OfertAmdocs = of;
		tmcode = tmcodeq;
		cargaProp();
		mod.insertPlan(pp);	
		mod.insertPlanTemp(pp);
		System.out.println("Valor para cambiar vigencia oferta:"+ plancop);
		if(plancop){	
			ppequ.setVIGENTE("N");
			mod.insertPlanTemp(ppequ);
		}
				
	}	
	private void cargaProp(){
		p = mod.getPlan(tmcode);
		
		Plan plan = new Plan();
		
		String[] seg = segmentar(OfertAmdocs.getDescripcion());
		String[] segAnot= segmentar(OfertAmdocs.getANOTACION_AD());
		for(Plan pl:p){						
			    plan.setPRODUCT_TYPE(OfertAmdocs.getProduct_type());
				System.out.println(pl.getCARACT_ELEG()+ " "+pl.getPLANPREPAGO());
				plan.setUSINGSIMCARDPREPAG1(OfertAmdocs.getUsingsimcardprepag1());
				plan.setTECNOLOGIA(OfertAmdocs.getTecnologia());
				plan.setPLANPREPAGO(OfertAmdocs.getPlanprepago());
				plan.setMIGRACION("P");
				plan.setFECHA_INICIO(OfertAmdocs.getFecha_inicio());
				plan.setTMCODE(OfertAmdocs.getTmcode());
				plan.setSPCODE(OfertAmdocs.getSpcode());
				plan.setPAQUETE(OfertAmdocs.getPaquete());
				plan.setPLAN(OfertAmdocs.getPlan());
				plan.setPLAN_DOX(OfertAmdocs.getPlan_dox());
				plan.setIVA(OfertAmdocs.getIva());
				if(OfertAmdocs.getVigente().equals("SI"))
					 plan.setVIGENTE("S");
				else plan.setVIGENTE("N");					
				if(OfertAmdocs.getTipo_plan().equals("VENTA")){
					plan.setOFER_CAMBIOPLAN("N");
					plan.setOFER_VENTA("S");					
				}else if(OfertAmdocs.getTipo_plan().equals("CAMBIO PLAN")){
					plan.setOFER_CAMBIOPLAN("S");
					plan.setOFER_VENTA("N");					
				}else{plan.setOFER_CAMBIOPLAN("S");
					plan.setOFER_VENTA("S");	}				
				plan.setAMSS(pl.getAMSS());
				plan.setBB_QOS_ACT(pl.getBB_QOS_ACT());
				plan.setBB_QOS_CONTROL(pl.getBB_QOS_CONTROL());
				if(pl.getNOMBRE_APROV()!=null) plan.setNOMBRE_APROV(pl.getNOMBRE_APROV());
				else plan.setNOMBRE_APROV("N/A");
				plan.setCID(pl.getCID());
				plan.setTIPOPLAN(pl.getTIPOPLAN());
		        plan.setSEGMENTO(pl.getSEGMENTO());
		        plan.setCFM(Double.valueOf(0));
		        plan.setTIPOBUZON(pl.getTIPOBUZON());
		        plan.setCARACT_PLAN(pl.getCARACT_PLAN());
		        plan.setQA_SERVICIO(pl.getQA_SERVICIO());
		        plan.setDPI_INICIAL(pl.getDPI_INICIAL());
		        plan.setDPI_CONTROL(pl.getDPI_CONTROL());
		        plan.setRC_ACT(pl.getRC_ACT());
		        plan.setRC_CONTROL(pl.getRC_CONTROL());
		        plan.setGAMA_OCC(pl.getGAMA_OCC());
		        plan.setGAMA_COS(pl.getGAMA_COS());
		        plan.setGAMA_ORI(pl.getGAMA_ORI());
		        plan.setBB_QOS_ACT(pl.getBB_QOS_ACT());
		        plan.setBB_QOS_CONTROL(pl.getBB_QOS_CONTROL());
		        plan.setDESCRIPCION1(seg[0]);
		        plan.setDESCRIPCION2(seg[1]);
		        plan.setDESCRIPCION3(seg[2]);
		        plan.setDESCRIPCION4(seg[3]); 
		        plan.setDESCRIPCION5(seg[4]);
		        plan.setDESCRIPCION6(seg[5]);
		        plan.setDESCRIPCION7(seg[6]); 
		        plan.setDESCRIPCION8(seg[7]);
		        plan.setDESCRIPCION(OfertAmdocs.getDescripcion().trim());
		        plan.setPERCENTAGE_FOR_DOWNGRADE(pl.getPERCENTAGE_FOR_DOWNGRADE());
		        plan.setEQUIPO(pl.getEQUIPO());
		        plan.setSUBTYPE(pl.getSUBTYPE());
		        plan.setAMSS(pl.getAMSS());
		        plan.setOMS(pl.getOMS());
		        plan.setQOS_CONTROL(pl.getQOS_CONTROL());
		        plan.setZONA(pl.getZONA());
		       // plan.setPRODUCT_TYPE(pl.getPRODUCT_TYPE());
		        plan.setLTE_QOS_ACT(pl.getLTE_QOS_ACT()); 
		        plan.setLTE_QOS_CONTROL(pl.getLTE_QOS_CONTROL()); 
		        plan.setPCRF_CODE(pl.getPCRF_CODE());
		        plan.setQCI(pl.getQCI()); 
		        plan.setUPLINK_SPEED(pl.getUPLINK_SPEED());
		        plan.setDOWNLINK_SPEED(pl.getDOWNLINK_SPEED()); 
		       // plan.setTECNOLOGIA(pl.getTECNOLOGIA());
		        plan.setEFFECTIVE_DATE(pl.getEFFECTIVE_DATE()); 
		        //plan.setUSINGSIMCARDPREPAG(pl.getUSINGSIMCARDPREPAG());
		        plan.setANOTACION_AD(segAnot[0]);
		        plan.setANOTACION_AD1(segAnot[1]);
		        plan.setANOTACION_AD2(segAnot[2]);
		        plan.setANOTACION_AD3(segAnot[3]);
		        plan.setANOTACION_AD4(segAnot[4]);
		        plan.setANOTACION_AD5(segAnot[5]);
		        plan.setANOTACION_AD6(segAnot[6]);
		        plan.setANOTACION_AD7(segAnot[7]);
		        if(OfertAmdocs.getCANALVENTA()!=null||!OfertAmdocs.getCANALVENTA().equals("") ||!OfertAmdocs.getCANALVENTA().contains("TODOS"))
		        	plan.setCANALVENTA(OfertAmdocs.getCANALVENTA());		        	
		        else plan.setCANALVENTA("TODOS");
		        plan.setRECORD_MIN(pl.getRECORD_MIN());
		        plan.setCAMB_MSISDN(pl.getCAMB_MSISDN());
		        plan.setCAMB_PLPRE(pl.getCAMB_PLPRE());
		        plan.setREPO_EQUIPO(pl.getREPO_EQUIPO());
		        pp =plan;
		        ppequ = pl;
		        }
		
}   
	  public String [] segmentar(String texto){
		  	String DESC[] = new String[10] ;
		  	for(int l=0; l<10;l++){
		  		DESC[l]= "";
		  	}
			String des = "";
			String des1 = "";
			String des2 = "";
			int colres = 0;
			int ext = 0;
		    int res = 0;
		    int div = 0;		  
		  des = texto;
		  ext =des.length();
		  res = ext%200;
		  div = ext/200;
		  colres = div+1;
				  if(res>0 && div>=1 ){
			   for(int l = 0; l<div;l++){
				   des1 = des.substring(l*200,l*200+200);								  
				   DESC[l]= des1;	
					}											 
				   des2 = des.substring(div*200, div*200+res);
	               DESC[colres-1]= des2;	
	               
		  			}
		  else if(res>0 && (div >=0 && div<1)){
			       des2 = des.substring(div*200, div+res);
			       DESC[1]= des2; 
			       }		
		  else if(res==0 && div>=1){
	             for(int l = 0; l<div;l++){
					des1 = des.substring(l*200,l*200+200);
					DESC[l+1]= des1; 	
					}								
						}
		  else{	 DESC[1]= "N/A";}
		  
		  return DESC;  
		  
	  }
	  
	  
		  
		  
		  
		  
	 

}
