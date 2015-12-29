package co.com.comcel.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import co.com.comcel.constants.IConstants;
import co.com.comcel.dao.DaoConfig;
import co.com.comcel.dao.IDataDAO;
import co.com.comcel.vo.AttrPaqLDI;
import co.com.comcel.vo.BillingOffer;
import co.com.comcel.vo.CompararTTS_BO;
import co.com.comcel.vo.Componentes;
import co.com.comcel.vo.MaterialSim;
import co.com.comcel.vo.NewOffersAmdocs;
import co.com.comcel.vo.OfertasDemanda;
import co.com.comcel.vo.OneTimeCharge;
import co.com.comcel.vo.Paquete;
import co.com.comcel.vo.PaqueteLDI;
import co.com.comcel.vo.Plan;
import co.com.comcel.vo.Price;
import co.com.comcel.vo.PriceGenerico;
import co.com.comcel.vo.Prit;
import co.com.comcel.vo.RecurringCharge;
import co.com.comcel.vo.ServiceFilterList;
import co.com.comcel.vo.ServiceFilterToCharge;
import co.com.comcel.vo.TOTALES_SEG_CAL;
import co.com.comcel.vo.TablaTraduccion;
import co.com.comcel.vo.UsageCharge;
import co.com.comcel.vo.Value;
import co.com.comcel.vo.BORevision;
import co.com.comcel.vo.ValuesPrepago;
import co.com.comcel.vo.servicio_comp;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;

public class DataModel implements IDataModel{

	private DaoManager daoManager;
	private IDataDAO dataDao;
	/**
	 * Obtiene los valores de service Filter y los asocia a un HashMap
	 */
	private HashMap<Integer, ServiceFilterList> mapServiceFilterList;
	/**
	 * Obtiene los valores de Service Filter To Charge
	 */
	private HashMap<Integer, ServiceFilterToCharge> mapServiceFilterToCharge;
	
	
	private static final Logger log = Logger.getLogger(DataModel.class.getName());
	public DataModel() {
		this.daoManager = DaoConfig.getDaoManager();
		this.dataDao = (IDataDAO)daoManager.getDao(IDataDAO.class);
		mapServiceFilterList = this.getServiceFilterList();
		mapServiceFilterToCharge = this.getServiceFilterToCharge();
	}

	public Collection<Plan> getPlanesMigracion(boolean panama)throws DaoException{
		if(!panama)
			return dataDao.getPlanesMigracion();
		else
			return dataDao.getPlanesMigracionPanama();
	}

	public String getPriceGenerico(String code, boolean panama)throws DaoException{
		if(!panama)
			return dataDao.getPriceGenerico(code);
		else
			return dataDao.getPriceGenericoPanama(code);
	}
	
	public String getPriceGenericoGPRS_PL(String code)throws DaoException{
		return dataDao.getPriceGenericoGPRS_PL(code);
	}
	
	public String getPriceGenericoGPRS_PQ(String code)throws DaoException{
		return dataDao.getPriceGenericoGPRS_PQ(code);
	}
	
	public String getPriceGenericoBB_PL(String code)throws DaoException{
		return dataDao.getPriceGenericoBB_PL(code);
	}
	
	public String getPriceGenericoBB_PQ(String code)throws DaoException{
		return dataDao.getPriceGenericoBB_PQ(code);
	}

	public BillingOffer generarBO(Plan plan, int promocion, int contLink, boolean panama, int isPlanBasico) throws Exception{
		int tipoBolsa = evaluaTipoBolsa(plan);
		String descripcion = "";
		if(plan.getDESCRIPCION1()!=null)descripcion+=plan.getDESCRIPCION1();
		if(plan.getDESCRIPCION2()!=null)descripcion+=plan.getDESCRIPCION2();
		if(plan.getDESCRIPCION3()!=null)descripcion+=plan.getDESCRIPCION3();
		if(plan.getDESCRIPCION4()!=null)descripcion+=plan.getDESCRIPCION4();
		if(plan.getDESCRIPCION5()!=null)descripcion+=plan.getDESCRIPCION5();
		if(plan.getDESCRIPCION6()!=null)descripcion+=plan.getDESCRIPCION6();
		if(plan.getDESCRIPCION7()!=null)descripcion+=plan.getDESCRIPCION7();
		if(plan.getDESCRIPCION8()!=null)descripcion+=plan.getDESCRIPCION8();
		BillingOffer bo = new BillingOffer();
		String nombreBO = plan.getPLAN_DOX().trim();
		if(promocion==1){
			nombreBO+=" - PromoOnnet";
			bo.setDurationA("Months");
			bo.setDurationB(plan.getMESES_PROMO()-1);
		}
		bo.setTipoBolsa(tipoBolsa);
		bo.setPlan(plan);
		bo.setTmcode(plan.getTMCODE());
		bo.setName(nombreBO);
	   	bo.setCode(nombreBO);
		bo.setLevel("Subscriber");
		bo.setType("Additional offer");
		//bo.setEffectiveDate("2011/01/01");//Siempre va a ser esta fecha. Definida por AMDOCS
		bo.setEffectiveDate(plan.getEFFECTIVE_DATE());
		bo.setSaleEffectiveDate(plan.getFECHA_INICIO());
		bo.setSaleExpirationdate(plan.getFECHA_VENTA());		
		bo.setDeployIndicator("FALSE");		
	    bo.setDescripcion(descripcion);
		//if(plan.getDESCRIPCION_COM()!=null){
		          //   bo.setDescripcion(plan.getDESCRIPCION_COM());}
		bo.setProductType("GSM");
		bo.setCurrency("COP");
		List<Price> priceList = new ArrayList<Price>();
		Price price = new Price();
		if(promocion==1){
			price.setName("PRC - "+plan.getPLAN_DOX().trim()+" - "+plan.getCARACT_PLAN()+" - PromoOnnet");
			price.setPrioridad(57000L);
			List<Prit> pritPromoList = generarPRITPromo(bo, plan, contLink);
			price.setPritList(pritPromoList);
			priceList.add(price);
			bo.setPriceList(priceList);
		}else{
			if(tipoBolsa==2){//Bolsa paralela
				if(plan.getCARACT_PLAN().equals(IConstants.BOLSA_PARALELA)||plan.getCARACT_PLAN().contains("Bolsa Paralela -")){
					price.setName("PRC - "+plan.getPLAN_DOX().trim()+" - "+plan.getCARACT_PLAN()+"_Onnet");
				}else{
					price.setName("PRC - "+plan.getPLAN_DOX().trim()+" - "+plan.getCARACT_PLAN()+"_Onnet_Fijo");
				}
				price.setPrioridad(46000L);
			}else{
				price.setName("PRC - "+plan.getPLAN_DOX().trim()+" - "+plan.getCARACT_PLAN());
				price.setPrioridad(47000L);
			}
			int entry = 0;
			
			if(tipoBolsa!=5&&tipoBolsa!=6&&tipoBolsa!=7&&tipoBolsa!=9){
				Prit prit = generarPRITComun(bo, price, plan, 0, plan.getCARACT_PLAN(), tipoBolsa, ++entry, contLink);//Prit comun todas las bolsas
		     if(plan.getTIPO_PLAN()!=null){
				if(plan.getTIPO_PLAN().equals("FAMILIA")){
					Prit pritEleg  = generaPritFamilia(bo,price,tipoBolsa,entry);
					price.getPritList().add(pritEleg);
								}
				}				
				price.getPritList().add(prit);
			}
			boolean planAbierto=false;
			if(plan.getTIPOPLAN().equalsIgnoreCase("Abierto")||plan.getTIPOPLAN().equalsIgnoreCase("Mixto")){
				planAbierto=true;
				entry = generarPritRate(bo, tipoBolsa, price, plan.getCARACT_PLAN(), ++entry, bo.getContLink()!=0?bo.getContLink():contLink);
			}

			if((tipoBolsa!=5&&tipoBolsa!=6&&tipoBolsa!=7&&tipoBolsa!=9)||planAbierto){
				priceList.add(price);
			}

			if(tipoBolsa==2){// genera price de bolsa paralela para todo destino
				price = new Price();
				Prit prit = new Prit();
				price.setName("PRC - "+plan.getPLAN_DOX().trim()+" - "+plan.getCARACT_PLAN()+"_TDestino");
				price.setPrioridad(47000L);
				prit = generarPRITComun(bo, price, plan, 1, plan.getCARACT_PLAN(), tipoBolsa, 1, bo.getContLink());//Segundo prit TDestino para bolsa paralela
				price.getPritList().add(prit);
				priceList.add(price);
			}
			bo.setPaquete(null);
			bo.setPaqueteLDI(null);
			price = new Price();
			price.setName("PRC - CFM - "+plan.getPLAN_DOX().trim());
			price.setPrioridad(77000L);
			Prit pritCFM = generarPritCFM(bo, plan, plan.getCARACT_PLAN(),null,null);
			price.getPritList().add(pritCFM);
			priceList.add(price);
			consultarGenericos(bo, plan, priceList, panama);	
			bo.setPriceList(priceList);
		}
		if(bo.getContLink()==0)bo.setContLink(contLink);

		plan.setPlanesAsocList(dataDao.getPlanesAsociados(plan.getTMCODE()));
		
		/*
		 * Se adiciona regla para filtar información de TMCODEs 
		 * repetidos al generar la tabla de TTs. POr lógica de 
		 * negocio se define que solo deben asociar la información
		 * del plan Básico, mas no de promociones y 12X12
		 */
		if (isPlanBasico == 1){
		 bo.setPlan_basico(1);
		}
		
		//Caso de Suspension, para que se pueda crear la PO.
		if (plan.getTMCODE() < 0){
			bo.setName(plan.getPLAN_DOX().trim());
		}

		return bo;
	}

	public BillingOffer generarBOPromo12x12(Plan plan, boolean panama) throws Exception{
		int tipoBolsa = evaluaTipoBolsa(plan);
		String descripcion = "";
		if(plan.getDESCRIPCION1()!=null)descripcion+=plan.getDESCRIPCION1();
		if(plan.getDESCRIPCION2()!=null)descripcion+=plan.getDESCRIPCION2();
		if(plan.getDESCRIPCION3()!=null)descripcion+=plan.getDESCRIPCION3();
		if(plan.getDESCRIPCION4()!=null)descripcion+=plan.getDESCRIPCION4();
		if(plan.getDESCRIPCION5()!=null)descripcion+=plan.getDESCRIPCION5();
		if(plan.getDESCRIPCION6()!=null)descripcion+=plan.getDESCRIPCION6();
		if(plan.getDESCRIPCION7()!=null)descripcion+=plan.getDESCRIPCION7();
		if(plan.getDESCRIPCION8()!=null)descripcion+=plan.getDESCRIPCION8();
		BillingOffer bo = new BillingOffer();
		String nombreBO = plan.getPLAN_DOX().trim()+" - Promo12x12";
		bo.setDurationA("Months");
		bo.setDurationB(12);
		bo.setTipoBolsa(tipoBolsa);
		bo.setPlan(plan);
		bo.setTmcode(plan.getTMCODE());
		bo.setName(nombreBO);
		bo.setCode(nombreBO);
		bo.setLevel("Subscriber");
		bo.setType("Additional offer");
		//bo.setEffectiveDate("2011/01/01");
		bo.setEffectiveDate(plan.getEFFECTIVE_DATE());
		bo.setSaleEffectiveDate(plan.getFECHA_INICIO());
		bo.setSaleExpirationdate(plan.getFECHA_VENTA());
		bo.setDeployIndicator("FALSE");
		bo.setDescripcion(descripcion);
		//if(plan.getDESCRIPCION_COM()!=null)
		//          bo.setDescripcion(plan.getDESCRIPCION_COM());
		bo.setProductType("GSM");
		bo.setCurrency("COP");
		List<Price> priceList = new ArrayList<Price>();
		Price price = new Price();
		price.setName("PRC - "+plan.getPLAN_DOX().trim()+" - "+plan.getCARACT_PLAN()+" - Promo12x12");
		price.setPrioridad(78000L);
		List<Prit> pritPromoList = generarPRITPromo12x12(bo, plan);
		price.setPritList(pritPromoList);
		priceList.add(price);
		bo.setPriceList(priceList);
		bo.getPlan().getTIPO_PLAN();
		return bo;
	}
	
	public void generaBolsaUnica(Prit prit){
		prit.getUsageCharge().setServiceFilter("NA");
		prit.getUsageCharge().setService_filter_groupB("WRL_VOZTDEST");
	}

	public void generaBolsaParalela(Prit prit, int bolsaParalela, String categPlan){
		if(bolsaParalela==0){
			if(categPlan.equals(IConstants.BOLSA_PARALELA)){
				prit.getUsageCharge().setServiceFilter(mapServiceFilterList.get(26).getName());//ONNET
				prit.getUsageCharge().setService_filter_groupB("SFG NA");
			}else{
				prit.getUsageCharge().setServiceFilter("NA");
				prit.getUsageCharge().setService_filter_groupB("WRL_VOZONFI");
			}
		}else if(bolsaParalela==1){
			prit.getUsageCharge().setServiceFilter("NA");
			prit.getUsageCharge().setService_filter_groupB("WRL_VOZTDEST");
		}
	}

	public void generaBolsaO(Prit prit){
		prit.getUsageCharge().setServiceFilter("NA");
		prit.getUsageCharge().setService_filter_groupB("WRL_VOZTDEST");
	}

	public void generaBolsaComcelClaro(Prit prit){
		prit.getUsageCharge().setServiceFilter(mapServiceFilterList.get(26).getName());//ONNET
		prit.getUsageCharge().setService_filter_groupB("SFG NA");
	}

	public void generaBolsaSms(Prit prit){
		prit.getUsageCharge().setServiceFilter(mapServiceFilterList.get(39).getName());//SMS_ONNET
		prit.getUsageCharge().setService_filter_groupB("SFG NA");
	}

	public void generaBolsaVCall(Prit prit){
		prit.getUsageCharge().setServiceFilter(mapServiceFilterList.get(43).getName());//VCALL
		prit.getUsageCharge().setService_filter_groupB("SFG NA");
	}

