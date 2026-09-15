package com.desarrolloweb.NegocioApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desarrolloweb.NegocioApp.entity.Negocio;

public interface NegocioRepository extends JpaRepository <Negocio, Long> {
    List<Negocio>findByUsuarioId(Long usuarioId);
    List<Negocio>findByNombreContainingIgnoreCase(String nombre);
    @Query("SELECT DISTINCT a.negocio FROM Producto a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombreProducto, '%'))")
    List<Negocio> findByProductoNombre(@Param("nombreProducto") String nombreProducto);
}



