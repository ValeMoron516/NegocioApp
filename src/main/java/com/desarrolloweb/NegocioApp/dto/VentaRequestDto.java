
package com.desarrolloweb.NegocioApp.dto;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VentaRequestDto {

    private Long usuarioId;
    private Long direccionId;
    private Long negocioId;
    private LocalDate fechaVenta;
    private String estado;
    private BigDecimal total;

    private List<ProductoCompraDto> productos = new ArrayList<>();

    public VentaRequestDto() {
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(Long direccionId) {
        this.direccionId = direccionId;
    }

    public Long getNegocioId() {
        return negocioId;
    }

    public void setNegocioId(Long negocioId) {
        this.negocioId = negocioId;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ProductoCompraDto> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCompraDto> productos) {
        this.productos = productos;
    }

    public record ProductoCompraDto(
            Long productoId,
            Integer cantidad
    ) {
    }
}
