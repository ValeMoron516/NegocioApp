package com.desarrolloweb.NegocioApp;

import com.desarrolloweb.NegocioApp.dto.ValoracionProductoRequestDTO;
import com.desarrolloweb.NegocioApp.entity.Producto;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.ValoracionProducto;
import com.desarrolloweb.NegocioApp.repository.ProductoRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.ValoracionProductoRepository;
import com.desarrolloweb.NegocioApp.service.ValoracionProductoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ValoracionProductoServiceTest {
    @Mock ValoracionProductoRepository valoracionProductoRepository;
    @Mock UsuarioRepository usuarioRepository;
    @Mock ProductoRepository productoRepository;
    @InjectMocks ValoracionProductoService service;

    @Test
    void crearGuardaUsuarioReal() {
        Usuario usuario = new Usuario(); usuario.setId(3L);
        Producto producto = new Producto(); producto.setId(7L);
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(7L)).thenReturn(Optional.of(producto));
        when(valoracionProductoRepository.save(any(ValoracionProducto.class))).thenAnswer(inv -> {
            ValoracionProducto v = inv.getArgument(0);
            assertEquals(3L, v.getUsuarioId().getId());
            v.setId(11L);
            return v;
        });
        var res = service.crearValoracion(new ValoracionProductoRequestDTO(3L, 7L, 5, "Muy bueno"));
        assertEquals(3L, res.getUsuarioId());
        assertEquals(7L, res.getProductoId());
    }

    @Test
    void crearSinUsuarioEsRechazada() {
        assertThrows(IllegalArgumentException.class,
                () -> service.crearValoracion(new ValoracionProductoRequestDTO(7L, 5, "Bien")));
        verify(valoracionProductoRepository, never()).save(any());
    }

    @Test
    void listarUsaUsuarioRealSinFingirId() {
        Usuario u = new Usuario(); u.setId(8L);
        Producto p = new Producto(); p.setId(7L);
        ValoracionProducto v = new ValoracionProducto(5L, u, p, 4, "Bien", java.time.LocalDateTime.now());
        when(valoracionProductoRepository.findByProductoId_Id(7L)).thenReturn(List.of(v));
        assertEquals(8L, service.obtenerPorProducto(7L).get(0).getUsuarioId());
    }
}
