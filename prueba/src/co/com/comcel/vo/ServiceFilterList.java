package co.com.comcel.vo;

import java.io.Serializable;

public class ServiceFilterList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8632738470710856750L;
	
	private int id;
	private String name;
	private String oldName;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	
	@Override
	public String toString() {
		return "ServiceFilterList [id=" + id + ", name=" + name + ", oldName="
				+ oldName + "]";
	}

}
