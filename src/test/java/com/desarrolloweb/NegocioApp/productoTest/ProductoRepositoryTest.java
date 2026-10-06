package com.desarrolloweb.NegocioApp.productoTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import java.math.BigDecimal;

@DataJpaTest
public class ProductoRepositoryTest {

	@Autowired
	CategoriaRepository categoriaRepository;

	@Autowired
	NegocioRepository negocioRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ProductoRepository productoRepository;

	// ##################################################

	@Test
	void existByProductoId_Existente() {
		Usuario u = new Usuario(null, "María", "Gómez", "+541123456789", "maria.gomez@email.com", "$2b$10$wXyZ2", null);
		Usuario uP = usuarioRepository.save(u);

		Categoria c = new Categoria(null, "Indumentaria", "Ropa, calzado y accesorios para todas las edades");
		Categoria cP = categoriaRepository.save(c);

		Negocio n = new Negocio(null, uP, "Sublime Ropa", "Moda urbana y tendencias actuales (Propiedad de María)");
		Negocio nP = negocioRepository.save(n);

		Producto p = new Producto(null, nP, cP, "Pantalón Cargo Verde", "Pantalón resistente con múltiples bolsillos", new BigDecimal("35000.00"), 12);
		productoRepository.save(p);

		Long id = cP.getId();

		boolean respuesta = productoRepository.existsByCategoriaId(id);

		assertTrue(respuesta);
	}


	@Test
	void existByProductoId_Inexistente() {
		Categoria c = new Categoria(null, "Indumentaria", "Ropa, calzado y accesorios para todas las edades");
		Categoria cP = categoriaRepository.save(c);

		Long id = cP.getId();

		boolean respuesta = productoRepository.existsByCategoriaId(id);

		assertFalse(respuesta);
	}
}
