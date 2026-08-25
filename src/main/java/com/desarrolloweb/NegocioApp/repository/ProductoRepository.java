package com.desarrolloweb.NegocioApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrolloweb.NegocioApp.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
    // List<Product> findByCategoriaId(Long categoriaId);
    boolean existsByCategoriaId(Long categoriaId); // para poder eliminar categorias sin productos asociados
}
