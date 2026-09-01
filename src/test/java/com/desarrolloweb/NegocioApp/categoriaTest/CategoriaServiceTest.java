package com.desarrolloweb.NegocioApp.categoriaTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import com.desarrolloweb.NegocioApp.entity.Categoria;
import com.desarrolloweb.NegocioApp.dtos.categoriaDTO.CategoriaResponseDTO;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.repository.CategoriaRepository;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.service.CategoriaService;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

	@Mock
	CategoriaRepository categoriaRepository; // Modulo simulado

	@Mock
	ProductoRepository productoRepository; // Modulo simulado

	@InjectMocks
	CategoriaService categoriaService; // Modulo principal

	// ##################################################
	// ##################################################
	
	@Test
	void obtenerCategoriaPorId_Exitoso() {
	    Long id = 1L;
	    Categoria respuesta = new Categoria(1L, "Hogar", "Productos para el hogar");
	    when(categoriaRepository.findById(id)).thenReturn(Optional.of(respuesta));
	    
	    CategoriaResponseDTO respuestaService = categoriaService.obtenerCategoriaPorId(id);
	    
	    assertEquals(respuesta.getId(), respuestaService.getId());
	    assertEquals(respuesta.getNombre(), respuestaService.getNombre());
	    assertEquals(respuesta.getDescripcion(), respuestaService.getDescripcion());
	    
	    verify(categoriaRepository, times(1)).findById(id);
	}
	
	@Test
	void obtenerCategoriaPorId_Invalido() {
	    Long id = 1L;
	    when(categoriaRepository.findById(id)).thenReturn(Optional.empty());
	    
	    Exception excepcion = assertThrows(NotFoundException.class, () -> {
            categoriaService.obtenerCategoriaPorId(id); 
        });
        
        assertEquals("El elemento solicitado no existe", excepcion.getMessage());
        verify(categoriaRepository, times(1)).findById(id);
	}
	
	// ##################################################
	// ##################################################

	@Test
	void borrarCategoriaPorId_Inexistente() {
		Long id = 1L;
		when(categoriaRepository.existsById(id)).thenReturn(false);

		Exception excepcion = assertThrows(NotFoundException.class, () -> {
			categoriaService.borrarCategoriaPorId(id);
		});

        assertEquals("La categoria con el ID provisto no existe", excepcion.getMessage());
		verify(categoriaRepository, times(1)).existsById(anyLong());
		verify(productoRepository, never()).existsByCategoriaId(anyLong());
		verify(categoriaRepository, never()).deleteById(anyLong());
	}

	@Test
	void borrarCategoriaPorId_ProductosAsociados() {
		Long id = 1L;
		when(categoriaRepository.existsById(id)).thenReturn(true);
		when(productoRepository.existsByCategoriaId(id)).thenReturn(true);

		Exception excepcion = assertThrows(ConflictException.class, () -> {
			categoriaService.borrarCategoriaPorId(id);
		});
		
        assertEquals("La categoria provista no puede eliminarse debido a que tiene productos asociados", excepcion.getMessage());
		verify(categoriaRepository, times(1)).existsById(anyLong());
		verify(productoRepository, times(1)).existsByCategoriaId(anyLong());
		verify(categoriaRepository, never()).deleteById(anyLong());
	}

	@Test
	void borrarCategoriaPorId_Exitoso() {
		Long id = 1L;
		when(categoriaRepository.existsById(id)).thenReturn(true);
		when(productoRepository.existsByCategoriaId(id)).thenReturn(false);

		categoriaService.borrarCategoriaPorId(id);

		verify(categoriaRepository, times(1)).existsById(anyLong());
		verify(productoRepository, times(1)).existsByCategoriaId(anyLong());
		verify(categoriaRepository, times(1)).deleteById(anyLong());
	}
}
