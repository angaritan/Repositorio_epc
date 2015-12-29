package co.com.comcel.mainpo;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import co.com.comcel.modelpo.DataModel;
import co.com.comcel.modelpo.IDataModel;
import co.com.comcel.vopo.DatosGenerales;
import co.com.comcel.vopo.productOffering;

public class GenPO
 {    
	//Adicionales para el archivo de PSheet Componentes...//
	//private static int regInicio1 = 22;
	private static int regInicio1 = 9;
	//....Adicion de nuevas BO con diferentes componentes.
    private final static int regWM_PB = 1;
    private final static int regPB_VMail = 1; 
	private final static int regVMail_CallID = 0; 
	private final static int regCallID_Inter = 12;
	private final static int regInter_WAP = 0; 
	private final static int regWAP_NM = 0; 
	private final static int regNM_BB = 3; 
	private final static int regBB_ChPW = 2;
	private final static int regChPW_Syn = 1;
	private final static int regSyn_SMS = 1;
	//private final static int regSMS_RBT = 7;	
	private final static int regSMS_WMess = 3;
	private final static int regWMess_SMail = 1;
	private final static int regSMail_RBT = 3;
	private final static int regRBT_Casc =0;	
	private final static int regCasc_VToV = 0;
	private final static int regVToV_Legis = 0;	
	private final static int regLegis_WEBTV = 1;
	private final static int regWEBTV_Insu = 0;	
	private final static int regInsu_LDI = 5;	
	private final static int regLDI_Equip = 29;
	private final static int regEqui_SIMCard = 2;
	private final static int regSIMCard_EquiCommit =0;
	private final static int regEquiCommit_PlanCommit = 0;
	private final static int regPlanCommit_VOnnetW = 7;
	private final static int regVOnnetW_VOnnetF= 0; 	
	private final static int regVOnnetF_1FnF = 4;
	private final static int reg1FnF_3FnF = 0;
	private final static int reg3FnF_4FnF = 0;
	private final static int reg4FnF_SMSOnnet = 21;
	private final static int regSMSonnet_VC = 5;
	private final static int regVC_ReMIN = 6;
	private final static int regReMIN_Pasa = 0;
	//private final static int regPasa_Welc = 1;
	private final static int regPasa_FulFill = 0;
	private final static int reFulFill_Welc = 0;
	private final static int regWelc_6FnF = 1;
	private final static int reg6FnF_9FnF = 0;
	private final static int reg9FnF_TelDir= 0;
	private final static int cantRow = 143;
	private static boolean tipoPlanprep = false;
	private static int contFnFVFijo;
	private static int contWindowsMob;
	private static int contPlanCommit;
	private static int contEquiCommit;
	private static int contNokia;
	private static int contWEBTV;
	private static int contWAP;
	private static int contBuzonVoz;
	private static int cont0OnnetFnF;
	private static int contSimPrep;
	private static int contRecMIN;
	private static int contWMess;
	private static int contSMail;
	private static int contFulFill;
	private static int contTelDir;
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {	
	String archivoEntr = "D://EntradasEPC/AMX EPC Wireless Product Offering.xlsx";
	String archivoSal = "D://SalidasEPC/AMX EPC Wireless Product Offering.xlsx";
	
    //----------------  Proceso de diligenciamiento de Hoja POtoPO de el formato de Product Offering---------------
/*	InputStream cargaLibro = new FileInputStream(archivo);   
	Workbook wb = WorkbookFactory.create(cargaLibro);		
	System.out.println("**INICIANDO**"); 
	llenarPOtoPO(wb);
	FileOutputStream fileOut = new FileOutputStream(archivo);   
	wb.write(fileOut); 
	fileOut.close();  
	System.out.println("**INICIANDO**");*/		
	System.out.println("**INICIADO**" + new Date());		
    HashMap<String, Integer> mapBO = obtenerNumCols("General Info");
   //---------------  Proceso de diligenciamiento de PO en hoja - General Info -------------------------------------
	InputStream  cargaLibro1 = new FileInputStream(archivoEntr);   
	Workbook wb1 = WorkbookFactory.create(cargaLibro1); 
	String tipo = JOptionPane.showInputDialog("TIPO DE PLAN \n Digite 1 para Prepago Incremental. \n Digite 2 para Prepago por Fase. \n Digite 3 para Postpago.").trim();	
	if(tipo.equals("1")||tipo.equals("2")){
		tipoPlanprep = true;
	    LLenarGenInfoPlanesPrepago(wb1, mapBO);
	   	    }
	else if(tipo.equals("3")){
		LLenarDatosGenerales(wb1);
		 	}
	else JOptionPane.showMessageDialog(null, "ERROR","No es valida esa opcion", JOptionPane.INFORMATION_MESSAGE);
	System.out.println("**FINALIZADO1**");
	FileOutputStream fileOut1 = new FileOutputStream(archivoSal);   
	wb1.write(fileOut1); 
	fileOut1.close(); 
	new XmlWriting(tipo);
	System.out.println("**FINALIZADO2**");
	
	///-------------- Proceso de Llenar ProductSpec-----------------------------------------------------------------	
   /* archivo = "D:\\AMX EPC Wireless Product Spec.xlsx";
	Hashtable<String, Integer> filas = insertarFilas(archivo);	
	InputStream cargaLibro2 = new FileInputStream(archivo);   
    Workbook wb2 = WorkbookFactory.create(cargaLibro2); 
	System.out.println("**INICIANDO3**");
	System.out.println("filas: "+(Integer)filas.get("BasePlan"));
	LLenarDGPaq( wb2, filas,"DetalleLlamada","0");
	LLenarPE(wb2, (Integer)filas.get("BasePlan"));
	LLenarDGPaq( wb2, filas,"BuzonVoz","0");
	LLenarDGPaq( wb2, filas,"IdLlamada","0");
    LLenarDGPaq( wb2, filas,"Internet","0");
    LLenarDGPaq( wb2, filas,"WAP","0");
    LLenarDGPaq( wb2, filas,"NokiaMessaging","0");
   	LLenarDGPaq( wb2, filas,"Blackberry","0");
	LLenarDGPaq( wb2, filas,"ChatPack","0");
	LLenarDGPaq( wb2, filas,"Synchronica","0");
	LLenarDGPaq( wb2, filas,"SMS", "0");
	LLenarDGPaq( wb2, filas,"Web Message", "0");
	LLenarDGPaq( wb2, filas,"Short Mail", "0");
	LLenarDGPaq( wb2, filas,"RingBackTone", "0");
	LLenarDGPaq( wb2, filas,"WEBTV", "0");
	LLenarDGPaq( wb2, filas,"Cascada","0");
	LLenarDGPaq( wb2, filas,"VozAVoz","0");
	LLenarDGPaq( wb2, filas,"LEGISMOVIL","0");
	LLenarDGPaq( wb2, filas,"sistencia","0");
	LLenarDGPaq( wb2, filas,"LDI","0");
	LLenarDGPaq( wb2, filas,"Equipo","0");
	LLenarDGPaq( wb2, filas, "SIMCard", "0");
	LLenarDGPaq( wb2, filas,"SANCIONEQUIPO","0");
	LLenarDGPaq( wb2, filas,"SANCIONPLAN","0");
	LLenarDGPaq( wb2, filas,"0FnFVozOnnet","99999999");
	LLenarDGPaq( wb2, filas,"FnFVozFijo","0");
	LLenarDGPaq( wb2, filas,"1FnFVozOnnet","1");
	LLenarDGPaq( wb2, filas,"3FnFVozOnnet","3");
	LLenarDGPaq( wb2, filas,"4FnFVozOnnet","4");	
	LLenarDGPaq( wb2, filas,"FnFSMSOnnet","0");	
	LLenarDGPaq( wb2, filas,"VideoLlamada","0");
	LLenarDGPaq( wb2, filas,"RecordacionMIN","0");
	LLenarDGPaq( wb2, filas,"RollOver","0");
	LLenarDGPaq( wb2, filas,"Reposicion","0");
	LLenarDGPaq( wb2, filas,"BienRepo","0");
	LLenarDGPaq( wb2, filas,"CobroDirTel","0");
	LLenarDGPaq( wb2, filas,"6FnFVozOnnet","6");
	LLenarDGPaq( wb2, filas,"9FnFVozOnnet","9");
	
	//-----------------------------------------------

	FileOutputStream fileOut2 = new FileOutputStream(archivo);   
	wb2.write(fileOut2); 
	fileOut2.close();
	System.out.println("**FINALIZADO**");
	System.out.println("**FINALIZADO**" + new Date());*/
 }
//---------------------------------------------------------------------------
  public static void llenarPOtoPO(Workbook wb){
	  IDataModel model = new DataModel();	
	  Sheet hoja = wb.getSheet("ProductOfferingToProductOffe0");
	  Collection<productOffering> PO = model.getProductOffering();
	  int fila = 6;  
	  int col = 0;
	  int colini = 0;
	  Cell cell = null;
	  for(productOffering boffer: PO){
		Row row = hoja.getRow(fila); 
	  	if(row==null)
			 row = hoja.createRow(fila);
	  	cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini); 
	    cell.setCellType(Cell.CELL_TYPE_STRING);  
	    cell.setCellValue("TRUE"); 
	    
	    colini++;
	    cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("Replace Offer - Wireless");
		
		colini = colini +6;
	    cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue( boffer.getDESC_BO()); 
		
		colini++;
	    cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("ProductOffering"); 
		
		colini++;
	    cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(boffer.getDESC_BO()); 
		colini = col;
		fila ++;
	  }	  
  }
