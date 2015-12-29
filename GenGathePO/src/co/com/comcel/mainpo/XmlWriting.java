package co.com.comcel.mainpo;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;

import co.com.comcel.modelpo.DataModel;
import co.com.comcel.modelpo.IDataModel;
import co.com.comcel.vopo.Componetes;
import co.com.comcel.vopo.POCodNum;
import co.com.comcel.vopo.productOffering;

import javax.swing.JOptionPane;


public class XmlWriting {
	private static final String XML_ENCODING = "UTF-8";
	private static List<String> noinclu;
	private static List<String> paqNoInclu;
	
	
	public XmlWriting(String genera) throws Exception{
		generarArchivo();
		LlamarExeInsercion(genera);
     }
	
	public void LlamarExeInsercion(String genera){
		/** Iniciamos  el llamado a el ejecutable de VB para insertar la hoja en el 
		 * formato de excel de AMDOCS Product Offering
		 * 
		 */				
		Runtime aplicacion = Runtime.getRuntime(); 
	    if(genera.equals("1")){   
		    try{                
		         aplicacion.exec("D://EntradasEPC/EjecutablesInsertar/Insertar.exe"); 	
			   }catch(Exception e){e.printStackTrace();
			          System.err.print(e);
			   }
		    }
	    else if(genera.equals("2")){	    	
	    	 try{                
		         aplicacion.exec("D://EntradasEPC/EjecutablesInsertar/Insertarporxfase.exe"); 	
			   }catch(Exception e){e.printStackTrace();
			          System.err.print(e);
			   }	    	
	    }else  if(genera.equals("3")) {
	    	try{                
		         aplicacion.exec("D://EntradasEPC/EjecutablesInsertar/InsertarPostpago.exe"); 	
			   }catch(Exception e){e.printStackTrace();
			          System.err.print(e);
			   }    	
	    }
	    	
		System.out.println(" Archivo generado e insertado en archivo formato.");
		System.out.println("**FINALIZADO EN **" + new Date());	
	}
   