	/**
	 * 
	 * @param plan
	 * @return
	 */
	public int evaluaTipoBolsa(Plan plan){
		System.out.println("Plan :" +plan.getTMCODE());
		if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_UNICA)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_UNICA_BB)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_UNICA_BB_GPRS)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_UNICA_GPRS)
				||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_UNICA_INHOUSE)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_UNICA_INHOUSE_DATOS)){
			return 1;//Bolsa Unica
		}else if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_C_F)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_C_F_BB)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_C_F_BB_GPRS)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_C_F_GPRS)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_LDI)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_GPRS)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_GPRS_BB)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_PARALELA_GPRS_BB)){
			return 2;//Bolsa Paralela
		}else if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_BB)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_C_F)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_C_F_BB_GPRS)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_GPRS)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_F_O)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_C_F_BB)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_C_F_GPRS)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_GPRS_BB)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_O_LDI)){
			return 3;//Bolsa O
		}else if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_COMCEL)){
			return 4;//Bolsa Comcel
		}else if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.SMS)){
			return 5;//SMS
		}else if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.VIDEO_LLAMADA)){
			return 6;//VideoLlamada
		}else if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BB)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BB_GPRS)||
				plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.DATOS)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.DATOS_INHOUSE)){
			return 7;//Datos
		}else if(plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_CLARO)||plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_CLARO_DATOS)){
			return 8;//Bolsa Claro
		}else if (plan.getCARACT_PLAN().equalsIgnoreCase(IConstants.BOLSA_DUMMY)){
			return 9;//Bolsa Dummy
		}else if (plan.getCARACT_PLAN().contains(IConstants.PREPAGO))
			return 10;// Bolsa Prepago
		return 0;
	}

	public Prit generarPritCFM(BillingOffer bo, Plan plan, String bolsa, Paquete paquete, PaqueteLDI paqueteLDI){
		Prit prit = new Prit();
		RecurringCharge recurringCharge = new RecurringCharge();
		recurringCharge.setEntryIndex(1L);
		if(bolsa!=null)
			recurringCharge.setName("PRIT - RC - "+bo.getName()+" - "+bolsa);
		else
			recurringCharge.setName("PRIT - RC - "+bo.getName());
		recurringCharge.setRole("Recurring charge");
		recurringCharge.setPit("**RC flat rate");
		recurringCharge.setCreate_multiple_charges_indicator("No");
		recurringCharge.setFrequency("Monthly");
		if(bo.getPlan()!=null){
			if(bo.getTipoBolsa()==1||bo.getTipoBolsa()==2||bo.getTipoBolsa()==3||bo.getTipoBolsa()==4||bo.getTipoBolsa()==8){
				recurringCharge.setCharge_code("WRL_RCVOZ");
			}else if(bo.getTipoBolsa()==5){
				recurringCharge.setCharge_code("WRL_RCSMSONT");
			}else if(bo.getTipoBolsa()==6){
				recurringCharge.setCharge_code("WRL_RCVCALL");
			}else if(bo.getTipoBolsa()==7){
				if(bo.getPlan().getCARACT_PLAN().equals(IConstants.BB))recurringCharge.setCharge_code("WRL_RCBBPL");
				else recurringCharge.setCharge_code("WRL_RCGPRSPL");
			}else if(bo.getTipoBolsa()==9){
				recurringCharge.setCharge_code("DUMMY");
			}
		}else if(bo.getPaquete()!=null){	
			if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("Internet")){
				recurringCharge.setCharge_code("WRL_RCGPRSPQ");
			}else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("SMS")){
				recurringCharge.setCharge_code("WRL_RCSMSONT");
			}else if(bo.getPaquete().getTIPO_ALLOWANCE().trim().equalsIgnoreCase("Blackberry")){
				recurringCharge.setCharge_code("WRL_RCBBPQ");
			}else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("VideoLlamada")){
				recurringCharge.setCharge_code("WRL_RCVCALL");
			}else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("FnFVozFijo")){
			    recurringCharge.setCharge_code("WRL_RCFFVFI");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("FnFVozOnnet")){
			    recurringCharge.setCharge_code("WRL_RCFFVON");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("FnFSMSOnnet")){
			    recurringCharge.setCharge_code("WRL_RCFFSMS");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("RollOver")){
			    recurringCharge.setCharge_code("WRL_ROLLOVER");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("IdLlamada")){
			    recurringCharge.setCharge_code("WRL_IDCALL");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("IdLlamadaRes")){
			    recurringCharge.setCharge_code("WRL_IDCALLR");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("ChatPack")){
			    recurringCharge.setCharge_code("WRL_RCCHATPK");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("WindowsMobile")){
			    recurringCharge.setCharge_code("WRL_RCWINMOB");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("Synchronica")){
			    recurringCharge.setCharge_code("WRL_RCSYNCHR");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("VozAVoz")){
			    recurringCharge.setCharge_code("WRL_RVOZAVOZ");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("Cascadas")){
			    recurringCharge.setCharge_code("WRL_CASCADAS");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("RingBackTone")){
			    recurringCharge.setCharge_code("WRL_RCRBT");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("DetalleLlamada")){
			    recurringCharge.setCharge_code("CALL_DETAILS");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("MarcaYa")){
			    recurringCharge.setCharge_code("WRL_MARCAYA");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("NeverLostCall")){
			    recurringCharge.setCharge_code("WRL_LLAMESPERA");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("WEBTV")){
			    recurringCharge.setCharge_code("WRL_IDEASWEBTV");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("WAP")){
			    recurringCharge.setCharge_code("WRL_RCWAP");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("Andiasistencia")){
			    recurringCharge.setCharge_code("WRL_ANDIASIST");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("AsistenciaLogy")){
			    recurringCharge.setCharge_code("WRL_CIRAZULLG");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("AsistenciaLabor")){
			    recurringCharge.setCharge_code("WRL_CIRAZULLF");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("AsistenciaClaro")){
			    recurringCharge.setCharge_code("WRL_CIRAZUL");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("BuzonVoz")){
			    recurringCharge.setCharge_code("WRL_RCVM");
		    }else if(bo.getPaquete().getTIPO_ALLOWANCE().equalsIgnoreCase("NokiaMessaging")){
			    recurringCharge.setCharge_code("WRL_RCNKAMS");
		    }		
		}else if(bo.getPaqueteLDI()!=null){
			recurringCharge.setCharge_code("WRL_RCLDIINF");
			recurringCharge.setTaxServiceType("20");
		}
		recurringCharge.setProration_method("Prorated");
		recurringCharge.setFrequency_of_payments("Generation frequency 1");
		recurringCharge.setRC_level("Subscriber");
		recurringCharge.setPayment_timing("Advance");
		recurringCharge.setBoundary_date_of_Special_suspension_rate("01/01/1960");//"1960/01/01"
		recurringCharge.setImmediate_payment_required("No");
		if(plan!=null){
			recurringCharge.setRate(plan.getCFM());
			recurringCharge.setImmediate_payment_required("No");
			recurringCharge.setTaxServiceType(plan.getIVA());
			//------------------------------------Codigo 27-09-2012---------------------------------------
			recurringCharge.setPer_for_downgrade(plan.getPERCENTAGE_FOR_DOWNGRADE());
			//--------------------------------------------------------------------------------------------
		}
		if (paquete!=null){
			recurringCharge.setRate(paquete.getCFM());
			recurringCharge.setTaxServiceType(paquete.getIMPUESTO()!=null?String.valueOf(paquete.getIMPUESTO()):"");
		}		
		if(paqueteLDI!=null){
			recurringCharge.setRate(paqueteLDI.getCFM());
		}
		recurringCharge.setAllow_RC_override_type("No");
		recurringCharge.setDiscount_percentage_per_suspension_reasonA("RLC_Discount_Percentage_per_Suspension_Reason");
		recurringCharge.setDiscount_percentage_per_suspension_reasonB("SPT Internet - Equipment");
		recurringCharge.setCharge_codes_objectA("RLC_Charge_codes");
		recurringCharge.setCharge_codes_objectB("Charge codes");
		recurringCharge.setAlternative_Charge_codes_objectA("RLC_Alternative_Charge_codes");
		prit.setRecurringCharge(recurringCharge);
		prit.setUsageCharge(null);
		return prit;
	}
	
	public Prit generarPritCFMEspecial(BillingOffer bo, Plan plan, String bolsa, Paquete paquete, PaqueteLDI paqueteLDI){
		Prit prit = new Prit();
		RecurringCharge recurringCharge = new RecurringCharge();
		recurringCharge.setEntryIndex(1L);
		if(bolsa!=null)
			recurringCharge.setName("PRIT - RC - "+bo.getName()+" - "+bolsa);
		else
			recurringCharge.setName("PRIT - RC - "+bo.getName());
		recurringCharge.setRole("Recurring charge");
		recurringCharge.setPit("**RC flat rate");
		recurringCharge.setCreate_multiple_charges_indicator("No");
		recurringCharge.setFrequency("Monthly");
		recurringCharge.setCharge_code(paquete.getCHARGE_CODE());
		recurringCharge.setTaxServiceType("20");
		
		recurringCharge.setProration_method("Prorated");
		recurringCharge.setFrequency_of_payments("Generation frequency 1");
		recurringCharge.setRC_level("Subscriber");
		recurringCharge.setPayment_timing("Advance");
		recurringCharge.setBoundary_date_of_Special_suspension_rate("01/01/1960");
		recurringCharge.setImmediate_payment_required("No");
		if(plan!=null){
			recurringCharge.setRate(plan.getCFM());
			recurringCharge.setImmediate_payment_required("No");
			recurringCharge.setTaxServiceType(plan.getIVA());
			//------------------------------------Codigo 27-09-2012---------------------------------------
			recurringCharge.setPer_for_downgrade(plan.getPERCENTAGE_FOR_DOWNGRADE());
			//--------------------------------------------------------------------------------------------
		}
		if (paquete!=null){
			recurringCharge.setRate(paquete.getCFM());
			recurringCharge.setTaxServiceType(paquete.getIMPUESTO()!=null?String.valueOf(paquete.getIMPUESTO()):"");
		}
		
		if(paqueteLDI!=null){
			recurringCharge.setRate(paqueteLDI.getCFM());
		}
		recurringCharge.setAllow_RC_override_type("No");
		recurringCharge.setDiscount_percentage_per_suspension_reasonA("RLC_Discount_Percentage_per_Suspension_Reason");
		recurringCharge.setDiscount_percentage_per_suspension_reasonB("Discount percentage per suspension reason");
		recurringCharge.setCharge_codes_objectA("RLC_Charge_codes");
		recurringCharge.setCharge_codes_objectB("Charge codes");
		recurringCharge.setAlternative_Charge_codes_objectA("RLC_Alternative_Charge_codes");
		prit.setRecurringCharge(recurringCharge);
		prit.setUsageCharge(null);
		return prit;
	}
	
	
	private List<Prit> generarPritOfertasDemanda(OfertasDemanda ofertaDemanda){				
		/***
		 * Consulta info BD y almacena en objeto Prit.
		 */		
		List<Prit> returnPritList = new ArrayList<Prit>();		
		List<RecurringCharge> lstRecurringCharge = new ArrayList<RecurringCharge>();
		List<UsageCharge> lstUsageCharge = new ArrayList<UsageCharge>();
		List<OneTimeCharge> lstOneTimeCharge = new ArrayList<OneTimeCharge>();
		
		if (ofertaDemanda.getRecurring_charge() == 1){
				
				lstRecurringCharge = dataDao.getPriceOfertaDemandaRC(ofertaDemanda.getId());
				long entry = 0;
				for (RecurringCharge recurringCharge : lstRecurringCharge) {
					recurringCharge.setEntryIndex(++entry);
					Prit prit = new Prit();
					prit.setRecurringCharge(recurringCharge);
					prit.setUsageCharge(null);
					prit.setOneTimeCharge(null);
					returnPritList.add(prit);
				}
		}		
		if (ofertaDemanda.getUsage_charge() == 1){
			
			lstUsageCharge = dataDao.getPriceOfertaDemandaUC(ofertaDemanda.getId());
			int entry = 0;
			for (UsageCharge usageCharge : lstUsageCharge) {
				usageCharge.setEntryIndex(++entry);
				usageCharge.setCharge_codes_objectB("Charge codes");
				usageCharge.setRLC_Tax_change("RLC_Tax_change");
				usageCharge.setTaxchange("Tax change");
				List<Value> lstValue = dataDao.getValue(ofertaDemanda.getId());
				if (usageCharge.getRTR_EntryIndex() != null && lstValue.size() >= 0){
					usageCharge.setLstValue(lstValue);
				}				
				Prit prit = new Prit();
				prit.setRecurringCharge(null);
				prit.setUsageCharge(usageCharge);
				prit.setOneTimeCharge(null);
				returnPritList.add(prit);
			}
	}		
		if (ofertaDemanda.getOne_time_charge() == 1){
			
			lstOneTimeCharge = dataDao.getPriceOfertaDemandaOTC(ofertaDemanda.getId());
			int entry = 0;
			for (OneTimeCharge oneTimeCharge : lstOneTimeCharge) {
				oneTimeCharge.setEntryIndex(++entry);
				oneTimeCharge.setCharCodesObject("Charge codes");
				oneTimeCharge.setCharCodesObjecTemp("RLC_Charge_codes");
				Prit prit = new Prit();
				prit.setRecurringCharge(null);
				prit.setUsageCharge(null);
				prit.setOneTimeCharge(oneTimeCharge);
				returnPritList.add(prit);
			}
	}	
		return returnPritList;
	}

	/**
	 * Método que genera el PRIT común para las bolsas. El PRIT común es el que define el allowance del plan
	 * @param bo
	 * @param price
	 * @param plan
	 * @param bolsaParalela
	 * @param strBolsa
	 * @param tipoBolsa
	 * @param entry
	 * @param contLink
	 * @return
	 */
	public Prit generarPRITComun(BillingOffer bo, Price price, Plan plan, int bolsaParalela, String strBolsa, int tipoBolsa, int entry, int contLink){
		Prit prit = new Prit();
		UsageCharge usageCharge = new UsageCharge();
		usageCharge.setEntryIndex(entry);
		usageCharge.setIntercalatedPromotionCycles(0);
		usageCharge.setQPP_Incremental("Yes");
		usageCharge.setURI_Incremental("Yes");
		usageCharge.setURI_EntryIndex(1);
		usageCharge.setQPP_EntryIndex(1);
		//System.out.println(bo.getTmcode() + " "+"QPP");
		if(tipoBolsa==2){
			if(bolsaParalela==0){
				if(plan.getCARACT_PLAN().equals(IConstants.BOLSA_PARALELA)||plan.getCARACT_PLAN().contains("Bolsa Paralela -")){
					usageCharge.setName("PRIT - ALLW - "+bo.getName()+" - "+strBolsa+" Onnet");
				}else{
					usageCharge.setName("PRIT - ALLW - "+bo.getName()+" - "+strBolsa+" Onnet_Fijo");
				}
				usageCharge.setPit("**Allowance duration recurring per period and call type with relative quota");
				bo.setATRMAP_TOPO("X");
				usageCharge.setIntercalatedPromotionCycles(null);
				usageCharge.setQPP_Incremental("No");
				usageCharge.setQCTP_Incremental("Yes");
				usageCharge.setURI_Incremental("Yes");
				usageCharge.setQPP_UOMForQuantity("Minutes");
				usageCharge.setQCTP_UOMForQuantity("Minutes");
			}
			if(bolsaParalela==1){
				usageCharge.setName("PRIT - ALLW - "+bo.getName()+" - "+strBolsa+" TDestino");
				usageCharge.setPit("**Allowance duration recurring per period");
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("Yes");
				usageCharge.setURI_EntryIndex(1);
				usageCharge.setQPP_EntryIndex(1);
				usageCharge.setQPP_UOMForQuantity("Minutes");
			}
		}else if(tipoBolsa==1 || tipoBolsa==4 || tipoBolsa==6 || tipoBolsa==8){
			usageCharge.setName("PRIT - ALLW - "+bo.getName()+" - "+strBolsa);
			usageCharge.setPit("**Allowance duration recurring per period");
			usageCharge.setQPP_Incremental("No");
			usageCharge.setURI_Incremental("Yes");
			usageCharge.setURI_EntryIndex(1);
			usageCharge.setQPP_EntryIndex(1);
			usageCharge.setQPP_UOMForQuantity("Minutes");
		}else if(tipoBolsa==5){
			usageCharge.setName("PRIT - ALLW - "+bo.getName()+" - "+strBolsa);
			usageCharge.setPit("**Allowance occurrence recurring per period");
			usageCharge.setQPP_Incremental("No");
			usageCharge.setURI_Incremental("No");
		
		}else{
			usageCharge.setName("PRIT - ALLW - "+bo.getName()+" - "+strBolsa);
			usageCharge.setPit("**Allowance duration recurring per period and call type with relative quota");
			bo.setATRMAP_TOPO("X");
			usageCharge.setIntercalatedPromotionCycles(null);
			usageCharge.setQPP_Incremental("No");
			usageCharge.setQCTP_Incremental("Yes");
			usageCharge.setURI_Incremental("Yes");
			usageCharge.setQPP_UOMForQuantity("Minutes");
			usageCharge.setQCTP_UOMForQuantity("Minutes");
		}
		if(tipoBolsa!=5){
			usageCharge.setDefaultRollingId(plan.getROLLOVER().equals("S")?"Yes":"No");
			usageCharge.setPeriodSensitivityPolicy("Map");
			usageCharge.setQPP_UOMForQuantity("Minutes");
			usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
			usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			contLink++;
		}else{
			usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
			usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			contLink++;
		}
		usageCharge.setRole("Allowance");
		usageCharge.setRollingPolicy("LIFO");
		usageCharge.setShouldProrate("No");
		if(bo.getPlan().getTIPOPLAN().equals("Mixto")){
		       usageCharge.setUtilizeZeroForRate("Yes");}
		else usageCharge.setUtilizeZeroForRate("No");
		usageCharge.setNumber_of_cycles_to_roll(0);
		usageCharge.setSpecial_day_setA("SpecialDaySetRef");
		usageCharge.setSpecial_day_setB("Special day set");
		usageCharge.setPeriod_setA("PeriodSetRef");
		usageCharge.setPeriod_setB("Single period period set1");
		usageCharge.setService_filter_groupA("RLC_Service_filter_group");
		usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
		usageCharge.setCharge_codes_objectB("Charge codes");
		

		if(bolsaParalela!=1&&tipoBolsa!=1&&tipoBolsa!=4&&tipoBolsa!=5&&tipoBolsa!=6&&tipoBolsa!=8){
			usageCharge.setQCTP_EntryIndex(1);
			//System.out.println(bo.getTmcode() + " "+"QCTP");
			usageCharge.setQCTP_Incremental("Yes");
			usageCharge.setQCTP_UOMForQuantity("Minutes");
			int aux=0;

			if(tipoBolsa==2&&bo.getPlan().getCARACT_PLAN().contains(IConstants.BOLSA_PARALELA_C_F)&&!price.getName().contains("TDestino"))aux=contLink+1;
			else if(bo.getTipoBolsa()==2&&bo.getPlan().getCARACT_PLAN().equals(IConstants.BOLSA_PARALELA)&&!price.getName().contains("TDestino"))aux=contLink;
			else if(tipoBolsa==3)aux=contLink+2;

			usageCharge.setQCTP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":L"+aux+"\"");
			usageCharge.setQCTP_PEComplexAttributeLink("'Values'!B"+contLink+":L"+aux);

			if(tipoBolsa==2&&bo.getPlan().getCARACT_PLAN().contains(IConstants.BOLSA_PARALELA_C_F)&&!price.getName().contains("TDestino"))contLink+=2;
			else if(bo.getTipoBolsa()==2&&bo.getPlan().getCARACT_PLAN().equals(IConstants.BOLSA_PARALELA)&&!price.getName().contains("TDestino"))contLink++;
			else if(tipoBolsa==3)contLink+=3;

			usageCharge.setQCTP_DimensionA("EnumerationDomain");
			//------------------ Adicion de codigo 10/04/2012  ----------------------------------------------
			if (usageCharge.getPit().toString().equals("**Allowance duration recurring per period and call type with relative quota"))
				  usageCharge.setQCTP_DimensionB("Call type for duration OR plan");
			else usageCharge.setQCTP_DimensionB("");
			//------------------------------------------------------------------------------------------------
		}
		//Unit Rate Ind
		usageCharge.setURI_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
		usageCharge.setURI_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
		usageCharge.setURI_DimensionA("EnumerationDomain");
		//------------------ Adicion de codigo 10/04/2012  ----------------------------------------------
		if (usageCharge.getPit().toString().equals("**Allowance duration recurring per period and call type with relative quota"))
			  usageCharge.setURI_DimensionB("Call type for duration OR plan");
			else
			  usageCharge.setURI_DimensionB("");		
		//------------------------------------------------------------------------------------------------
		
		//usageCharge.setURI_DimensionB("Call type");
		//contLink++;
		if(bo.getTipoBolsa()==1||bo.getTipoBolsa()==2||bo.getTipoBolsa()==3){
			if(bo.getTipoBolsa()==2&&bo.getPlan().getCARACT_PLAN().contains(IConstants.BOLSA_PARALELA_C_F)&&!price.getName().contains("TDestino")){
				contLink+=2;
			}else if(bo.getTipoBolsa()==2&&bo.getPlan().getCARACT_PLAN().equals(IConstants.BOLSA_PARALELA)&&!price.getName().contains("TDestino")){
				contLink++;
			}else{
				contLink+=3;
			}
		}else{
			contLink+=1;
		}
		
		//Unit Rate Ind
		if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
			usageCharge.setPeriod_setB("Peak/Offpeak period set");
		}
		prit.setUsageCharge(usageCharge);
		if(tipoBolsa==1){
			generaBolsaUnica(prit);
		}else if(tipoBolsa==2){
			generaBolsaParalela(prit, bolsaParalela, plan.getCARACT_PLAN());
		}else if(tipoBolsa==3){
			generaBolsaO(prit);
		}else if(tipoBolsa==4){
			generaBolsaComcelClaro(prit);
		}else if(tipoBolsa==5){
			generaBolsaSms(prit);
		}else if(tipoBolsa==6){
			generaBolsaVCall(prit);
		}else if(tipoBolsa==8){
			generaBolsaComcelClaro(prit);
		}
		prit.setRecurringCharge(null);
		if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
			usageCharge.setPeriod_setB("Peak/Offpeak period set");
		}
		bo.setContLink(contLink);
		return prit;
	}

	public int generarPritRate(BillingOffer bo, int tipoBolsa, Price price, String bolsa, int entry, int contLink){
		UsageCharge usageCharge = new UsageCharge();
		Prit prit = new Prit();
		int numPrits=1;
		if(tipoBolsa==2||tipoBolsa==3)numPrits=3;
		for(int i=0;i<numPrits;i++){
			prit = new Prit();
			usageCharge = new UsageCharge();
			usageCharge.setEntryIndex(entry++);
			if(tipoBolsa==2||tipoBolsa==3){
				usageCharge.setService_filter_groupB("SFG NA");
				if(i==0){
					usageCharge.setName("PRIT - RATE ONNET - "+bo.getName()+" - "+bolsa);
					usageCharge.setServiceFilter(mapServiceFilterList.get(26).getName());//ONNET
					usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(30).getName());//"SFtoCC VOZ ONNET"
				}
				if(i==1){
					usageCharge.setName("PRIT - RATE FIJO - "+bo.getName()+" - "+bolsa);
					usageCharge.setServiceFilter(mapServiceFilterList.get(5).getName());//FIJO
					usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(5).getName());//"SFtoCC VOZ FIJO"
				}
				if(i==2){
					usageCharge.setName("PRIT - RATE OFFNET - "+bo.getName()+" - "+bolsa);
					usageCharge.setServiceFilter(mapServiceFilterList.get(25).getName());//OFFNET
					usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(28).getName());//"SFtoCC VOZ OFFNET"
				}
			}else{
				usageCharge.setName("PRIT - RATE - "+bo.getName()+" - "+bolsa);
				usageCharge.setServiceFilter("NA");
				usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(8).getName());//"SFtoCC VOZ TDESTINO"
				usageCharge.setService_filter_groupB("WRL_VOZTDEST");
			}
			usageCharge.setRole("Rate");
			usageCharge.setPit("**Rate duration per period");
			usageCharge.setPeriodSensitivityPolicy("Map");
			usageCharge.setRoundingFactorCantidad(60);
			usageCharge.setRoundingFactorMedida("Seconds");
			usageCharge.setRoundingMethod("Up");
			usageCharge.setMinimum_unitA("Seconds");
			usageCharge.setMinimum_unitB(0);
			usageCharge.setSpecial_day_setA("SpecialDaySetRef");
			usageCharge.setSpecial_day_setB("Special day set");
			usageCharge.setPeriod_setA("PeriodSetRef");
			usageCharge.setTaxchange("Tax change");
			usageCharge.setRLC_Tax_change("RLC_Tax_change");
			usageCharge.setPeriod_setB("Single period period set1");
			usageCharge.setService_filter_to_charge_codeA("RLC_Service_filter_to_charge_code");
			usageCharge.setService_filter_groupA("RLC_Service_filter_group");
			usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
			usageCharge.setCharge_codes_objectB("Charge codes");
			usageCharge.setRPP_EntryIndex(1);
			usageCharge.setRPP_Incremental("Yes");
			usageCharge.setRPP_UOMForQuantity("Minutes");
			usageCharge.setRPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
			usageCharge.setRPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			contLink++;
			if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
				usageCharge.setPeriod_setB("Peak/Offpeak period set");
			}			
			prit.setUsageCharge(usageCharge);
			prit.setRecurringCharge(null);
			price.getPritList().add(prit);			
		}
		entry = pritBuzondeVoz(bo,price,entry,contLink, bo.getPlan().getTIPOBUZON().trim());
		bo.setContLink(contLink);
		return entry;
	}
	//------------02-07-2013--Creacion de metodo para la incorporacion de Servicio de Buzon de Correo de Voz------------------------------------------
	
	public int  pritBuzondeVoz(BillingOffer bo,  Price price, int entry, int contLink, String tipobuzon){
		UsageCharge usageCharge = new UsageCharge();
		Prit prit = new Prit();		
		if(tipobuzon.equalsIgnoreCase("Evento")){
			int cont = 2;
		    for(int i= 0; i<cont;i++){
			    prit = new Prit();
			    usageCharge = new UsageCharge();	
			    if(i == 0){	
			    usageCharge.setEntryIndex(entry++);
				usageCharge.setName("PRIT - RATE - "+ "VOICE MAIL");
				usageCharge.setRole("Rate");
				usageCharge.setPit("**Rate duration intra stepped");
				usageCharge.setPeriodSensitivityPolicy("Map");
				usageCharge.setRoundingFactorCantidad(60);
				usageCharge.setRoundingFactorMedida("Seconds");
				usageCharge.setMinimum_unitA("Seconds");
				usageCharge.setMinimum_unitB(0);
				//usageCharge.setSpecial_day_setA("SpecialDaySetRef");
				usageCharge.setSpecial_day_setB("Special day set");
				usageCharge.setPeriod_setB("Single period period set1");
				usageCharge.setServiceFilter("NA");
				usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(8).getName());//"SFtoCC VOZ TDESTINO"
				usageCharge.setService_filter_groupB("WRL_VOZTDEST");
				usageCharge.setCharge_codes_objectB("Charge codes");
				usageCharge.setTaxchange("Tax change");
				usageCharge.setRSPR_EntryIndex(1);
				usageCharge.setRSPR_Incremental("Yes");
				usageCharge.setRSPR_UOMForQuantity("Minutes");	
				usageCharge.setDS_EntryIndex(1);
				usageCharge.setDS_Units("Minutes");
				usageCharge.setDSScale__EntryIndex(1);
				usageCharge.setDSScale_From(0);
				usageCharge.setDSScale_To(1);
				contLink++;	
				}
			    if(i== 1){
			    	
			    	 usageCharge.setEntryIndex(entry-1);
			    	 usageCharge.setName("");
			    	 usageCharge.setPit("**Rate duration intra stepped");
			    	 usageCharge.setDS_EntryIndex(1);
			    	 usageCharge.setDSScale__EntryIndex(2);
			    	 usageCharge.setDSScale_From(1);
					 usageCharge.setDSScale_To(-1);
					 contLink++;	
			     }
			    prit.setUsageCharge(usageCharge);
				prit.setRecurringCharge(null);
				price.getPritList().add(prit);
			    }		
		}
		else if(tipobuzon.equalsIgnoreCase("Normal")){	
			usageCharge.setEntryIndex(entry++);
			usageCharge.setName("PRIT - RATE - "+ "VOICE MAIL");
			usageCharge.setServiceFilter("NA");
			usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(8).getName());//"SFtoCC VOZ TDESTINO"
			usageCharge.setService_filter_groupB("WRL_VOZTDEST");		
			usageCharge.setRole("Rate");
			usageCharge.setPit("**Rate duration per period");
			usageCharge.setPeriodSensitivityPolicy("Map");
			usageCharge.setRoundingFactorCantidad(60);
			usageCharge.setRoundingFactorMedida("Seconds");
			usageCharge.setRoundingMethod("Up");
			usageCharge.setMinimum_unitA("Seconds");
			usageCharge.setMinimum_unitB(0);
			usageCharge.setSpecial_day_setA("SpecialDaySetRef");
			usageCharge.setSpecial_day_setB("Special day set");
			usageCharge.setPeriod_setA("PeriodSetRef");
			usageCharge.setTaxchange("Tax change");
			usageCharge.setRLC_Tax_change("RLC_Tax_change");
			usageCharge.setPeriod_setB("Single period period set1");
			usageCharge.setService_filter_to_charge_codeA("RLC_Service_filter_to_charge_code");
			usageCharge.setService_filter_groupA("RLC_Service_filter_group");
			usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
			usageCharge.setCharge_codes_objectB("Charge codes");
			usageCharge.setRPP_EntryIndex(1);
			usageCharge.setRPP_Incremental("Yes");
			usageCharge.setRPP_UOMForQuantity("Minutes");
			usageCharge.setRPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
			usageCharge.setRPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			contLink++;	
			prit.setUsageCharge(usageCharge);
			prit.setRecurringCharge(null);
			price.getPritList().add(prit);
		}
		
		return entry;
	}
	
   //-------------02-07-2013-- FIN Creacion de metodo para la incorporacion de Servicio de Buzon de Correo de Voz------------------------------------------------------------------
	public static void consultarGenericos(BillingOffer bo, Plan plan, List<Price> priceList, boolean panama){
		IDataModel model = new DataModel();
		Price price = new Price();

		if(plan.getCODIGO_ELEG()!=null){
			String elegidos = model.getPriceGenerico(plan.getCODIGO_ELEG(),panama);
			String [] elegArray = elegidos.split(",");
			for(int i=0;i<elegArray.length;i++){
				String str = elegArray[i];
				if(!str.equals("PRC - 3 FnF Onnet X Minutos Gratis-Familia")&&!str.equals("PRC - Maestra FnF Onnet 15 Minutos Gratis")){/**PIT NO DISPONIBLE, SE OBVIA MIENTRAS AMDOCS IMPLEMENTA EL PIT*/
					price = new Price();
					price.setName(str);
					price.setIndicadorGenerico(1);
					bo.setATRMAP_TOPO("X");
					priceList.add(price);
				}
			}
		}
		if(plan.getCODIGO_SMS()!=null){
			String sms = model.getPriceGenerico(plan.getCODIGO_SMS(),panama);
			price = new Price();
			price.setName(sms);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(plan.getCODIGO_GPRS()!=null){
			String gprs = model.getPriceGenericoGPRS_PL(plan.getCODIGO_GPRS());
			if (gprs == null){
				gprs = model.getPriceGenerico(plan.getCODIGO_GPRS(), panama);
			}
			price = new Price();
			price.setName(gprs);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(plan.getCODIGO_MMS()!=null){
			String mms = model.getPriceGenerico(plan.getCODIGO_MMS(),panama);
			price = new Price();
			price.setName(mms);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(plan.getCODIGO_BB()!=null){
			String bb = model.getPriceGenericoBB_PL(plan.getCODIGO_BB());
			if (bb == null){
				//bb = model.getPriceGenerico(plan.getCODIGO_BB(), panama);
			}
			price = new Price();
			price.setName(bb);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		//--------Agregar price generico ilimitado WAP para BlackBerry--------
		if(plan.getCODIGO_GPRSBB()!=null){
			String bb = model.getPriceGenericoBB_PL(plan.getCODIGO_GPRSBB());
			System.out.println("valor bb: " +bb);
			if (bb == null){
				//bb = model.getPriceGenerico(plan.getCODIGO_GPRSBB(), panama);
			}
			price = new Price();
			price.setName(bb);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
	    //--------Agregar price generico para allowance Elegidos Fijos en Bolsa-----
		if(plan.getCODIGO_EFIJO()!=null){
			String efijo = model.getPriceGenericoBB_PL(plan.getCODIGO_EFIJO());
			if (efijo == null){
				//bb = model.getPriceGenerico(plan.getCODIGO_GPRSBB(), panama);
			}
			price = new Price();
			price.setName(efijo);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}		
		
	}

	public static void consultarGenericos(BillingOffer bo,  Paquete paquete, List<Price> priceList, boolean panama){
		IDataModel model = new DataModel();
		Price price = new Price();
		
		if(paquete.getCODIGO_SMS()!=null&&paquete.getCODIGO_SMS()!="0"){
			String sms = model.getPriceGenerico(paquete.getCODIGO_SMS(),panama);
			price = new Price();
			price.setName(sms);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(paquete.getCODIGO_EFIJO()!=null&&paquete.getCODIGO_EFIJO()!="0"){
			String efijo = model.getPriceGenerico(paquete.getCODIGO_EFIJO(),panama);
			price = new Price();
			price.setName(efijo);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(paquete.getCODIGO_GPRS()!=null&&paquete.getCODIGO_GPRS()!="0"){
			String gprs = model.getPriceGenericoGPRS_PQ(paquete.getCODIGO_GPRS());
			if (gprs == null){
				gprs = model.getPriceGenerico(paquete.getCODIGO_GPRS(), panama);
			}
			price = new Price();
			price.setName(gprs);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(paquete.getCODIGO_MMS()!=null&&paquete.getCODIGO_MMS()!="0"){
			String mms = model.getPriceGenerico(paquete.getCODIGO_MMS(),panama);
			price = new Price();
			price.setName(mms);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(paquete.getCODIGO_BB()!=null&&paquete.getCODIGO_BB()!="0"){
			String bb = model.getPriceGenericoBB_PQ(paquete.getCODIGO_BB());
			if (bb == null){
				bb = model.getPriceGenerico(paquete.getCODIGO_BB(), panama);
			}
			price = new Price();
			price.setName(bb);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(paquete.getCODIGO_VLLAMADA()!=null&&paquete.getCODIGO_VLLAMADA()!="0"){
			String bb = model.getPriceGenerico(paquete.getCODIGO_VLLAMADA(),panama);
			price = new Price();
			price.setName(bb);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
		if(paquete.getCODIGO_FNF()!=null&&paquete.getCODIGO_FNF()!="0"){
			String bb = model.getPriceGenerico(paquete.getCODIGO_FNF(),panama);
			price = new Price();
			price.setName(bb);
			price.setIndicadorGenerico(1);
			bo.setATRMAP_TOPO("X");
			priceList.add(price);		
		}
		if(paquete.getCODIGO_ELESMS()!=null&&paquete.getCODIGO_ELESMS()!="0"){
			String esms = model.getPriceGenerico(paquete.getCODIGO_ELESMS(),panama);
			price = new Price();
			price.setName(esms);
			price.setIndicadorGenerico(1);
			priceList.add(price);		
		}
	}
	
	/**
	 * Método que genera los prices genéricos. Prices genéricos corresponden a los prices para SMS, GPRS, BB y VideoLlamada.
	 * Se consultan de la tabla price_genericos.
	 */
	/* (non-Javadoc)
	 * @see co.com.comcel.model.IDataModel#generarPriceGenerico(int, co.com.comcel.vo.BillingOffer, boolean)
	 */
	public List<Price> generarPriceGenerico(int contLink, BillingOffer bo, boolean panama){
		List<Price> priceList = new ArrayList<Price>();
		List<PriceGenerico> priceGenericoList = new ArrayList<PriceGenerico>(); 
		if(!panama){
			priceGenericoList = dataDao.getPricesGenericos();
		}else{
			priceGenericoList = dataDao.getPricesGenericosPanama();
		}

		Prit prit = new Prit();
		Price price = new Price();
		int tipoGenerico=0;
		for(PriceGenerico pg:priceGenericoList){
			int entry=1;
			prit = new Prit();
			price = new Price();
			price.setAllwGenerico(pg.getALLOWANCE());
			price.setRateGenerico(pg.getRATE());
			price.setName(pg.getDES_PRICE());
			price.setPrioridad(46000L);
			UsageCharge usageCharge = new UsageCharge();
			usageCharge.setEntryIndex(entry++);
			usageCharge.setName("PRIT - ALLW - "+price.getName().replaceAll("PRC - ", ""));
			usageCharge.setRole("Allowance");
			usageCharge.setQPP_Incremental("Yes");
			usageCharge.setURI_Incremental("Yes");
			usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
			usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			usageCharge.setURI_EntryIndex(1);
			usageCharge.setQPP_EntryIndex(1);
			if(pg.getTIPO_PRICE().equalsIgnoreCase("SMS")){
				tipoGenerico=1;
				usageCharge.setServiceFilter(mapServiceFilterList.get(39).getName());//SMS_ONNET
				usageCharge.setPit("**Allowance occurrence recurring per period");
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
				usageCharge.setRollingPolicy("LIFO");
				usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("BBPL")){
				tipoGenerico=2;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(1).getName());//WRL_BBPL
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
				    usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("BBPQ")){
				tipoGenerico=2;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(1).getName());//WRL_BBPQ
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
				    usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("GPRSPL")){
				tipoGenerico=3;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(15).getName());//WRL_GPRSPL
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
					usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("GPRSPQ")){
				tipoGenerico=3;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(15).getName());//WRL_GPRSPQ
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
					usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("VCALL")){
				tipoGenerico=4;
				usageCharge.setPit("**Allowance duration recurring per period");
				usageCharge.setDefaultRollingId("No");
				usageCharge.setPeriodSensitivityPolicy("Map");
				usageCharge.setRollingPolicy("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(43).getName());//VCALL
				usageCharge.setQPP_UOMForQuantity("Minutes");
				usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("Yes");
				usageCharge.setURI_EntryIndex(1);
				usageCharge.setQPP_EntryIndex(1);
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("ELEG")){
				tipoGenerico=5;
				usageCharge.setPit("**Allowance duration recurring per period for FNF- X first min free for a call");
				usageCharge.setDefaultRollingId("No");
				usageCharge.setPeriodSensitivityPolicy("Map");
				usageCharge.setRollingPolicy("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(26).getName());//Elegidos
				usageCharge.setShouldProrate("No");
				usageCharge.setQualificationCriterion("Friends and Family");
				usageCharge.setQPP_UOMForQuantity("Minutes");
				usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
				usageCharge.setQPP_EntryIndex(1);
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("Yes");
				usageCharge.setURI_EntryIndex(1);				
				usageCharge.setQCPP_EntryIndex(1);
				usageCharge.setQCPP_Incremental("Yes");
				usageCharge.setQCPP_UOMForQuantity("Minutes");
				
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("CHATPACK")){
				tipoGenerico=3;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(4).getName());//WRL_CHATPK
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
					usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("WINMOB")){
				tipoGenerico=3;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(47).getName());//WRL_WINMOB
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
					usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("SYNCHRONICA")){
				tipoGenerico=3;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(42).getName());//WRL_SYNCHR
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
					usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("NOKIAMESS")){
				tipoGenerico=3;
				usageCharge.setPit("**Allowance volume recurring per period");
				usageCharge.setRollingPolicyAllowance("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(21).getName());//WRL_NKAMS
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setQPP_UOMForQuantity("GBytes");
				}else{
					usageCharge.setQPP_UOMForQuantity("KBytes");
				}
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
			}else if(pg.getTIPO_PRICE().equalsIgnoreCase("MMS")){
				tipoGenerico=1;
				usageCharge.setServiceFilter(mapServiceFilterList.get(20).getName());//WRL_MMS
				usageCharge.setPit("**Allowance occurrence recurring per period");
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
				usageCharge.setRollingPolicy("LIFO");
				usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			}			
			else if(pg.getTIPO_PRICE().equalsIgnoreCase("EFIJO")){
				tipoGenerico=6;
				usageCharge.setPit("**Allowance duration recurring per period for FnF");
				usageCharge.setDefaultRollingId("No");
				usageCharge.setPeriodSensitivityPolicy("Map");
				usageCharge.setRollingPolicy("LIFO");
				usageCharge.setServiceFilter(mapServiceFilterList.get(5).getName());//EFijos - WRL_FIJO
				usageCharge.setQPP_UOMForQuantity("Minutes");
				usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("Yes");
				usageCharge.setURI_EntryIndex(1);
				usageCharge.setQPP_EntryIndex(1);
		    }else if(pg.getTIPO_PRICE().equalsIgnoreCase("ELESMS")){
				tipoGenerico=1;
				usageCharge.setPit("**Allowance occurrence recurring per period for FnF");
				usageCharge.setServiceFilter(mapServiceFilterList.get(10).getName());//ElegidosSMS
				usageCharge.setURI_EntryIndex(1);
				usageCharge.setQPP_EntryIndex(1);
				usageCharge.setQPP_Incremental("No");
				usageCharge.setURI_Incremental("No");
				usageCharge.setRollingPolicy("LIFO");
				usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
				
				}
			contLink++;
			usageCharge.setShouldProrate("No");			
			if(!pg.getTIPO_PRICE().equalsIgnoreCase("ELEG")&&!pg.getTIPO_PRICE().equalsIgnoreCase("ELESMS")&&!pg.getTIPO_PRICE().equalsIgnoreCase("EFIJO"))
			     usageCharge.setIntercalatedPromotionCycles(0);
			usageCharge.setUtilizeZeroForRate("No");
			usageCharge.setNumber_of_cycles_to_roll(0);
			usageCharge.setSpecial_day_setA("SpecialDaySetRef");
			usageCharge.setSpecial_day_setB("Special day set");
			usageCharge.setPeriod_setA("PeriodSetRef");
			usageCharge.setPeriod_setB("Single period period set1");
			usageCharge.setService_filter_groupA("RLC_Service_filter_group");
			usageCharge.setService_filter_groupB("SFG NA");
			usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
			usageCharge.setCharge_codes_objectB("Charge codes");
			//Unit Rate Ind
			usageCharge.setURI_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
			usageCharge.setURI_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			contLink++;
			//Unit Rate Ind
			if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
				usageCharge.setPeriod_setB("Peak/Offpeak period set");
			}
			prit.setUsageCharge(usageCharge);
			prit.setRecurringCharge(null);
			price.getPritList().add(prit);
			/**
			 * Si TIPO_PRICE es "SMS"
			 * 1. SMS
			 * 2. BB
			 * 3. GPRS
			 * 4. VCALL
			 * 5. ELEG
			 * 3. CHATPACK
			 * 3. Windows Mobile
			 * 3. SYNCHRONICA
			 * 6. ELEGIDO FIJO
			 */
			if(tipoGenerico==1 && pg.getRATE()!=null  ){	///&& !pg.getRATE().equals("0") 			
				prit = new Prit();				
				usageCharge = new UsageCharge();
				usageCharge.setEntryIndex(entry);
				usageCharge.setName("PRIT - RATE - "+price.getName().replaceAll("PRC - ", ""));
				usageCharge.setRole("Rate");
				usageCharge.setPit("**Rate occurrence per period");
				usageCharge.setServiceFilter(mapServiceFilterList.get(39).getName());//SMS_ONNET"
				usageCharge.setSpecial_day_setA("SpecialDaySetRef");
				usageCharge.setSpecial_day_setB("Special day set");
				usageCharge.setPeriod_setA("PeriodSetRef");
				usageCharge.setPeriod_setB("Single period period set1");
				usageCharge.setRLC_Tax_change("RLC_Tax_change");
				usageCharge.setTaxchange("Tax change");
				usageCharge.setService_filter_to_charge_codeA("RLC_Service_filter_to_charge_code");
				usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(47).getName());//"SFtoCC SMS ONNET"
				usageCharge.setService_filter_groupA("RLC_Service_filter_group");
				usageCharge.setService_filter_groupB("SFG NA");
				usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
				usageCharge.setCharge_codes_objectB("Charge codes");
				usageCharge.setRPP_EntryIndex(1);
				usageCharge.setRPP_Incremental("Yes");
				usageCharge.setRPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setRPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
				contLink++;
				if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
					usageCharge.setPeriod_setB("Peak/Offpeak period set");
				}
				prit.setUsageCharge(usageCharge);
				prit.setRecurringCharge(null);
				price.getPritList().add(prit);
			}else if((tipoGenerico==2)){//&& !pg.getDES_PRICE().contains("Ilimitado")
				prit = new Prit();
				usageCharge = new UsageCharge();
				usageCharge.setEntryIndex(entry);
				usageCharge.setName("PRIT - RATE - "+price.getName().replaceAll("PRC - ", ""));
				usageCharge.setRole("Rate");
				usageCharge.setPit("**Rate volume per period");
				usageCharge.setRoundingFactorMedida("KBytes");
				usageCharge.setServiceFilter(mapServiceFilterList.get(1).getName());//BB - WRL_BB
				usageCharge.setRoundingFactorCantidad(1);
				usageCharge.setPeriod_setB("Single period period set1");
				usageCharge.setRLC_Tax_change("RLC_Tax_change");
				usageCharge.setTaxchange("Tax change");
				usageCharge.setRoundingMethod("Up");
				usageCharge.setMinimum_unitA("KBytes");
				usageCharge.setMinimum_unitB(1);
				usageCharge.setSpecial_day_setA("SpecialDaySetRef");
				usageCharge.setSpecial_day_setB("Special day set");
				usageCharge.setPeriod_setA("PeriodSetRef");
				usageCharge.setPeriod_setB("Single period period set1");
				if(pg.getTIPO_PRICE().equalsIgnoreCase("BBPL")){
				  usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(57).getName());//"SFtoCC_WRL_BBPL"
				}else if (pg.getTIPO_PRICE().equalsIgnoreCase("BBPQ")){
				  usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(1).getName());//"SFtoCC_WRL_BBPQ"
				}
				usageCharge.setService_filter_to_charge_codeA("RLC_Service_filter_to_charge_code");
				usageCharge.setService_filter_groupA("RLC_Service_filter_group");
				usageCharge.setService_filter_groupB("SFG NA");
				usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
				usageCharge.setCharge_codes_objectB("Charge codes");
				usageCharge.setRPP_EntryIndex(1);
				usageCharge.setRPP_Incremental("Yes");
				if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
					usageCharge.setRPP_UOMForQuantity("GBytes");
				}else{
					usageCharge.setRPP_UOMForQuantity("KBytes");
				}
				usageCharge.setRPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				usageCharge.setRPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
				contLink++;
				if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
					usageCharge.setPeriod_setB("Peak/Offpeak period set");
				}
				prit.setUsageCharge(usageCharge);
				prit.setRecurringCharge(null);
				price.getPritList().add(prit);
			}else if(tipoGenerico==3){//&& !pg.getRATE().equals("0")
				if(!pg.getID_PRICE().contains("WAP")) {
					//------------cambio 31-10-2012 ----------------- para price wap no genera rate------
						prit = new Prit();
						usageCharge = new UsageCharge();
						usageCharge.setEntryIndex(entry);
						usageCharge.setName("PRIT - RATE - "+price.getName().replaceAll("PRC - ", ""));
						usageCharge.setRole("Rate");
						usageCharge.setPit("**Rate volume per period");						
						usageCharge.setRoundingFactorMedida("KBytes");
						usageCharge.setRoundingFactorCantidad(1);
						usageCharge.setRoundingMethod("Up");
						usageCharge.setMinimum_unitA("KBytes");
						usageCharge.setMinimum_unitB(1);
						usageCharge.setRLC_Tax_change("RLC_Tax_change");
						usageCharge.setTaxchange("Tax change");
						usageCharge.setSpecial_day_setA("SpecialDaySetRef");
						usageCharge.setSpecial_day_setB("Special day set");
						usageCharge.setPeriod_setA("PeriodSetRef");
						usageCharge.setPeriod_setB("Single period period set1");
						usageCharge.setService_filter_to_charge_codeA("RLC_Service_filter_to_charge_code");
						if(pg.getTIPO_PRICE().equalsIgnoreCase("GPRSPL")){
						  usageCharge.setServiceFilter(mapServiceFilterList.get(15).getName());//GPRS
						  usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(18).getName());//"SFtoCC GPRSPL"
						}else if(pg.getTIPO_PRICE().equalsIgnoreCase("GPRSPQ")){
						  usageCharge.setServiceFilter(mapServiceFilterList.get(15).getName());//GPRS
						  usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(58).getName());//"SFtoCC GPRSPQ"
						}else if(pg.getTIPO_PRICE().equalsIgnoreCase("NOKIAMESS")){
						  usageCharge.setServiceFilter(mapServiceFilterList.get(21).getName());//Nokia Messenger
						  usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(23).getName());//SFtoCC_WRL_NKAMS	
						}else if(pg.getTIPO_PRICE().equalsIgnoreCase("SYNCHRONICA")){
						  usageCharge.setServiceFilter(mapServiceFilterList.get(42).getName());//Synchronica
						  usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(51).getName());//SFtoCC_WRL_SYNCHR
						}else if(pg.getTIPO_PRICE().equalsIgnoreCase("WINMOB")){
						  usageCharge.setServiceFilter(mapServiceFilterList.get(47).getName());//Windows Mobile
					      usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(56).getName());//SFtoCC_WRL_WINMOB
						}else if(pg.getTIPO_PRICE().equalsIgnoreCase("CHATPACK")){
						  usageCharge.setServiceFilter(mapServiceFilterList.get(4).getName());//ChatPack Windows
						  usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(4).getName());//SFtoCC_WRL_CHATPK
						}
						usageCharge.setService_filter_groupA("RLC_Service_filter_group");
						usageCharge.setService_filter_groupB("SFG NA");
						usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
						usageCharge.setCharge_codes_objectB("Charge codes");
						usageCharge.setRPP_EntryIndex(1);
						usageCharge.setRPP_Incremental("Yes");
						if (pg.getALLOWANCE()!=null && pg.getALLOWANCE().longValue() == 99999999l){
							usageCharge.setRPP_UOMForQuantity("GBytes");
						}else{
							usageCharge.setRPP_UOMForQuantity("KBytes");
						}
						usageCharge.setRPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
						usageCharge.setRPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
						contLink++;
						if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
							usageCharge.setPeriod_setB("Peak/Offpeak period set");
						}
						prit.setUsageCharge(usageCharge);
						prit.setRecurringCharge(null);
						price.getPritList().add(prit);
					}
				//-------------------------------------------------------------------------------------
			}if(tipoGenerico== 4 && pg.getRATE()!=null){// && !pg.getRATE().equals("0")  			
				prit = new Prit();				
				usageCharge = new UsageCharge();
				usageCharge.setEntryIndex(entry);
				usageCharge.setName("PRIT - RATE - "+price.getName().replaceAll("PRC - ", ""));
				usageCharge.setRole("Rate");
				usageCharge.setPit("**Rate duration per period");	
				usageCharge.setPeriodSensitivityPolicy("Map");
				usageCharge.setServiceFilter(mapServiceFilterList.get(43).getName());//WRL_VCALL
				usageCharge.setSpecial_day_setA("SpecialDaySetRef");
				usageCharge.setSpecial_day_setB("Special day set");
				usageCharge.setPeriod_setA("PeriodSetRef");
				usageCharge.setPeriod_setB("Single period period set1");
				usageCharge.setRLC_Tax_change("RLC_Tax_change");
				usageCharge.setTaxchange("Tax change");				
				usageCharge.setRoundingFactorMedida("Seconds");
				usageCharge.setRoundingFactorCantidad(60);
				usageCharge.setRoundingMethod("Up");
				usageCharge.setMinimum_unitA("Seconds");
				usageCharge.setMinimum_unitB(0);				
				usageCharge.setService_filter_to_charge_codeA("RLC_Service_filter_to_charge_code");
				usageCharge.setService_filter_to_charge_codeB(mapServiceFilterToCharge.get(52).getName());//"SFtoCC SMS ONNET"
				usageCharge.setService_filter_groupA("RLC_Service_filter_group");
				usageCharge.setService_filter_groupB("SFG NA");
				usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
				usageCharge.setCharge_codes_objectB("Charge codes");
				usageCharge.setRPP_EntryIndex(1);
				usageCharge.setRPP_Incremental("Yes");
				usageCharge.setRPP_UOMForQuantity("Minutes");
				//usageCharge.setRPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
				//usageCharge.setRPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
				contLink++;
				if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
					usageCharge.setPeriod_setB("Peak/Offpeak period set");
				}
				prit.setUsageCharge(usageCharge);
				prit.setRecurringCharge(null);
				price.getPritList().add(prit);
			}

			priceList.add(price);
		}
		bo.setContLink(contLink);
		return priceList;
	}

	public List<Prit> generarPRITPromo(BillingOffer bo, Plan plan, int contLink){
		List<Prit> pritList = new ArrayList<Prit>();
		Prit prit = new Prit();
		UsageCharge usageCharge = new UsageCharge();
		usageCharge.setEntryIndex(1);
		usageCharge.setName("PRIT - ALLW - "+plan.getPLAN_DOX()+" - PromoOnnet");
		usageCharge.setRole("Allowance");
		usageCharge.setQPP_Incremental("Yes");
		usageCharge.setURI_Incremental("Yes");
		usageCharge.setURI_EntryIndex(1);
		usageCharge.setQPP_EntryIndex(1);
		if(plan.getTIPO_ALLOWANCE_PROMO().equals("MB")){
			usageCharge.setPit("**Allowance volume recurring per period");
			usageCharge.setRollingPolicyAllowance("LIFO");
			usageCharge.setServiceFilter(mapServiceFilterList.get(15).getName());//GPRSPL
			usageCharge.setQPP_UOMForQuantity("KBytes");
			usageCharge.setQPP_Incremental("No");
			usageCharge.setURI_Incremental("No");
		}else if(plan.getTIPO_ALLOWANCE_PROMO().equals("MINUTOS")){
			usageCharge.setPit("**Allowance duration recurring per period");
			usageCharge.setDefaultRollingId("No");
			usageCharge.setPeriodSensitivityPolicy("Map");
			usageCharge.setRollingPolicy("LIFO");
			usageCharge.setServiceFilter(mapServiceFilterList.get(43).getName());//VCALL
			usageCharge.setQPP_UOMForQuantity("Minutes");
			usageCharge.setQPP_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
			usageCharge.setQPP_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
			usageCharge.setQPP_Incremental("No");
			usageCharge.setURI_Incremental("Yes");
			usageCharge.setURI_EntryIndex(1);
			usageCharge.setQPP_EntryIndex(1);
		}
		contLink++;
		usageCharge.setShouldProrate("No");
		usageCharge.setIntercalatedPromotionCycles(0);
		usageCharge.setUtilizeZeroForRate("No");
		usageCharge.setNumber_of_cycles_to_roll(0);
		usageCharge.setSpecial_day_setA("SpecialDaySetRef");
		usageCharge.setSpecial_day_setB("Special day set");
		usageCharge.setPeriod_setA("PeriodSetRef");
		usageCharge.setPeriod_setB("Single period period set1");
		usageCharge.setService_filter_groupA("RLC_Service_filter_group");
		usageCharge.setService_filter_groupB("SFG NA");
		usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
		usageCharge.setCharge_codes_objectB("Charge codes");
		

		//Unit Rate Ind
		usageCharge.setURI_PEComplexAttributeValue("Sheet=\"Values\"; Range=\"B"+contLink+":G"+contLink+"\"");
		usageCharge.setURI_PEComplexAttributeLink("'Values'!B"+contLink+":G"+contLink);
		contLink++;
		//Unit Rate Ind

		//Si la promoción es intercalada se ejecuta este código
		if(plan.getPROMO_INTERCALACION().equals(1)){
			int mes=1;
			for(int i=1;i<plan.getMESES_PROMO()-1;i++){
				usageCharge.setEntryIndex(1);
				usageCharge.setPit("**Allowance duration recurring per period");
				usageCharge.setIntercalatedPromotionCycles(mes);
				if (i == 1){
					usageCharge.setQPP_Incremental("Yes");
					usageCharge.setQPP_UOMForQuantity("Minutes");
					usageCharge.setQPP_EntryIndex(1);
				}
				usageCharge.setURI_Incremental("Yes");
				usageCharge.setURI_EntryIndex(1);
				mes+=2;
				if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
					usageCharge.setPeriod_setB("Peak/Offpeak period set");
				}
				prit.setUsageCharge(usageCharge);
				prit.setRecurringCharge(null);
				pritList.add(prit);
				prit = new Prit();
				usageCharge = new UsageCharge();
			}
		}else{
			usageCharge.setIntercalatedPromotionCycles(plan.getPROMO_INTERCALACION());
			if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
				usageCharge.setPeriod_setB("Peak/Offpeak period set");
			}
			prit.setUsageCharge(usageCharge);
			prit.setRecurringCharge(null);
			pritList.add(prit);
		}
		if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
			usageCharge.setPeriod_setB("Peak/Offpeak period set");
		}
		bo.setContLink(contLink);
		return pritList;
		}

	public List<Prit> generarPRITPromo12x12(BillingOffer bo, Plan plan){
		List<Prit> pritList = new ArrayList<Prit>();
		Prit prit = new Prit();
		RecurringCharge recurringCharge = new RecurringCharge();
		recurringCharge.setEntryIndex(1L);
		recurringCharge.setName("PRIT - RC DiscountFlat - "+bo.getName());
		recurringCharge.setRole("RC discount");
		recurringCharge.setPit("**RC discount flat");
		recurringCharge.setProration_method("Prorated");
		recurringCharge.setDiscount(12);
		recurringCharge.setDiscount_scheme("Percentage");
		recurringCharge.setDiscount_percentage_per_suspension_reasonA("RLC_Discount_Percentage_per_Suspension_Reason");
		recurringCharge.setDiscount_percentage_per_suspension_reasonB("SPT - Seasonal Charge");
		recurringCharge.setCharge_codes_objectA("RLC_Charge_codes");
		recurringCharge.setCharge_codes_objectB("Charge codes");
		recurringCharge.setQcc_entryIndex(1);
		recurringCharge.setQcc_incremental("Yes");
		prit.setRecurringCharge(recurringCharge);
		prit.setUsageCharge(null);
		pritList.add(prit);
		return pritList;
	}
	
	public List<BillingOffer> generarBOPaquetes(boolean panama){
		List<BillingOffer> boPaqList = new ArrayList<BillingOffer>();
		List<Paquete> paqList = new ArrayList<Paquete>();
		/// Se agrega mapa para BOffer de BlackBerry 18/02/2013 --------
		Map<Long,BillingOffer> mapBo = new HashMap< Long, BillingOffer>();
		List<Price> priceList1 = new ArrayList<Price>();
		
		BillingOffer boaux = null ;
		Boolean paso = false;
		long spcode = -1;
		if(!panama){
			paqList = dataDao.getPaquetesMigracion();
		}else{
			paqList = dataDao.getPaquetesMigracionPanama();
		}
		BillingOffer bo = new BillingOffer();	
		BillingOffer bo1= new BillingOffer();
				
		Map<Long, Long> spcodeRep = new HashMap<Long, Long>();
		
		for(Paquete paq:paqList){
			 if(paq.getTIPO_ALLOWANCE().equals("Blackberry")){
							bo = new BillingOffer();
							bo.setPlan(null);
							bo.setPaqueteLDI(null);
							bo.setPaquete(paq);
							bo.setName(paq.getNVO_NOMBRE_PAQ().trim());
							bo.setCode(paq.getNVO_NOMBRE_PAQ().trim());
							bo.setEffectiveDate(paq.getEFFECTIVE_DATE());
							//bo.setSaleEffectiveDate("2007/01/01");
							bo.setSaleEffectiveDate("31/03/2014");
							bo.setCurrency("COP");
							bo.setLevel("Subscriber");
							bo.setType("Additional offer");
							bo.setDeployIndicator("FALSE");
							//bo.setDescripcion(paq.getDESCRIPCION_COM()==null?paq.getNVO_NOMBRE_PAQ().trim():paq.getDESCRIPCION_COM().trim());
							bo.setDescripcion(paq.getDESCRIPCION()==null?paq.getNVO_NOMBRE_PAQ().trim():paq.getDESCRIPCION().trim());
							bo.setProductType("GSM");
							bo.setProductType("GSM");
							consultarGenericos(bo, paq, bo.getPriceList(),panama);
				    		Price price = new Price();
							price.setName("PRC - " + paq.getNVO_NOMBRE_PAQ().trim() + " - CFM");
							price.setPrioridad(78000L);
							Prit pritCFM = generarPritCFM(bo, null, null, paq, null);				
							price.getPritList().add(pritCFM);
							bo.getPriceList().add(price);
							mapBo.put(paq.getSPCODE(), bo);							
						    spcodeRep.put(paq.getSPCODE(), paq.getSPCODE());						
						}			  		
		}		
		for(Paquete paquete:paqList){			
			if(paquete.getTIPO_ALLOWANCE().contains("BienRepo")){						
				       /// se agrega esta condicion para evaluar paq con mismo nombre o spcode -------------------
				if(paquete.getDURACION().contains("90")) {					   
					//paso= false;
					bo = new BillingOffer();
					bo.setPlan(null);
					bo.setPaqueteLDI(null);
					bo.setPaquete(paquete);
					bo.setDurationA("Months"); 
					bo.setDurationB(3);
					bo.setName(paquete.getNVO_NOMBRE_PAQ().trim()+"-GRPS-90días");
					bo.setCode(paquete.getNVO_NOMBRE_PAQ().trim()+"-GRPS-90días");
					bo.setEffectiveDate(paquete.getEFFECTIVE_DATE());
					bo.setSaleEffectiveDate("31/03/2014");
					bo.setCurrency("COP");
					bo.setLevel("Subscriber");	
					bo.setType("Additional offer");
					bo.setDeployIndicator("FALSE");
					//bo.setDescripcion(paquete.getDESCRIPCION_COM()==null?paquete.getNVO_NOMBRE_PAQUETE().trim():paquete.getDESCRIPCION_COM().trim());
					bo.setDescripcion(paquete.getDESCRIPCION()==null?paquete.getNVO_NOMBRE_PAQUETE().trim():paquete.getDESCRIPCION().trim());
					bo.setProductType("GSM");
					consultarGenericos(bo, paquete, bo.getPriceList(),panama); 
					boPaqList.add(bo);				   
				   }				
				
		    if(paquete.getDURACION().contains("30")) {	
				
				if( spcode!=paquete.getSPCODE()){
						/*if(boaux!=null){
							 boPaqList.add(boaux);
							 boaux = null;
						    }*/
						paso= false;
						bo = new BillingOffer();
						bo.setPlan(null);
						bo.setPaqueteLDI(null);
						bo.setPaquete(paquete);
						bo.setDurationA("Months");
						int duracion = 0;
						 if(bo.getPaquete().getDURACION()!=null){
							 //if(bo.getPaquete().getTIPO_ALLOWANCE().equals("Internet")){
						         if( bo.getPaquete().getDURACION().contains("30")) duracion = 1;
						         else if( bo.getPaquete().getDURACION().contains("60"))duracion =2;
						         else if(bo.getPaquete().getDURACION().contains("90"))duracion = 3;
						         else if(bo.getPaquete().getDURACION().contains("120"))duracion = 4;//}					        	 
								}
						bo.setDurationB(duracion);
						bo.setName(paquete.getNVO_NOMBRE_PAQ().trim()+"-30dias");
						bo.setCode(paquete.getNVO_NOMBRE_PAQ().trim()+"-30dias");
						bo.setEffectiveDate(paquete.getEFFECTIVE_DATE());
						bo.setSaleEffectiveDate("31/03/2014");
						bo.setCurrency("COP");
						bo.setLevel("Subscriber");	
						bo.setType("Additional offer");
						bo.setDeployIndicator("FALSE");
						//bo.setDescripcion(paquete.getDESCRIPCION_COM()==null?paquete.getNVO_NOMBRE_PAQ().trim():paquete.getDESCRIPCION_COM().trim());
						bo.setDescripcion(paquete.getDESCRIPCION()==null?paquete.getNVO_NOMBRE_PAQUETE().trim():paquete.getDESCRIPCION().trim());
						bo.setProductType("GSM");
						consulGenericRep(paquete, priceList1, panama);			
					  	Price price = new Price();
						price.setName("PRC - " + paquete.getNVO_NOMBRE_PAQ().trim() + " - CFM");
						price.setPrioridad(78000L);
						if(paso == false){boaux = bo;						
						                  paso = true;}
						}
				 else{
				     consulGenericRep(paquete, priceList1, false);		
				     }	
		           }
				 }
			  else if(paquete.getTIPO_ALLOWANCE().equals("Internet")){
				if(spcodeRep.get(paquete.getSPCODE())!=null){
					bo1 = mapBo.get(paquete.getSPCODE());
				    consultarGenericos(bo1, paquete, bo1.getPriceList(),panama);
				    mapBo.put(paquete.getSPCODE(), bo1);}	 			
				else {
					bo = new BillingOffer();
					bo.setPlan(null);
					bo.setPaqueteLDI(null);
					bo.setPaquete(paquete);
					bo.setName(paquete.getNVO_NOMBRE_PAQ().trim());
					bo.setCode(paquete.getNVO_NOMBRE_PAQ().trim());
					//bo.setEffectiveDate("2011/01/01");
					bo.setEffectiveDate(paquete.getEFFECTIVE_DATE());
					//bo.setSaleEffectiveDate("2007/01/01");
					bo.setSaleEffectiveDate("31/03/2014");
					bo.setCurrency("COP");
					bo.setLevel("Subscriber");
					bo.setType("Additional offer");
					bo.setDeployIndicator("FALSE");
				    //bo.setDescripcion(paquete.getDESCRIPCION_COM()==null?paquete.getNVO_NOMBRE_PAQ().trim():paquete.getDESCRIPCION_COM().trim());
					bo.setDescripcion(paquete.getDESCRIPCION()==null?paquete.getNVO_NOMBRE_PAQUETE().trim():paquete.getDESCRIPCION().trim());
					bo.setProductType("GSM");
					consultarGenericos(bo, paquete, bo.getPriceList(),panama);
		    		Price price = new Price();
					price.setName("PRC - " + paquete.getNVO_NOMBRE_PAQ().trim() + " - CFM");
					price.setPrioridad(78000L);
					Prit pritCFM = generarPritCFM(bo, null, null, paquete, null);				
					price.getPritList().add(pritCFM);
					bo.getPriceList().add(price);
					boPaqList.add(bo);					     
				     }			
			}else if(!paquete.getTIPO_ALLOWANCE().equals("Blackberry")){	
		    	if(paso ==true){ boaux.setPriceList(priceList1);
		    					 priceList1= new ArrayList<Price>();
		    					 boPaqList.add(boaux); 
		    					 paso = false;//boaux= null;
		    					 }		    		           
		    	bo = new BillingOffer();
				bo.setPlan(null);
				bo.setPaqueteLDI(null);
				bo.setPaquete(paquete);
				bo.setName(paquete.getNVO_NOMBRE_PAQ().trim());
				bo.setCode(paquete.getNVO_NOMBRE_PAQ().trim());
				//bo.setEffectiveDate("2011/01/01");
				bo.setEffectiveDate(paquete.getEFFECTIVE_DATE());
				bo.setSaleEffectiveDate("31/03/2014");
				if(paquete.getDURACION().contains("12 Meses")){
					bo.setDurationA("Months");
					bo.setDurationB(12);
				}
				bo.setCurrency("COP");
				bo.setLevel("Subscriber");
				bo.setType("Additional offer");
				bo.setDeployIndicator("FALSE");
				//bo.setDescripcion(paquete.getDESCRIPCION_COM()==null?paquete.getNVO_NOMBRE_PAQ().trim():paquete.getDESCRIPCION_COM().trim());
				bo.setDescripcion(paquete.getDESCRIPCION()==null?paquete.getNVO_NOMBRE_PAQUETE().trim():paquete.getDESCRIPCION().trim());
				bo.setProductType("GSM");
				consultarGenericos(bo, paquete, bo.getPriceList(),panama);
	    		Price price = new Price();
				price.setName("PRC - " + paquete.getNVO_NOMBRE_PAQ().trim() + " - CFM");
				price.setPrioridad(78000L);
				Prit pritCFM = generarPritCFM(bo, null, null, paquete, null);				
				price.getPritList().add(pritCFM);
				bo.getPriceList().add(price);
				boPaqList.add(bo);		
		        }
		     spcode = paquete.getSPCODE(); 	 		    
		}		
		Iterator<Entry<Long, BillingOffer>> i =  mapBo.entrySet().iterator();
		while(i.hasNext()){
		  @SuppressWarnings("rawtypes")
		  Map.Entry	me = (Map.Entry)i.next();	  
		  boPaqList.add((BillingOffer)me.getValue());			
		}
	  return boPaqList;
	}
	
	
	/**
	 * metodo que define una sola BO para Bienvenida Reposicion...............2013-01.28-.........
	 */	
	public void consulGenericRep(Paquete paquete, List<Price> pr, Boolean panama){
		IDataModel model = new DataModel();
		Price price = new Price();		    		
		if(paquete.getCODIGO_SMS()!=null&&paquete.getCODIGO_SMS()!="0"){
			String sms = model.getPriceGenerico(paquete.getCODIGO_SMS(),panama);
			price = new Price();
			price.setName(sms);
			price.setIndicadorGenerico(1);
			pr.add(price);		
		}if(paquete.getCODIGO_GPRS()!=null&&paquete.getCODIGO_GPRS()!="0"){
			String gprs = model.getPriceGenericoGPRS_PQ(paquete.getCODIGO_GPRS());
			if (gprs == null){
				gprs = model.getPriceGenerico(paquete.getCODIGO_GPRS(), panama);
			}
			price = new Price();
			price.setName(gprs);
			price.setIndicadorGenerico(1);
			pr.add(price);		
		}if(paquete.getCODIGO_MMS()!=null&&paquete.getCODIGO_MMS()!="0"){
			String mms = model.getPriceGenerico(paquete.getCODIGO_MMS(),panama);
			price = new Price();
			price.setName(mms);
			price.setIndicadorGenerico(1);
			pr.add(price);		
		}if(paquete.getCODIGO_VLLAMADA()!=null&&paquete.getCODIGO_VLLAMADA()!="0"){
			String bb = model.getPriceGenerico(paquete.getCODIGO_VLLAMADA(),panama);
			price = new Price();
			price.setName(bb);
			price.setIndicadorGenerico(1);
			pr.add(price);		
		}	
		
	}	
	/**
	 * Metodo sin finalizar lógica, para cargar la información copiada manualmente, 
	 * se creo la tabla: paquetes_especiales. la cual es necesario consultar y llenar el 
	 * objeto Paquete.
	 */
	public List<BillingOffer> generarBOPaquetesEspecial(){
		List<BillingOffer> boPaqList = new ArrayList<BillingOffer>();
		List<Paquete> paqList = new ArrayList<Paquete>();
		paqList = dataDao.getPaquetesEspeciales(); //Consulta que retorna la información de los paquetes Especiales.
		
		BillingOffer bo = new BillingOffer();
		for(Paquete paquete:paqList){
			
			bo = new BillingOffer();
			bo.setPlan(null);
			bo.setPaqueteLDI(null);
			bo.setPaquete(paquete);
			bo.setName(paquete.getNVO_NOMBRE_PAQ().trim());
			bo.setCode(paquete.getNVO_NOMBRE_PAQ().trim());
			//bo.setEffectiveDate("2011/01/01");
			bo.setEffectiveDate("31/03/2014");
			bo.setSaleEffectiveDate("31/03/2014");
			bo.setCurrency("COP");
			bo.setLevel("Subscriber");
			bo.setType("Additional offer");
			bo.setDeployIndicator("FALSE");
			bo.setDescripcion(paquete.getDESCRIPCION()==null?paquete.getDESCRIPCION().trim():paquete.getDESCRIPCION().trim());
			//bo.setDescripcion(paquete.getDESCRIPCION_COM()==null?paquete.getDESCRIPCION_COM().trim():paquete.getDESCRIPCION_COM().trim());
			bo.setProductType("GSM");
			Price price = new Price();
			price.setName("PRC - " + paquete.getNVO_NOMBRE_PAQ().trim() );
			price.setPrioridad(78000L);
			Prit pritCFM = generarPritCFMEspecial(bo, null, null, paquete, null);
			price.getPritList().add(pritCFM);
			bo.getPriceList().add(price);
			boPaqList.add(bo);
		}
		return boPaqList;
	}
	
	/**
	 * Metodo sin finalizar lógica, para cargar la información copiada manualmente, 
	 * se creo la tabla: paquetes_especiales. la cual es necesario consultar y llenar el 
	 * objeto Paquete.
	 */
	public List<BillingOffer> generarBOOfertasDemanda(){
		List<BillingOffer> boPaqList = new ArrayList<BillingOffer>();
		List<OfertasDemanda> ofertasDemandaList = new ArrayList<OfertasDemanda>();
		List<OfertasDemanda> ofertasSuspenList = new ArrayList<OfertasDemanda>();
		ofertasDemandaList = dataDao.getOfertasDemanda(); //Consulta que retorna la información de los paquetes x demanda
		ofertasSuspenList  = dataDao.getOfertasDemandaSus();//Consulta que retorna la información de BO de suspension y VIP
		BillingOffer bo = new BillingOffer();
		bo.setPlan(new Plan());
		bo.getPlan().setTIPOPLAN("N/A");
		bo.setDemanda(true);
		bo.setPaqueteLDI(null);
		bo.setName("Dmd. - Market Level - Comcel");
		bo.setCode("Dmd. - Market Level - Comcel");
		bo.setDeployIndicator("FALSE");
		bo.getPlan().setCARACT_PLAN(null);
		for(OfertasDemanda ofertas:ofertasDemandaList){
			bo.setEffectiveDate(ofertas.getBO_EFFECTIVEDAY());
			bo.setSaleEffectiveDate(ofertas.getBO_SALEFFECTIVEDAY());
			bo.setCurrency(ofertas.getBO_CURRENCY());
			bo.setTipo_oferta(ofertas.getTipo_oferta());
			bo.setTipo(ofertas.getTipo());
			bo.setLevel(ofertas.getBO_LEVEL());
			bo.setType(ofertas.getBO_TYPE());
		 	bo.setDescripcion(ofertas.getBO_DESCRIPCION());
			bo.setProductType(ofertas.getBO_PRODUCTTYPE());
		 	break; }				
		for(OfertasDemanda ofertaDemanda:ofertasDemandaList){
			Price price = new Price();
			price.setName("PRC - Dmd. - " + ofertaDemanda.getNombre().trim() );
			price.setPrioridad(ofertaDemanda.getBO_PRIORIDAD());
			List<Prit> pritlist = generarPritOfertasDemanda(ofertaDemanda);
			price.setPritList(pritlist);
			bo.getPriceList().add(price);
		}		
		boPaqList.add(bo);		
		for(OfertasDemanda od:ofertasSuspenList){
		bo  = new BillingOffer();
		bo.setPlan(null);
		bo.setPaquete(new Paquete());
		bo.getPaquete().setSPCODE(od.getSpcode());
		bo.getPaquete().setNOMBRE_APROV("N/A");	
		bo.setTipo(od.getTipo());		
		if(od.getBO_TIPOALLOW()!=null) bo.getPaquete().setTIPO_ALLOWANCE(od.getBO_TIPOALLOW());
		bo.getPaquete().setQOS_INICIAL("");
		bo.setDemanda(true);
		bo.setPaqueteLDI(null);
		bo.setEffectiveDate(od.getBO_EFFECTIVEDAY());
		bo.setTipo_oferta(od.getTipo_oferta());
		bo.setName(od.getNombre());
		System.out.println(" oferta susp:  " + od.getNombre());
		String cod = od.getNombre();
		cod.replace(" ","_");
		bo.setCode(cod);
		bo.setSaleEffectiveDate(od.getBO_SALEFFECTIVEDAY());
		if(od.getBO_SALEEXPIRATIONDATE()!=null) bo.setSaleExpirationdate(od.getBO_SALEEXPIRATIONDATE());
		bo.setDescripcion(od.getBO_DESCRIPCION());
		bo.setLevel(od.getBO_LEVEL());
		bo.setType(od.getBO_TYPE());
		bo.setType(od.getBO_PRODUCTTYPE());
		Price price = new Price();
		price.setName("PRC - " + od.getNombre().trim() );
		price.setPrioridad(od.getBO_PRIORIDAD());
		List<Prit> pritList = generarPritOfertasDemanda(od);
		price.setPritList(pritList);
		bo.getPriceList().add(price);
		boPaqList.add(bo);			
		}
		
		return boPaqList;
	}

	public List<BillingOffer> generarBOPaquetesLDI(){
		List<BillingOffer> boPaqLDIList = new ArrayList<BillingOffer>();
		List<PaqueteLDI> paqLDIList = new ArrayList<PaqueteLDI>();

		paqLDIList = dataDao.getPaquetesLDI();

		BillingOffer bo = new BillingOffer();
		for(PaqueteLDI paqueteLDI:paqLDIList){
			if(paqueteLDI.getTIPO_BOLSA()==null||!paqueteLDI.getTIPO_BOLSA().equals("O")){
				bo = new BillingOffer();
				bo.setPlan(null);
				bo.setPaquete(null);
				bo.setPaqueteLDI(paqueteLDI);
				bo.setName(paqueteLDI.getNOMBRE_AMDOCS());
				bo.setCode(paqueteLDI.getNOMBRE_AMDOCS());				
				bo.setEffectiveDate("01/01/2011");
				bo.setSaleEffectiveDate("31/03/2014");
				bo.setCurrency("COP");
				bo.setLevel("Subscriber");
				bo.setType("Additional offer");
				bo.setDeployIndicator("FALSE");
				bo.setDescripcion(paqueteLDI.getDESCRIPCION());
				bo.setProductType("GSM");
				Price price = new Price();
				price.setName("PRC - " + paqueteLDI.getNOMBRE_AMDOCS());
				price.setPrioridad(46000L);
				List<Prit> pritList = generarPRIT_LDI(bo, price, paqueteLDI);
				price.setPritList(pritList);
				bo.getPriceList().add(price);
				price = new Price();
				price.setName("PRC - " + paqueteLDI.getNOMBRE_AMDOCS()+" CFM");
				price.setPrioridad(78000L);
				Prit pritCFM = generarPritCFM(bo, null, null, null, paqueteLDI);
				price.getPritList().add(pritCFM);
				bo.getPriceList().add(price);
				boPaqLDIList.add(bo);
			}
		}
		return boPaqLDIList;
	}

	public List<Prit> generarPRIT_LDI(BillingOffer bo, Price price, PaqueteLDI paqueteLDI){
		List<Prit> pritList = new ArrayList<Prit>();
		Prit prit = new Prit();
		UsageCharge usageCharge = new UsageCharge();
		int entry=0;
		if(((AttrPaqLDI)paqueteLDI.getAttrPaqLDIList().get(0)).getALLOWANCE()!=null){
			usageCharge.setEntryIndex(++entry);
			usageCharge.setName("PRIT - ALLW - "+bo.getName());
			usageCharge.setRole("Allowance");
			usageCharge.setPit("**Allowance duration recurring per period for international zone and LDI operators");
			usageCharge.setDefaultRollingId("No");
			usageCharge.setPeriodSensitivityPolicy("Map");
			usageCharge.setRollingPolicy("LIFO");
			usageCharge.setServiceFilter(mapServiceFilterList.get(17).getName());//LDIINFRACEL
			usageCharge.setService_filter_groupA("RLC_Service_filter_group");
			usageCharge.setService_filter_groupB("SFG NA");
			usageCharge.setShouldProrate("No");
			usageCharge.setUtilizeZeroForRate("No");
			usageCharge.setNumber_of_cycles_to_roll(0);
			usageCharge.setSpecial_day_setA("SpecialDaySetRef");
			usageCharge.setSpecial_day_setB("Special day set");
			usageCharge.setPeriod_setA("PeriodSetRef");
			usageCharge.setPeriod_setB("Single period period set1");
			usageCharge.setZone_mapping(paqueteLDI.getCCTOCZ());
			usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
			usageCharge.setCharge_codes_objectB("Charge codes");
			usageCharge.setProviders_group("Provider list");/***/
			usageCharge.setZone_list_group("UT_Zone list group");
			usageCharge.setQPP_EntryIndex(1);
			usageCharge.setQPP_Incremental("Yes");
			usageCharge.setQPP_UOMForQuantity("Minutes");
			usageCharge.setURI_EntryIndex(1);
			usageCharge.setURI_Incremental("Yes");
			if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
				usageCharge.setPeriod_setB("Peak/Offpeak period set");
			}
			prit.setUsageCharge(usageCharge);
			prit.setRecurringCharge(null);
			if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
				usageCharge.setPeriod_setB("Peak/Offpeak period set");
			}
			pritList.add(prit);
		}
		if(paqueteLDI.getCLASE().equals("ABIERTO"))	{
			prit = new Prit();
			usageCharge = new UsageCharge();
			usageCharge.setEntryIndex(++entry);
			usageCharge.setName("PRIT - RATE - "+bo.getName());
			usageCharge.setRole("Rate");
			usageCharge.setPit("**Rate duration per period and international zone for LDI operators");
			usageCharge.setPeriodSensitivityPolicy("Map");
			usageCharge.setServiceFilter(mapServiceFilterList.get(17).getName());//LDIINFRACEL
			usageCharge.setService_filter_groupA("RLC_Service_filter_group");
			usageCharge.setService_filter_groupB("SFG NA");
			usageCharge.setRoundingFactorMedida("Seconds");
			usageCharge.setRoundingFactorCantidad(1);
			usageCharge.setRoundingMethod("Up");
			usageCharge.setMinimum_unitA("Seconds");
			usageCharge.setMinimum_unitB(0);
			usageCharge.setSpecial_day_setA("SpecialDaySetRef");
			usageCharge.setSpecial_day_setB("Special day set");
			usageCharge.setPeriod_setA("PeriodSetRef");
			usageCharge.setPeriod_setB("Single period period set1");
			usageCharge.setService_filter_to_charge_codeA("RLC_Service_filter_to_charge_code");
			usageCharge.setService_filter_to_charge_codeB("SFtoCC_WRL_LDIINF");/**Actualizar valor*/
			usageCharge.setProviders_group("Provider list");/**Actualizar valor*/
			usageCharge.setZone_mapping(paqueteLDI.getCCTOCZ());
			usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
			usageCharge.setCharge_codes_objectB("Charge codes");
			usageCharge.setZones("Zone list");/**Actualizar valor*/
			usageCharge.setRTR_EntryIndex(1);
			usageCharge.setRTR_Incremental("Yes");
			usageCharge.setRTR_UOMForQuantity("Minutes");
			if(bo !=  null && bo.getPlan() != null && bo.getPlan().getHORARIO_PROMO()!=null&&bo.getPlan().getHORARIO_PROMO().contains(" entre las ")&&bo.getName().contains("PromoOnnet")){
				usageCharge.setPeriod_setB("Peak/Offpeak period set");
			}
			prit.setUsageCharge(usageCharge);
			prit.setRecurringCharge(null);
			pritList.add(prit);
		}
		return pritList;
	}

	public void truncateBO(){
		dataDao.truncateBO();
	}
	public void truncateTTS(){
		dataDao.truncateTTS();
	}
	public void updateBO_numEleg(){
		dataDao.updateBO_numEleg();
	}
	
	public void updateBO_spCode(){
		dataDao.updateBO_spCode();
	}

	public void insertBO(List<BillingOffer> boList){
		for(BillingOffer bo:boList){
			if (bo.getName().toString().trim().contains("Suspension")||bo.getName().toString().trim().contains("VIP")){
				dataDao.insertBOSuspension(bo);
				continue;
			}
				dataDao.insertBO(bo);
		}
	}
	/**
	 * Metodo para Insertar en tabla de Seguimiento y validacion de BO y sus caracteristicas.
	 */
	public void insertBOREVPaq(List<BORevision> boRList){
		for(BORevision boR:boRList){
			dataDao.insertBOREVBPaq(boR);
		}
	}
	
	 public void insertBOREVplan(List<BORevision> boRList){
		 for(BORevision boR:boRList){
				dataDao.insertBOREVBPlan(boR);
		}
	 }
	 @Override
	 public void insertBOREVddem(List<BORevision> boRList) {
		 for(BORevision boR:boRList){
				dataDao.insertBOREVBddem(boR);
		}
    
	 }
	 public List<ValuesPrepago>  getValuesPrepago(){
    	 return dataDao.getValuesPrepago();
     }
	 	
	

	public void insertBOPaq(List<BillingOffer> boList){
		int flag4519 = 0;
		int flag4537 = 0;
		for(BillingOffer bo:boList){
			//No se debe repetir esta BO, ya que una tinen CFM 0 y la otra de 11400, en la
			//lógica que se hablo solo se debe tomar el de CFM mayor a 0;
			if (bo.getPaquete().getSPCODE() != null && bo.getPaquete().getSPCODE().longValue()==4519 && flag4519 == 0){
				flag4519 = 1;
				continue;
			}
			//No se debe repetir esta BO, ya que una tinen CFM 0 y la otra de 54680, en la
			//lógica que se hablo solo se debe tomar el de CFM mayor a 0;
			if (bo.getPaquete().getSPCODE() != null && bo.getPaquete().getSPCODE().longValue()==4537 && flag4537 == 0){
				flag4537 = 1;
				continue;
			}
			dataDao.insertBOPaq(bo);
		}
	}

	public void insertBOPaqLDI(List<BillingOffer> boList){
		for(BillingOffer bo:boList){
			dataDao.insertBOPaqLDI(bo);
		}
	}

	public void truncateBOPanama(){
		dataDao.truncateBOPanama();
	}

	public void insertBOPanama(List<BillingOffer> boList){
		for(BillingOffer bo:boList){
			dataDao.insertBOPanama(bo);
		}
	}

	public void insertBOPaqPanama(List<BillingOffer> boList){
		for(BillingOffer bo:boList){
			dataDao.insertBOPaqPanama(bo);
		}
	}
	
	public void insertTTPO(List<TablaTraduccion> ttList){
		for(TablaTraduccion tt: ttList){
			dataDao.insertTTPO(tt);
		}
	}

	public void insertTTBO(List<TablaTraduccion> ttList){
		for(TablaTraduccion tt: ttList){
			dataDao.insertTTBO(tt);
			System.out.println("Insert: "+tt.getTmcode()+ " "+ tt.getDesc_bo());
		}
	}
	
	
	public List<TablaTraduccion> getInfoTT(boolean panama){
		if(!panama)
			return dataDao.getInfoTT();
		else
			return dataDao.getInfoTTPanama();
	}
	public List<TablaTraduccion> getInfoTTPre(boolean panama){
		if(!panama)
			return dataDao.getInfoTTPre();
		else
			return dataDao.getInfoTTPanama();
	}
	public List<CompararTTS_BO> comparPLANTTS_BO() {
		return dataDao.comparPLANTTS_BO();
	}
	public List<CompararTTS_BO> getcomparPAQTTS_BO(){
		return dataDao.getcomparPAQTTS_BO();
	}
	
	
	
	public List<Integer> getTMCODE_NoMigrados(){
			return dataDao.getTMCODE_NoMigrados();
	}

	public List<Integer> getTmcodeAd(Long tmcode){
		return dataDao.getTmcodeAd(tmcode);
	}

	public List<Integer> getTmcodeAdPanama(Long tmcode){
		return dataDao.getTmcodeAdPanama(tmcode);
	}

	public List<PaqueteLDI> getPaquetesLDI(){
		return dataDao.getPaquetesLDI();
	}
	
	public List<RecurringCharge> getPriceOfertaDemandaRC(int idOfertaDemanda){
		return dataDao.getPriceOfertaDemandaRC(idOfertaDemanda);
	}
	
	public List<UsageCharge> getPriceOfertaDemandaUC(int idOfertaDemanda){
		return dataDao.getPriceOfertaDemandaUC(idOfertaDemanda);
	}
	
	

	public List<Integer> getPlanesAsociados(Long tmcode){
		return dataDao.getPlanesAsociados(tmcode);
	}
	
	public HashMap<Integer, ServiceFilterList> getServiceFilterList(){
		HashMap <Integer, ServiceFilterList> serFilList = new HashMap<Integer, ServiceFilterList>();
		for (ServiceFilterList sfl: dataDao.getServiceFilterList()){
			serFilList.put(sfl.getId(),sfl);
		}		
		return serFilList;
	}
	
	public HashMap<Integer, ServiceFilterToCharge> getServiceFilterToCharge(){
		HashMap <Integer, ServiceFilterToCharge> serFilTChar = new HashMap<Integer, ServiceFilterToCharge>();
		for (ServiceFilterToCharge sfl: dataDao.getServiceFilterToCharge()){
			serFilTChar.put(sfl.getId(),sfl);
		}		
		return serFilTChar;
	}
	
	public void segmentacioDesc(String archivo, String strSheet) throws Exception {
		int fila = 1;
		int col  = 0; 
		Integer coldes [] = new Integer[10] ;
		String des = "";
		String des1 = "";
		String des2 = "";
		int colres = 0;
		int ext = 0;
	    int val = 147; //Definir valor por la cantidad de columnas en archivo....
	    int res = 0;
	    int div = 0;
	    Cell cell = null;
	    Cell cell1 = null;
	    //InputStream inp = new FileInputStream("D://Users/Administrador/Desktop/scripts_/ALLPLANS_BSCS 06112012.xlsx");
		InputStream inp = new FileInputStream(archivo); 
		Workbook wb = WorkbookFactory.create(inp);   
		Sheet sheet = wb.getSheet(strSheet);
		Row row = sheet.getRow(fila);	
	
		for (Cell cell3 : row) {
			if(cell3!=null){	
				if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION")){
					coldes[0] =  cell3.getColumnIndex();System.out.println(cell3.getColumnIndex());					    
				}		
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION1")){
					coldes[1] =  cell3.getColumnIndex();System.out.println(cell3.getColumnIndex());					    
				}
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION2")){
					coldes[2] =  cell3.getColumnIndex();System.out.println(cell3.getColumnIndex());					    
				}
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION3")){
					coldes[3] =  cell3.getColumnIndex();System.out.println(cell3.getColumnIndex());					    
				}
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION4")){
					coldes[4] =  cell3.getColumnIndex();				    
				}
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION5")){
					coldes[5]=  cell3.getColumnIndex();				    
				}
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION6")){
					coldes[6] =  cell3.getColumnIndex();			   					
				}
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION7")){
					coldes[7] =  cell3.getColumnIndex();					   					
				}
				else if(cell3.getRichStringCellValue().getString().equals("DESCRIPCION8")){
					coldes[8] =  cell3.getColumnIndex();					   					
				}		
				else if(cell3.getRichStringCellValue().getString().equals("TMCODE")){
					coldes[9] =  cell3.getColumnIndex();					   					
				}
				else System.out.println(" no "+cell3.getColumnIndex());	
			}			
		 }
		
		fila++;
		row = sheet.getRow(fila);
		while (row!=null){		
		 for(int i = 0; i<val; i++){
			 cell1 = row.getCell(coldes[9]);		  
			  if(cell1!=null){
				   cell = row.getCell(i);
				      if (cell==null)
				    	  cell = row.createCell(i);   
				      if(i==coldes[0]){
				    	      		  des = cell.getRichStringCellValue().getString().trim();
									  ext =des.length();
									  res = ext%200;
									  div = ext/200;
									  colres = div+1;
									  System.out.println("Residuo: "+ res + " divi: "+ div);
							if(res>0 && div>=1 ){
											 for(int l = 0; l<div;l++){
												       des1 = des.substring(l*200,l*200+200);								  
													   cell =  row.getCell(coldes[l+1]);
													   if (cell == null)   
															cell = row.createCell(coldes[l+1]);   
													   cell.setCellValue(des1);	
													                   }											 
											   des2 = des.substring(div*200, div*200+res);
								               cell =  row.getCell(coldes[colres]);
								               if (cell == null)   
													cell = row.createCell(coldes[colres]);   
											   cell.setCellValue(des2);											   									 
									  			}
							else if(res>0 && (div >=0 && div<1)) {
										       des2 = des.substring(div*200, div+res);
										       cell =  row.getCell(coldes[1]);
										       if (cell == null)   
												   cell = row.createCell(coldes[1]);   
											   cell.setCellValue(des2);
													}
							else if(res==0 && div>=1){
								               for(int l = 0; l<div;l++){
												       des1 = des.substring(l*200,l*200+200);								  
													   cell =  row.getCell(coldes[l+1]);
													   if (cell == null)   
															cell = row.createCell(coldes[l+1]);   
													   cell.setCellValue(des1);	
								               							}								
													}
							else                   {
								                  cell =  row.getCell(coldes[1]);
											       if (cell == null)   
													   cell = row.createCell(coldes[1]);   
												  cell.setCellValue("N/A");
											       }
	     		      						}					             
			  					}
		 					}	
		              fila++; 
					 row = sheet.getRow(fila);
		          }
		
		FileOutputStream fileOut = new FileOutputStream(archivo);		
		wb.write(fileOut);   
		fileOut.close();		
	}
	