/// Insertar informacion al archivo de excel  Product Offering hoja 
public static void LLenarDatosGenerales( Workbook wb) throws ParseException{
	IDataModel model = new DataModel();	
	List<DatosGenerales> dg = model.getDatosGenerales();
	Sheet hoja = wb.getSheet("General Info");
	int fila = 9;
	int col = 0;
	int colini = col;
	Cell cell = null;
	String plan = "";
	 
	for(DatosGenerales DG : dg){
		System.out.println(DG.getTMCODE());
		Row row = hoja.getRow(fila); 
		 if(row==null)
			 row = hoja.createRow(fila);		  
		 //*****************Genera celda columna para llenar primer dato*****************************//
		 cell = row.getCell(colini);				
			if(cell ==null) cell = row.createCell(colini); 
		    cell.setCellType(Cell.CELL_TYPE_STRING);  
	  //-------------------Descripcion O nombre Ingles ----------------
			colini ++;
			cell = row.getCell(colini);				
			if(cell ==null) cell = row.createCell(colini);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellValue(DG.getDESC_BO());
	//--------------------- BusinessVersionEffectiveDate --------------------------------------
			colini = colini +1;
			cell = row.getCell(colini);				
			if(cell ==null) cell = row.createCell(colini); 
			Calendar calendar = Calendar.getInstance();
			CellStyle style;
			DataFormat fmt ;
			Date date;
			if(DG.getEFFECTIVE_DATE()!=null){		
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        date = sdf.parse(DG.getEFFECTIVE_DATE());
	        calendar.setTime(date);        
	        fmt = wb.createDataFormat();
	        style = wb.createCellStyle();
	        style.setAlignment(CellStyle.ALIGN_RIGHT);
	        style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss")); 
	        cell.setCellStyle(style); 				 
			cell.setCellValue(calendar);}			
			
    //-------------------fecha inicio - SaleEffectiveDate-------------------------------
		colini = colini +3;	
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini); 
		SimpleDateFormat sdf;
		if(DG.getFECHA_INICIO()!=null){
		calendar = Calendar.getInstance();
		sdf = new SimpleDateFormat("dd/MM/yyyy");
        date = sdf.parse(DG.getFECHA_INICIO());
        calendar.setTime(date);        
        fmt = wb.createDataFormat();
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss")); 
        cell.setCellStyle(style); 				 
		cell.setCellValue(calendar);}	
		//--------------------------SaleExpirationDate-------------------------------------------
		colini = colini +1;	
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini); 
		if(DG.getFECHA_VENTA()!=null){
			calendar = Calendar.getInstance();	   
			sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			date = sdf.parse(DG.getFECHA_VENTA());
			calendar.setTime(date);        
			fmt = wb.createDataFormat();
			style = wb.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss")); 
			cell.setCellStyle(style); 				 
			cell.setCellValue(calendar);}		
		//-------------------(Code) ----------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		plan = DG.getDESC_BO().trim().replace(" ","_");
		cell.setCellValue(plan);
		//-----------------------Classification---------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("ST");
		//-----------------------Line of Business--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("WS");
		//-----------------------Sellable--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		DataFormat fmt1 = wb.createDataFormat();
        CellStyle style1 = wb.createCellStyle();
        style1.setAlignment(CellStyle.ALIGN_RIGHT);
        style1.setDataFormat(fmt1.getFormat("@")); 
        cell.setCellStyle(style1); 				 
		cell.setCellValue("TRUE");
		//-----------------------Name: Spanish--------------------------
	/*	colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getDESC_BO());*/
		//-----------------------Sales Channel--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("Call Center");
		//-----------------------Index--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Purpose-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("DE");
		//-----------------------DescriptionEnglish-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		String desc = "";
			/*	if (DG.getDESCRIPCION1()== null) desc = desc +"";
		        else desc = desc + DG.getDESCRIPCION1();
				if (DG.getDESCRIPCION2()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION2();
				if (DG.getDESCRIPCION3()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION3();
				if (DG.getDESCRIPCION4()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION4();
				if (DG.getDESCRIPCION5()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION5();
				if (DG.getDESCRIPCION6()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION6();
				if (DG.getDESCRIPCION7()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION7(); */
				
		if(desc!=null){
			if (desc.trim().equals("Na")) desc = DG.getDESC_BO();}	
				
		 if( DG.getDESC_BO().contains("MAS 210 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 210 Ab Mail Chat Redes Iph")||
				 DG.getDESC_BO().contains("MAS 330 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 330 Ab Mail Chat Redes Ip")||
				 DG.getDESC_BO().contains("MAS 480 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 480 Ab Mail Chat Redes Iph")||
				 DG.getDESC_BO().contains("MAS 780 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 780 Ab Mail Chat Redes Iph"))
			 {   desc = desc.replaceAll("En Roaming Internacional la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
			 	 desc = desc.replaceAll("En Roaming Internacional  la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
			     cell.setCellValue(desc);
			     System.out.println(" si pasa MAS: " + DG.getDESC_BO());
			 }
		  else if(DG.getDESC_BO().contains("Pocket Mail 115 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 115 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 1250 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 1250 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 320 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 320 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 480 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 780 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 920 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 920 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 480 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 780 SI11 Abierto")	)	 
		 	{
			 desc = desc.replaceAll("La utilización de los servicios Messenger de Blackberry, Blackberry Mail, Chat MSN - Yahoo - Gtalk, en Roaming Internacional serán facturados de manera adicional con tarifas de Roaming Internacional vigentes", "");
			 cell.setCellValue(desc);
			 System.out.println(" si pasa pocket:" + DG.getDESC_BO());
		 	}
		  else {cell.setCellValue(desc);}
				
		//-----------------------DescriptionSpanish-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
			      desc = "";
				/*if (DG.getDESCRIPCION1()== null) desc = desc +"";
		        else desc = desc + DG.getDESCRIPCION1();
				if (DG.getDESCRIPCION2()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION2();
				if (DG.getDESCRIPCION3()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION3();
				if (DG.getDESCRIPCION4()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION4();
				if (DG.getDESCRIPCION5()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION5();
				if (DG.getDESCRIPCION6()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION6();
				if (DG.getDESCRIPCION7()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION7();	*/		
				desc = DG.getDESCRIPCIONT();
				if(desc!=null){
					if (desc.trim().equals("Na")) desc = DG.getDESC_BO();}	
				
				if( DG.getDESC_BO().contains("MAS 210 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 210 Ab Mail Chat Redes Iph")||
						 DG.getDESC_BO().contains("MAS 330 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 330 Ab Mail Chat Redes Ip")||
						 DG.getDESC_BO().contains("MAS 480 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 480 Ab Mail Chat Redes Iph")||
						 DG.getDESC_BO().contains("MAS 780 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 780 Ab Mail Chat Redes Iph"))
					 {   
					 
					     desc = desc.replaceAll("En Roaming Internacional la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
					     desc = desc.replaceAll("En Roaming Internacional  la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
					    
					      cell.setCellValue(desc);
					     System.out.println(" si pasa MAS: " + DG.getDESC_BO());
					     System.out.println(desc);
					 }
				  else if(DG.getDESC_BO().contains("Pocket Mail 115 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 115 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 1250 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 1250 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 320 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 320 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 480 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 780 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 920 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 920 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 480 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 780 SI11 Abierto")	)	 
				 	{
					 desc = desc.replaceAll("La utilización de los servicios Messenger de Blackberry, Blackberry Mail, Chat MSN - Yahoo - Gtalk, en Roaming Internacional serán facturados de manera adicional con tarifas de Roaming Internacional vigentes", "");
					 cell.setCellValue(desc);
					 System.out.println(" si pasa pocket:" + DG.getDESC_BO());
				 	}
				  else {cell.setCellValue(desc);}
		//-----------------------Index-------------------------
		colini = colini +8;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
			//-----------------------Minimum Quantity-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Maximum Quantity-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Default Quantity-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Product Relation Role-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("SA");
		//-----------------------Product Name-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("Wireless");
		//-----------------------Eligibility Rule:Index-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Eligibility Rule:active Indication-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("TRUE");
		//-----------------------Eligibility Rule:EligibilityRuleName-------------------------
		colini = colini +4;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
			if(DG.getOFER_CAMBIOPLAN()!=null&&DG.getVIGENTE().equals("S")){			
				if(DG.getOFER_CAMBIOPLAN().equals("S")&&DG.getOFER_VENTA().equals("N"))
					cell.setCellValue("Only allow to offer change");
				//else if (DG.getDESC_BO().equals("Plan - Especial Costa 2 Eleg"))
				else if (DG.getOFER_CAMBIOPLAN().equals("S")&&DG.getOFER_VENTA().equals("S"))
					{ if(DG.getZONA().equals("COS"))
						cell.setCellValue("Offer valid for COSTA region Only");		    	
					  else cell.setCellValue("");				
					}
				else cell.setCellValue("Offers valid Only for Provide");}
			else cell.setCellValue("Dummy Eligibility Rule");
	
		//-----------------------Field List-------------------------
		colini = colini +4;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("Product Offering Properties Comcel Colombia");
		//--------------Product Offering:F&F Max Number of Friends-Video Call Onnet Wireless-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-SMS Onnet Wireless--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-SMS Other Wireless--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-Voice Onnet Wireless-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if( DG.getCARACT_ELEG()!=null){
		 if (DG.getCARACT_ELEG().trim().contains("Eleg"))
			 cell.setCellValue(DG.getCARACT_ELEG().trim().substring(0, 1));
		 else if (DG.getCARACT_ELEG().trim().contains("Elegidos (50%)"))
			 cell.setCellValue(DG.getCARACT_ELEG().trim().substring(0, 1));}
			 //Ojo continuar aca ...................................................		 { 	cell.setCellValue("");}
		 else cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-MMS Onnet Wireless---------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-Video Call Wireles--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);		
			
		//--------------Product Offering:F&F Max Number of Friends-Voice Other Fix--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if((DG.getTMCODE()>= 6594 && DG.getTMCODE()<= 6599) ||(DG.getTMCODE()>= 6649 && DG.getTMCODE()<= 6654)  )
			 cell.setCellValue(1);
		else cell.setCellValue(0);	
		//--------------Product Offering:F&F Max Number of Friends-MMS Other Wireless--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);	
		//--------------Product Offering:F&F Max Number of Friends-Voice Other Wireless--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-Voice Onnet Fix--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);	
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-Voice International--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-SMS_International--------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);		
		/// cambio a nuevo formato ------
		//--------------Product Offering:Display Priority---------------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:ConfigurationType ---------------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("P");
		//-------------- Product Offering:MaxQuantity ----------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
			if(DG.getTMCODE() ==0 ) cell.setCellValue(" ");
			else cell.setCellValue(999);
		//-------------- Product Offering:Allow Equipment Installments ----------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
			if(DG.getTMCODE() ==0 ) cell.setCellValue(" ");
			else cell.setCellValue("No");				
		//--------------Product Offering:Plan Type ---------------------------------------------------------------
		colini = colini +2;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		     //  if(DG.getTMCODE() ==0 )	cell.setCellValue(" ");
		     //   else
		       if(DG.getTIPOPLAN().trim().equals("Mixto")){
					cell.setCellValue("MX");
					}
				else{
					cell.setCellValue("OP");
					}	
		//-------------- Product Offering:GAMA_ALL ---------------------------------------------------------------
		colini = colini +2;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if(DG.getGAMA_ORIENTE()!= null){
			if(DG.getGAMA_ORIENTE().trim().equals("ALTA"))
				cell.setCellValue("ALTA");
				if(DG.getGAMA_ORIENTE().trim().equals("BAJA"))
				cell.setCellValue("BAJA");
				if(DG.getGAMA_ORIENTE().trim().equals("MEDIA")  )
				 cell.setCellValue("MEDIA"); }
			else cell.setCellValue("");
		//-------------- Product Offering:GAMA_COSTA ----------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getGAMA_COSTA());
		//-------------- Product Offering:GAMA_OCCIDENTE ----------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getGAMA_OCCIDENTE());
		//-------------- Product Offering:GAMA_ORIENTE ----------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getGAMA_ORIENTE());		
		//-------------- Product Offering:Required Evident COSTA ----------------------------------------------------------
		colini = colini +3;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if (DG.getTMCODE()== 0) cell.setCellValue("");
		else if(DG.getGAMA_COSTA().equals("BAJA")) cell.setCellValue("No");
		else cell.setCellValue("Yes");
		//-------------- Product Offering:Required Evident OCCIDENTE ----------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if (DG.getTMCODE()== 0) cell.setCellValue("");
		else if(DG.getGAMA_OCCIDENTE().equals("BAJA")) cell.setCellValue("No");
		else cell.setCellValue("Yes");
		//-------------- Product Offering:Required Evident ORIENTE ----------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if (DG.getTMCODE()== 0) cell.setCellValue("");
		else if(DG.getGAMA_ORIENTE().equals("BAJA")) cell.setCellValue("No");
		else cell.setCellValue("Yes");	
		//-------------- Plan Sub Type ----------------------------------------------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				if (DG.getTMCODE()== 0) cell.setCellValue("");
				else if(DG.getTIPO_PLAN()!=null) cell.setCellValue("FP");
				else cell.setCellValue("");	
				
				//-------------- Plan Services ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				if(DG.getCARACT_PLAN().equals("Datos")||DG.getCARACT_PLAN().equals("BB")||DG.getCARACT_PLAN().equals("Video llamada")||DG.getCARACT_PLAN().equals("SMS"))
					cell.setCellValue("Datos");  
				else if(DG.getCARACT_PLAN().contains("Bolsa")){
					if(DG.getCARACT_PLAN().contains("Datos")||DG.getCARACT_PLAN().contains("BB"))
						cell.setCellValue("Both");					
					else cell.setCellValue("Voz");						
				}else if(DG.getCARACT_PLAN().equals("Dummy"))
					cell.setCellValue("Both");
				
				 //-------------- Equipment SKU ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				//cell.setCellValue("");  
				 //-------------- SIM SKU ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				//cell.setCellValue("");  
				 //-------------- Equipment Classification ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getDESC_EQUIPO().trim().toUpperCase());  
				 //-------------- Is TacticalSIM ---------------------------------------------------------------------------------------	
			/*	colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				if(DG.getDESC_BO().contains("TARIFA UNICA TACTICO") ||DG.getDESC_BO().contains("WELCOME BACK TACTICO 2011") )
				      cell.setCellValue("Y"); 
				else  cell.setCellValue("N"); */
				
				//-------------------------  UsingSIMCardPrepago -------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);		
				if(DG.getUsingsimcardprepag1()!=null)
				 cell.setCellValue(DG.getUsingsimcardprepag1());
				else 
					 cell.setCellValue("");
				//-------------------------    Region  ---------------------------------------------------------------------------------------
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);		
				  cell.setCellValue(DG.getZONA());					
				
				//-------------- Product Type ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				  if(DG.getPRODUCT_TYPE()!= null)
						cell.setCellValue(DG.getPRODUCT_TYPE());	  
				  else
					  cell.setCellValue("");
	
				 //-------------- Change MSISDN Num Month before Commitment End date ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue("0");
				
				//-------------- BFNF Voice Additional Days To Next Number Change ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue("0");  
				 //-------------- BFNF Voice Other Fix---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue("0"); 
				 //-------------- BFNF Voice Duration Till Renew ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getBFNFVDURTILLRE()); 
				 //-------------- BFNF Voice Days To Next Number Change ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				 cell.setCellValue(DG.getBFNFVDAYNEXTNCH()); 
				 
				 //-------------- BFNF Voice Onnet Wireless ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue("0"); 
				 //-------------- BFNF Voice Onnet Fix ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue("0"); 
				 //-------------- BFNF Voice Other Wireless ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue("0"); 
				 //-------------- BFNF Voice All Destination  ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
					if(DG.getDESC_BO().equals("Plan - Chip Prepago Costa 2014")|| DG.getDESC_BO().equals("Plan - Especial Costa"))
						cell.setCellValue("2"); 
					else cell.setCellValue(DG.getELEGIDOS_BFNF()); 
				 //-------------- BFNF Voice International ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue("0"); 
				 //-------------- BFNF Voice Service Duration  ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getBFNFVDURTD()); 				
				//--------- nuevas propiedades --------------------------------------------------------------------
		    	 //-------------- FNF SMS Days To Next Number Change ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFSMSDAYNEXTNCH()); 	
				 //-------------- FNF SMS Service Duration ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFSMSDUR()); 	
				 //-------------- FNF SMS Duration Till Renew ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFSMSDURTILLRE()); 	
				 //-------------- FNF Max Number of Friends - SMS All Destination ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFSMSMAXNUMELE_TD()); 	
				 //-------------- FNF Voice Days To Next Number Change ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFVZDAYNEXTNCH()); 	
				 //-------------- FNF Voice Service Duration  ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFVZDUR()); 	
				 //-------------- FNF Max Number of Friends - Voice All Destination ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFVZMAXNUMELE_TD()); 	
				 //-------------- FNF Voice Duration Till Renew ---------------------------------------------------------------------------------------	
				colini = colini +1;		
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getFFVZDURTILLRE()); 		
				
			 // -------------------------------------------------------					
		//----------------- Product Offering:Related Attributes:Entry Index    --------------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Attribute --------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("Equipment Classification SE");
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Constant Values --------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if(DG.getTMCODE() ==0 )
		    cell.setCellValue("TODOS");
		else {if(DG.getDESC_EQUIPO().trim().equals("N/A")) cell.setCellValue(""); 
		      else cell.setCellValue(DG.getDESC_EQUIPO().trim().toUpperCase());	
		}
		
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("EnumerationDomain");
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("AbsEquipmentClassification");
		//--------------Legacy Code-----------------------------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getTMCODE());	
		fila++;
		for(int j = 0;j<11;j++){
			if(j==3 && DG.getTIPO_PLAN()!=null){
				continue;
			}else{
	           colini = col;
			   row = hoja.getRow(fila); 
			   if(row==null)  row = hoja.createRow(fila);		  
			 //**********************************************//
			    cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				//cell.setCellValue("TRUE");
			 //................................................. 
			    colini = colini +1; 
			    cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getDESC_BO());			
				//-----------------------
				colini = colini +10;
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				if(j==0)  { cell.setCellValue("Dealer");
				
	       /**-------------------------------------------------------	 **/
						//----------------- Product Offering:Related Attributes:Entry Index    --------------------------------------------------------------
						colini = colini +83;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue(2);
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Attribute --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("Plan Type SE");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Constant Values --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						//if(DG.getTMCODE() ==0 )
						//	cell.setCellValue(" ");
						//else
						if(DG.getTIPOPLAN().trim().equals("Mixto")){
							cell.setCellValue("MX");
							}
						else if(DG.getTIPOPLAN().trim().equals("Abierto")){
							cell.setCellValue("OP");
							}
						else cell.setCellValue("OP");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("EnumerationDomain");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("AbsHybridType");
				/**------------------------------------------------------	**/
				}				
				if(j==1) {cell.setCellValue("Super User");
				/** ...........................................................
				        ----------------- Product Offering:Related Attributes:Entry Index    -------------------------------------------------------------- **/
						colini = colini +83;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue(3);
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Attribute --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("Plan Sub Type SE");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Constant Values --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						if(DG.getTMCODE() ==0 ){ cell.setCellValue("VO");}
						else if(DG.getSUBTYPE().equals("N/A")) cell.setCellValue("");
						else cell.setCellValue(DG.getSUBTYPE());
						
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("EnumerationDomain");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("AbsPlanSubtype");	}
				
				/**..............................................................**/
				if(j==2) cell.setCellValue("Retail using AMSS");
				if(j==3 && DG.getTIPO_PLAN()==null) cell.setCellValue("Self Service");	
				if(j==4) cell.setCellValue("MASS");	
				if(j==5) cell.setCellValue("OMS");
				if(j==6) cell.setCellValue("CVC");
				if(j==7) cell.setCellValue("CAC");
				if(j==8) cell.setCellValue("IVR");
				if(j==9) cell.setCellValue("Retailer");
				if(j==10) cell.setCellValue("Temporary");				
				if(j!=10) fila ++;
							}
					}//termina ciclo for 		
		       colini = col;
		       fila++;	           
			}
	    }
