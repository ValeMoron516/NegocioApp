
package com.desarrolloweb.NegocioApp.dto.venta;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VentaRequestDTO {

    private Long usuarioId;
    private Long direccionId;
    private Long negocioId;
    private LocalDate fechaVenta;
    private String estado;
    private BigDecimal total;

    private List<ProductoCompraDTO> productos = new ArrayList<>();

    public VentaRequestDTO() {
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

    public List<ProductoCompraDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCompraDTO> productos) {
        this.productos = productos;
    }

    public record ProductoCompraDTO(
            Long productoId,
            Integer cantidad
    ) {
    }
}