public Prit generaPritFamilia(BillingOffer bo, Price price, int tipoBolsa, int entry){
	Prit prit = new Prit();
	UsageCharge usageCharge = new UsageCharge();
	usageCharge.setEntryIndex(entry);
	usageCharge.setName("PRIT - ALLW - "+price.getName().replaceAll("PRC - ", "") +" - ELEG FAM.");
	usageCharge.setRole("Allowance");
	usageCharge.setPit("**Allowance duration recurring per period CUG - x first min free for a call");
	usageCharge.setDefaultRollingId("Yes");
	usageCharge.setPeriodSensitivityPolicy("Map");
	usageCharge.setRollingPolicyAllowance("LIFO");
	usageCharge.setShouldProrate("Yes");
	if(bo.getPlan().getTIPOPLAN().equals("Mixto"))
		  usageCharge.setUtilizeZeroForRate("Yes");
	else  usageCharge.setUtilizeZeroForRate("No");
	
	usageCharge.setNumber_of_cycles_to_roll(0);
	usageCharge.setServiceFilter(mapServiceFilterList.get(26).getName());
	usageCharge.setSpecial_day_setA("SpecialDaySetRef");
	usageCharge.setSpecial_day_setB("Special day set");
	usageCharge.setPeriod_setA("PeriodSetRef");
	usageCharge.setPeriod_setB("Single period period set1");
	usageCharge.setService_filter_groupB("SFG NA");
	usageCharge.setCharge_codes_objectA("RLC_Charge_codes");
	usageCharge.setCharge_codes_objectB("Charge codes");
	usageCharge.setQPP_EntryIndex(1);
	usageCharge.setQPP_Incremental("No");
	usageCharge.setQPP_UOMForQuantity("Minutes");
	usageCharge.setURI_EntryIndex(1);
	usageCharge.setURI_Incremental("Yes");
	usageCharge.setQCPP_EntryIndex(1);
	usageCharge.setQCPP_Incremental("Yes");
	usageCharge.setQCPP_UOMForQuantity("Minutes");	
	prit.setUsageCharge(usageCharge);
	prit.setRecurringCharge(null);	
 return prit;	}


 public List<TablaTraduccion> getRefTT() {
	return dataDao.getRefTT();
          }
 public List<TablaTraduccion> getRefTTPre() {
	return dataDao.getRefTTPre();
          }
 