//-------------------------------Escribir General Info en PO para planes - prepago ------------------------------------------------------------
public static void LLenarGenInfoPlanesPrepago( Workbook wb, HashMap<String, Integer> mapBO) throws ParseException{
	IDataModel model = new DataModel();	
	List<DatosGenerales> dg = model.getDatosGenerales();	
	Sheet hoja = wb.getSheet("General Info");
	int fila = 9;
	int col = 0;
	int colini = col;
	Cell cell = null;
	String plan = "";
	 
	for(DatosGenerales DG : dg){
		System.out.println(DG.getTMCODE());
		Row row = hoja.getRow(fila); 
		 if(row==null)
			 row = hoja.createRow(fila);		  
		 //*****************Genera celda columna para llenar primer dato*****************************//
		 cell = row.getCell(colini);				
			if(cell ==null) cell = row.createCell(colini); 
		    cell.setCellType(Cell.CELL_TYPE_STRING);  
			
	   //-------------------Descripcion O nombre Ingles ----------------
		   /* cell = row.getCell(mapBO.get("Template=\"ProductOffering\";UniqueIdentifier=\"Element Name\";locale=\"EN\"")); 
			if (cell == null)   
				cell = row.createCell(mapBO.get("Template=\"ProductOffering\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(DG.getDESC_BO());	 */
		colini ++; //1
		cell = row.getCell(colini); 
		if (cell == null)   
				cell = row.createCell(colini);   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellValue(DG.getDESC_BO());	
		//--------------------- BusinessVersionEffectiveDate --------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini); 
		Calendar calendar = Calendar.getInstance();
		CellStyle style;
		DataFormat fmt ;
		Date date;
		if(DG.getEFFECTIVE_DATE()!=null){		
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        date = sdf.parse(DG.getEFFECTIVE_DATE());
        calendar.setTime(date);        
        fmt = wb.createDataFormat();
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss")); 
        cell.setCellStyle(style); 				 
		cell.setCellValue(calendar);}		
		//-------------------fecha inicio - SaleEffectiveDate -------------------------------
		colini = colini +3;	
		//cell = row.getCell(mapBO.get("SaleEffectiveDate"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("SaleEffectiveDate")); 
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		SimpleDateFormat sdf;
		if(DG.getFECHA_INICIO()!=null){
		calendar = Calendar.getInstance();
		sdf = new SimpleDateFormat("dd/MM/yyyy");
        date = sdf.parse(DG.getFECHA_INICIO());
        calendar.setTime(date);        
        fmt = wb.createDataFormat();
        style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss")); 
        cell.setCellStyle(style); 				 
		cell.setCellValue(calendar);}
		
		//--------------------------SaleExpirationDate-------------------------------------------
		colini = colini +1;	
		//cell = row.getCell(mapBO.get("SaleExpirationDate"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("SaleExpirationDate")); 
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini); 
		if(DG.getFECHA_VENTA()!=null){
		    calendar = Calendar.getInstance();	   
	        sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	        date = sdf.parse(DG.getFECHA_VENTA());
	        calendar.setTime(date);        
	        fmt = wb.createDataFormat();
	        style = wb.createCellStyle();
	        style.setAlignment(CellStyle.ALIGN_RIGHT);
	        style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss")); 
	        cell.setCellStyle(style); 				 
			cell.setCellValue(calendar);}	
		//-------------------(Code) ----------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("CodeToFillA"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("CodeToFillA"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		plan = DG.getDESC_BO().trim().replace(" ","_");
		cell.setCellValue(plan);
		//-----------------------Classification---------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("ClassificationToFillA"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("ClassificationToFillA"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("ST");
		//-----------------------Line of Business--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("LineOfBusiness"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("LineOfBusiness"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("WS");
		//-----------------------Sellable--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("SellableInd"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("SellableInd"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		DataFormat fmt1 = wb.createDataFormat();
        CellStyle style1 = wb.createCellStyle();
        style1.setAlignment(CellStyle.ALIGN_RIGHT);
        style1.setDataFormat(fmt1.getFormat("@")); 
        cell.setCellStyle(style1); 				 
		cell.setCellValue("TRUE");
		//-----------------------Name: Spanish--------------------------
	/*	colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell = row.getCell(mapBO.get("CoreElementPath=\"Element Name\";locale=\"es\""));				
		//if(cell ==null) cell = row.createCell(mapBO.get("CoreElementPath=\"Element Name\";locale=\"es\""));	
		cell.setCellValue(DG.getDESC_BO());*/
		//-----------------------Sales Channel--------------------------
		String[] canalventa = DG.getCanalventa().split(",");
		 
		for(int i = 0; i<canalventa.length; i++){
		System.out.println(canalventa[i]+ " valor en: "+ i);}
		//--------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//if(DG.getTMCODE()==13620)
		//cell = row.getCell(mapBO.get("SalesChannel;UniqueIdentifier=\"Element Name\"ToFillA"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("SalesChannel;UniqueIdentifier=\"Element Name\"ToFillA"));
		//cell.setCellValue("AMSS");
	    if(canalventa.length> 0){
	    	if(canalventa[0].equals("TODOS"))cell.setCellValue(""); 
	    	else cell.setCellValue(canalventa[0]); 	}
		
		//-----------------------Index--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("Description;EntryIndex=\"Dummy Entry Index\";Level=1"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Description;EntryIndex=\"Dummy Entry Index\";Level=1"));		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Purpose-------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("PurposeToFillA"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("PurposeToFillA"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("DE");
		//-----------------------DescriptionEnglish-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell = row.getCell(mapBO.get("Description;Locale=\"en\""));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Description;Locale=\"en\""));		
		String desc = "";
			/*	if (DG.getDESCRIPCION1()== null) desc = desc +"";
		        else desc = desc + DG.getDESCRIPCION1();
				if (DG.getDESCRIPCION2()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION2();
				if (DG.getDESCRIPCION3()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION3();
				if (DG.getDESCRIPCION4()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION4();
				if (DG.getDESCRIPCION5()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION5();
				if (DG.getDESCRIPCION6()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION6();
				if (DG.getDESCRIPCION7()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION7();	
				desc = DG.getDESCRIPCIONT();*/
				System.out.println(DG.getTMCODE()+ "  Descripcion: "+DG.getDESCRIPCIONT() );
		
		if(desc!=null){
		if (desc.trim().equals("Na")) desc = DG.getDESC_BO();}		  
				
		 if( DG.getDESC_BO().contains("MAS 210 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 210 Ab Mail Chat Redes Iph")||
				 DG.getDESC_BO().contains("MAS 330 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 330 Ab Mail Chat Redes Ip")||
				 DG.getDESC_BO().contains("MAS 480 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 480 Ab Mail Chat Redes Iph")||
				 DG.getDESC_BO().contains("MAS 780 Ab Chat Mail Redes")||
				 DG.getDESC_BO().contains("MAS 780 Ab Mail Chat Redes Iph"))
			 {   desc = desc.replaceAll("En Roaming Internacional la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
			 	 desc = desc.replaceAll("En Roaming Internacional  la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
			     cell.setCellValue(desc);
			     System.out.println(" si pasa MAS: " + DG.getDESC_BO());
			 }
		  else if(DG.getDESC_BO().contains("Pocket Mail 115 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 115 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 1250 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 1250 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 320 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 320 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 480 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 780 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 920 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 920 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 480 SI11 Abierto")||
				 DG.getDESC_BO().contains("Pocket Mail 780 SI11 Abierto")	)	 
		 	{
			 desc = desc.replaceAll("La utilización de los servicios Messenger de Blackberry, Blackberry Mail, Chat MSN - Yahoo - Gtalk, en Roaming Internacional serán facturados de manera adicional con tarifas de Roaming Internacional vigentes", "");
			 cell.setCellValue(desc);
			 System.out.println(" si pasa pocket:" + DG.getDESC_BO());
		 	}
		  else {cell.setCellValue(desc);}
				
		//-----------------------DescriptionSpanish-------------------------
		colini = colini +1;
		        //cell = row.getCell(mapBO.get("Description;Locale=\"es\""));				
				//if(cell ==null) cell = row.createCell(mapBO.get("Description;Locale=\"es\""));
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
			      desc = "";
			/*	if (DG.getDESCRIPCION1()== null) desc = desc +"";
		        else desc = desc + DG.getDESCRIPCION1();
				if (DG.getDESCRIPCION2()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION2();
				if (DG.getDESCRIPCION3()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION3();
				if (DG.getDESCRIPCION4()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION4();
				if (DG.getDESCRIPCION5()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION5();
				if (DG.getDESCRIPCION6()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION6();
				if (DG.getDESCRIPCION7()== null) desc = desc +"";
				 else desc = desc + DG.getDESCRIPCION7(); 	*/			
				
				desc = DG.getDESCRIPCIONT();
				if(desc!=null){
				if (desc.trim().equals("Na")) desc = DG.getDESC_BO();}		  
		
				 if( DG.getDESC_BO().contains("MAS 210 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 210 Ab Mail Chat Redes Iph")||
						 DG.getDESC_BO().contains("MAS 330 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 330 Ab Mail Chat Redes Ip")||
						 DG.getDESC_BO().contains("MAS 480 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 480 Ab Mail Chat Redes Iph")||
						 DG.getDESC_BO().contains("MAS 780 Ab Chat Mail Redes")||
						 DG.getDESC_BO().contains("MAS 780 Ab Mail Chat Redes Iph"))
					 {  					 
					     desc = desc.replaceAll("En Roaming Internacional la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
					     desc = desc.replaceAll("En Roaming Internacional  la utilización de los servicios de datos serán facturados como servicios adicionales con tarifas de Roaming Internacional vigentes.", "");
					    
					      cell.setCellValue(desc);
					     System.out.println(" si pasa MAS: " + DG.getDESC_BO());
					     System.out.println(desc);
					 }
				  else if(DG.getDESC_BO().contains("Pocket Mail 115 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 115 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 1250 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 1250 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 320 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 320 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 480 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 780 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 920 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 920 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 480 SI11 Abierto")||
						 DG.getDESC_BO().contains("Pocket Mail 780 SI11 Abierto")	)	 
				 	{
					 desc = desc.replaceAll("La utilización de los servicios Messenger de Blackberry, Blackberry Mail, Chat MSN - Yahoo - Gtalk, en Roaming Internacional serán facturados de manera adicional con tarifas de Roaming Internacional vigentes", "");
					 cell.setCellValue(desc);
					 System.out.println(" si pasa pocket:" + DG.getDESC_BO());
				 	}
				  else {cell.setCellValue(desc);}
		//-----------------------Index-------------------------
		colini = colini +8;
		//cell = row.getCell(mapBO.get("OfferingProductRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("OfferingProductRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);		
		cell.setCellValue(1);
			//-----------------------Minimum Quantity-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell = row.getCell(mapBO.get("MinimumQuantity"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("MinimumQuantity"));
		cell.setCellValue(1);
		//-----------------------Maximum Quantity-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell = row.getCell(mapBO.get("MaximumQuantity"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("MaximumQuantity"));
		cell.setCellValue(1);
		//-----------------------Default Quantity-------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("DefaultQuantity"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("DefaultQuantity"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Product Relation Role-------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("ProductRelationRole"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("ProductRelationRole"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("SA");
		//-----------------------Product Name-------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell = row.getCell(mapBO.get("Product;UniqueIdentifier=\"Element Name\""));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Product;UniqueIdentifier=\"Element Name\""));
		cell.setCellValue("Wireless");		
		//-----------------------Eligibility Rule:Index-------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("EligibilityRule;EntryIndex=\"Dummy Entry Index\";Level=1"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("EligibilityRule;EntryIndex=\"Dummy Entry Index\";Level=1"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//-----------------------Eligibility Rule:active Indication-------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("ActiveIndication"));			
		//if(cell ==null) cell = row.createCell(mapBO.get("ActiveIndication"));
		cell = row.getCell(colini);			
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("TRUE");
		//-----------------------Eligibility Rule:EligibilityRuleName-------------------------
		colini = colini +4;
		//cell = row.getCell(mapBO.get("EligibilityRule;UniqueIdentifier=\"Element Name\""));				
		//if(cell ==null) cell = row.createCell(mapBO.get("EligibilityRule;UniqueIdentifier=\"Element Name\""));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);		
		if(DG.getOFER_CAMBIOPLAN()!=null&&DG.getVIGENTE().equals("S")){			
			if(DG.getOFER_CAMBIOPLAN().equals("S")&&DG.getOFER_VENTA().equals("N"))
			 cell.setCellValue("Only allow to offer change");
			//else if (DG.getDESC_BO().equals("Plan - Especial Costa 2 Eleg"))
		    else if (DG.getOFER_CAMBIOPLAN().equals("S")&&DG.getOFER_VENTA().equals("S"))
		    	{ if(DG.getZONA().equals("COS"))
		    			cell.setCellValue("Offer valid for COSTA region Only");		    	
		    	  else cell.setCellValue("");				
		    	}
				else cell.setCellValue("Offers valid Only for Provide");}
		   else cell.setCellValue("Dummy Eligibility Rule");
		
		//-----------------------Field List-------------------------
		colini = colini +4;
		//cell = row.getCell(mapBO.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("BFNF Product Offering Properties Comcel Colombia Prepaid");
		//--------------Product Offering:F&F Max Number of Friends-Video Call Onnet Wireless-------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-Video_Call_Onnet_Wireless"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-Video_Call_Onnet_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-SMS Onnet Wireless--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-SMS_Onnet_Wireless"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-SMS_Onnet_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-SMS Other Wireless--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-SMS_Other_Wireless"));			
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-SMS_Other_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);		
		
		//--------------Product Offering:F&F Max Number of Friends-Voice Onnet Wireless-------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-Onnet_Wireless"));			
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-Onnet_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if( DG.getCARACT_ELEG()!=null){
		 if (DG.getCARACT_ELEG().trim().contains("Eleg"))
			 cell.setCellValue(DG.getCARACT_ELEG().trim().substring(0, 1));
		 else if (DG.getCARACT_ELEG().trim().contains("Elegidos (50%)"))
			 cell.setCellValue(DG.getCARACT_ELEG().trim().substring(0, 1));}
			 //Ojo continuar aca ...................................................		 { 	cell.setCellValue("");}
		 else cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-MMS Onnet Wireless---------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-MMS_Onnet_Wireless"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-MMS_Onnet_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-Video Call Wireles--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-Video_Call_Other_Wireless"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-Video_Call_Other_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		
		//--------------Product Offering:F&F Max Number of Friends-Voice Other Fix--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-Other_Fix"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-Other_Fix"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if((DG.getTMCODE()>= 6594 && DG.getTMCODE()<= 6599) ||(DG.getTMCODE()>= 6649 && DG.getTMCODE()<= 6654)  )
			 cell.setCellValue(1);
		else cell.setCellValue(0);	
		//--------------Product Offering:F&F Max Number of Friends-MMS Other Wireless--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-MMS_Other_Wireless"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-MMS_Other_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);	
		//--------------Product Offering:F&F Max Number of Friends-Voice Other Wireless--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-Other_Wireless"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-Other_Wireless"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-Voice Onnet Fix--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-Onnet_Fix"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-Onnet_Fix"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-Voice International--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-F&F_International"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-F&F_International"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);
		//--------------Product Offering:F&F Max Number of Friends-SMS_International--------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("F&F Max Number of Friends-SMS_F&F_International"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("F&F Max Number of Friends-SMS_F&F_International"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(0);		
		/// cambio a nuevo formato ------	
		//--------------Product Offering:Display Priority---------------------------------------------------------------
		colini = colini +1;
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell = row.getCell(mapBO.get("Display Priority"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Display Priority"));
		cell.setCellValue(0);
		//--------------Product Offering:ConfigurationType ---------------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("ConfigurationType"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("ConfigurationType"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("P");
		//-------------- Product Offering:MaxQuantity ----------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("MaxQuantity"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("MaxQuantity"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
			if(DG.getTMCODE() ==0 ) cell.setCellValue(" ");
			else cell.setCellValue(999);
		//-------------- Product Offering:Allow Equipment Installments ----------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("Allow Equipment Installments"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Allow Equipment Installments"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
			if(DG.getTMCODE() ==0 ) cell.setCellValue(" ");
			else cell.setCellValue("No");				
		//--------------Product Offering:Plan Type ---------------------------------------------------------------
		colini = colini +2;
		//cell = row.getCell(mapBO.get("Plan Type"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Plan Type"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		       if(DG.getTIPOPLAN().trim().equals("Mixto")){
					cell.setCellValue("PR");
					}
				else{
					cell.setCellValue("PR");
					}	
		//-------------- Product Offering:GAMA_ALL ---------------------------------------------------------------
		colini = colini +2;
		//cell = row.getCell(mapBO.get("GAMA_ALL"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("GAMA_ALL"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if(DG.getGAMA_ORIENTE()!= null){
			if(DG.getGAMA_ORIENTE().trim().equals("ALTA"))
				cell.setCellValue("ALTA");
				if(DG.getGAMA_ORIENTE().trim().equals("BAJA"))
				cell.setCellValue("BAJA");
				if(DG.getGAMA_ORIENTE().trim().equals("MEDIA")  )
				 cell.setCellValue("MEDIA"); }
			else cell.setCellValue("");
		//-------------- Product Offering:GAMA_COSTA ----------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("GAMA_COSTA"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("GAMA_COSTA"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getGAMA_COSTA());
		//-------------- Product Offering:GAMA_OCCIDENTE ----------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("GAMA_OCCIDENTE"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("GAMA_OCCIDENTE"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getGAMA_OCCIDENTE());
		//-------------- Product Offering:GAMA_ORIENTE ----------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("GAMA_ORIENTE"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("GAMA_ORIENTE"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getGAMA_ORIENTE());		
		//-------------- Product Offering:Required Evident COSTA ----------------------------------------------------------
		colini = colini +3;
		//cell = row.getCell(mapBO.get("Required Evident COSTA"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Required Evident COSTA"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if (DG.getTMCODE()== 0) cell.setCellValue("");
		else if(DG.getGAMA_COSTA().equals("BAJA")) cell.setCellValue("No");
		else cell.setCellValue("Yes");
		//-------------- Product Offering:Required Evident OCCIDENTE ----------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("Required Evident OCCIDENTE"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Required Evident OCCIDENTE"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if (DG.getTMCODE()== 0) cell.setCellValue("");
		else if(DG.getGAMA_OCCIDENTE().equals("BAJA")) cell.setCellValue("No");
		else cell.setCellValue("Yes");
		//-------------- Product Offering:Required Evident ORIENTE ----------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("Required Evident ORIENTE"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Required Evident ORIENTE"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if (DG.getTMCODE()== 0) cell.setCellValue("");
		else if(DG.getGAMA_ORIENTE().equals("BAJA")) cell.setCellValue("No");
		else cell.setCellValue("Yes");	
		//-------------- Plan Sub Type ----------------------------------------------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("Plan Sub Type"));				
		//		if(cell ==null) cell = row.createCell(mapBO.get("Plan Sub Type"));
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				if (DG.getTMCODE()== 0) cell.setCellValue("");
				//else if(DG.getTIPO_PLAN()!=null) cell.setCellValue("PP");
				else cell.setCellValue("PP");		
		
	    //-------------- Plan Services ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("Voice");  
		 //-------------- Equipment SKU ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell.setCellValue("");  
		 //-------------- SIM SKU ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		//cell.setCellValue("");  
		 //-------------- Equipment Classification ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getDESC_EQUIPO().trim().toUpperCase());  
		 //-------------- Is TacticalSIM ---------------------------------------------------------------------------------------	
	/*	colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if(DG.getDESC_BO().contains("Tarifa Unica Tactico") ||DG.getDESC_BO().contains("WelcomeBack Tactico Costa 2011") )
		      cell.setCellValue("Yes"); 
		else  cell.setCellValue("No"); */
	    
	   //-------------------------  UsingSIMCardPrepago ------------------------------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);		
		if(DG.getUsingsimcardprepag1()!=null)
		 cell.setCellValue(DG.getUsingsimcardprepag1());
		else 
			 cell.setCellValue("");
		//--------------------------------- Region  ----------------------------------------------------------------------------------------------------------------
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);		
		  cell.setCellValue(DG.getZONA());
		
		
		//--------------------------- Product Type -----------------------------------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		  if(DG.getPRODUCT_TYPE()!= null)
				cell.setCellValue(DG.getPRODUCT_TYPE());	  
		  else
			  cell.setCellValue(""); 	
		 //-------------- Change MSISDN Num Month before Commitment End date ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("0");  
		 
		//-------------- BFNF Voice Additional Days To Next Number Change ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("0");  
		 //-------------- BFNF Voice Other Fix---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("0"); 
		 //-------------- BFNF Voice Duration Till Renew ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getBFNFVDURTILLRE()); 
		 //-------------- BFNF Voice Days To Next Number Change ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		 cell.setCellValue(DG.getBFNFVDAYNEXTNCH()); 
		 
		 //-------------- BFNF Voice Onnet Wireless ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("0"); 
		 //-------------- BFNF Voice Onnet Fix ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("0"); 
		 //-------------- BFNF Voice Other Wireless ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("0"); 
		 //-------------- BFNF Voice All Destination  ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
			if(DG.getDESC_BO().equals("Plan - Chip Prepago Costa 2014")|| DG.getDESC_BO().equals("Plan - Especial Costa"))
				cell.setCellValue("2"); 
			else cell.setCellValue(DG.getELEGIDOS_BFNF()); 
		 //-------------- BFNF Voice International ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("0"); 
		 //-------------- BFNF Voice Service Duration  ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getBFNFVDURTD()); 	
		
		//--------- nuevas propiedades --------------------------------------------------------------------
		
		 //-------------- FNF SMS Days To Next Number Change ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFSMSDAYNEXTNCH()); 	
		 //-------------- FNF SMS Service Duration ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFSMSDUR()); 	
		 //-------------- FNF SMS Duration Till Renew ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFSMSDURTILLRE()); 	
		 //-------------- FNF Max Number of Friends - SMS All Destination ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFSMSMAXNUMELE_TD()); 	
		 //-------------- FNF Voice Days To Next Number Change ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFVZDAYNEXTNCH()); 	
		 //-------------- FNF Voice Service Duration  ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFVZDUR()); 	
		 //-------------- FNF Max Number of Friends - Voice All Destination ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFVZMAXNUMELE_TD()); 	
		 //-------------- FNF Voice Duration Till Renew ---------------------------------------------------------------------------------------	
		colini = colini +1;		
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getFFVZDURTILLRE()); 		
		
	 // -------------------------------------------------------		
				
	    //----------------- Product Offering:Related Attributes:Entry Index    --------------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(1);
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Attribute --------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("AssignableAttribute;UniqueIdentifier=\"Element Name\""));				
		//if(cell ==null) cell = row.createCell(mapBO.get("AssignableAttribute;UniqueIdentifier=\"Element Name\""));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("Equipment Classification SE");
		
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Constant Values --------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("ConstantValues"));				
		//if(cell ==null) cell = row.createCell(mapBO.get("ConstantValues"));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		if(DG.getTMCODE() ==0 )
		    cell.setCellValue("TODOS");
		else {if(DG.getDESC_EQUIPO().trim().equals("N/A")) cell.setCellValue(""); 
		      else cell.setCellValue(DG.getDESC_EQUIPO().trim().toUpperCase());	
		}
		
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("Domain;Template=\"name\""));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Domain;Template=\"name\""));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("EnumerationDomain");
		//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain Element Name--------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get("Domain;UniqueIdentifier=\"Element Name\""));				
		//if(cell ==null) cell = row.createCell(mapBO.get("Domain;UniqueIdentifier=\"Element Name\""));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue("AbsEquipmentClassification");
		//--------------Legacy Code-----------------------------------------------------------------------------
		colini = colini +1;
		//cell = row.getCell(mapBO.get(mapBO.get("Domain;UniqueIdentifier=\"Element Name\"")+1));				
		//if(cell ==null) cell = row.createCell(mapBO.get(mapBO.get("Domain;UniqueIdentifier=\"Element Name\"")+1));
		cell = row.getCell(colini);				
		if(cell ==null) cell = row.createCell(colini);
		cell.setCellValue(DG.getTMCODE());	
		fila++;
		for(int j = 0;j<2;j++){//2
			if(j==2 && DG.getTIPO_PLAN()!=null){
				continue;
			}else{
	           colini = col;
			   row = hoja.getRow(fila); 
			   if(row==null)  row = hoja.createRow(fila);		  
			    colini = colini +1; 
			    //cell = row.getCell(mapBO.get("Template=\"ProductOffering\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));				
				//if(cell ==null) cell = row.createCell(mapBO.get("Template=\"ProductOffering\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);
				cell.setCellValue(DG.getDESC_BO());			
				//-----------------------
				colini = colini +10;
			///	cell = row.getCell(mapBO.get("SalesChannel;UniqueIdentifier=\"Element Name\""));				
			//	if(cell ==null) cell = row.createCell(mapBO.get("SalesChannel;UniqueIdentifier=\"Element Name\""));
				cell = row.getCell(colini);				
				if(cell ==null) cell = row.createCell(colini);				
				if(j==0){
					    //if(DG.getTMCODE()==13620)
						//	cell.setCellValue("IVR");
					   if(canalventa[0].equals("TODOS"))
						   cell.setCellValue("");
					   else if(canalventa.length>1)
						   cell.setCellValue(canalventa[1].trim());
					   else cell.setCellValue("");					
	         			
					 //----------------- Product Offering:Related Attributes:EligibilityRule;UniqueIdentifier="Element Name" --------------------------------------
					   colini = colini +23;
						cell = row.getCell(colini);	
						if(DG.getVIGENTE().equals("S")&&(!DG.getOFER_CAMBIOPLAN().equals("S")&&!DG.getOFER_VENTA().equals("S"))){
							if(DG.getZONA().trim().equals("COS")){
								if(cell ==null) cell = row.createCell(colini);
								cell.setCellValue("Offer valid for COSTA region Only");				   
								}
							}
					   
					   //----------------- Product Offering:Related Attributes:Entry Index    --------------------------------------------------------------
						//colini = colini +82;
						colini = colini +60;
						//cell = row.getCell(mapBO.get("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));				
						//if(cell ==null) cell = row.createCell(mapBO.get("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));
						cell = row.getCell(colini);	
						 if(cell ==null) cell = row.createCell(colini);					
						   cell.setCellValue("2");
								
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Attribute --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("AssignableAttribute;UniqueIdentifier=\"Element Name\""));				
						//if(cell ==null) cell = row.createCell(mapBO.get("AssignableAttribute;UniqueIdentifier=\"Element Name\""));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("Plan Type SE");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Constant Values --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("ConstantValues"));				
						//if(cell ==null) cell = row.createCell(mapBO.get("ConstantValues"));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);					
						
						//if(DG.getTMCODE() ==0 )
						//	cell.setCellValue(" ");
						//else
						if(DG.getTIPOPLAN().trim().equals("Mixto")){
							cell.setCellValue("PR");
							}
						else if(DG.getTIPOPLAN().trim().equals("Abierto")){
							cell.setCellValue("PR");
							}
						else cell.setCellValue("PR");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("Domain;Template=\"name\""));				
						//if(cell ==null) cell = row.createCell(mapBO.get("Domain;Template=\"name\""));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("EnumerationDomain");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("Domain;UniqueIdentifier=\"Element Name\""));				
						//if(cell ==null) cell = row.createCell(mapBO.get("Domain;UniqueIdentifier=\"Element Name\""));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("AbsHybridType");
				///------------------------------------------------------	
				}				
				if(j==1){
					   /*if(DG.getTMCODE()==13620)
						  cell.setCellValue("Super User");*/
						if(canalventa[0].equals("TODOS"))
						   cell.setCellValue("");
					    else if(canalventa.length>2)
						   cell.setCellValue(canalventa[2].trim());
					    else cell.setCellValue("");
					
					    //    ----------------- Product Offering:Related Attributes:Entry Index    -------------------------------------------------------------- 
						colini = colini +83;
						//cell = row.getCell(mapBO.get("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));				
						//if(cell ==null) cell = row.createCell(mapBO.get("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1"));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue(3);
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Attribute --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("AssignableAttribute;UniqueIdentifier=\"Element Name\""));				
						//if(cell ==null) cell = row.createCell(mapBO.get("AssignableAttribute;UniqueIdentifier=\"Element Name\""));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("Plan Sub Type SE");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Constant Values --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("ConstantValues"));				
						//if(cell ==null) cell = row.createCell(mapBO.get("ConstantValues"));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						if(DG.getTMCODE() ==0 ){ cell.setCellValue("VO");}
						else if(DG.getSUBTYPE().equals("N/A")) cell.setCellValue("");
						else cell.setCellValue(DG.getSUBTYPE());
						
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("Domain;Template=\"name\""));				
						//if(cell ==null) cell = row.createCell(mapBO.get("Domain;Template=\"name\""));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("EnumerationDomain");
						//----------------- Product Offering:Related Attributes:ProductAttributeRelation:Domain --------------------------------------
						colini = colini +1;
						//cell = row.getCell(mapBO.get("Domain;UniqueIdentifier=\"Element Name\""));				
						//if(cell ==null) cell = row.createCell(mapBO.get("Domain;UniqueIdentifier=\"Element Name\""));
						cell = row.getCell(colini);				
						if(cell ==null) cell = row.createCell(colini);
						cell.setCellValue("AbsPlanSubtype");	}
				
				
			 /*     if(j==2) {//if(DG.getTMCODE()==13620)cell.setCellValue("");
					if(canalventa[0].equals("TODOS"))
						   cell.setCellValue("");
					else if(canalventa.length>3)
						   cell.setCellValue(canalventa[3].trim());
				    else cell.setCellValue("");		
				}*/	
				//if(j==3) //&& DG.getTIPO_PLAN()==null cell.setCellValue("Self Service");	
				//if(j==4) cell.setCellValue("MASS");	
				//if(j==5) cell.setCellValue("OMS");
				//if(j==6) cell.setCellValue("CVC");
				//if(j==7) cell.setCellValue("CAC");
				//if(j==8) cell.setCellValue("IVR");
				//if(j==9) cell.setCellValue("Retailer");
				//if(j==10) cell.setCellValue("Temporary");				
				if(j!=1) fila ++;
								}
							 }//termina ciclo for 	
		       colini = col;
		       fila++;	      	     
			}
	    }
