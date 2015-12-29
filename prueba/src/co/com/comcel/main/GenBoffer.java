package co.com.comcel.main;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;


import co.com.comcel.constants.IConstants;
import co.com.comcel.model.DataModel;
import co.com.comcel.model.IDataModel;
import co.com.comcel.vo.AttrPaqLDI;
import co.com.comcel.vo.BillingOffer;
import co.com.comcel.vo.CompararTTS_BO;
import co.com.comcel.vo.Componentes;
import co.com.comcel.vo.MaterialSim;
import co.com.comcel.vo.NewOffersAmdocs;
import co.com.comcel.vo.OfertasDemanda;
import co.com.comcel.vo.OneTimeCharge;
import co.com.comcel.vo.Paquete;
import co.com.comcel.vo.Plan;
import co.com.comcel.vo.Price;
import co.com.comcel.vo.Prit;
import co.com.comcel.vo.TOTALES_SEG_CAL;
import co.com.comcel.vo.TablaTraduccion;
import co.com.comcel.vo.Value;
import co.com.comcel.vo.BORevision;
import co.com.comcel.vo.ValuesPrepago;
import co.com.comcel.vo.servicio_comp;

public class GenBoffer{
	static Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
	static Hashtable<String, Integer> htc = new Hashtable<String, Integer>();
	static Hashtable<String, Integer> hto = new Hashtable<String, Integer>();
	static Hashtable<String, Integer> htp = new Hashtable<String, Integer>();
	/**
	 * Método principal. Inicio de ejecución
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		System.out.println("**INICIANDO**" + new Date());		
		IDataModel model = new DataModel();
		
		/**
		 * Metodo para operativizar la descripcion de los planes en BD y cargarla a diferentes campos 
		 * de descripcion en el archivo de excel para cargue de adtos y planes a migrar.
		 * 
		 */
		/*   	   String archivo = "C:/Users/Admin/Desktop/PLANES-PREPAGO-MAYO-19-2014_R.xlsx";
	     String strSheet ="PLANES";
	     model.segmentacioDesc(archivo, strSheet);		
		/**
		 * Mapea el nombre de las  Columnas con el indice  de la columna. 
		 */			
	    HashMap<String, Integer> mapBO = obtnerNumCols("BillingOffer");
		HashMap<String, Integer> mapPrice = obtnerNumCols("Price");
		HashMap<String, Integer> mapValues = obtnerNumColsValues();
				
		int filaActualBO = 6;
		int filaActualPrice = 8;
		int filaActualValues = 1;
		int filaNewOffers = 1;
		//  Realiza truncamiento de la tabla: BOFFER y BOFFER_PANAMA 		
     	model.truncateBO();
	    model.truncateBOPanama();
		int contLink=2;
		boolean panama = false;
		boolean prepago = false;
		boolean prepincr = false;
		int fila;
		
		//*************************************************COMCEL*************************************************
		//Se generan todos los prices genéricos y se asignan a una billing offer para luego ser pintados en el excel.
		List<BillingOffer> boPrePalist = new ArrayList<BillingOffer>();
			String tipo = JOptionPane.showInputDialog("Generar BILLING OFFER : "
				+ "\n  Digite I para : PREPAGO INCREMENTAL"
				+ "\n  Digite F para : PREPAGO FASE: "
				+ "\n  Digite P para : POSTPAGO");
		
		if(tipo.equalsIgnoreCase("I"))	 prepincr = true;
		else prepincr = false;
		List<BillingOffer> boPrepEspeciales = model.generarBOOfertasPrepago();			
		List<BillingOffer> boGenList = new ArrayList<BillingOffer>();
		BillingOffer bo = new BillingOffer();
		List<Price> priceGenList = model.generarPriceGenerico(contLink,bo,panama);
		bo.getPriceList().addAll(priceGenList);
		bo.setEffectiveDate("01/01/2011");
		bo.setSaleEffectiveDate("31/03/2014");		
		fila = filaActualPrice;
		bo.setBillOfferGenerics(1);
		boGenList.add(bo);
		contLink=bo.getContLink(); 		
		if(tipo.equalsIgnoreCase("I")||tipo.equalsIgnoreCase("F")){
			filaActualBO = 4;
			filaActualPrice = 6;
			filaActualValues = 1;
			if(tipo.equalsIgnoreCase("I"))boPrePalist = model.generarBOPlanPrepago(true);
			else boPrePalist = model.generarBOPlanPrepago(false);			
			int filactual = escribirBO(boPrePalist,filaActualBO,mapBO,panama, false, prepago, true);	
			filaActualBO = escribirBO(boPrepEspeciales,filactual,mapBO,panama, true, prepago, false);			        
			fila = escribirPrices(boPrepEspeciales,filaActualPrice,mapPrice,panama);
			model.insertBO(boPrePalist);
			insertBOManual(model, prepago, boPrepEspeciales);
			List<ValuesPrepago> vp = model.getValuesPrepago();
		    int filaValores = escribirValoresDemanda(boPrepEspeciales,filaActualValues,panama, mapValues, prepincr, true);
			//escribirValoresPrepago(vp,filaValores, panama, mapValues, prepincr );		
			List<TablaTraduccion> ttList = model.getInfoTTPre(panama);
			List<TablaTraduccion> ttPO  = model.getRefTTPre();
		  	List<servicio_comp>   infComp = model.getInfoComp();
			escribirTT(boPrePalist, ttList, ttPO, panama, model, infComp, prepincr);
			//GenArchTTS_BO_TOTAL(model);		
			List<NewOffersAmdocs>  NOA = model.getNewOffersAmdocs();
			List<MaterialSim>      ms  = model.getMaterialSim();			
			escribirNewOffers(model, NOA, filaNewOffers, prepago, ms);
			
		}else{			
    	/**Se generan las Billing Offers correspondientes a planes de migración.**/
		Collection<Plan> planes = model.getPlanesMigracion(panama);
		List<BillingOffer> boList = new ArrayList<BillingOffer>();
		for(Plan plan:planes){			
			/**contlink ya no hace nada. Se omite**/
			bo = model.generarBO(plan,0,contLink,panama, 1);
			contLink=bo.getContLink();
			
			boList.add(bo);
			// Ojo aca para definir las diferentes promociones con lo de error1 y error2 como lo de revision manula para intercalacion
			if(!plan.getPROMOCION().equals("N/A")&&!plan.getPROMOCION().equals("ERROR: 3")&&!plan.getPROMOCION().equals("ERROR: 2")){
				bo = model.generarBO(plan,1,contLink,panama, 0);
				contLink=bo.getContLink();
				boList.add(bo);
			}
			if(!plan.getDES12X12().equals("N")){
				bo = model.generarBOPromo12x12(plan, panama);
				contLink=bo.getContLink();
				boList.add(bo);
			}
		}			
	    List<BillingOffer> boPaqListDemanda = model.generarBOOfertasDemanda();
		filaActualBO = escribirBO(boPaqListDemanda,filaActualBO,mapBO,panama, true, prepago, true );
		//List<BillingOffer> boPaqListEspecial = model.generarBOPaquetesEspecial();
		//filaActualBO = escribirBO(boPaqListEspecial,filaActualBO,mapBO,panama, true);
		
		filaActualBO = escribirBO(boList,filaActualBO,mapBO,panama, false, prepago, false);		
		List<BillingOffer> boPaqList = model.generarBOPaquetes(panama);
		filaActualBO = escribirBO(boPaqList,filaActualBO,mapBO,panama, false, prepago, false);
//	----	List<BillingOffer> boPaqLDI = model.generarBOPaquetesLDI(); //GENERA BO DE LDI
//	----	filaActualBO = escribirBO(boPaqLDI,filaActualBO,mapBO,panama, false); //PINTA INFORMACION DE LDI
		
		fila = escribirPrices(boPaqListDemanda, filaActualPrice,mapPrice,panama);
		//fila = escribirPrices(boPaqListEspecial, fila,mapPrice,panama);
		fila = escribirPrices(boGenList, fila, mapPrice,panama);
		fila = escribirPrices(boList,fila,mapPrice,panama);
		fila = escribirPrices(boPaqList,fila,mapPrice,panama);
//	---	fila = escribirPrices(boPaqLDI, fila,mapPrice,panama); //PRICES DE LDI

		//Se insertan las BO generadas en la tabla BOFFER
		model.insertBO(boList);
		model.insertBOPaq(boPaqList);
//	---	model.insertBOPaqLDI(boPaqLDI); //LDI	
		insertBOManual(model, false, boPaqListDemanda);		
		int filaValores = escribirValores(boPaqListDemanda, filaActualValues,panama, mapValues, true);
		filaValores = escribirValoresDemanda(boPaqListDemanda, filaValores,panama, mapValues, prepincr, false);
		filaValores = escribirValores(boGenList, filaValores,panama, mapValues, true);
		filaValores = escribirValores(boList, filaValores,panama,mapValues, false);		
	//---	escribirValoresLDI(boPaqLDI, filaValores,panama,mapValues); //LDI
		 //Informacion TTs
	    List<TablaTraduccion> ttList = model.getInfoTT(panama);
	    List<TablaTraduccion> ttPO  = model.getRefTT();
		List<servicio_comp>   infComp = model.getInfoComp();
		escribirTT(boList, ttList, ttPO, panama, model, infComp, prepincr);
			
		GeneraTablaRev(boList, "PL",model);
		GeneraTablaRev(boPaqList,"PQ",model);
    	//GenArchTTS_BO_TOTAL(model);
		}		
		//*************************************************COMCEL*************************************************

