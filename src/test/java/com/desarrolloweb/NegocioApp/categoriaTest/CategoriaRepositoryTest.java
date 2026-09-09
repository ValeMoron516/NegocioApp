package com.desarrolloweb.NegocioApp.categoriaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;

@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    CategoriaRepository categoriaRepository;

    // @AfterEach
    // void limpiarBdd() {
    //     categoriaRepository.deleteAll();
    // }
    
    @Test
    void existByNombre_Existente() {
        String nombreCategoria = "Hogar";
        Categoria c = new Categoria(null, "Hogar", "Productos para el hogar");
        categoriaRepository.save(c);

        boolean respuesta = categoriaRepository.existsByNombre(nombreCategoria);
        
        assertTrue(respuesta);
    }


    @Test
    void existByNombre_Inexistente() {
        String nombreCategoria = "Hogar";

        boolean respuesta = categoriaRepository.existsByNombre(nombreCategoria);
        
        assertFalse(respuesta);
    }
}