@Override
public List<servicio_comp> getInfoComp() {
	// TODO Auto-generated method stub
	return dataDao.getInfoComp();
}

@Override
public void segmentacioDesc(String archivo) throws Exception {
	// TODO Auto-generated method stub	
}

public List<TOTALES_SEG_CAL> generaTotalesCalidad(){	 
	 return dataDao.generaTotalesCalidad();
}

public void insertCalidad(){
	dataDao.insertCalidad();
}
public List<NewOffersAmdocs> getNewOffersAmdocs(){
	return dataDao.getNewOffersAmdocs();
}
//--------------------------------------------------------
public List<NewOffersAmdocs> getOfertasVig(){
	return dataDao.getOfertasVig();
}
public List<NewOffersAmdocs> getOfertasNoVig(){
	return dataDao.getOfertasNoVig();
}
public List<NewOffersAmdocs> getOferCambioPlanVig(){
	return dataDao.getOferCambioPlanVig();
}
public List<NewOffersAmdocs> getOferCambioPlanNoVig(){
	return dataDao.getOferCambioPlanNoVig();}

public List<NewOffersAmdocs> getOfertasPlan() {
	return dataDao.getOfertasPlan();
}	
public List<OfertasDemanda> getOfertasServ() {
	return dataDao.getOfertasServ();
}	
public List<OfertasDemanda> getOfertasAdicional() {
	return dataDao.getOfertasAdicional();
}	
public List<OfertasDemanda> getOfertaMercado() {
	return dataDao.getOfertaMercado();
}

