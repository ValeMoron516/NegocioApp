package com.desarrolloweb.NegocioApp.productoTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import java.math.BigDecimal;

@DataJpaTest
public class ProductoRepositoryTest {

	@Autowired
	ProductoRepository productoRepository;

	@Autowired
	NegocioRepository negocioRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	ProductoRepository productoRepository;

	// ##################################################

	@Test
	void existByProductoId_Existente() {
		Usuario u = new Usuario("María", "Gómez", "+541123456789", "maria.gomez@email.com", "$2b$10$wXyZ2");
		Usuario uP = usuarioRepository.save(u);

		Producto c = new Producto("Indumentaria", "Ropa, calzado y accesorios para todas las edades");
		Producto cP = productoRepository.save(c);

		Negocio n = new Negocio(uP, "Sublime Ropa", "Moda urbana y tendencias actuales (Propiedad de María)");
		Negocio nP = negocioRepository.save(n);

		Producto p = new Producto(nP, cP, "Pantalón Cargo Verde", "Pantalón resistente con múltiples bolsillos", new BigDecimal("35000.00"), 12);
		productoRepository.save(p);

		Long id = 1L;

		boolean respuesta = productoRepository.existsByProductoId(id);

		assertTrue(respuesta);
	}


	@Test
	void existByProductoId_Inexistente() {
		Long id = 1L;

		boolean respuesta = productoRepository.existsByProductoId(id);

		assertFalse(respuesta);
	}
}
