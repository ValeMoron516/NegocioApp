package com.desarrolloweb.NegocioApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrolloweb.NegocioApp.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByTelefono(String telefono);

    Optional<Usuario> findByTelefono(String telefono);
}