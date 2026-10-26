package com.desarrolloweb.NegocioApp.repository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.desarrolloweb.NegocioApp.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaVentaBetween(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
    List<Venta> findByUsuarioId(Long usuarioId);
    Long countByNegocioId(Long negocioId);
}