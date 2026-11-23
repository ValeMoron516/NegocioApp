package com.desarrolloweb.NegocioApp;

import com.desarrolloweb.NegocioApp.dto.valoracionProducto.ValoracionProductoRequestDTO;
import com.desarrolloweb.NegocioApp.entity.*;
import com.desarrolloweb.NegocioApp.repository.*;
import com.desarrolloweb.NegocioApp.service.AccessGuard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CierrePermisosYValoracionesTest {
  @Mock UsuarioRepository users;
  @Mock NegocioRepository businesses;
  @Mock VentaRepository sales;
  @Mock ProductoRepository products;
  @Mock DireccionRepository addresses;
  @Mock DetalleVentaRepository lines;
  @Mock ValoracionProductoRepository productRatings;
  @Mock ValoracionNegocioRepository businessRatings;
  AccessGuard guard;
  Usuario cliente;

  @BeforeEach void init() {
    guard = new AccessGuard(users, businesses, sales, products, addresses, lines, productRatings, businessRatings);
    cliente = new Usuario(); cliente.setId(90001L); cliente.setRoles(Set.of(new Rol(1L,"CLIENTE")));
    when(users.findById(90001L)).thenReturn(Optional.of(cliente));
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("90001", "", List.of()));
  }
  @AfterEach void cleanup() {SecurityContextHolder.clearContext();}

  @Test void clienteNoPuedeAdministrar() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, guard::adminOnly);
    assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
  }
  @Test void administradorPuedeAdministrar() {
    cliente.setRoles(Set.of(new Rol(2L,"ADMINISTRADOR")));
    assertDoesNotThrow(guard::adminOnly);
  }
  @Test void clienteNoPuedeFingirSerOtroUsuarioAlValorar() {
    ValoracionProductoRequestDTO dto = new ValoracionProductoRequestDTO();
    dto.setUsuarioId(90002L); dto.setProductoId(44L);
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, ()->guard.createsProductRating(dto));
    assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
  }
  @Test void noPuedeValorarProductoConVentaPendiente() {
    Venta venta = venta(80L,"PENDIENTE",77L);
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta));
    ValoracionProductoRequestDTO dto = new ValoracionProductoRequestDTO();
    dto.setUsuarioId(90001L); dto.setProductoId(44L);
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, ()->guard.createsProductRating(dto));
    assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
  }
  @Test void puedeValorarProductoTrasEntrega() {
    Venta venta = venta(80L,"ENTREGADO",77L);
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta));
    when(lines.existsByVentaIdAndProductoId(80L,44L)).thenReturn(true);
    productoDeOtroComerciante();
    when(productRatings.findByProductoId_Id(44L)).thenReturn(List.of());
    ValoracionProductoRequestDTO dto = new ValoracionProductoRequestDTO();
    dto.setUsuarioId(90001L); dto.setProductoId(44L);
    assertDoesNotThrow(()->guard.createsProductRating(dto));
  }
  @Test void noPuedeValorarDosVecesElProducto() {
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta(80L,"ENTREGADO",77L)));
    when(lines.existsByVentaIdAndProductoId(80L,44L)).thenReturn(true);
    productoDeOtroComerciante();
    ValoracionProducto previa = new ValoracionProducto(); previa.setUsuarioId(cliente);
    when(productRatings.findByProductoId_Id(44L)).thenReturn(List.of(previa));
    ValoracionProductoRequestDTO dto = new ValoracionProductoRequestDTO();
    dto.setUsuarioId(90001L); dto.setProductoId(44L);
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, ()->guard.createsProductRating(dto));
    assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
  }
  @Test void noPuedeValorarNegocioSinEntrega() {
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta(80L,"PENDIENTE",77L)));
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        ()->guard.createsBusinessRating(77L,90001L));
    assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
  }
  @Test void puedeValorarNegocioTrasEntrega() {
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta(80L,"ENTREGADO",77L)));
    negocioDeOtroComerciante();
    when(businessRatings.findByNegocioId(77L)).thenReturn(List.of());
    assertDoesNotThrow(()->guard.createsBusinessRating(77L,90001L));
  }
  @Test void noPuedeValorarDosVecesElNegocio() {
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta(80L,"ENTREGADO",77L)));
    negocioDeOtroComerciante();
    ValoracionNegocio previa = new ValoracionNegocio(); previa.setCliente(cliente);
    when(businessRatings.findByNegocioId(77L)).thenReturn(List.of(previa));
    ResponseStatusException ex = assertThrows(ResponseStatusException.class,
        ()->guard.createsBusinessRating(77L,90001L));
    assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
  }
  @Test void noPuedeValorarSuPropioProducto() {
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta(80L,"ENTREGADO",77L)));
    when(lines.existsByVentaIdAndProductoId(80L,44L)).thenReturn(true);
    Producto p = new Producto(); Negocio n = new Negocio(); n.setUsuario(cliente); p.setNegocio(n);
    when(products.findById(44L)).thenReturn(Optional.of(p));
    ValoracionProductoRequestDTO dto = new ValoracionProductoRequestDTO();
    dto.setUsuarioId(90001L); dto.setProductoId(44L);
    assertEquals(HttpStatus.FORBIDDEN, assertThrows(ResponseStatusException.class,
        () -> guard.createsProductRating(dto)).getStatusCode());
  }
  @Test void noPuedeValorarSuPropioNegocio() {
    when(sales.findByUsuarioId(90001L)).thenReturn(List.of(venta(80L,"ENTREGADO",77L)));
    Negocio n = new Negocio(); n.setUsuario(cliente);
    when(businesses.findById(77L)).thenReturn(Optional.of(n));
    assertEquals(HttpStatus.FORBIDDEN, assertThrows(ResponseStatusException.class,
        () -> guard.createsBusinessRating(77L, 90001L)).getStatusCode());
  }
  private void negocioDeOtroComerciante() {
    Usuario dueno = new Usuario(); dueno.setId(91000L);
    Negocio n = new Negocio(); n.setUsuario(dueno);
    when(businesses.findById(77L)).thenReturn(Optional.of(n));
  }
  private void productoDeOtroComerciante() {
    Usuario dueno = new Usuario(); dueno.setId(91000L);
    Negocio n = new Negocio(); n.setUsuario(dueno);
    Producto p = new Producto(); p.setNegocio(n);
    when(products.findById(44L)).thenReturn(Optional.of(p));
  }
  private Venta venta(Long id, String estado, Long negocioId) {
    Venta v = new Venta(); v.setId(id); v.setEstado(estado);
    Negocio n = new Negocio(); n.setId(negocioId); v.setNegocio(n);
    return v;
  }
}
