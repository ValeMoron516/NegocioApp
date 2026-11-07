package com.desarrolloweb.NegocioApp.entity;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "negocios")
public class Negocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Usuario usuario;
    @Column(length = 150, nullable = false)
    private String nombre;
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    // Las solicitudes se guardan en la misma tabla negocios.
    // Los negocios historicos se migran a APROBADO antes de activar esta version.
    @Column(name = "estado_aprobacion", nullable = false, length = 20)
    private String estadoAprobacion = "APROBADO";
    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;
    @Column(name = "fecha_decision")
    private LocalDateTime fechaDecision;
    @Column(name = "revisada_por_id")
    private Long revisadaPorId;
    @Column(name = "motivo_rechazo", length = 500)
    private String motivoRechazo;
    public boolean estaAprobado(){ return "APROBADO".equals(estadoAprobacion); }
    public String getEstadoAprobacion(){ return estadoAprobacion; }
    public void setEstadoAprobacion(String estado){ this.estadoAprobacion=estado; }
    public LocalDateTime getFechaSolicitud(){ return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fecha){ this.fechaSolicitud=fecha; }
    public LocalDateTime getFechaDecision(){ return fechaDecision; }
    public void setFechaDecision(LocalDateTime fecha){ this.fechaDecision=fecha; }
    public Long getRevisadaPorId(){ return revisadaPorId; }
    public void setRevisadaPorId(Long id){ this.revisadaPorId=id; }
    public String getMotivoRechazo(){ return motivoRechazo; }
    public void setMotivoRechazo(String motivo){ this.motivoRechazo=motivo; }
    public Negocio(Long id, Usuario usuario, String nombre, String descripcion) {
        this.id = id;
        this.usuario = usuario;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public Negocio() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

