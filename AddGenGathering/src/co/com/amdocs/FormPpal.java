package co.com.amdocs;

import java.awt.Component;
import java.awt.EventQueue;
import co.com.comcel.main.GenBoffer;
import co.com.comcel.mainpo.GenPO;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.border.TitledBorder;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import co.com.amdocs.model.DataModel;
import co.com.amdocs.model.IDataModel;
import co.com.amdocs.vo.Ofertas_demanda;
import co.com.amdocs.vo.PRODUCT_TYPE;
import co.com.amdocs.vo.PlanPrepago;
import co.com.amdocs.vo.Tipo_oferta;
import co.com.amdocs.vo.canales_venta;
import co.com.amdocs.vo.oferta_amdocs;
import co.com.amdocs.vo.usingSimcard;
import co.com.amdocs.vo.zona;

import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import javax.swing.UIManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Font;

import com.toedter.calendar.JDateChooser;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;



public class FormPpal extends JFrame {
    
	private JPanel contentPane;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTable table;
	private String s ="";
	private Boolean checkofsim = false;
	private String ptypecomb="";
	private String tiofcomb ="";
	private String tecncomb= "";
	private String ussimcomb = "";
	private String zonacomb = "";
	private String ivacomb = "";
	private String ppcomb = "";
	private String tmcodeq = "";
	private String spcodeq= "";
	private String anotaciones = "";
	private String vigcomb = "";
	private List<Object> lis = new ArrayList<Object>();
	private List<canales_venta> cv = new ArrayList<canales_venta>();
	@SuppressWarnings("rawtypes")
	private DefaultListModel model;
	IDataModel mod = new DataModel();    
	private JTextField textField_7;
	List<oferta_amdocs> ofam = new ArrayList<oferta_amdocs>();
	List<PRODUCT_TYPE>  pt =  new ArrayList<PRODUCT_TYPE>();
	List<Tipo_oferta>   to = new ArrayList<Tipo_oferta>();
	List<Ofertas_demanda> od = new ArrayList<Ofertas_demanda>();
	List<PlanPrepago>   pp = new ArrayList<PlanPrepago>();
	List<usingSimcard>  us = new ArrayList<usingSimcard>();
	List<zona>			zn = new ArrayList<zona>();
	protected Component parentComponent;
	private JTextField txtPlan;
	private JTextField textField_5;
	private JTextField textField_2;
	private JTextField textField_6;
	private JTextField textField_8;
	private JTextField textField;
	private JTextField textField_9;
	private JTextField textField_10;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormPpal frame = new FormPpal();
					frame.setVisible(true);					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void PonerAplicacion(JTable tabla, TableColumn columna){
		JCheckBox check = new JCheckBox();
		check.setVisible(true);
		check.setSelected(false);
		columna.setCellEditor(new Celda_CheckBox());
		columna.setCellRenderer(new Render_CheckBox());
	}
	
