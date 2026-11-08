package com.desarrolloweb.NegocioApp.dto.detalleVenta;

public class DetalleVentaRequestDTO {

	private Long ventaId;
	private Long productoId;
	private Integer cantidad;

	public DetalleVentaRequestDTO() {
	}

	public DetalleVentaRequestDTO(Long ventaId, Long productoId, Integer cantidad) {
		this.ventaId = ventaId;
		this.productoId = productoId;
		this.cantidad = cantidad;
	}

	public Long getVentaId() {
		return ventaId;
	}

	public void setVentaId(Long ventaId) {
		this.ventaId = ventaId;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
