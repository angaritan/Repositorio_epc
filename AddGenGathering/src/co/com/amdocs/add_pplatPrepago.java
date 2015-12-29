package co.com.amdocs;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;

import co.com.amdocs.model.DataModel;
import co.com.amdocs.model.IDataModel;
import co.com.amdocs.vo.PlanPrepago;


import co.com.amdocs.vo.carac_elegidos;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class add_pplatPrepago extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
	private String carfnfcomb = ""; 
	private String carbfnfcomb = ""; 
	IDataModel mod = new DataModel();    
    static JFrame jf;
	List<PlanPrepago>   plp = new ArrayList<PlanPrepago>();
    List<carac_elegidos>  ce = new ArrayList<carac_elegidos>();
    List<carac_elegidos>  cefnf = new ArrayList<carac_elegidos>();
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					add_pplatPrepago frame = new add_pplatPrepago();
					jf =frame;
					jf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public add_pplatPrepago(final JPanel pane,final JComboBox comboB) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 781, 339);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "PLAN PLATAFORMA PREPAGO", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblElegidosFnf = new JLabel("ELEGIDOS FNF");
		lblElegidosFnf.setBounds(10, 71, 92, 14);
		contentPane.add(lblElegidosFnf);
		
		textField = new JTextField();
		textField.setBounds(105, 68, 86, 20);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField.getText().length()==2){
					e.consume();
				}
			}
		});
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("ELEGIDOS BFNF");
		lblNewLabel.setBounds(10, 96, 92, 14);
		contentPane.add(lblNewLabel);
		
		textField_1 = new JTextField();
		textField_1.setBounds(105, 96, 86, 20);
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_1.getText().length()==2){
					e.consume();
				}
			}
		});
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblDuracionServicio = new JLabel("DURACION SERVICIO FNF");
		lblDuracionServicio.setBounds(201, 71, 167, 14);
		contentPane.add(lblDuracionServicio);
		
		JLabel lblDuracionServicioBfnf = new JLabel("DURACION SERVICIO BFNF");
		lblDuracionServicioBfnf.setBounds(201, 96, 167, 14);
		contentPane.add(lblDuracionServicioBfnf);
		
		textField_2 = new JTextField();
		textField_2.setBounds(359, 68, 86, 20);
		textField_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_2.getText().length()==3){
					e.consume();
				}
			}
		});
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setBounds(359, 93, 86, 20);
		textField_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_3.getText().length()==3){
					e.consume();
				}
			}
		});
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblPlanPlataformaPreapgo = new JLabel("PLAN PLATAFORMA PREPAGO");
		lblPlanPlataformaPreapgo.setBounds(10, 29, 179, 14);
		contentPane.add(lblPlanPlataformaPreapgo);
		
		textField_4 = new JTextField();
		textField_4.setBounds(205, 26, 240, 20);
		contentPane.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblDurAntesDe = new JLabel("DUR. ANTES DE EXPIRACION SERV");
		lblDurAntesDe.setBounds(455, 71, 193, 14);
		contentPane.add(lblDurAntesDe);
		
		JLabel label = new JLabel("DUR. ANTES DE EXPIRACION SERV");
		label.setBounds(455, 96, 193, 14);
		contentPane.add(label);
		
		textField_5 = new JTextField();
		textField_5.setBounds(651, 68, 86, 20);
		textField_5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_5.getText().length()==3){
					e.consume();
				}
			}
		});
		contentPane.add(textField_5);
		textField_5.setColumns(10);
		
		
		textField_6 = new JTextField();
		textField_6.setBounds(651, 93, 86, 20);
		textField_6.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_6.getText().length()==3){
					e.consume();
				}
			}
		});
		contentPane.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel lblEnMeses_1 = new JLabel("EN MESES - (999 ILIMITADO)");
		lblEnMeses_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblEnMeses_1.setBounds(342, 46, 158, 14);
		contentPane.add(lblEnMeses_1);
		
		JLabel lblValor = new JLabel("VALOR MIN. ONNET");
		lblValor.setBounds(10, 173, 126, 14);
		contentPane.add(lblValor);
		
		textField_7 = new JTextField();
		textField_7.setBounds(127, 170, 107, 20);
		textField_7.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_7.getText().length()==5){
					e.consume();
				}
			}
		});
		contentPane.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel lblValorMinOffnet = new JLabel("VALOR MIN. OFFNET");
		lblValorMinOffnet.setBounds(249, 173, 132, 14);
		contentPane.add(lblValorMinOffnet);
		
		textField_8 = new JTextField();
		textField_8.setBounds(376, 170, 124, 20);
		textField_8.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_8.getText().length()==5){
					e.consume();
				}
			}
		});
		contentPane.add(textField_8);
		textField_8.setColumns(10);
		
		JLabel lblValorMinFijo = new JLabel("VALOR MIN. FIJO");
		lblValorMinFijo.setBounds(506, 173, 104, 14);
		contentPane.add(lblValorMinFijo);
		
		textField_9 = new JTextField();
		textField_9.setBounds(620, 170, 117, 20);
		textField_9.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_9.getText().length()==5){
					e.consume();
				}
			}
		});
		contentPane.add(textField_9);
		textField_9.setColumns(10);
		
		JLabel lblElegidosSmsOnnet = new JLabel("ELEGIDOS SMS");
		lblElegidosSmsOnnet.setBounds(10, 121, 92, 20);
		contentPane.add(lblElegidosSmsOnnet);
		
		textField_10 = new JTextField();
		textField_10.setBounds(105, 121, 86, 20);
		textField_10.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_10.getText().length()==2){
					e.consume();
				}
			}
		});
		contentPane.add(textField_10);
		textField_10.setColumns(10);
		
		JLabel lblDuracionServEleg = new JLabel("DUR. SERV. ELEG. SMS");
		lblDuracionServEleg.setBounds(201, 121, 145, 14);
		contentPane.add(lblDuracionServEleg);
		
		textField_11 = new JTextField();
		textField_11.setBounds(359, 121, 86, 20);
		textField_11.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_11.getText().length()==3){
					e.consume();
				}
			}
		});
		contentPane.add(textField_11);
		textField_11.setColumns(10);
		
		JLabel label_2 = new JLabel("DUR. ANTES DE EXPIRACION SERV");
		label_2.setBounds(455, 121, 193, 14);
		contentPane.add(label_2);
		
		textField_12 = new JTextField();
		textField_12.setBounds(651, 121, 86, 20);
		textField_12.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char key = e.getKeyChar();
				if(key < '0'||key >'9'||textField_12.getText().length()==3){
					e.consume();
				}
			}
		});
		contentPane.add(textField_12);
		textField_12.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("CARACTERISTICA BFNF");
		lblNewLabel_1.setBounds(10, 209, 146, 14);
		contentPane.add(lblNewLabel_1);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {				
				carbfnfcomb = (String)comboBox.getSelectedItem();
			}
		});
		comboBox.setBounds(144, 206, 193, 20);
		ce = mod.getCaractBFNF();
		comboBox.addItem("");
		for(carac_elegidos  car: ce){			
			comboBox.addItem(car.getDesc_eleg());			
		}
		contentPane.add(comboBox);
		
		JLabel lblCaracteristicaFnf = new JLabel("CARACTERISTICA FNF");
		lblCaracteristicaFnf.setBounds(396, 209, 139, 14);
		contentPane.add(lblCaracteristicaFnf);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				carfnfcomb = (String)comboBox_1.getSelectedItem();
			}
		});
		comboBox_1.setBounds(545, 206, 190, 20);
		
		cefnf = mod.getCaractFNF();
		comboBox_1.addItem("");
		for(carac_elegidos ceff: cefnf){			
			comboBox_1.addItem(ceff.getDesc_eleg());
		}		
		contentPane.add(comboBox_1);
		
		JButton btnNewButton = new JButton("ENVIAR");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				if(textField_1.getText().trim().equals("")||textField_2.getText().trim().equals("")||textField_3.getText().trim().equals("")
		       ||textField_4.getText().trim().equals("")||textField_5.getText().trim().equals("")||textField_5.getText().trim().equals("")
		       ||textField_6.getText().trim().equals("")||textField_7.getText().trim().equals("")||textField_8.getText().trim().equals("")
		       ||textField_9.getText().trim().equals("")||textField_10.getText().trim().equals("")||textField_11.getText().trim().equals("")
			   ||textField_12.getText().trim().equals("")||textField.getText().trim().equals("")||carbfnfcomb.trim().equals("")||carfnfcomb.trim().equals("")){
			   JOptionPane.showMessageDialog(null, "Diligenciar todos los campos solicitados", " ERROR", JOptionPane.ERROR_MESSAGE);
			 System.out.println(carbfnfcomb +"  eleccion " + carfnfcomb);
			}			
			else {
				
				PlanPrepago pp =  new PlanPrepago();
				pp.setFfsmsmaxnumele_on(textField_10.getText().trim());
				pp.setBfnfvdurtd(textField_3.getText().trim());
				pp.setFfsmsdur(textField_11.getText().trim());
				pp.setFfvzdur(textField_2.getText().trim());
				pp.setPlanprepago(textField_4.getText().trim());
			    pp.setCaract_elegbfnf(carbfnfcomb);
			    pp.setCaract_elegfnf(carfnfcomb);
			    pp.setFfvzdurtillre(textField_5.getText().trim());
			    pp.setBfnfvdurtillre(textField_6.getText().trim());
			    pp.setFfsmsdurtillre(textField_12.getText().trim());
			    pp.setBfnfvdaynextnch("0");
			    pp.setFfsmsdaynextnch("0");
			    pp.setFfvzdaynextnch("0");
			    pp.setFfvzmaxnumele_td(textField.getText().trim());
			  
			try {
				pp.setElegidos_bfnf(Long.parseLong(textField_1.getText().trim()));
				pp.setElegidos_fnf(Long.parseLong(textField.getText().trim()));			
				pp.setValor_onnet(Long.parseLong(textField_7.getText().trim()));
				pp.setValor_offnet(Long.parseLong(textField_8.getText().trim()));
				pp.setValor_fijo(Long.parseLong(textField_9.getText().trim()));
				mod.insertPrepagoPlat(pp);				
			}
			catch(NumberFormatException e ){
				JOptionPane.showMessageDialog(null,"Debe digitar numero en los campos de elegidos o valores, Rectifique! \n" + e, "ERROR.", JOptionPane.ERROR_MESSAGE);
					}			
			catch(Exception exc){
				JOptionPane.showMessageDialog(null,"ERROR! \n" + exc, "ERROR.", JOptionPane.ERROR_MESSAGE);
					}
			finally{
				JOptionPane.showMessageDialog(null, "AVISO!!! FUE INSERTADO PLAN PREPAGO EXITOSAMENTE", "AVISO", JOptionPane.INFORMATION_MESSAGE);
			}
			
			}
			}
		});
		btnNewButton.setBounds(180, 251, 107, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("CANCELAR");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
				textField_1.setText("");
				textField_2.setText("");
				textField_3.setText("");
				textField_4.setText("");
				textField_5.setText("");
				textField_6.setText("");
				textField_7.setText("");
				textField_8.setText("");
				textField_9.setText("");
				textField_10.setText("");
				textField_11.setText("");
				textField_12.setText("");			
			}
		});
		btnNewButton_1.setBounds(313, 251, 107, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("SALIR");
		btnNewButton_2.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
				comboB.removeAllItems();
				List<PlanPrepago> pp = mod.getPlanPrepago();
				comboB.addItem("");
				for(PlanPrepago  pl: pp){
					comboB.addItem(pl.getPlanprepago());
				}
				pane.add(comboB);			
			}
		});
		btnNewButton_2.setBounds(446, 251, 107, 23);
		contentPane.add(btnNewButton_2);
		
		JLabel label_1 = new JLabel("EN MESES - (999 ILIMITADO)");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 10));
		label_1.setBounds(625, 46, 158, 14);
		contentPane.add(label_1);
		
	}
}