	public static void generarArchivo() throws Exception {     
		// Step 1. Create a template file. Setup sheets and workbook-level objects such as
		// cell styles, number formats, etc.
		System.out.println("**INICIADO**" + new Date());	
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Configuration");

		Map<String, XSSFCellStyle> styles = createStyles(wb);
		//name of the zip entry holding sheet data, e.g. /xl/worksheets/sheet1.xml
		String sheetRef = sheet.getPackagePart().getPartName().getName();

		//save the template
		FileOutputStream os = new FileOutputStream("D://template.xlsx");
		wb.write(os);
		os.close();        
		//Step 2. Generate XML file.
		//File tmp = File.createTempFile("sheet", ".xml");
		File tmp=new File("D://prueba1.xml");
		if(!tmp.exists()){
			tmp.createNewFile();
			System.out.println("New file \"xmlsheet.xml\" has been created to the current directory");
		}

		Writer fw = new OutputStreamWriter(new FileOutputStream(tmp), XML_ENCODING);
		generate(fw, styles);
		fw.close();
		//Step 3. Substitute the template entry with the generated data D:\\AMX EPC Wireless Product Offering Version 2012-02-21.xlsx
		FileOutputStream out = new FileOutputStream("D://SalidasEPC/AMX_EPC_Wireless_Product_Offering_Formato.xlsx");
		substitute(new File("D://template.xlsx"), tmp, sheetRef.substring(1), out);
		
		out.close();	
		
	}
	/**
	 * Create a library of cell styles.
	 */
	private static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){
		Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();
		XSSFDataFormat fmt = wb.createDataFormat();

		XSSFCellStyle style1 = wb.createCellStyle();
		style1.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style1.setDataFormat(fmt.getFormat("0.0%"));
		styles.put("percent", style1);

		XSSFCellStyle style2 = wb.createCellStyle();
		style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style2.setDataFormat(fmt.getFormat("0.0X"));
		styles.put("coeff", style2);

		XSSFCellStyle style3 = wb.createCellStyle();
		style3.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style3.setDataFormat(fmt.getFormat("$#,##0.00"));
		styles.put("currency", style3);

		XSSFCellStyle style4 = wb.createCellStyle();
		style4.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
		style4.setDataFormat(fmt.getFormat("mmm dd"));
		styles.put("date", style4);

		///------------------------------------------
		XSSFCellStyle style5 = wb.createCellStyle();
		XSSFFont headerFont = wb.createFont();
		headerFont.setBold(true);
		style5.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style5.setFont(headerFont);
		styles.put("header", style5);

		// Empezamos con los header de las PO....
		XSSFCellStyle style6 = wb.createCellStyle();
		XSSFFont cabFont = wb.createFont();
		cabFont.setBold(true);
		cabFont.setColor(IndexedColors.DARK_BLUE.getIndex());
		cabFont.setFontName("Calibri");
		cabFont.setFontHeightInPoints((short)11);
		style6.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		style6.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style6.setFont(cabFont);
		style6.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style6.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style6.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style6.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style6.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styles.put("cabecera", style6);


		XSSFCellStyle style7 = wb.createCellStyle();
		style7.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style7.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style7.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style7.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style7.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 204, 153)));
		style7.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style7.setFont(cabFont);
		style7.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style7.setVerticalAlignment(VerticalAlignment.CENTER);		
		styles.put("cab1", style7);

		XSSFCellStyle style8 = wb.createCellStyle();
		style8.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204)));
		style8.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style8.setFont(cabFont);
		style8.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style8.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style8.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style8.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style8.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style8.setVerticalAlignment(VerticalAlignment.CENTER);
		styles.put("cab2", style8);
		
		XSSFCellStyle style10 = wb.createCellStyle();
		style10.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style10.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style10.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style10.setBorderBottom(XSSFCellStyle.BORDER_THIN);

		style10.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 204, 0)));
		style10.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style10.setFont(cabFont);
		style10.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		styles.put("cab3", style10);
		
		XSSFCellStyle style11 = wb.createCellStyle();
		style11.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 153, 0)));
		style11.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style11.setFont(cabFont);
		style11.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		styles.put("cab4", style11);
		
		XSSFCellStyle style12 = wb.createCellStyle();
		style12.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 153, 0)));
		style12.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style12.setFont(cabFont);
		style12.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		style12.setBorderRight(XSSFCellStyle.BORDER_THIN);
		styles.put("cab5", style12);
		
		XSSFCellStyle style9 = wb.createCellStyle();
		XSSFFont cabFont1 = wb.createFont();
		//cabFont1.setBold(true);
		//cabFont1.setColor(IndexedColors.BLACK.getIndex());
		cabFont1.setFontName("Calibri");
		cabFont1.setFontHeightInPoints((short)10);
		style9.setFont(cabFont1);
		style9.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style9.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style9.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style9.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style9.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styles.put("styCel", style9);

		return styles;
	}
	private static int escribirIncComp(SpreadsheetWriter sw,  Map<String, XSSFCellStyle> styles, int indRow, int indice, String segmento, Boolean postpago ) throws Exception{
		IDataModel model = new DataModel();			
		Collection <productOffering> PO = model.getProductOffering();
		List <Componetes> comp =  model.getCompTotal();	
		if(postpago){	   
			Boolean flagBloq = false;
			Boolean flagB = false; 
			Boolean flagBloqA = false;
		    Boolean flagBloqPadre = false; 
		    Boolean f1 = false;
		    Boolean f2 = false;
		    Boolean f3 = false;
			String sncode = ""; 
			int temp = indice;
			for(Componetes com : comp){	
				if(com.getSEGMENTO().trim().equals(segmento)){
				indRow++;
				sw.insertRow(indRow);
				for(int i= 0; i< indice;i++){
					if (i == 0) {
						if(com.getDESCRIPCION().trim().equals("**TEST ADD BO") ||com.getDESCRIPCION().trim().equals("**TEST PP BO")||com.getDESCRIPCION().trim().equals("Technical PP")){ sw.createCell(i, "Abstract Price", styles.get("styCel").getIndex());
						System.out.println("Descripcion: Test" +com.getDESCRIPCION().trim());
						}
						else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")){
							sw.createCell(i, "Activity Charge", styles.get("styCel").getIndex()); 
							System.out.println("Descripcion Down: " +com.getDESCRIPCION().trim());						
						}
						else if(com.getDESCRIPCION().trim().equals("Wireless")){
							sw.createCell(i, "Product Spec", styles.get("styCel").getIndex()); 	
							System.out.println("Descripcion Product: " +com.getDESCRIPCION().trim());
						      }							
						     else {
									sw.createCell(i, "Component", styles.get("styCel").getIndex()); 	
									System.out.println("Descripcion Comp: " +com.getDESCRIPCION().trim());
						}
					} 			        	     
					if (i == 1) sw.createCell(i, com.getDESCRIPCION(), styles.get("styCel").getIndex());	
					if (i == 2) {if(com.getPRODUCT_SPEC()==null)sw.createCell(i, "", styles.get("styCel").getIndex());	
								else sw.createCell(i, com.getPRODUCT_SPEC(), styles.get("styCel").getIndex());	
					}
					if (i == 3) {if(com.getMAIN_COMP()==null)sw.createCell(i, "", styles.get("styCel").getIndex());
								else sw.createCell(i, com.getMAIN_COMP(), styles.get("styCel").getIndex());	
								}
					if (i == 4){if(com.getCOMPONENT1()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
								else sw.createCell(i, com.getCOMPONENT1(), styles.get("styCel").getIndex());	
								}
					if (i == 5){if(com.getCOMPONENT2()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
								else sw.createCell(i, com.getCOMPONENT2(), styles.get("styCel").getIndex());
								}
					if (i == 6){if(com.getCOMPONENT3()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
					            else sw.createCell(i, com.getCOMPONENT3(), styles.get("styCel").getIndex());	
								}
					if (i == 7) sw.createCell(i, "", styles.get("styCel").getIndex());
				}
		///-------         Agregar para definir si los componentes padres tienen bloqueo  
	    ///-------                         Cambios agregados 27/03/2012
				List<POCodNum> BloqPad1 = null;	
				List<POCodNum> BloqPad2 = null;	
				List<POCodNum> BloqPad3 = null;	
				List<POCodNum> BloqHijo = null;
				if(!com.getBLOQUEO().trim().equals("0"))
				         BloqHijo = model.defExisCompPA(com.getBLOQUEO());
								 
				if(com.getPadres_comp()!=null){
						  List<POCodNum> padres = com.getPadres_comp();
				
				 int sncode1 = -1;
				 int sncode2 = -1;
				 int sncode3 = -1;
				 
						 for(int i = 0; i<padres.size();i++){
							 if(com.getPadres_comp().get(i).getSncode()!=0){
							     if(com.getPadres_comp().get(i).getTipo().equals("1") ){
									   if(sncode1 != com.getPadres_comp().get(i).getSncode()) 
									   	BloqPad1 = model.defExisCompPA(String.valueOf(com.getPadres_comp().get(i).getSncode()));
									   
									   	sncode1= com.getPadres_comp().get(i).getSncode();							   
								   }
								  if(com.getPadres_comp().get(i).getTipo().equals("2")){
								        if(sncode2 != com.getPadres_comp().get(i).getSncode()) 
								        	BloqPad2 = model.defExisCompPA(String.valueOf(com.getPadres_comp().get(i).getSncode()));	
										
								        sncode2= com.getPadres_comp().get(i).getSncode();
								    }
								  if(com.getPadres_comp().get(i).getTipo().equals("3")) { 
										if(sncode3 != com.getPadres_comp().get(i).getSncode()) 
											BloqPad3 = model.defExisCompPA(String.valueOf(com.getPadres_comp().get(i).getSncode()));	
										
										sncode3 = com.getPadres_comp().get(i).getSncode();
									}							
								  }												
							}										
								
				}						
				for( productOffering po : PO){
				
					if(com.getDESCRIPCION().trim().equals("**TEST PP BO"))
					{
						sw.createCell(temp,"Excluded(mandatory)", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
							
					}else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")
							||com.getDESCRIPCION().trim().equals("Change MSISDN")||com.getDESCRIPCION().trim().equals("Change Offer")
							||com.getDESCRIPCION().trim().equals("Replace Equipment")||com.getDESCRIPCION().trim().equals("Recall Service")){
							
							sw.createCell(temp,"not selected by default", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							}
				/**   ------------   Nuevos requerimientos   -------------------      **/	     
					else if(com.getDESCRIPCION().trim().equals("Base Plan")||com.getDESCRIPCION().trim().equals("Wireless") ||com.getDESCRIPCION().trim().equals("Wireless Main")||com.getDESCRIPCION().trim().equals("Equipment &amp; SIM") ){
						sw.createCell(temp,"", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, 1, styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, 1, styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, 1, styles.get("styCel").getIndex());
						temp++;					
					          }
					else if(com.getDESCRIPCION().trim().equals("Web TV Package")){
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 999, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;	
								//}				
					          }	
					else if(com.getDESCRIPCION().trim().equals("Equipment Commitment")||com.getDESCRIPCION().trim().equals("Fulfillment &amp; Return")||com.getDESCRIPCION().trim().equals("Welcome Promotion")){						
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 1, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 1, styles.get("styCel").getIndex());
								temp++;	//	}					          
					}						
					else if(com.getDESCRIPCION().trim().equals("Technical PP"))
					{
						sw.createCell(temp,"mandatory", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
							
					}
					else if(com.getDESCRIPCION().trim().equals("Equipment")||com.getDESCRIPCION().trim().equals("SIM Card") ||com.getDESCRIPCION().trim().equals("Plan Commitment")||com.getDESCRIPCION().trim().equals("Never Lost Call")){
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;					
						
					}
					else if(com.getDESCRIPCION().trim().equals("Family Group")){
						if(po.getTMCODE().equals("0")){							
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;							
						}else if(po.getTIPO_PLAN_FAM()!=null){
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;						
						}else{
						
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;		}
					 }				
					
			    /**  ---------------------------------------- Fin Nuevos requerimientos -----------------------------------------------**/	     	
				else{
					//--------------------------------------------------------------------------------------------------------	
					 if(po.getTMCODE().equals("0") && !com.getDESCRIPCION().equals("Wireless")) {
						    sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;					
					 }
					 else {
					//---------------------------------------------------------------------------------------------------------
				  						
					if(BloqPad1 !=null){
						
					for(POCodNum bloq: BloqPad1 ){
				//		System.out.println(bloq.getTmcode()+" bloqueo = "+ bloq.getSncode());
						 if (bloq.getTmcode() == Integer.parseInt(po.getTMCODE())){
							 f1 = true;
							 break;	
						      }					
					       }
						}
					if(BloqPad2!=null){
						for(POCodNum bloq1: BloqPad2 ){
					//		System.out.println(bloq1.getTmcode()+" bloqueo = "+ bloq1.getSncode());
							 if (bloq1.getTmcode() == Integer.parseInt(po.getTMCODE())){
								 f2 = true;
								 break;	
							      }					
						       }
							}
					if(BloqPad3!=null){
						for(POCodNum bloq2: BloqPad3 ){
							 if (bloq2.getTmcode() == Integer.parseInt(po.getTMCODE())){
								// System.out.println("pasa 3" );
								 f3 = true;
								 break;	
							      }					
						       }
							}
					if(f1 == true || f2 == true || f3==true){
						flagBloqPadre =  true;
					}
					if(BloqHijo !=null){	   
					for(POCodNum bloqH : BloqHijo){
						if(po.getTMCODE().trim().equals(String.valueOf(bloqH.getTmcode()))&& bloqH.getTipo().trim().equals("A")){
							flagBloqA = true;
						      }
					       }
					    }
					if(com.getSNCODE().trim().equals("1"))
							     { sncode = com.getOBSERVACION().trim();}
				    else {sncode = com.getSNCODE().trim();      }
					System.out.println("bloqueoPadre: " +flagBloqPadre + " bloqHijoA: " + flagBloqA );  
					
					   if(flagBloqPadre == true) {
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;									
							   }						  
					    else{ 					    	
					    	
						    if(com.getBLOQUEO().trim().equals("0")){	        		  	   	
									flagB = model.defExisCompPO(po.getTMCODE().trim(), sncode);
									   if(flagB == true){
											
											sw.createCell(temp,"", styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											System.out.println("Pasa sin bloqueo...flagB = true "+ flagB +" sncode: "+ sncode  );
										       }	
										else   {																
											sw.createCell(temp,"", styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;  		  	   		
										  
										        } 										
								//--------------------------Termina Codigo que se agrego y se comenta el siguiente -----------------------------------		
								     		
								} 												
							else{
									flagB = model.defExisCompPO(po.getTMCODE().trim(),  sncode);
									sncode = com.getBLOQUEO().trim();
									flagBloq = model.defExisCompPO(po.getTMCODE().trim(),  sncode); 
								   	if(flagBloq == true && flagB == false) {
										sw.createCell(temp, "", styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;										
									}
									if(flagBloq == false){
										if(flagB == true) {
											sw.createCell(temp, "", styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											 }
										else {												
												sw.createCell(temp, "", styles.get("styCel").getIndex());
												temp++;
												sw.createCell(temp, 0, styles.get("styCel").getIndex());
												temp++;
												sw.createCell(temp, 1, styles.get("styCel").getIndex());
												temp++;
												sw.createCell(temp, 0, styles.get("styCel").getIndex());
												temp++;}												
									}
									if(flagBloq == true && flagB == true) {
										System.out.println("Pasa por el valor inusual,,,, no debe pasar por aca");
										sw.createCell(temp, "", styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 1, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;																		}
									 }  
						  		}// ******fin del else ************						         
				      }
				   }// Fin del else para llenar cardinalidad de cada una delos componentes con PO 
					flagBloqPadre = false;
					flagBloqA = false; f1 = false; f2=false; f3=false;
				
				}
				sw.endRow();
				temp = indice;
				
				}		
		     }    	
		}
		else{		
			int temp = indice;
			for(Componetes com : comp){	
				System.out.println("segmento:  "+com.getSEGMENTO() + " solicitado: "+ segmento);
				if(com.getSEGMENTO().trim().equals(segmento)){					
					indRow++;
					sw.insertRow(indRow);
					for(int i= 0; i< indice;i++){
						if (i == 0) {

							if(com.getDESCRIPCION().trim().equals("**TEST ADD BO") ||com.getDESCRIPCION().trim().equals("**TEST PP BO")||com.getDESCRIPCION().trim().equals("Technical PP")){ sw.createCell(i, "Abstract Price", styles.get("styCel").getIndex());
							System.out.println("Descripcion: Test" +com.getDESCRIPCION().trim());
							}
							else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")||com.getDESCRIPCION().trim().equals("Change MSISDN")||com.getDESCRIPCION().trim().equals("Change Offer")
									||com.getDESCRIPCION().trim().equals("Replace Equipment")||com.getDESCRIPCION().trim().equals("Recall Service")){
								sw.createCell(i, "Activity Charge", styles.get("styCel").getIndex()); 
								System.out.println("Descripcion Down: " +com.getDESCRIPCION().trim());						
							}
							else if(com.getDESCRIPCION().trim().equals("Wireless")){
								sw.createCell(i, "Product Spec", styles.get("styCel").getIndex()); 	
								System.out.println("Descripcion Product: " +com.getDESCRIPCION().trim());
							      }							
							     else {
										sw.createCell(i, "Component", styles.get("styCel").getIndex()); 	
										System.out.println("Descripcion Comp: " +com.getDESCRIPCION().trim());
							}
						} 			        	     
						if (i == 1) sw.createCell(i, com.getDESCRIPCION(), styles.get("styCel").getIndex());	
						if (i == 2) {if(com.getPRODUCT_SPEC()==null)sw.createCell(i, "", styles.get("styCel").getIndex());	
									else sw.createCell(i, com.getPRODUCT_SPEC(), styles.get("styCel").getIndex());	
						}
						if (i == 3) {if(com.getMAIN_COMP()==null)sw.createCell(i, "", styles.get("styCel").getIndex());
									else sw.createCell(i, com.getMAIN_COMP(), styles.get("styCel").getIndex());	
									}
						if (i == 4){if(com.getCOMPONENT1()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
									else sw.createCell(i, com.getCOMPONENT1(), styles.get("styCel").getIndex());	
									}
						if (i == 5){if(com.getCOMPONENT2()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
									else sw.createCell(i, com.getCOMPONENT2(), styles.get("styCel").getIndex());
									}
						if (i == 6){if(com.getCOMPONENT3()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
						            else sw.createCell(i, com.getCOMPONENT3(), styles.get("styCel").getIndex());	
									}
						if (i == 7) sw.createCell(i, "", styles.get("styCel").getIndex());		
			
					}					
					for( productOffering po : PO){	
						
						if(com.getDESCRIPCION().trim().equals("Technical PP"))
						{
								sw.createCell(temp,"mandatory", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								
						}else if((po.getDESC_BO().contains("KIT")||po.getDESC_BO().contains("Kit")||po.getDESC_BO().contains("Especial Nacional")) &&com.getDESCRIPCION().trim().equals("Equipment")){
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	
							  ///com.getDESCRIPCION().trim().equals("Equipment") ||
						}else if(po.getDESC_BO().contains("Tactico")&&(com.getDESCRIPCION().trim().equals("Fulfillment &amp; Return") 
								||com.getDESCRIPCION().trim().equals("Temp Equipment")||com.getDESCRIPCION().trim().equals("Warranty"))){
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
						
						}else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")
								||(com.getDESCRIPCION().trim().equals("Replace Equipment")&&po.getDESC_BO().trim().contains("Tactico"))){
								sw.createCell(temp,"excluded", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
						}else if(com.getDESCRIPCION().trim().equals("Change MSISDN")||com.getDESCRIPCION().trim().equals("Change Offer")
								||(com.getDESCRIPCION().trim().equals("Replace Equipment")&&!po.getDESC_BO().trim().contains("Tactico"))||com.getDESCRIPCION().trim().equals("Recall Service")){
								sw.createCell(temp,"not selected by default", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
						}else if(!po.getELEGIDOS_BFNF().equals("0")&&!po.getELEGIDOS_FNF().equals("0")){											 
							
						if(com.getDESCRIPCION().equals("BFNF In Plan")||com.getDESCRIPCION().equals("Best Friends and Family")||com.getDESCRIPCION().equals("BFNF Voice"))
								{sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						else if (com.getDESCRIPCION().equals("BFNF Voice All Destination")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;								
						}else if(com.getDESCRIPCION().contains("BFNF")&&com.getDESCRIPCION().contains("Additional")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}
					  						
						else if(com.getDESCRIPCION().equals("FNF Voice")||com.getDESCRIPCION().equals("Friends &amp; Family")||com.getDESCRIPCION().equals("FNF Voice All Destination")||com.getDESCRIPCION().equals("In Plan")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
					/*	else if (com.getDESCRIPCION().equals("FNF Voice All Destination")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}*/
						else if(com.getDESCRIPCION().equals("FNF Voice All Destination Additional")||com.getDESCRIPCION().equals("FNF Voice Additional")||com.getDESCRIPCION().equals("Additional")){
					    		sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}					 						
						else  { 
					    	   sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
							  temp++;}			    
					  
					}else if(po.getELEGIDOS_BFNF().equals("0")&&!po.getELEGIDOS_FNF().equals("0")){
												
						if(com.getDESCRIPCION().equals("FNF Voice")||com.getDESCRIPCION().equals("Friends &amp; Family")||com.getDESCRIPCION().equals("FNF Voice All Destination")||com.getDESCRIPCION().equals("In Plan")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						/*else if (com.getDESCRIPCION().equals("FNF Voice All Destination")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}*/
						else if(com.getDESCRIPCION().equals("FNF Voice All Destination Additional")||com.getDESCRIPCION().equals("FNF Voice Additional")||com.getDESCRIPCION().equals("Additional")){
					    		sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}
						else{ 
						    	sw.createCell(temp, "", styles.get("styCel").getIndex());
							    temp++;
								sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
								temp++;	}				
				
					}else if(!po.getELEGIDOS_BFNF().equals("0")&&po.getELEGIDOS_FNF().equals("0")){
						
						if(com.getDESCRIPCION().equals("BFNF In Plan")||com.getDESCRIPCION().equals("Best Friends and Family")||com.getDESCRIPCION().equals("BFNF Voice"))
								{sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						else if (com.getDESCRIPCION().equals("BFNF Voice All Destination")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						else if(com.getDESCRIPCION().contains("BFNF")&&com.getDESCRIPCION().contains("Additional")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}
						else { 
						    	sw.createCell(temp, "", styles.get("styCel").getIndex());
							    temp++;
								sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
								temp++;	}				
										
					}else{				
							//System.out.println("Pasa por valores de establecidos  ya como cardinalidad fija");
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
							temp++;	
							}					
					}
					sw.endRow();
					temp = indice;			
			    }				
			}			
		}
		System.out.println(" termina Ciclo y Retorna: "+ indRow);
		return indRow;
	}
	private static void generate(Writer out, Map<String, XSSFCellStyle> styles) throws Exception {
		IDataModel model = new DataModel();	
		Collection <productOffering> PO = model.getProductOffering();
		Boolean post = true;
		List <Componetes> comp =  model.getComponentes();	
		SpreadsheetWriter sw = new SpreadsheetWriter(out);
		sw.beginWorkSheet();
		sw.InicColsForm();
		// Definir tamaño para la primeras 8 columnnas.......
		sw.colsForm(0, 16);
		sw.colsForm(1, 20);
		sw.colsForm(2, 50);
		sw.colsForm(3, 20);
		sw.colsForm(4, 20);
		sw.colsForm(5, 20);
		sw.colsForm(6, 20);
		sw.colsForm(7, 20);
		sw.EndColsForm();
		sw.beginSheet();		
		int col = 8;
		/**     ..insert header row: Inicio primera fila 1........      **/
		sw.insertRow(0);   
		int indice = col;
		int indRow = 3;
		/**
		 * Cambio para insertar nuevos encabezados  nuevas filas a archivo de PO original 
		 */
		int styleIndex0 = styles.get("cab3").getIndex();
		sw.createCell(0, "Entity", styleIndex0);
		sw.createCell(1, "", styleIndex0);
		sw.createCell(2, "", styleIndex0);
		sw.createCell(3, "", styleIndex0);
		sw.createCell(4, "", styleIndex0);
		sw.createCell(5, "", styleIndex0);
		sw.createCell(6, "", styleIndex0);
		sw.createCell(7, "", styleIndex0);
		List<Integer> indices0 = new ArrayList<Integer>();
		
		int prim = 0;
		int cont = 0;
		noinclu = model.existplanMaestra();
		paqNoInclu = model.existpaqMaestra();	
		GenArchPOnoInclu();
		
		  for( productOffering po : PO){
			 if (prim == 0) {	sw.createCell(indice, "Inclusion", styles.get("cab4").getIndex()); 
		                  	prim++;
		   				   }
					for(int j =0 ; j<4;j++){ 
						
							if(j==3){sw.createCell(indice, "", styles.get("cab5").getIndex()); 
							     indice ++;  }
							else {sw.createCell(indice, "", styles.get("cab4").getIndex()); 
							     indice ++;}
										 }	
								}			 
		sw.endRow(); 
	
		/**      ...................... Inicio de segunda fila  en columna 8  ------------------      **/
		sw.insertRow(1); 
		
		int styleIndex1 = styles.get("cab2").getIndex();
		sw.createCell(0, "Type", styleIndex1);
		sw.createCell(1, "Name", styleIndex1);

		int styleIndex = styles.get("cabecera").getIndex();
		sw.createCell(2, "Path", styleIndex1);
		sw.createCell(3, "", styleIndex1);
		sw.createCell(4, "", styleIndex1);
		sw.createCell(5, "", styleIndex1);
		sw.createCell(6, "", styleIndex1);
		sw.createCell(7, "", styleIndex1);
		
		indice = col;	
		for( productOffering po : PO){
			System.out.println(po.getDESC_BO()+"indice val =" +indice);
			for(int j =0 ; j<4;j++){ 
				if(j == 0)  {sw.createCell(indice, po.getDESC_BO(), styles.get("cab1").getIndex()); 
					indices0.add(indice);
					indice ++;	}      
				else        { sw.createCell(indice, "", styles.get("cab1").getIndex()); 
				    indice ++;		}  
				}
		}
		sw.endRow(); 
		/** ..............................Insercion de tercera  Fila .....................................**/
		sw.insertRow(2); 
		sw.createCell(0, "", styleIndex1);
		sw.createCell(1, "", styleIndex1);
		sw.createCell(2, "Product Spec", styleIndex1);
		sw.createCell(3, "Main Component", styleIndex1);
		sw.createCell(4, "Component", styleIndex1);
		sw.createCell(5, "Component", styleIndex1);
		sw.createCell(6, "Component", styleIndex1);
		sw.createCell(7, "Component", styleIndex1);
		indice = col;
		
		while (indice < (PO.size()-cont)*4+8){
			sw.createCell(indice, "", styles.get("cab1").getIndex()); 
			 indice++;
		    }
		sw.endRow(); 
		
		/** ..............................Insercion de cuarta Fila .....................................**/
		sw.insertRow(3); 
		sw.createCell(0, "", styleIndex1);
		sw.createCell(1, "", styleIndex1);
		sw.createCell(2, "", styleIndex);
		sw.createCell(3, "", styleIndex);
		sw.createCell(4, "", styleIndex);
		sw.createCell(5, "", styleIndex);
		sw.createCell(6, "", styleIndex);
		sw.createCell(7, "", styleIndex);
		indice = col;
		for( productOffering po : PO){
			 if (po.getTIPO_PLAN().contains("Prepago")) post = false;
			 
			for(int j =0 ; j<4;j++){ 
				if(j == 0)  {sw.createCell(indice, "Inclusion", styles.get("cab1").getIndex()); 
				indice ++;    		                 
				}      
				if(j == 1)  { sw.createCell(indice, "Min", styles.get("cab1").getIndex()); 
				indice ++;    		    			
				}
				if(j == 2)  { sw.createCell(indice, "Max", styles.get("cab1").getIndex()); 
				indice ++;    		    			
				}
				if(j == 3)  { sw.createCell(indice, "Def", styles.get("cab1").getIndex()); 
				indice ++;    		    			
				} 		    
			}        
		}		
		sw.endRow();  
		indice = col;
		//-------------------------------------------------------------------------------------
				indRow = escribirIncComp(sw,styles, indRow, indice, "1", post);
				//-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "DetalleLlamada","0", false, false, false, false);	
			    //-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "2", post);	    
			    //-------------------------------------------------------------------------------------    
				indRow = escribirIncPO(sw, styles,  indRow, indice, "");		
			    //-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "3", post);
			 	//-------------------------------------------------------------------------------------
				indRow = escribirIncPO(sw, styles,  indRow, indice, "FAMILIA");
				//-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "4", post);
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "SANCIONPLAN","0", false, false,false, false);
				//-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "5", post);
			    //-------------------------------------------------------------------------------------			 	
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "Equipos","0", false, false, false, false);
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "Equipo Prepago","0", false, false, false, true);
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "DsctoEquipo","0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------			    
			    indRow = escribirIncComp(sw,styles, indRow, indice, "6", post);
			    //-------------------------------------------------------------------------------------			    
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "SANCIONEQUIPO","0", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "7", post);
			    //-------------------------------------------------------------------------------------			    
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "Reposicion","0", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "8", post);
			    //-------------------------------------------------------------------------------------			    
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "SIMCard","0", false, false, false, false);
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "DsctoSIMCard","0", false, false, false, false);
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "Equipo Prepago","0", false, false, false, false);
			    //-------------------------------------------------------------------------------------		    
			    indRow = escribirIncComp(sw,styles, indRow, indice, "9", post);
			    //-------------------------------------------------------------------------------------
			    // indRow = escribirPaqInc(sw, styles, indRow, indice, "Bienvenida","0");
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "BienRepo","0", false, false, false, false);
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "BienRepoAVIOS","0", false, false, false, false);		   
			    
			    if(post){ 
			    //-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "10", post);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFVozOnnet", "1", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "11", post);
			 	//-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFVozOnnet", "3", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "12", post);
			 	//-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFVozOnnet", "4", false, false, false, false);
			    //-------------------------------------------------------------------------------------	    
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "13", post);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFVozOnnet", "6", false, false, false, false);
			    //-------------------------------------------------------------------------------------	    
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "14", post);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFVozOnnet", "9", false, false, false, false);
			    //-------------------------------------------------------------------------------------	    
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "15",  post);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFSMSOnnet","0", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "16", post);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFVozFijo","0", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "17", post);
				//-------------------------------------------------------------------------------------		  	
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "FnFVozOnnet", "99999999", false, false, false, false);  
			    //-------------------------------------------------------------------------------------			    
			    indRow = escribirIncComp(sw,styles, indRow, indice, "18", post);
				//-------------------------------------------------------------------------------------			   
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Blackberry","0", false, false, false, false);
				//-------------------------------------------------------------------------------------
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "19", post);
			    //-------------------------------------------------------------------------------------
			  	indRow = escribirPaqInc(sw, styles, indRow, indice, "ChatPack","0", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "20", post);
				//-------------------------------------------------------------------------------------
			  	indRow = escribirPaqInc(sw, styles, indRow, indice, "Internet","0", false, false, false, false);
			  	indRow = escribirPaqInc(sw, styles, indRow, indice, "WindowsMobile","0", false, false, false, false);
				//-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "21", post);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirPaqInc(sw, styles, indRow, indice, "NokiaMessaging","0", false, false, false, false);
			    //-------------------------------------------------------------------------------------
			    indRow = escribirIncComp(sw,styles, indRow, indice, "22", post);
				//-------------------------------------------------------------------------------------
			  	indRow = escribirPaqInc(sw, styles, indRow, indice, "Synchronica","0", false, false, false, false);
				//-------------------------------------------------------------------------------------
			  	indRow = escribirIncComp(sw,styles, indRow, indice, "23", post);
				//-------------------------------------------------------------------------------------
			  	indRow = escribirPaqInc(sw, styles, indRow, indice, "WAP","0", false, false, false, false);
				//-------------------------------------------------------------------------------------			  	
	     	  	indRow = escribirIncComp(sw,styles, indRow, indice, "24", post);
				//-------------------------------------------------------------------------------------
			  	indRow = escribirPaqInc(sw, styles, indRow, indice, "LDI","0", false, false, false, false);
			  	indRow = escribirPaqInc(sw, styles, indRow, indice, "LDIInfracel","0", false, false, false, false);
				//-------------------------------------------------------------------------------------				  	
				indRow = escribirIncComp(sw,styles, indRow, indice, "25", post);			
				//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "SMS", "0", false, false, false, false);
				//-------------------------------------------------------------------------------------			 	
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "26", post);			
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Short Mail", "0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------			 	
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "27", post);			
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Web Message", "0", false, false, false, false);		 	
			 	//-------------------------------------------------------------------------------------			 	
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "28", post);
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Cascada","0", false, false, false, false);
				//-------------------------------------------------------------------------------------		 	
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "29", post);			
				//-------------------------------------------------------------------------------------
				//indRow = escribirPaqInc(sw, styles, indRow, indice, "Seguros","0");
				indRow = escribirPaqInc(sw, styles, indRow, indice, "AsistenciaLabor","0", false, false, false, false);
				indRow = escribirPaqInc(sw, styles, indRow, indice, "AsistenciaLogy","0", false, false, false, false);
				indRow = escribirPaqInc(sw, styles, indRow, indice, "Andiasistencia","0", false, false, false, false);
				indRow = escribirPaqInc(sw, styles, indRow, indice, "AsistenciaClaro","0", false, false, false, false);				
				//-------------------------------------------------------------------------------------
				indRow = escribirIncComp(sw,styles, indRow, indice, "30", post);			
				//-------------------------------------------------------------------------------------
				indRow = escribirPaqInc(sw, styles, indRow, indice, "LEGISMOVIL","0", false, false, false, false);
				//-------------------------------------------------------------------------------------
				indRow = escribirIncComp(sw,styles, indRow, indice, "31", post);			
				//-------------------------------------------------------------------------------------
				indRow = escribirPaqInc(sw, styles, indRow, indice, "RingBackTone", "0", false, false, false, false);
				//-------------------------------------------------------------------------------------
				indRow = escribirIncComp(sw,styles, indRow, indice, "32", post);			
				//-------------------------------------------------------------------------------------
				indRow = escribirPaqInc(sw, styles, indRow, indice, "VideoLlamada","0", false, false, false, false);
				//-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "33", post);		
				//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "VozAVoz","0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------
				indRow = escribirIncComp(sw,styles, indRow, indice, "34", post);		
				//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobroDirTel","0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "35", post);		
				//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "WEBTV","0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "36", post);	
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "RecordacionMIN","0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "37", post);			 	
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "RollOver","0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "38", post);		
				//-------------------------------------------------------------------------------------
			 	//indRow = escribirPaqInc(sw, styles, indRow, indice, "IdentificaLlamada","0");
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "IdLlamada","0", false, false, false, false);
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "IdLlamadaRes","0", false, false, false, false);	
			 	//-------------------------------------------------------------------------------------
	          	indRow = escribirIncComp(sw,styles, indRow, indice, "39", post);
				//-------------------------------------------------------------------------------------	
			 	indRow = escribirPaqInc(sw, styles, indRow, indice, "BuzonVoz","0", false, false, false, false);
			 	//-------------------------------------------------------------------------------------
			 	indRow = escribirIncComp(sw,styles, indRow, indice, "40", post);}
			    
			    else{	    	
			    	
			    	indRow = escribirIncComp(sw,styles, indRow, indice, "10", post);
					//-------------------------------------------------------------------------------------
			    	//indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegVZTD","0");
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegAcum","0", true, true, false, false);
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "11", post);
					//-------------------------------------------------------------------------------------
			    	//indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegVZTD","0");
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobRevElegVZTD","0", true, true, false, false);
				 	//-------------------------------------------------------------------------------------
			    	indRow = escribirIncComp(sw,styles, indRow, indice, "12", post);
					//-------------------------------------------------------------------------------------
			    	//indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegVZTD","0");
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegAcum","0", false, true, false, false);
				 	//-------------------------------------------------------------------------------------
					indRow = escribirIncComp(sw,styles, indRow, indice, "13", post);
					//-------------------------------------------------------------------------------------
					indRow = escribirPaqInc(sw, styles, indRow, indice, "BFNFVTD1","0", false, true, false, false);
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "BFNFVTD3","0", false, true, false, false);				 	
				 	//-------------------------------------------------------------------------------------			 	
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "14", post);
			    	//-------------------------------------------------------------------------------------
			    	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegAcum","0", true, false, false, false);
				 	//-------------------------------------------------------------------------------------
			    	indRow = escribirIncComp(sw,styles, indRow, indice, "15", post);
			    	//-------------------------------------------------------------------------------------
			    	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegAcum","0", true, false, true, false);
				 	//-------------------------------------------------------------------------------------	
			    	indRow = escribirIncComp(sw,styles, indRow, indice, "16", post);
			    	//-------------------------------------------------------------------------------------
			    	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobRevElegVZTD","0", true, false, false, false);
				 	//-------------------------------------------------------------------------------------			    	
			    	indRow = escribirIncComp(sw,styles, indRow, indice, "17", post);
			    	//-------------------------------------------------------------------------------------
			    	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobCambElegSMSPre","0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------
			    	indRow = escribirIncComp(sw,styles, indRow, indice, "18", post);
					//-------------------------------------------------------------------------------------
			    	indRow = escribirPaqInc(sw, styles, indRow, indice, "FNFSMSTD5","0", false, false, false, false);
			    	//indRow = escribirPaqInc(sw, styles, indRow, indice, "CobRevElegSMSPre","0");
				 	//-------------------------------------------------------------------------------------
			    	indRow = escribirIncComp(sw,styles, indRow, indice, "19", post);
					//-------------------------------------------------------------------------------------			   
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Blackberry","0", false, false, false, false);
					//-------------------------------------------------------------------------------------
				  	indRow = escribirIncComp(sw,styles, indRow, indice, "20", post);
				    //-------------------------------------------------------------------------------------
				  	indRow = escribirPaqInc(sw, styles, indRow, indice, "ChatPack","0", false, false, false, false);
				    //-------------------------------------------------------------------------------------				  					  	
				  	indRow = escribirIncComp(sw,styles, indRow, indice, "21", post);
				    //-------------------------------------------------------------------------------------
				  	indRow = escribirPaqInc(sw, styles, indRow, indice, "Internet","0", false, false, false, false);
					indRow = escribirPaqInc(sw, styles, indRow, indice, "WindowsMobile","0", false, false, false, false);
				    //-------------------------------------------------------------------------------------				  	
				    indRow = escribirIncComp(sw,styles, indRow, indice, "22", post);
				    //-------------------------------------------------------------------------------------
				    indRow = escribirPaqInc(sw, styles, indRow, indice, "NokiaMessaging","0", false, false, false, false);
				    //-------------------------------------------------------------------------------------
				    indRow = escribirIncComp(sw,styles, indRow, indice, "23", post);
					//-------------------------------------------------------------------------------------
				  	indRow = escribirPaqInc(sw, styles, indRow, indice, "Synchronica","0", false, false, false, false);
					//-------------------------------------------------------------------------------------
				  	indRow = escribirIncComp(sw,styles, indRow, indice, "24", post);
					//-------------------------------------------------------------------------------------
				  	indRow = escribirPaqInc(sw, styles, indRow, indice, "WAP","0", false, false, false, false);
					//-------------------------------------------------------------------------------------			  	
		     	  	indRow = escribirIncComp(sw,styles, indRow, indice, "25", post);
					//-------------------------------------------------------------------------------------
				  	indRow = escribirPaqInc(sw, styles, indRow, indice, "LDI","0", false, false, false, false);
				  	indRow = escribirPaqInc(sw, styles, indRow, indice, "LDIInfracel","0", false, false, false, false);
					//-------------------------------------------------------------------------------------				  	
					indRow = escribirIncComp(sw,styles, indRow, indice, "26", post);			
					//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "SMS", "0", false, false, false, false);
					//-------------------------------------------------------------------------------------			 	
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "27", post);			
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Short Mail", "0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------			 	
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "28", post);			
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Web Message", "0", false, false, false, false);		 	
				 	//-------------------------------------------------------------------------------------			 	
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "29", post);
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "Cascada","0", false, false, false, false);
					//-------------------------------------------------------------------------------------		 	
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "30", post);			
					//-------------------------------------------------------------------------------------
					//indRow = escribirPaqInc(sw, styles, indRow, indice, "Seguros","0");
					indRow = escribirPaqInc(sw, styles, indRow, indice, "AsistenciaLabor","0", false, false, false, false);
					indRow = escribirPaqInc(sw, styles, indRow, indice, "AsistenciaLogy","0", false, false, false, false);
					indRow = escribirPaqInc(sw, styles, indRow, indice, "Andiasistencia","0", false, false, false, false);
					indRow = escribirPaqInc(sw, styles, indRow, indice, "AsistenciaClaro","0", false, false, false, false);				
					//-------------------------------------------------------------------------------------
					indRow = escribirIncComp(sw,styles, indRow, indice, "31", post);			
					//-------------------------------------------------------------------------------------
					indRow = escribirPaqInc(sw, styles, indRow, indice, "LEGISMOVIL","0", false, false, false, false);
					//-------------------------------------------------------------------------------------
					indRow = escribirIncComp(sw,styles, indRow, indice, "32", post);			
					//-------------------------------------------------------------------------------------
					indRow = escribirPaqInc(sw, styles, indRow, indice, "RingBackTone", "0", false, false, false, false);
					//-------------------------------------------------------------------------------------
					indRow = escribirIncComp(sw,styles, indRow, indice, "33", post);			
					//-------------------------------------------------------------------------------------
					indRow = escribirPaqInc(sw, styles, indRow, indice, "VideoLlamada","0", false, false, false, false);
					//-------------------------------------------------------------------------------------
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "34", post);		
					//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "VozAVoz","0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------
					indRow = escribirIncComp(sw,styles, indRow, indice, "35", post);		
					//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "CobroDirTel","0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "36", post);		
					//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "WEBTV","0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "37", post);	
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "RecordacionMIN","0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "38", post);			 	
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "RollOver","0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "39", post);		
					//-------------------------------------------------------------------------------------
				 	//indRow = escribirPaqInc(sw, styles, indRow, indice, "IdentificaLlamada","0");
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "IdLlamada","0", false, false, false, false);
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "IdLlamadaRes","0", false, false, false, false);	
				 	//-------------------------------------------------------------------------------------
		          	indRow = escribirIncComp(sw,styles, indRow, indice, "40", post);
					//-------------------------------------------------------------------------------------	
				 	indRow = escribirPaqInc(sw, styles, indRow, indice, "BuzonVoz","0", false, false, false, false);
				 	//-------------------------------------------------------------------------------------
				 	System.out.println("Antes de Ultimo de los ciclo de componentes ");
				 	indRow = escribirIncComp(sw,styles, indRow, indice, "41", post);
				 	System.out.println("Termina Ultimo de los ciclo de componentes ");
			    }			    
			 	
			 	System.out.println(" indice de RegULT "+indRow);
				//-------------------------------------------------------------------------------------
		sw.endSheet();        
		sw.mergeCell(indices0, 1, 0);
		sw.endWorkSheet();  
	}
		///------------------------------------------------------------------------
	/**
	 *
	 * @param zipfile the template file
	 * @param tmpfile the XML file with the sheet data
	 * @param entry the name of the sheet entry to substitute, e.g. xl/worksheets/sheet1.xml
	 * @param out the stream to write the result to
	 */
	private static void substitute(File zipfile, File tmpfile, String entry, OutputStream out) throws IOException {
		ZipFile zip = new ZipFile(zipfile);

		ZipOutputStream zos = new ZipOutputStream(out);

		@SuppressWarnings("unchecked")
		Enumeration<ZipEntry> en = (Enumeration<ZipEntry>) zip.entries();
		
		while (en.hasMoreElements()) {
			ZipEntry ze = en.nextElement();
			if(!ze.getName().equals(entry)){
				zos.putNextEntry(new ZipEntry(ze.getName()));
				InputStream is = zip.getInputStream(ze);
				copyStream(is, zos);
				is.close();
			}
		}
		zos.putNextEntry(new ZipEntry(entry));
		InputStream is = new FileInputStream(tmpfile);
		copyStream(is, zos);
		is.close();

		zos.close();
	}

	private static void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] chunk = new byte[1024];
		int count;
		while ((count = in.read(chunk)) >=0 ) {
			out.write(chunk,0,count);
		}
	}

	private static int escribirIncPO(SpreadsheetWriter sw,  Map<String, XSSFCellStyle> styles, int indRow, int indice, String fam) throws Exception{
		IDataModel model = new DataModel();	
		Collection <productOffering> PO = model.getProductOffering();
		List <productOffering> BOCol= model.getBO();
		int largo = 0;
		String dato = "";
		int temp = indice;
		for(productOffering Col : BOCol ){
			
		   if(Col.getTIPO_PLAN_FAM()==null &&fam.equals("")) {
			indRow++;
			sw.insertRow(indRow);
			for(int i= 0; i< indice;i++){			
				if (i == 0) sw.createCell(i, "Abstract Price", styles.get("styCel").getIndex()); 	
				if (i == 1) sw.createCell(i, Col.getDESC_BO(), styles.get("styCel").getIndex());	
				if (i == 2) sw.createCell(i, "Wireless", styles.get("styCel").getIndex());	
				if (i == 3) sw.createCell(i, "Wireless Main", styles.get("styCel").getIndex());	
				if (i == 4) sw.createCell(i, "Base Plan", styles.get("styCel").getIndex());	
				if (i == 5) sw.createCell(i, "", styles.get("styCel").getIndex());
				if (i == 6) sw.createCell(i, "", styles.get("styCel").getIndex());
				if (i == 7) sw.createCell(i, "", styles.get("styCel").getIndex());
			}
			for(productOffering po: PO){
				dato =  Col.getDESC_BO().trim();
				largo = Col.getDESC_BO().length();
				largo = largo - 13;	
				if(largo >=0) dato = dato.substring(0, largo);			

				if(po.getDESC_BO().trim().equals(Col.getDESC_BO().trim()) || (po.getDESC_BO().trim().equals(dato)&&po.getDESC_BO().trim().contains("- Promo"))){
					sw.createCell(temp, "mandatory", styles.get("styCel").getIndex()); 	
					temp++;
					
				}
				else if(Col.getDESC_BO().contains("Suspension - Comcel")){
					sw.createCell(temp, "selected by default", styles.get("styCel").getIndex());
					temp++;		
					
				}else if(Col.getDESC_BO().contains("Numero VIP")){
					sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex());
					temp++;		
					
				}else {
					sw.createCell(temp, "excluded", styles.get("styCel").getIndex());
					temp++;							
				       }
				sw.createCell(temp, "", styles.get("styCel").getIndex());
				temp++;
				sw.createCell(temp, "", styles.get("styCel").getIndex());
				temp++;
				sw.createCell(temp, "", styles.get("styCel").getIndex());
				temp++;

			}
			sw.endRow();
			temp = indice;}
		   if(Col.getTIPO_PLAN_FAM()!=null ) {
			   if(Col.getTIPO_PLAN_FAM().equals(fam)){			   
				indRow++;
				sw.insertRow(indRow);
				for(int i= 0; i< indice;i++){			
					if (i == 0) sw.createCell(i, "Abstract Price", styles.get("styCel").getIndex()); 	
					if (i == 1) sw.createCell(i, Col.getDESC_BO(), styles.get("styCel").getIndex());	
					if (i == 2) sw.createCell(i, "Wireless", styles.get("styCel").getIndex());	
					if (i == 3) sw.createCell(i, "Wireless Main", styles.get("styCel").getIndex());	
					if (i == 4) sw.createCell(i, "Base Plan", styles.get("styCel").getIndex());	
					if (i == 5) sw.createCell(i, "", styles.get("styCel").getIndex());
					if (i == 6) sw.createCell(i, "", styles.get("styCel").getIndex());
					if (i == 7) sw.createCell(i, "", styles.get("styCel").getIndex());
				}
				for(productOffering po: PO){
					dato =  Col.getDESC_BO().trim();
					largo = Col.getDESC_BO().length();
					largo = largo - 13;	
					if(largo >=0) dato = dato.substring(0, largo);	
					if(po.getDESC_BO().trim().equals(Col.getDESC_BO().trim()) || po.getDESC_BO().trim().equals(dato)){
						sw.createCell(temp, "mandatory", styles.get("styCel").getIndex()); 	
						temp++;						
					}	
					else {
						sw.createCell(temp, "excluded", styles.get("styCel").getIndex());
						temp++;											
					}
					sw.createCell(temp, "", styles.get("styCel").getIndex());
					temp++;
					sw.createCell(temp, "", styles.get("styCel").getIndex());
					temp++;
					sw.createCell(temp, "", styles.get("styCel").getIndex());
					temp++;
				}
				sw.endRow();
				temp = indice;}}
		  	}   
		return indRow;
	}	
	///------------------------------ 20PCION LLEVAMOS UNA LISTA CON TODOS LOS TMCODE EN LOS QUE ESTAN INCLUIDOS LOS PAQ----------------------------------------------------
	private static int escribirPaqInc(SpreadsheetWriter sw,  Map<String, XSSFCellStyle> styles, int indRow, int indice, String tipoPaq, String eleg, boolean inplan, boolean bfnf, boolean fnfsms, boolean eq ) throws Exception{
		IDataModel model = new DataModel();	
		POCodNum ponum = new POCodNum ();
		Collection <productOffering> PO = model.getProductOffering();
		List<productOffering> paq = model.getBOColumna();
		List<POCodNum> paqList = new ArrayList<POCodNum>();
		Boolean flag = false;
		String flat = "0";
		
		int temp = indice;
		for(productOffering PAQUET : paq){
			      flat = "0";
			    if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozFijo")|| PAQUET.getTIPO_PAQ().trim().equals("FnFSMSOnnet")) 
			    	  flat = PAQUET.getNUM_ELEG().trim();
			    else flat = eleg; 
			
			if(PAQUET.getTIPO_PAQ().trim().equals(tipoPaq) && PAQUET.getNUM_ELEG().trim().equals(flat)  ){
				indRow++;
				sw.insertRow(indRow);
				for(int i= 0; i< indice;i++){			
					if (i == 0) {sw.createCell(i, "Abstract Price", styles.get("styCel").getIndex());	}
		           	if (i == 1) {sw.createCell(i, PAQUET.getDESC_BO(), styles.get("styCel").getIndex());}					
					if (i == 2) {sw.createCell(i, "Wireless", styles.get("styCel").getIndex());	}
					if (i == 3) {sw.createCell(i, "Wireless Main", styles.get("styCel").getIndex()); }	
					if (i == 4) {	
						if(PAQUET.getTIPO_PAQ().trim().equals("Blackberry")) {sw.createCell(i, "GPRS", styles.get("styCel").getIndex()); }      
						if(PAQUET.getTIPO_PAQ().trim().equals("Internet")) { sw.createCell(i, "GPRS", styles.get("styCel").getIndex());}  
						if(PAQUET.getTIPO_PAQ().trim().equals("SMS")){ sw.createCell(i, "Messaging", styles.get("styCel").getIndex()); }     
						if(PAQUET.getTIPO_PAQ().trim().equals("VideoLlamada")){sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex());} 
						if(PAQUET.getTIPO_PAQ().trim().contains("Asistencia") ||PAQUET.getTIPO_PAQ().trim().contains("Andiasistencia")){sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex());} 
						if(PAQUET.getTIPO_PAQ().trim().contains("LDI")){ sw.createCell(i, "International", styles.get("styCel").getIndex()); }  
						if(PAQUET.getTIPO_PAQ().trim().equals("Equipos")||PAQUET.getTIPO_PAQ().trim().contains("SANCIONEQUIPO")||PAQUET.getTIPO_PAQ().trim().contains("DsctoEquipo")){ sw.createCell(i, "Equipment &amp; SIM", styles.get("styCel").getIndex()); }   
						if(PAQUET.getTIPO_PAQ().trim().equals("RecordacionMIN")){ sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex()); }   
						if(PAQUET.getTIPO_PAQ().trim().equals("SANCIONPLAN")) sw.createCell(i, "Plan Commitment", styles.get("styCel").getIndex());
						if(PAQUET.getTIPO_PAQ().trim().equals("Bienvenida")||PAQUET.getTIPO_PAQ().trim().contains("BienRepo")||PAQUET.getTIPO_PAQ().trim().equals("Equipo Prepago")||PAQUET.getTIPO_PAQ().trim().equals("SIMCard Prepago")) {sw.createCell(i, "Equipment &amp; SIM", styles.get("styCel").getIndex()); }      
						if(PAQUET.getTIPO_PAQ().trim().equals("ChatPack")){ sw.createCell(i, "GPRS", styles.get("styCel").getIndex()); }     
						if(PAQUET.getTIPO_PAQ().trim().equals("Synchronica")){sw.createCell(i, "GPRS", styles.get("styCel").getIndex());} 
						if(PAQUET.getTIPO_PAQ().trim().equals("Short Mail")){sw.createCell(i, "Messaging", styles.get("styCel").getIndex());} 
						if(PAQUET.getTIPO_PAQ().trim().equals("Web Message")){sw.createCell(i, "Messaging", styles.get("styCel").getIndex());} 
						if(PAQUET.getTIPO_PAQ().trim().equals("Cascada")){sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex());} 
						if(PAQUET.getTIPO_PAQ().trim().equals("LEGISMOVIL")){ sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex()); }  
						if(PAQUET.getTIPO_PAQ().trim().equals("RingBackTone")){ sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex()); }  
						if(PAQUET.getTIPO_PAQ().trim().equals("VozAVoz")) {sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex()); }      
						if(PAQUET.getTIPO_PAQ().trim().equals("RollOver")) { sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex());}  
						if(PAQUET.getTIPO_PAQ().trim().contains("IdLlamada")){ sw.createCell(i, "Voice Basic Features", styles.get("styCel").getIndex()); }
						if(PAQUET.getTIPO_PAQ().trim().equals("WindowsMobile")) { sw.createCell(i, "GPRS", styles.get("styCel").getIndex());}  
						if(PAQUET.getTIPO_PAQ().trim().equals("NokiaMessaging")) { sw.createCell(i, "GPRS", styles.get("styCel").getIndex());}  
						if(PAQUET.getTIPO_PAQ().trim().equals("WAP")) { sw.createCell(i, "GPRS", styles.get("styCel").getIndex());}  
						if(PAQUET.getTIPO_PAQ().trim().equals("WEBTV")||PAQUET.getTIPO_PAQ().trim().equals("CobroDirTel")) { sw.createCell(i, "Value Added Services", styles.get("styCel").getIndex());}
						if(PAQUET.getTIPO_PAQ().trim().equals("BuzonVoz")) { sw.createCell(i, "Voice Basic Features", styles.get("styCel").getIndex());}
						if(PAQUET.getTIPO_PAQ().trim().contains("SIMCard")||PAQUET.getTIPO_PAQ().trim().contains("DsctoSIMCard")) {sw.createCell(i, "Equipment &amp; SIM", styles.get("styCel").getIndex()); } 
						if(PAQUET.getTIPO_PAQ().trim().equals("Equipo Prepago")||PAQUET.getTIPO_PAQ().trim().equals("Reposicion")) {sw.createCell(i, "Equipment &amp; SIM", styles.get("styCel").getIndex()); }      
						if(PAQUET.getTIPO_PAQ().trim().contains("BFNF")||(PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD")&&bfnf)||(PAQUET.getTIPO_PAQ().trim().contains("CambElegAcum")&&bfnf))sw.createCell(i,"Best Friends and Family", styles.get("styCel").getIndex());						 
						if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozOnnet")||PAQUET.getTIPO_PAQ().trim().equals("FnFVozFijo")||PAQUET.getTIPO_PAQ().trim().equals("FnFSMSOnnet")||PAQUET.getTIPO_PAQ().trim().equals("FNFSMSTD5")||PAQUET.getTIPO_PAQ().trim().contains("ElegSMSPre")||(PAQUET.getTIPO_PAQ().trim().contains("ElegAcum")&&!bfnf)||(PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD")&&!bfnf)) { sw.createCell(i, "Friends &amp; Family", styles.get("styCel").getIndex());}  
					}
					if (i == 5) {  if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozOnnet")) {
						               if(!PAQUET.getNUM_ELEG().equals("0"))sw.createCell(i, "Additional", styles.get("styCel").getIndex());
						               else sw.createCell(i, "In Plan", styles.get("styCel").getIndex());
						            }if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozFijo")||PAQUET.getTIPO_PAQ().trim().equals("FnFSMSOnnet")){ sw.createCell(i, "In Plan", styles.get("styCel").getIndex()); } 
						            else if(PAQUET.getTIPO_PAQ().trim().equals("FNFSMSTD5")||(PAQUET.getTIPO_PAQ().trim().contains("ElegSMSPre")&&!inplan)) { sw.createCell(i, "Additional", styles.get("styCel").getIndex());}  
						            else if((PAQUET.getTIPO_PAQ().trim().contains("ElegSMSPre")&&inplan)||(PAQUET.getTIPO_PAQ().trim().contains("ElegAcum")&&!bfnf)||(PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD")&&!bfnf)) { sw.createCell(i, "In Plan", styles.get("styCel").getIndex());}  
						            else if(PAQUET.getTIPO_PAQ().trim().equals("Equipos")||(PAQUET.getTIPO_PAQ().trim().equals("Equipo Prepago")&&eq)||PAQUET.getTIPO_PAQ().trim().contains("DsctoEquipo")) sw.createCell(i, "Equipment", styles.get("styCel").getIndex());
						            else if(PAQUET.getTIPO_PAQ().trim().equals("SIMCard")||PAQUET.getTIPO_PAQ().trim().equals("DsctoSIMCard")) sw.createCell(i, "SIM Card", styles.get("styCel").getIndex());	
						            else if(PAQUET.getTIPO_PAQ().trim().equals("RecordacionMIN")){ sw.createCell(i, "RECORDATION de MIN", styles.get("styCel").getIndex()); }   
						            else if(PAQUET.getTIPO_PAQ().trim().equals("SANCIONEQUIPO")) sw.createCell(i, "Equipment Commitment", styles.get("styCel").getIndex());					               
					                else if(PAQUET.getTIPO_PAQ().trim().equals("Seguros")||PAQUET.getTIPO_PAQ().trim().contains("Asistencia") ||PAQUET.getTIPO_PAQ().trim().contains("Andiasistencia")) sw.createCell(i, "Insurance", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().contains("LDI")) sw.createCell(i, "LDI Voice", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().contains("Blackberry")) sw.createCell(i, "Blackberry", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().contains("Internet")) sw.createCell(i, "Internet", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().equals("Bienvenida")||PAQUET.getTIPO_PAQ().trim().contains("BienRepo")) {sw.createCell(i, "Welcome Promotion", styles.get("styCel").getIndex()); }  
					                else if(PAQUET.getTIPO_PAQ().trim().equals("ChatPack")){ sw.createCell(i, "Chat Pack Windows", styles.get("styCel").getIndex()); }     
					                else if(PAQUET.getTIPO_PAQ().trim().equals("Synchronica")){sw.createCell(i, "Synchronica", styles.get("styCel").getIndex());} 
					                else if(PAQUET.getTIPO_PAQ().trim().equals("Cascada")){sw.createCell(i, "Cascadas Service", styles.get("styCel").getIndex());} 
					                else if(PAQUET.getTIPO_PAQ().trim().equals("LEGISMOVIL")){ sw.createCell(i, "Legismovil Service", styles.get("styCel").getIndex()); }  
					                else if(PAQUET.getTIPO_PAQ().trim().equals("RingBackTone")){ sw.createCell(i, "Ring Back Tone", styles.get("styCel").getIndex()); }  
					                else if(PAQUET.getTIPO_PAQ().trim().equals("VozAVoz")) {sw.createCell(i, "Voice to Voice Magazine", styles.get("styCel").getIndex()); }      
					                else if(PAQUET.getTIPO_PAQ().trim().equals("RollOver")) { sw.createCell(i, "Pasaminutos", styles.get("styCel").getIndex());}  
					                else if(PAQUET.getTIPO_PAQ().trim().contains("IdLlamada")){ sw.createCell(i, "Caller ID Presentation", styles.get("styCel").getIndex()); }
					                else if(PAQUET.getTIPO_PAQ().trim().equals("Short Mail")){sw.createCell(i, "Short Mail", styles.get("styCel").getIndex());} 
					                else if(PAQUET.getTIPO_PAQ().trim().equals("Web Message")){sw.createCell(i, "Web Message", styles.get("styCel").getIndex());} 
					                else if(PAQUET.getTIPO_PAQ().trim().equals("CobroDirTel")){sw.createCell(i, "Telephone Directory", styles.get("styCel").getIndex());} 
					                else if(PAQUET.getTIPO_PAQ().trim().equals("WindowsMobile")) { sw.createCell(i, "Internet", styles.get("styCel").getIndex());}
					                else if(PAQUET.getTIPO_PAQ().trim().equals("VideoLlamada")) sw.createCell(i, "Video Call", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().equals("DetalleLlamada")) sw.createCell(i,"", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().equals("NokiaMessaging")) { sw.createCell(i, "Nokia Messaging", styles.get("styCel").getIndex());}  
					                else if(PAQUET.getTIPO_PAQ().trim().equals("WAP")) sw.createCell(i,"WAP", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().equals("WEBTV")) sw.createCell(i,"Web TV", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().equals("BuzonVoz")) { sw.createCell(i, "Voice Mail", styles.get("styCel").getIndex());}
					                else if(PAQUET.getTIPO_PAQ().trim().equals("SMS")){ sw.createCell(i, "SMS", styles.get("styCel").getIndex()); }  
					                else if(PAQUET.getTIPO_PAQ().trim().equals("Reposicion")){ sw.createCell(i, "Fulfillment &amp; Return", styles.get("styCel").getIndex()); } 
					                else if(PAQUET.getTIPO_PLAN_FAM()!=null)sw.createCell(i,"Family Group", styles.get("styCel").getIndex());
					                else if(PAQUET.getTIPO_PAQ().trim().contains("BFNF")||(PAQUET.getTIPO_PAQ().trim().contains("CambElegAcum")&&!inplan))sw.createCell(i,"BFNF Additional", styles.get("styCel").getIndex());
						            else if(PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD")||(PAQUET.getTIPO_PAQ().trim().contains("CambElegAcum")&&inplan))sw.createCell(i,"BFNF In Plan", styles.get("styCel").getIndex());
						            else if(PAQUET.getTIPO_PAQ().trim().equals("Equipo Prepago")&&!eq)sw.createCell(i, "SIM Card", styles.get("styCel").getIndex());
						            else sw.createCell(i,"", styles.get("styCel").getIndex());				                				                }
								    
					if (i == 6) {	 if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozOnnet")) { 
										 if(!PAQUET.getNUM_ELEG().equals("99999999"))sw.createCell(i, "F&amp;F Voice Additional", styles.get("styCel").getIndex());
										 else  sw.createCell(i, "FNF Voice", styles.get("styCel").getIndex());} 
									 else if((PAQUET.getTIPO_PAQ().trim().contains("ElegAcum")||PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD"))&&!bfnf&&!fnfsms) sw.createCell(i, "FNF Voice", styles.get("styCel").getIndex());
									 else if(PAQUET.getTIPO_PAQ().trim().contains("LDI")) sw.createCell(i, "LDI Voice Infracel", styles.get("styCel").getIndex());
									 else if(PAQUET.getTIPO_PAQ().trim().contains("BFNF")||(PAQUET.getTIPO_PAQ().trim().contains("CambElegAcum")&&!inplan))sw.createCell(i,"BFNF Voice Additional", styles.get("styCel").getIndex());
									 else if(PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD")||(PAQUET.getTIPO_PAQ().trim().contains("CambElegAcum")&&inplan&&!fnfsms))sw.createCell(i,"BFNF Voice", styles.get("styCel").getIndex());
									 else if(PAQUET.getTIPO_PAQ().trim().contains("ElegAcum")&&fnfsms)sw.createCell(i, "FNF SMS", styles.get("styCel").getIndex());
									 else if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozFijo")||PAQUET.getTIPO_PAQ().trim().equals("FnFSMSOnnet")){ sw.createCell(i, "FNF SMS", styles.get("styCel").getIndex()); } 
									 else if(PAQUET.getTIPO_PAQ().trim().equals("FNFSMSTD5")||(PAQUET.getTIPO_PAQ().trim().contains("ElegSMSPre")&&!inplan)) sw.createCell(i, "FNF SMS Additional", styles.get("styCel").getIndex());
									 else if(PAQUET.getTIPO_PAQ().trim().contains("ElegSMSPre")&&inplan) sw.createCell(i, "FNF SMS", styles.get("styCel").getIndex());
									 else sw.createCell(i, "", styles.get("styCel").getIndex());}
					if (i == 7) {  
						           if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozOnnet")) { 
						        	    if(!PAQUET.getNUM_ELEG().equals("99999999"))sw.createCell(i, eleg+" Onnet Wireless Voice Recurring", styles.get("styCel").getIndex());
						        	    else sw.createCell(i,"Voice Onnet Wireless", styles.get("styCel").getIndex());}
						           		else if(PAQUET.getTIPO_PAQ().trim().contains("BFNFVTD"))sw.createCell(i,"BFNF Voice All Destination Additional", styles.get("styCel").getIndex());
						           		else if(PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD")&&inplan&&bfnf)sw.createCell(i,"BFNF Voice All Destination", styles.get("styCel").getIndex());
						           		else if(PAQUET.getTIPO_PAQ().trim().equals("FnFVozFijo"))sw.createCell(i, "Voice Onnet Fix", styles.get("styCel").getIndex());
						           		else if(PAQUET.getTIPO_PAQ().trim().contains("ElegVZTD")&&!bfnf)sw.createCell(i,"FNF Voice All Destination", styles.get("styCel").getIndex());
						           		else if(PAQUET.getTIPO_PAQ().trim().equals("FNFSMSTD5")) sw.createCell(i, "FNF SMS Onnet Additional", styles.get("styCel").getIndex());
						           	        		else if(PAQUET.getTIPO_PAQ().trim().equals("FnFSMSOnnet")) sw.createCell(i, "SMS Onnet Wireless", styles.get("styCel").getIndex());
						           	    else sw.createCell(i, "", styles.get("styCel").getIndex());  	 
					            }
				}				
				ponum.setSpcode(Integer.parseInt(PAQUET.getSPCODE()));
				paqList=  model.defExistPaqPo(ponum);
				for(productOffering  Poffer : PO){
					flag=false;
					for(POCodNum auxPoCodNum:paqList){
						if(Poffer.getTMCODE().equals(auxPoCodNum.getTmcode()+"")){
							flag=true;
							break;
						}
					}					
					
					if (flag == true) { 
						// Se agrega codigo para las diferentes paquetes y po con valores en cardinalidad para componentes Internet,  video call  
						 if((Poffer.getTMCODE().equals("7332")||Poffer.getTMCODE().equals("7333")||Poffer.getTMCODE().equals("7334")||Poffer.getTMCODE().equals("7445")||Poffer.getTMCODE().equals("7446")||Poffer.getTMCODE().equals("7444")||Poffer.getTMCODE().equals("7447")
								 || Poffer.getTMCODE().equals("7448")||Poffer.getDESC_BO().contains("Pocket Mail")||Poffer.getDESC_BO().contains("Pocket Chat")) &&PAQUET.getTIPO_PAQ().trim().equals("Internet")){
						     sw.createCell(temp, "excluded", styles.get("styCel").getIndex());					
							 temp++;
						     }
						 else if ((Poffer.getTMCODE().equals("7448")||Poffer.getTMCODE().equals("7447"))&&PAQUET.getTIPO_PAQ().trim().equals("Videollamada")){
							 sw.createCell(temp, "excluded", styles.get("styCel").getIndex());
							 temp++;	 						 
						     }
						 else if ((Poffer.getTMCODE().equals("5332")||Poffer.getTMCODE().equals("6191")||Poffer.getTMCODE().equals("5983")||Poffer.getTMCODE().equals("7937")&&PAQUET.getDESC_BO().contains("LDI ARGOS-BCOLOMBIA-CHOCOLATES"))){
							 sw.createCell(temp, "excluded", styles.get("styCel").getIndex());
							 temp++; 
							 }
						 else if((Poffer.getDESC_BO().contains("Pocket Mail")||Poffer.getDESC_BO().contains("WINDOWS CHAT")||Poffer.getDESC_BO().contains("Pocket Chat")||Poffer.getDESC_BO().contains("Telemic Unico"))&&PAQUET.getTIPO_PAQ().trim().equals("WAP")){
							 sw.createCell(temp, "excluded", styles.get("styCel").getIndex()); 
							 temp++;						 
						 }else{	
							 //....................... Termina codigo temporal para las nateriores PO y paquetes ..................
							 sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex()); 	
							 temp++;}
					}
					else {   
							 if(Poffer.getTMCODE().equals("0")){
								  sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex()); 
								  temp++;
							 } else{
								 if( PAQUET.getDESC_BO().equals("Seguros")||PAQUET.getTIPO_PAQ().equals("DsctoSIMCard")||
								     PAQUET.getDESC_BO().equals("Bienvenida Reposiciones")||PAQUET.getDESC_BO().equals("Rollover Pasaminutos")||
								     PAQUET.getDESC_BO().contains("Identificador Llamadas")||PAQUET.getDESC_BO().contains("Detalle de Llamadas")||
								     PAQUET.getTIPO_PAQ().contains("SANCION")||PAQUET.getTIPO_PAQ().contains("RecordacionMIN")||(PAQUET.getTIPO_PAQ().contains("Equipo Prepago")&&eq)||
								     PAQUET.getTIPO_PAQ().contains("DsctoEquipo")||PAQUET.getTIPO_PAQ().contains("Reposicion")||PAQUET.getTIPO_PAQ().trim().equals("CobroDirTel"))
								     sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex()); 	
								/* else if (Poffer.getELEGIDOS_BFNF().equals("0")&&(PAQUET.getTIPO_PAQ().contains("BFNF"))) 
									 sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex()); */
								 else if(PAQUET.getTIPO_PAQ().contains("Web Message")||(PAQUET.getTIPO_PAQ().contains("Equipo Prepago")&&!eq))//||PAQUET.getTIPO_PAQ().equals("SIMCard"))
									 sw.createCell(temp, "selected by default", styles.get("styCel").getIndex()); 
								 else if(!Poffer.getELEGIDOS_BFNF().equals("0")&&PAQUET.getTIPO_PAQ().contains("ElegVZTD")&&bfnf)
									 sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex()); 
								 else if(!Poffer.getELEGIDOS_BFNF().equals("0")&&PAQUET.getTIPO_PAQ().contains("ElegAcum")&&inplan&&bfnf){
									 sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex()); 
								 }else if(Poffer.getELEGIDOS_BFNF().equals("0")&&PAQUET.getTIPO_PAQ().contains("ElegAcum")&&!inplan&&bfnf){
									 sw.createCell(temp, "excluded", styles.get("styCel").getIndex());  }							 
								/* else if(!Poffer.getELEGIDOS_FNF().equals("0")&&PAQUET.getTIPO_PAQ().contains("ElegAcum")&&!bfnf){//-- ojo para futuras cobros de FNF Voice y SMS falta una bandera para definir cuando es SMS-----------------
									 sw.createCell(temp, "excluded", styles.get("styCel").getIndex());  }*/
								 else if(!Poffer.getELEGIDOS_FNF().equals("0")&&PAQUET.getTIPO_PAQ().contains("ElegVZTD")&&!bfnf){
									 sw.createCell(temp, "not selected by default", styles.get("styCel").getIndex());  }
								 else sw.createCell(temp, "excluded", styles.get("styCel").getIndex()); 	
								 temp++;    
								 }    		
					}
                    sw.createCell(temp, "", styles.get("styCel").getIndex());
					temp++;
					sw.createCell(temp, "", styles.get("styCel").getIndex());
					temp++;
					sw.createCell(temp, "", styles.get("styCel").getIndex());
					temp++;
					
				}
				sw.endRow();
				temp = indice;	
				
			}
		}

		return indRow;
	}
	/**
	 * Writes spreadsheet data in a Writer.
	 * (YK: in future it may evolve in a full-featured API for streaming data in Excel)
	 */
	public static class SpreadsheetWriter {
		private final Writer _out;
		private int _rownum;

		public SpreadsheetWriter(Writer out){
			_out = out;
		}		
		public void beginWorkSheet() throws IOException{
			_out.write("<?xml version=\"1.0\" encoding=\""+XML_ENCODING+"\"?>" +
			"<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">" );
		}	
		public void beginSheet() throws IOException {
			_out.write("<sheetData>\n");
		}
		public void endSheet() throws IOException {
			_out.write("</sheetData>");
		}
		public void endWorkSheet() throws IOException {
			_out.write("</worksheet>");
		}
		/**
		 * Insert a new row
		 *
		 * @param rownum 0-based row number
		 */
		public void insertRow(int rownum) throws IOException {
			_out.write("<row r=\""+(rownum+1)+"\">\n");
			this._rownum = rownum;
		}

		/**
		 * Insert row end marker
		 */
		public void endRow() throws IOException {
			_out.write("</row>\n");
		}
		public void createCell(int columnIndex, String value, int styleIndex) throws IOException {
			String ref = new CellReference(_rownum, columnIndex).formatAsString();
			_out.write("<c r=\""+ref+"\" t=\"inlineStr\"");
			if(styleIndex != -1) _out.write(" s=\""+styleIndex+"\"");
			_out.write(">");
			_out.write("<is><t>"+value+"</t></is>");
			_out.write("</c>");
		}
		public void createCell(int columnIndex, String value) throws IOException {
			createCell(columnIndex, value, -1);
		}
		public void createCell(int columnIndex, double value, int styleIndex) throws IOException {
			String ref = new CellReference(_rownum, columnIndex).formatAsString();
			_out.write("<c r=\""+ref+"\" t=\"n\"");
			if(styleIndex != -1) _out.write(" s=\""+styleIndex+"\"");
			_out.write(">");
			_out.write("<v>"+value+"</v>");
			_out.write("</c>");
		}
		public void createCell(int columnIndex, double value) throws IOException {
			createCell(columnIndex, value, -1);
		}
		public void createCell(int columnIndex, Calendar value, int styleIndex) throws IOException {
			createCell(columnIndex, DateUtil.getExcelDate(value, false), styleIndex);
		}
		public void mergeCell(List<Integer>  ind, int row, int col) throws IOException {
			/** row = 1; 
			 *  col = 0; 
			 *  **/
			String r = new CellReference(row , col ).formatAsString();
			String re = new CellReference(row+2 , col ).formatAsString();	
			_out.write("<mergeCells>");  
		    	_out.write("<mergeCell ref=\""+r+":"+re+"\"/>");	
		    	col++;
     	    	r = new CellReference(row , col ).formatAsString();
		    	re = new CellReference(row+2 , col ).formatAsString();	
		    	_out.write("<mergeCell ref=\""+r+":"+re+"\"/>");	
		    	col++;
		    	r = new CellReference(row , col ).formatAsString();
		    	re = new CellReference(row , col+5 ).formatAsString();
		    	_out.write("<mergeCell ref=\""+r+":"+re+"\"/>");	
		    	
		    	for (int i =0; i<6;i++){
		    		r = new CellReference(row+1 , col ).formatAsString();
			    	re = new CellReference(row+2 , col ).formatAsString();
			    	_out.write("<mergeCell ref=\""+r+":"+re+"\"/>");
			    	col++;
		    	    }  	
		    	
				for( int indic : ind){
		              String ref = new CellReference(row, indic ).formatAsString();
					  String ref1 = new CellReference(row+1, indic+3).formatAsString();	
						_out.write("<mergeCell ref=\""+ref+":"+ref1+"\"/>");
								} 
			_out.write("</mergeCells>");
		}

		public void mergeCell(int  ind) throws IOException {
			String ref = new CellReference(0, ind ).formatAsString();
			String ref1 = new CellReference(0, ind+3).formatAsString();
			_out.write("<mergeCells>");    
			_out.write("<mergeCell ref=\""+ref+":"+ref1+"\"/>");

			_out.write("</mergeCells>");
		}
		public void InicColsForm()throws IOException {
	    	_out.write("<cols>");    	        
	     }
		public void colsForm(int min, double width)throws IOException{
			_out.write("<col min= \""+min+"\" max=\""+min+"\" width=\""+width+"\" bestFit=\"1\" customWidth=\"1\"/>");
		}
		
		public void EndColsForm()throws IOException {
			_out.write("</cols>"); }    
	}
  /**
   * Metodo para versionar formatos de PS y PO 
   */
  public static void GenArchPOnoInclu() {
	  File f;
		   f = new File("D:/Comcel/EPC/Product Offering/Log_PO_No_InicluidosLOG_NO_INC.txt");	 
	
	try{    
		  System.out.println("pasa por aca ");		
			FileWriter w = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw);  
			wr.write("PlANES NO INCLUIDOS EN LA TABLA PLAN_PAQ_SERV:  ");
			System.out.println("pasa por aca 1 ");
			int cont = 0;//escribimos en el archivo
			int cont1 = 0;
			for(String po: noinclu){
				 wr.append("\n"+ po +" | "); //concatenamos en el archivo sin borrar lo existente
				 cont++;				 
			       }
			wr.append("\n" +"PAQUETES NO INCLUIDOS EN LA TABLA PLAN_PAQ_SERV(SPCODE):  ");
			
			for(String po: paqNoInclu){
				 wr.append("\n"+ po +" | "); //concatenamos en el archivo sin borrar lo existente
				 cont1++;				 
			       }
			
			
			JOptionPane.showMessageDialog(null, "Numero de planes no incluidos en tabla maestro: "+ cont +"\n Numero de paquetes no incluidos en tabla maestro: "+cont1);
			wr.close();
			bw.close();

	}catch(IOException e){
		e.printStackTrace();
	};    
	  
  }  
  /*
   * 
   METODO PRUEBA PARA ESTANDARIZACION DE PROCESO DE GENERACION DE ARCHIVO PO 
   * 
   * 
   * 
   * 
   *
  private static int escribirIncComp(SpreadsheetWriter sw,  Map<String, XSSFCellStyle> styles, int indRow, int indice, String segmento, Boolean postpago ) throws Exception{
		IDataModel model = new DataModel();			
		Collection <productOffering> PO = model.getProductOffering();
		List <Componetes> comp =  model.getCompTotal();	
		if(postpago){	   
			Boolean flagBloq = false;
			Boolean flagB = false; 
			Boolean flagBloqA = false;
		    Boolean flagBloqPadre = false; 
		    Boolean f1 = false;
		    Boolean f2 = false;
		    Boolean f3 = false;
			String sncode = ""; 
			int temp = indice;
			for(Componetes com : comp){	
				if(com.getSEGMENTO().trim().equals(segmento)){
				indRow++;
				sw.insertRow(indRow);
				for(int i= 0; i< indice;i++){
					if (i == 0) {
						if(com.getDESCRIPCION().trim().equals("**TEST ADD BO") ||com.getDESCRIPCION().trim().equals("**TEST PP BO")||com.getDESCRIPCION().trim().equals("Technical PP")){ sw.createCell(i, "Abstract Price", styles.get("styCel").getIndex());
						System.out.println("Descripcion: Test" +com.getDESCRIPCION().trim());
						}
						else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")){
							sw.createCell(i, "Activity Charge", styles.get("styCel").getIndex()); 
							System.out.println("Descripcion Down: " +com.getDESCRIPCION().trim());						
						}
						else if(com.getDESCRIPCION().trim().equals("Wireless")){
							sw.createCell(i, "Product Spec", styles.get("styCel").getIndex()); 	
							System.out.println("Descripcion Product: " +com.getDESCRIPCION().trim());
						      }							
						     else {
									sw.createCell(i, "Component", styles.get("styCel").getIndex()); 	
									System.out.println("Descripcion Comp: " +com.getDESCRIPCION().trim());
						}
					} 			        	     
					if (i == 1) sw.createCell(i, com.getDESCRIPCION(), styles.get("styCel").getIndex());	
					if (i == 2) {if(com.getPRODUCT_SPEC()==null)sw.createCell(i, "", styles.get("styCel").getIndex());	
								else sw.createCell(i, com.getPRODUCT_SPEC(), styles.get("styCel").getIndex());	
					}
					if (i == 3) {if(com.getMAIN_COMP()==null)sw.createCell(i, "", styles.get("styCel").getIndex());
								else sw.createCell(i, com.getMAIN_COMP(), styles.get("styCel").getIndex());	
								}
					if (i == 4){if(com.getCOMPONENT1()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
								else sw.createCell(i, com.getCOMPONENT1(), styles.get("styCel").getIndex());	
								}
					if (i == 5){if(com.getCOMPONENT2()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
								else sw.createCell(i, com.getCOMPONENT2(), styles.get("styCel").getIndex());
								}
					if (i == 6){if(com.getCOMPONENT3()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
					            else sw.createCell(i, com.getCOMPONENT3(), styles.get("styCel").getIndex());	
								}
					if (i == 7) sw.createCell(i, "", styles.get("styCel").getIndex());
				}
		///-------         Agregar para definir si los componentes padres tienen bloqueo  
	    ///-------                         Cambios agregados 27/03/2012
				List<POCodNum> BloqPad1 = null;	
				List<POCodNum> BloqPad2 = null;	
				List<POCodNum> BloqPad3 = null;	
				List<POCodNum> BloqHijo = null;
				if(!com.getBLOQUEO().trim().equals("0"))
				         BloqHijo = model.defExisCompPA(com.getBLOQUEO());
								 
				if(com.getPadres_comp()!=null){
						  List<POCodNum> padres = com.getPadres_comp();
				
				 int sncode1 = -1;
				 int sncode2 = -1;
				 int sncode3 = -1;
				 
						 for(int i = 0; i<padres.size();i++){
							 if(com.getPadres_comp().get(i).getSncode()!=0){
							     if(com.getPadres_comp().get(i).getTipo().equals("1") ){
									   if(sncode1 != com.getPadres_comp().get(i).getSncode()) 
									   	BloqPad1 = model.defExisCompPA(String.valueOf(com.getPadres_comp().get(i).getSncode()));
									   
									   	sncode1= com.getPadres_comp().get(i).getSncode();							   
								   }
								  if(com.getPadres_comp().get(i).getTipo().equals("2")){
								        if(sncode2 != com.getPadres_comp().get(i).getSncode()) 
								        	BloqPad2 = model.defExisCompPA(String.valueOf(com.getPadres_comp().get(i).getSncode()));	
										
								        sncode2= com.getPadres_comp().get(i).getSncode();
								    }
								  if(com.getPadres_comp().get(i).getTipo().equals("3")) { 
										if(sncode3 != com.getPadres_comp().get(i).getSncode()) 
											BloqPad3 = model.defExisCompPA(String.valueOf(com.getPadres_comp().get(i).getSncode()));	
										
										sncode3 = com.getPadres_comp().get(i).getSncode();
									}							
								  }												
							}										
								
				}						
				for( productOffering po : PO){
				
					if(com.getDESCRIPCION().trim().equals("**TEST PP BO"))
					{
						sw.createCell(temp,"Excluded(mandatory)", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
							
					}else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")
							||com.getDESCRIPCION().trim().equals("Change MSISDN")||com.getDESCRIPCION().trim().equals("Change Offer")
							||com.getDESCRIPCION().trim().equals("Replace Equipment")||com.getDESCRIPCION().trim().equals("Recall Service")){
							
							sw.createCell(temp,"not selected by default", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							}
				//   ------------   Nuevos requerimientos   -------------------           
					else if(com.getDESCRIPCION().trim().equals("Base Plan")||com.getDESCRIPCION().trim().equals("Wireless") ||com.getDESCRIPCION().trim().equals("Wireless Main")||com.getDESCRIPCION().trim().equals("Equipment &amp; SIM") ){
						sw.createCell(temp,"", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, 1, styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, 1, styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, 1, styles.get("styCel").getIndex());
						temp++;					
					          }
					else if(com.getDESCRIPCION().trim().equals("Web TV Package")){
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 999, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;	
								//}				
					          }	
					else if(com.getDESCRIPCION().trim().equals("Equipment Commitment")||com.getDESCRIPCION().trim().equals("Fulfillment &amp; Return")||com.getDESCRIPCION().trim().equals("Welcome Promotion")){						
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 1, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 1, styles.get("styCel").getIndex());
								temp++;	//	}					          
					}						
					else if(com.getDESCRIPCION().trim().equals("Technical PP"))
					{
						sw.createCell(temp,"mandatory", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
						sw.createCell(temp, "", styles.get("styCel").getIndex());
						temp++;
							
					}
					else if(com.getDESCRIPCION().trim().equals("Equipment")||com.getDESCRIPCION().trim().equals("SIM Card") ||com.getDESCRIPCION().trim().equals("Plan Commitment")||com.getDESCRIPCION().trim().equals("Never Lost Call")){
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;					
						
					}
					else if(com.getDESCRIPCION().trim().equals("Family Group")){
						if(po.getTMCODE().equals("0")){							
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;							
						}else if(po.getTIPO_PLAN_FAM()!=null){
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;						
						}else{
						
							sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;		}
					 }				
					
			    //  ---------------------------------------- Fin Nuevos requerimientos -----------------------------------------------/	     	
				else{
					//--------------------------------------------------------------------------------------------------------	
					 if(po.getTMCODE().equals("0") && !com.getDESCRIPCION().equals("Wireless")) {
						    sw.createCell(temp,"", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 1, styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, 0, styles.get("styCel").getIndex());
							temp++;					
					 }
					 else {
					//---------------------------------------------------------------------------------------------------------
				  						
					if(BloqPad1 !=null){
						
					for(POCodNum bloq: BloqPad1 ){
				//		System.out.println(bloq.getTmcode()+" bloqueo = "+ bloq.getSncode());
						 if (bloq.getTmcode() == Integer.parseInt(po.getTMCODE())){
							 f1 = true;
							 break;	
						      }					
					       }
						}
					if(BloqPad2!=null){
						for(POCodNum bloq1: BloqPad2 ){
					//		System.out.println(bloq1.getTmcode()+" bloqueo = "+ bloq1.getSncode());
							 if (bloq1.getTmcode() == Integer.parseInt(po.getTMCODE())){
								 f2 = true;
								 break;	
							      }					
						       }
							}
					if(BloqPad3!=null){
						for(POCodNum bloq2: BloqPad3 ){
							 if (bloq2.getTmcode() == Integer.parseInt(po.getTMCODE())){
								// System.out.println("pasa 3" );
								 f3 = true;
								 break;	
							      }					
						       }
							}
					if(f1 == true || f2 == true || f3==true){
						flagBloqPadre =  true;
					}
					if(BloqHijo !=null){	   
					for(POCodNum bloqH : BloqHijo){
						if(po.getTMCODE().trim().equals(String.valueOf(bloqH.getTmcode()))&& bloqH.getTipo().trim().equals("A")){
							flagBloqA = true;
						      }
					       }
					    }
					if(com.getSNCODE().trim().equals("1"))
							     { sncode = com.getOBSERVACION().trim();}
				    else {sncode = com.getSNCODE().trim();      }
					System.out.println("bloqueoPadre: " +flagBloqPadre + " bloqHijoA: " + flagBloqA );  
					
					   if(flagBloqPadre == true) {
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, 0, styles.get("styCel").getIndex());
								temp++;									
							   }						  
					    else{ 					    	
					    	
						    if(com.getBLOQUEO().trim().equals("0")){	        		  	   	
									flagB = model.defExisCompPO(po.getTMCODE().trim(), sncode);
									   if(flagB == true){
											
											sw.createCell(temp,"", styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											System.out.println("Pasa sin bloqueo...flagB = true "+ flagB +" sncode: "+ sncode  );
										       }	
										else   {																
											sw.createCell(temp,"", styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;  		  	   		
										  
										        } 										
								//--------------------------Termina Codigo que se agrego y se comenta el siguiente -----------------------------------		
								     		
								} 												
							else{
									flagB = model.defExisCompPO(po.getTMCODE().trim(),  sncode);
									sncode = com.getBLOQUEO().trim();
									flagBloq = model.defExisCompPO(po.getTMCODE().trim(),  sncode); 
								   	if(flagBloq == true && flagB == false) {
										sw.createCell(temp, "", styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;										
									}
									if(flagBloq == false){
										if(flagB == true) {
											sw.createCell(temp, "", styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 0, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											sw.createCell(temp, 1, styles.get("styCel").getIndex());
											temp++;
											 }
										else {												
												sw.createCell(temp, "", styles.get("styCel").getIndex());
												temp++;
												sw.createCell(temp, 0, styles.get("styCel").getIndex());
												temp++;
												sw.createCell(temp, 1, styles.get("styCel").getIndex());
												temp++;
												sw.createCell(temp, 0, styles.get("styCel").getIndex());
												temp++;}												
									}
									if(flagBloq == true && flagB == true) {
										System.out.println("Pasa por el valor inusual,,,, no debe pasar por aca");
										sw.createCell(temp, "", styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 1, styles.get("styCel").getIndex());
										temp++;
										sw.createCell(temp, 0, styles.get("styCel").getIndex());
										temp++;																		}
									 }  
						  		}// ******fin del else ************						         
				      }
				   }// Fin del else para llenar cardinalidad de cada una delos componentes con PO 
					flagBloqPadre = false;
					flagBloqA = false; f1 = false; f2=false; f3=false;
				
				}
				sw.endRow();
				temp = indice;
				
				}		
		     }    	
		}
		else{		
			int temp = indice;
			for(Componetes com : comp){	
				System.out.println("segmento:  "+com.getSEGMENTO() + " solicitado: "+ segmento);
				if(com.getSEGMENTO().trim().equals(segmento)){					
					indRow++;
					sw.insertRow(indRow);
					for(int i= 0; i< indice;i++){
						if (i == 0) {

							if(com.getDESCRIPCION().trim().equals("**TEST ADD BO") ||com.getDESCRIPCION().trim().equals("**TEST PP BO")||com.getDESCRIPCION().trim().equals("Technical PP")){ sw.createCell(i, "Abstract Price", styles.get("styCel").getIndex());
							System.out.println("Descripcion: Test" +com.getDESCRIPCION().trim());
							}
							else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")||com.getDESCRIPCION().trim().equals("Change MSISDN")||com.getDESCRIPCION().trim().equals("Change Offer")
									||com.getDESCRIPCION().trim().equals("Replace Equipment")||com.getDESCRIPCION().trim().equals("Recall Service")){
								sw.createCell(i, "Activity Charge", styles.get("styCel").getIndex()); 
								System.out.println("Descripcion Down: " +com.getDESCRIPCION().trim());						
							}
							else if(com.getDESCRIPCION().trim().equals("Wireless")){
								sw.createCell(i, "Product Spec", styles.get("styCel").getIndex()); 	
								System.out.println("Descripcion Product: " +com.getDESCRIPCION().trim());
							      }							
							     else {
										sw.createCell(i, "Component", styles.get("styCel").getIndex()); 	
										System.out.println("Descripcion Comp: " +com.getDESCRIPCION().trim());
							}
						} 			        	     
						if (i == 1) sw.createCell(i, com.getDESCRIPCION(), styles.get("styCel").getIndex());	
						if (i == 2) {if(com.getPRODUCT_SPEC()==null)sw.createCell(i, "", styles.get("styCel").getIndex());	
									else sw.createCell(i, com.getPRODUCT_SPEC(), styles.get("styCel").getIndex());	
						}
						if (i == 3) {if(com.getMAIN_COMP()==null)sw.createCell(i, "", styles.get("styCel").getIndex());
									else sw.createCell(i, com.getMAIN_COMP(), styles.get("styCel").getIndex());	
									}
						if (i == 4){if(com.getCOMPONENT1()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
									else sw.createCell(i, com.getCOMPONENT1(), styles.get("styCel").getIndex());	
									}
						if (i == 5){if(com.getCOMPONENT2()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
									else sw.createCell(i, com.getCOMPONENT2(), styles.get("styCel").getIndex());
									}
						if (i == 6){if(com.getCOMPONENT3()==null) sw.createCell(i, "", styles.get("styCel").getIndex());
						            else sw.createCell(i, com.getCOMPONENT3(), styles.get("styCel").getIndex());	
									}
						if (i == 7) sw.createCell(i, "", styles.get("styCel").getIndex());		
			
					}					
					for( productOffering po : PO){	
						
						if(com.getDESCRIPCION().trim().equals("Technical PP"))
						{
								sw.createCell(temp,"mandatory", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								
						}else if((po.getDESC_BO().contains("KIT")||po.getDESC_BO().contains("Kit")||po.getDESC_BO().contains("Especial Nacional")) &&com.getDESCRIPCION().trim().equals("Equipment")){
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	
							  ///com.getDESCRIPCION().trim().equals("Equipment") ||
						}else if(po.getDESC_BO().contains("Tactico")&&(com.getDESCRIPCION().trim().equals("Fulfillment &amp; Return") 
								||com.getDESCRIPCION().trim().equals("Temp Equipment")||com.getDESCRIPCION().trim().equals("Warranty"))){
								sw.createCell(temp,"", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
						
						}else if(com.getDESCRIPCION().trim().equals("Downgrade Plan Charge")|| com.getDESCRIPCION().trim().equals("Equipment Early Termination Charge")
								||(com.getDESCRIPCION().trim().equals("Replace Equipment")&&po.getDESC_BO().trim().contains("Tactico"))){
								sw.createCell(temp,"excluded", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
						}else if(com.getDESCRIPCION().trim().equals("Change MSISDN")||com.getDESCRIPCION().trim().equals("Change Offer")
								||(com.getDESCRIPCION().trim().equals("Replace Equipment")&&!po.getDESC_BO().trim().contains("Tactico"))||com.getDESCRIPCION().trim().equals("Recall Service")){
								sw.createCell(temp,"not selected by default", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
						}else if(!po.getELEGIDOS_BFNF().equals("0")&&!po.getELEGIDOS_FNF().equals("0")){											 
							
						if(com.getDESCRIPCION().equals("BFNF In Plan")||com.getDESCRIPCION().equals("Best Friends and Family")||com.getDESCRIPCION().equals("BFNF Voice"))
								{sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						else if (com.getDESCRIPCION().equals("BFNF Voice All Destination")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;								
						}else if(com.getDESCRIPCION().contains("BFNF")&&com.getDESCRIPCION().contains("Additional")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}
					  						
						else if(com.getDESCRIPCION().equals("FNF Voice")||com.getDESCRIPCION().equals("Friends &amp; Family")||com.getDESCRIPCION().equals("FNF Voice All Destination")||com.getDESCRIPCION().equals("In Plan")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
					//	else if (com.getDESCRIPCION().equals("FNF Voice All Destination")){
					//			sw.createCell(temp, "", styles.get("styCel").getIndex());
					//			temp++;
					//			sw.createCell(temp, "0", styles.get("styCel").getIndex());
					//			temp++;
					//			sw.createCell(temp, "1", styles.get("styCel").getIndex());
					//			temp++;
					//			sw.createCell(temp, "1", styles.get("styCel").getIndex());
					//			temp++;	}
						else if(com.getDESCRIPCION().equals("FNF Voice All Destination Additional")||com.getDESCRIPCION().equals("FNF Voice Additional")||com.getDESCRIPCION().equals("Additional")){
					    		sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}					 						
						else  { 
					    	   sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
							  temp++;}			    
					  
					}else if(po.getELEGIDOS_BFNF().equals("0")&&!po.getELEGIDOS_FNF().equals("0")){
												
						if(com.getDESCRIPCION().equals("FNF Voice")||com.getDESCRIPCION().equals("Friends &amp; Family")||com.getDESCRIPCION().equals("FNF Voice All Destination")||com.getDESCRIPCION().equals("In Plan")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						//else if (com.getDESCRIPCION().equals("FNF Voice All Destination")){
						//		sw.createCell(temp, "", styles.get("styCel").getIndex());
						//		temp++;
						//		sw.createCell(temp, "0", styles.get("styCel").getIndex());
						//		temp++;
						//		sw.createCell(temp, "1", styles.get("styCel").getIndex());
						//		temp++;
						//		sw.createCell(temp, "1", styles.get("styCel").getIndex());
						//		temp++;	}
						else if(com.getDESCRIPCION().equals("FNF Voice All Destination Additional")||com.getDESCRIPCION().equals("FNF Voice Additional")||com.getDESCRIPCION().equals("Additional")){
					    		sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}
						else{ 
						    	sw.createCell(temp, "", styles.get("styCel").getIndex());
							    temp++;
								sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
								temp++;	}				
				
					}else if(!po.getELEGIDOS_BFNF().equals("0")&&po.getELEGIDOS_FNF().equals("0")){
						
						if(com.getDESCRIPCION().equals("BFNF In Plan")||com.getDESCRIPCION().equals("Best Friends and Family")||com.getDESCRIPCION().equals("BFNF Voice"))
								{sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						else if (com.getDESCRIPCION().equals("BFNF Voice All Destination")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "1", styles.get("styCel").getIndex());
								temp++;	}
						else if(com.getDESCRIPCION().contains("BFNF")&&com.getDESCRIPCION().contains("Additional")){
								sw.createCell(temp, "", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, "0", styles.get("styCel").getIndex());
								temp++;	}
						else { 
						    	sw.createCell(temp, "", styles.get("styCel").getIndex());
							    temp++;
								sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
								temp++;
								sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
								temp++;	}				
										
					}else{				
							//System.out.println("Pasa por valores de establecidos  ya como cardinalidad fija");
							sw.createCell(temp, "", styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, com.getPREP_MIN(), styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, com.getPREP_MAX(), styles.get("styCel").getIndex());
							temp++;
							sw.createCell(temp, com.getPREP_DEF(), styles.get("styCel").getIndex());
							temp++;	
							}					
					}
					sw.endRow();
					temp = indice;			
			    }				
			}			
		}
		System.out.println(" termina Ciclo y Retorna: "+ indRow);
		return indRow;
	}
    
*/
  
  
  

}