public List<NewOffersAmdocs> getOferElegFNF() {
	return dataDao.getOferElegFNF();
}

public List<NewOffersAmdocs> getOferEleg1BFNF() {
	return dataDao.getOferEleg1BFNF();
}

public List<NewOffersAmdocs> getOferEleg2BFNF() {
	return dataDao.getOferEleg2BFNF();
}

public List<NewOffersAmdocs> getOferEleg3BFNF() {
	return dataDao.getOferEleg3BFNF();
}

public List<NewOffersAmdocs> getOferEleg0BFNF() {
	return dataDao.getOferEleg0BFNF();
}

public List<Componentes> getCompMand() {
	return dataDao.getCompMand();
}

public List<Componentes> getCompOpc() {
	return dataDao.getCompOpc();
}

public List<Componentes> getCompOpcxDef() {
	return dataDao.getCompOpcxDef();
}

public List<Componentes> getCompExcluidos() {
	return dataDao.getCompExcluidos();
}

public List<Componentes> getCompPrepago() {
	return dataDao.getCompPrepago();
}

public List<Componentes> getActCharge() {
	return dataDao.getActCharge();
}

public List<NewOffersAmdocs> getOferEleg0FNF() {
	return dataDao.getOferEleg0FNF();
}
//--------------------------------------------------------
public List<MaterialSim> getMaterialSim(){	
	return dataDao.getMaterialSim();
	}

