package com.desarrolloweb.NegocioApp.dto;
import java.time.LocalDate;
import java.math.BigDecimal;

public class VentaResponseDto {
    private Long id;
    private Long usuarioId;
    private Long direccionId;
    private Long negocioId;
    private LocalDate fechaVenta;
    private String estado;
    private BigDecimal total;
    public VentaResponseDto(){

    }
    public VentaResponseDto(Long id, Long usuarioId, Long direccionId, Long negocioId, LocalDate fechaVenta, String estado, BigDecimal total){
        this.id = id;
        this.usuarioId = usuarioId;
        this.direccionId = direccionId;
        this.negocioId = negocioId;
        this.fechaVenta = fechaVenta;
        this.estado = estado;
        this.total = total;
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public Long getUsuarioId(){
        return usuarioId;
    }
    public void setUsuarioId(Long usuarioId){
        this.usuarioId = usuarioId;
    }
    public Long getDireccionId(){
        return direccionId;
    }
    public void setDireccionId(Long direccionId){
        this.direccionId = direccionId;
    }
    public Long getNegocioId(){
        return negocioId;
    }
    public void setNegocioId(Long negocioId){
        this.negocioId = negocioId;
    }
    public LocalDate getFechaVenta(){
        return fechaVenta;
    }
    public void setFechaVenta(LocalDate fechaVenta){
        this.fechaVenta = fechaVenta;
    }
    public String getEstado(){
        return estado;
    }
    public void setEstado(String estado){
        this.estado = estado;
    }
    public BigDecimal getTotal(){
        return total;
    }
    public void setTotal(BigDecimal total){
        this.total = total;
    }
}
