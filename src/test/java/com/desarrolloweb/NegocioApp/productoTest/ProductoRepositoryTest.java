//package com.desarrolloweb.NegocioApp.productoTest;

//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;

//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
//import org.springframework.test.context.jdbc.Sql;

//import com.desarrolloweb.NegocioApp.entity.Producto;
//import com.desarrolloweb.NegocioApp.entity.Negocio;
//import com.desarrolloweb.NegocioApp.entity.Usuario;
//import com.desarrolloweb.NegocioApp.entity.Categoria;
//import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
//import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
//import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
//import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;

//@DataJpaTest
//public class ProductoRepositoryTest {

//    @Autowired
//    ProductoRepository productoRepository;
//    
//    @Autowired
//    NegocioRepository negocioRepository;
//    
//    @Autowired
//    UsuarioRepository usuarioRepository;
//    
//    @Autowired
//    CategoriaRepository categoriaRepository;

//    // ##################################################
//    
//    @Test
//    void existByCategoriaId_Existente() {
//        Usuario u = new Usuario('María', 'Gómez', '+541123456789', 'maria.gomez@email.com', '$2b$10$wXyZ2');
//        Usuario uP = usuarioReposotory.save(u);
//        
//        Categoria c = new Categoria('Indumentaria', 'Ropa, calzado y accesorios para todas las edades');
//        Categoria cP = categoriaRepository.save(c);
//        
//        Negocio n = new Negocio(uP, 'Sublime Ropa', 'Moda urbana y tendencias actuales (Propiedad de María)');
//        Negocio nP = negocioRepository.save(n);
//        
//        Producto p = new Producto(nP, cP, 'Pantalón Cargo Verde', 'Pantalón resistente con múltiples bolsillos', 35000.00, 12);
//        productoRepository.save(p)
//        
//        Long id = 1L;

//        boolean respuesta = productoRepository.existsByCategoriaId(id);
//        
//        assertTrue(respuesta);
//    }


//    @Test
//    void existByCategoriaId_Inexistente() {
//        Long id = 1L;

//        boolean respuesta = productoRepository.existsByCategoriaId(id);
//        
//        assertFalse(respuesta);
//    }
//}
