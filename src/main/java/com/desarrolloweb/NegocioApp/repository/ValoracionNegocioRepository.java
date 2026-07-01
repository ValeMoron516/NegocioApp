package com.desarrolloweb.NegocioApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrolloweb.NegocioApp.entity.ValoracionNegocio;

@Repository
public interface ValoracionNegocioRepository extends JpaRepository<ValoracionNegocio, Long> {

    List<ValoracionNegocio> findByNegocioId(Long negocioId);

    List<ValoracionNegocio> findByClienteId(Long clienteId);

    List<ValoracionNegocio> findByEstrellas(Integer estrellas);

}