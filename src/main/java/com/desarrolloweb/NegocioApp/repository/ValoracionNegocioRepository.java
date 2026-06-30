package com.desarrolloweb.NegocioApp.repository;

import com.desarrolloweb.NegocioApp.entity.ValoracionNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValoracionNegocioRepository extends JpaRepository<ValoracionNegocio, Long> {
}