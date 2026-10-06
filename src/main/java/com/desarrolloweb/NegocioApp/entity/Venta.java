package com.desarrolloweb.NegocioApp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Direccion direccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "negocio_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Negocio negocio;

    @Column(name = "fecha_venta", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaVenta;

    @Column(name = "estado", length = 50, nullable = false, columnDefinition = "VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE'")
    private String estado;

    @Column(name = "total", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.0 CHECK (total >= 0)")
    private BigDecimal total;

    public Venta() {}

    public Venta(Long id, Usuario usuario, Direccion direccion, Negocio negocio, LocalDateTime fechaVenta, String estado, BigDecimal total) {
        this.id = id;
        this.usuario = usuario;
        this.direccion = direccion;
        this.negocio = negocio;
        this.fechaVenta = fechaVenta;
        this.estado = estado;
        this.total = total;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    public Negocio getNegocio() { return negocio;}
    public void setNegocio( Negocio negocio){ this.negocio = negocio; }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) { this.fechaVenta = fechaVenta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}