package co.com.comcel.vo;

import java.util.ArrayList;
import java.util.List;

public class Price {
	
	private String name;
	private Long prioridad;
	private int indicadorGenerico;
	
	private Long allwGenerico;
	private String rateGenerico;
	
	private List<Prit> pritList;
	
	public Price(){
		this.pritList = new ArrayList<Prit>();
		this.prioridad=0L;
	}
	
	public Long getAllwGenerico() {
		return allwGenerico;
	}
	public void setAllwGenerico(Long allwGenerico) {
		this.allwGenerico = allwGenerico;
	}
	public String getRateGenerico() {
		return rateGenerico;
	}
	public void setRateGenerico(String rateGenerico) {
		this.rateGenerico = rateGenerico;
	}
	public int getIndicadorGenerico() {
		return indicadorGenerico;
	}
	public void setIndicadorGenerico(int indicadorGenerico) {
		this.indicadorGenerico = indicadorGenerico;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(Long prioridad) {
		this.prioridad = prioridad;
	}
	public List<Prit> getPritList() {
		return pritList;
	}
	public void setPritList(List<Prit> pritList) {
		this.pritList = pritList;
	}

	
}
