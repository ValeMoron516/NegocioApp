package com.desarrolloweb.NegocioApp.dto.producto;

import java.math.BigDecimal;
 

public class ProductoRequestDTO {

    private Long negocioId;
    private String nombreNegocio;
    private Long categoriaId;
    private String nombreCategoria;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String imagenUrl;

    // Constructores
    public ProductoRequestDTO() { }

    public ProductoRequestDTO(Long negocioId, String nombreNegocio, 
            Long categoriaId, String nombreCategoria, String nombre,
            String descripcion, Double precio, Integer stock, String imagenUrl) {
        this.negocioId = negocioId;
        this.nombreNegocio = nombreNegocio;
        this.categoriaId = categoriaId;
        this.nombreCategoria = nombreCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagenUrl = imagenUrl;
    }

    // --- Getters y Setters ---
    public Long getNegocioId() { return negocioId; }
    public void setNegocioId(Long negocioId) { this.negocioId = negocioId; }

    public String getNombreNegocio() { return nombreNegocio; }
    public void setNombreNegocio(String nombreNegocio) { this.nombreNegocio = nombreNegocio; }

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
        
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
