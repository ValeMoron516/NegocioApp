package com.desarrolloweb.NegocioApp.dtos.DetalleVentaDTO;

import java.math.BigDecimal;

public class DetalleVentaResponse {

	private Long id;
	private Long ventaId;
	private Long productoId;
	private Integer cantidad;
	private BigDecimal precioUnitario;

	public DetalleVentaResponse() {
	}

	public DetalleVentaResponse(Long id, Long ventaId, Long productoId,
			Integer cantidad, BigDecimal precioUnitario) {
		this.id = id;
		this.ventaId = ventaId;
		this.productoId = productoId;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
}