//-------------------------------Fin de general info para planes prepago------------------------------------------------------------------------
public static void LLenarPE(Workbook wb, int fil){
	IDataModel model = new DataModel();	
	Sheet hoja = wb.getSheet("Component");
	List <productOffering> BOCol= model.getBO1();
	int fila = 10 ;
	int col =0;
	int temp = 0;	
	int contador = 0;
	 for(productOffering bo:BOCol ){				 
		 temp= col;		 
		 Row row = hoja.getRow(fila); 
		 if(row==null) row = hoja.createRow(fila );		  
		 //**********************************************//
		 Cell cell = row.getCell(temp); 
		if (cell == null) cell = row.createCell(temp);
		 if(contador == 0) {
			 cell.setCellValue("");
			 //......................... Name:English ......................................
			 temp ++;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("Base Plan");
			
			 /*
			 //......................... SellableInd ......................................
			 temp = temp+12;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("FALSE");
			//......................... AllowSuspendInd ......................................
			 temp ++;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("FALSE");
			//......................... Service Type ......................................
			 temp ++;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("PP"); 
			//......................... Installation Address Required ......................................
			 temp = temp +3;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("TRUE"); 
			//......................... Entry Index ......................................
			 temp = temp +4;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue(1);
			//......................... Purpose ......................................
			 temp++;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("DE");
			 */			 
			 
			 //......................... Entry Index ......................................
			 //temp= temp+3;
			 temp= temp+5; 
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue(1);
			//......................... Billing Offer Name - Inclusion ......................................
				temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 cell.setCellValue("Mandatory");	
				//......................... Billing Offer Name - EnableForSelection ......................................
	 			temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 cell.setCellValue("FALSE");		 
			 
			//......................... Billing Offer Name - Template name ......................................
			 // temp= temp+3;
			 temp= temp+2;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("BillingOffer");
			//......................... Billing Offer Name - name ......................................
			 temp++;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue(bo.getDESC_BO());
			 
			//......................... ProductPriceRelation:Attribute Mapping - Entry Index ......................................
			if(bo.getATRMAP_TOPO()!=null) {
			if(bo.getATRMAP_TOPO().equals("X")&& !bo.getNUM_ELEG().equals("0")){
					 temp++;
					 cell = row.getCell(temp); 
					 if (cell == null) cell = row.createCell(temp);
					 cell.setCellValue("1");	
					 
		    //......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Ignore Domain Method ......................................
					 temp++;
					 cell = row.getCell(temp); 
					 if (cell == null) cell = row.createCell(temp);
					 cell.setCellValue("TRUE");	 
			//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Template name ......................................
					 temp++;
					 cell = row.getCell(temp); 
					 if (cell == null) cell = row.createCell(temp);
					 cell.setCellValue("Attribute");	 		 
			//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Name ......................................
					 temp++;
					 cell = row.getCell(temp); 
					 if (cell == null) cell = row.createCell(temp);
					 cell.setCellValue("In Plan Friend & Family List");	
			//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Template name ......................................
					 temp++;
					 cell = row.getCell(temp); 
					 if (cell == null) cell = row.createCell(temp);
					 cell.setCellValue("Attribute"); 
			//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Name ......................................
					 temp++;
					 cell = row.getCell(temp); 
					 if (cell == null) cell = row.createCell(temp);
					 cell.setCellValue("Friends numbers"); 	
			//......................... Entry Index ......................................
			/*		 temp = temp +23;
					 cell = row.getCell(temp); 
					 if (cell == null) cell = row.createCell(temp);
					 cell.setCellValue(1);				 
			*/ 
				 }}
		 
			 contador ++; 		 
			 fila ++;
		 }
		 else {
			 contador++;			 
			 temp = 0;
			  row = hoja.getRow(fila); 
			  if(row==null)  row = hoja.createRow(fila);	
			 //*******************************Entry Index****************************************
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("");
			//------------------------------- Template name -------------------------------------------------
			 temp ++;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("Base Plan");			 
			//------------------------------- Entry Index -------------------------------------------------
			 //temp = temp+25;
			  temp = temp+5;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue(contador);
			//......................... Billing Offer Name - Inclusion ......................................
				temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 cell.setCellValue("Mandatory");	
			//......................... Billing Offer Name - EnableForSelection ......................................
	 			temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 cell.setCellValue("FALSE");		 
			 
		     //temp = temp +3
			 temp = temp +2;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue("BillingOffer");
			 //-------------------------------  name -------------------------------------------------
			 temp++;
			 cell = row.getCell(temp); 
			 if (cell == null) cell = row.createCell(temp);
			 cell.setCellValue(bo.getDESC_BO()); 
			//......................... ProductPriceRelation:Attribute Mapping - Entry Index ......................................
			 if(bo.getATRMAP_TOPO()!=null) {
			 if( bo.getATRMAP_TOPO().trim().equals("X")&& !bo.getNUM_ELEG().equals("0")){
						 temp++;
						 cell = row.getCell(temp); 
						 if (cell == null) cell = row.createCell(temp);
						 cell.setCellValue("1");	
						 
			    //......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Ignore Domain Method ......................................
						 temp++;
						 cell = row.getCell(temp); 
						 if (cell == null) cell = row.createCell(temp);
						 cell.setCellValue("TRUE");	 
				//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Template name ......................................
						 temp++;
						 cell = row.getCell(temp); 
						 if (cell == null) cell = row.createCell(temp);
						 cell.setCellValue("Attribute");	 		 
				//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Name ......................................
						 temp++;
						 cell = row.getCell(temp); 
						 if (cell == null) cell = row.createCell(temp);
						 cell.setCellValue("In Plan Friend & Family List");	
				//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Template name ......................................
						 temp++;
						 cell = row.getCell(temp); 
						 if (cell == null) cell = row.createCell(temp);
						 cell.setCellValue("Attribute"); 
				//......................... ProductPriceRelation:Attribute Mapping - AttributeMapping:Name ......................................
						 temp++;
						 cell = row.getCell(temp); 
						 if (cell == null) cell = row.createCell(temp);
						 cell.setCellValue("Friends numbers"); 	
				//......................... Entry Index ......................................
						 temp = temp +23;
						 cell = row.getCell(temp); 
						 if (cell == null) cell = row.createCell(temp);
						 //cell.setCellValue(1);				 
			                 }
					 }			 
			 
		     fila ++;	    }	 
		 				}
	 		}
    public static Hashtable<String, Integer> insertarFilas(String archivo) throws Exception{
    	/** Definicion de cantidad de Registros de  componentes en la hoja de PS.(Componetes) 
    	    String archivo = "D:\\AMX EPC Wireless Product Spec Version 2011-12-22.xlsx"; **/
    	InputStream cargaLibroDet = new FileInputStream(archivo);   
    	Workbook wbDet = WorkbookFactory.create(cargaLibroDet);
    	Hashtable<String, Integer> hashtable = new Hashtable<String, Integer>();
    	int contINT =0;
    	int contBB = 0;
    	int contSMS = 0;
    	int contVLL = 0;
    	int contIDCall =0;
    	int contChatPack =0;
    	int contSinchro = 0;
    	int contCascada = 0;
    	int contVotoVo = 0;
    	int contLegis = 0;
    	int contInsurance= 0;
    	int contLDI = 0;
    	int contEquip = 0;    	
    	int cont1OnnetFnF = 0;
    	int cont3OnnetFnF = 0;
    	int cont4OnnetFnF = 0;
    	int cont6OnnetFnF = 0;
    	int cont9OnnetFnF = 0;
    	contSimPrep = 0;
    	contPlanCommit = 0;
    	contEquiCommit = 0;
    	cont0OnnetFnF = 0;
    	contFulFill = 0;
    	contTelDir = 0;
    	int contPasamin = 0;
    	int contWelcome = 0;
    	int contRingBackTone =0;
    	contWindowsMob = 0; 
    	int contDetLlamada =0;
    	contNokia = 0;
    	int contFamilyGroup = 0;
    	contWEBTV = 0;
    	contWAP = 0;
    	contBuzonVoz = 0;
    	contFnFVFijo = 0;    
    	int contSmsFnFonnet = 0;
    	IDataModel model = new DataModel();	
    	List<productOffering> BOCol= model.getBO();
    	List<productOffering> paq = model.getBOColumna();
    	String eleg = "0";
    	for(productOffering pa: paq  ){  
    		eleg ="0";
    		if(pa.getTIPO_PAQ().trim().equals("Internet")) contINT++;
    		if(pa.getTIPO_PAQ().trim().equals("Blackberry")) contBB++;
    		if(pa.getTIPO_PAQ().trim().equals("SMS")) contSMS++;
    		if(pa.getTIPO_PAQ().trim().equals("VideoLlamada")) contVLL++;
    		//-----------------------------------------------------------------
    		if(pa.getTIPO_PLAN_FAM()!=null) contFamilyGroup++;
    		if(pa.getTIPO_PAQ().trim().equals("DetalleLlamada")) contDetLlamada++;
    		if(pa.getTIPO_PAQ().trim().equals("Equipos")) contEquip++;
    		if(pa.getTIPO_PAQ().trim().contains("BienRepo")) contWelcome++;
    		if(pa.getTIPO_PAQ().trim().equals("ChatPack")) contChatPack++;
    		if(pa.getTIPO_PAQ().trim().equals("WindowsMobile")) contWindowsMob++;
    		if(pa.getTIPO_PAQ().trim().equals("Synchronica")) contSinchro++;
    		if(pa.getTIPO_PAQ().trim().contains("LDI")) contLDI++;
    		if(pa.getTIPO_PAQ().trim().contains("Asistencia")||pa.getTIPO_PAQ().trim().equals("Andiasistencia")) contInsurance++;
    		if(pa.getTIPO_PAQ().trim().equals("LEGISMOVIL")) contLegis++;
    		if(pa.getTIPO_PAQ().trim().equals("RingBackTone")) contRingBackTone++;
    		if(pa.getTIPO_PAQ().trim().equals("VozAVoz")) contVotoVo++;
    		if(pa.getTIPO_PAQ().trim().equals("RollOver")) contPasamin++;
    		if(pa.getTIPO_PAQ().trim().equalsIgnoreCase("RecordacionMIN")) contRecMIN++;
    		if(pa.getTIPO_PAQ().trim().equalsIgnoreCase("Short Mail")) contSMail++;
    		if(pa.getTIPO_PAQ().trim().equalsIgnoreCase("Web Message")) contWMess++;
    		if(pa.getTIPO_PAQ().trim().contains("IdLlamada")) contIDCall++;
    		if(pa.getTIPO_PAQ().trim().contains("Cascada")) contCascada++;
    		if(pa.getTIPO_PAQ().trim().equals("SIMCard")) contSimPrep++;
    		if(pa.getTIPO_PAQ().trim().equals("SANCIONPLAN"))contPlanCommit++;
    		if(pa.getTIPO_PAQ().trim().equals("SANCIONEQUIPO"))contEquiCommit++;
    		if(pa.getTIPO_PAQ().trim().equals("NokiaMessaging")) contNokia++;    		
    		if(pa.getTIPO_PAQ().trim().equals("FnFVozOnnet")&& pa.getNUM_ELEG().equals("1")) cont1OnnetFnF++;    	
    		if(pa.getTIPO_PAQ().trim().equals("FnFVozOnnet")&& pa.getNUM_ELEG().equals("3")) cont3OnnetFnF++;
    		if(pa.getTIPO_PAQ().trim().equals("FnFVozOnnet")&& pa.getNUM_ELEG().equals("4")) cont4OnnetFnF++;
    		if(pa.getTIPO_PAQ().trim().equals("FnFVozOnnet")&& pa.getNUM_ELEG().equals("6")) cont6OnnetFnF++;
    		if(pa.getTIPO_PAQ().trim().equals("FnFVozOnnet")&& pa.getNUM_ELEG().equals("9")) cont9OnnetFnF++;
    		if(pa.getTIPO_PAQ().trim().equals("FnFVozOnnet")&& pa.getNUM_ELEG().equals("99999999")) cont0OnnetFnF++;    		
    		if(pa.getTIPO_PAQ().trim().contains("WEBTV")&& pa.getNUM_ELEG().equals("0"))  contWEBTV++;
    		if(pa.getTIPO_PAQ().trim().contains("WAP")&& pa.getNUM_ELEG().equals("0"))  contWAP++;
    		if(pa.getTIPO_PAQ().trim().contains("BuzonVoz")&& pa.getNUM_ELEG().equals("0")) contBuzonVoz++;    		
    		if(pa.getTIPO_PAQ().trim().contains("FnFSMSOnnet")||pa.getTIPO_PAQ().trim().equals("FnFVozFijo"))
    		   eleg = pa.getNUM_ELEG();
    		if(pa.getTIPO_PAQ().trim().contains("FnFSMSOnnet")&& pa.getNUM_ELEG().equals(eleg))contSmsFnFonnet++;
    		if(pa.getTIPO_PAQ().trim().equals("FnFVozFijo")&& pa.getNUM_ELEG().equals(eleg)) contFnFVFijo++;  
    		if(pa.getTIPO_PAQ().trim().contains("Reposicion")&& pa.getNUM_ELEG().equals(eleg))contFulFill++;
    		if(pa.getTIPO_PAQ().trim().contains("CobroDirTel")&& pa.getNUM_ELEG().equals(eleg))contTelDir++;
    	}    	
    	System.out.println("FnF1 = " +cont1OnnetFnF);
    	int finReg = 0;
    	Sheet hoja = null;
       	
    	hoja = wbDet.getSheet("Component");        	
       	finReg =  contDetLlamada +cantRow;  
       	System.out.println(finReg + ",  contDetLLama:  "+contDetLlamada );
        if( contDetLlamada > 0)
               hoja.shiftRows(regInicio1,  finReg, contDetLlamada);       
        hashtable.put("DetalleLlamada", regInicio1);
        finReg = finReg + BOCol.size();
        FileOutputStream fileOutDet = new FileOutputStream(archivo);   
    	wbDet.write(fileOutDet); 
    	fileOutDet.close();         	
       	regInicio1 = regInicio1+contDetLlamada +regWM_PB ;
        	
       /**	
        * insertamos billing offer para detalle llamada en el codigo anterior
        */       	
       	InputStream cargaLibro = new FileInputStream(archivo);   
    	Workbook wb = WorkbookFactory.create(cargaLibro); 
        hoja = null;
       	hoja = wb.getSheet("Component");  
    	int iniReg1 = regInicio1 + BOCol.size() ;
       	//int iniReg = regInicio + BOCol.size() - regAddPB ;
       	//int val = BOCol.size()- regAddPB;
    	if(BOCol.size()>0)
    		hoja.shiftRows(regInicio1,  finReg, BOCol.size() );
        System.out.print(" registro inicio BP: " + regInicio1);
        FileOutputStream fileOut = new FileOutputStream(archivo);   
    	wb.write(fileOut); 
    	fileOut.close();   
    	System.out.println( "Inter: "+ contINT+" BB: "+contBB+ " SMS: "+ contSMS + " VLL: "+contVLL);
    	hashtable.put("BasePlan", regInicio1); 
    	
    	//-------------------   para paquetes Buzon de mensajes- o Voice mail -----------------------------------
    	InputStream cargaLibroVM = new FileInputStream(archivo);   
    	Workbook wbvm = WorkbookFactory.create(cargaLibroVM);
    	hoja = wbvm.getSheet("Component");
    	//iniReg = iniReg + regPB_CallID;
    	iniReg1 = iniReg1 + regPB_VMail;
    	hashtable.put("BuzonVoz", iniReg1 );
    	finReg = finReg +contBuzonVoz;
    	if(contBuzonVoz>0)
    		hoja.shiftRows(iniReg1, finReg, contBuzonVoz );
    	System.out.print(" valor fiN: " + finReg);
    	FileOutputStream fileOutvm = new FileOutputStream(archivo);   
    	wbvm.write(fileOutvm); 
    	fileOutvm.close();	  
    	
    	//-------------------   para paquetes ID_llamada -----------------------------------
    	InputStream cargaLibroid = new FileInputStream(archivo);   
    	Workbook wbid = WorkbookFactory.create(cargaLibroid);
    	hoja = wbid.getSheet("Component");
    	iniReg1 = iniReg1 + regVMail_CallID +contBuzonVoz ;
    	hashtable.put("IdLlamada", iniReg1 );
      	finReg = finReg +contIDCall;
      	if(contIDCall > 0)
      		hoja.shiftRows(iniReg1, finReg, contIDCall );
    	System.out.print(" valor fiN: " + finReg);
    	FileOutputStream fileOutid = new FileOutputStream(archivo);   
    	wbid.write(fileOutid); 
    	fileOutid.close();	 	
    	
    	//-------------------   para paquetes INTERNET -----------------------------------
    	InputStream cargaLibro2 = new FileInputStream(archivo);   
    	Workbook wb2 = WorkbookFactory.create(cargaLibro2);
    	hoja = wb2.getSheet("Component");
    	iniReg1 = iniReg1 +regCallID_Inter+ contIDCall;
    	hashtable.put("Internet", iniReg1 );
      	finReg = finReg +contINT;
      	if(contINT>0)
      		hoja.shiftRows(iniReg1, finReg, contINT );
    	System.out.print(" valor fiN: " + finReg);
    	FileOutputStream fileOut2 = new FileOutputStream(archivo);   
    	wb2.write(fileOut2); 
    	fileOut2.close();
    	
    	//--------------------   para paquetes WAP ---------------------------------------
    	InputStream cargaLibrowap = new FileInputStream(archivo);   
    	Workbook wbwp = WorkbookFactory.create(cargaLibrowap);
    	hoja = wbwp.getSheet("Component");
    	iniReg1 = iniReg1 +regInter_WAP+contINT ;
    	hashtable.put("WAP", iniReg1 );
      	finReg = finReg +contWAP;
      	if(contWAP>0)
      		hoja.shiftRows(iniReg1, finReg, contWAP );
    	System.out.print(" valor fiN: " + finReg);
    	FileOutputStream fileOutwap = new FileOutputStream(archivo);   
    	wbwp.write(fileOutwap); 
    	fileOutwap.close();   
    	//--------------------   para paquetes NokiaMessaging ---------------------------------------
     	InputStream cargaLibronk = new FileInputStream(archivo);   
    	Workbook wbnk = WorkbookFactory.create(cargaLibronk);
    	hoja = wbnk.getSheet("Component");
       	iniReg1 = iniReg1 +contWAP +regWAP_NM ;
     	hashtable.put("NokiaMessaging", iniReg1);
    	finReg = finReg +contNokia;
    	System.out.print(" valor fiN: " + finReg);
    	if(contNokia>0)
    		hoja.shiftRows(iniReg1, finReg, contNokia);
        FileOutputStream fileOutnok = new FileOutputStream(archivo);   
    	wbnk.write(fileOutnok); 
    	fileOutnok.close();	
       	//--------------------   para paquetes BB ---------------------------------------
     	InputStream cargaLibro3 = new FileInputStream(archivo);   
    	Workbook wb3 = WorkbookFactory.create(cargaLibro3);
    	hoja = wb3.getSheet("Component");
      	iniReg1 = iniReg1 +contNokia +regNM_BB ;
     	hashtable.put("Blackberry", iniReg1);
    	finReg = finReg +contBB;
    	System.out.print(" valor fiN: " + finReg);
    	if(contBB>0)
    		hoja.shiftRows(iniReg1, finReg, contBB);
        FileOutputStream fileOut3 = new FileOutputStream(archivo);   
    	wb3.write(fileOut3); 
    	fileOut3.close();
    
    	//---------------------  para paquetes Chat Pack Windows --------------------------------------
    	InputStream cargaLibroC = new FileInputStream(archivo);   
    	Workbook wbC = WorkbookFactory.create(cargaLibroC);
    	hoja = wbC.getSheet("Component");
    	iniReg1 = iniReg1 + contBB+ regBB_ChPW;
    	hashtable.put("ChatPack", iniReg1); 
    	finReg = finReg +contChatPack ;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contChatPack>0)
    		hoja.shiftRows(iniReg1, finReg, contChatPack );
    	FileOutputStream fileOutC = new FileOutputStream(archivo);   
    	wbC.write(fileOutC); 
    	fileOutC.close();
    	//-----------------------Para paquetes Synchro ------------------------------------
        InputStream cargaLibroS = new FileInputStream(archivo);   
    	Workbook wbS = WorkbookFactory.create(cargaLibroS);
    	hoja = wbS.getSheet("Component");
    	iniReg1 = iniReg1 + contChatPack + regChPW_Syn;
       	hashtable.put("Synchronica", iniReg1);
    	finReg = finReg +contSinchro;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contSinchro>0)
    		hoja.shiftRows(iniReg1,finReg, contSinchro);  
    	FileOutputStream fileOutS = new FileOutputStream(archivo);   
    	wbS.write(fileOutS); 
    	fileOutS.close();
    	//-----------------------Para paquetes SMS ------------------------------------
        InputStream cargaLibroSM = new FileInputStream(archivo);   
    	Workbook wbSM = WorkbookFactory.create(cargaLibroSM);
    	hoja = wbSM.getSheet("Component");
    	iniReg1 = iniReg1 + contSinchro + regSyn_SMS;
       	hashtable.put("SMS", iniReg1);
    	finReg = finReg +contSMS;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contSMS>0)
    		hoja.shiftRows(iniReg1,finReg, contSMS);  
    	FileOutputStream fileOutSM = new FileOutputStream(archivo);   
    	wbSM.write(fileOutSM); 
    	fileOutSM.close();
    	
    	//-----------------------Para paquetes WebMessage ------------------------------------
        InputStream cargaLibroWM = new FileInputStream(archivo);   
    	Workbook wbWM = WorkbookFactory.create(cargaLibroWM);
    	hoja = wbWM.getSheet("Component");
    	iniReg1 = iniReg1 + contSMS + regSMS_WMess;
       	hashtable.put("Web Message", iniReg1);
    	finReg = finReg +contWMess;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contWMess>0)
    		hoja.shiftRows(iniReg1,finReg, contWMess);  
    	FileOutputStream fileOutWM = new FileOutputStream(archivo);   
    	wbWM.write(fileOutWM); 
    	fileOutWM.close();
    	//-----------------------Para paquetes Short Mail------------------------------------
        InputStream cargaLibroSMail = new FileInputStream(archivo);   
    	Workbook wbSMail = WorkbookFactory.create(cargaLibroSMail);
    	hoja = wbSMail.getSheet("Component");
    	iniReg1 = iniReg1 + contWMess + regWMess_SMail;
       	hashtable.put("Short Mail", iniReg1);
    	finReg = finReg +contSMail;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contSMail>0)
    		hoja.shiftRows(iniReg1,finReg, contSMail);  
    	FileOutputStream fileOutSMail = new FileOutputStream(archivo);   
    	wbSMail.write(fileOutSMail); 
    	fileOutSMail.close();   	
       	//-----------------------Para paquetes RingBackTone ------------------------------------
        InputStream cargaLibroV = new FileInputStream(archivo);   
    	Workbook wbV = WorkbookFactory.create(cargaLibroV);
    	hoja = wbV.getSheet("Component");
    	iniReg1 = iniReg1 + contSMail + regSMail_RBT;
    	hashtable.put("RingBackTone", iniReg1);
    	finReg = finReg +contRingBackTone;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contRingBackTone > 0)
    		hoja.shiftRows(iniReg1,finReg, contRingBackTone);  
    	FileOutputStream fileOutV = new FileOutputStream(archivo);   
    	wbV.write(fileOutV); 
    	fileOutV.close();    	
    	//-----------------------Para paquetes cascada ------------------------------------
        InputStream cargaLibroCas = new FileInputStream(archivo);   
    	Workbook wbCas = WorkbookFactory.create(cargaLibroCas);
    	hoja = wbCas.getSheet("Component");
    	iniReg1 = iniReg1 + contRingBackTone + regRBT_Casc;
       	hashtable.put("Cascada", iniReg1);
    	finReg = finReg + contCascada;
    	 System.out.print(" valor fiN: " + finReg);
        if(contCascada > 0)
        	hoja.shiftRows(iniReg1,finReg, contCascada);  
    	FileOutputStream fileOutcas = new FileOutputStream(archivo);   
    	wbCas.write(fileOutcas); 
    	fileOutcas.close();
    	//-----------------------Para paquetes VoicetoVoice ------------------------------------
        InputStream cargaLibrovoz = new FileInputStream(archivo);   
    	Workbook wbvoz = WorkbookFactory.create(cargaLibrovoz);
    	hoja = wbvoz.getSheet("Component");
    	iniReg1 = iniReg1 + contCascada + regCasc_VToV ;
       	hashtable.put("VozAVoz", iniReg1);
    	finReg = finReg + contVotoVo;
    	System.out.print(" valor fiN: " + finReg);
    	if(contVotoVo > 0)
    		hoja.shiftRows(iniReg1,finReg, contVotoVo);  
    	FileOutputStream fileOutvoz = new FileOutputStream(archivo);   
    	wbvoz.write(fileOutvoz); 
    	fileOutvoz.close();
    	//-----------------------Para paquetes legis ------------------------------------
        InputStream cargaLibroL = new FileInputStream(archivo);   
    	Workbook wbL = WorkbookFactory.create(cargaLibroL);
    	hoja = wbL.getSheet("Component");
    	iniReg1 = iniReg1 + contVotoVo + regVToV_Legis;
       	hashtable.put("LEGISMOVIL", iniReg1);
    	finReg = finReg +contLegis;
    	System.out.print(" valor fiN: " + finReg);
    	 if(contLegis > 0)
    		 hoja.shiftRows(iniReg1,finReg, contLegis);  
    	FileOutputStream fileOutL = new FileOutputStream(archivo);   
    	wbL.write(fileOutL); 
    	fileOutL.close();
    	//-----------------------Para paquetes webtv ------------------------------------
        InputStream cargaLibroweb = new FileInputStream(archivo);   
    	Workbook wbweb = WorkbookFactory.create(cargaLibroweb);
    	hoja = wbweb.getSheet("Component");
    	iniReg1 = iniReg1 + contLegis + regLegis_WEBTV;
       	hashtable.put("WEBTV", iniReg1);
    	finReg = finReg +contWEBTV;
    	 System.out.print(" valor fiN: " + finReg);
    	 if(contWEBTV>0)
    		 hoja.shiftRows(iniReg1,finReg, contWEBTV);  
    	FileOutputStream fileOutweb = new FileOutputStream(archivo);   
    	wbweb.write(fileOutweb); 
    	fileOutweb.close();    	
    	//-----------------------Para paquetes Seguro ------------------------------------
        InputStream cargaLibroSe = new FileInputStream(archivo);   
    	Workbook wbSe = WorkbookFactory.create(cargaLibroSe);
    	hoja = wbSe.getSheet("Component");
    	iniReg1 = iniReg1 + contWEBTV + regWEBTV_Insu;
       	hashtable.put("sistencia", iniReg1);
    	finReg = finReg +contInsurance;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contInsurance>0)
    		hoja.shiftRows(iniReg1,finReg, contInsurance);  
    	FileOutputStream fileOutSe = new FileOutputStream(archivo);   
    	wbSe.write(fileOutSe); 
    	fileOutSe.close();
    	//-----------------------Para paquetes LDI------------------------------------
        InputStream cargaLibroLD = new FileInputStream(archivo);   
    	Workbook wbLD = WorkbookFactory.create(cargaLibroLD);
    	hoja = wbLD.getSheet("Component");
    	iniReg1 = iniReg1 + contInsurance + regInsu_LDI;
       	hashtable.put("LDI", iniReg1);
    	finReg = finReg +contLDI;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contLDI > 0)
    	hoja.shiftRows(iniReg1,finReg, contLDI);  
    	FileOutputStream fileOutLD = new FileOutputStream(archivo);   
    	wbLD.write(fileOutLD); 
    	fileOutLD.close();
    	//-----------------------Para paquetes Equipo ------------------------------------
        InputStream cargaLibroEq = new FileInputStream(archivo);   
    	Workbook wbEq = WorkbookFactory.create(cargaLibroEq);
    	hoja = wbEq.getSheet("Component");
    	iniReg1 = iniReg1 + contLDI + regLDI_Equip;
       	hashtable.put("Equipo", iniReg1);
    	finReg = finReg + contEquip;
    	System.out.print(" valor fiN: " + finReg);
    	if(contEquip>0)
    		hoja.shiftRows(iniReg1,finReg,  contEquip);  
    	FileOutputStream fileOutEq = new FileOutputStream(archivo);   
    	wbEq.write(fileOutEq); 
    	fileOutEq.close();    	
     	//-----------------------Para paquetes SIMCard  ------------------------------------------------
     	InputStream cargaLibSiMcard = new FileInputStream(archivo);   
     	Workbook wbSimCard = WorkbookFactory.create(cargaLibSiMcard);
     	hoja = wbSimCard.getSheet("Component");
     	iniReg1 = iniReg1 +contEquip + regEqui_SIMCard;
        hashtable.put("SIMCard", iniReg1);
     	finReg = finReg + contSimPrep;
     	System.out.print(" valor fiN: " + finReg);
     	if(contSimPrep>0)
     		hoja.shiftRows(iniReg1,finReg,  contSimPrep);  
     	FileOutputStream fileOutSim = new FileOutputStream(archivo);   
     	wbSimCard.write(fileOutSim); 
     	fileOutSim.close();  
     	//-----------------------Para paquetes Equipment Commitment ------------------------------------
    	InputStream cargaLibEqCommit = new FileInputStream(archivo);   
     	Workbook wbEqComm = WorkbookFactory.create(cargaLibEqCommit);
     	hoja = wbEqComm.getSheet("Component");
     	iniReg1 = iniReg1 + contSimPrep+ regSIMCard_EquiCommit; ;
    	 	hashtable.put("SANCIONEQUIPO", iniReg1);
     	finReg = finReg + contEquiCommit;
     	System.out.print(" valor fiN: " + finReg);
     	if(contEquiCommit>0)
     		hoja.shiftRows(iniReg1,finReg,  contEquiCommit);  
     	FileOutputStream fileOutEqCo = new FileOutputStream(archivo);   
     	wbEqComm.write(fileOutEqCo); 
     	fileOutEqCo.close();
     	
     	//-----------------------Para paquetes Plan Commitment ------------------------------------
    	InputStream cargaLibPlanCommit = new FileInputStream(archivo);   
     	Workbook wbPlanComm = WorkbookFactory.create(cargaLibPlanCommit);
     	hoja = wbPlanComm.getSheet("Component");
     	iniReg1 = iniReg1 + contEquiCommit+ regEquiCommit_PlanCommit;
        hashtable.put("SANCIONPLAN", iniReg1);
     	finReg = finReg + contPlanCommit;
     	System.out.print(" valor fiN: " + finReg);
     	if(contPlanCommit>0)
     		hoja.shiftRows(iniReg1,finReg,  contPlanCommit);  
     	FileOutputStream fileOutPlanCo = new FileOutputStream(archivo);   
     	wbPlanComm.write(fileOutPlanCo); 
     	fileOutPlanCo.close();      	
    	
    	//-----------------------Para paquetes FnF Voz Onnet Wireless------------------------------------
        InputStream cargaLibro0F = new FileInputStream(archivo);   
    	Workbook wb0F = WorkbookFactory.create(cargaLibro0F);
    	hoja = wb0F.getSheet("Component");
    	iniReg1 = iniReg1 +  contPlanCommit + regPlanCommit_VOnnetW;
       	hashtable.put("0FnFVozOnnet", iniReg1);
    	finReg = finReg +cont0OnnetFnF;
    	 System.out.print(" valor fiN: " + finReg);
    	if(cont0OnnetFnF>0)
    		hoja.shiftRows(iniReg1,finReg, cont0OnnetFnF);  
    	FileOutputStream fileOut0F = new FileOutputStream(archivo);   
    	wb0F.write(fileOut0F); 
    	fileOut0F.close();	    	
    	//-----------------------Para paquetes FnF Voz Fijo------------------------------------
        InputStream cargaLibroVF = new FileInputStream(archivo);   
    	Workbook wbVF = WorkbookFactory.create(cargaLibroVF);
    	hoja = wbVF.getSheet("Component");
    	iniReg1 = iniReg1 +  cont0OnnetFnF + regVOnnetW_VOnnetF;
       	hashtable.put("FnFVozFijo", iniReg1);
    	finReg = finReg +contFnFVFijo;
    	 System.out.print(" valor fiN: " + finReg);
    	 if(contFnFVFijo>0)
    		 hoja.shiftRows(iniReg1,finReg, contFnFVFijo);  
    	FileOutputStream fileOutVF = new FileOutputStream(archivo);   
    	wbVF.write(fileOutVF); 
    	fileOutVF.close();  
    	
    	//-----------------------Para paquetes 1FnF ------------------------------------
        InputStream cargaLibro1F = new FileInputStream(archivo);   
    	Workbook wb1F = WorkbookFactory.create(cargaLibro1F);
    	hoja = wb1F.getSheet("Component");
    	iniReg1 = iniReg1 + contFnFVFijo + regVOnnetF_1FnF;
       	hashtable.put("1FnFVozOnnet", iniReg1);
    	finReg = finReg + cont1OnnetFnF;
    	System.out.print(" valor fiN: " + finReg);
    	if(cont1OnnetFnF>0)
    		 hoja.shiftRows(iniReg1,finReg,  cont1OnnetFnF);  
    	FileOutputStream fileOut1F = new FileOutputStream(archivo);   
    	wb1F.write(fileOut1F); 
    	fileOut1F.close();    	
    	//-----------------------Para paquetes 3FnF  ------------------------------------
        InputStream cargaLibro3F = new FileInputStream(archivo);   
    	Workbook wb3F = WorkbookFactory.create(cargaLibro3F);
    	hoja = wb3F.getSheet("Component");    	
    	iniReg1 = iniReg1 + cont1OnnetFnF + reg1FnF_3FnF;
       	hashtable.put("3FnFVozOnnet", iniReg1);
    	finReg = finReg +cont4OnnetFnF;
    	 System.out.print(" valor fiN: " + finReg);
    	if(cont3OnnetFnF>0)
    		hoja.shiftRows(iniReg1,finReg, cont3OnnetFnF);  
    	FileOutputStream fileOut3F = new FileOutputStream(archivo);   
    	wb3F.write(fileOut3F); 
    	fileOut3F.close();
    	//-----------------------Para paquetes 4FnF  ------------------------------------
        InputStream cargaLibro4F = new FileInputStream(archivo);   
    	Workbook wb4F = WorkbookFactory.create(cargaLibro4F);
    	hoja = wb4F.getSheet("Component");    	
    	iniReg1 = iniReg1 + cont3OnnetFnF + reg3FnF_4FnF;
       	hashtable.put("4FnFVozOnnet", iniReg1);
    	finReg = finReg +cont4OnnetFnF;
    	System.out.print(" valor fiN: " + finReg);
    	if(cont4OnnetFnF>0)
    		 hoja.shiftRows(iniReg1,finReg, cont4OnnetFnF);  
    	FileOutputStream fileOut4F = new FileOutputStream(archivo);   
    	wb4F.write(fileOut4F); 
    	fileOut4F.close();  
    	//-----------------------Para paquetes Fnf sms onnet ------------------------------------
        InputStream cargaLibroSMO = new FileInputStream(archivo);   
    	Workbook wbSMO = WorkbookFactory.create(cargaLibroSMO);
    	hoja = wbSMO.getSheet("Component");    	
    	iniReg1 = iniReg1 + cont4OnnetFnF + reg4FnF_SMSOnnet;
       	hashtable.put("FnFSMSOnnet", iniReg1);
    	finReg = finReg +contSmsFnFonnet;
    	System.out.print(" valor fiN: " + finReg);
    	if(contSmsFnFonnet>0)
    		 hoja.shiftRows(iniReg1,finReg, contSmsFnFonnet);  
    	FileOutputStream fileOutSMO = new FileOutputStream(archivo);   
    	wbSMO.write(fileOutSMO); 
    	fileOutSMO.close();  	
    	
    	//-----------------------Para paquetes Video Llamada ------------------------------------
        InputStream cargaLibroRo = new FileInputStream(archivo);   
    	Workbook wbRo = WorkbookFactory.create(cargaLibroRo);
    	hoja = wbRo.getSheet("Component");
    	iniReg1 = iniReg1 +contSmsFnFonnet  +  regSMSonnet_VC;
       	hashtable.put("VideoLlamada", iniReg1);
    	finReg = finReg +contVLL;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contVLL>0)
    	hoja.shiftRows(iniReg1,finReg, contVLL);  
    	FileOutputStream fileOutRo = new FileOutputStream(archivo);   
    	wbRo.write(fileOutRo); 
    	fileOutRo.close();
    	//-----------------------Para paquetes RecordacionMIN ------------------------------------
        InputStream cargaLibroRECMIN = new FileInputStream(archivo);   
    	Workbook wbReMIN = WorkbookFactory.create(cargaLibroRECMIN);
    	hoja = wbReMIN.getSheet("Component");
    	iniReg1 = iniReg1 +contVLL + regVC_ReMIN;
       	hashtable.put("RecordacionMIN", iniReg1);
    	finReg = finReg +contRecMIN;
    	System.out.print(" valor fiN: " + finReg);
    	if(contRecMIN >0)
    		hoja.shiftRows(iniReg1,finReg, contRecMIN);  
    	FileOutputStream fileOutReMIN = new FileOutputStream(archivo);   
    	wbReMIN.write(fileOutReMIN); 
    	fileOutReMIN.close();    	
    	//-----------------------Para paquetes Pasaminutos ------------------------------------
        InputStream cargaLibroCall = new FileInputStream(archivo);   
    	Workbook wbCall = WorkbookFactory.create(cargaLibroCall);
    	hoja = wbCall.getSheet("Component");
    	iniReg1 = iniReg1 +contRecMIN + regReMIN_Pasa;
       	hashtable.put("RollOver", iniReg1);
    	finReg = finReg +contPasamin;
    	System.out.print(" valor fiN: " + finReg);
    	if(contPasamin >0)
    		hoja.shiftRows(iniReg1,finReg, contPasamin);  
    	FileOutputStream fileOutCall = new FileOutputStream(archivo);   
    	wbCall.write(fileOutCall); 
    	fileOutCall.close(); 	    	
    	//-----------------------Para paquetes FulFill ------------------------------------
        InputStream cargaLibroFulFill = new FileInputStream(archivo);   
    	Workbook wbFulFill = WorkbookFactory.create(cargaLibroFulFill);
    	hoja = wbFulFill.getSheet("Component");
    	iniReg1 = iniReg1 +contPasamin + regPasa_FulFill;
       	hashtable.put("Reposicion", iniReg1);
    	finReg = finReg +contFulFill;
    	System.out.print(" valor fiN: " + finReg);
    	if(contFulFill >0)
    		hoja.shiftRows(iniReg1,finReg, contFulFill);  
    	FileOutputStream fileFulFill = new FileOutputStream(archivo);   
    	wbFulFill.write(fileFulFill); 
    	fileFulFill.close();    	
    	//-----------------------Para paquetes bienvenida  ------------------------------------
        InputStream cargaLibroW = new FileInputStream(archivo);   
    	Workbook wbW = WorkbookFactory.create(cargaLibroW);
    	hoja = wbW.getSheet("Component");
    	iniReg1 = iniReg1 + contFulFill + reFulFill_Welc;
       	hashtable.put("BienRepo", iniReg1);
    	finReg = finReg +contWelcome;
    	System.out.print(" valor fiN: " + finReg);
    	if(contWelcome>0)
    		hoja.shiftRows(iniReg1,finReg, contWelcome);  
    	FileOutputStream fileOutW = new FileOutputStream(archivo);   
    	wbW.write(fileOutW); 
    	fileOutW.close();
    	//-----------------------Para paquetes 6FnF ------------------------------------
        InputStream cargaLibro6F = new FileInputStream(archivo);   
    	Workbook wb6F = WorkbookFactory.create(cargaLibro6F);
    	hoja = wb6F.getSheet("Component");
    	iniReg1 = iniReg1 + contWelcome + regWelc_6FnF;
       	hashtable.put("6FnFVozOnnet", iniReg1);
    	finReg = finReg +cont6OnnetFnF;
    	System.out.print(" valor fiN: " + finReg);
    	if(cont6OnnetFnF>0)
    		 hoja.shiftRows(iniReg1,finReg, cont6OnnetFnF);  
    	FileOutputStream fileOut6F = new FileOutputStream(archivo);   
    	wb6F.write(fileOut6F); 
    	fileOut6F.close();    	
    	//-----------------------Para paquetes 9FnF ------------------------------------
        InputStream cargaLibro9F = new FileInputStream(archivo);   
    	Workbook wb9F = WorkbookFactory.create(cargaLibro9F);
    	hoja = wb9F.getSheet("Component");
       	iniReg1 = iniReg1 + cont6OnnetFnF + reg6FnF_9FnF;
       	hashtable.put("9FnFVozOnnet", iniReg1);
    	finReg = finReg +cont9OnnetFnF;
    	 System.out.print(" valor fiN: " + finReg);
    	if(cont9OnnetFnF>0)
    	hoja.shiftRows(iniReg1,finReg, cont9OnnetFnF);  
    	FileOutputStream fileOut9F = new FileOutputStream(archivo);   
    	wb9F.write(fileOut9F); 
    	fileOut9F.close();
    	//-----------------------Para paquetes 9FnF ------------------------------------
        InputStream cargaLibroDirTel = new FileInputStream(archivo);   
    	Workbook wbDirTel = WorkbookFactory.create(cargaLibroDirTel);
    	hoja =  wbDirTel.getSheet("Component");
       	iniReg1 = iniReg1 + cont9OnnetFnF + reg9FnF_TelDir;
       	hashtable.put("CobroDirTel", iniReg1);
    	finReg = finReg +contTelDir;
    	 System.out.print(" valor fiN: " + finReg);
    	if(contTelDir>0)
    	hoja.shiftRows(iniReg1,finReg, contTelDir);  
    	FileOutputStream fileOutDirTel = new FileOutputStream(archivo);   
    	wbDirTel.write(fileOutDirTel); 
    	fileOutDirTel.close();
    	   	
    	/** 
    	 * Termina nueva insercion de espacios para BO en nuevos componentes 
    	 */
    	
    	return hashtable;}

 	public static void LLenarDGPaq( Workbook wb, Hashtable<String, Integer> filini,String tipo, String eleg) throws ParseException{
 		IDataModel model = new DataModel();	
 		String  flat = "0";
 		Sheet hoja = wb.getSheet("Component");
 		List<productOffering> paq = model.getBOColumna();
 		System.out.println("Tipo "+tipo);
 		int fila = (Integer)filini.get(tipo);
 		System.out.print(" Fila: "+ fila);
 		int col =0;
 		int temp = 0;	
 		int colInc = 7;
 		int contador = 0; 		
 		if(tipo.contains("FnFVozOnnet")) tipo = "FnFVozOnnet"; 		 
 		for(productOffering pa:paq ){				 
 			 temp= col;		
 			 flat ="0";
 			   if(pa.getTIPO_PAQ().equals("FnFVozFijo")||pa.getTIPO_PAQ().equals("FnFSMSOnnet")){
 				flat = pa.getNUM_ELEG().trim() ;} 
 			   else flat = eleg; 					
 		  if(pa.getTIPO_PAQ().trim().contains(tipo) && pa.getNUM_ELEG().trim().equals(flat)){
 			 if(tipo.equals("SMS")&& pa.getTIPO_PAQ().equals("FnFSMSOnnet")) 
 				 continue;
 			  
 			 Row row = hoja.getRow(fila); 
 			 if(row==null) row = hoja.createRow(fila );		  
 			 //**********************************************//
 			 Cell cell = row.getCell(temp); 
 			 if (cell == null) cell = row.createCell(temp);
 			///nuevo codigo para inluir campo de inclusion para BB e internet 
 	    		if(contador == 0 ){
 				 cell.setCellValue("");
 				//......................... Name:English  ......................................
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				     if(tipo.trim().equals("IdentificaLlamada")||tipo.trim().contains("IdLlamada") ) cell.setCellValue("Caller ID Presentation");
 				     else if(tipo.trim().equals("VideoLlamada")) cell.setCellValue("Video Call");
	 				 else if(tipo.trim().equals("ChatPack")) cell.setCellValue("Chat Pack Windows");
	 				 else if(tipo.trim().equals("RingBackTone")) cell.setCellValue("Ring Back Tone");
	 				 else if(tipo.trim().equals("Cascada")) cell.setCellValue("Cascadas Service");
	 				 else if(tipo.trim().equals("VozAVoz")) cell.setCellValue("Voice to Voice Magazine");
	 				 else if(tipo.trim().equals("LEGISMOVIL")) cell.setCellValue("Legismovil Service");
	 				 else if(tipo.trim().equals("Seguros")||tipo.trim().equals("sistencia")) cell.setCellValue("Insurance");
	 				 else if(tipo.trim().equals("LDI")) cell.setCellValue("LDI Voice Infracel");
	 				 else if(tipo.trim().contains("Equipo")) cell.setCellValue("Equipment");
	 				 else if(tipo.trim().contains("SIMCard")) cell.setCellValue("SIM Card");
	 				 else if(tipo.trim().equals("Videollamada")) cell.setCellValue("Video Call");
	 				 else if(tipo.trim().equals("RecordacionMIN")) cell.setCellValue("RECORDATION de MIN");
	 				 else if(tipo.trim().equals("RollOver")) cell.setCellValue("Pasaminutos");
	 				 else if(tipo.trim().equals("BienRepo")) cell.setCellValue("Welcome Promotion");
	 				 else if(tipo.trim().equals("SANCIONEQUIPO")) cell.setCellValue("Equipment Commitment");
	 				 else if(tipo.trim().equals("SANCIONPLAN")) cell.setCellValue("Plan Commitment");
	 				 else if(tipo.trim().equals("BuzonVoz")) cell.setCellValue("Voice Mail");
	 				 else if(tipo.trim().equals("NokiaMessaging")) cell.setCellValue("Nokia Messaging"); 	
	 				 else if(tipo.trim().contains("FnFSMSOnnet")) cell.setCellValue("SMS Onnet Wireless"); 
	 				 else if(tipo.trim().contains("FnFVozFijo")) cell.setCellValue("Voice Onnet Fix");
	 				 else if(tipo.trim().contains("CobroDirTel")) cell.setCellValue("Telephone Directory");
	 				 else if(tipo.trim().equals("WEBTV")) cell.setCellValue("Web TV");
	 				 else if(tipo.trim().contains("Web Message")) cell.setCellValue("Web Message");
	 				 else if(tipo.trim().contains("Short Mail")) cell.setCellValue("Short Mail");
	 				 else if(tipo.trim().contains("Reposicion")) cell.setCellValue("Fulfillment & Return");
	 				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("99999999"))cell.setCellValue("Voice Onnet Wireless");
	 				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("1")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
	 				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("3")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
	 				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("4")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
	 				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("6")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
	 				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("9")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
	 				 else if(eleg.equals("0")&&pa.getTIPO_PAQ().trim().equals("DetalleLlamada")) cell.setCellValue("Wireless GenPO");
	 				 else cell.setCellValue(pa.getTIPO_PAQ());	
 				 //......................... Code  ......................................
 				 //temp = temp +8;
 				 temp = temp +4;    
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				if(tipo.trim().equals("IdentificaLlamada")||tipo.trim().contains("IdLlamada") ) cell.setCellValue("Caller_ID_Presentation");
			     else if(tipo.trim().equals("VideoLlamada")) cell.setCellValue("Video_Call");
				 else if(tipo.trim().equals("ChatPack")) cell.setCellValue("Chat_Pack_Windows");
				 else if(tipo.trim().equals("RecordacionMIN")) cell.setCellValue("RECORDATION_de_MIN");
				 else if(tipo.trim().equals("RingBackTone")) cell.setCellValue("Ring_Back_Tone");
				 else if(tipo.trim().equals("Cascada")) cell.setCellValue("Cascadas_Service");
				 else if(tipo.trim().equals("VozAVoz")) cell.setCellValue("Voice_to_Voice_Magazine");
				 else if(tipo.trim().equals("LEGISMOVIL")) cell.setCellValue("Legismovil_Service");
				 else if(tipo.trim().equals("Seguros")||tipo.trim().equals("sistencia")) cell.setCellValue("Insurance");
				 else if(tipo.trim().equals("LDI")) cell.setCellValue("LDI_Voice_Infracel");
				 else if(tipo.trim().equals("SANCIONEQUIPO")) cell.setCellValue("Equipment_Commitment");
 				 else if(tipo.trim().equals("SANCIONPLAN")) cell.setCellValue("Plan_Commitment");
				 else if(tipo.trim().equals("Equipos")) cell.setCellValue("");
				 else if(tipo.trim().equals("IdLlamada"))cell.setCellValue("");
				 else if(tipo.trim().equals("Videollamada")) cell.setCellValue("Video_Call");
				 else if(tipo.trim().equals("RollOver")) cell.setCellValue("Pasaminutos");
				 else if(tipo.trim().equals("BienRepo")) cell.setCellValue("Welcome_Promotion");
				 else if(tipo.trim().equals("BuzonVoz")) cell.setCellValue("Voice_Mail");
				 else if(tipo.trim().equals("NokiaMessaging")) cell.setCellValue("Nokia_Messaging"); 	
				 else if(tipo.trim().contains("FnFSMSOnnet")) cell.setCellValue("SMS_Onnet_Wireless"); 
				 else if(tipo.trim().contains("FnFVozFijo")) cell.setCellValue("Onnet_Fix");
				 else if(tipo.trim().equals("WEBTV")) cell.setCellValue("WEB_TV_GROUP");
				 else if(tipo.trim().contains("Web Message")) cell.setCellValue("WEB_Message");
				 else if(tipo.trim().contains("CobroDirTel")) cell.setCellValue("Telephone_Directory_Component");
 				 else if(tipo.trim().contains("Short Mail")) cell.setCellValue("Short_Mail");
 				 else if(tipo.trim().contains("Reposicion")) cell.setCellValue("FULFILLMENT_RETURN");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("99999999"))cell.setCellValue("Voice_Onnet_Wireless");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("1")) cell.setCellValue( eleg +"_Onnet_Wireless_Voice_Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("3")) cell.setCellValue( eleg +"_Onnet_Wireless_Voice_Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("4")) cell.setCellValue( eleg +"_Onnet_Wireless_Voice_Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("6")) cell.setCellValue( eleg +"_Onnet_Wireless_Voice_Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("9")) cell.setCellValue( eleg +"_Onnet_Wireless_Voice_Recurring");
				 else if(eleg.equals("0")&&pa.getTIPO_PAQ().trim().equals("DetalleLlamada")) cell.setCellValue("Wireless_Main");
				 else cell.setCellValue(pa.getTIPO_PAQ());						 
 							 
 				 //......................... Sale Effective Date ......................................
 			/*	 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
                 Calendar calendar = Calendar.getInstance();
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                 Date date = sdf.parse("2011/01/01");
                 calendar.setTime(date);        
                 DataFormat fmt = wb.createDataFormat();
                 CellStyle style = wb.createCellStyle();
                 style.setAlignment(CellStyle.ALIGN_RIGHT);
                 style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss")); 
                 cell.setCellStyle(style); 				 
 				 cell.setCellValue(calendar);
 				//......................... SellableInd ......................................
 				 temp = temp +3;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("FALSE");	
 				//......................... AllowSuspendInd ......................................
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("FALSE");					 
 				//......................... Service Type ......................................
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 if(tipo.trim().equals("IdentificaLlamada")) cell.setCellValue("CLIP");
 				 if(tipo.trim().equals("Internet")) cell.setCellValue("WEB");
 				 if(tipo.trim().equals("Blackberry")) cell.setCellValue("BB");	
 				 if(tipo.trim().equals("SMS")) cell.setCellValue("SMST");
 				 if(tipo.trim().equals("Videollamada")) cell.setCellValue("VCAL");
 				 if(tipo.trim().equals("ChatPack")) cell.setCellValue("CPCK");
 				 if(tipo.trim().equals("LEGISMOVIL")) cell.setCellValue("LGMV");
 				 if(tipo.trim().equals("VozAVoz")) cell.setCellValue("V2VM");
 				 if(tipo.trim().equals("Seguros")) cell.setCellValue("INSR");
 				 if(tipo.trim().equals("RingBackTone")) cell.setCellValue("RBT");
 				 if(tipo.trim().equals("Cascada")) cell.setCellValue("CSCD");
 				 if(tipo.trim().equals("LDI")) cell.setCellValue("LDI");
 				// if(tipo.trim().equals("Equipos")) cell.setCellValue("EQIP");
 				 if(tipo.trim().equals("FnF")) cell.setCellValue("FOWV"); 				 
 				 if(tipo.trim().equals("Synchronica")) cell.setCellValue("SYNC");
 				 if(tipo.trim().equals("RollOver")) cell.setCellValue("PM");
 				 if(tipo.trim().equals("Bienvenida")) cell.setCellValue("WCBK");
 				 //......................... Installation Address Required ......................................
 				 temp = temp +3;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("TRUE"); 
 				//......................... Entry Index ......................................
 				 temp = temp +4;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue(1);
 				//......................... Purpose ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("DE");
 				
 				//......................... Description:English ......................................
				 temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 System.out.println("tipo: "+ tipo + " num eleg: "+ pa.getNUM_ELEG());
				 if(tipo.trim().equals("IdentificaLlamada"))cell.setCellValue("Caller ID enables the receiver to view the ID of the caller");
				 if(tipo.trim().equals("Internet"))cell.setCellValue("The internet component will enable access to internet outside of AMX APN");
				 if(tipo.trim().equals("Blackberry"))cell.setCellValue("The Blackberry component handles Blackberry Service");
				 if(tipo.trim().equals("SMS"))cell.setCellValue("The SMS component handles SMS Services");
				 if(tipo.trim().equals("Videollamada"))cell.setCellValue("The Video Call component enables Video Call to a phone that supports this capability");
				 if(tipo.trim().equals("ChatPack"))cell.setCellValue("The Chat Pack Windows component enables access to Hotmail email service and MSN Messenger instant messaging unlimited.");
				 if(tipo.trim().equals("Synchronica"))cell.setCellValue("The Synchronica service provides Mobile Gateway push email, synchronization, and instant messaging service as Messenger Comcel.");
				 //*------
				 if(tipo.trim().equals("LEGISMOVIL")) cell.setCellValue("Legismovil Service provides the subscriber content on the legal area");
 				 if(tipo.trim().equals("VozAVoz")) cell.setCellValue("The Voice to Voice Magazine component provides Magazine Service to Comcel subscribers");
 				 if(tipo.trim().equals("Seguros")) cell.setCellValue("The Insurance component enables Insurance Service");
 				 if(tipo.trim().equals("RingBackTone")) cell.setCellValue("The Ring Back Tone component enables download of Ring Back Tones");
 				 if(tipo.trim().equals("Cascada")) cell.setCellValue("The CascadasCascades Service enables configuration so that incoming calls will ring several destination numbers one after another");
 				 if(tipo.trim().equals("LDI")) cell.setCellValue("The LDI Voice Infracel component handles Long Distance International Voice Service via Infracel opertaor.");
 				// if(tipo.trim().equals("Equipos")) cell.setCellValue("Equipment component captures equipment details and additional parameters required for calculating equipment price");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("1")) cell.setCellValue("The 1 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component."); 				 
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("3")) cell.setCellValue("The 3 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("4")) cell.setCellValue("The 4 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("6")) cell.setCellValue("The 6 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("9")) cell.setCellValue("The 9 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("RollOver")) cell.setCellValue("enables customer to configure the allowance of the base plan to be rollover, starting the coming cycle");
 				 if(tipo.trim().equals("Bienvenida")) cell.setCellValue("Comcel provide welcome promotion for limited period in two scenarios: when new line is activated or equipment is replaced");				 
     			 //......................... Description:Spanish ......................................
				 temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 if(tipo.trim().equals("IdentificaLlamada"))cell.setCellValue("Caller ID enables the receiver to view the ID of the caller");
				 if(tipo.trim().equals("Internet"))cell.setCellValue("The internet component will enable access to internet outside of AMX APN");
				 if(tipo.trim().equals("Blackberry"))cell.setCellValue("The Blackberry component handles Blackberry Service");
				 if(tipo.trim().equals("SMS"))cell.setCellValue("The SMS component handles SMS Services");
				 if(tipo.trim().equals("Videollamada"))cell.setCellValue("The Video Call component enables Video Call to a phone that supports this capability");
				 if(tipo.trim().equals("ChatPack"))cell.setCellValue("The Chat Pack Windows component enables access to Hotmail email service and MSN Messenger instant messaging unlimited.");
				 if(tipo.trim().equals("Synchronica"))cell.setCellValue("The Synchronica service provides Mobile Gateway push email, synchronization, and instant messaging service as Messenger Comcel.");
				 if(tipo.trim().equals("LEGISMOVIL")) cell.setCellValue("Legismovil Service provides the subscriber content on the legal area");
 				 if(tipo.trim().equals("VozAVoz")) cell.setCellValue("The Voice to Voice Magazine component provides Magazine Service to Comcel subscribers");
 				 if(tipo.trim().equals("Seguros")) cell.setCellValue("The Insurance component enables Insurance Service");
 				 if(tipo.trim().equals("RingBackTone")) cell.setCellValue("The Ring Back Tone component enables download of Ring Back Tones");
 				 if(tipo.trim().equals("Cascada")) cell.setCellValue("The CascadasCascades Service enables configuration so that incoming calls will ring several destination numbers one after another");
 				 if(tipo.trim().equals("LDI")) cell.setCellValue("The LDI Voice Infracel component handles Long Distance International Voice Service via Infracel opertaor.");
 				 //if(tipo.trim().equals("Equipos")) cell.setCellValue("Equipment component captures equipment details and additional parameters required for calculating equipment price");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("1")) cell.setCellValue("The 1 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component."); 				 
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("3")) cell.setCellValue("The 3 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("4")) cell.setCellValue("The 4 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("6")) cell.setCellValue("The 6 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("FnF")&& pa.getNUM_ELEG().equals("9")) cell.setCellValue("The 9 Onnet Wireless component handles Onnet Wireless F&F numbers for the Additional F&F service. This is a monthly recurring charging F&F component.");
 				 if(tipo.trim().equals("RollOver")) cell.setCellValue("enables customer to configure the allowance of the base plan to be rollover, starting the coming cycle");
 				 if(tipo.trim().equals("Bienvenida")) cell.setCellValue("Comcel provide welcome promotion for limited period in two scenarios: when new line is activated or equipment is replaced");				 
				 */
				 //......................... Entry Index ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue(1);
 				//......................... Billing Offer Name - Inclusion ......................................
 				temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 cell.setCellValue("Optional not selected by default");	
				 System.out.println( " Pasa por Optional   entoncessssssss");
				//......................... Billing Offer Name - EnableForSelection ......................................
	 			temp++;
				 cell = row.getCell(temp); 
				 if (cell == null) cell = row.createCell(temp);
				 cell.setCellValue("TRUE");	
 				 
 				 //......................... Billing Offer Name - Template name ......................................
 				 //temp= temp+3;
				 temp= temp+2;				 
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("BillingOffer");
 				//......................... Billing Offer Name - name ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue(pa.getDESC_BO());
 				
 				 if(pa.getTIPO_PAQ().contains("FnF")){
 					 
 					//......................... Product Price Relation - Entry Index ......................................
 					 temp++;
 	 				 cell = row.getCell(temp); 
 	 				 if (cell == null) cell = row.createCell(temp);
 	 				 cell.setCellValue("1");
 	 				//......................... Product Price Relation - AttributeMapping:Ignore Domain Method ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("TRUE");
	 				//......................... Product Price Relation:Product Attribute - Template name ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("Attribute");
	 				//......................... Product Price Relation:Product Attribute - name ......................................
					 if(pa.getTIPO_PAQ().equals("FnFVozFijo")||(pa.getTIPO_PAQ().equals("FnFVozOnnet")&&pa.getNUM_ELEG().equals("99999999"))){
		 				 temp++;
		 				 cell = row.getCell(temp); 
		 				 if (cell == null) cell = row.createCell(temp);
		 				 cell.setCellValue("In Plan Friend & Family List");}
					 else {
						 temp++;
						 cell = row.getCell(temp); 
		 				 if (cell == null) cell = row.createCell(temp);
						 String valor = "F&F Number "+ pa.getNUM_ELEG();					 
						 cell.setCellValue(valor);
					      }
	 				//......................... Product Price Relation:Billing Attribute - Template name ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("Attribute");
	 				//......................... Product Price Relation:Billing Attribute - name ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("Friends numbers");				 
 				 
 				 } 
 				 
 				 /*
 				 //......................... Entry Index ......................................
 				 temp = temp +27;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue( 1);
 				 //......................... Constant ......................................
 				 temp = temp +2;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("FALSE");
 				 //......................... UseAsServiceID ......................................
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("FALSE");
 				 //......................... Mandatory ......................................
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("FALSE");
 				//.........................Attribute Name - Name:English......................................
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 					 
 				//.........................Domain  - Template  ......................................
 				 temp = temp +2;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 							 
 				//.........................Domain  - Name:English  ......................................
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 				 
 				//.........................Entry Index  ......................................
 				 temp= temp +30;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue(1);
 				//.........................DisplayMode:Name ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("Component Default");
 				//.........................DisplayMode:DefaultModeInd ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("TRUE");
 				//.........................DisplayMode:RedoMode ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("All");
 				//.........................DisplayMode:OrderActionType ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("All");
 				//.........................DisplayMode:AmendMode ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("All");
 				//.........................DisplayMode:ActivityName ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("All");
 				//.........................DisplayMode:OrderMode ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("All");
 				//.........................DisplayMode:ID ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("All");
 				//.........................DisplayMode:SalesChannel ......................................
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("All");		*/ 
 				 contador ++; 		 
 				 fila ++;
 			 }
 			 else {
 				 contador++;			 
 				 temp = 0;
 				  row = hoja.getRow(fila); 
 				  if(row==null)  row = hoja.createRow(fila);	
 				 //*******************************Entry Index****************************************
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("");
 				//------------------------------- Template name -------------------------------------------------
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp); 
 				if(tipo.trim().equals("IdentificaLlamada")||tipo.trim().contains("IdLlamada") ) cell.setCellValue("Caller ID Presentation");
			     else if(tipo.trim().equals("VideoLlamada")) cell.setCellValue("Video Call");
				 else if(tipo.trim().equals("ChatPack")) cell.setCellValue("Chat Pack Windows");
				 else if(tipo.trim().equals("RingBackTone")) cell.setCellValue("Ring Back Tone");
				 else if(tipo.trim().equals("Cascada")) cell.setCellValue("Cascadas Service");
				 else if(tipo.trim().equals("VozAVoz")) cell.setCellValue("Voice to Voice Magazine");
				 else if(tipo.trim().equals("RecordacionMIN")) cell.setCellValue("RECORDATION de MIN");
				 else if(tipo.trim().equals("LEGISMOVIL")) cell.setCellValue("Legismovil Service");
				 else if(tipo.trim().equals("Seguros")||tipo.trim().contains("sistencia")) cell.setCellValue("Insurance");
				 else if(tipo.trim().equals("LDI")) cell.setCellValue("LDI Voice Infracel");
				 else if(tipo.trim().contains("Equipo")) cell.setCellValue("Equipment");
 				 else if(tipo.trim().contains("SIMCard")) cell.setCellValue("SIM Card");
 				 else if(tipo.trim().contains("CobroDirTel")) cell.setCellValue("Telephone Directory");
				 else if(tipo.trim().equals("SANCIONEQUIPO")) cell.setCellValue("Equipment Commitment");
 				 else if(tipo.trim().equals("SANCIONPLAN")) cell.setCellValue("Plan Commitment");
				 else if(tipo.trim().equals("Videollamada")) cell.setCellValue("Video Call");
				 else if(tipo.trim().equals("RollOver")) cell.setCellValue("Pasaminutos");
				 else if(tipo.trim().equals("BienRepo")) cell.setCellValue("Welcome Promotion");
				 else if(tipo.trim().equals("BuzonVoz")) cell.setCellValue("Voice Mail");
				 else if(tipo.trim().equals("NokiaMessaging")) cell.setCellValue("Nokia Messaging"); 	
				 else if(tipo.trim().contains("FnFSMSOnnet")) cell.setCellValue("SMS Onnet Wireless"); 
				 else if(tipo.trim().contains("FnFVozFijo")) cell.setCellValue("Voice Onnet Fix");
				 else if(tipo.trim().equals("WEBTV")) cell.setCellValue("Web TV");
				 else if(tipo.trim().contains("Web Message")) cell.setCellValue("Web Message");
 				 else if(tipo.trim().contains("Short Mail")) cell.setCellValue("Short Mail");
 				 else if(tipo.trim().contains("Reposicion")) cell.setCellValue("Fulfillment & Return");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("99999999"))cell.setCellValue("Voice Onnet Wireless");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("1")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("3")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("4")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("6")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
				 else if(tipo.trim().contains("FnFVozOnnet") && eleg.equals("9")) cell.setCellValue( eleg +" Onnet Wireless Voice Recurring");
				 else if(eleg.equals("0")&&pa.getTIPO_PAQ().trim().equals("DetalleLlamada")) cell.setCellValue("Wireless GenPO");
				 else cell.setCellValue(pa.getTIPO_PAQ());						 
 				//------------------------------- Template index -------------------------------------------------
 				 //temp = temp+25;
 				temp = temp+5;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue(contador);
 				//------------------------------- Inclusion -------------------------------------------------
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("Optional not selected by default");
 				//------------------------------- EnableFor Selection -------------------------------------------------
 				 temp ++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("TRUE"); 				 
 				 //------------------------------- Template name -------------------------------------------------
 				 //temp = temp +3;
 				 temp = temp +2;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue("BillingOffer");
 				 //-------------------------------  name -------------------------------------------------
 				 temp++;
 				 cell = row.getCell(temp); 
 				 if (cell == null) cell = row.createCell(temp);
 				 cell.setCellValue(pa.getDESC_BO());  	 
 			     fila ++;
 			     
 			    if(pa.getTIPO_PAQ().contains("FnF")){
					 
 					//......................... Product Price Relation - Entry Index ......................................
 					 temp++;
 	 				 cell = row.getCell(temp); 
 	 				 if (cell == null) cell = row.createCell(temp);
 	 				 cell.setCellValue("1");
 	 				//......................... Product Price Relation - AttributeMapping:Ignore Domain Method ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("TRUE");
	 				//......................... Product Price Relation:Product Attribute - Template name ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("Attribute");
	 				//......................... Product Price Relation:Product Attribute - name ......................................
					 if(pa.getTIPO_PAQ().equals("FnFVozFijo")||(pa.getTIPO_PAQ().equals("FnFVozOnnet")&&pa.getNUM_ELEG().equals("0"))){
		 				 temp++;
		 				 cell = row.getCell(temp); 
		 				 if (cell == null) cell = row.createCell(temp);
		 				 cell.setCellValue("In Plan Friend & Family List");}
					 else {
						 temp++;
						 cell = row.getCell(temp); 
		 				 if (cell == null) cell = row.createCell(temp);
						 String valor = "F&F Number "+ pa.getNUM_ELEG();					 
						 cell.setCellValue(valor);
					      }
	 				//......................... Product Price Relation:Billing Attribute - Template name ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("Attribute");
	 				//......................... Product Price Relation:Billing Attribute - name ......................................
					 temp++;
	 				 cell = row.getCell(temp); 
	 				 if (cell == null) cell = row.createCell(temp);
	 				 cell.setCellValue("Friends numbers");				 
 				 
 				                 } 			                        
 			                  }	 
 			 			 }
 		 			}
 		    }	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	
 	public static HashMap<String, Integer> obtenerNumCols(String strSheet) throws Exception{
		String columnName="";
		InputStream inp = new FileInputStream("D://EntradasEPC/AMX EPC Wireless Product Offering.xlsx");   
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet(strSheet);

		Row row = sheet.getRow(0);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Cell cell : row) {
			if(cell!=null){
				if(strSheet.equals("General Info")){
					if(cell.getRichStringCellValue().getString().equals("Template=\"ProductOffering\";UniqueIdentifier=\"Element Name\";locale=\"EN\"")){
						//if(map.get("Template=\"ProductOffering\";UniqueIdentifier=\"Element Name\";locale=\"EN\"")==null){
						   map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("SaleEffectiveDate")){
						if(map.get("SaleEffectiveDate")==null){
						map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							
						}
					}else if(cell.getRichStringCellValue().getString().equals("Code")){
							if(cell.getColumnIndex()>4&&cell.getColumnIndex()<12){
								columnName="CodeToFillA";
								cell.setCellValue("CodeToFillA");
								map.put(columnName, cell.getColumnIndex());
							}
						 
					}else if(cell.getRichStringCellValue().getString().equals("Classification")){
						if(cell.getColumnIndex()>6&&cell.getColumnIndex()<12){
							columnName="ClassificationToFillA";
							cell.setCellValue("ClassificationToFillA");
							map.put(columnName, cell.getColumnIndex());
						}
						
					}else if(cell.getRichStringCellValue().getString().equals("LineOfBusiness")){
						if(map.get("LineOfBusiness")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());								
							}
					}else if(cell.getRichStringCellValue().getString().equals("SellableInd")){
						if(map.get("SellableInd")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("CoreElementPath=\"Element Name\";locale=\"es\"")){
						//if(map.get("CoreElementPath=\"Element Name\";locale=\"es\"")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
							
						}
					}else if(cell.getRichStringCellValue().getString().equals("SalesChannel;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>10&&cell.getColumnIndex()<16){
							columnName="SalesChannel;UniqueIdentifier=\"Element Name\"ToFillA";
							cell.setCellValue("SalesChannel;UniqueIdentifier=\"Element Name\"ToFillA");
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Description;EntryIndex=\"Dummy Entry Index\";Level=1")){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						
					}else if(cell.getRichStringCellValue().getString().equals("Purpose")){
						if(cell.getColumnIndex()>11&&cell.getColumnIndex()<18){
							columnName="PurposeToFillA";
							cell.setCellValue("PurposeToFillA");
							map.put(columnName, cell.getColumnIndex());
							
						}
					}else if(cell.getRichStringCellValue().getString().equalsIgnoreCase("Description;Locale=\"en\"")){
						//(if(map.get("Description;Locale=\"en\"")==null){
						    map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("Description;Locale=\"es\"")){
						//if(map.get("Description;Locale=\"es\"")==null){
						    map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("OfferingProductRelation;EntryIndex=\"Dummy Entry Index\";Level=1")){
						//if(map.get("OfferingProductRelation;EntryIndex=\"Dummy Entry Index\";Level=1")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("MinimumQuantity")){
						//if(map.get("MinimumQuantity")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("MaximumQuantity")){
						//if(map.get("MaximumQuantity")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("DefaultQuantity")){
						//if(map.get("DefaultQuantity")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("ProductRelationRole")){
						//if(map.get("ProductRelationRole")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("Product;UniqueIdentifier=\"Element Name\"")){
						//if(map.get("Product;UniqueIdentifier=\"Element Name\"")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("EligibilityRule;EntryIndex=\"Dummy Entry Index\";Level=1")){
						//if(map.get("EligibilityRule;EntryIndex=\"Dummy Entry Index\";Level=1")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("ActiveIndication")){
						//if(map.get("ActiveIndication")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("EligibilityRule;UniqueIdentifier=\"Element Name\"")){
						//if(map.get("EligibilityRule;UniqueIdentifier=\"Element Name\"")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("Properties;UniqueIdentifier=\"Element Name\";Level=1")){
						//if(map.get("Properties;UniqueIdentifier=\"Element Name\";Level=1")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-Video_Call_Onnet_Wireless")){
						//if(map.get("F&F Max Number of Friends-Video_Call_Onnet_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-SMS_Onnet_Wireless")){
						//if(map.get("F&F Max Number of Friends-SMS_Onnet_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-Onnet_Wireless")){
						//if(map.get("F&F Max Number of Friends-Onnet_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-MMS_Onnet_Wireless")){
						//if(map.get("F&F Max Number of Friends-MMS_Onnet_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-Video_Call_Other_Wireless")){
						//if(map.get("F&F Max Number of Friends-Video_Call_Other_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-SMS_Other_Wireless")){
						//if(map.get("F&F Max Number of Friends-SMS_Other_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-Other_Fix")){
						//if(map.get("F&F Max Number of Friends-Other_Fix")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-MMS_Other_Wireless")){
						//if(map.get("F&F Max Number of Friends-MMS_Other_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-Other_Wireless")){
						//if(map.get("F&F Max Number of Friends-Other_Wireless")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-Onnet_Fix")){
						//if(map.get("F&F Max Number of Friends-Onnet_Fix")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-F&F_International")){
						//if(map.get("F&F Max Number of Friends-F&F_International")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("F&F Max Number of Friends-SMS_F&F_International")){
						//if(map.get("F&F Max Number of Friends-SMS_F&F_International")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("Display Priority")){
						//if(map.get("Display Priority")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("ConfigurationType")){
						//if(map.get("ConfigurationType")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}					
					}else if(cell.getRichStringCellValue().getString().equals("MaxQuantity")){
						//if(map.get("MaxQuantity")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}	
					}else if(cell.getRichStringCellValue().getString().equals("Allow Equipment Installments")){
						//if(map.get("Allow Equipment Installments")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}	
					}else if(cell.getRichStringCellValue().getString().equals("Plan Type")){
						//if(map.get("Plan Type")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("GAMA_ALL")){
						//if(map.get("GAMA_ALL")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("GAMA_COSTA")){
						//if(map.get("GAMA_COSTA")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("GAMA_OCCIDENTE")){
						//if(map.get("GAMA_OCCIDENTE")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("GAMA_ORIENTE")){
						//if(map.get("GAMA_ORIENTE")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}		
					}else if(cell.getRichStringCellValue().getString().equals("Required Evident COSTA")){
						//	if(map.get("Required Evident COSTA")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//	}	
					}else if(cell.getRichStringCellValue().getString().equals("Required Evident OCCIDENTE")){
						//if(map.get("Required Evident OCCIDENTE")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}	
					}else if(cell.getRichStringCellValue().getString().equals("Required Evident ORIENTE")){
						//if(map.get("Required Evident ORIENTE")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}	
					}else if(cell.getRichStringCellValue().getString().equals("Plan Sub Type")){
						//if(map.get("Plan Sub Type")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
							//}
					}else if(cell.getRichStringCellValue().getString().equals("Equipment SKU")){
						//if(map.get("Equipment SKU")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("SIM SKU")){
						//if(map.get("SIM SKU")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("Equipment Classification")){
						//if(map.get("Equipment Classification")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("Is TacticalSIM")){
						//if(map.get("Is TacticalSIM")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("Change MSISDN Num Month before Commitment End date")){
						//if(map.get("Change MSISDN Num Month before Commitment End date")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1")){
						//if(map.get("ProductAttributeRelation;EntryIndex=\"Dummy Entry Index\";Level=1")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("AssignableAttribute;UniqueIdentifier=\"Element Name\"")){/**PENDIENTE*/
						//if(map.get("AssignableAttribute;UniqueIdentifier=\"Element Name\"")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("ConstantValues")){
						if(map.get("ConstantValues")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Domain;Template=\"name\"")){
						//if(map.get("Domain;Template=\"name\"")==null){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						//}
					}else if(cell.getRichStringCellValue().getString().equals("Domain;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>929&&cell.getColumnIndex()<961){//NUEVO----
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}
					
					/*else if(cell.getRichStringCellValue().getString().equals("Boundary_date_of_Special_suspension_rate")){
						if(cell.getColumnIndex()>860&&cell.getColumnIndex()<895){//NUEVO---
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Immediate_payment_required")){
						if(cell.getColumnIndex()>892&&cell.getColumnIndex()<924){//NUEVO--
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Tax Service Type")){
						if(cell.getColumnIndex()>70&&cell.getColumnIndex()<92){//NUEVO----
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Properties;UniqueIdentifier=\"Element Name\";Level=1")){
						if(cell.getColumnIndex()>70&&cell.getColumnIndex()<92){//NUEVO---
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rate")){
						if(cell.getColumnIndex()>146&&cell.getColumnIndex()<166){
							columnName="RateA";//Usage Charge
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>531&&cell.getColumnIndex()<555){
							columnName="RateB";//One Time Charge
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>932&&cell.getColumnIndex()<952){//NUEVO---
							columnName="RateC";//Recurring Charge
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Charge_code")){
						if(cell.getColumnIndex()>890&&cell.getColumnIndex()<910){//nuevo---
							columnName="Charge_codeA";//Recurring Charge
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>698&&cell.getColumnIndex()<718){//
							columnName="Charge_codeB";//One Time Charge
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rate_table")){
						 if(cell.getColumnIndex()>698&&cell.getColumnIndex()<718){//
							columnName="Rate_table";//One Time Charge
							map.put(columnName, cell.getColumnIndex());
						}
					}//Termino Cambio para One time Charge 15/01/2013 - Rate_table---			*/
					else{
						map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
					}
				}		
		
		return map;
	}	
 	
	

 	
 	
 }


