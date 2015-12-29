package co.com.amdocs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class VerificarTexto extends InputVerifier {

	Pattern formato = Pattern.compile("Plan - .*"); 
	Matcher mat;
	
		public boolean verify(JComponent input){
		
		if(input instanceof JTextField){
			String texto = ((JTextField)input).getText();			
			if(texto.length()<=5){
				
				mat = formato.matcher(texto);
				if(mat.matches()){
					return true;
				}
				
				return false;				
			}
		}
	return false;	
	}

	
	
}
