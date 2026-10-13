package com.desarrolloweb.NegocioApp.repository;


import com.desarrolloweb.NegocioApp.entity.ValoracionProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ValoracionProductoRepository extends JpaRepository<ValoracionProducto, Long> {
    List<ValoracionProducto> findByProductoId_Id(Long productoId);
}
