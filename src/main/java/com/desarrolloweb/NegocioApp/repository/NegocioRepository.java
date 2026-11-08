package com.desarrolloweb.NegocioApp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desarrolloweb.NegocioApp.entity.Negocio;

public interface NegocioRepository extends JpaRepository <Negocio, Long> {
    List<Negocio>findByUsuarioId(Long usuarioId);
    @Query("select n from Negocio n where n.usuario.id=:usuarioId and n.fechaSolicitud is not null order by n.fechaSolicitud desc, n.id desc")
    List<Negocio> solicitudesDelUsuario(@Param("usuarioId") Long usuarioId);
    @Query("select n from Negocio n where n.fechaSolicitud is not null order by n.fechaSolicitud desc, n.id desc")
    List<Negocio> solicitudesOrdenadas();
    @Query("select n from Negocio n where n.fechaSolicitud is not null and n.estadoAprobacion=:estado order by n.fechaSolicitud desc, n.id desc")
    List<Negocio> solicitudesPorEstado(@Param("estado") String estado);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select n from Negocio n where n.id=:id and n.fechaSolicitud is not null")
    Optional<Negocio> solicitudBloqueada(@Param("id") Long id);
    List<Negocio>findByNombreContainingIgnoreCase(String nombre);
    @Query("SELECT DISTINCT a.negocio FROM Producto a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombreProducto, '%'))")
    List<Negocio> findByProductoNombre(@Param("nombreProducto") String nombreProducto);
}



