package co.com.amdocs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.MaskFormatter;

public class FormatoFecha extends MaskFormatter{
	
	private static final long serialVersionUID = 1L;
	//private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
	private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	public FormatoFecha() throws ParseException
    {
        // Las # son cifras y representa "dd/mm/yy hh:mm:ss"
        super ("##/##/####");
    }
	
	public Object stringToValue(String text) throws ParseException
    {
        return formato.parseObject(text);
    }
	
	 public String valueToString(Object value) throws ParseException
	    {
	        if (value instanceof Date)
	            return formato.format((Date)value);
	        return formato.format(new Date());
	    }
}
