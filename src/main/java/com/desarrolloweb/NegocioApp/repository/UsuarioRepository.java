package com.desarrolloweb.NegocioApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desarrolloweb.NegocioApp.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);

    // Usado por RolService para impedir borrar un rol que tiene usuarios
    @Query("SELECT COUNT(u) > 0 FROM Usuario u JOIN u.roles r WHERE r.id = :rolId")
    boolean existsByRolId(@Param("rolId") Long rolId);
}