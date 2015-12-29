package co.com.comcel.vo;

import java.io.Serializable;

public class ServiceFilterToCharge implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4334142788862600445L;
	
	
	private int id;
	private String name;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceFilterToCharge [id=" + id + ", name=" + name + "]";
	}
	
	

}