		//*************************************************PANAMA*************************************************
		/*panama=true;
		filaActualBO = 4;
		filaActualPrice = 18;

		boGenList = new ArrayList<BillingOffer>();
		bo = new BillingOffer();
		priceGenList = model.generarPriceGenerico(contLink,bo,panama);
		bo.getPriceList().addAll(priceGenList);
		bo.setBillOfferGenerics(1);
		bo.setSaleEffectiveDate("2007/01/01");
		boGenList.add(bo);
		contLink=bo.getContLink();

		planes = model.getPlanesMigracion(panama);
		boList = new ArrayList<BillingOffer>();
		for(Plan plan:planes){
			bo = model.generarBO(plan,0,contLink,panama);
			if(!plan.getPROMOCION().equals("N/A")){
				bo.setTmcode(bo.getPlan().getTMCODE_EQ());
			}else{
				bo.setTmcode(bo.getPlan().getTMCODE());
			}
			contLink=bo.getContLink();
			boList.add(bo);
			if(!plan.getPROMOCION().equals("N/A")){
				bo = model.generarBO(plan,1,contLink,panama);
				bo.setTmcode(bo.getPlan().getTMCODE());
				contLink=bo.getContLink();
				boList.add(bo);
			}
		}
		filaActualBO = escribirBO(boList,filaActualBO,mapBO,panama);
		boPaqList = model.generarBOPaquetes(panama);
		escribirBO(boPaqList,filaActualBO,mapBO,panama);
		fila = escribirPrices(boGenList, filaActualPrice, mapPrice,panama);
		fila = escribirPrices(boList,fila,mapPrice,panama);
		escribirPrices(boPaqList,fila,mapPrice,panama);
		model.insertBOPanama(boList);
		model.insertBOPaqPanama(boPaqList);
		//escribirTMCODEvsBO(boList);
		//escribirSPCODEvsBO(boPaqList);
		filaValores = escribirValores(boGenList, 1,panama);
		escribirValores(boList, filaValores,panama);

		ttList = model.getInfoTT(panama);
		escribirTT(boList, ttList, panama);
		//*************************************************PANAMA*************************************************/
		System.out.println("**FINALIZADO**" + new Date());
	}
	
	private static void insertBOManual(IDataModel model, boolean soloPrep, List<BillingOffer> bo){		
			
		List<BillingOffer> boListPaquete = new ArrayList<BillingOffer>();
		BillingOffer boSop = new BillingOffer();
		Paquete pqSop = new Paquete();
		for(BillingOffer offer: bo){
		if(!offer.isDemanda()){
			boSop =  new BillingOffer();
			boSop.setATRMAP_TOPO("");
			boSop.setSaleEffectiveDate("");
			boSop.setName(offer.getName());
			pqSop = new Paquete();
		    pqSop.setSPCODE(offer.getPaquete().getSPCODE());
			pqSop.setTIPO_ALLOWANCE(offer.getPaquete().getTIPO_ALLOWANCE());	
			pqSop.setALLOWANCE("0");
			pqSop.setSNCODE(0L);			
			boSop.setPaquete(pqSop);
			boListPaquete.add(boSop);}
		}					
		model.insertBOPaq(boListPaquete);
		model.updateBO_numEleg();
		model.updateBO_spCode();
		
	}
	/**
	 * Método que escribe las Billing Offers en el archivo excel de plantilla D:\\AMX EPC Billing Offers Version 9.xlsx
	 * @param boList: Billing Offers a escribir
	 * @param fila: Fila actual de escritura
	 * @param mapBO: Mapa de ubicación de columnas en el archivo
	 * @param panama: Bandera para indicar si la lista corresponde a BillingOffers de panama o colombia
	 * @return numero de fila en que queda la escritura
	 * @throws Exception
	 */
	public static int escribirBO(List<BillingOffer> boList, int fila, HashMap<String, Integer> mapBO, boolean panama, boolean isPqSpecial, boolean prepago, boolean primera) throws Exception{
		InputStream inp=null;
		if(!panama){												
			if(primera)	inp = new FileInputStream("D://EntradasEPC/AMX EPC Billing Offers Version 9.xlsx");
			else        inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			if(primera)	inp = new FileInputStream("D://EntradasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
			else        inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
		}
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("BillingOffer");
		for(BillingOffer bo:boList){	
			System.out.println("Nombre : " +bo.getName().toString().trim());
			if(bo.isDemanda()){
					for(int i = 0; i< bo.getPriceList().size(); i++) {
							System.out.println("Nombre Price: "+bo.getPriceList().get(i).getName());
					}}
			/**
			 * La de suspensión se esta haciendo manual, solo se guarda en BO, para generar la PO.
			 */
			if (bo.isDemanda()== false &&(
			  bo.getName().toString().trim().contains("Suspension")
			  ||bo.getName().toString().trim().contains("Numero VIP")
			 )){
				continue;
				}
			
			Row row = sheet.getRow(fila); 
			if(row==null)
				row = sheet.createRow(fila);
			Cell cell = row.getCell(mapBO.get("Code")); 
			if (cell == null)   
				cell = row.createCell(mapBO.get("Code"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getCode().replace(" ", "_"));
			
			cell = row.getCell(mapBO.get("BillingOfferIndication")); 
			if (cell == null)   
				cell = row.createCell(mapBO.get("BillingOfferIndication"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue("false");

			cell = row.getCell(mapBO.get("Level"));
			if (cell == null)   
				cell = row.createCell(mapBO.get("Level"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getLevel());

			cell = row.getCell(mapBO.get("ServiceType"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("ServiceType"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getType());
			Calendar calendar;
			SimpleDateFormat sdf;
			Date date;
			DataFormat fmt;
			CellStyle style;
			cell = row.getCell(mapBO.get("SaleEffectiveDate"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("SaleEffectiveDate")); 
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if(bo.getSaleEffectiveDate()!= null){
			calendar = Calendar.getInstance();
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			date = sdf.parse(bo.getSaleEffectiveDate());
			calendar.setTime(date);
			fmt = wb.createDataFormat();
			style = wb.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_RIGHT);
			style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss"));
			cell.setCellStyle(style);
			cell.setCellValue(calendar);}
			
			//-----------------------------------------------------------
			if(bo.getSaleExpirationdate()!=null){
					cell = row.getCell(mapBO.get("SaleExpirationDate"));   
					if (cell == null)   
						cell = row.createCell(mapBO.get("SaleExpirationDate")); 
					cell.setCellType(Cell.CELL_TYPE_STRING);
					if(bo.getSaleExpirationdate()!=null){
					 calendar = Calendar.getInstance();
					//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
					date = sdf1.parse(bo.getSaleExpirationdate());
					calendar.setTime(date);
					fmt = wb.createDataFormat();
					style = wb.createCellStyle();
					style.setAlignment(CellStyle.ALIGN_RIGHT);
					style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss"));
					cell.setCellStyle(style);
					cell.setCellValue(calendar);}		
			
			}			
			//----------------------------------------------------------
			cell = row.getCell(mapBO.get("Duration;Quantity=\"unit\"ToFill"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("Duration;Quantity=\"unit\"ToFill"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getDurationA());

			cell = row.getCell(mapBO.get("CurrencyToFill"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("CurrencyToFill"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getCurrency());

			cell = row.getCell(mapBO.get("OrderingType"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("OrderingType"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			if(bo.getPlan()!=null){
				cell.setCellValue("BS");
			}else{ if(!prepago) cell.setCellValue("AD");
				   else         cell.setCellValue("BS");
			}
			
			cell = row.getCell(mapBO.get("billMarketingDescription;Locale=\"en\""));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("billMarketingDescription;Locale=\"en\""));   
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(bo.getDescripcion());
			
			cell = row.getCell(mapBO.get("billMarketingDescription;Locale=\"es\""));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("billMarketingDescription;Locale=\"es\""));   
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(bo.getDescripcion());			              

			if(bo.getDurationB()!=null){
				cell = row.getCell(mapBO.get("Duration;Quantity=\"value\"ToFill"));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("Duration;Quantity=\"value\"ToFill"));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(bo.getDurationB());
			}

			cell = row.getCell(mapBO.get("DeployIndicator"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("DeployIndicator"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getDeployIndicator());

			cell = row.getCell(mapBO.get("billMarketingDescription"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("billMarketingDescription"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getDescripcion());

			cell = row.getCell(mapBO.get("ProductType"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("ProductType"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getProductType());


			//*****Dynamic Properties
			cell = row.getCell(mapBO.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);
			
			if(bo.getPlan()!=null){
					      if(bo.getPlan().getTIPOPLAN()!=null) {					    	  
					    	  		if(  bo.getPlan().getTIPOPLAN().equals("Prepago")) cell.setCellValue("Billing Offer Properties Comcel Colombia Prepaid");
					    	  		else	cell.setCellValue("Billing Offer Properties Comcel Colombia");					      
					      }else if(bo.getTipo().equals("Prepago"))cell.setCellValue("Billing Offer Properties Comcel Colombia Prepaid");
				          else cell.setCellValue("Billing Offer Properties Comcel Colombia");
			}else if(bo.getPaquete()!=null){
				    if(bo.getTipo()!=null){
				          if(bo.getTipo().equals("Prepago"))  cell.setCellValue("Billing Offer Properties Comcel Colombia Prepaid");
				          else if(bo.getTipo().equals("Postpago"))cell.setCellValue("Billing Offer Properties Comcel Colombia");
				          else cell.setCellValue("Billing Offer Properties Comcel Colombia");
				    			}
		   }else  cell.setCellValue("Billing Offer Properties Comcel Colombia");
			
			if(bo.getPlan()!=null&&bo.getPlan().getDPI_INICIAL()!=null){
				cell = row.getCell(mapBO.get("DPI_ACT"));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("DPI_ACT"));  
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(!bo.getPlan().getDPI_INICIAL().equals("0")?bo.getPlan().getDPI_INICIAL():"");
			}

			if(bo.getPlan()!=null&&bo.getPlan().getDPI_CONTROL()!=null){
				cell = row.getCell(mapBO.get("DPI_Control"));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("DPI_Control"));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(!bo.getPlan().getDPI_CONTROL().equals("0")?bo.getPlan().getDPI_CONTROL():"");
			}

			cell = row.getCell(mapBO.get("RC_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("RC_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getRC_ACT():"");
			if(bo.getPaquete()!=null) cell.setCellValue("0");

			cell = row.getCell(mapBO.get("RC_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("RC_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			if(bo.getPlan()!=null){
						cell.setCellValue(bo.getPlan().getRC_CONTROL()!=null?bo.getPlan().getRC_CONTROL():"");}
			if(bo.getPaquete()!=null){						
				cell.setCellValue("0");
			}			
			cell = row.getCell(mapBO.get("Blackberry Service Code"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("Blackberry Service Code"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if(bo.getPlan()!=null&&bo.getPlan().getNOMBRE_APROV()!=null){
				cell.setCellValue(!bo.getPlan().getNOMBRE_APROV().equals("N/A")?bo.getPlan().getNOMBRE_APROV():"");
			}else if(bo.getPaquete()!=null){
				cell.setCellValue(!bo.getPaquete().getNOMBRE_APROV().equals("N/A")?bo.getPaquete().getNOMBRE_APROV():"");
			}else{
				cell.setCellValue("");
			}

	//		if (!isPqSpecial){
					cell = row.getCell(mapBO.get("Required Or Restrict Internet Service"));   
					if (cell == null)
						cell = row.createCell(mapBO.get("Required Or Restrict Internet Service"));   
					cell.setCellType(Cell.CELL_TYPE_STRING);  
					if(bo.getPlan()!=null&&bo.getPlan().getTIPO_BLACKBERRY()!=null){
						 if(!bo.getPlan().getTIPO_BLACKBERRY().equals("N/A"))
						         cell.setCellValue("Required");
						 else    cell.setCellValue("Restrict");
					}else if(bo.getPaquete()!=null){
						cell.setCellValue((bo.getPaquete().getCODIGO_BB()!=null&&bo.getPaquete().getCODIGO_GPRS()!=null)?"Required":"Restrict");
					}else{
						cell.setCellValue("Restrict");
					}
	//		}

	//		if (!isPqSpecial){
				cell = row.getCell(mapBO.get("Required Or Restrict WAP Service"));   
				if (cell == null)
					cell = row.createCell(mapBO.get("Required Or Restrict WAP Service"));   
				cell.setCellType(Cell.CELL_TYPE_STRING);  
				cell.setCellValue("Required");
			/*
			 * Se define servicio de WAP solo para paquetes y planes de Balckberry 
			 */
				if(bo.getPlan()!=null){
					if(bo.getPlan().getCODIGO_BB()!=null)
					                    cell.setCellValue("Required");
					else  cell.setCellValue("Restrict");
				}else if(bo.getPaquete()!=null &&bo.getPaquete().getTIPO_ALLOWANCE()!=null){
					if(bo.getPaquete().getTIPO_ALLOWANCE().equals("Blackberry"))
					                    cell.setCellValue("Required");
					else cell.setCellValue("Restrict");
				}			
	//		}
			//-------------------------------------cambio 26/11/2012--------------------------------
			cell = row.getCell(mapBO.get("Business_Type"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("Business_Type"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			if(bo.getPlan()!=null){
			if(bo.getPlan().getCARACT_PLAN()!=null) {
				if(!bo.getPlan().getCARACT_PLAN().equals(IConstants.BOLSA_PARALELA_LDI)||bo.getPlan().getTIPOPLAN().equalsIgnoreCase(IConstants.PREPAGO))					
				cell.setCellValue("P");
				else cell.setCellValue("PL");       }
			}else if(bo.getPaquete()!=null){
				cell.setCellValue("ADP");			
			}else if(bo.getPaqueteLDI()!=null){
				cell.setCellValue("AL");
			}else cell.setCellValue("");			
			//-------------------------------------cambio 26/11/2012--------------------------------
		/*	cell = row.getCell(mapBO.get("Additional Data Package"));   
			if (cell == null)
				cell = row.createCell(mapBO.get("Additional Data Package"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getDATA_ADD_PACK():"");*/

			cell = row.getCell(mapBO.get("Promotion Category for plan penalty"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("Promotion Category for plan penalty"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   

			if(bo.getName().contains(" - PromoOnnet")){
				if(bo.getPlan().getMESES_PROMO().equals(4) && bo.getPlan().getPROMO_INTERCALACION().equals(1)){					
					cell.setCellValue("Promotion6Month");
				}else if(bo.getPlan().getMESES_PROMO().equals(4) && bo.getPlan().getPROMO_INTERCALACION().equals(0)){
					cell.setCellValue("Promotion3Month");
				}else if(bo.getPlan().getMESES_PROMO().equals(7) && bo.getPlan().getPROMO_INTERCALACION().equals(1)){
					cell.setCellValue("Promotion12Month");
				}else if(bo.getPlan().getMESES_PROMO().equals(7) && bo.getPlan().getPROMO_INTERCALACION().equals(0)){
					cell.setCellValue("Promotion6Month");
				}else if(bo.getPlan().getMESES_PROMO().equals(13) && bo.getPlan().getPROMO_INTERCALACION().equals(0)){
					cell.setCellValue("Promotion12Month");
				}
			}else if(bo.getName().contains(" - Promo12x12")){
				cell.setCellValue("Promotion12Month");
			}
			cell = row.getCell(mapBO.get("WEB_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("WEB_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getWEB_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQOS_INICIAL().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				  cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue(""); }

			cell = row.getCell(mapBO.get("WEB_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("WEB_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getWEB_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");				
			}
			cell = row.getCell(mapBO.get("Welcome Eligible Equipment"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("Welcome Eligible Equipment"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			if(bo.getPaquete()!=null){
				if(bo.getPaquete().getTIPO_ALLOWANCE()!=null){
					if(bo.getPaquete().getTIPO_ALLOWANCE().equals("BienRepo"))
					         cell.setCellValue("ALL");    
				    else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("BienRepoAVIOS"))
							cell.setCellValue("AVIOSN50");
				    }else cell.setCellValue("");
			}
			cell = row.getCell(mapBO.get("WAP_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("WAP_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getWAP_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("WAP_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("WAP_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getWAP_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}
    //------------------------------------------ 
			cell = row.getCell(mapBO.get("BFNF Voice duration till renew"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("BFNF Voice duration till renew"));   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			 if(bo.getPaquete()!=null&&bo.getBFNF_Vdur_till_renew()!=null){
					cell.setCellValue(bo.getBFNF_Vdur_till_renew());
				}
			 else if(bo.getPlan()!=null){
				  cell.setCellValue("0");
				}
			 else cell.setCellValue("");
			
			 cell = row.getCell(mapBO.get("BFNF Voice Max Number Of Friends"));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("BFNF Voice Max Number Of Friends"));   
				cell.setCellType(Cell.CELL_TYPE_STRING); 
				 if(bo.getPaquete()!=null&&bo.getBFNF_Max_Num_Friends()!=null){
						cell.setCellValue(bo.getBFNF_Max_Num_Friends());
					} else if(bo.getPlan()!=null){
						cell.setCellValue("0");
					}
				 else cell.setCellValue("");
			 cell = row.getCell(mapBO.get("BFNF Voice Service duration"));   
					if (cell == null)   
						cell = row.createCell(mapBO.get("BFNF Voice Service duration"));   
					cell.setCellType(Cell.CELL_TYPE_STRING); 
					 if(bo.getPaquete()!=null&&bo.getBFNF_Voice_Serv_dur()!= null){
							cell.setCellValue(bo.getBFNF_Voice_Serv_dur());
						}else if(bo.getPlan()!=null){
							cell.setCellValue("0");
						}
					 else cell.setCellValue("");
					 
			 cell = row.getCell(mapBO.get("BFNF Voice Days to next number change"));   
						if (cell == null)   
							cell = row.createCell(mapBO.get("BFNF Voice Days to next number change"));   
						cell.setCellType(Cell.CELL_TYPE_STRING); 
						 if(bo.getPaquete()!=null&&bo.getBFNF_VDays_next_numchange()!=null){
								cell.setCellValue(bo.getBFNF_VDays_next_numchange());
							}else if(bo.getPlan()!=null){
								cell.setCellValue("0");
							}
						 else cell.setCellValue("");
			 cell = row.getCell(mapBO.get("BFNF Pre2Post Indicator"));   
							if (cell == null)   
								cell = row.createCell(mapBO.get("BFNF Pre2Post Indicator"));   
							cell.setCellType(Cell.CELL_TYPE_STRING); 
							 if(bo.getPaquete()!=null&&bo.getBFNF_Pre2Post_Ind()!= null){
									cell.setCellValue(bo.getBFNF_Pre2Post_Ind());
							}else if(bo.getPlan()!=null){
								cell.setCellValue("N");
							}
						    else cell.setCellValue("");
 //--------------------- nuevas propiedades --- --------------------
        cell = row.getCell(mapBO.get("FNF Voice Max Number Of Friends"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF Voice Max Number Of Friends"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		if(bo.getPaquete()!=null){			
			if(bo.getFNFV_MAXNUM()!=null)cell.setCellValue(bo.getFNFV_MAXNUM());			
		}else if(bo.getPlan()!=null){
			cell.setCellValue("0");		
			}
	    else cell.setCellValue("");
							 
		cell = row.getCell(mapBO.get("FNF Voice Service duration"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF Voice Service duration"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		if(bo.getPaquete()!=null){
			if(bo.getFNFV_VOICESERVDUR()!=null) cell.setCellValue(bo.getFNFV_VOICESERVDUR());	
		}else if(bo.getPlan()!=null){		
		   cell.setCellValue("0");
		}
	    else cell.setCellValue("");
		
		cell = row.getCell(mapBO.get("FNF Voice duration till renew"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF Voice duration till renew"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		   if(bo.getPaquete()!=null){
				if(bo.getFNFV_VDURTILLRENEW_()!=null) cell.setCellValue(bo.getFNFV_VDURTILLRENEW_());
		   }else if(bo.getPlan()!=null){		
			   cell.setCellValue("0");
			}
		    else cell.setCellValue("");
		   
		cell = row.getCell(mapBO.get("FNF Voice Days to next number change"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF Voice Days to next number change"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		   if(bo.getPaquete()!=null){
				if(bo.getFNFV_VDAYSNEXTNUMCHAN()!=null) cell.setCellValue(bo.getFNFV_VDAYSNEXTNUMCHAN());				
			}else if(bo.getPlan()!=null){		
			   cell.setCellValue("0");
			}else cell.setCellValue(bo.getFNFV_VDAYSNEXTNUMCHAN());
		
		cell = row.getCell(mapBO.get("FNF SMS Max Number Of Friends"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF SMS Max Number Of Friends"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		   if(bo.getPaquete()!=null){
				if(bo.getFNFVSMS_MAXNUM()!=null) cell.setCellValue(bo.getFNFVSMS_MAXNUM());
				}else 
		   if(bo.getPlan()!=null){		
			   cell.setCellValue("0");
			}else cell.setCellValue("");
		
		cell = row.getCell(mapBO.get("FNF SMS Service duration"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF SMS Service duration"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		   if(bo.getPaquete()!=null){
				if(bo.getFNFVSMS_VOICESERVDUR()!=null) cell.setCellValue(bo.getFNFVSMS_VOICESERVDUR());
				}else if(bo.getPlan()!=null){		
			   cell.setCellValue("0");
			}else cell.setCellValue("");
		
		cell = row.getCell(mapBO.get("FNF SMS duration till renew"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF SMS duration till renew"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		   if(bo.getPaquete()!=null){
				if(bo.getFNFVSMS_VDURTILLRENEW()!=null) cell.setCellValue(bo.getFNFVSMS_VDURTILLRENEW());				
			}else if(bo.getPlan()!=null){		
			   cell.setCellValue("0");
			}else cell.setCellValue("");
		
		cell = row.getCell(mapBO.get("FNF SMS Days to next number change"));   
		if (cell == null)   
		   cell = row.createCell(mapBO.get("FNF SMS Days to next number change"));   
		   cell.setCellType(Cell.CELL_TYPE_STRING); 
		   if(bo.getPaquete()!=null){
				if(bo.getFNFVSMS_VDAYSNEXTNUMCHAN()!=null) cell.setCellValue(bo.getFNFVSMS_VDAYSNEXTNUMCHAN());				
			}else if(bo.getPlan()!=null){		
			   cell.setCellValue("0");
			}else cell.setCellValue("");
		
		//___LTE Configuracion    
	   cell = row.getCell(mapBO.get("LTE_QOS_ACT"));   
		if (cell == null)   
		    cell = row.createCell(mapBO.get("LTE_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			if(bo.getPlan()!=null && bo.getPlan().getLTE_QOS_ACT()!=null){	
				   cell.setCellValue(bo.getPlan().getLTE_QOS_ACT());
			}else cell.setCellValue("");
			
	   cell = row.getCell(mapBO.get("LTE_QOS_Control"));
	   if (cell == null)
	      cell = row.createCell(mapBO.get("LTE_QOS_Control"));
		  cell.setCellType(Cell.CELL_TYPE_STRING);
		  if(bo.getPlan()!=null && bo.getPlan().getLTE_QOS_CONTROL()!=null){
		     cell.setCellValue(bo.getPlan().getLTE_QOS_CONTROL());
		  }else cell.setCellValue("");
		  
		cell = row.getCell(mapBO.get("PCRF Code"));
		   if (cell == null)
		      cell = row.createCell(mapBO.get("PCRF Code"));
			  cell.setCellType(Cell.CELL_TYPE_STRING);
			  if(bo.getPlan()!=null && bo.getPlan().getPCRF_CODE()!=null){
			     cell.setCellValue(bo.getPlan().getPCRF_CODE());
		}else cell.setCellValue("");	  
	   
	   cell = row.getCell(mapBO.get("QCI"));
	   if (cell == null)
		  cell = row.createCell(mapBO.get("QCI"));
	      cell.setCellType(Cell.CELL_TYPE_STRING);
		  if(bo.getPlan()!=null && bo.getPlan().getQCI()!=null){
			cell.setCellValue(bo.getPlan().getQCI());
		   }else cell.setCellValue("");
		  
	    cell = row.getCell(mapBO.get("Uplink Speed"));
	    if (cell == null)
	      cell = row.createCell(mapBO.get("Uplink Speed"));
		  cell.setCellType(Cell.CELL_TYPE_STRING);
		  if(bo.getPlan()!=null && bo.getPlan().getUPLINK_SPEED()!=null){
		    cell.setCellValue(bo.getPlan().getUPLINK_SPEED());
		  }else cell.setCellValue("");
		
		cell = row.getCell(mapBO.get("Downlink Speed"));
		if (cell == null)
		  cell = row.createCell(mapBO.get("Downlink Speed"));
		  cell.setCellType(Cell.CELL_TYPE_STRING);
		  if(bo.getPlan()!=null && bo.getPlan().getDOWNLINK_SPEED()!=null){
		    cell.setCellValue(bo.getPlan().getDOWNLINK_SPEED());
		  }else cell.setCellValue("");
	//------------------------------------------		
			cell = row.getCell(mapBO.get("BB_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("BB_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			if(bo.getPlan()!=null)System.out.println(bo.getName());
			if(bo.getPlan()!=null&&bo.getPlan().getTIPOPLAN().equals("Prepago")){
				cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getBB_QOS_ACT():"");}			
			else if(bo.getPlan()!=null&&bo.getPlan().getNOMBRE_APROV()!=null){
				cell.setCellValue(!bo.getPlan().getNOMBRE_APROV().equals("N/A")?"99":"");					
			}else if(bo.getPaquete()!=null){
				if(bo.getTipo()!=null&&bo.getTipo().equals("Prepago"))	cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
				else cell.setCellValue(!bo.getPaquete().getNOMBRE_APROV().equals("N/A")?"99":"");					 
			}else{
				cell.setCellValue("");
			}			
			cell = row.getCell(mapBO.get("BB_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("BB_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);
			if(bo.getPlan()!=null&&bo.getPlan().getTIPOPLAN().equals("Prepago")){
			cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getBB_QOS_CONTROL():"");}			
			else if(bo.getPlan()!=null&&bo.getPlan().getNOMBRE_APROV()!=null){
				cell.setCellValue(!bo.getPlan().getNOMBRE_APROV().equals("N/A")?"99":"");				
			}else if(bo.getPaquete()!=null){
				if(bo.getTipo()!=null&&bo.getTipo().equals("Prepago"))	cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
				else cell.setCellValue(!bo.getPaquete().getNOMBRE_APROV().equals("N/A")?"99":"");
			}else{
				cell.setCellValue("");
				}

			cell = row.getCell(mapBO.get("MMS_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("MMS_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getMMS_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("N/A")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("MMS_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("MMS_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getMMS_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("IAPN_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("IAPN_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getIAPN_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				 cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("IAPN_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("IAPN_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getIAPN_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("VVM_QOS_ACT"));   
			if (cell == null)
				cell = row.createCell(mapBO.get("VVM_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getVVM_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("VVM_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("VVM_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getVVM_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("NML_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("NML_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getNML_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("NML_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("NML_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getNML_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
			//	cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("PAPN_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("PAPN_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getPAPN_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("PAPN_QOS_Control"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("PAPN_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getPAPN_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				 cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}

			cell = row.getCell(mapBO.get("WLM_QOS_ACT"));   
			if (cell == null)   
				cell = row.createCell(mapBO.get("WLM_QOS_ACT"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getWLM_QOS_ACT():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
				//cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			}else{
				cell.setCellValue("");
			}	
			cell = row.getCell(mapBO.get("WLM_QOS_Control"));   
			if (cell == null)
				cell = row.createCell(mapBO.get("WLM_QOS_Control"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			//cell.setCellValue(bo.getPlan()!=null?bo.getPlan().getWLM_QOS_CONTROL():"");
			if(bo.getPlan()!=null&&bo.getPlan().getQA_SERVICIO()!=null){
				cell.setCellValue(bo.getPlan().getQA_SERVICIO());
			}else if(bo.getPaquete()!=null){
			//	cell.setCellValue(!bo.getPaquete().getQA_SERVICIO().equals("NA")?bo.getPaquete().getQA_SERVICIO():"");
				cell.setCellValue(bo.getPaquete().getQOS_INICIAL());
			 }else{
				cell.setCellValue("");
			}
			if(prepago = true ){
				cell = row.getCell(mapBO.get("Template=\"BillingOffer\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));   //Cambio de esta nombre en formato 2013-01-18
				if (cell == null)   
					cell = row.createCell(mapBO.get("Template=\"BillingOffer\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(bo.getName()); 
				
			/*	cell = row.getCell(mapBO.get("CoreElementPath=\"Element Name\";locale=\"ES\""));   //Cambio de esta nombre en formato 2013-01-18
				if (cell == null)   
					cell = row.createCell(mapBO.get("CoreElementPath=\"Element Name\";locale=\"ES\""));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(bo.getName()); */
				
				cell = row.getCell(mapBO.get("BusinessVersionEffectiveDate"));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("BusinessVersionEffectiveDate")); 
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if(bo.getEffectiveDate()!=null){
				calendar = Calendar.getInstance();
				//sdf = new SimpleDateFormat("yyyy/MM/dd");
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				date = sdf.parse(bo.getEffectiveDate());
				calendar.setTime(date);
				fmt = wb.createDataFormat();
				style = wb.createCellStyle();
				style.setAlignment(CellStyle.ALIGN_RIGHT);
				style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss"));
				cell.setCellStyle(style);
				cell.setCellValue(calendar);}				
			}		
			
			//*****Dynamic Properties
           if(bo.getPriceList()!=null){
			for(int j=0;j<bo.getPriceList().size();j++){
				Price price=(Price)bo.getPriceList().get(j);
				if(j!=0){
					fila++;
					row = sheet.getRow(fila); 
					if(row==null)
						row = sheet.createRow(fila);
				}
				cell = row.getCell(mapBO.get("Template=\"BillingOffer\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));   //Cambio de esta nombre en formato 2013-01-18
				if (cell == null)   
					cell = row.createCell(mapBO.get("Template=\"BillingOffer\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(bo.getName()); 
				/**
				 * Agregar nuevo nombre en BO--  CoreElementPath="Element Name";locale="ES" --2013-02-25-----
				 */
		/*		cell = row.getCell(mapBO.get("CoreElementPath=\"Element Name\";locale=\"ES\""));   //Cambio de esta nombre en formato 2013-01-18
				if (cell == null)   
					cell = row.createCell(mapBO.get("CoreElementPath=\"Element Name\";locale=\"ES\""));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(bo.getName()); */
				//------------------------------------------------------------------------------------------------               
				cell = row.getCell(mapBO.get("BusinessVersionEffectiveDate"));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("BusinessVersionEffectiveDate")); 
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if(bo.getEffectiveDate()!=null){
				calendar = Calendar.getInstance();
				//sdf = new SimpleDateFormat("yyyy/MM/dd");
				sdf = new SimpleDateFormat("dd/MM/yyyy");
				date = sdf.parse(bo.getEffectiveDate());
				calendar.setTime(date);
				fmt = wb.createDataFormat();
				style = wb.createCellStyle();
				style.setAlignment(CellStyle.ALIGN_RIGHT);
				style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss"));
				cell.setCellStyle(style);
				cell.setCellValue(calendar);}
				
				cell = row.getCell(mapBO.get("Price;Template=\"name\""));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("Price;Template=\"name\""));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue("Price");//2

				cell = row.getCell(mapBO.get("Price;UniqueIdentifier=\"Element Name\""));   
				if (cell == null)   
					cell = row.createCell(mapBO.get("Price;UniqueIdentifier=\"Element Name\""));   
				cell.setCellType(Cell.CELL_TYPE_STRING);   
				cell.setCellValue(price.getName());
				}
			}
			fila++;
			System.out.println(" fila " + fila );		 
		}
		FileOutputStream fileOut = null;
		if(!panama){
			fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}
		wb.write(fileOut);   
		fileOut.close();
		return fila++;
	}

	/**
	 * Método que escribe los prices en el archivo excel D:\\AMX EPC Billing Offers Version 9.xlsx
	 * @param boList: Listado de Billing Offers a escribir
	 * @param fila. Fila actual de escritura
	 * @param mapPrice: Mapa de ubicación de columnas en el archivo
	 * @param panama: Indica si la migración es de Colombia o de Panamá
	 * @return fila actual del proceso de escritura
	 * @throws Exception
	 */
	public static int escribirPrices(List<BillingOffer> boList, int fila, HashMap<String, Integer> mapPrice, boolean panama) throws Exception{
		InputStream inp=null;
		if(!panama){
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}
		Workbook wb = WorkbookFactory.create(inp); 
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);
		Sheet sheet = wb.getSheet("Price");
		for(BillingOffer bo:boList){			
			/**
			 * La de suspensión se esta haciendo manual, solo se guarda en BO, para generar la PO.
			 */
			if ( bo.getName() != null && bo.isDemanda()== false &&( 
					  bo.getName().toString().trim().contains("Suspension")					
					||bo.getName().toString().trim().contains("Numero VIP"))){
						continue;
					}
				
			for(Price price:bo.getPriceList()){				
				if(price.getIndicadorGenerico()!=1){
					System.out.println("Nombre Price _- : "+price.getName()+ "  "+price.getPrioridad() );
					String f = price.getName();
					Row row = sheet.getRow(fila); 
					if(row==null)
						row = sheet.createRow(fila);
					for(int j=0;j<price.getPritList().size();j++){
						Prit prit=(Prit)price.getPritList().get(j);
								if(j!=0){
									fila++;
									row = sheet.getRow(fila); 
									if(row==null)
										row = sheet.createRow(fila);
								}
						prit.setRow(fila);
						Cell cell = row.getCell(mapPrice.get("Template=\"Price\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));  
						if (cell == null)   
							cell = row.createCell(mapPrice.get("Template=\"Price\";UniqueIdentifier=\"Element Name\";locale=\"EN\""));   
						cell.setCellType(Cell.CELL_TYPE_STRING);   
						cell.setCellValue(f);					
						// cambio por adicion de campo para nombre en español de Price-----
						
						cell = row.getCell(mapPrice.get("CoreElementPath=\"Element Name\";locale=\"ES\""));  
						if (cell == null)   
							cell = row.createCell(mapPrice.get("CoreElementPath=\"Element Name\";locale=\"ES\""));   
						cell.setCellType(Cell.CELL_TYPE_STRING);   
						cell.setCellValue(price.getName());						
						//------------------------------------------------------------------------------------
						cell = row.getCell(mapPrice.get("BusinessVersionEffectiveDate"));   
						if (cell == null)   
							cell = row.createCell(mapPrice.get("BusinessVersionEffectiveDate"));
						if(bo.getEffectiveDate()!=null){
						Calendar calendar = Calendar.getInstance();
						//SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						Date date = sdf.parse(bo.getEffectiveDate());
						calendar.setTime(date);
						DataFormat fmt = wb.createDataFormat();
						CellStyle style = wb.createCellStyle();
						style.setAlignment(CellStyle.ALIGN_RIGHT);
						style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss"));
						cell.setCellStyle(style);  
						cell.setCellValue(calendar);}
						if(j==0){
							cell = row.getCell(mapPrice.get("Code"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Code"));  
							/* 
							 * Cambiar las descripciones de espacios a underscore.....
							 */
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(price.getName().replace(" ", "_"));

							cell = row.getCell(mapPrice.get("Priority"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Priority"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(price.getPrioridad());

							cell = row.getCell(mapPrice.get("Description;Locale=\"es\"")); 
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Description;Locale=\"es\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(price.getName());

							cell = row.getCell(mapPrice.get("Currency")); 
							if (cell == null)
								cell = row.createCell(mapPrice.get("Currency"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue("COP");

							cell = row.getCell(mapPrice.get("MarketIndicator")); 
							if (cell == null)   
								cell = row.createCell(mapPrice.get("MarketIndicator"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue("false");
							
							cell = row.getCell(mapPrice.get("BillingOfferIndication")); 
							if (cell == null)   
								cell = row.createCell(mapPrice.get("BillingOfferIndication"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue("false");
						}

						if(prit.getUsageCharge()!=null){

							if(prit.getUsageCharge().getEntryIndex()!=null){
								cell = row.getCell(mapPrice.get("UsageCharge;EntryIndex=\"Dummy Entry Index\";Level=1"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("UsageCharge;EntryIndex=\"Dummy Entry Index\";Level=1"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getEntryIndex());
							}

							cell = row.getCell(mapPrice.get("NameToFillA"));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("NameToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getName());

							cell = row.getCell(mapPrice.get("DescriptionToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("DescriptionToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getName());

							cell = row.getCell(mapPrice.get("RoleToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("RoleToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRole());

							cell = row.getCell(mapPrice.get("ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getPit());

							cell = row.getCell(mapPrice.get("Default_Rolling_ind"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Default_Rolling_ind"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getDefaultRollingId());

							cell = row.getCell(mapPrice.get("Period_sensitivity_policy"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Period_sensitivity_policy"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getPeriodSensitivityPolicy());

							cell = row.getCell(mapPrice.get("Rolling_policy_A"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Rolling_policy_A")); 
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRollingPolicy());

							cell = row.getCell(mapPrice.get("Rolling_policy_B"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Rolling_policy_B")); 
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRollingPolicyAllowance());

							cell = row.getCell(mapPrice.get("Service_filter"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Service_filter"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);     
							cell.setCellValue(prit.getUsageCharge().getServiceFilter());

							cell = row.getCell(mapPrice.get("Rounding_factor;Quantity=\"unit\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Rounding_factor;Quantity=\"unit\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRoundingFactorMedida());

							if(prit.getUsageCharge().getRoundingFactorCantidad()!=null){
								cell = row.getCell(mapPrice.get("Rounding_factor;Quantity=\"value\""));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Rounding_factor;Quantity=\"value\""));   
								cell.setCellType(Cell.CELL_TYPE_STRING);
								cell.setCellValue(prit.getUsageCharge().getRoundingFactorCantidad());
							}

							cell = row.getCell(mapPrice.get("Should_prorate"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Should_prorate"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getShouldProrate());

							cell = row.getCell(mapPrice.get("Rounding_method"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Rounding_method"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRoundingMethod());

							if(prit.getUsageCharge().getIntercalatedPromotionCycles()!=null){
								cell = row.getCell(mapPrice.get("Intercalated_promotion_cycles"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Intercalated_promotion_cycles"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getIntercalatedPromotionCycles());
							}

							cell = row.getCell(mapPrice.get("Utilize_for_zero_rate"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Utilize_for_zero_rate"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getUtilizeZeroForRate());

							if(prit.getUsageCharge().getNumber_of_cycles_to_roll()!=null){
								cell = row.getCell(mapPrice.get("Number_of_cycles_to_roll"));  
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Number_of_cycles_to_roll"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getNumber_of_cycles_to_roll());
							}

							cell = row.getCell(mapPrice.get("Minimum_unit;Quantity=\"unit\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Minimum_unit;Quantity=\"unit\""));    
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getMinimum_unitA());

							if(prit.getUsageCharge().getMinimum_unitB()!=null){
								cell = row.getCell(mapPrice.get("Minimum_unit;Quantity=\"value\""));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Minimum_unit;Quantity=\"value\""));   
								cell.setCellType(Cell.CELL_TYPE_STRING); 
								cell.setCellValue(prit.getUsageCharge().getMinimum_unitB());
							}

							cell = row.getCell(mapPrice.get("Special_day_set;Template=\"name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Special_day_set;Template=\"name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getSpecial_day_setA());

							cell = row.getCell(mapPrice.get("Special_day_set;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Special_day_set;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getSpecial_day_setB());

							cell = row.getCell(mapPrice.get("Period_set;Template=\"name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Period_set;Template=\"name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getPeriod_setA());

							cell = row.getCell(mapPrice.get("Period_set;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Period_set;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getPeriod_setB());

							cell = row.getCell(mapPrice.get("Service_filter_to_charge_code;Template=\"name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Service_filter_to_charge_code;Template=\"name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getService_filter_to_charge_codeA());

							cell = row.getCell(mapPrice.get("Service_filter_to_charge_code;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Service_filter_to_charge_code;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getService_filter_to_charge_codeB());

							cell = row.getCell(mapPrice.get("Service_filter_group;Template=\"name\""));  
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Service_filter_group;Template=\"name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getService_filter_groupA());

							cell = row.getCell(mapPrice.get("Service_filter_group;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Service_filter_group;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getService_filter_groupB());

							cell = row.getCell(mapPrice.get("Charge_codes_object;Template=\"name\"ToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codes_object;Template=\"name\"ToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getCharge_codes_objectA());

							cell = row.getCell(mapPrice.get("Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getCharge_codes_objectB());
							
							cell = row.getCell(mapPrice.get("Rate_group_list;UniqueIdentifier=\"Element Name\"ToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Rate_group_list;UniqueIdentifier=\"Element Name\"ToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRateGroupList());

							cell = row.getCell(mapPrice.get("Zone_mapping;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Zone_mapping;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getZone_mapping());

							cell = row.getCell(mapPrice.get("LDI_operators_group;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("LDI_operators_group;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getProviders_group());

							cell = row.getCell(mapPrice.get("Zone_list_group;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Zone_list_group;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getZone_list_group());

							cell = row.getCell(mapPrice.get("Zones;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Zones;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getZones());
							//...............Incluir Tax_change_objec-31-01-2013.........................................
							cell = row.getCell(mapPrice.get("Tax_change_object;Template=\"name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Tax_change_object;Template=\"name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRLC_Tax_change());									
							
							cell = row.getCell(mapPrice.get("Tax_change_object;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Tax_change_object;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getTaxchange());
							//...............Termina Incluir Tax_change_objec-31-01-2013.........................................
							//---------------Inicia inclusion de codigo para  Buzon correo de voz--------------------------------
							if(prit.getUsageCharge().getRSPR_EntryIndex()!=null){
								
								cell = row.getCell(mapPrice.get("Rate_based_on_scale_and_period_rate;EntryIndex=\"Dummy Entry Index\";Level=3ToFillA"));    
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Rate_based_on_scale_and_period_rate;EntryIndex=\"Dummy Entry Index\";Level=3ToFillA"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getRSPR_EntryIndex());
							}
							cell = row.getCell(mapPrice.get("IncrementalToFillH"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillH"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRSPR_Incremental());
							
							cell = row.getCell(mapPrice.get("UOMForQuantityToFillG"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UOMForQuantityToFillG"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRSPR_UOMForQuantity());							
							
							//------------------------------------------------------------------------------------------------
							if(prit.getUsageCharge().getQPP_EntryIndex()!=null){
								
								cell = row.getCell(mapPrice.get("Quota_per_period;EntryIndex=\"Dummy Entry Index\";Level=3"));    
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Quota_per_period;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getQPP_EntryIndex());
							}

							cell = row.getCell(mapPrice.get("IncrementalToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQPP_Incremental());

							cell = row.getCell(mapPrice.get("UOMForQuantityToFillA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UOMForQuantityToFillA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQPP_UOMForQuantity());

							if(prit.getUsageCharge().getQCTP_EntryIndex()!=null){
								cell = row.getCell(mapPrice.get("Quota_per_call_type_and_period;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Quota_per_call_type_and_period;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getQCTP_EntryIndex());
							}

							cell = row.getCell(mapPrice.get("IncrementalToFillB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQCTP_Incremental());

							cell = row.getCell(mapPrice.get("UOMForQuantityToFillB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UOMForQuantityToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQCTP_UOMForQuantity());

							cell = row.getCell(mapPrice.get("Dimension;Template=\"name\"A"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Dimension;Template=\"name\"A"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQCTP_DimensionA());

							cell = row.getCell(mapPrice.get("Dimension;UniqueIdentifier=\"Element Name\"A"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Dimension;UniqueIdentifier=\"Element Name\"A"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQCTP_DimensionB());

							if(prit.getUsageCharge().getRPP_EntryIndex()!=null){
								cell = row.getCell(mapPrice.get("Rate_per_period_rate;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Rate_per_period_rate;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								cell.setCellType(Cell.CELL_TYPE_STRING); 
								cell.setCellValue(prit.getUsageCharge().getRPP_EntryIndex());
							}

							cell = row.getCell(mapPrice.get("IncrementalToFillC"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRPP_Incremental());

							cell = row.getCell(mapPrice.get("UOMForQuantityToFillC"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UOMForQuantityToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRPP_UOMForQuantity());


							//UNIT RATE INDICATOR
							if(prit.getUsageCharge().getURI_EntryIndex()!=null){
								cell = row.getCell(mapPrice.get("Unit_rate;EntryIndex=\"Dummy Entry Index\";Level=3"));    
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Unit_rate;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getURI_EntryIndex());
							}

							cell = row.getCell(mapPrice.get("IncrementalToFillD"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillD"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getURI_Incremental());

							cell = row.getCell(mapPrice.get("UOMForQuantityToFillD"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UOMForQuantityToFillD"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getURI_UOMForQuantity());

                            cell = row.getCell(mapPrice.get("Dimension;UniqueIdentifier=\"Element Name\"B"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Dimension;UniqueIdentifier=\"Element Name\"B"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getURI_DimensionB());
							//UNIT RATE INDICATOR
							//----------------------- cambio de 30-12-2012----------------------------------------
							//Quota_per_call_per_period
							if(prit.getUsageCharge().getQCPP_EntryIndex()!=null){
								cell = row.getCell(mapPrice.get("Quota_per_call_per_period;EntryIndex=\"Dummy Entry Index\";Level=3"));    
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Quota_per_call_per_period;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getQCPP_EntryIndex());
							}
							cell = row.getCell(mapPrice.get("IncrementalToFillG"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillG"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQCPP_Incremental());

							cell = row.getCell(mapPrice.get("UOMForQuantityToFillF"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UOMForQuantityToFillF"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getQCPP_UOMForQuantity());							
							
							//----------------------- termina cambio de 30-12-2012----------------------------------------
							//DURATION_SCALE
							if(prit.getUsageCharge().getDS_EntryIndex()!=null){								
								cell = row.getCell(mapPrice.get("Duration_scale;EntryIndex=\"Dummy Entry Index\";Level=3ToFillA"));    
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Duration_scale;EntryIndex=\"Dummy Entry Index\";Level=3ToFillA"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getDS_EntryIndex());
							}
							cell = row.getCell(mapPrice.get("UnitsToFillC"));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UnitsToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getDS_Units());
							
							if(prit.getUsageCharge().getDSScale__EntryIndex()!=null){
								
									cell = row.getCell(mapPrice.get("ScaleRange;EntryIndex=\"Dummy Entry Index\";Level=4ToFillC"));
									if (cell == null)   
										cell = row.createCell(mapPrice.get("ScaleRange;EntryIndex=\"Dummy Entry Index\";Level=4ToFillC"));   
									cell.setCellType(Cell.CELL_TYPE_STRING);   
									cell.setCellValue(prit.getUsageCharge().getDSScale__EntryIndex());
							}
							if(prit.getUsageCharge().getDSScale_From()!=null){
									cell = row.getCell(mapPrice.get("FromToFillC"));   
									if (cell == null)   
										cell = row.createCell(mapPrice.get("FromToFillC"));   
									cell.setCellType(Cell.CELL_TYPE_STRING);   
									cell.setCellValue(prit.getUsageCharge().getDSScale_From());
							}
							if(prit.getUsageCharge().getDSScale_To()!=null){
									cell = row.getCell(mapPrice.get("ToToFillC"));   
									if (cell == null)   
										cell = row.createCell(mapPrice.get("ToToFillC"));   
									cell.setCellType(Cell.CELL_TYPE_STRING);   
									cell.setCellValue(prit.getUsageCharge().getDSScale_To());						
							}
							//RATE TABLE RATE
							if(prit.getUsageCharge().getRTR_EntryIndex()!=null){
								cell = row.getCell(mapPrice.get("Rate_table_rate;EntryIndex=\"Dummy Entry Index\";Level=3"));    
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Rate_table_rate;EntryIndex=\"Dummy Entry Index\";Level=3"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getRTR_EntryIndex());
							}

							cell = row.getCell(mapPrice.get("IncrementalToFillE"));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillE"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRTR_Incremental());

							cell = row.getCell(mapPrice.get("UOMForQuantityToFillE"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("UOMForQuantityToFillE"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getUsageCharge().getRTR_UOMForQuantity());
							//RATE TABLE RATE
						}
						if(prit.getRecurringCharge()!=null){
							cell = row.getCell(mapPrice.get("RecurringCharge;EntryIndex=\"Dummy Entry Index\";Level=1"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("RecurringCharge;EntryIndex=\"Dummy Entry Index\";Level=1"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getEntryIndex());

							cell = row.getCell(mapPrice.get("NameToFillB"));   
							if (cell == null)
								cell = row.createCell(mapPrice.get("NameToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getName());

							cell = row.getCell(mapPrice.get("DescriptionToFillB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("DescriptionToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getName());

							cell = row.getCell(mapPrice.get("RoleToFillB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("RoleToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getRole());

							cell = row.getCell(mapPrice.get("ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getPit());

							if(prit.getRecurringCharge().getBoundary_date_of_Special_suspension_rate()!=null){
								cell = row.getCell(mapPrice.get("Boundary_date_of_Special_suspension_rate"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Boundary_date_of_Special_suspension_rate")); 
								cell.setCellType(Cell.CELL_TYPE_STRING);
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = null;
								if(prit.getRecurringCharge().getBoundary_date_of_Special_suspension_rate()!=null)
									 date = sdf.parse(prit.getRecurringCharge().getBoundary_date_of_Special_suspension_rate());
								 else if(prit.getRecurringCharge().getBou_date_spe_sus_rate()!=null)
									 date = sdf.parse(prit.getRecurringCharge().getBou_date_spe_sus_rate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd-mmm-yyyy hh:mm:ss"));
								cell.setCellStyle(style);
								cell.setCellValue(calendar);
							}

							cell = row.getCell(mapPrice.get("Immediate_payment_requiredA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Immediate_payment_requiredA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getImmediate_payment_required());
							
							if (prit.getRecurringCharge() != null && (prit.getRecurringCharge().getTaxServiceType() != null && !prit.getRecurringCharge().getTaxServiceType().equals(""))){
								cell = row.getCell(mapPrice.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Price Properties");
							}
														
							if(prit.getRecurringCharge() != null && (prit.getRecurringCharge().getTaxServiceType() != null)){
								//System.out.println("pasa por Tax service type");
							cell = row.getCell(mapPrice.get("Tax Service Type"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Tax Service Type"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getTaxServiceType());}
							
							cell = row.getCell(mapPrice.get("Create_multiple_charges_indicator"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Create_multiple_charges_indicator"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getCreate_multiple_charges_indicator());

							cell = row.getCell(mapPrice.get("Frequency"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Frequency"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getFrequency());

							cell = row.getCell(mapPrice.get("Charge_codeA"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codeA"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getCharge_code());

							cell = row.getCell(mapPrice.get("Proration_method"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Proration_method"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getProration_method());

							cell = row.getCell(mapPrice.get("Frequency_of_payments"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Frequency_of_payments"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getFrequency_of_payments());

							cell = row.getCell(mapPrice.get("RC_level"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("RC_level"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getRC_level());

							cell = row.getCell(mapPrice.get("Payment_timing"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Payment_timing"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getPayment_timing());

							if(prit.getRecurringCharge().getRate()!=null){
								cell = row.getCell(mapPrice.get("RateC"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("RateC"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getRecurringCharge().getRate());
							}

							cell = row.getCell(mapPrice.get("Allow_RC_override_type"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Allow_RC_override_type"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getAllow_RC_override_type());

							cell = row.getCell(mapPrice.get("Discount_percentage_per_suspension_reason;Template=\"name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Discount_percentage_per_suspension_reason;Template=\"name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getDiscount_percentage_per_suspension_reasonA());

							cell = row.getCell(mapPrice.get("Discount_percentage_per_suspension_reason;UniqueIdentifier=\"Element Name\""));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Discount_percentage_per_suspension_reason;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getDiscount_percentage_per_suspension_reasonB());

							cell = row.getCell(mapPrice.get("Charge_codes_object;Template=\"name\"ToFillB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codes_object;Template=\"name\"ToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getCharge_codes_objectA());

							cell = row.getCell(mapPrice.get("Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getCharge_codes_objectB());
							
							//--------------------------------- CODIGO DE 27-09-2012-------------------------------------------
							if(prit.getRecurringCharge().getPer_for_downgrade()!=null&&!prit.getRecurringCharge().getPer_for_downgrade().equals("")){
							cell = row.getCell(mapPrice.get("Percentage_for_downgrade"));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Percentage_for_downgrade"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getPer_for_downgrade());	
							}			
							//-------------------------------------------------------------------------------------------------
							
							cell = row.getCell(mapPrice.get("Discount_percentage_per_suspension_reason;Template=\"name\""));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Discount_percentage_per_suspension_reason;Template=\"name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getDiscount_percentage_per_suspension_reasonA());

							cell = row.getCell(mapPrice.get("Discount_percentage_per_suspension_reason;UniqueIdentifier=\"Element Name\""));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Discount_percentage_per_suspension_reason;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getDiscount_percentage_per_suspension_reasonB());

							cell = row.getCell(mapPrice.get("Alternative_Charge_codes_object;UniqueIdentifier=\"Element Name\""));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Alternative_Charge_codes_object;UniqueIdentifier=\"Element Name\""));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getAlternative_Charge_codes_objectB());

							if(prit.getRecurringCharge().getDiscount()!=null){
								cell = row.getCell(mapPrice.get("DiscountC"));
								if (cell == null)   
									cell = row.createCell(mapPrice.get("DiscountC"));   
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell.setCellValue(prit.getRecurringCharge().getDiscount());
							}
							if(prit.getRecurringCharge().getDiscount_scheme()!=null){
							cell = row.getCell(mapPrice.get("Discount_schemeC"));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Discount_schemeC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getDiscount_scheme());
							}
							//QUALIFIED CHARGE CODES
							if(prit.getRecurringCharge().getQcc_entryIndex()!=null){
								cell = row.getCell(mapPrice.get("Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3C"));    
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3C"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getRecurringCharge().getQcc_entryIndex());
							}

							cell = row.getCell(mapPrice.get("IncrementalToFillF"));
							if (cell == null)   
								cell = row.createCell(mapPrice.get("IncrementalToFillF"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getRecurringCharge().getQcc_incremental());
						}
						
						if(prit.getOneTimeCharge()!=null){
							
							cell = row.getCell(mapPrice.get("OneTimeCharge;EntryIndex=\"Dummy Entry Index\";Level=1"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("OneTimeCharge;EntryIndex=\"Dummy Entry Index\";Level=1"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getOneTimeCharge().getEntryIndex());
							
							
							cell = row.getCell(mapPrice.get("NameToFillC"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("NameToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getOneTimeCharge().getName());
							
							cell = row.getCell(mapPrice.get("RoleToFillC"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("RoleToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getOneTimeCharge().getRole());
							
							cell = row.getCell(mapPrice.get("ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillC"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getOneTimeCharge().getPit());
							
							cell = row.getCell(mapPrice.get("RateB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("RateB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING); 
							if(prit.getOneTimeCharge().getPriceRate()!=null)
							    cell.setCellValue(prit.getOneTimeCharge().getPriceRate());
							
							cell = row.getCell(mapPrice.get("Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillC"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getOneTimeCharge().getCharCodesObject());
							
							cell = row.getCell(mapPrice.get("Charge_codes_object;Template=\"name\"ToFillC"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codes_object;Template=\"name\"ToFillC"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getOneTimeCharge().getCharCodesObjecTemp());
														
							cell = row.getCell(mapPrice.get("Charge_codeB"));   
							if (cell == null)   
								cell = row.createCell(mapPrice.get("Charge_codeB"));   
							cell.setCellType(Cell.CELL_TYPE_STRING);   
							cell.setCellValue(prit.getOneTimeCharge().getOneTimeCC());	
							
							if(prit.getOneTimeCharge().getQUAl_CC_INDEX()!=null){								
								cell = row.getCell(mapPrice.get("Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3B"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3B"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getQUAl_CC_INDEX());}
							if(prit.getOneTimeCharge().getQUAL_CC_INCRE()!=null){
								cell = row.getCell(mapPrice.get("IncrementalToFillI"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("IncrementalToFillI"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getQUAL_CC_INCRE());}							
							
							//-------------------------------------------------------------------------------------------											
							if(mapPrice.get("Paid_from_balance")!=null){
								cell = row.getCell(mapPrice.get("Paid_from_balance"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Paid_from_balance"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getPaidfrombalance());}
							
							if(prit.getOneTimeCharge().getDISCOUNT()!=null){
								cell = row.getCell(mapPrice.get("DiscountB"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("DiscountB"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getDISCOUNT());}
							
							if(prit.getOneTimeCharge().getDISCOUNT_SCHEME()!=null){
								cell = row.getCell(mapPrice.get("Discount_schemeB"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Discount_schemeB"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getDISCOUNT_SCHEME());}						
							//-------------------------------------------------------------------------------------------							
							if(prit.getOneTimeCharge().getTaxServiceType() != null && !prit.getOneTimeCharge().getTaxServiceType().equals("")) {
								cell = row.getCell(mapPrice.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Properties;UniqueIdentifier=\"Element Name\";Level=1"));
								cell.setCellType(Cell.CELL_TYPE_STRING);
								cell.setCellValue("Price Properties");
							}
							if(prit.getOneTimeCharge().getTaxServiceType() != null ){
								cell = row.getCell(mapPrice.get("Tax Service Type"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Tax Service Type"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getTaxServiceType());
								}
							if(prit.getOneTimeCharge().getImmediatepaymentrequired()!= null ){
								cell = row.getCell(mapPrice.get("Immediate_payment_requiredB"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Immediate_payment_requiredB"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getImmediatepaymentrequired());
								}
							if(prit.getOneTimeCharge().getOC_LEVEL()!= null ){
								cell = row.getCell(mapPrice.get("OC_level"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("OC_level"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getOC_LEVEL());
								}
							if(prit.getOneTimeCharge().getALLOWOVERRIDETYPE()!= null ){
								cell = row.getCell(mapPrice.get("Allow_override_type"));   
								if (cell == null)   
									cell = row.createCell(mapPrice.get("Allow_override_type"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getALLOWOVERRIDETYPE());
								}
						}
					}
					fila++;
				}
			}
		}

		FileOutputStream fileOut = null;
		if(!panama){
			fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}
		wb.write(fileOut);   
		fileOut.close();
		return fila++;
	}

	/**
	 * Método que mapea los números de columna con el nombre de la columna
	 * @param strSheet: Hoja del archivo
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Integer> obtnerNumCols(String strSheet) throws Exception{
		String columnName="";
		InputStream inp = new FileInputStream("D://EntradasEPC/AMX EPC Billing Offers Version 9.xlsx");   
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet(strSheet);

		Row row = sheet.getRow(0);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Cell cell : row) {
			if(cell!=null){
				if(strSheet.equals("BillingOffer")){
					if(cell.getRichStringCellValue().getString().equals("Currency")){
						if(map.get("CurrencyToFill")==null){
							columnName="CurrencyToFill";
							cell.setCellValue("CurrencyToFill");
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Duration;Quantity=\"unit\"")){
						if(map.get("Duration;Quantity=\"unit\"ToFill")==null){
							columnName="Duration;Quantity=\"unit\"ToFill";
							cell.setCellValue("Duration;Quantity=\"unit\"ToFill");
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Duration;Quantity=\"value\"")){
						if(map.get("Duration;Quantity=\"value\"ToFill")==null){
							columnName="Duration;Quantity=\"value\"ToFill";
							cell.setCellValue("Duration;Quantity=\"value\"ToFill");
							map.put(columnName, cell.getColumnIndex());
						}
					}else{
						map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
					}
				}
				if(strSheet.equals("Price")){
					if(cell.getRichStringCellValue().getString().equals("Description;Locale=\"es\"")){
						if(cell.getColumnIndex()>13&&cell.getColumnIndex()<23){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Name")){
						if(cell.getColumnIndex()>86&&cell.getColumnIndex()<96){
							columnName="NameToFillA";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>855&&cell.getColumnIndex()<890){//NUEVO--
							columnName="NameToFillB";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>470&&cell.getColumnIndex()<490){
							columnName="NameToFillC";
							map.put(columnName, cell.getColumnIndex());
						}
						
					}else if(cell.getRichStringCellValue().getString().equals("Description")){
						if(cell.getColumnIndex()>88&&cell.getColumnIndex()<98){
							columnName="DescriptionToFillA";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>854&&cell.getColumnIndex()<894){//NUEVO--
							columnName="DescriptionToFillB";
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Currency")){
						if(cell.getColumnIndex()>10&&cell.getColumnIndex()<25){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Role")){
						if(cell.getColumnIndex()>87&&cell.getColumnIndex()<107){
							columnName="RoleToFillA";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>859&&cell.getColumnIndex()<892){//NUEVO---
							columnName="RoleToFillB";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>474&&cell.getColumnIndex()<494){
							columnName="RoleToFillC";
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2")){
						if(cell.getColumnIndex()>88&&cell.getColumnIndex()<108){
							columnName="ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillA";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>860&&cell.getColumnIndex()<893){
							columnName="ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillB";
							map.put(columnName, cell.getColumnIndex());//NUEVO--
						}else if(cell.getColumnIndex()>475&&cell.getColumnIndex()<495){
							columnName="ItemParameterValues;UniqueIdentifier=\"Element Name\";Level=2ToFillC";
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Default_Rolling_ind")){
						if(cell.getColumnIndex()>102&&cell.getColumnIndex()<122){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Period_sensitivity_policy")){
						if(cell.getColumnIndex()>116&&cell.getColumnIndex()<136){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equalsIgnoreCase("Rolling_policy")){
						if(cell.getColumnIndex()>122&&cell.getColumnIndex()<132){
							columnName="Rolling_policy_A";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>133&&cell.getColumnIndex()<143){
							columnName="Rolling_policy_B";
							map.put(columnName, cell.getColumnIndex());
						} 
					}else if(cell.getRichStringCellValue().getString().equals("Service_filter")){
						if(cell.getColumnIndex()>120&&cell.getColumnIndex()<140){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rounding_factor;Quantity=\"unit\"")){
						if(cell.getColumnIndex()>136&&cell.getColumnIndex()<156){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rounding_factor;Quantity=\"value\"")){
						if(cell.getColumnIndex()>137&&cell.getColumnIndex()<157){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Should_prorate")){
						if(cell.getColumnIndex()>138&&cell.getColumnIndex()<158){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rounding_method")){
						if(cell.getColumnIndex()>140&&cell.getColumnIndex()<160){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Intercalated_promotion_cycles")){
						if(cell.getColumnIndex()>141&&cell.getColumnIndex()<161){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Utilize_for_zero_rate")){
						if(cell.getColumnIndex()>145&&cell.getColumnIndex()<165){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Number_of_cycles_to_roll")){
						if(cell.getColumnIndex()>148&&cell.getColumnIndex()<168){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Minimum_unit;Quantity=\"unit\"")){
						if(cell.getColumnIndex()>152&&cell.getColumnIndex()<172){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Minimum_unit;Quantity=\"value\"")){
						if(cell.getColumnIndex()>153&&cell.getColumnIndex()<173){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Special_day_set;Template=\"name\"")){
						if(cell.getColumnIndex()>161&&cell.getColumnIndex()<181){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Special_day_set;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>162&&cell.getColumnIndex()<182){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Period_set;Template=\"name\"")){
						if(cell.getColumnIndex()>165&&cell.getColumnIndex()<185){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Period_set;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>166&&cell.getColumnIndex()<186){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Service_filter_to_charge_code;Template=\"name\"")){
						if(cell.getColumnIndex()>169&&cell.getColumnIndex()<189){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Service_filter_to_charge_code;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>170&&cell.getColumnIndex()<190){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Service_filter_group;Template=\"name\"")){
						if(cell.getColumnIndex()>179&&cell.getColumnIndex()<199){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Zone_mapping;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>187&&cell.getColumnIndex()<207){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Providers_group;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>201&&cell.getColumnIndex()<221){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Zone_list_group;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>211&&cell.getColumnIndex()<231){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Zones;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>213&&cell.getColumnIndex()<233){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Service_filter_group;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>128&&cell.getColumnIndex()<148){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Charge_codes_object;Template=\"name\"")){
						if(cell.getColumnIndex()>186&&cell.getColumnIndex()<206){
							columnName="Charge_codes_object;Template=\"name\"ToFillA";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>959&&cell.getColumnIndex()<993){//NUEVO----
							columnName="Charge_codes_object;Template=\"name\"ToFillB";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>572&&cell.getColumnIndex()<592){
							columnName="Charge_codes_object;Template=\"name\"ToFillC";
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Charge_codes_object;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>187&&cell.getColumnIndex()<207){
							columnName="Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillA";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>864&&cell.getColumnIndex()<995){//NUEVO----
							columnName="Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillB";
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>573&&cell.getColumnIndex()<593){
							columnName="Charge_codes_object;UniqueIdentifier=\"Element Name\"ToFillC";
							map.put(columnName, cell.getColumnIndex());
						}
					}
					//---------------------------------------------------------- NUEVO CODIGO 27-09-2012-----------------------------
					else if(cell.getRichStringCellValue().getString().equals("Percentage_for_downgrade")){
					     	columnName="Percentage_for_downgrade";
							map.put(columnName, cell.getColumnIndex());						
					}else if(cell.getRichStringCellValue().getString().equals("Paid_from_balance")){
				     	columnName="Paid_from_balance";
						map.put(columnName, cell.getColumnIndex());	 
						}//---------------------------------------------------------------------------------------------------------------
					else if(cell.getRichStringCellValue().getString().equals("Discount_scheme")){
						if(cell.getColumnIndex()>544&&cell.getColumnIndex()<564){
					     	columnName="Discount_schemeB";
							map.put(columnName, cell.getColumnIndex());	
							}
						if(cell.getColumnIndex()>934&&cell.getColumnIndex()<966){
					     	columnName="Discount_schemeC";
							map.put(columnName, cell.getColumnIndex());	
							}
						}
					else if(cell.getRichStringCellValue().getString().equals("Discount")){
						if(cell.getColumnIndex()>138&&cell.getColumnIndex()<148){
					     	columnName="DiscountA";
							map.put(columnName, cell.getColumnIndex());	
							}	
						if(cell.getColumnIndex()>524&&cell.getColumnIndex()<534){
							System.out.println(" hola  ..............................................................");
					     	columnName="DiscountB";
							map.put(columnName, cell.getColumnIndex());	
							}
						if(cell.getColumnIndex()>904&&cell.getColumnIndex()<936){
							columnName="DiscountC";
							map.put(columnName, cell.getColumnIndex());	
							}
						}					
					//-------------------------------------------------------------------------------------				
					else if(cell.getRichStringCellValue().getString().equals("Rate_group_list;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>203&&cell.getColumnIndex()<223){
							columnName="Rate_group_list;UniqueIdentifier=\"Element Name\"ToFillA";
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rate_based_on_scale_and_period_rate;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>240&&cell.getColumnIndex()<255){
							columnName="Rate_based_on_scale_and_period_rate;EntryIndex=\"Dummy Entry Index\";Level=3ToFillA";
							map.put(columnName, cell.getColumnIndex());							
						}
					}else if(cell.getRichStringCellValue().getString().equals("Quota_per_period;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>253&&cell.getColumnIndex()<263){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rate_table_rate;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>375&&cell.getColumnIndex()<385){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Incremental")){
						if(cell.getColumnIndex()>253&&cell.getColumnIndex()<267){
							columnName="IncrementalToFillA";//Quota per period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>342&&cell.getColumnIndex()<350){
							columnName="IncrementalToFillB";//Quota per call type period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>465&&cell.getColumnIndex()<480){
							columnName="IncrementalToFillC";//Rate per period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>434&&cell.getColumnIndex()<440){
							columnName="IncrementalToFillD";//Unit rate ind
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>374&&cell.getColumnIndex()<388){
							columnName="IncrementalToFillE";//Rate table rate
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>1105&&cell.getColumnIndex()<1115){
							columnName="IncrementalToFillF";//Qualified charge codes//NUEVO---
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>406&&cell.getColumnIndex()<416){
							columnName="IncrementalToFillG";//Quota_per_call_per_period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>242&&cell.getColumnIndex()<252){
							columnName="IncrementalToFillH";//Price:Rate_based_on_scale_and_period_rate
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>725&&cell.getColumnIndex()<737){
							columnName="IncrementalToFillI";//Qualified_charge_codes
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("UOMForQuantity")){
						if(cell.getColumnIndex()>256&&cell.getColumnIndex()<266){
							columnName="UOMForQuantityToFillA";//Quota per period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>342&&cell.getColumnIndex()<350){
							columnName="UOMForQuantityToFillB";//Quota per call type period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>473&&cell.getColumnIndex()<481){
							columnName="UOMForQuantityToFillC";//Rate per period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>434&&cell.getColumnIndex()<441){
							columnName="UOMForQuantityToFillD";//Unit rate ind
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>379&&cell.getColumnIndex()<389){
							columnName="UOMForQuantityToFillE";//Rate table rate
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>409&&cell.getColumnIndex()<417){
							columnName="UOMForQuantityToFillF";//Quota_per_call_per_period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>243&&cell.getColumnIndex()<253){
							columnName="UOMForQuantityToFillG";//Price:Rate_based_on_scale_and_period_rate
							map.put(columnName, cell.getColumnIndex());
						}							
					}else if(cell.getRichStringCellValue().getString().equals("Dimension;Template=\"name\"")){
						if(cell.getColumnIndex()>340&&cell.getColumnIndex()<350){
							columnName="Dimension;Template=\"name\"A";//Quota per call type period
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Dimension;UniqueIdentifier=\"Element Name\"")){
						if(cell.getColumnIndex()>340&&cell.getColumnIndex()<353){
							columnName="Dimension;UniqueIdentifier=\"Element Name\"A";//Quota per call type period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>434&&cell.getColumnIndex()<444){
							columnName="Dimension;UniqueIdentifier=\"Element Name\"B";//Unit rate ind
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("PEComplexAttributeValue;CLOBSheet=\"PEComplexAttributeValue <XML>\"")){
						if(cell.getColumnIndex()>248&&cell.getColumnIndex()<258){
							columnName="PEComplexAttributeValue;CLOBSheet=\"PEComplexAttributeValue <XML>\"ToFillA";//Quota per period
							//cell.setCellValue(columnName);
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>340&&cell.getColumnIndex()<350){
							columnName="PEComplexAttributeValue;CLOBSheet=\"PEComplexAttributeValue <XML>\"ToFillB";//Quota per call type period
							//cell.setCellValue(columnName);
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>462&&cell.getColumnIndex()<472){
							columnName="PEComplexAttributeValue;CLOBSheet=\"PEComplexAttributeValue <XML>\"ToFillC";//Rate per period
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>422&&cell.getColumnIndex()<432){
							columnName="PEComplexAttributeValue;CLOBSheet=\"PEComplexAttributeValue <XML>\"ToFillD";//Unit rate ind
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>370&&cell.getColumnIndex()<380){
							columnName="PEComplexAttributeValue;CLOBSheet=\"PEComplexAttributeValue <XML>\"ToFillD";//Rate table rate
							map.put(columnName, cell.getColumnIndex());
						}
					}
					//----------------------------------------------------------------------------------------------
					else if(cell.getRichStringCellValue().getString().equals("Duration_scale;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>414&&cell.getColumnIndex()<424){
							columnName="Duration_scale;EntryIndex=\"Dummy Entry Index\";Level=3ToFillA";//Duration_scale
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Units")){
						if(cell.getColumnIndex()>305&&cell.getColumnIndex()<316){
							columnName="UnitsToFillA";//Occurrence_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>365&&cell.getColumnIndex()<377){//NUEVO----
							columnName="UnitsToFillB";//Volume_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>414&&cell.getColumnIndex()<421){
							columnName="UnitsToFillC";//Duration_scale
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("ScaleRange;EntryIndex=\"Dummy Entry Index\";Level=4")){
						if(cell.getColumnIndex()>306&&cell.getColumnIndex()<317){
							columnName="ScaleRange;EntryIndex=\"Dummy Entry Index\";Level=4ToFillA";//Occurrence_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>366&&cell.getColumnIndex()<378){//NUEVO----
							columnName="ScaleRange;EntryIndex=\"Dummy Entry Index\";Level=4ToFillB";//Volume_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>415&&cell.getColumnIndex()<422){
							columnName="ScaleRange;EntryIndex=\"Dummy Entry Index\";Level=4ToFillC";//Duration_scale
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("From")){
						if(cell.getColumnIndex()>307&&cell.getColumnIndex()<318){
							columnName="FromToFillA";//Occurrence_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>367&&cell.getColumnIndex()<379){//NUEVO----
							columnName="FromToFillB";//Volume_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>416&&cell.getColumnIndex()<422){
							columnName="FromToFillC";//Duration_scale
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("To")){
						if(cell.getColumnIndex()>308&&cell.getColumnIndex()<319){
							columnName="ToToFillA";//Occurrence_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>368&&cell.getColumnIndex()<380){//NUEVO----
							columnName="ToToFillB";//Volume_scale
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>417&&cell.getColumnIndex()<422){
							columnName="ToToFillC";//Duration_scale
							map.put(columnName, cell.getColumnIndex());
						}
					}					
					//----------------------------------------------------------------------------------------------
					else if(cell.getRichStringCellValue().getString().equals("Quota_per_call_type_and_period;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>340&&cell.getColumnIndex()<349){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rate_per_period_rate;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>470&&cell.getColumnIndex()<480	){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Unit_rate;EntryIndex=\"Dummy Entry Index\";Level=3")){/**PENDIENTE*/
						if(cell.getColumnIndex()>432&&cell.getColumnIndex()<441){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Quota_per_call_per_period;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>407&&cell.getColumnIndex()<417){
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}/*else if(cell.getRichStringCellValue().getString().equals("Discount")){
						if(cell.getColumnIndex()>904&&cell.getColumnIndex()<936){//NUEVO---
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Discount_scheme")){
						if(cell.getColumnIndex()>934&&cell.getColumnIndex()<966){//NUEVO----
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}*/else if(cell.getRichStringCellValue().getString().equals("Boundary_date_of_Special_suspension_rate")){
						if(cell.getColumnIndex()>865&&cell.getColumnIndex()<900){//NUEVO---
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Immediate_payment_required")){
						if(cell.getColumnIndex()>897&&cell.getColumnIndex()<929){//NUEVO--
							columnName="Immediate_payment_requiredA";//Recurring Charge
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>482&&cell.getColumnIndex()<535){
							columnName="Immediate_payment_requiredB";//One Time Charge
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>119&&cell.getColumnIndex()<150){//NUEVO---
							columnName="Immediate_payment_requiredC";//Usage Charge
							map.put(columnName, cell.getColumnIndex());
						}					
					}else if(cell.getRichStringCellValue().getString().equals("OC_level")){
						if(cell.getColumnIndex()>537&&cell.getColumnIndex()<557){//NUEVO----
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Allow_override_type")){
						if(cell.getColumnIndex()>539&&cell.getColumnIndex()<559){//NUEVO----
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Tax Service Type")){
						if(cell.getColumnIndex()>70&&cell.getColumnIndex()<98){//NUEVO----
							map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Properties;UniqueIdentifier=\"Element Name\";Level=1")){
						if(cell.getColumnIndex()>70&&cell.getColumnIndex()<98){//NUEVO---
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
					}else if(cell.getRichStringCellValue().getString().equals("Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3")){
						if(cell.getColumnIndex()>314&&cell.getColumnIndex()<342){//nuevo---
							columnName="Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3A";//Using Charge
							map.put(columnName, cell.getColumnIndex());
						}else if(cell.getColumnIndex()>720&&cell.getColumnIndex()<745){//
							columnName="Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3B";//One Time Charge
							map.put(columnName, cell.getColumnIndex());
						}if(cell.getColumnIndex()>1059&&cell.getColumnIndex()<1135){//nuevo---
							columnName="Qualified_charge_codes;EntryIndex=\"Dummy Entry Index\";Level=3C";//Recurring Charge
							map.put(columnName, cell.getColumnIndex());
						}
					}else if(cell.getRichStringCellValue().getString().equals("Rate_table")){
						 if(cell.getColumnIndex()>703&&cell.getColumnIndex()<723){//
							columnName="Rate_table";//One Time Charge
							map.put(columnName, cell.getColumnIndex());
						}
					}//Termino Cambio para One time Charge 15/01/2013 - Rate_table---			
					else{
						map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
					}
				}
			}
		}
		return map;
	}
	/**
	 * Método deprecado
	 * @param boList
	 * @throws Exception
	 */
	public static void escribirTMCODEvsBO(List<BillingOffer> boList) throws Exception{
		InputStream inp = new FileInputStream("D:\\BOvsTMCODE.xlsx");   
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("TMCODEvsBO");
		int fila=1;
		for(BillingOffer bo:boList){
			Row row = sheet.getRow(fila); 
			if(row==null)
				row = sheet.createRow(fila);

			Cell cell = row.getCell(0); 
			if (cell == null)   
				cell = row.createCell(0);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getTmcode());

			cell = row.getCell(1);
			if (cell == null)   
				cell = row.createCell(1);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getName());

			fila++;
		}

		FileOutputStream fileOut = new FileOutputStream("D:\\BOvsTMCODE.xlsx");   
		wb.write(fileOut);   
		fileOut.close();
	}

	/**
	 * Método deprecado
	 * @param boList
	 * @throws Exception
	 */
	public static void escribirSPCODEvsBO(List<BillingOffer> boList) throws Exception{
		InputStream inp = new FileInputStream("D:\\BOvsSPCODE.xlsx");   
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("SPCODEvsBO");
		int fila=1;
		for(BillingOffer bo:boList){
			Row row = sheet.getRow(fila); 
			if(row==null)
				row = sheet.createRow(fila);

			Cell cell = row.getCell(0); 
			if (cell == null)   
				cell = row.createCell(0);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getPaquete().getSPCODE());

			cell = row.getCell(1);
			if (cell == null)   
				cell = row.createCell(1);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellValue(bo.getName());

			fila++;
		}

		FileOutputStream fileOut = new FileOutputStream("D:\\BOvsSPCODE.xlsx");   
		wb.write(fileOut);   
		fileOut.close();
	}

	/**
	 * Método que escribe los valores correspondientes a cada ItemParameter que se configura en un Price
	 * @param boList: Lista de Billing Offers
	 * @param fila: Fila actual de escritura
	 * @param panama: bandera que indica si la migración es de Colombia o de Panamá
	 * @param mapValues: mapa de ubicación de las columnas de la hoja de values en el archivo
	 * @return fila actual del proceso de escritura
	 * @throws Exception
	 */
	public static int escribirValores(List<BillingOffer> boList, int fila, boolean panama, HashMap<String, Integer> mapValues, boolean fin) throws Exception{
		InputStream inp=null;
		if(!panama){
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("Values");

		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);
		for(BillingOffer bo:boList){
			//System.out.println(" BO: " +bo.getName());			
			/**
			 * La de suspensión se esta haciendo manual, solo se guarda en BO, para generar la PO.
			 */			
			if (bo.getName() != null && bo.isDemanda()== false &&(
					  bo.getName().toString().trim().contains("Suspension") 
					||bo.getName().toString().trim().contains("Numero VIP"))){
						continue;
					}
			
			if (bo.getName() != null && bo.getName().toString().trim().contains("780 Mixto") ){
				System.out.println(bo.getName().toString());
			}
			
			if (bo.getName() != null && bo.getName().toString().trim().contains("780 Abierto") ){
				System.out.println(bo.getName().toString());
			} 
			
			for(Price price:bo.getPriceList()){
				for(Prit prit:price.getPritList()){
					if(prit.getUsageCharge()!=null && prit.getUsageCharge().getName()!=null ){
						if(prit.getUsageCharge().getQPP_EntryIndex()!=null){
							Row row = sheet.getRow(fila); 
							if(row==null)
								row = sheet.createRow(fila);
							Cell cell1 = row.getCell(mapValues.get("Price")); 
							if (cell1 == null)   
								cell1 = row.createCell(mapValues.get("Price"));   
							cell1.setCellType(Cell.CELL_TYPE_STRING);   
							cell1.setCellValue(price.getName());
							
							cell1 = row.getCell(mapValues.get("Pricing Item")); 
							if (cell1 == null)   
								cell1 = row.createCell(mapValues.get("Pricing Item"));   
							cell1.setCellType(Cell.CELL_TYPE_STRING);   
							cell1.setCellValue(prit.getUsageCharge().getName());

							cell1 = row.getCell(mapValues.get("Effective Date"));   
							if (cell1 == null)   
								cell1 = row.createCell(mapValues.get("Effective Date")); 
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							if(bo.getEffectiveDate()!=null){
							Date date = sdf.parse(bo.getEffectiveDate());
							calendar.setTime(date);
							DataFormat fmt = wb.createDataFormat();
							CellStyle style = wb.createCellStyle();
							style.setAlignment(CellStyle.ALIGN_RIGHT);
							style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
							cell1.setCellStyle(style);
							cell1.setCellValue(calendar);}

							cell1 = row.getCell(mapValues.get("Item Parameter")); 
							if (cell1 == null)   
								cell1 = row.createCell(mapValues.get("Item Parameter"));   
							cell1.setCellType(Cell.CELL_TYPE_STRING);   
							cell1.setCellValue("Quota_per_period");
		
							cell1 = row.getCell(mapValues.get("Value")); 
							if (cell1 == null)   
								cell1 = row.createCell(mapValues.get("Value"));   
							cell1.setCellType(Cell.CELL_TYPE_STRING);
							if(bo.getBillOfferGenerics()==1){
								cell1.setCellValue(price.getAllwGenerico());
							}else{
								if(prit != null && prit.getUsageCharge() != null && prit.getUsageCharge().getName() != null && prit.getUsageCharge().getName().contains("TDestino"))
									cell1.setCellValue(bo.getPlan().getALLW_TDESTINO());
								else{
									if(bo.getName().contains("PromoOnnet")){
										cell1.setCellValue(bo.getPlan().getALLW_PROMO());
									}else if(bo.getTipoBolsa()==1||bo.getTipoBolsa()==2||bo.getTipoBolsa()==3||bo.getTipoBolsa()==4||bo.getTipoBolsa()==8){
										if(prit.getUsageCharge().getName().contains("- ELEG FAM.")) cell1.setCellValue(0);
										else cell1.setCellValue(bo.getPlan().getALLW_ONNET());	
									}else if(bo.getTipoBolsa()==5){
										cell1.setCellValue(bo.getPlan().getALLW_SMS());
									}else if(bo.getTipoBolsa()==6){
										cell1.setCellValue(bo.getPlan().getALLW_ONNET());
									}else{
										cell1.setCellValue(0);
									}
								}
							}
							cell1 = row.getCell(mapValues.get("QuantityValue")); 
							if (cell1 == null)   
								cell1 = row.createCell(mapValues.get("QuantityValue"));   
							cell1.setCellType(Cell.CELL_TYPE_NUMERIC);   
							cell1.setCellValue(1);

							cell1 = row.getCell(mapValues.get("Period")); 
							if (cell1 == null)   
								cell1 = row.createCell(mapValues.get("Period"));   
							cell1.setCellType(Cell.CELL_TYPE_STRING);   
							if(bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
								cell1.setCellValue("Offpeak");
							}
							else{
								cell1.setCellValue("Period N/A");
							}

							fila++;
							
							if(bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
								fillPeak_Quota_per_period(sheet, fila, mapValues, price, prit, bo, wb);
								fila++;
							}
						}
						if(prit.getUsageCharge().getQCTP_EntryIndex()!=null){
														
							int cont=1;
							if(bo.getTipoBolsa()==2&&!bo.getPlan().getCARACT_PLAN().equals(IConstants.BOLSA_PARALELA)){
								cont=2;
							}else if(bo.getTipoBolsa()==3){
								cont=3;
							}
							for(int i=0;i<cont;i++){
								Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell2 = row.getCell(mapValues.get("Price")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Price"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue(price.getName());

								cell2 = row.getCell(mapValues.get("Pricing Item")); 
								if (cell2 == null)
									cell2 = row.createCell(mapValues.get("Pricing Item"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue(prit.getUsageCharge().getName());

								cell2 = row.getCell(mapValues.get("Effective Date"));   
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Effective Date")); 
								cell2.setCellType(Cell.CELL_TYPE_STRING);
								if(bo.getEffectiveDate()!=null){
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell2.setCellStyle(style);
								cell2.setCellValue(calendar);}

								cell2 = row.getCell(mapValues.get("Item Parameter")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Item Parameter"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue("Quota_per_call_type_and_period");
								System.out.println(" Pasa por iteracion: "+bo.getPlan().getPLAN() + " , "+ bo.getPlan().getTMCODE());

								cell2 = row.getCell(mapValues.get("Value")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Value"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								if(i==0){
									cell2.setCellValue(bo.getPlan().getALLW_ONNET());
								}
								if(i==1)cell2.setCellValue(bo.getPlan().getALLW_FIJOS());
								if(i==2)cell2.setCellValue(bo.getPlan().getALLW_OFFNET());

								cell2 = row.getCell(mapValues.get("QuantityValue")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("QuantityValue"));   
								cell2.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell2.setCellValue(1);

								cell2 = row.getCell(mapValues.get("Period")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Period"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue("Period N/A");

								cell2 = row.getCell(mapValues.get("Call type")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Call type"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								if(i==0)cell2.setCellValue("V_ONNETMOBILE");
								if(i==1){								
									if(bo.getPlan().getALLW_EFIJO()!=null){
										System.out.println("Allowance_fijo: "+bo.getPlan().getALLW_EFIJO());
										if(bo.getPlan().getALLW_EFIJO()!= 0)
											 cell2.setCellValue("V_ONNETFIXFF");								   
										else cell2.setCellValue("V_OTHERFIX"); }
																	}								
								if(i==2)cell2.setCellValue("V_OTHERMOBILE");
								fila++;
							}
						}
				//------------- Cambio para incluir planes que pertenecen a grupo familia 02-01-2013-------------------------------------------------------------
						
						if(prit.getUsageCharge().getQCPP_EntryIndex()!=null){
							    Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell2 = row.getCell(mapValues.get("Price")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Price"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue(price.getName());

								cell2 = row.getCell(mapValues.get("Pricing Item")); 
								if (cell2 == null)
									cell2 = row.createCell(mapValues.get("Pricing Item"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue(prit.getUsageCharge().getName());

								cell2 = row.getCell(mapValues.get("Effective Date"));   
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Effective Date")); 
								cell2.setCellType(Cell.CELL_TYPE_STRING);
								if(bo.getEffectiveDate()!=null){
									Calendar calendar = Calendar.getInstance();
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									Date date = sdf.parse(bo.getEffectiveDate());
									calendar.setTime(date);
									DataFormat fmt = wb.createDataFormat();
									CellStyle style = wb.createCellStyle();
									style.setAlignment(CellStyle.ALIGN_RIGHT);
									style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
									cell2.setCellStyle(style);
									cell2.setCellValue(calendar);}
								else 
									cell2.setCellValue("");

								cell2 = row.getCell(mapValues.get("Item Parameter")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Item Parameter"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue("Quota_per_call_per_period");

								cell2 = row.getCell(mapValues.get("Value")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Value"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								//cell2.setCellValue(bo.getPlan().getALLW_ELEGONNET()); Este corresponde a cantidad de minutos elegidos familia 
								cell2.setCellValue("5");
								cell2 = row.getCell(mapValues.get("QuantityValue")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("QuantityValue"));   
								cell2.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell2.setCellValue(1);

								cell2 = row.getCell(mapValues.get("Period")); 
								if (cell2 == null)   
									cell2 = row.createCell(mapValues.get("Period"));   
								cell2.setCellType(Cell.CELL_TYPE_STRING);   
								cell2.setCellValue("Period N/A");
								fila++;							
						}						
			   //------------- termina -- Cambio para incluir planes que pertenecen a grupo familia 02-01-2013----------------------------------------------			
						
						if(prit.getUsageCharge().getURI_EntryIndex()!=null){
							int cont=1;
							System.out.println(prit.getUsageCharge().getName());
							if(prit.getUsageCharge().getPit().equals("**Allowance duration recurring per period and call type with relative quota")){
								if(bo.getTipoBolsa()==2&&bo.getPlan().getCARACT_PLAN().contains(IConstants.BOLSA_PARALELA_C_F)&&!price.getName().contains("TDestino")){
									cont=2;
								}else{
									cont=3;
								}
							}else if(prit.getUsageCharge().getPit().equals("**Allowance duration recurring per period")){
								cont=1;
							}
							for(int i=0;i<cont;i++){
								Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell3 = row.getCell(mapValues.get("Price")); 
								if (cell3 == null)   
									cell3 = row.createCell(mapValues.get("Price"));   
								cell3.setCellType(Cell.CELL_TYPE_STRING);   
								cell3.setCellValue(price.getName());

								cell3 = row.getCell(mapValues.get("Pricing Item")); 
								if (cell3 == null)
									cell3 = row.createCell(mapValues.get("Pricing Item"));   
								cell3.setCellType(Cell.CELL_TYPE_STRING);   
								cell3.setCellValue(prit.getUsageCharge().getName());

								cell3 = row.getCell(mapValues.get("Effective Date"));   
								if (cell3 == null)   
									cell3 = row.createCell(mapValues.get("Effective Date")); 
								cell3.setCellType(Cell.CELL_TYPE_STRING);
								if(bo.getEffectiveDate()!=null){
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell3.setCellStyle(style);
								cell3.setCellValue(calendar);}

								cell3 = row.getCell(mapValues.get("Item Parameter")); 
								if (cell3 == null)   
									cell3 = row.createCell(mapValues.get("Item Parameter"));   
								cell3.setCellType(Cell.CELL_TYPE_STRING);   
								cell3.setCellValue("Unit_rate");

								cell3 = row.getCell(mapValues.get("Value")); 
								if (cell3 == null)   
									cell3 = row.createCell(mapValues.get("Value"));   
								cell3.setCellType(Cell.CELL_TYPE_STRING);   
								if(bo.getBillOfferGenerics()==1){
									if(price.getRateGenerico()!=null){
										if(price.getRateGenerico().contains("Prepago")){
											cell3.setCellValue(0);
										}else{
											cell3.setCellValue(Double.parseDouble(price.getRateGenerico().replaceAll(",", ".")));
										}
									}
								}else{
									if(prit.getUsageCharge().getName().contains("- ELEG FAM."))
										cell3.setCellValue(bo.getPlan().getVALORADC_EONNET());
									else if(bo.getTipoBolsa()==2&&price.getName().contains("TDestino")){
										cell3.setCellValue(bo.getPlan().getVALORINC_ONNET());
									}else{
										if(i==0)cell3.setCellValue(bo.getPlan().getVALORINC_ONNET());
										if(i==1)cell3.setCellValue(bo.getPlan().getVALORINC_FIJO());
										if(i==2)cell3.setCellValue(bo.getPlan().getVALORINC_OFFNET());
									}
								}
								cell3 = row.getCell(mapValues.get("QuantityValue")); 
								if (cell3 == null)   
									cell3 = row.createCell(mapValues.get("QuantityValue"));   
								cell3.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell3.setCellValue(1);

								cell3 = row.getCell(mapValues.get("Period")); 
								if (cell3 == null)   
									cell3 = row.createCell(mapValues.get("Period"));   
								cell3.setCellType(Cell.CELL_TYPE_STRING);
								if(bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
									cell3.setCellValue("Offpeak");
								}
								else{
									cell3.setCellValue("Period N/A");
								}

								if(cont>1){
									cell3 = row.getCell(mapValues.get("Call type")); 
									if (cell3 == null)   
										cell3 = row.createCell(mapValues.get("Call type"));   
									cell3.setCellType(Cell.CELL_TYPE_STRING);   
									if(i==0) cell3.setCellValue("V_ONNETMOBILE");										 
									if(i==1){									   
										if(bo.getPlan().getALLW_EFIJO()!=null){
												if(bo.getPlan().getALLW_EFIJO()!=0)cell3.setCellValue("V_ONNETFIXFF");										   
												else                               cell3.setCellValue("V_OTHERFIX"); }																	}									
									if(i==2)cell3.setCellValue("V_OTHERMOBILE");
								}
								fila++;								
								if(bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
									fillPeak_Unit_rate(sheet, fila, mapValues, price, prit, bo, wb);
									fila++;
								}
							}
						}
						if(prit.getUsageCharge().getRPP_EntryIndex()!=null){
							Row row = sheet.getRow(fila); 
							if(row==null)
								row = sheet.createRow(fila);

							Cell cell4 = row.getCell(mapValues.get("Price")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Price"));   
							cell4.setCellType(Cell.CELL_TYPE_STRING);   
							cell4.setCellValue(price.getName());

							cell4 = row.getCell(mapValues.get("Pricing Item")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Pricing Item"));   
							cell4.setCellType(Cell.CELL_TYPE_STRING);   
							cell4.setCellValue(prit.getUsageCharge().getName());

							cell4 = row.getCell(mapValues.get("Effective Date"));   
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Effective Date")); 
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							if(bo.getEffectiveDate()!=null){
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell4.setCellStyle(style);
								cell4.setCellValue(calendar);}
							else cell4.setCellValue("");

							cell4 = row.getCell(mapValues.get("Item Parameter")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Item Parameter"));   
							cell4.setCellType(Cell.CELL_TYPE_STRING);   
							cell4.setCellValue("Rate_per_period_rate");

							cell4 = row.getCell(mapValues.get("Value")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Value"));   
							cell4.setCellType(Cell.CELL_TYPE_NUMERIC);
							if(bo.getBillOfferGenerics()==1){
								if(price.getRateGenerico().contains("Prepago")){
									cell4.setCellValue(0);
								}else{
									cell4.setCellValue(Double.parseDouble(price.getRateGenerico().replaceAll(",", ".")));
								}
							}else if(bo.isDemanda()){
								//Logica de Negocio para demanda
								cell4.setCellValue(Double.parseDouble(prit.getUsageCharge().getValue().replaceAll(",", ".")));
							}else{
								if(prit.getUsageCharge().getName().contains("ONNET"))cell4.setCellValue(Double.parseDouble(bo.getPlan().getVALORADC_ONNET().replaceAll(",", ".").equals("88888888")? "0":bo.getPlan().getVALORADC_ONNET().replaceAll(",", ".")));
							else if(prit.getUsageCharge().getName().contains("FIJO"))cell4.setCellValue(Double.parseDouble(bo.getPlan().getVALORADC_FIJO().replaceAll(",", ".").equals("88888888")? "0":bo.getPlan().getVALORADC_FIJO().replaceAll(",", ".")));
								else if(prit.getUsageCharge().getName().contains("OFFNET"))cell4.setCellValue(Double.parseDouble(bo.getPlan().getVALORADC_OFFNET().replaceAll(",", ".").equals("88888888")? "0":bo.getPlan().getVALORADC_OFFNET().replaceAll(",", ".")));
								else cell4.setCellValue(Double.parseDouble(bo.getPlan().getVALORADC_ONNET().replaceAll(",", ".").equals("88888888")? "0":bo.getPlan().getVALORADC_ONNET().replaceAll(",", ".")));
							}
                             
							cell4 = row.getCell(mapValues.get("QuantityValue")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("QuantityValue"));   
							cell4.setCellType(Cell.CELL_TYPE_NUMERIC);   
							cell4.setCellValue(1);

							cell4 = row.getCell(mapValues.get("Period"));
							if (cell4 == null)
								cell4 = row.createCell(mapValues.get("Period"));
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							cell4.setCellValue("Period N/A");

							fila++;
						}
						if(prit.getUsageCharge().getRSPR_EntryIndex()!=null){
							Row row = sheet.getRow(fila); 
							if(row==null)
								row = sheet.createRow(fila);

							Cell cell4 = row.getCell(mapValues.get("Price")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Price"));   
							cell4.setCellType(Cell.CELL_TYPE_STRING);   
							cell4.setCellValue(price.getName());

							cell4 = row.getCell(mapValues.get("Pricing Item")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Pricing Item"));   
							cell4.setCellType(Cell.CELL_TYPE_STRING);   
							cell4.setCellValue(prit.getUsageCharge().getName());

							cell4 = row.getCell(mapValues.get("Effective Date"));   
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Effective Date")); 
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							if(bo.getEffectiveDate()!= null){
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell4.setCellStyle(style);
								cell4.setCellValue(calendar);}
							else cell4.setCellValue("");

							cell4 = row.getCell(mapValues.get("Item Parameter")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Item Parameter"));   
							cell4.setCellType(Cell.CELL_TYPE_STRING);   
							cell4.setCellValue("Rate_based_on_scale_and_period_rate");

							cell4 = row.getCell(mapValues.get("Value")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("Value"));   
							cell4.setCellType(Cell.CELL_TYPE_NUMERIC);
							cell4.setCellValue(Double.parseDouble(bo.getPlan().getVALORADC_ONNET().replaceAll(",", ".").equals("88888888")? "0":bo.getPlan().getVALORADC_ONNET().replaceAll(",", ".")));

							cell4 = row.getCell(mapValues.get("QuantityValue")); 
							if (cell4 == null)   
								cell4 = row.createCell(mapValues.get("QuantityValue"));   
							cell4.setCellType(Cell.CELL_TYPE_NUMERIC);   
							cell4.setCellValue(1);

							cell4 = row.getCell(mapValues.get("Period"));
							if (cell4 == null)
								cell4 = row.createCell(mapValues.get("Period"));
							cell4.setCellType(Cell.CELL_TYPE_STRING);
							cell4.setCellValue("Period N/A");
							fila++;
						}						
						
					}
					if(prit.getRecurringCharge()!=null&&prit.getRecurringCharge().getQcc_entryIndex()!=null){
						Row row = sheet.getRow(fila); 
						if(row==null)
							row = sheet.createRow(fila);

						Cell cell5 = row.getCell(mapValues.get("Price")); 
						if (cell5 == null)
							cell5 = row.createCell(mapValues.get("Price"));   
						cell5.setCellType(Cell.CELL_TYPE_STRING);   
						cell5.setCellValue(price.getName());

						cell5 = row.getCell(mapValues.get("Pricing Item")); 
						if (cell5 == null)   
							cell5 = row.createCell(mapValues.get("Pricing Item"));   
						cell5.setCellType(Cell.CELL_TYPE_STRING);   
						cell5.setCellValue(prit.getRecurringCharge().getName());

						cell5 = row.getCell(mapValues.get("Effective Date"));   
						if (cell5 == null)   
							cell5 = row.createCell(mapValues.get("Effective Date")); 
						cell5.setCellType(Cell.CELL_TYPE_STRING);
						if(bo.getEffectiveDate()!=null){
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							Date date = sdf.parse(bo.getEffectiveDate());
							calendar.setTime(date);
							DataFormat fmt = wb.createDataFormat();
							CellStyle style = wb.createCellStyle();
							style.setAlignment(CellStyle.ALIGN_RIGHT);
							style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
							cell5.setCellStyle(style);
							cell5.setCellValue(calendar);}
						else cell5.setCellValue("");

						cell5 = row.getCell(mapValues.get("Item Parameter")); 
						if (cell5 == null)   
							cell5 = row.createCell(mapValues.get("Item Parameter"));   
						cell5.setCellType(Cell.CELL_TYPE_STRING);   
						cell5.setCellValue("Qualified_charge_codes");

						cell5 = row.getCell(mapValues.get("Value")); 
						if (cell5 == null)   
							cell5 = row.createCell(mapValues.get("Value"));   
						cell5.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell5.setCellValue(1);
						cell5 = row.getCell(mapValues.get("QuantityValue")); 
						if (cell5 == null)   
							cell5 = row.createCell(mapValues.get("QuantityValue"));   
						cell5.setCellType(Cell.CELL_TYPE_NUMERIC);   
						cell5.setCellValue(1);

						cell5 = row.getCell(mapValues.get("Charge code")); 
						if (cell5 == null)   
							cell5 = row.createCell(mapValues.get("Charge code"));   
						cell5.setCellType(Cell.CELL_TYPE_STRING);   
						cell5.setCellValue("RC_VOZ");
						
						fila++;
					}
					System.out.println("Pasa por aca .......por todos........ se acabooo");
					///--------------------- Itemparameter para buzoncorreodevoz-------------------------------------
					
				}
			}
		}
		
		Date now = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd");
		
		FileOutputStream fileOut = null;
		if(fin){
					if(!panama){
						fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
					}else{
						fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
					}
		}else{
				fileOut = new FileOutputStream("D://SalidasEPC/BOS_COL_"+sdf.format(now)+"_POS.xlsx");
		}
			
			
			
			
			wb.write(fileOut);   
			fileOut.close();
			return fila++;
	
	}
	
	private static void fillPeak_Unit_rate(Sheet sheet, int fila, HashMap<String, Integer> mapValues, Price price, Prit prit, BillingOffer bo, Workbook wb) throws Exception{
		

		Row row = sheet.getRow(fila); 
		if(row==null)
			row = sheet.createRow(fila);

		Cell cell3 = row.getCell(mapValues.get("Price")); 
		if (cell3 == null)   
			cell3 = row.createCell(mapValues.get("Price"));   
		cell3.setCellType(Cell.CELL_TYPE_STRING);   
		cell3.setCellValue(price.getName());

		cell3 = row.getCell(mapValues.get("Pricing Item")); 
		if (cell3 == null)
			cell3 = row.createCell(mapValues.get("Pricing Item"));   
		cell3.setCellType(Cell.CELL_TYPE_STRING);   
		cell3.setCellValue(prit.getUsageCharge().getName());

		cell3 = row.getCell(mapValues.get("Effective Date"));   
		if (cell3 == null)   
			cell3 = row.createCell(mapValues.get("Effective Date")); 
		cell3.setCellType(Cell.CELL_TYPE_STRING);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if(bo.getEffectiveDate()!=null){
		Date date = sdf.parse(bo.getEffectiveDate());
		calendar.setTime(date);
		DataFormat fmt = wb.createDataFormat();
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
		cell3.setCellStyle(style);
		cell3.setCellValue(calendar);}

		cell3 = row.getCell(mapValues.get("Item Parameter")); 
		if (cell3 == null)   
			cell3 = row.createCell(mapValues.get("Item Parameter"));   
		cell3.setCellType(Cell.CELL_TYPE_STRING);   
		cell3.setCellValue("Unit_rate");

		cell3 = row.getCell(mapValues.get("Value")); 
		if (cell3 == null)   
			cell3 = row.createCell(mapValues.get("Value"));   
		cell3.setCellType(Cell.CELL_TYPE_STRING);
		cell3.setCellValue(-1);
		

		cell3 = row.getCell(mapValues.get("QuantityValue")); 
		if (cell3 == null)   
			cell3 = row.createCell(mapValues.get("QuantityValue"));   
		cell3.setCellType(Cell.CELL_TYPE_NUMERIC);   
		cell3.setCellValue(1);

		cell3 = row.getCell(mapValues.get("Period")); 
		if (cell3 == null)   
			cell3 = row.createCell(mapValues.get("Period"));   
		cell3.setCellType(Cell.CELL_TYPE_STRING);
		cell3.setCellValue("Peak");
		
	}
	
	private static void fillPeak_Quota_per_period(Sheet sheet, int fila, HashMap<String, Integer> mapValues, Price price, Prit prit, BillingOffer bo, Workbook wb) throws Exception{
		

		Row row = sheet.getRow(fila); 
		if(row==null)
			row = sheet.createRow(fila);


		Cell cell1 = row.getCell(mapValues.get("Price")); 
		if (cell1 == null)   
			cell1 = row.createCell(mapValues.get("Price"));   
		cell1.setCellType(Cell.CELL_TYPE_STRING);   
		cell1.setCellValue(price.getName());
		
		cell1 = row.getCell(mapValues.get("Pricing Item")); 
		if (cell1 == null)   
			cell1 = row.createCell(mapValues.get("Pricing Item"));   
		cell1.setCellType(Cell.CELL_TYPE_STRING);   
		cell1.setCellValue(prit.getUsageCharge().getName());

		cell1 = row.getCell(mapValues.get("Effective Date"));   
		if (cell1 == null)   
			cell1 = row.createCell(mapValues.get("Effective Date")); 
		cell1.setCellType(Cell.CELL_TYPE_STRING);
		if(bo.getEffectiveDate()!=null){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse(bo.getEffectiveDate());
		calendar.setTime(date);
		DataFormat fmt = wb.createDataFormat();
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
		cell1.setCellStyle(style);
		cell1.setCellValue(calendar);}

		cell1 = row.getCell(mapValues.get("Item Parameter")); 
		if (cell1 == null)   
			cell1 = row.createCell(mapValues.get("Item Parameter"));   
		cell1.setCellType(Cell.CELL_TYPE_STRING);   
		cell1.setCellValue("Quota_per_period");

		cell1 = row.getCell(mapValues.get("Value")); 
		if (cell1 == null)   
			cell1 = row.createCell(mapValues.get("Value"));   
		cell1.setCellType(Cell.CELL_TYPE_STRING);
		cell1.setCellValue(0);
		
		cell1 = row.getCell(mapValues.get("QuantityValue")); 
		if (cell1 == null)   
			cell1 = row.createCell(mapValues.get("QuantityValue"));   
		cell1.setCellType(Cell.CELL_TYPE_NUMERIC);   
		cell1.setCellValue(1);

		cell1 = row.getCell(mapValues.get("Period")); 
		if (cell1 == null)   
			cell1 = row.createCell(mapValues.get("Period"));   
		cell1.setCellType(Cell.CELL_TYPE_STRING);   
		cell1.setCellValue("Peak");
		
	}

	public static int escribirValoresLDI(List<BillingOffer> boList, int fila, boolean panama, HashMap<String, Integer> mapValues) throws Exception{
		InputStream inp=null;
		if(!panama){
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
		}
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("Values");

		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);

		for(BillingOffer bo:boList){
			for(Price price:bo.getPriceList()){
				for(Prit prit:price.getPritList()){
					if(prit.getUsageCharge()!=null){
						for(AttrPaqLDI attrPaqLDI:bo.getPaqueteLDI().getAttrPaqLDIList()){
							if(prit.getUsageCharge().getQPP_EntryIndex()!=null){
								Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell = row.getCell(mapValues.get("Price")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Price"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(price.getName());

								cell = row.getCell(mapValues.get("Pricing Item")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Pricing Item"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getName());

								cell = row.getCell(mapValues.get("Effective Date"));   
								if (cell == null)   
									cell = row.createCell(mapValues.get("Effective Date")); 
								cell.setCellType(Cell.CELL_TYPE_STRING);
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell.setCellStyle(style);
								cell.setCellValue(calendar);

								cell = row.getCell(mapValues.get("Item Parameter")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Item Parameter"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Quota_per_period");

								cell = row.getCell(mapValues.get("Value")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Value"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);
								cell.setCellValue(attrPaqLDI.getALLOWANCE()==null?"":attrPaqLDI.getALLOWANCE().toString());

								cell = row.getCell(mapValues.get("QuantityValue")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("QuantityValue"));   
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell.setCellValue(1);

								cell = row.getCell(mapValues.get("Period")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Period"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Period N/A");

								fila++;
							}
							if(prit.getUsageCharge().getURI_EntryIndex()!=null){
								Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell = row.getCell(mapValues.get("Price")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Price"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(price.getName());

								cell = row.getCell(mapValues.get("Pricing Item")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Pricing Item"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getName());

								cell = row.getCell(mapValues.get("Effective Date"));  
								if (cell == null)   
									cell = row.createCell(mapValues.get("Effective Date")); 
								cell.setCellType(Cell.CELL_TYPE_STRING);
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell.setCellStyle(style);
								cell.setCellValue(calendar);

								cell = row.getCell(mapValues.get("Item Parameter")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Item Parameter"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Unit_rate");

								if(attrPaqLDI.getVLR_MIN()!=null){
									cell = row.getCell(mapValues.get("Value")); 
									if (cell == null)   
										cell = row.createCell(mapValues.get("Value"));   
									cell.setCellType(Cell.CELL_TYPE_STRING);
									cell.setCellValue(attrPaqLDI.getVLR_MIN());
								}

								cell = row.getCell(mapValues.get("QuantityValue")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("QuantityValue"));   
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell.setCellValue(1);

								cell = row.getCell(mapValues.get("Period")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Period"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Period N/A");

								fila++;
							}
							if(prit.getUsageCharge().getRTR_EntryIndex()!=null){
								Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell = row.getCell(mapValues.get("Price")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Price"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(price.getName());

								cell = row.getCell(mapValues.get("Pricing Item")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Pricing Item"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getName());

								cell = row.getCell(mapValues.get("Effective Date"));   
								if (cell == null)
									cell = row.createCell(mapValues.get("Effective Date")); 
								cell.setCellType(Cell.CELL_TYPE_STRING);
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell.setCellStyle(style);
								cell.setCellValue(calendar);

								cell = row.getCell(mapValues.get("Item Parameter")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Item Parameter"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Rate_table_rate");

								if(attrPaqLDI.getVLR_MIN()!=null){
									cell = row.getCell(mapValues.get("Value")); 
									if (cell == null)   
										cell = row.createCell(mapValues.get("Value"));   
									cell.setCellType(Cell.CELL_TYPE_STRING);
									cell.setCellValue(attrPaqLDI.getVLR_MIN());
								}

								cell = row.getCell(mapValues.get("QuantityValue")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("QuantityValue"));   
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell.setCellValue(1);

								cell = row.getCell(mapValues.get("Period")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Period"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Period N/A");

								cell = row.getCell(mapValues.get("Zone")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Zone"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(attrPaqLDI.getZONA());

								fila++;
							}
						}
					}
				}
			}
		}
		FileOutputStream fileOut = null;
		if(!panama){
			fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
		}
		wb.write(fileOut);   
		fileOut.close();
		return fila++;
	}
	
	public static int escribirValoresDemanda(List<BillingOffer> boList, int fila, boolean panama, HashMap<String, Integer> mapValues, boolean incre, boolean prep) throws Exception{
		InputStream inp=null;
		if(!panama){
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		}else{
			inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
		}
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("Values");

		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);

		for(BillingOffer bo:boList){
			for(Price price:bo.getPriceList()){
				for(Prit prit:price.getPritList()){
					if(prit.getUsageCharge()!=null && prit.getUsageCharge().getLstValue() != null){
						for(Value attrValue :prit.getUsageCharge().getLstValue()){
							
							if(prit.getUsageCharge().getRTR_EntryIndex()!=null){
								Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell = row.getCell(mapValues.get("Price")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Price"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(price.getName());

								cell = row.getCell(mapValues.get("Pricing Item")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Pricing Item"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getUsageCharge().getName());

								cell = row.getCell(mapValues.get("Effective Date"));   
								if (cell == null)
									cell = row.createCell(mapValues.get("Effective Date")); 
								cell.setCellType(Cell.CELL_TYPE_STRING);
								if(bo.getEffectiveDate()!=null){
									Calendar calendar = Calendar.getInstance();
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									Date date = sdf.parse(bo.getEffectiveDate());
									calendar.setTime(date);
									DataFormat fmt = wb.createDataFormat();
									CellStyle style = wb.createCellStyle();
									style.setAlignment(CellStyle.ALIGN_RIGHT);
									style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
									cell.setCellStyle(style);
									cell.setCellValue(calendar);}
								else cell.setCellValue("");

								cell = row.getCell(mapValues.get("Item Parameter")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Item Parameter"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Rate_table_rate");

								if(attrValue.getValue()!=null){
									cell = row.getCell(mapValues.get("Value")); 
									if (cell == null)   
										cell = row.createCell(mapValues.get("Value"));   
									cell.setCellType(Cell.CELL_TYPE_STRING);
									cell.setCellValue(Double.parseDouble(attrValue.getValue().replaceAll(",", ".")));
								}

								cell = row.getCell(mapValues.get("QuantityValue")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("QuantityValue"));   
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell.setCellValue(1);

								cell = row.getCell(mapValues.get("Period")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Period"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Period N/A");

								cell = row.getCell(mapValues.get("Rate group")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Rate group"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(attrValue.getRate_group());

								fila++;
							}
						}
					}
			/**
			 * Cambio para insetar valores para la oferta BO Consulta Saldo 21/01/2013----		
			 */					
					if(prit.getOneTimeCharge()!=null && (prit.getOneTimeCharge().getPit().equals("**OC CM activity")||prit.getOneTimeCharge().getRole().equals("OC Discount"))){
						OneTimeCharge OTC = prit.getOneTimeCharge();
						//for(OneTimeCharge attrValue :prit.getOneTimeCharge()){
							if(prit.getOneTimeCharge().getEntryIndex()!=null){
								Row row = sheet.getRow(fila); 
								if(row==null)
									row = sheet.createRow(fila);

								Cell cell = row.getCell(mapValues.get("Price")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Price"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(price.getName());

								cell = row.getCell(mapValues.get("Pricing Item")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Pricing Item"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue(prit.getOneTimeCharge().getName());

								cell = row.getCell(mapValues.get("Effective Date"));   
								if (cell == null)
									cell = row.createCell(mapValues.get("Effective Date")); 
								cell.setCellType(Cell.CELL_TYPE_STRING);
								if(bo.getEffectiveDate()!=null){
								Calendar calendar = Calendar.getInstance();								
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date date = sdf.parse(bo.getEffectiveDate());
								calendar.setTime(date);
								DataFormat fmt = wb.createDataFormat();
								CellStyle style = wb.createCellStyle();
								style.setAlignment(CellStyle.ALIGN_RIGHT);
								style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
								cell.setCellStyle(style);
								cell.setCellValue(calendar);}

								cell = row.getCell(mapValues.get("Item Parameter")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Item Parameter"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								
								if(OTC.getRole().equals("OC Discount"))
									cell.setCellValue("Qualified_charge_codes");
								else cell.setCellValue("Rate_table");

								
								cell = row.getCell(mapValues.get("Value")); 
								if (cell == null)   
								   	cell = row.createCell(mapValues.get("Value"));   
									cell.setCellType(Cell.CELL_TYPE_STRING);
									if(OTC.getVALUE()!=null)
									     cell.setCellValue(OTC.getVALUE());
									else cell.setCellValue("");

								cell = row.getCell(mapValues.get("QuantityValue")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("QuantityValue"));   
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
								cell.setCellValue("");

								cell = row.getCell(mapValues.get("Period")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Period"));   
								cell.setCellType(Cell.CELL_TYPE_STRING);   
								cell.setCellValue("Period N/A");

								cell = row.getCell(mapValues.get("Charge code")); 
								if (cell == null)   
									cell = row.createCell(mapValues.get("Charge code"));   
								cell.setCellType(Cell.CELL_TYPE_STRING); 
								if(OTC.getRole().contains("OC Discount")&&OTC.getCC_AlQUEDESCONT()!=null)cell.setCellValue(OTC.getCC_AlQUEDESCONT());
								else cell.setCellValue(OTC.getOneTimeCC());
								fila++;
							}
					//	}
					}				
				}
			}
		}
		
		Date now = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd");
		
		FileOutputStream fileOut = null;
		if(prep){
			if(!panama){
				if(incre)fileOut = new FileOutputStream("D://SalidasEPC/BOS_COL_"+sdf.format(now)+"_PreRO_PROD.xlsx");
					else fileOut = new FileOutputStream("D://SalidasEPC/BOS_COL_"+sdf.format(now)+"_PreRO_.xlsx");
			}else{
					fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
				 }
		}else{
				if(!panama){
					fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
				}else{
					fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
					 }		
		}
			
		wb.write(fileOut);   
		fileOut.close();
		return fila++;
	}
	
	public static void escribirValoresPrepago(List<ValuesPrepago> vp, int fila, boolean panama, HashMap<String, Integer> mapValues, boolean incre) throws InvalidFormatException, IOException, ParseException{
		InputStream inp = new FileInputStream("D://SalidasEPC/AMX EPC Billing Offers Version 9.xlsx");
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("Values");
       for(ValuesPrepago values : vp){
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);
		Row row = sheet.getRow(fila); 
		if(row==null)
			row = sheet.createRow(fila);

		Cell cell = row.getCell(mapValues.get("Price")); 
		if (cell == null)   
			cell = row.createCell(mapValues.get("Price"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue(values.getPRICE());

		cell = row.getCell(mapValues.get("Pricing Item")); 
		if (cell == null)   
			cell = row.createCell(mapValues.get("Pricing Item"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue(values.getPRICING_ITEM());
		
		/*cell = row.getCell(mapValues.get("Effective Date"));   
		if (cell == null)
			cell = row.createCell(mapValues.get("Effective Date")); 
		cell.setCellType(Cell.CELL_TYPE_STRING);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-mmm-yyyy hh:mm:ss");
		Date date = sdf.parse(values.getEFFECTIVE_DATE());
		calendar.setTime(date);
		DataFormat fmt = wb.createDataFormat();
		CellStyle style = wb.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setDataFormat(fmt.getFormat("dd/mm/yyyy"));
		cell.setCellStyle(style);
		cell.setCellValue(calendar);*/
		cell = row.getCell(mapValues.get("Effective Date"));  
		if (cell == null)
			cell = row.createCell(mapValues.get("Effective Date")); 
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue(values.getEFFECTIVE_DATE());

		cell = row.getCell(mapValues.get("Item Parameter")); 
		if (cell == null)   
			cell = row.createCell(mapValues.get("Item Parameter"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue(values.getITEM_PARAMETER());

		if(values.getVALUE()!=null){
			cell = row.getCell(mapValues.get("Value")); 
			if (cell == null)   
				cell = row.createCell(mapValues.get("Value"));   
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue(Double.parseDouble(values.getVALUE().replaceAll(",", ".")));
		}

		cell = row.getCell(mapValues.get("SIM SKU")); 
		if (cell == null)   
			cell = row.createCell(mapValues.get("SIM SKU"));   
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);   
		cell.setCellValue(values.getSIM_SKU());

		cell = row.getCell(mapValues.get("Period")); 
		if (cell == null)   
			cell = row.createCell(mapValues.get("Period"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue("Period N/A");		
		fila++;					
       }
       Date now = new Date();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd");
       
       FileOutputStream fileOut = null;
		if(!panama){
			if(incre)fileOut = new FileOutputStream("D://SalidasEPC/BOS_COL_"+sdf.format(now)+"_PreRO_PROD.xlsx");
			else fileOut = new FileOutputStream("D://SalidasEPC/BOS_COL_"+sdf.format(now)+"_PreRO_.xlsx");
		}else{
			fileOut = new FileOutputStream("D://SalidasEPC/AMX EPC Billing Offers Panama Version 1.xlsx");
		}
		wb.write(fileOut);   
		fileOut.close();	
		
	}
	/**
	 * Método que escribe las tablas de traducción
	 * @param boList: Listado de Billing Offers
	 * @param ttList: Listado de tabla de traducción de TRANS_BILLOFF
	 * @param panama: Bandera que indica si la migración es de Colombia o de Panamá
	 * @param model: Instancia de la capa de modelo de la aplicación
	 * @throws Exception
	 */
	public static void escribirTT(List<BillingOffer> boList, List<TablaTraduccion> ttList, List<TablaTraduccion> ttPO , boolean panama, IDataModel model, List<servicio_comp> infoComp, boolean increm) throws Exception{
		System.out.println("Escribiendo TT");
		InputStream inp=null;
		if(!panama){
			inp = new FileInputStream("D://EntradasEPC/AMX PRICING TTs v2 4.xlsm");
		}else{
			inp = new FileInputStream("D://EntradasEPC/AMX PRICING TTs Panama Version 1.xlsm");
		}
		Workbook wb = WorkbookFactory.create(inp);   
		CellStyle borderStyle = createBorderedStyle(wb);
		Sheet sheet = wb.getSheet("TRANS_OFFERMC");
		int fila=3;
	     /**
         * Adecuacion de generacion de hoja trans_offermac de las tts 
         * Tomando en cuenta solo la tabla de planes_migracion
         * y la tabla de boffer  09-01-2013 ------------
         */
for(TablaTraduccion  tt:  ttPO){			
			
			Row row = sheet.getRow(fila); 
			if(row==null)
				row = sheet.createRow(fila);
			
			Cell cell = row.getCell(0); 
			if (cell == null)   
				cell = row.createCell(0);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue(1);

			cell = row.getCell(1);
			if (cell == null)
				cell = row.createCell(1);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellStyle(borderStyle);
			cell.setCellValue("COLOMBIA");

			cell = row.getCell(2);
			if (cell == null)   
				cell = row.createCell(2);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue("TRANS_OFFERMC");

			cell = row.getCell(3);
			if (cell == null)   
				cell = row.createCell(3);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getTmcode());
			
			cell = row.getCell(4);
			if (cell == null)   
				cell = row.createCell(4);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);
			if(tt.getSpcode()!=null)
			    cell.setCellValue(tt.getSpcode());
			
			cell = row.getCell(13);
			if (cell == null)   
				cell = row.createCell(13);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getDesc_bo());
			
			cell = row.getCell(14);
			if (cell == null)   
				cell = row.createCell(14);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("AMX_Wireless");
			
			cell = row.getCell(15);
			if (cell == null)   
				cell = row.createCell(15);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("Wireless_Main");
			
			cell = row.getCell(16);
			if (cell == null)   
				cell = row.createCell(16);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("Technical PP");
			
			cell = row.getCell(17);
			if (cell == null)   
				cell = row.createCell(17);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getDesc_bo());
			
			cell = row.getCell(18);
			if (cell == null)   
				cell = row.createCell(18);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle); 
			if(tt.getDescripcion()!=null){
					if(tt.getDescripcion().contains("N/A")||tt.getDescripcion().contains("ERROR:")||tt.getDesc_bo().contains("Clientes Facturas Abiertas"))
					     cell.setCellValue("");
					else cell.setCellValue("X");}
			else cell.setCellValue("");
			
			cell = row.getCell(19);
			if (cell == null)   
				cell = row.createCell(19);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getFecha_venta());	

			fila++;
		}	

		/**
		 * terminacion de codigo para hoja de trans_offermac 
		 *       -------      09-01-2013   -------------
		 */
		
  	sheet = wb.getSheet("TRANS_BILLOFF");
	fila=3;
		/** 
		 * Cambio para insertar ciclo for para comparar los tmcode y los sncode  
		 * y no repetir llave para planes solo asignacion de basicos 
		 */
    String temporal = " ";	
    for(TablaTraduccion tt:ttList){
	       if ( tt.getTmcode()==null ) {
		} else {
		}
	       if ( tt.getSncode()==null ) {
		} else {
		}	
	       
			Row row = sheet.getRow(fila); 
			if(row==null)
				row = sheet.createRow(fila);
			Cell cell = row.getCell(0); 
			if (cell == null)
				cell = row.createCell(0);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue(1);

			cell = row.getCell(1);
			if (cell == null)   
				cell = row.createCell(1);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellStyle(borderStyle);
			cell.setCellValue("COLOMBIA");

			cell = row.getCell(2);
			if (cell == null)   
				cell = row.createCell(2);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue("TRANS_BILLOFF");

			cell = row.getCell(3);
			if (cell == null)   
				cell = row.createCell(3);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);
	
			/** ---- Cambio para introducir en null el valor de cero  ***/
			
			if(tt.getTmcode()!=null)
				 cell.setCellValue(tt.getTmcode());
			   
			else cell.setCellValue(0);

			cell = row.getCell(4);
			if (cell == null)   
				cell = row.createCell(4);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);   
			if(tt.getCaract_eleg()!=null&&tt.getCaract_eleg()!="0"&&!tt.getDesc_bo().contains("Clientes Facturas Abiertas")){
				//String strEleg = tt.getCaract_eleg().replaceAll(",ELEP3","").replaceAll(",ELEP6", "").replaceAll("ELEI6", "").replaceAll("ELEI9", "");
				cell.setCellValue(tt.getCaract_eleg());		
			}else{
				if(tt.getSpcode()!=null)
				cell.setCellValue(tt.getSpcode());
				else cell.setCellValue("");
				}
			cell = row.getCell(5);
			if (cell == null)
				cell = row.createCell(5);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getSncode());

			cell = row.getCell(6);
			if (cell == null)   
				cell = row.createCell(6);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);  
			if(tt.getPlan()!=null)
				cell.setCellValue(tt.getPlan());

			cell = row.getCell(7);
			if (cell == null)   
				cell = row.createCell(7);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getPaquete());

			cell = row.getCell(8);
			if (cell == null)   
				cell = row.createCell(8);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getDescripcion());

			cell = row.getCell(9);
			if (cell == null)   
				cell = row.createCell(9);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);
			 if (tt.getCFM()!=null)
				 cell.setCellValue(tt.getCFM());
			 else cell.setCellValue("");
			
			 cell = row.getCell(10);
			if (cell == null)   
				cell = row.createCell(10);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(11);
			if (cell == null)   
				cell = row.createCell(11);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(12);
			if (cell == null)   
				cell = row.createCell(12);
			cell.setCellStyle(borderStyle);

			cell = row.getCell(13);
			if (cell == null)   
				cell = row.createCell(13);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getDesc_bo());
            /** temporal para tener el descriptor de plan anterior **/
			if(tt.getDesc_bo().length()>= 13){
			temporal = tt.getDesc_bo();
			int val = temporal.length();
			val = val - 13;
			temporal = temporal.substring(0, val);}
			if(tt.getTmcode()== null ) {
			} else {
			}
			if(tt.getSncode()== null) {
			} else {
			}	 
			
			cell = row.getCell(14);
			if (cell == null)   
				cell = row.createCell(14);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tt.getFecha_venta());			
			fila++;
     	   }  
     /**
      * Codigo para incorporar hoja de TRANS_COMPS - 10/04/2013-----
      * 
      */
 /*     sheet = wb.getSheet("TRANS_COMPS");
	  fila=3;
       for (servicio_comp sc: infoComp){
    	   Row row = sheet.getRow(fila); 
			if(row==null)
				row = sheet.createRow(fila);
			Cell cell = row.getCell(0); 
			if (cell == null)
				cell = row.createCell(0);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue(1);

			cell = row.getCell(1);
			if (cell == null)   
				cell = row.createCell(1);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellStyle(borderStyle);
			cell.setCellValue("COLOMBIA");

			cell = row.getCell(2);
			if (cell == null)   
				cell = row.createCell(2);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue("TRANS_COMPS");

			cell = row.getCell(3);
			if (cell == null)   
				cell = row.createCell(3);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);
	        cell.setCellValue(sc.getSNCODE());			

			cell = row.getCell(4);
			if (cell == null)   
				cell = row.createCell(4);
			cell.setCellStyle(borderStyle);   
			if(sc.getDESC_SNCODE()!=null)
				 cell.setCellValue(sc.getDESC_SNCODE());		
			else cell.setCellValue("");				
				
			cell = row.getCell(5);
			if (cell == null)
				cell = row.createCell(5);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("");

			cell = row.getCell(6);
			if (cell == null)   
				cell = row.createCell(6);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);  
				cell.setCellValue("");

			cell = row.getCell(7);
			if (cell == null)   
				cell = row.createCell(7);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("");

			cell = row.getCell(8);
			if (cell == null)   
				cell = row.createCell(8);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("");

			cell = row.getCell(9);
			if (cell == null)   
				cell = row.createCell(9);
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue("");
			
			 cell = row.getCell(10);
			if (cell == null)   
				cell = row.createCell(10);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(11);
			if (cell == null)   
				cell = row.createCell(11);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(12);
			if (cell == null)   
				cell = row.createCell(12);
			cell.setCellStyle(borderStyle);

			cell = row.getCell(13);
			if (cell == null)   
				cell = row.createCell(13);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(sc.getCOMP_CODE());
          	fila++;   	   
       }    */
    	Date now = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMMdd");
    
		FileOutputStream fileOut = null;
		if(!panama){
			if(increm)
				fileOut = new FileOutputStream("D://SalidasEPC/TTS_COL_"+sdf.format(now)+"_PreRO_PROD.xlsm");
			else
				fileOut = new FileOutputStream("D://SalidasEPC/TTS_COL_"+sdf.format(now)+"_PreRO.xlsm");			
			
		}else{
			fileOut = new FileOutputStream("D://SalidasEPC/AMX PRICING TTs v2 4 Panama.xlsm" );
		}
		wb.write(fileOut);   
		fileOut.close();		
		model.truncateTTS();
		model.insertTTBO(ttList);
		model.insertTTPO(ttPO);
		
	}

	public static int evaluaPlanAsoc(Row row, Sheet sheet, BillingOffer bo, int fila, CellStyle borderStyle, List<Integer> list){
		for(Integer tmcode:list){
			row = sheet.getRow(fila); 
			if(row==null)
				row = sheet.createRow(fila);
			Cell cell = row.getCell(0); 
			if (cell == null)   
				cell = row.createCell(0);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue(1);

			cell = row.getCell(1);
			if (cell == null)   
				cell = row.createCell(1);   
			cell.setCellType(Cell.CELL_TYPE_STRING);   
			cell.setCellStyle(borderStyle);
			cell.setCellValue("COLOMBIA");

			cell = row.getCell(2);
			if (cell == null)   
				cell = row.createCell(2);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);
			cell.setCellValue("TRANS_OFFERMC");
			
			cell = row.getCell(3);
			if (cell == null)   
				cell = row.createCell(3);   
			cell.setCellType(Cell.CELL_TYPE_NUMERIC); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(tmcode);

			cell = row.getCell(4);
			if (cell == null)   
				cell = row.createCell(4);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(5);
			if (cell == null)   
				cell = row.createCell(5);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(6);
			if (cell == null)   
				cell = row.createCell(6);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(7);
			if (cell == null)   
				cell = row.createCell(7);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(8);
			if (cell == null)   
				cell = row.createCell(8);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(9);
			if (cell == null)   
				cell = row.createCell(9);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(10);
			if (cell == null)   
				cell = row.createCell(10);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(11);
			if (cell == null)   
				cell = row.createCell(11);
			cell.setCellStyle(borderStyle);
			cell = row.getCell(12);
			if (cell == null)   
				cell = row.createCell(12);
			cell.setCellStyle(borderStyle);

			cell = row.getCell(13);
			if (cell == null)   
				cell = row.createCell(13);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(bo.getName());

			cell = row.getCell(14);
			if (cell == null)   
				cell = row.createCell(14);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("AMX_Wireless");

			cell = row.getCell(15);
			if (cell == null)   
				cell = row.createCell(15);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("Wireless_Main");

			cell = row.getCell(16);
			if (cell == null)   
				cell = row.createCell(16);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue("Technical PP");

			cell = row.getCell(17);
			if (cell == null)   
				cell = row.createCell(17);   
			cell.setCellType(Cell.CELL_TYPE_STRING); 
			cell.setCellStyle(borderStyle);   
			cell.setCellValue(bo.getName());

			cell = row.getCell(18);
			if (cell == null)   
				cell = row.createCell(18);   
			cell.setCellStyle(borderStyle);  
			if(!bo.getPlan().getPROMOCION().equals("N/A")){
				cell.setCellType(Cell.CELL_TYPE_STRING);  
				cell.setCellValue("X");
			}
			fila++;
		}
		return fila;
	}
    
	public static void GeneraTablaRev(List<BillingOffer> boListRev, String Tipo_BO, IDataModel model){
		
		List<BORevision>  LBOrev = new ArrayList<BORevision>();
		
		BORevision RBO = new BORevision();
		
		for(BillingOffer rev:boListRev){
			
			for(Price  pr: rev.getPriceList()){
				
				for(Prit  prt: pr.getPritList()){
					RBO = new BORevision();
					RBO.setTIPO_BO(Tipo_BO);
					RBO.setNOMBRE_PRICE(pr.getName());
					RBO.setNOMBRE_BO(rev.getName());
					System.out.println(pr.getName()+' '+rev.getName() );
			        RBO.setVERSION("Version_"+new Date());
					if(prt.getUsageCharge()!=null){
						          RBO.setNOMBRE_PRIT(prt.getUsageCharge().getName());
						          RBO.setTIPO_PRIT("UC");
						          if(prt.getUsageCharge().getRole()!=null)
						             RBO.setROLE_PRIT(prt.getUsageCharge().getRole());
					}
					if(prt.getRecurringCharge()!=null){
						          RBO.setNOMBRE_PRIT(prt.getRecurringCharge().getName());
						          RBO.setTIPO_PRIT("RC");
						          if(prt.getRecurringCharge().getRole()!=null)
						             RBO.setROLE_PRIT(prt.getRecurringCharge().getRole());
					}
					if(prt.getOneTimeCharge()!=null){
						          RBO.setNOMBRE_PRIT(prt.getOneTimeCharge().getName());
						          RBO.setTIPO_PRIT("OC");
						          if(prt.getOneTimeCharge().getRole()!=null)
						             RBO.setROLE_PRIT(prt.getOneTimeCharge().getRole());
					}
					
					if(rev.getPlan()!=null){
							if(rev.getPlan().getTMCODE()!=null)
							      RBO.setTMCODE(rev.getPlan().getTMCODE());
							if(rev.getPlan().getSPCODE()!=null)
							      RBO.setSPCODE(rev.getPlan().getSPCODE());
							}					
					if(rev.getPaquete()!=null){
						  if(rev.getPaquete().getSPCODE()!=null)
							      RBO.setSPCODE(rev.getPaquete().getSPCODE());
						  if(rev.getPaquete().getSNCODE()!=null)
							      RBO.setSNCODE(rev.getPaquete().getSNCODE());
							        		  }
					 LBOrev.add(RBO);		
				               }	
				     
					}
		     }	
		
		if(Tipo_BO.equals("PL")) 
		    model.insertBOREVplan(LBOrev);
		if(Tipo_BO.equals("PQ")) 
			 model.insertBOREVPaq(LBOrev);
	}
	
	/**
	 * Método que mapea los números de columna con el nombre de la columna de la hoja values
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, Integer> obtnerNumColsValues() throws Exception{
		InputStream inp = new FileInputStream("D://EntradasEPC/AMX EPC Billing Offers Version 9.xlsx");   
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet("Values");
        
		Row row = sheet.getRow(0);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Cell cell : row) {
			if(cell!=null)
				map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
		}
		return map;
	}
	public static HashMap<String, Integer> obtnerNumColsNewOffersAmdocs(String hoja) throws Exception{
		InputStream inp = new FileInputStream("D://EntradasEPC/METRICAS_EPC.xlsx");   
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet(hoja);       
		Row row = sheet.getRow(1);
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (Cell cell : row) {
			if(cell!=null)
				map.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
		}
		return map;
	}
	

	
	public static void GenArchTTS_BO_TOTAL(IDataModel model) {
		
		JFrame ventana = new JFrame("Mi Ventana");
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    JTextArea textArea = new JTextArea(30, 80);		
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		List<CompararTTS_BO>   compararPaq = new ArrayList<CompararTTS_BO>();
		List<CompararTTS_BO>   compararPlan = new ArrayList<CompararTTS_BO>();
		
		compararPaq = model.getcomparPAQTTS_BO();
		compararPlan = model.comparPLANTTS_BO();
		  
		  
		List<TOTALES_SEG_CAL>  totalesCal = new ArrayList<TOTALES_SEG_CAL>();
		   model.insertCalidad();
		   totalesCal = model.generaTotalesCalidad();
		   
		  File f;
			   f = new File("D:/Log_BO_TTS_TOTALES.txt");	 
		
		try{    
			  System.out.println("pasa por aca ");
			 	FileWriter w = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(w);
				PrintWriter wr = new PrintWriter(bw);  
				wr.write("Informe de Totales Generados en version "+ new Date()+"  \n");
				System.out.println("pasa por aca 1 ");
				int cont = 0;//escribimos en el archivo
				int cont1 = 0;
				for(TOTALES_SEG_CAL totales:totalesCal ){
					 wr.println();
					 wr.println();					 
					 wr.append("\n Total Planes en Tabla Planes: "+ totales.getTOTAL_REG_PLANES()+"   \n");
					 wr.println();
					 textArea.setText("\n Total Planes en Tabla Planes: "+ totales.getTOTAL_REG_PLANES()+"   \n");					
					
					 wr.append("\n Total Paquetes en Tabla de Paquetes: "+ totales.getTOTAL_REG_PAQ()+"   \n");
					 wr.println();
					 textArea.append("\n Total Paquetes en Tabla Paquetes: "+ totales.getTOTAL_REG_PAQ()+"   \n");					
					
					 wr.println();
					 wr.append("\n Total Paquetes a Migrar en  Postpago: "+ totales.getTOT_PAQ_MIG() +"   \n");
					 wr.println();
					 textArea.append("\n Total Paquetes a Migrar en Postpago: "+ totales.getTOT_PAQ_MIG() +"   \n");
					 
					 wr.append("\n Total Planes a Migrar en  Postpago: "+ totales.getTOT_PLAN_MIG() +"   \n");
					 wr.println();
					 textArea.append("\n Total Planes a Migrar en  Postpago: "+ totales.getTOT_PLAN_MIG() +"   \n");
					
					 wr.append("\n Numero Total de Servicios PostPago: "+ totales.getTot_boservpost() +"   \n");
					 wr.println();
					 textArea.append("\n Numero Total de Servicios PostPago: "+totales.getTot_boservpost()+"   \n");				 
					 
					 wr.append("\n Numero Total de Ofertas Demanda PostPago: "+ totales.getTot_bodempost() +"   \n");
					 wr.println();
					 textArea.append("\n Numero Total de Servicios PostPago : "+totales.getTot_bodempost() +"   \n");
					 wr.println();					 
					 wr.append("\n               INFORME TOTALES DE GENERACION DE OFERTAS PREPAGO             ");
					 wr.println();
					 wr.println();
					 wr.append("\n Total de planes prepago a migrar:: "+ totales.getTot_planprepmig()+"   \n");
					 wr.println();
					 textArea.append("\n Total de planes prepago a migrar: "+totales.getTot_planprepmig()+"   \n");
					 
					 wr.append("\n Total Ofertas Demanda Prepago: "+ totales.getTot_bodemprep()+"   \n");
					 wr.println();
					 textArea.append("\n Total Ofertas Demanda Prepago: "+totales.getTot_bodemprep()+"   \n");
								 
					 wr.append("\n Total Servicios Prepago: "+ totales.getTot_boservprep()+"   \n");
					 wr.println();
					 textArea.append("\n Total Servicios Prepago: "+ totales.getTot_boservprep()+"   \n");
					 
					 wr.append("\n Total Adicionales y/o paquetes Prepago: "+ totales.getTot_boadiciprep()+"   \n");
					 wr.println();
					 textArea.append("\n  Total Adicionales y/o paquetes Prepago: "+ totales.getTot_boadiciprep()+"   \n");					 					
					 wr.println();
					 
					 wr.append("\n Total BO de Planes: "+ totales.getTOTAL_BO_PLAN() +"   \n");
					 wr.println();
					 textArea.append("\n Total BO de Planes: "+ totales.getTOTAL_BO_PLAN() +"   \n");				 
					 
					 wr.append("\n Total BO paquetes y servicios: "+ totales.getTOTAL_BO_PAQ() +"   \n");
					 wr.println();
					 textArea.append("\n Total BO paquetes y servicios: "+ totales.getTOTAL_BO_PAQ() +"   \n");
					 
					 wr.append("\n Numero Total de BillingOffers Generadas: "+ totales.getTOTAL_BO() +"   \n");
					 wr.println();
					 textArea.append("\n Numero Total de BillingOffers Generadas: "+ totales.getTOTAL_BO() +"   \n");				 
					 
					//------------------------------------------------------------------------------------------
					 		 
					 wr.append("\n Total BO Plan en TTS: "+ totales.getTOT_BO_PLTTS() +"   \n");
					 wr.println();
					 textArea.append("\n Total BO Plan en TTS: "+ totales.getTOT_BO_PLTTS() +"   \n");
					 
					 wr.append("\n Total BO Paquetes en TTS: "+ totales.getTOT_BO_PQTTS()+"   \n");
					 wr.println();
					 textArea.append("\n Total BO Paquetes en TTS: "+ totales.getTOT_BO_PQTTS()+"   \n");
					 
					 wr.append("\n Total Product Offering en TTS: "+ totales.getTOT_PO_TTS() +"   \n");
					 wr.println();
					 textArea.append("\n Total PO en TTS: "+ totales.getTOT_PO_TTS() +"   \n");					 
				 }				
				JScrollPane scrollPane = new JScrollPane(textArea);
				ventana.getContentPane().add(scrollPane, BorderLayout.CENTER);				
				JButton salir = new JButton("Salir");
				salir.addActionListener(new ActionListener() {					 
		            public void actionPerformed(ActionEvent e)
		            {
		            	System.exit(0); 
		            }
		        }); ;
				ventana.getContentPane().add(salir, BorderLayout.SOUTH);
				ventana.add(scrollPane);
				ventana.pack();
				ventana.setVisible(true);
				JOptionPane.showMessageDialog(null, "Numero de planes no incluidos en tabla maestro: "+ cont +"\n Numero de paquetes no incluidos en tabla maestro: "+cont1);
				wr.close();
				bw.close();
		}catch(IOException e){
			e.printStackTrace();
		}; 		
		 File tts = new File("D:/ConciliacionTTS_BO.txt");	 
		try{
			FileWriter w = new FileWriter(tts);
			BufferedWriter bw = new BufferedWriter(w);
			PrintWriter wr = new PrintWriter(bw);
			    wr.write("       Informe de Comparacion TTS versus Boffers y Product Offering Generadas en version  "+ new Date()+"  \n");
			    wr.println();
				wr.println();
				wr.append("            Diferencia entre  Billing Offers de Paquetes Generados y Billing Offers mapedas en TTS: ");
				wr.println();
				wr.println();
				
				if(compararPaq.size() > 0){
					    for(CompararTTS_BO  comPaq:compararPaq ){
							    wr.println();	
								wr.append("   SERVICIO/ADIC/PAQUETE: "+comPaq.getDESC_BO() + " -----  SPCODE: "+ comPaq.getSPCODE()+ " SNCODE: "+ comPaq.getSNCODE());
								wr.println();	
					    		}
			    		}
				else {
								wr.println();	
								wr.append(" No existe ninguna diferencia en BO  de paquetes");
								wr.println();
					
						}
				wr.println();
				wr.println();
				wr.append("            Diferencia entre Billing Offers de Plan Generadas  y  Product offering mapedas en TTS: ");
				wr.println();
				wr.println();				
				if(compararPlan.size() > 0){		 	
					    for(CompararTTS_BO comPlan:compararPlan){
					    	  	wr.println();
					    		wr.append("  PLAN:  "+comPlan.getDESC_BO() + "  tmcode: "+ comPlan.getTMCODE()+ " spcode: "+ comPlan.getSPCODE());
					    		wr.println();
					    }				
				}else{
								wr.println();	
								wr.append(" No existe ninguna diferencia en BO de planes");
								wr.println();
					
				}
			
			for(TOTALES_SEG_CAL TTs:totalesCal ){
			wr.println();
			wr.println();
			wr.append("               INFORME COMPARACION DE GENERACION DE OFERTAS PREPAGO con respecto a TTS           \n");
			wr.println();
			wr.println();
			wr.println();					 
			wr.append("\n Total de planes prepago a migrar:        "+ TTs.getTot_planprepmig()+" \n");
			wr.println();		 
			wr.append("\n Total Ofertas Demanda Prepago:           "+ TTs.getTot_bodemprep()+"   \n");
			wr.println();
			wr.append("\n Total Servicios Prepago:                 "+ TTs.getTot_boservprep()+"  \n");
			wr.println();		 
			wr.append("\n Total Adicionales y/o paquetes Prepago:  "+ TTs.getTot_boadiciprep()+" \n");
			wr.println();				 					
		    wr.println();
		    wr.append("\n Total BO de Planes:                      "+ TTs.getTOTAL_BO_PLAN() +"  \n");
			wr.println();
		    wr.append("\n Total BO paquetes y servicios:           "+ TTs.getTOTAL_BO_PAQ() +"   \n");
			wr.println();
		    wr.append("\n Numero Total de BillingOffers Generadas: "+ TTs.getTOTAL_BO() +"       \n");
		    wr.println();
		    wr.println();
		    wr.append("\n   ---------------------------------------------------------------------------------   \n");
		    wr.println();
			wr.append("\n Total de BO Plan en TTS:       "+ TTs.getTOT_BO_PLTTS() +"      VRS   Total de planes prepago a migrar: \n"+TTs.getTot_planprepmig());
			wr.println();
			wr.append("\n Total Product Offering en TTS: "+ TTs.getTOT_PO_TTS() +  "      VRS   Total de planes prepago a migrar: \n"+TTs.getTot_planprepmig());
			wr.println();			
			wr.append("\n Total de BO Plan en TTS:       "+ TTs.getTOT_BO_PLTTS() +"      VRS    Total BO de Planes Generados: "+ TTs.getTOTAL_BO_PLAN());
		    wr.println();	
			wr.append("\n Total BO Paquetes en TTS:      "+ TTs.getTOT_BO_PQTTS()+ "      VRS    Total Adicionales y/o paquetes Prepago: "+ TTs.getTot_boadiciprep());   
			wr.println();
			wr.println();
			wr.append("\n Tomar en cuenta que los paquetes en prepago en su mayoria no poseen spcode o es negativo,  por lo cual no aparecen en la TTS  ");
			wr.close();
			bw.close();
			}
		}catch(IOException e ){
			e.printStackTrace();
		}
      }
	/**
	 * 
	 * @param vp
	 * @param fila
	 * @param panama
	 * @param mapValues
	 * @param ms
	 * @param mapMatSim
	 * @throws Exception 
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void escribirNewOffers(IDataModel model, List<NewOffersAmdocs> vp, int fila, boolean panama, List<MaterialSim>  ms) throws Exception {
	//-------------- detalle de estado de ofertas en Amdocs ----------------
	HashMap<String, Integer> mapValues = obtnerNumColsNewOffersAmdocs("NewOffersAmdocs");
	HashMap<String, Integer> mapMatSim = obtnerNumColsNewOffersAmdocs("CodMaterialSim");			
	HashMap<String, Integer> mapEstOfer = obtnerNumColsNewOffersAmdocs("Estado_de_Ofertas_Gather");
	HashMap<String, Integer> mapEleg = obtnerNumColsNewOffersAmdocs("Elegidos");
	HashMap<String, Integer> mapBilling = obtnerNumColsNewOffersAmdocs("Billing_Offers");
	HashMap<String, Integer> mapComp = obtnerNumColsNewOffersAmdocs("Componentes");	
	List<TOTALES_SEG_CAL>  totalesCal = new ArrayList<TOTALES_SEG_CAL>();
    
	List<NewOffersAmdocs> ovv = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> nvv = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> cpv = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> cpnv = new ArrayList<NewOffersAmdocs>();
	//-------------  Detalle de Ofertas con Elegidos -----------------------
	List<NewOffersAmdocs> eFNF = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> e1BFNF = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> e2BFNF = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> e3BFNF = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> enoFNF = new ArrayList<NewOffersAmdocs>();
	List<NewOffersAmdocs> enoBFNF = new ArrayList<NewOffersAmdocs>();
	//-------------- Detalle Boffers -------------------------------------
	List<NewOffersAmdocs> bop = new ArrayList<NewOffersAmdocs>();
	List<OfertasDemanda> boad = new ArrayList<OfertasDemanda>();
	List<OfertasDemanda> boser = new ArrayList<OfertasDemanda>();
	List<OfertasDemanda> bodem = new ArrayList<OfertasDemanda>();
	//---------------- Detalle componentes 
	List<Componentes> cman = new ArrayList<Componentes>();
	List<Componentes> copc = new ArrayList<Componentes>();
	List<Componentes> coxd = new ArrayList<Componentes>();
	List<Componentes> cpre = new ArrayList<Componentes>();
	List<Componentes> cach = new ArrayList<Componentes>();
	List<Componentes> cexc = new ArrayList<Componentes>();
	totalesCal = model.generaTotalesCalidad();
	//--------------------------------------------------------------------------
	ovv = model.getOfertasVig();	
	nvv = model.getOfertasNoVig();	
	cpv = model.getOferCambioPlanVig();
	cpnv = model.getOferCambioPlanNoVig();
	
	eFNF   = model.getOferElegFNF();
	e1BFNF = model.getOferEleg1BFNF();
	e2BFNF = model.getOferEleg2BFNF();
	e3BFNF = model.getOferEleg3BFNF();
	enoFNF = model.getOferEleg0FNF();
	enoBFNF = model.getOferEleg0BFNF();
	
	bop   = model.getOfertasPlan();
	boad  = model.getOfertasAdicional();
	boser = model.getOfertasServ();
	bodem = model.getOfertaMercado();
	
	cman = model.getCompMand();
	copc = model.getCompOpc();
	coxd = model.getCompOpcxDef();
	cpre = model.getCompPrepago();
	cach = model.getActCharge();
	cexc = model.getCompExcluidos();
	System.out.println(" cargo la informacion de detalle ....");
	int reg = 1;
	//----------------------------------------------------------------------------------------------------
	  InputStream arch = new FileInputStream("D://EntradasEPC/METRICAS_EPC.xlsx");
	    Workbook work = WorkbookFactory.create(arch);
		System.out.println( "--Wactory");	
		reg = escribirPlanDetalle(ovv, "DetEstadoOfertas", "Ofertas Vigentes para la Venta" , reg, true, work, hto);
		reg = escribirPlanDetalle(nvv, "DetEstadoOfertas", "Ofertas No Vigentes para la Venta", reg, false, work, hto);
		reg = escribirPlanDetalle(cpv, "DetEstadoOfertas", "Ofertas Vigentes para Cambio Plan", reg, false, work,  hto);
		reg = escribirPlanDetalle(cpnv,"DetEstadoOfertas", "Ofertas No Vigentes para Cambio Plan", reg, false, work,  hto); 		
		reg = 1;
		reg = escribirPlanDetalle(eFNF,    "DetPlanesxElegidos", "Planes con Elegidos FNF" , reg, true, work, ht);
		reg = escribirPlanDetalle(e1BFNF,  "DetPlanesxElegidos", "Planes con 1 Elegido BFNF" ,reg, false,work,ht);
		reg = escribirPlanDetalle(e2BFNF,  "DetPlanesxElegidos", "Planes con 2 Elegidos BFNF" ,reg, false,work,ht);
		reg = escribirPlanDetalle(e3BFNF,  "DetPlanesxElegidos", "Planes con 3 Elegidos BFNF" ,reg, false,work, ht);
		reg = escribirPlanDetalle(enoFNF,  "DetPlanesxElegidos", "Planes sin Elegidos FNF" , reg, false,work, ht);
		reg = escribirPlanDetalle(enoBFNF, "DetPlanesxElegidos", "Planes sin Elegidos BFNF" , reg, false, work,ht);		
		reg = 1;
		reg = escribirPlanDetalle(bop,   "DetBillingOffers", "Billing Offers de Plan" , reg, true, work, htp);
		reg = escriOferEspDetalle(boad,  "DetBillingOffers", "Billing Offers Adicionales" ,reg, false, work,htp);
		reg = escriOferEspDetalle(boser, "DetBillingOffers", "Billing Offers de Servicios" ,reg, false,work,htp);
		reg = escriOferEspDetalle(bodem, "DetBillingOffers", "Billing Offers de Demanda (Activity Charge)" ,reg, false, work,htp);		
		reg = 1;
		reg =  escriCompDetalle(cman, "DetComponentes", "COMPONENTES CON CARDINALIDAD (MANDATORIA)", reg, true, work, htc) ;
		reg =  escriCompDetalle(copc, "DetComponentes", "COMPONENTES CON CARDINALIDAD(OPCIONAL)", reg, false, work, htc) ;
		reg =  escriCompDetalle(coxd, "DetComponentes", "COMPONENTES CON CARDINALIDAD (OPCIONAL POR DEFECTO)", reg, false, work, htc) ;
		reg =  escriCompDetalle(cpre, "DetComponentes", "COMPONENTES INCLUIDAS (PREPAGO)", reg, false, work, htc) ;
		reg =  escriCompDetalle(cach, "DetComponentes", "ACTIVITY CHARGE", reg, false, work, htc) ;
		reg =  escriCompDetalle(cexc, "DetComponentes", "COMPONENTES EXCLUIDOS", reg, false, work, htc);
		FileOutputStream file = new FileOutputStream("D://SalidasEPC/METRICAS_EPC.xlsx");	
		work.write(file); 
		file.close();
	//-----------------------------------------------------------------------------------------------------
	InputStream in = new FileInputStream("D://SalidasEPC/METRICAS_EPC.xlsx");
	 Workbook wb = WorkbookFactory.create(in);
	 CreationHelper createHelper = wb.getCreationHelper();
	 CellStyle borderStyle = createBorderedStyle(wb);
	 System.out.println( "Pasamos para escribir archivo de metricas");
		//-------------- para generar hyperlinks ----------------------
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);	 		  
		//-------------------------------------------------------------   
      Sheet sheet1 = wb.getSheet("Componentes");
      fila  = 2; 
      for(TOTALES_SEG_CAL  tot: totalesCal){
    	  Row row = sheet1.getRow(fila); 
  		if(row==null)
  			row = sheet1.createRow(fila);  
  		
  		Cell cell = row.getCell(mapComp.get("CON CARDINALIDAD (MANDATORIA)")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("CON CARDINALIDAD (MANDATORIA)"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue(tot.getCompcard_mand());
		cell.setCellStyle(borderStyle);   	 
		
		cell = row.getCell(mapComp.get("CON CARDINALIDAD(OPCIONAL)")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("CON CARDINALIDAD(OPCIONAL)"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getCompcard_opc());	
		
		cell = row.getCell(mapComp.get("CON CARDINALIDAD (OPCIONAL POR DEFECTO)")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("CON CARDINALIDAD (OPCIONAL POR DEFECTO)"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getCompcard_opcxdef());
		
		cell = row.getCell(mapComp.get("INCLUIDAS (PREPAGO)")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("INCLUIDAS (PREPAGO)"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getCompprepago());	
		
		cell = row.getCell(mapComp.get("ACTIVITY CHARGE")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("ACTIVITY CHARGE"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getActivitycharge());	
		
		cell = row.getCell(mapComp.get("EXCLUIDOS")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("EXCLUIDOS"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getCompcard_exc());	
		fila++;    	   
      } 
      
      Row row = sheet1.getRow(fila); 
		if(row==null)
			row = sheet1.createRow(fila);  
		
		Cell cell = row.getCell(mapComp.get("CON CARDINALIDAD (MANDATORIA)"));
		if (cell == null)   
			cell = row.createCell(mapComp.get("CON CARDINALIDAD (MANDATORIA)")); 	
		cell.setCellValue("LINK_CON_CARDINALIDAD_(MANDATORIA)");
		Hyperlink l1 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		l1.setAddress("'DetComponentes'!B"+htc.get("COMPONENTES CON CARDINALIDAD (MANDATORIA)"));
		cell.setHyperlink(l1);
		cell.setCellStyle(hlink_style);
	
		
		cell = row.getCell(mapComp.get("CON CARDINALIDAD(OPCIONAL)")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("CON CARDINALIDAD(OPCIONAL)"));   
		 cell.setCellValue("LINK_CON_CARDINALIDAD(OPCIONAL)");
		 Hyperlink l2 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		 l2.setAddress("'DetComponentes'!B"+htc.get("COMPONENTES CON CARDINALIDAD(OPCIONAL)"));
		 cell.setHyperlink(l2);
		cell.setCellStyle(hlink_style);
		
		cell = row.getCell(mapComp.get("CON CARDINALIDAD (OPCIONAL POR DEFECTO)")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("CON CARDINALIDAD (OPCIONAL POR DEFECTO)"));   
		cell.setCellValue("LINK_CON_CARDINALIDAD_(OPCIONAL_POR_DEFECTO)");
		 Hyperlink l3 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		l3.setAddress("'DetComponentes'!B"+htc.get("COMPONENTES CON CARDINALIDAD (OPCIONAL POR DEFECTO)"));
		cell.setHyperlink(l3);
		cell.setCellStyle(hlink_style);
		
		cell = row.getCell(mapComp.get("INCLUIDAS (PREPAGO)")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("INCLUIDAS (PREPAGO)"));   
		cell.setCellValue("LINK_INCLUIDAS_(PREPAGO)");	
		 Hyperlink l4 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		l4.setAddress("'DetComponentes'!B"+htc.get("COMPONENTES INCLUIDAS (PREPAGO)"));
		cell.setHyperlink(l4);
		cell.setCellStyle(hlink_style);
		
		cell = row.getCell(mapComp.get("ACTIVITY CHARGE")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("ACTIVITY CHARGE"));   
		cell.setCellValue("LINK_ACTIVITY_CHARGE");	
		Hyperlink l5 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		l5.setAddress("'DetComponentes'!B"+htc.get("ACTIVITY CHARGE"));
		cell.setHyperlink(l5);
		cell.setCellStyle(hlink_style);
		
		cell = row.getCell(mapComp.get("EXCLUIDOS")); 
		if (cell == null)   
			cell = row.createCell(mapComp.get("EXCLUIDOS"));   
		cell.setCellValue("LINK_EXCLUIDOS");
		 Hyperlink l6 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		l6.setAddress("'DetComponentes'!B"+htc.get("COMPONENTES EXCLUIDOS"));
		cell.setHyperlink(l6);
		cell.setCellStyle(hlink_style);
      //----------------------------------
      Sheet sheet5 = wb.getSheet("Billing_Offers");
      fila  = 2; 
      for(TOTALES_SEG_CAL  tot: totalesCal){
    	  row = sheet5.getRow(fila); 
  		if(row==null)
  			row = sheet5.createRow(fila);     		
  		cell = row.getCell(mapBilling.get("EN PLAN")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("EN PLAN"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue(tot.getTot_planprepmig());
		cell.setCellStyle(borderStyle);
		
		System.out.println("Billing_Offers");   
		
		cell = row.getCell(mapBilling.get("ADICIONALES (PAQUETES ADICIONALES)")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("ADICIONALES (PAQUETES ADICIONALES)"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getTot_boadiciprep());	
		
		cell = row.getCell(mapBilling.get("DE SERVICIO")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("DE SERVICIO"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getTot_boservprep());	
		
		cell = row.getCell(mapBilling.get("DE DEMANDA(activity charge)")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("DE DEMANDA(activity charge)"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getTot_bodemprep());	
		
		cell = row.getCell(mapBilling.get("TOTAL")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("TOTAL"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		//cell.setCellValue(values.getPlanprepago());	
   	 fila++;  
   	   
      }
      row = sheet5.getRow(fila); 
		if(row==null)
			row = sheet5.createRow(fila);     		
		cell = row.getCell(mapBilling.get("EN PLAN")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("EN PLAN"));   
		cell.setCellValue("LINK_EN_PLAN");
		Hyperlink h2 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		h2.setAddress("'DetBillingOffers'!B"+htp.get("Billing Offers de Plan"));
		cell.setHyperlink(h2);
		cell.setCellStyle(hlink_style);
		
		cell = row.getCell(mapBilling.get("ADICIONALES (PAQUETES ADICIONALES)")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("ADICIONALES (PAQUETES ADICIONALES)"));
			Hyperlink h3 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			cell.setCellValue("LINK_ADICIONALES_(PAQUETES_ADICIONALES)");	
			h3.setAddress("'DetBillingOffers'!B"+htp.get("Billing Offers Adicionales"));
			cell.setHyperlink(h3);
			cell.setCellStyle(hlink_style);
			
		cell = row.getCell(mapBilling.get("DE SERVICIO")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("DE SERVICIO"));
			Hyperlink h4 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			cell.setCellValue("LINK_DE_SERVICIO");	
			h4.setAddress("'DetBillingOffers'!B"+htp.get("Billing Offers de Servicios"));
			cell.setHyperlink(h4);
			cell.setCellStyle(hlink_style);
			
		cell = row.getCell(mapBilling.get("DE DEMANDA(activity charge)")); 
		if (cell == null)   
			cell = row.createCell(mapBilling.get("DE DEMANDA(activity charge)")); 
		Hyperlink h5 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			cell.setCellValue("LINK_DE_DEMANDA(activity_charge)");	
			h5.setAddress("'DetBillingOffers'!B"+htp.get("Billing Offers de Demanda (Activity Charge)"));
			cell.setHyperlink(h5);
			cell.setCellStyle(hlink_style);		 
         
      //--------------------------------------------------------------------------
      Sheet sheet3 = wb.getSheet("Estado_de_Ofertas_Gather");
      fila  = 2; 
      for(TOTALES_SEG_CAL  tot: totalesCal){
    	row = sheet3.getRow(fila); 
  		if(row==null)
  			row = sheet3.createRow(fila);     		
  		cell = row.getCell(mapEstOfer.get("PARA VENTA VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA VENTA VIGENTES"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue(tot.getOfventa_vig());
		cell.setCellStyle(borderStyle);
		
		cell = row.getCell(mapEstOfer.get("PARA VENTA NO VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA VENTA NO VIGENTES"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getOfventa_novig());	
		
		cell = row.getCell(mapEstOfer.get("PARA CAMBIO DE PLAN VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA CAMBIO DE PLAN VIGENTES"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getOfcamplan_vig());	
		
		cell = row.getCell(mapEstOfer.get("PARA CAMBIO DE PLAN NO VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA CAMBIO DE PLAN NO VIGENTES"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getOfcamplan_novig());	
		
		cell = row.getCell(mapEstOfer.get("TOTAL OFERTAS ENVIADAS")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("TOTAL OFERTAS ENVIADAS"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		//cell.setCellValue(values.getPlanprepago());	
   	 fila++;     	 
      }
      row = sheet3.getRow(fila); 
		if(row==null)
			row = sheet3.createRow(fila);     		
		cell = row.getCell(mapEstOfer.get("PARA VENTA VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA VENTA VIGENTES"));   
		    cell.setCellValue("LINK_PARA_VENTA_VIGENTES");
		    Hyperlink p = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			p.setAddress("'DetEstadoOfertas'!B"+hto.get("Ofertas Vigentes para la Venta"));
			cell.setHyperlink(p);
			cell.setCellStyle(hlink_style);	
		
		cell = row.getCell(mapEstOfer.get("PARA VENTA NO VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA VENTA NO VIGENTES")); 
		  Hyperlink p1 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
	    	cell.setCellValue("LINK_PARA_VENTA_NO_VIGENTES");
	    	p1.setAddress("'DetEstadoOfertas'!B"+hto.get("Ofertas No Vigentes para la Venta"));
	    	cell.setHyperlink(p1);
	    	cell.setCellStyle(hlink_style);	
		
		cell = row.getCell(mapEstOfer.get("PARA CAMBIO DE PLAN VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA CAMBIO DE PLAN VIGENTES")); 
		  Hyperlink p2 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			cell.setCellValue("LINK_PARA_CAMBIO_DE_PLAN_VIGENTES");	
			p2.setAddress("'DetEstadoOfertas'!B"+hto.get("Ofertas Vigentes para Cambio Plan"));
			cell.setHyperlink(p2);
			cell.setCellStyle(hlink_style);	
		
		cell = row.getCell(mapEstOfer.get("PARA CAMBIO DE PLAN NO VIGENTES")); 
		if (cell == null)   
			cell = row.createCell(mapEstOfer.get("PARA CAMBIO DE PLAN NO VIGENTES"));  
		 Hyperlink p3 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
			cell.setCellValue("LINK_PARA_CAMBIO_DE_PLAN_NO_VIGENTES");
			 p3.setAddress("'DetEstadoOfertas'!B"+hto.get("Ofertas No Vigentes para Cambio Plan"));
			cell.setHyperlink(p3);
			cell.setCellStyle(hlink_style);	  
      
      
     Sheet sheet4 = wb.getSheet("Elegidos");
      fila  = 2; 
      for(TOTALES_SEG_CAL  tot: totalesCal){
    	row = sheet4.getRow(fila); 
  		if(row==null)
  			row = sheet4.createRow(fila);   		
  		cell = row.getCell(mapEleg.get("CON ELEGIFOS FNF")); 
		if (cell == null)   
			cell = row.createCell(mapEleg.get("CON ELEGIFOS FNF"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);   
		cell.setCellValue(tot.getEleg_fnf());
		cell.setCellStyle(borderStyle);
		
		System.out.println("Hoja Elegidos");  
		
		cell = row.getCell(mapEleg.get("CON 1 ELEGIDO BFNF")); 
		if (cell == null)   
			cell = row.createCell(mapEleg.get("CON 1 ELEGIDO BFNF"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getEleg_bfnf1());	
		
		cell = row.getCell(mapEleg.get("CON 2 ELEGIDOS BFNF")); 
		if (cell == null)   
			cell = row.createCell(mapEleg.get("CON 2 ELEGIDOS BFNF"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getEleg_bfnf2());
		
		cell = row.getCell(mapEleg.get("CON 3 ELEGIDOS BFNF")); 
		if (cell == null)   
			cell = row.createCell(mapEleg.get("CON 3 ELEGIDOS BFNF"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getEleg_bfnf3());	
		
		cell = row.getCell(mapEleg.get("SIN ELEGIDOS FNF")); 
		if (cell == null)   
			cell = row.createCell(mapEleg.get("SIN ELEGIDOS FNF"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getEleg_fnf0());	
		
		cell = row.getCell(mapEleg.get("SIN ELEGIDOS BFNF")); 
		if (cell == null)   
			cell = row.createCell(mapEleg.get("SIN ELEGIDOS BFNF"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		cell.setCellValue(tot.getEleg_bfnf0());
				
		cell = row.getCell(mapEleg.get("CON ELEGIDOS BFNF Y FNF")); 
		if (cell == null)   
			cell = row.createCell(mapEleg.get("CON ELEGIDOS BFNF Y FNF"));   
		cell.setCellType(Cell.CELL_TYPE_STRING); 
		cell.setCellStyle(borderStyle);
		//cell.setCellValue(values.getPlanprepago());	
   	  fila++;      	   
      } 
     
  	row = sheet4.getRow(fila); 
		if(row==null)
			row = sheet4.createRow(fila);   		
		cell = row.getCell(mapEleg.get("CON ELEGIFOS FNF")); 
	if (cell == null)   
		cell = row.createCell(mapEleg.get("CON ELEGIFOS FNF"));   
	cell.setCellValue("Link_CON_ELEGIFOS_FNF");
	Hyperlink link2 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
	link2.setAddress("'DetPlanesxElegidos'!B"+ht.get("Planes con Elegidos FNF"));
	cell.setHyperlink(link2);
	cell.setCellStyle(hlink_style);		
	
	cell = row.getCell(mapEleg.get("CON 1 ELEGIDO BFNF")); 
	if (cell == null)   
		cell = row.createCell(mapEleg.get("CON 1 ELEGIDO BFNF")); 	
	cell.setCellValue("Link_CON_1_ELEGIDO_BFNF");	
	Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
	link.setAddress("'DetPlanesxElegidos'!B"+ht.get("Planes con 1 Elegido BFNF"));
	cell.setHyperlink(link);
	cell.setCellStyle(hlink_style);	
	
	cell = row.getCell(mapEleg.get("CON 2 ELEGIDOS BFNF")); 
	if (cell == null)   
		cell = row.createCell(mapEleg.get("CON 2 ELEGIDOS BFNF"));   
	cell.setCellValue("Link_CON_2_ELEGIDOS_BFNF");
	Hyperlink link3 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
	link3.setAddress("'DetPlanesxElegidos'!B"+ht.get("Planes con 2 Elegidos BFNF"));
	cell.setHyperlink(link3);
	cell.setCellStyle(hlink_style);	
	
	cell = row.getCell(mapEleg.get("CON 3 ELEGIDOS BFNF")); 
	if (cell == null)   
		cell = row.createCell(mapEleg.get("CON 3 ELEGIDOS BFNF")); 
	cell.setCellValue("Link_CON 3_ELEGIDOS_BFNF");	
	Hyperlink link4 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
	link4.setAddress("'DetPlanesxElegidos'!B"+ht.get("Planes con 3 Elegidos BFNF"));
	cell.setHyperlink(link4);
	cell.setCellStyle(hlink_style);	
	
	cell = row.getCell(mapEleg.get("SIN ELEGIDOS FNF")); 
	if (cell == null)   
		cell = row.createCell(mapEleg.get("SIN ELEGIDOS FNF"));	
	cell.setCellValue("Link_SIN_ELEGIDOS_FNF");	
	Hyperlink link5 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
	link5.setAddress("'DetPlanesxElegidos'!B"+ht.get("Planes sin Elegidos FNF"));
	cell.setHyperlink(link5);
	cell.setCellStyle(hlink_style);	
	
	cell = row.getCell(mapEleg.get("SIN ELEGIDOS BFNF")); 
	if (cell == null)   
		cell = row.createCell(mapEleg.get("SIN ELEGIDOS BFNF"));   
	cell.setCellValue("Link_SIN_ELEGIDOS_BFNF");
	Hyperlink link6 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
	link6.setAddress("'DetPlanesxElegidos'!B"+ht.get("Planes sin Elegidos BFNF"));
	cell.setHyperlink(link6);
	cell.setCellStyle(hlink_style);	      
   //-------------------------------------------------------------------------------------
       Sheet sheet6 = wb.getSheet("CodMaterialSim");      
       fila  =2;       
       for(MaterialSim  msim: ms){
    	   row = sheet6.getRow(fila); 
   		if(row==null)
   			row = sheet6.createRow(fila);   		
   		 cell = row.getCell(mapMatSim.get("codmaterial")); 
   		if (cell == null)   
   			cell = row.createCell(mapMatSim.get("codmaterial"));   
   		cell.setCellType(Cell.CELL_TYPE_STRING); 
   		cell.setCellStyle(borderStyle); 
   		cell.setCellValue(msim.getCodmaterial());
   		
   		cell = row.getCell(mapMatSim.get("Descmaterial")); 
		if (cell == null)   
			cell = row.createCell(mapMatSim.get("Descmaterial"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellStyle(borderStyle);
		cell.setCellValue(msim.getDescmaterial());	
		System.out.println("  :"+ msim.getDescmaterial());
		
		cell = row.getCell(mapMatSim.get("Condiciones del Codigo en Plataforma PREPAGO")); 
		if (cell == null)   
			cell = row.createCell(mapMatSim.get("Condiciones del Codigo en Plataforma PREPAGO"));   
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellStyle(borderStyle);
		if(msim.getCodplatprepago()!= null)
			cell.setCellValue(msim.getCodplatprepago()); 
		else cell.setCellValue("");
		
		System.out.println(msim.getCodplatprepago());
    	
		fila++;     	   
       } 
    /*  FileOutputStream fileOutcm = new FileOutputStream("D:\\METRICAS.xlsx");	
 	  wb.write(fileOutcm);  
 	  fileOutcm.close();
 	  
 	  InputStream nof= new FileInputStream("D:\\METRICAS.xlsx");
 	  Workbook wbnof = WorkbookFactory.create(nof);
 	  CellStyle borderStylenof = createBorderedStyle(wbnof);	*/  
	  fila = 2;
       Sheet sheet2 = wb.getSheet("NewOffersAmdocs");	  
       for(NewOffersAmdocs values : vp){    	  
          row = sheet2.getRow(fila); 
     	   if(row==null)
 			row = sheet2.createRow(fila);
        cell = row.getCell(mapValues.get("PLAN BSCS")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("PLAN BSCS"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING);   
 		cell.setCellValue(values.getPlan_bscs());
 		cell.setCellStyle(borderStyle);
 		System.out.println(values.getPlan_bscs() + " "+ " valor de indice: "+ mapValues.get("PLAN BSCS"));

 		cell = row.getCell(mapValues.get("PLAN PLATAFORMA PREPAGO")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("PLAN PLATAFORMA PREPAGO"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING); 
 		cell.setCellStyle(borderStyle);
 		cell.setCellValue(values.getPlanprepago());		
 			
 		cell = row.getCell(mapValues.get("TMCODE")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("TMCODE"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING);
 		cell.setCellStyle(borderStyle);
 		cell.setCellValue(values.getTmcode());

 		cell = row.getCell(mapValues.get("SPCODE")); 
 			if (cell == null)   
 				cell = row.createCell(mapValues.get("SPCODE"));   
 			cell.setCellType(Cell.CELL_TYPE_STRING);
 			cell.setCellStyle(borderStyle);
 			cell.setCellValue(values.getSpcode());		

 		if(values.getPaquete_bscs()!= null){
 			cell = row.getCell(mapValues.get("PAQUETE BSCS")); 
 			if (cell == null)   
 				cell = row.createCell(mapValues.get("PAQUETE BSCS"));   
 			cell.setCellType(Cell.CELL_TYPE_STRING); 
 			cell.setCellStyle(borderStyle);
 			cell.setCellValue(values.getPaquete_bscs());}

 		cell = row.getCell(mapValues.get("ZONA")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("ZONA"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING);
 		cell.setCellStyle(borderStyle);
 		cell.setCellValue(values.getZona());		
 		
 		cell = row.getCell(mapValues.get("BO_ID")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("BO_ID"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING); 
 		cell.setCellStyle(borderStyle);
 		cell.setCellValue(values.getBo_id());		
 		
 		cell = row.getCell(mapValues.get("Billing Offer")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("Billing Offer"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING); 
 		cell.setCellStyle(borderStyle);
 		cell.setCellValue(values.getBilling_offer());
 		
 		cell = row.getCell(mapValues.get("PO_ID")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("PO_ID"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING);
 		cell.setCellStyle(borderStyle);
 		cell.setCellValue(values.getPo_id());
 		
 		cell = row.getCell(mapValues.get("Product Offer")); 
 		if (cell == null)   
 			cell = row.createCell(mapValues.get("Product Offer"));   
 		cell.setCellType(Cell.CELL_TYPE_STRING);
 		cell.setCellStyle(borderStyle);
 		cell.setCellValue(values.getProduct_offering());	
 		fila++;					
        }     
       
        System.out.println("Termina procesooo ");
        FileOutputStream fileOutnof = new FileOutputStream("D://SalidasEPC/METRICAS_EPC.xlsx");	
      	wb.write(fileOutnof); 
	    fileOutnof.close();	    
	 	
	}	
	private static int escribirPlanDetalle(List<NewOffersAmdocs> no, String hoja, String titulo, int fila, boolean creaHoja, Workbook area, Hashtable<String, Integer> ht) 
	throws InvalidFormatException, IOException{	
		int fil = 0;
		try {
		System.out.println("carga archivo1");
		CellStyle EstiloBorde = null;
		EstiloBorde = createBorderedStyle(area);
		CellStyle celdaStyle  = createStyle(area);
		CellStyle titStyle = createTitStyle(area);
		Sheet sheet = null;
		if(creaHoja) { 	sheet = area.createSheet(hoja); 
					    fil = fila;
					 }
		else  		 {	fil = fila +2;
						sheet = area.getSheet(hoja);	 
				     } 	
		 Row  row1 = sheet.getRow(fil);
		 if(row1==null)
			row1 = sheet.createRow(fil);
		 
		 Cell cell = row1.getCell(1); 
		 if (cell == null)   
			 cell = row1.createCell(1);	
		     cell.setCellStyle(titStyle);
		     cell.setCellValue(titulo);  
		 fil++;		 
		 ht.put(titulo, fila +3);
	   	  for(int col = 1; col < 11; col++){
			  Row  row = sheet.getRow(fil); 
		    //cell1= sheet1.createRow(fil).createCell((short)col);
			  if(row==null)
				row = sheet.createRow(fil);
			    Cell cell1 = row.getCell(col); 
				if (cell1 == null)   
			    cell1 = row.createCell(col);	
				cell1.setCellStyle(celdaStyle);
			    if(col==1)
			    	cell1.setCellValue("PLAN PLATAFORMA PREPAGO");
			    if(col==2)
			    	cell1.setCellValue("TMCODE");
			    if(col==3)
			    	cell1.setCellValue("PLAN BSCS");
			    if(col==4)
			    	cell1.setCellValue("SPCODE");
			    if(col==5)
			    	cell1.setCellValue("PAQUETE BSCS");
			    if(col==6)
			    	cell1.setCellValue("ZONA");
			    if(col==7)
			    	cell1.setCellValue("BO_ID");
			    if(col==8)
			    	cell1.setCellValue("Billing Offer");
			    if(col==9)
			    	cell1.setCellValue("PO_ID");
			    if(col==10)
			    	cell1.setCellValue("Product Offer");
		  }	    
	  fil++;	
	  int col;
	    for(NewOffersAmdocs  of: no){	
	       col= 1;	     
	       Row  row = sheet.getRow(fil); 
    	   if(row==null)
			row = sheet.createRow(fil);
            Cell cell1 = row.getCell(col); 
		    if (cell1 == null)   
		   cell1 = row.createCell(col);	       
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getPlanprepago());
		   cell1.setCellStyle(EstiloBorde);		   
		   col++;
		 
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);
		   cell1.setCellValue(of.getTmcode());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;		   
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getPlan_bscs());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);	   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getSpcode());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getPaquete_bscs());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);	   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getZona());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);	   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getBo_id());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getBilling_offer());
		   cell1.setCellStyle(EstiloBorde);		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);  
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getPo_id());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		   if (cell1 == null)   
			cell1 = row.createCell(col);   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getProduct_offering());
		   cell1.setCellStyle(EstiloBorde);		   
		  fil++;
	    }	    
		}catch(Error e){
			 System.out.println("Unable to Create Workbook" + e.getMessage());
		}		
		return fil;
	}	
	private static int escriOferEspDetalle(List<OfertasDemanda> od, String hoja, String titulo, int fila, boolean creaHoja, Workbook ar,  Hashtable<String, Integer> ht) 
			throws InvalidFormatException, IOException{		
	//	InputStream en = new FileInputStream("D:\\METRICAS.xlsx");
		System.out.println("carga archivo");
		int fil = 0;
		int col = 1;
		try {
	//	Workbook ar = null;
	 // ar = WorkbookFactory.create(en);		
		System.out.println("carga archivo1");
		CellStyle EstiloBorde = null;
		EstiloBorde = createBorderedStyle(ar);
		CellStyle celdaStyle  = createStyle(ar);	
		CellStyle titStyle = createTitStyle(ar);
		Sheet sheet = null;
		if(creaHoja) { 	sheet = ar.createSheet(hoja); 
					    fil = fila;
					 }
		else  		 {	fil = fila +2;
						sheet = ar.getSheet(hoja);	 
				     } 	
		System.out.println("inicia  ceacion de fila1");
		
		 Row  rowd = sheet.getRow(fil);
		 if(rowd==null)
			rowd = sheet.createRow(fil);
		 Cell cell = rowd.getCell(col); 
		 if (cell == null)   
			 cell = rowd.createCell(col);	
		     cell.setCellStyle(titStyle);
		     cell.setCellValue(titulo);  
		 fil++;
		 ht.put(titulo, fila +3);
		//---------------Crea fila con titulos ---------------------------
		 rowd = sheet.getRow(fil);
		 if(rowd==null)
			 rowd = sheet.createRow(fil);
		 cell = rowd.getCell(col); 
		 if (cell == null)   
			 cell = rowd.createCell(col);	
		 System.out.println("inicia  ceacion de celda2");
		     cell.setCellStyle(celdaStyle);
		     cell.setCellValue("NOMBRE  OFERTA ");  
		 fil++;			 
		 for(OfertasDemanda d: od){
			   Row  row = sheet.getRow(fil); 
	    	   if(row==null)row = sheet.createRow(fil);
	           Cell cellu = row.getCell(col); 
			   if (cellu == null) cellu = row.createCell(col);	       
			   cellu.setCellType(Cell.CELL_TYPE_STRING);   
			   cellu.setCellValue(d.getBO_DESCRIPCION());
			   cellu.setCellStyle(EstiloBorde);	
			 fil++;					 
		 }		 	
	
		}catch(Exception e){			
			 System.out.println("Unable to Create Workbook" + e.getMessage());
		}			
		return fil;
	}		
	
	private static int escriCompDetalle(List<Componentes> com, String hoja, String titulo, int fila, boolean creaHoja, Workbook are,  Hashtable<String, Integer> ht) 
			throws InvalidFormatException, IOException{	
		int fil = 0;
		try {
				
		System.out.println("carga archivo1");
		CellStyle EstiloBorde = null;
		EstiloBorde = createBorderedStyle(are);
	    CellStyle celdaStyle  = createStyle(are);
	    CellStyle titStyle = createTitStyle(are);
		Sheet sheet = null;
		if(creaHoja) { 	sheet = are.createSheet(hoja); 
					    fil = fila;
					 }
		else  		 {	fil = fila +2;
						sheet = are.getSheet(hoja);	 
				     } 	
		 Row  row1 = sheet.getRow(fil);
		 if(row1==null)
			row1 = sheet.createRow(fil);
		 
		 Cell cell = row1.getCell(1); 
		 if (cell == null)   
			 cell = row1.createCell(1);	
		     cell.setCellStyle(titStyle);
		     cell.setCellValue(titulo);  
		 fil++;
		 ht.put(titulo, fila +3);
	 		  for(int col = 1; col < 11; col++){
			  Row  row = sheet.getRow(fil); 
		    //cell1= sheet1.createRow(fil).createCell((short)col);
			  if(row==null)
				row = sheet.createRow(fil);
			    Cell cell1 = row.getCell(col); 
				if (cell1 == null)   
			    cell1 = row.createCell(col);	
				cell1.setCellStyle(celdaStyle);
			    if(col==1)
			    	cell1.setCellValue("DESCRIPCION");
			    if(col==2)
			    	cell1.setCellValue("PRODUCT_SPEC");
			    if(col==3)
			    	cell1.setCellValue("MAIN_COMP");
			    if(col==4)
			    	cell1.setCellValue("COMPONENT1");
			    if(col==5)
			    	cell1.setCellValue("COMPONENT2");
			    if(col==6)
			    	cell1.setCellValue("COMPONENT3");
			    if(col==7)
			    	cell1.setCellValue("SNCODE");
			    if(col==8)
			    	cell1.setCellValue("BLOQUEO");
			    if(col==9)
			    	cell1.setCellValue("TYPE");
			    if(col==10)
			    	cell1.setCellValue("OBSERVACION");
		  }	    
	  fil++;	
	  int col;
	    for(Componentes  of: com){	
	       col= 1;	     
	       Row  row = sheet.getRow(fil); 
    	   if(row==null)
			row = sheet.createRow(fil);
            Cell cell1 = row.getCell(col); 
		    if (cell1 == null)   
		   cell1 = row.createCell(col);	       
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getDESCRIPCION());
		   cell1.setCellStyle(EstiloBorde);		   
		   col++;
		  /* cell1= sheet1.createRow(fil).createCell((short)col);	   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);  */
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);
		   cell1.setCellValue(of.getPRODUCT_SPEC());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;		   
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getMAIN_COMP());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);	   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getCOMPONENT1());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getCOMPONENT2());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);	   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getCOMPONENT3());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);	   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getSNCODE());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getBLOQUEO());
		   cell1.setCellStyle(EstiloBorde);		   
		   col++;
		   cell1 = row.getCell(col); 
		    if (cell1 == null)   
			cell1 = row.createCell(col);  
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getTYPE());
		   cell1.setCellStyle(EstiloBorde);
		   
		   col++;
		   cell1 = row.getCell(col); 
		   if (cell1 == null)   
			cell1 = row.createCell(col);   
		   cell1.setCellType(Cell.CELL_TYPE_STRING);   
		   cell1.setCellValue(of.getOBSERVACION());
		   cell1.setCellStyle(EstiloBorde);		   
		  fil++;
	    }	    
	  
		}catch(Error e){
			 System.out.println("Unable to Create Workbook" + e.getMessage());
		}		
		return fil;	
	}
	/**
	 * Create a library of cell styles.
	 */
	private static CellStyle createBorderedStyle(Workbook wb){
		CellStyle style = wb.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}	
	
	private static CellStyle createStyle(Workbook wb){
		CellStyle style = wb.createCellStyle();
		Font f = wb.createFont();
		f.setBoldweight((short)2);
		f.setColor(IndexedColors.DARK_BLUE.getIndex());
		f.setFontName("Calibri");
		f.setFontHeightInPoints((short)11);
		style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setFont(f);
		style.setFillBackgroundColor((short)4);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);		
		return style;}
	
	
	private static CellStyle createTitStyle(Workbook wb){
		CellStyle style = wb.createCellStyle();
		Font f = wb.createFont();
		f.setBoldweight((short)2);
		f.setColor(IndexedColors.DARK_TEAL.getIndex());
		f.setFontName("Calibri");
		f.setFontHeightInPoints((short)12);
		style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		style.setFont(f);
		style.setFillBackgroundColor((short)6);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);		
		return style;}

}