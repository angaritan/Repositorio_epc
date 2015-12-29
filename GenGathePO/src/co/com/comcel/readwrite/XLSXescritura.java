package co.com.comcel.readwrite;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class XLSXescritura {
	static int BUFFER = 2048; 
	 static byte[] tempBuffer = null;


	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		byte data[] = new byte[BUFFER]; 
		
		FileInputStream fis = new FileInputStream("D:\\Libro1.xlsx"); 
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis)); 
 
        FileOutputStream fos =  new FileOutputStream("D:\\libro2.xlsx"); 
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos)); 
        ZipEntry  entry ;
        while ((entry = zis.getNextEntry()) != null) { 
 
            System.out.println("Processing Entry : " + entry.getName()); 
            System.out.println("Processing Entry Size : " + entry.getSize()); 
            System.out.println("Entry Available : " + zis.available()); 
 
       /* if(entry.getName().equals(ZIP_ENTRY_WOOKBOOK_XML)){ 
          //Process XML 
        } */
        int count;
        ZipEntry ze = new ZipEntry(entry.getName()); 
            zos.putNextEntry(ze); 
            
           if(entry.getName().equals("Libro2.xml")){ 
                 //   zos.write(XML.getBytes()); 
            }else{ 
                ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
                while (( count = zis.read(data, 0, BUFFER)) != -1) { 
                    stream.write(data, 0, count); 
                } 
               
                tempBuffer = stream.toByteArray(); 
                zos.write(tempBuffer); 
            } 
            zos.closeEntry();                
        } 
        zos.close(); 
        fos.close(); 
        zis.close(); 
        fis.close(); 


	}

}
