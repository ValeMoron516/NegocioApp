package com.desarrolloweb.NegocioApp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "valoraciones_producto")
public class ValoracionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto productoId;

    @Column(nullable = false)
    private Integer puntuacion;

    @Column(nullable = true, length = 500)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    // 1. Constructor vacío obligatorio para JPA
    public ValoracionProducto() {}

    // 2. Constructor con todos los datos usando tus setters
    public ValoracionProducto(Long id, Usuario usuarioId, Producto productoId, Integer puntuacion, String comentario, LocalDateTime fecha) {
        setId(id);
        setUsuarioId(usuarioId);
        setProductoId(productoId);
        setPuntuacion(puntuacion);
        setComentario(comentario);
        setFecha(fecha);
    }

    // Getters convencionales
    public Long getId() { 
        return id; 
    }

    public Usuario getUsuarioId() { 
        return usuarioId; 
    }

    public Producto getProductoId() { 
        return productoId; 
    }

    public Integer getPuntuacion() { 
        return puntuacion; 
    }

    public String getComentario() { 
        return comentario; 
    }

    public LocalDateTime getFecha() { 
        return fecha; 
    }

    // Setters que juegan con los Getters para controlar el flujo (sin usar 'this.')
    public void setId(Long nuevoId) {
        if (nuevoId != getId()) {
            id = nuevoId;
        }
    }

    public void setUsuarioId(Usuario nuevoUsuarioId) {
        if (nuevoUsuarioId != getUsuarioId()) {
            usuarioId = nuevoUsuarioId;
        }
    }

    public void setProductoId(Producto nuevoProductoId) {
        if (nuevoProductoId != getProductoId()) {
            productoId = nuevoProductoId;
        }
    }

    public void setPuntuacion(Integer nuevaPuntuacion) {
        if (nuevaPuntuacion != null && !nuevaPuntuacion.equals(getPuntuacion())) {
            puntuacion = nuevaPuntuacion;
        }
    }

    public void setComentario(String nuevoComentario) {
        if (nuevoComentario != null && !nuevoComentario.equals(getComentario())) {
            comentario = nuevoComentario;
        }
    }

    public void setFecha(LocalDateTime nuevaFecha) {
        if (nuevaFecha != null && !nuevaFecha.equals(getFecha())) {
            fecha = nuevaFecha;
        }
    }
}