public List<BillingOffer> generarBOPlanPrepago(boolean incre){
	List<BillingOffer> boPlanPrep = new ArrayList<BillingOffer>();
	List<Plan> planPrep = new ArrayList<Plan>();		
	planPrep =dataDao.getPlanesPrepago(incre);	
	for(Plan pp: planPrep){
		String descripcion = "";
	/*	if(pp.getDESCRIPCION1()!=null)descripcion+=pp.getDESCRIPCION1();
		if(pp.getDESCRIPCION2()!=null)descripcion+=pp.getDESCRIPCION2();
		if(pp.getDESCRIPCION3()!=null)descripcion+=pp.getDESCRIPCION3();
		if(pp.getDESCRIPCION4()!=null)descripcion+=pp.getDESCRIPCION4();
		if(pp.getDESCRIPCION5()!=null)descripcion+=pp.getDESCRIPCION5();
		if(pp.getDESCRIPCION6()!=null)descripcion+=pp.getDESCRIPCION6();
		if(pp.getDESCRIPCION7()!=null)descripcion+=pp.getDESCRIPCION7();
		if(pp.getDESCRIPCION8()!=null)descripcion+=pp.getDESCRIPCION8();*/
	    //System.out.println("Descripcion: "+pp.getDESCRIPCIONT());	
	    // if( pp.getDESCRIPCIONT()!=null) 
	    	 descripcion = pp.getDESCRIPCIONT();
		BillingOffer bo = new BillingOffer();
		String nombreBO = pp.getPLAN_DOX().trim();
		bo.setTipoBolsa(evaluaTipoBolsa(pp));
		bo.setPlan(pp);
		bo.setTmcode(pp.getTMCODE());
		bo.setName(nombreBO);
	   	bo.setCode(nombreBO);
		bo.setLevel("Subscriber");
		bo.setType("Additional offer");
		//bo.setEffectiveDate("2011/01/01");//Siempre va a ser esta fecha. Definida por AMDOCS
		//nueva fecha ("31/03/2014"
		bo.setEffectiveDate(pp.getEFFECTIVE_DATE());
		bo.setSaleEffectiveDate(pp.getFECHA_INICIO());
		bo.setSaleExpirationdate(pp.getFECHA_VENTA());
		bo.setDeployIndicator("FALSE");
		if(descripcion!=null){
			if(descripcion.trim().equals("Na")) bo.setDescripcion(nombreBO);
			else bo.setDescripcion(descripcion);}
		else descripcion = "Na";
	    bo.setBFNF_Max_Num_Friends(Integer.toString(pp.getElegidos_bfnf()));
	    bo.setBFNF_Pre2Post_Ind("No");
	    bo.setBFNF_VDays_next_numchange(pp.getBFNFVDAYNEXTNCH());
	    bo.setBFNF_Vdur_till_renew(pp.getBFNFVDURTILLRE());
	    bo.setBFNF_Voice_Serv_dur(pp.getBFNFVDURTD());
	    bo.setFNFV_MAXNUM(Integer.toString(pp.getElegidos_fnf()));
	    bo.setFNFV_VDAYSNEXTNUMCHAN(pp.getFfvzdaynextnch());
	    bo.setFNFV_VDURTILLRENEW_(pp.getFfvzdurtillre());
	    bo.setFNFV_VOICESERVDUR(pp.getFfvzdur());
	    bo.setFNFVSMS_MAXNUM(pp.getFfsmsmaxnumele_on());
	    bo.setFNFVSMS_VDAYSNEXTNUMCHAN(pp.getFfsmsdaynextnch());
	    bo.setFNFVSMS_VDURTILLRENEW(pp.getFfvzdurtillre());
	    bo.setFNFVSMS_VOICESERVDUR(pp.getFfsmsdur());	    
		bo.setProductType("GSM");		
		bo.setCurrency("COP");
		bo.setPlan_basico(1);	
		boPlanPrep.add(bo);		
	}
	
	return boPlanPrep;
}


