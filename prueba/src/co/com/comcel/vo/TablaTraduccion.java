package co.com.comcel.vo;

public class TablaTraduccion {

	private Long tmcode;
	private Long spcode;
	private Long sncode;
	private String plan;
	private String paquete;
	private String descripcion;
	private String desc_bo;
	private String caract_eleg;
	private String fecha_inicio;
	private String fecha_venta;
	private Double CFM;	
	
	
	public String getFecha_venta() {
		return fecha_venta;
	}
	public void setFecha_venta(String fecha_venta) {
		this.fecha_venta = fecha_venta;
	}
	public String getFecha_inicio() {
		return fecha_inicio;
	}
	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}
	
	public Double getCFM() {
		return CFM;
	}	
	public void setCFM(Double cFM) {
		CFM = cFM;
	}
	public String getCaract_eleg() {
		return caract_eleg;
	}
	public void setCaract_eleg(String caractEleg) {
		caract_eleg = caractEleg;
	}
	public Long getTmcode() {
		return tmcode;
	}
	public void setTmcode(Long tmcode) {
		this.tmcode = tmcode;
	}
	public Long getSpcode() {
		return spcode;
	}
	public void setSpcode(Long spcode) {
		this.spcode = spcode;
	}
	public Long getSncode() {
		return sncode;
	}
	public void setSncode(Long sncode) {
		this.sncode = sncode;
	}
	public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public String getPaquete() {
		return paquete;
	}
	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDesc_bo() {
		return desc_bo;
	}
	public void setDesc_bo(String descBo) {
		desc_bo = descBo;
	}
	
}
