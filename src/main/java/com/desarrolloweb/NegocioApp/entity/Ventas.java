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
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuarios usuarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Direccion direccion;

    @Column(name = "fecha_venta", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaVentas;

    @Column(name = "estado", length = 50, nullable = false, columnDefinition = "VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE'")
    private String estado;

    @Column(name = "total", nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0.0 CHECK (total >= 0)")
    private BigDecimal total;

    public Ventas() {}

    public Ventas(Long id, Usuarios usuarios, Direccion direccion, LocalDateTime fechaVentas, String estado, BigDecimal total) {
        this.id = id;
        this.usuarios = usuarios;
        this.direccion = direccion;
        this.fechaVentas = fechaVentas;
        this.estado = estado;
        this.total = total;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuarios getUsuarios() { return usuarios; }
    public void setUsuarios(Usuarios usuarios) { this.usuarios = usuarios; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

    public LocalDateTime getFechaVentas() { return fechaVentas; }
    public void setFechaVentas(LocalDateTime fechaVentas) { this.fechaVentas = fechaVentas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
