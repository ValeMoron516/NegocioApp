package com.desarrolloweb.NegocioApp.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.desarrolloweb.NegocioApp.entity.Venta;
import jakarta.persistence.LockModeType;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaVentaBetween(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
    List<Venta> findByUsuarioId(Long usuarioId);
    Long countByNegocioId(Long negocioId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Venta v WHERE v.id = :id")
    Optional<Venta> buscarParaModificar(@Param("id") Long id);
}
