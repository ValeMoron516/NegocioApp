package com.backend.javaCrudApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.javaCrudApp.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>{
    
}