	@SuppressWarnings("unchecked")
	public void ActualizarCombo(@SuppressWarnings("rawtypes") JComboBox comboBox, JPanel panel_2 ){
		comboBox.removeAllItems();
		pp = mod.getPlanPrepago();
		comboBox.addItem("");
		for(PlanPrepago  pl: pp){
			comboBox.addItem(pl.getPlanprepago());
		}
		panel_2.add(comboBox);
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FormPpal() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 803, 732);
		contentPane = new JPanel();
		contentPane.setForeground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 5, 777, 224);
		panel.setBorder(new TitledBorder(null, "INFORMACION DE PLAN EN BSCS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBackground(SystemColor.controlHighlight);
		
		final JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 228, 777, 356);
		panel_1.setBorder(new TitledBorder(null, "INFORMACION OFERTA DE AMDOCS", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBackground(SystemColor.controlHighlight);
		
		final JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 584, 777, 82);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "INFORMACION EN PLATAFORMA PREPAGO E INFORMACION DE NUEVA OFERTA", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBackground(SystemColor.controlHighlight);
		
		final JTextArea textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);
		textArea_1.setWrapStyleWord(true);
		JScrollPane scrollPane_1 = new JScrollPane(textArea_1);
		scrollPane_1.setBounds(163, 134, 268, 112);
		panel_1.add(scrollPane_1);	
		
		//----------------------------------------------
		JLabel lblNewLabel_3 = new JLabel("PLANPREPAGO **");
		lblNewLabel_3.setBounds(10, 23, 110, 21);
		panel_2.add(lblNewLabel_3);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				ppcomb = (String)comboBox.getSelectedItem();			
			}
		});
		pp = mod.getPlanPrepago();
		comboBox.addItem("");
		for(PlanPrepago  pl: pp){
			comboBox.addItem(pl.getPlanprepago());
		}
		comboBox.setBounds(111, 23, 136, 20);
		panel_2.add(comboBox);
		panel_1.setLayout(null);
		
		JLabel lblNombrePlanAmdocs = new JLabel("NOMBRE PLAN AMDOCS**");
		lblNombrePlanAmdocs.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNombrePlanAmdocs.setBounds(24, 29, 165, 14);
		panel_1.add(lblNombrePlanAmdocs);
		
		textField_4 = new JTextField();
		textField_4.setToolTipText("EL nombre de plan en Amdocs se inicia con  Plan - y la primera letra en Mayúscula.");
		textField_4.setBounds(228, 26, 511, 20);
		//textField_4.setInputVerifier(new VerificarTexto());
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblProductType = new JLabel("PRODUCT TYPE");
		lblProductType.setBounds(24, 65, 98, 14);
		panel_1.add(lblProductType);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setToolTipText("El Valor de PRODUCT TYPE depende del COMBO de TECNOLOGIA ");
		
		comboBox_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				 				
				 ptypecomb = (String)comboBox_1.getSelectedItem();				
			}
		});
		comboBox_1.setBounds(115, 62, 137, 20);
		comboBox_1.setEditable(false);
		//comboBox_1.setEnabled(false);
		pt = mod.getProductType();
		comboBox_1.addItem("");
		for(PRODUCT_TYPE  pty: pt){
			comboBox_1.addItem(pty.getPRODUCT_TYPE());
		}	
		panel_1.add(comboBox_1);
		
		JLabel lblTipoOferta = new JLabel("TIPO OFERTA**");
		lblTipoOferta.setBounds(255, 65, 89, 14);
		panel_1.add(lblTipoOferta);
		
		final JComboBox comboBox_2 = new JComboBox();
		comboBox_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				 tiofcomb = (String)comboBox_2.getSelectedItem();
			}
		});
		comboBox_2.setBounds(354, 62, 137, 20);
		to = mod.getTipoOferta();
		comboBox_2.addItem("");
		for(Tipo_oferta tofer: to){
			comboBox_2.addItem(tofer.getTipo_oferta());			
		}		
		panel_1.add(comboBox_2);
		
		JLabel lblTecnologia = new JLabel("TECNOLOGIA**");
		lblTecnologia.setBounds(501, 65, 89, 14);
		panel_1.add(lblTecnologia);
		
		final JComboBox comboBox_3 = new JComboBox();
		comboBox_3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				 tecncomb = (String)comboBox_3.getSelectedItem();
				 if(tecncomb!=null&&tecncomb.equals("Kit"))
				 comboBox_1.setSelectedItem("KITPREP");	
				 if(tecncomb!=null&&tecncomb.equals("Sim Card"))
					 comboBox_1.setSelectedItem("SLSIMPR");	
				 if(tecncomb!=null&&tecncomb.equals("Tactico"))
					 comboBox_1.setSelectedItem("SIMTCPR");	
				 if(tecncomb!=null&&tecncomb.equals("Chip Costa"))
					 comboBox_1.setSelectedItem("SIMCENP");	
				 if(tecncomb!=null&&tecncomb.equals("Estrategico"))
					 comboBox_1.setSelectedItem("SIMCENP");	
				 if(tecncomb==null)tecncomb="";
			}
		});
		comboBox_3.setBounds(593, 62, 158, 20);
		pt= mod.getTecnologia();
		comboBox_3.addItem("");
		for(PRODUCT_TYPE t: pt){
			comboBox_3.addItem(t.getTECNOLOGIA());
		}
		panel_1.add(comboBox_3);
		
		JLabel lblNewLabel_1 = new JLabel("USING SIMCARD PREPAGO**");
		lblNewLabel_1.setBounds(24, 95, 165, 14);
		panel_1.add(lblNewLabel_1);
		
		final JComboBox comboBox_4 = new JComboBox();
		comboBox_4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				ussimcomb = (String) comboBox_4.getSelectedItem();			
			}
		});
		comboBox_4.setBounds(189, 93, 124, 20);		
		us = mod.getUsingSimcard();
		comboBox_4.addItem("");
		for(usingSimcard u: us){
			comboBox_4.addItem(u.getUSINGSIMCARDPREPAG1());		
		}		
		panel_1.add(comboBox_4);
    	 
		JLabel lblNewLabel_2 = new JLabel("IVA");
		lblNewLabel_2.setBounds(323, 90, 23, 20);
		panel_1.add(lblNewLabel_2);
		
		final JComboBox comboBox_5 = new JComboBox();
		comboBox_5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				ivacomb = (String)comboBox_5.getSelectedItem();
			}
		});
		comboBox_5.setBounds(364, 93, 124, 20);
		comboBox_5.addItem("");
		comboBox_5.addItem("0");
		comboBox_5.addItem("16");
		comboBox_5.addItem("20");
		panel_1.add(comboBox_5);
		
		
		//----------------------------------------------------------------------------
		JLabel lblObservaciones = new JLabel("ANOTACIONES ADICIONALES");
		lblObservaciones.setBounds(444, 121, 197, 14);
		panel_1.add(lblObservaciones);
		
		panel.setLayout(null);
		JLabel lblTmcode = new JLabel("TMCODE");
		lblTmcode.setBounds(21, 70, 58, 23);
		panel.add(lblTmcode);
		
		JLabel lblNewLabel = new JLabel("PLAN EN BSCS");
		lblNewLabel.setBounds(181, 74, 105, 14);
		panel.add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setBounds(285, 71, 467, 20);
		textField_1.setEditable(false);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblSpcode = new JLabel("SPCODE");
		lblSpcode.setBounds(21, 106, 52, 14);
		panel.add(lblSpcode);
		
		textField_3 = new JTextField();
		textField_3.setBounds(285, 104, 467, 20);
		textField_3.setEditable(false);
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblPaqEnBscs = new JLabel("PAQ EN BSCS");
		lblPaqEnBscs.setBounds(181, 106, 105, 14);
		panel.add(lblPaqEnBscs);
		
		JLabel lblTmcodeEquivalente = new JLabel("TMCODE EQUIVALENTE");
		lblTmcodeEquivalente.setBounds(21, 34, 144, 14);
		panel.add(lblTmcodeEquivalente);
		
		JLabel lblDescripcionPlan = new JLabel("DESCRIPCION PLAN");
		lblDescripcionPlan.setBounds(21, 135, 139, 14);
		panel.add(lblDescripcionPlan);
		
		final JTextArea textArea = new JTextArea();
		textArea.setColumns(1);
		textArea.isMaximumSizeSet();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane desc = new JScrollPane(textArea);
		desc.setBounds(156, 135, 596, 77);
		panel.add(desc);
		
		textField_7 = new JTextField();
		//textField_7.setInputVerifier(new VerificarTexto());
		textField_7.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {				
				char key = arg0.getKeyChar();
				if(key < '0'||key >'9'||textField_7.getText().length()==6){
					arg0.consume();
			}
		}});
		
		textField_7.setBounds(181, 31, 86, 20);
		panel.add(textField_7);
		textField_7.setColumns(10);
		contentPane.add(panel);
		contentPane.add(panel_1);
		
		final DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		final JDateChooser dateChooser = new JDateChooser(new Date(), "dd/MM/yyyy");
		dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
			
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				System.out.println(" Fecha inicio: "+ arg0.getNewValue());
				if( !arg0.getNewValue().toString().contains("javax.swing.JPanel"))
				 s  =sdf.format(arg0.getNewValue());							
			}
		});
		dateChooser.setBounds(606, 90, 145, 20);
		panel_1.add(dateChooser);
		JButton btnNewButton = new JButton("BUSCAR");
		btnNewButton.setBounds(582, 30, 98, 23);
		btnNewButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  Date fechaNull;
			  String fec = "00/00/0000 00:00:00";
			  Date fechain = null;
			  if (e.getActionCommand().equals("BUSCAR")) {				  
			      tmcodeq= textField_7.getText().trim();
			      if(!textField_5.getText().equals("")&&!textField_7.getText().equals("")){
			    	  System.out.println(" pasa por opcion de spcode--");
			    	  spcodeq = textField_5.getText();
			    	  tmcodeq = textField_7.getText();
			      	  oferta_amdocs ofer = new oferta_amdocs();
			      	  System.out.println("spcode: "+spcodeq+ " tmcode: "+tmcodeq);
			      	  ofer.setSpcode(Long.valueOf(spcodeq));
			      	  ofer.setTmcode(Long.valueOf(tmcodeq));
			      	  ofam = mod.getPlanEquiv2(ofer);
			      	if(ofam.size()==0){
				    	 textField_6.setText(""); 
				    	 textField_1.setText(""); 
				    	 textField_3.setText("");
				    	 textField_8.setText("");
				    	 textArea.setText("");
				    	 try {
							fechaNull = df.parse(fec);
							fechain=fechaNull;
				    		dateChooser.setDate(fechain);
				     				} catch (ParseException e1) {
				     					System.out.println(e1.getMessage());	
						}
			      	}	
			      	  
			      	else {for(oferta_amdocs om: ofam){
			      		 if(om.getTmcode()!=null)textField_6.setText(String.valueOf(om.getTmcode())); 			    	   
				    	 textField_1.setText(om.getPlan());
				    	 if(om.getSpcode()!=null)textField_8.setText(String.valueOf(om.getSpcode())); 
				    	 textField_3.setText(om.getPaquete());
				    	 textArea.setText(om.getDescripcion());	
				    	 try {
					    		fechaNull = df.parse(fec);
					    		fechain=fechaNull;
					    		if(om.getFecha_inicio()!=null)fechain = df.parse(om.getFecha_inicio());
								if(om.getFecha_venta()!=null) {
								}
								dateChooser.setDate(fechain);
							    } catch (ParseException e1) {System.out.println(e1.getMessage());	}
			      			}
			      	}
			     }
			  else {
				  ofam = null;
			      try {
			    		fechaNull = df.parse(fec);
			    		fechain=fechaNull;
			    		dateChooser.setDate(fechain);
					    } catch (ParseException e1) {
						System.out.println(e1.getMessage());						
					}   	 
			     //}	
			     if(ofam ==null||ofam.size()==0 ) {
			    	 textField_6.setText(""); 
			    	 textField_1.setText(""); 
			    	 textField_3.setText("");
			    	 textField_8.setText("");
			    	 textArea.setText("");
			     				}
			    		}
			      }			  
		  } 
		});
		panel.add(btnNewButton);
		
		textField_5 = new JTextField();
		textField_5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char key = arg0.getKeyChar();
				if(key < '0'||key >'9'||textField_5.getText().length()==5){
					arg0.consume();
					}				
			}
		});
		textField_5.setBounds(442, 31, 98, 20);
		panel.add(textField_5);
		textField_5.setColumns(10);
		
		textField_6 = new JTextField();
		textField_6.setBounds(74, 71, 86, 20);
		textField_6.setEditable(false);
		panel.add(textField_6);
		textField_6.setColumns(10);
		
		textField_8 = new JTextField();
		textField_8.setBounds(74, 104, 86, 20);
		textField_8.setEditable(false);
		panel.add(textField_8);
		textField_8.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("SPCODE EQUIVALENTE");
		lblNewLabel_7.setBounds(293, 34, 154, 14);
		panel.add(lblNewLabel_7);
		
		JCheckBox chckbxDeclararNoVigente = new JCheckBox("Declarar no vigente");
		chckbxDeclararNoVigente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				AbstractButton abstractButton = (AbstractButton) arg0.getSource();
				checkofsim  = abstractButton.getModel().isSelected();
				System.out.println(checkofsim);
				
			}
		});
		chckbxDeclararNoVigente.setBounds(6, 156, 144, 23);
		panel.add(chckbxDeclararNoVigente);
		
		JLabel lblOfertaCopiaDe = new JLabel("     Oferta copia de ref.");
		lblOfertaCopiaDe.setBounds(16, 178, 127, 14);
		panel.add(lblOfertaCopiaDe);
		contentPane.setLayout(null);
		//----------------------------------------- adicionar botones		
		panel_1.setVisible(true);
		contentPane.add(panel_2);		
		final JTextArea textArea_2 = new JTextArea();
		textArea_2.setLineWrap(true);
		textArea_2.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea_2);
		scrollPane.setBounds(441, 134, 310, 110);
		panel_1.add(scrollPane);		
		JLabel lblNewLabel_4 = new JLabel("DESCRIPCION OFERTA AMDOCS");
		lblNewLabel_4.setBounds(163, 120, 197, 14);
		panel_1.add(lblNewLabel_4);
		JLabel lblPlanesMercadoAfectan = new JLabel("CARGOS DE OFERTAS DE  DEMANDA QUE APLICAN A NUEVA OFERTA");
		lblPlanesMercadoAfectan.setBounds(24, 251, 454, 14);
		panel_1.add(lblPlanesMercadoAfectan);
		
		 final DefaultTableModel dtm= new DefaultTableModel(){			 
			private static final long serialVersionUID = 7069790950732761288L;

			public boolean isCellEditable(int fila, int col){
				 if (col==4) return true;
				 else  return false;
			 }
		 };
        dtm.addColumn("Nombre Cobro de Demanda");
        dtm.addColumn("Valor Cargo");
        dtm.addColumn("IVA");
        dtm.addColumn("Nuevo Cargo");
        dtm.addColumn("Aplica");
    	od = mod.getOfertasDemanda();
		Object[] data = new Object[5];
        for(Ofertas_demanda of: od){
        	for(int i= 0; i<5;i++){
        		if(i == 0)data[i]= of.getNOM_OFERTA();
        		if(i == 1)data[i]= of.getCARGO();
        		if(i == 2)data[i]= of.getIVA();
        		if(i == 3)data[i]= "";
        		if(i == 4)data[i]= true;      		
        	}  	
        	dtm.addRow(data);
        }
        table = new JTable(dtm){
        	/**
			 * 
			 */
			private static final long serialVersionUID = -1311364216572388409L;

			public boolean isCellEditable(int row, int column) {
                int modelColumn = convertColumnIndexToModel( column );
                if (modelColumn ==4)  return true;
                 
                else return super.isCellEditable(row, column);
            }        	
        };
        table.addKeyListener(new KeyAdapter() {
        	@Override
        	public void keyReleased(KeyEvent arg0) {
        		if (!table.isEditing() && !table.editCellAt(table.getSelectedRow(),table.getSelectedColumn())) {
        			table.getEditorComponent().requestFocusInWindow();  // obligamos que la celda reciba el foc
                    }	
        	}
        });
       	table.getColumnModel().getColumn(0).setPreferredWidth(179);
		PonerAplicacion(table, table.getColumnModel().getColumn(4));		
		table.getTableHeader().setReorderingAllowed(false);
		/*table.getModel().addTableModelListener(new TableModelListener() {
			 public void tableChanged(TableModelEvent e) {				 
				 System.out.println(  " columna: "+ e.getColumn()+ " - fila Ultima: "+ e.getLastRow()+" primera fila: "+e.getFirstRow());
				 }			
		});*/
		//-----------------------------------------------------------------
		ListSelectionModel cellSelectionModel = table.getSelectionModel();
	    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent e) {         
	          int[] selectedRow = table.getSelectedRows();
	          int[] selectedColumns = table.getSelectedColumns();
               if(selectedColumns.length >= 1 && selectedRow.length>0){            	
                 	 if(selectedColumns[0] >= 4&& (Boolean) table.getValueAt(selectedRow[0], selectedColumns[0])){
                     JOptionPane.showMessageDialog(null, "POR FAVOR SI EXCLUYO A UNA DE ESTAS OFERTAS DIGITE SUS COMENTARIOS DE EN ANOTACIONES ","ADVERTENCIA", JOptionPane.INFORMATION_MESSAGE);  
            	      anotaciones =  "Oferta: "+ (String) table.getValueAt(selectedRow[0], selectedColumns[0]-4 )+",  Cargo: "+ (String) table.getValueAt(selectedRow[0], selectedColumns[0]-3)+",  IVA: "+(String) table.getValueAt(selectedRow[0], selectedColumns[0]-2)+ ", NO APLICA PARA NUEVA OFERTA. |\n" ;         	 
            	      
                 	  System.out.println("valor actual reg: "+selectedRow[0]+"  col: "+ selectedColumns[0]);
                 	  System.out.println("valor actual " +(Boolean) table.getValueAt(selectedRow[0], selectedColumns[0]));
                 	  textArea_2.append(anotaciones);
            	      textArea_2.setFocusable(true);}
               			}
	          /*  for (int i = 0; i < selectedRow.length; i++) {
	            for (int j = 0; j < selectedColumns.length; j++) {
	            	selectedData = (String) table.getValueAt(selectedRow[0], selectedColumns[0]-1);            
	            }
	          }*/
	          System.out.println("Selected: " + e);
	        }
	      });   
	    
	    final JScrollPane tab = new JScrollPane(table);
		tab.setBounds(24, 265, 524, 83);
		panel_1.add(tab);	
		
		txtPlan = new JTextField();
		txtPlan.setBounds(189, 26, 29, 20);
		txtPlan.setText("Plan");
		txtPlan.setEditable(false);
		panel_1.add(txtPlan);
		txtPlan.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("  -");
		lblNewLabel_5.setBounds(214, 29, 29, 14);
		panel_1.add(lblNewLabel_5);		
		
		JLabel label_2 = new JLabel("CANAL DE VENTA");
		label_2.setBounds(24, 120, 106, 14);
		panel_1.add(label_2);
		
		final JList list = new JList();	
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		cv = mod.getCanalesVenta();
		model = new DefaultListModel();	   
		for(canales_venta canal: cv){
			model.addElement(canal.getCanal_venta());
				}
		list.setModel(model);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				 boolean adjust = e.getValueIsAdjusting();
				 if(!adjust){
					 JList list = (JList) e.getSource(); 
					 lis = list.getSelectedValuesList();
				 }		
			}
		});
		final JScrollPane scrollPane_2 = new JScrollPane(list);
		scrollPane_2.setBounds(24, 134, 118, 112);
		panel_1.add(scrollPane_2);
		
		JLabel lblFechaInicio = new JLabel("FECHA INICIO**");
		lblFechaInicio.setBounds(513, 93, 98, 14);
		panel_1.add(lblFechaInicio);
		
		JLabel label_1 = new JLabel("(SalesEffectiveDate)");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 9));
		label_1.setBounds(507, 107, 89, 14);
		panel_1.add(label_1);	
		
		JLabel lblZona = new JLabel("ZONA **");
		lblZona.setBounds(558, 266, 58, 14);
		panel_1.add(lblZona);		
		final JComboBox comboBox_6 = new JComboBox();
		comboBox_6.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				zonacomb = (String)comboBox_6.getSelectedItem();
			}
		});
		zn = mod.getZona();
		comboBox_6.addItem("");
		for(zona  z: zn){
			comboBox_6.addItem(z.getZona().trim());			
		}
		comboBox_6.setBounds(606, 263, 145, 20);
		panel_1.add(comboBox_6);
		
		final JComboBox comboBox_7 = new JComboBox();
		comboBox_7.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				vigcomb = (String)comboBox_7.getSelectedItem();
			
			}
		});
		comboBox_7.setBounds(693, 294, 58, 20);
		comboBox_7.addItem("SI");
		comboBox_7.addItem("NO");
		panel_1.add(comboBox_7);
		
		JLabel lblVigencia = new JLabel("VIGENCIA**:");
		lblVigencia.setBounds(558, 291, 98, 23);
		panel_1.add(lblVigencia);
		//----------------------------------------------------------------------------------------
		final JButton btnNewButton_1 = new JButton("ENVIAR");
			btnNewButton_1.addActionListener(new ActionListener() {		
				public void actionPerformed(ActionEvent arg0) {					
				if(textField_4.getText().trim().equals("") || textField_2.getText().trim().equals("") ||textField.getText().trim().equals("")||tecncomb.equals("")||tiofcomb.equals("")||s.equals("")||ppcomb.equals("")||zonacomb.equals("")||vigcomb.equals("")) {
					 
							JOptionPane.showMessageDialog(null, "ERROR: VERIFICAR ESTABLECER FECHA DE INICIO Y TODOS LOS CAMPOS CON DOBLE ASTERISCO(**) \n DEBEN DILIGENCIARSE, NO DEBEN ESTAR VACIOS!!! ", "Mensaje de Advertencia", JOptionPane.ERROR_MESSAGE);
					
				}else {						
						oferta_amdocs oam = new oferta_amdocs();
						String message =  "Esta seguro de insertar informacion en BD para Nueva Oferta?";
						String title = "CONFIRMACION DE INSERCION OFERTA";				
						 Integer a = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
						if(a == JOptionPane.YES_OPTION){
						if(ivacomb.equals("")) ivacomb = "0";
						oam.setZona(zonacomb);
					    oam.setIva(ivacomb);
						oam.setFecha_inicio(s);
						oam.setVigente(vigcomb);
						oam.setPlan_dox("");
						oam.setProduct_type(ptypecomb);
						oam.setUsingsimcardprepag1(ussimcomb);
						oam.setPlanprepago(ppcomb);
						oam.setTecnologia(tecncomb);
						oam.setTipo_plan(tiofcomb);
						System.out.println("tipo de oferta: "+tecncomb);
						oam.setANOTACION_AD(textArea_2.getText());
						String element = "";					
								if(lis==null||lis.size()==0){
									JOptionPane.showMessageDialog(btnNewButton_1, "CANAL DE VENTA NO HA SIDO SELECCIONADO: POR DEFECTO ES TODOS!!");
									oam.setCANALVENTA("TODOS");									
								}
								else{
									Iterator iterator = lis.iterator();
										while(iterator.hasNext()){
											element = element + (String) iterator.next()+", " ;			   
													}
									oam.setCANALVENTA(element);
									}
								if(textField.getText().trim()!=null) oam.setTmcode(Long.valueOf(textField.getText().trim()));
								else oam.setTmcode((long) 111111);
								oam.setPlan(textField_9.getText().trim());
								if(textField.getText().trim()!=null)  oam.setSpcode(Long.valueOf(textField_2.getText().trim()));
								oam.setPaquete(textField_10.getText().trim());						
								oam.setPlan_dox("Plan - "+textField_4.getText().trim());
								oam.setDescripcion(textArea_1.getText().trim());
								cargaPlan cp ;
								if(!tmcodeq.equals(""))	cp = new cargaPlan(tmcodeq, oam, checkofsim);	
								else cp = new cargaPlan("8614", oam, checkofsim);	
					/*	}catch(Exception sql){
							JOptionPane.showMessageDialog(null, "LA OFERTA YA EXISTE O NO SE HAN DILIGENCIADO LOS CAMPOS OBLIGATORIOS!!!", "Mensaje de ERROR", JOptionPane.ERROR_MESSAGE);
							sql.printStackTrace();			 
										}	*/		
								}	
							}
						}
				});
				btnNewButton_1.setBounds(261, 669, 96, 23);
				//-----------------------------------------------------
				JButton btnNewButton_2 = new JButton("CANCELAR");
				btnNewButton_2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						textField_7.setText("");
						textField.setText("");
						textField_1.setText("");
						textField_2.setText("");
						textField_3.setText("");
						textArea_1.setText("");
						textArea.setText("");
						textArea_2.setText("");
						textField_4.setText("");
						textField_5.setText("");
						textField_6.setText("");
						textField_8.setText("");
						textField_9.setText("");
						textField_10.setText("");
						
						//-----------------------------
						comboBox_3.removeAllItems();
						pt= mod.getTecnologia();
						comboBox_3.addItem("");
						for(PRODUCT_TYPE t: pt){
							comboBox_3.addItem(t.getTECNOLOGIA());
						}
						panel_1.add(comboBox_3);
						//----------------------------
						comboBox_1.removeAllItems();
						pt = mod.getProductType();
						comboBox_1.addItem("");
						for(PRODUCT_TYPE  pty: pt){
							comboBox_1.addItem(pty.getPRODUCT_TYPE());
						}	
						panel_1.add(comboBox_1);
						//----------------------------						
						comboBox_4.removeAllItems();	
						us = mod.getUsingSimcard();
						comboBox_4.addItem("");
						for(usingSimcard u: us){
							comboBox_4.addItem(u.getUSINGSIMCARDPREPAG1());		
						}		
						panel_1.add(comboBox_4);
						
						//--------------IVA ----------------
						comboBox_5.removeAllItems();
						comboBox_5.addItem("");
						comboBox_5.addItem("0");
						comboBox_5.addItem("16");
						comboBox_5.addItem("20");
						panel_1.add(comboBox_5);
						//--------------						
						comboBox_2.removeAllItems();
						to = mod.getTipoOferta();
						comboBox_2.addItem("");
						for(Tipo_oferta tofer: to){
							comboBox_2.addItem(tofer.getTipo_oferta());			
						}		
						panel_1.add(comboBox_2);
						//----------------------------
						
						comboBox_6.removeAllItems();
						zn = mod.getZona();
						comboBox_6.addItem("");
						for(zona  zo: zn){
							comboBox_6.addItem(zo.getZona());
						}
						panel_1.add(comboBox_6);
						
						//-----------------------------
						comboBox.removeAllItems();
						pp = mod.getPlanPrepago();
						comboBox.addItem("");
						for(PlanPrepago  pl: pp){
							comboBox.addItem(pl.getPlanprepago());
						}
						panel_2.add(comboBox);
						//-----------------------------
						model.clear();
						cv = mod.getCanalesVenta();					   
						for(canales_venta canal: cv){
							model.addElement(canal.getCanal_venta());
								}
						list.setModel(model);
						scrollPane_2.setViewportView(list);
						panel_1.add(scrollPane_2);
						//-----------------------------
						//table.setRowSelectionAllowed(false);
					    //table.setColumnSelectionAllowed(false);
					    //table.setCellSelectionEnabled(false);												
				     /*	int filas = dtm.getRowCount() - 1;		
						for( int i = filas ; i>=0; i--){
							dtm.removeRow(i);
						}
							table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
							table.getColumnModel().removeColumn(table.getColumnModel().getColumn(1));
							table.getColumnModel().removeColumn(table.getColumnModel().getColumn(2));
							table.getColumnModel().removeColumn(table.getColumnModel().getColumn(3));
							table.getColumnModel().removeColumn(table.getColumnModel().getColumn(4));*/										
						//----------------------------------------------------						
							dtm.setColumnCount(0);
							dtm.setRowCount(0);						
						    dtm.addColumn("Nombre Cobro de Demanda");
					        dtm.addColumn("Valor Cargo");
					        dtm.addColumn("IVA");
					        dtm.addColumn("Nuevo Cargo");
					        dtm.addColumn("Aplica");
					    	od = mod.getOfertasDemanda();
							Object[] data = new Object[5];
					        for(Ofertas_demanda of: od){
					        	for(int i= 0; i<5;i++){
					        		if(i == 0)data[i]= of.getNOM_OFERTA();
					        		if(i == 1)data[i]= of.getCARGO();
					        		if(i == 2)data[i]= of.getIVA();
					        		if(i == 3)data[i]= "";
					        		if(i == 4)data[i]= true;      		
					        	}  	
					        	dtm.addRow(data);
					        }
					        table.setModel(dtm);
					        table.getTableHeader().setReorderingAllowed(false);
					        PonerAplicacion(table, table.getColumnModel().getColumn(4));	
					        tab.setViewportView(table);
					        panel_1.add(tab);						
					}
				});
				
				
				btnNewButton_2.setBounds(367, 669, 105, 23);				
				JButton btnNewButton_3 = new JButton("SALIR");
				btnNewButton_3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});		
				btnNewButton_3.setBounds(482, 669, 96, 23);
				panel_2.setLayout(null);
				
				textField_2 = new JTextField();
				textField_2.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						char key = e.getKeyChar();
						if(key < '0'||key >'9'||textField_2.getText().length()==5){
							e.consume();
					}
					}
				});
				textField_2.setColumns(10);
				textField_2.setBounds(649, 23, 92, 20);
				panel_2.add(textField_2);
				
				JLabel lblSpcodeNuevo = new JLabel("NUEVO SPCODE** ");
				lblSpcodeNuevo.setBounds(542, 26, 123, 14);
				panel_2.add(lblSpcodeNuevo);
				
				JLabel lblNuevoTmcode = new JLabel(" NUEVO TMCODE**");
				lblNuevoTmcode.setBounds(335, 26, 110, 14);
				panel_2.add(lblNuevoTmcode);
				
				textField = new JTextField();
				textField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						char key = e.getKeyChar();
						if(key < '0'||key >'9'||textField.getText().length()==6){
							e.consume();
						}
					}
				});
				textField.setColumns(10);
				textField.setBounds(440, 23, 92, 20);
				panel_2.add(textField);
				
				JLabel lblNewLabel_6 = new JLabel("PLAN EN BSCS:");
				lblNewLabel_6.setBounds(10, 53, 147, 17);
				panel_2.add(lblNewLabel_6);
				
				textField_9 = new JTextField();
				textField_9.setBounds(101, 51, 235, 20);
				panel_2.add(textField_9);
				textField_9.setColumns(10);
				
				JLabel lblPaqueteEnBscs = new JLabel("PAQUETE EN BSCS:");
				lblPaqueteEnBscs.setBounds(345, 51, 135, 17);
				panel_2.add(lblPaqueteEnBscs);
				
				textField_10 = new JTextField();
				textField_10.setBounds(475, 49, 266, 20);
				panel_2.add(textField_10);
				textField_10.setColumns(10);
				
				JButton btnNewButton_4 = new JButton("NUEVO");
				btnNewButton_4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						add_pplatPrepago  nppp = new add_pplatPrepago (panel_2, comboBox);						
						nppp.setVisible(true);
						
					}
				});
				btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 9));
				btnNewButton_4.setBounds(257, 24, 70, 18);
				panel_2.add(btnNewButton_4);
				contentPane.add(btnNewButton_1);
				contentPane.add(btnNewButton_2);
				contentPane.add(btnNewButton_3);				
				JButton btnGenbo = new JButton("GEN_BO");
				btnGenbo.setBackground(Color.GRAY);
				btnGenbo.addActionListener(new ActionListener() {
					@SuppressWarnings("static-access")
					public void actionPerformed(ActionEvent arg0) {						
						GenBoffer ofer = new GenBoffer();
						String [] args = null;
						try {
							ofer.main(args);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			
				btnGenbo.setBounds(10, 669, 104, 23);
				contentPane.add(btnGenbo);
				
				JButton btnGenpo = new JButton("GEN_PO");
				btnGenpo.addActionListener(new ActionListener() {
					@SuppressWarnings("static-access")
					public void actionPerformed(ActionEvent arg0) {						
						GenPO  po = new GenPO();
						String [] arg = null;
						try {
							po.main(arg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				btnGenpo.setBackground(Color.GRAY);
				btnGenpo.setBounds(124, 669, 89, 23);
				contentPane.add(btnGenpo);
		
	}
}
