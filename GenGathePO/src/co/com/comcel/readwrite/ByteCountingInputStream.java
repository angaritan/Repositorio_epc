package co.com.comcel.readwrite;


import java.io.FilterInputStream; 
import java.io.IOException; 
import java.io.InputStream; 
 
/** 
 *  
 */ 
 
/** 
 * @author clint 
 *  
 */ 

public class ByteCountingInputStream extends FilterInputStream  {
	public int totalRead = 0; 
	 
	  /** 
	   * @param in 
	   */ 
	  protected ByteCountingInputStream(InputStream in) { 
	    super(in); 
	    // TODO Auto-generated constructor stub 
	  } 
	 
	  /* (non-Javadoc) 
	   * @see java.io.FilterInputStream#read() 
	   */ 
	  @Override 
	  public int read() throws IOException { 
	    int ret = super.read(); 
	    totalRead++; 
	    return ret; 
	  } 
	 
	  /* (non-Javadoc) 
	   * @see java.io.FilterInputStream#read(byte[], int, int) 
	   */ 
	  @Override 
	  public int read(byte[] b, int off, int len) throws IOException { 
	    int ret = super.read(b, off, len); 
	    totalRead += ret; 
	    return ret; 
	  } 
	 
	  /* (non-Javadoc) 
	   * @see java.io.FilterInputStream#read(byte[]) 
	   */ 
	  @Override 
	  public int read(byte[] b) throws IOException { 
	    int ret = super.read(b); 
	    totalRead += ret; 
	    return ret; 
	  } 
	 
	  /* (non-Javadoc) 
	   * @see java.io.FilterInputStream#skip(long) 
	   */ 
	  @Override 
	  public long skip(long n) throws IOException { 
	    //What to do? 
	    return super.skip(n); 
	  } 
	 
	  /** 
	   * @return the totalRead 
	   */ 
	  protected int getTotalRead() { 
	    return this.totalRead; 
	  } 

}