public List<BillingOffer> generarBOOfertasPrepago(){
	List<BillingOffer> boPlanPrep = new ArrayList<BillingOffer>();
	List<OfertasDemanda> ofertasPrepago = new ArrayList<OfertasDemanda>();
	List<OfertasDemanda> ofertasDemanda= new ArrayList<OfertasDemanda>();
	ofertasPrepago = dataDao.getOfertasPrepago( );
	ofertasDemanda = dataDao.getOfertasDemPrep();	
	BillingOffer bo = new BillingOffer();
	BillingOffer bodem = new BillingOffer();
	bodem.setPlan(new Plan());	
    bodem.getPlan().setTIPOPLAN("Prepago");
	bodem.setDemanda(true);
	bodem.setPaqueteLDI(null);
	bodem.getPlan().setQA_SERVICIO(0L);
	bodem.getPlan().setBB_QOS_ACT("0");
	bodem.getPlan().setBB_QOS_CONTROL("0");
	bodem.getPlan().setRC_ACT("0");
	bodem.getPlan().setRC_CONTROL("0");	
	bodem.setName("Dmd. - Market Level - Comcel");
	bodem.setCode("Dmd. - Market Level - Comcel");		
	for(OfertasDemanda prepago : ofertasDemanda ){	
		    bodem.setTipo_oferta(prepago.getTipo_oferta());
			bodem.setTipo(prepago.getTipo());	
			bodem.setEffectiveDate(prepago.getBO_EFFECTIVEDAY());
			bodem.setSaleEffectiveDate(prepago.getBO_SALEFFECTIVEDAY());
			bodem.setType(prepago.getBO_TYPE());
			bodem.setCurrency(prepago.getBO_CURRENCY());
			bodem.setDescripcion("Valores x demanda para "+ prepago.getNombre().trim());
			bodem.setLevel(prepago.getBO_LEVEL());
			bodem.setDeployIndicator("FALSE");
			bodem.setProductType(prepago.getBO_PRODUCTTYPE());
			bodem.setBFNF_Max_Num_Friends(prepago.getBO_MAXNUMBFNF());
			bodem.setBFNF_Pre2Post_Ind(prepago.getBO_PRE2POSIND());
			bodem.setBFNF_VDays_next_numchange(prepago.getBO_VDAYSNEXTNUMCHANGE());
			bodem.setBFNF_Vdur_till_renew(prepago.getBO_VDURTILLRENEW());
			bodem.setBFNF_Voice_Serv_dur(prepago.getBO_VOICESERVDUR());
			bodem.setFNFV_MAXNUM(prepago.getBO_MAXNUM_FNFV());
			bodem.setFNFV_VDAYSNEXTNUMCHAN(prepago.getBO_VDAYSNEXTNUMCHAN_FNFV());
			bodem.setFNFV_VDURTILLRENEW_(prepago.getBO_VDURTILLRENEW_FNFV());
			bodem.setFNFV_VOICESERVDUR(prepago.getBO_VDURTILLRENEW_FNFV());
			bodem.setFNFVSMS_MAXNUM(prepago.getBO_MAXNUM_FNFVSMS());
			bodem.setFNFVSMS_VDAYSNEXTNUMCHAN(prepago.getBO_VDAYSNEXTNUMCHAN_FNFSMS());
			bodem.setFNFVSMS_VDURTILLRENEW(prepago.getBO_VDURTILLRENEW_FNFSMS());
			bodem.setFNFVSMS_VOICESERVDUR(prepago.getBO_VOICESERVDUR_FNFSMS());
			break;
			}
	for(OfertasDemanda prepago1 : ofertasDemanda ){			
		    Price price = new Price();
			price.setName("PRC - Dmd. - " + prepago1.getNombre().trim() );
			//System.out.println("PRC - Dmd. - " + prepago1.getNombre().trim());
			price.setPrioridad(prepago1.getBO_PRIORIDAD());
			List<Prit> pritlist = generarPritOfertasDemanda(prepago1);
			price.setPritList(pritlist);
			bodem.getPriceList().add(price);   	
		} 
		boPlanPrep.add(bodem);
	for(OfertasDemanda prepago : ofertasPrepago ){	
		bo  = new BillingOffer();	
	   	bo.setPlan(null);
		bo.setPaquete(new Paquete());
		bo.getPaquete().setQOS_INICIAL("0");
		bo.getPaquete().setQOS_CONTROL("0");
		bo.getPaquete().setQA_SERVICIO("0");
		bo.getPaquete().setNOMBRE_APROV("N/A");
		bo.setPaqueteLDI(null);
		bo.setTipo(prepago.getTipo());
		bo.setTipo_oferta(prepago.getTipo_oferta());
		if(prepago.getBO_TIPOALLOW()!=null) bo.getPaquete().setTIPO_ALLOWANCE(prepago.getBO_TIPOALLOW());
		bo.getPaquete().setSPCODE(prepago.getSpcode());
		bo.setBFNF_Max_Num_Friends(prepago.getBO_MAXNUMBFNF());
		bo.setBFNF_Pre2Post_Ind(prepago.getBO_PRE2POSIND());
		bo.setBFNF_VDays_next_numchange(prepago.getBO_VDAYSNEXTNUMCHANGE());
		bo.setBFNF_Vdur_till_renew(prepago.getBO_VDURTILLRENEW());
		bo.setBFNF_Voice_Serv_dur(prepago.getBO_VOICESERVDUR());
		bo.setFNFV_MAXNUM(prepago.getBO_MAXNUM_FNFV());
		bo.setFNFV_VDAYSNEXTNUMCHAN(prepago.getBO_VDAYSNEXTNUMCHAN_FNFV());
		bo.setFNFV_VDURTILLRENEW_(prepago.getBO_VDURTILLRENEW_FNFV());
		bo.setFNFV_VOICESERVDUR(prepago.getBO_VOICESERVDUR_FNFV());
		bo.setFNFVSMS_MAXNUM(prepago.getBO_MAXNUM_FNFVSMS());
		bo.setFNFVSMS_VDAYSNEXTNUMCHAN(prepago.getBO_VDAYSNEXTNUMCHAN_FNFSMS());
		bo.setFNFVSMS_VDURTILLRENEW(prepago.getBO_VDURTILLRENEW_FNFSMS());
		bo.setFNFVSMS_VOICESERVDUR(prepago.getBO_VOICESERVDUR_FNFSMS());
		bo.setDemanda(false);
		bo.setPaqueteLDI(null);
		bo.setEffectiveDate(prepago.getBO_EFFECTIVEDAY());
		bo.setName(prepago.getNombre());
		System.out.println(prepago.getNombre()+"   ");
		String cod;
		if(prepago.getNombre().contains("Instalacion de Web Message"))
		  cod = "Instalacion de Web Message";
		else cod = prepago.getNombre();		
		cod.replace(" ","_");
		bo.setCode(cod);
		bo.setSaleEffectiveDate(prepago.getBO_SALEFFECTIVEDAY());
		if(prepago.getBO_SALEEXPIRATIONDATE()!=null)bo.setSaleExpirationdate(prepago.getBO_SALEEXPIRATIONDATE());		
		if(prepago.getBO_DESCRIPCION()!=null) bo.setDescripcion(prepago.getBO_DESCRIPCION());
		bo.setType(prepago.getBO_TYPE());
		bo.setLevel(prepago.getBO_LEVEL());
		bo.setCurrency(prepago.getBO_CURRENCY());
		bo.setProductType(prepago.getBO_PRODUCTTYPE());
		Price price = new Price();
		price.setName("PRC - " + prepago.getNombre().trim() );
		price.setPrioridad(prepago.getBO_PRIORIDAD());		
		List<Prit> pritList = generarPritOfertasDemanda(prepago);
		price.setPritList(pritList);
		bo.getPriceList().add(price);		
		boPlanPrep.add(bo);	
		}	
		
    return boPlanPrep;
}

 }
