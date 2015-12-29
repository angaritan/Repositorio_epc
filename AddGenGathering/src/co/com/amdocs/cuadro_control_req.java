package co.com.amdocs;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.border.BevelBorder;

import co.com.amdocs.model.DataModel;
import co.com.amdocs.model.IDataModel;
import co.com.amdocs.vo.CcRequerimientos;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;

import com.toedter.calendar.JDateChooser;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class cuadro_control_req extends JFrame {

	private static final long serialVersionUID = 8184620480422668299L;

private JPanel contentPane;		
	
	private JTextField textField_1;
	private JTextField textField_5;
	private JTextField textField_8;
	private JTextField textField;
	private JTextField textField_9;
	private JTextField textField_10;
	/*Traer el listado (num_req)CCrequerimientos*/
	List <CcRequerimientos> Num_Requerimiento = new ArrayList<CcRequerimientos>();	
	List <CcRequerimientos> NumReqConsulta= new ArrayList<CcRequerimientos>();	
	CcRequerimientos Actualiza= new CcRequerimientos();
	IDataModel mod = new DataModel();
	/*variable fecha*/
	private String s ="";
	private String s2 ="";
	private Date fechaNull;
	private String FechaVar = "00/00/0000 00:00:00";
	private Date FechaEntrada = null;
	private Date FechaRecibido = null;
	
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	private JTextField textField_17;
	private JTextField textField_12;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cuadro_control_req frame = new cuadro_control_req();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public cuadro_control_req() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 865, 786);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCuadroDeControl = new JLabel("Cuadro de Control Requerimientos Prepago");
		lblCuadroDeControl.setFont(new Font("Arial Black", Font.PLAIN, 20));
		lblCuadroDeControl.setBounds(259, 35, 486, 50);
		contentPane.add(lblCuadroDeControl);
		
	JLabel lblNewLabel = new JLabel("");
	//	ImageIcon icono=new ImageIcon("D:\\AreaLocal\\Add_planesprepago\\images\\image001.png");
	//	Image Imagen= icono.getImage();
    //ImageIcon iconoEscalado= new ImageIcon(Imagen.getScaledInstance(40, 40,Image.SCALE_SMOOTH));
		
		lblNewLabel.setIcon(new ImageIcon("D:\\AreaLocal\\Add_planesprepago\\images\\logo.png"));
		lblNewLabel.setBounds(120, 11, 85, 97);
		contentPane.add(lblNewLabel);
		
		JLabel lblNmeroDeRequerimiento = new JLabel("Numero de Requerimiento");
		lblNmeroDeRequerimiento.setBounds(49, 119, 200, 33);
		contentPane.add(lblNmeroDeRequerimiento);
	
		JLabel lblNmeroDeDefecto = new JLabel("Numero de Defecto");
		lblNmeroDeDefecto.setBounds(249, 192, 106, 23);
		contentPane.add(lblNmeroDeDefecto);
		
		textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_1.getText().length()==10){
					e.consume();
				}
			}
		});
		textField_1.setColumns(10);
		textField_1.setBounds(247, 219, 141, 33);
		contentPane.add(textField_1);
		
		JLabel lblNombreDeRequerimiento = new JLabel("Nombre de Requerimiento");
		lblNombreDeRequerimiento.setBounds(518, 188, 174, 30);
		contentPane.add(lblNombreDeRequerimiento);
		
		
		
	
		
		
		JLabel label = new JLabel("Descripción Tarea");
		label.setBounds(76, 424, 200, 23);
		contentPane.add(label);
		
		final JTextArea textArea= new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(49, 452, 790, 97);
		contentPane.add(scrollPane);
		//contentPane.add(textField_4);
		
		JLabel lblFechaDeEntrega = new JLabel("Fecha de Entrega");
		lblFechaDeEntrega.setBounds(518, 293, 174, 50);
		contentPane.add(lblFechaDeEntrega);
		
		JLabel lblSolicitante = new JLabel("Solicitante");
		lblSolicitante.setBounds(76, 309, 79, 23);
		contentPane.add(lblSolicitante);
		
		JLabel lblFechaRecibido = new JLabel("Fecha recibido");
		lblFechaRecibido.setBounds(279, 312, 106, 23);
		contentPane.add(lblFechaRecibido);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		textField_5.setBounds(49, 338, 141, 33);
		contentPane.add(textField_5);
		
		JLabel lblAvance = new JLabel("% Avance");
		lblAvance.setBounds(730, 302, 65, 33);
		contentPane.add(lblAvance);
		
		JButton button = new JButton("Guardar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField_9.getText().equals("")
						||textField_5.getText().equals("")||s.equals("")||s2.equals("")
						||textArea.getText().equals("")||textField_10.getText().equals("")){
					JOptionPane.showMessageDialog(null,"¡Por favor llenar todos los campos Obligatorios!", "ERROR: ",JOptionPane.ERROR_MESSAGE);	  	  
				  }else{
						 CcRequerimientos traerInfo= new CcRequerimientos();
						 traerInfo.setSOLICITANTE(textField_5.getText().trim());
						 traerInfo.setFECHA_ENTREGA(s2.trim());
						 traerInfo.setFECHA_RECIBIDO(s.trim());
						 traerInfo.setDESCRIP_TAREA(textArea.getText().trim());
						 traerInfo.setREQUERIMIENTO(textField_9.getText().trim());
						 traerInfo.setAVANCE(textField_10.getText().trim());
						 traerInfo.setNUM_REQ(Long.valueOf(textField.getText().trim()));
						 if(textField_1.getText().equals("")){
							 	traerInfo.setNRO_DEFECTO(Long.valueOf(0));
						 }else {
						 traerInfo.setNRO_DEFECTO(Long.valueOf(textField_1.getText().trim()));
						 }					
					  
					  System.out.println(traerInfo.getREQUERIMIENTO());
					  try{
						  mod.insertCC_req(traerInfo);
						  JOptionPane.showMessageDialog(null, "Ha sido correcto el diligenciamiento de los datos");
						 
					  }	catch (Exception e4){
						  
						  JOptionPane.showMessageDialog(null,"Ya se encuentra los datos almacenados","Error",JOptionPane.ERROR_MESSAGE);
					  }
				  }
				
			}
		});
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(213, 43, 30));
		button.setBounds(161, 641, 85, 33);
		contentPane.add(button);
		
		JButton button_1 = new JButton("Exportar");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		button_1.setForeground(Color.WHITE);
		button_1.setBackground(new Color(213, 43, 30));
		button_1.setBounds(710, 144, 85, 33);
		contentPane.add(button_1);
		
		JButton button_2 = new JButton("Eliminar");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Registro?");
				System.out.println(resp);
				if(resp==0){
					mod.EliminarReq(Integer.parseInt(textField_8.getText()));
				JOptionPane.showMessageDialog(null,"El registro a sido  eliminado existosamente");
				}	
			}
		});
		button_2.setForeground(Color.WHITE);
		button_2.setBackground(new Color(213, 43, 30));
		button_2.setBounds(282, 641, 85, 33);
		contentPane.add(button_2);
		
		JButton button_3 = new JButton("Salir");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		button_3.setForeground(Color.WHITE);
		button_3.setBackground(new Color(213, 43, 30));
		button_3.setBounds(548, 641, 85, 33);
		contentPane.add(button_3);
		
		
		
		JButton button_4 = new JButton("Cancelar");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField_1.setText(""); 
				textField_9 .setText(""); 
				textField_5.setText("");  
				textField_8.setText(""); 
				textField_10.setText(""); 
				textArea.setText(""); 
			}
		});
		button_4.setForeground(Color.WHITE);
		button_4.setBackground(new Color(213, 43, 30));
		button_4.setBounds(425, 641, 85, 33);
		contentPane.add(button_4);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				CcRequerimientos Bus = new CcRequerimientos ();
				if(textField_8.getText().equals("")){
					Bus.setNUM_REQ(Long.valueOf(0));	
				}else{
					Bus.setNUM_REQ(Long.valueOf(textField_8.getText()));  
				}
				   
				NumReqConsulta= mod.getBuscarReq(Bus);
				
				for(CcRequerimientos g:NumReqConsulta){
					
					textField.setText(Long.toString(g.getNUM_REQ()));
					textField_1.setText(Long.toString(g.getNRO_DEFECTO()));
					textField_9.setText(g.getREQUERIMIENTO()); 
					textField_5.setText(g.getSOLICITANTE()); 
					textField_10.setText(g.getAVANCE()); 
					textArea.setText(g.getDESCRIP_TAREA());
					
					
				}
				
				
				
				
			}
		});
		btnBuscar.setForeground(Color.WHITE);
		btnBuscar.setBackground(new Color(213, 43, 30));
		btnBuscar.setBounds(139, 144, 94, 33);
		contentPane.add(btnBuscar);
		
		JButton btnActualizar = new JButton("Actualizar\r\n");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Actualiza.setNRO_DEFECTO(Long.parseLong(textField_1.getText()));
				Actualiza.setREQUERIMIENTO(textField_9.getText().trim());
				Actualiza.setFECHA_ENTREGA(s2.trim());
				Actualiza.setFECHA_RECIBIDO(s.trim());
				Actualiza.setSOLICITANTE(textField_5.getText().trim());
				Actualiza.setAVANCE(textField_10.getText().trim());
				Actualiza.setDESCRIP_TAREA(textArea.getText().trim());
				Actualiza.setNUM_REQ(Long.parseLong(textField_8.getText().trim()));
				mod.UpdateReq(Actualiza);
				
			}
		});
		btnActualizar.setForeground(Color.WHITE);
		btnActualizar.setBackground(new Color(213, 43, 30));
		btnActualizar.setBounds(574, 144, 94, 33);
		contentPane.add(btnActualizar);
		
		textField_8 = new JTextField();
		textField_8.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_8.getText().length()==10){
					e.consume();
				}
			}
		});
		textField_8.setColumns(10);
		textField_8.setBounds(49, 144, 80, 33);
		contentPane.add(textField_8);
		
		JLabel label_1 = new JLabel("N\u00FAmero de Requerimiento");
		label_1.setBounds(49, 179, 200, 50);
		contentPane.add(label_1);
		
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(49, 219, 141, 33);
		contentPane.add(textField);
		Num_Requerimiento = mod.getNum_req();
		for(CcRequerimientos i: Num_Requerimiento){
			
			textField.setText( Long.toString(i.getNUM_REQ()));
		}
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		textField_9.setBounds(492, 219, 347, 33);
		contentPane.add(textField_9);
		
		textField_10 = new JTextField();
		textField_10.setColumns(10);
		textField_10.setBounds(698, 338, 141, 33);
		contentPane.add(textField_10);
		
		textField_13 = new JTextField();
		textField_13.setText("(*)");
		textField_13.setForeground(Color.RED);
		textField_13.setFont(new Font("Arial Black", Font.PLAIN, 11));
		textField_13.setColumns(10);
		textField_13.setBounds(492, 194, 22, 23);
		contentPane.add(textField_13);
		
		textField_14 = new JTextField();
		textField_14.setText("(*)");
		textField_14.setForeground(Color.RED);
		textField_14.setFont(new Font("Arial Black", Font.PLAIN, 11));
		textField_14.setColumns(10);
		textField_14.setBounds(49, 308, 22, 23);
		contentPane.add(textField_14);
		
		textField_15 = new JTextField();
		textField_15.setText("(*)");
		textField_15.setForeground(Color.RED);
		textField_15.setFont(new Font("Arial Black", Font.PLAIN, 11));
		textField_15.setColumns(10);
		textField_15.setBounds(247, 310, 22, 23);
		contentPane.add(textField_15);
		
		textField_16 = new JTextField();
		textField_16.setText("(*)");
		textField_16.setForeground(Color.RED);
		textField_16.setFont(new Font("Arial Black", Font.PLAIN, 11));
		textField_16.setColumns(10);
		textField_16.setBounds(492, 306, 22, 23);
		contentPane.add(textField_16);
		
		textField_17 = new JTextField();
		textField_17.setText("(*)");
		textField_17.setForeground(Color.RED);
		textField_17.setFont(new Font("Arial Black", Font.PLAIN, 11));
		textField_17.setColumns(10);
		textField_17.setBounds(49, 424, 22, 23);
		contentPane.add(textField_17);
		
		textField_12 = new JTextField();
		textField_12.setText("(*)");
		textField_12.setForeground(Color.RED);
		textField_12.setFont(new Font("Arial Black", Font.PLAIN, 11));
		textField_12.setColumns(10);
		textField_12.setBounds(698, 306, 22, 23);
		contentPane.add(textField_12);
		
		final DateFormat lip = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		JDateChooser dateChooser = new JDateChooser(new Date(), "EEEE dd/MM/yyyy");
		dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				SimpleDateFormat lip = new SimpleDateFormat("EEEE dd/MM/yyyy");
				System.out.println(" Fecha inicio: "+ arg0.getNewValue());
				if( !arg0.getNewValue().toString().contains("javax.swing.JPanel")){
						s2  =lip.format(arg0.getNewValue());
				}
			}
		});
		
		dateChooser.setBounds(492, 348, 141, 23);
		contentPane.add(dateChooser);
		
		final DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		
		JDateChooser dateChooser_1 = new JDateChooser(new Date(), "EEEE dd/MM/yyyy");
		dateChooser_1.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy");
				System.out.println(" Fecha inicio: "+ arg0.getNewValue());
				if( !arg0.getNewValue().toString().contains("javax.swing.JPanel")){
						s  =sdf.format(arg0.getNewValue());
				}
			}
		});
		dateChooser_1.setBounds(249, 346, 139, 20);
		contentPane.add(dateChooser_1);
		
		
		
	
		
		
	}

}
