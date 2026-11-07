package com.desarrolloweb.NegocioApp.service;
import com.desarrolloweb.NegocioApp.dto.detalleVenta.DetalleVentaRequestDTO;
import com.desarrolloweb.NegocioApp.dto.direccion.DireccionRequestDTO;
import com.desarrolloweb.NegocioApp.dto.negocio.NegocioRequestDTO;
import com.desarrolloweb.NegocioApp.dto.valoracionProducto.ValoracionProductoRequestDTO;
import com.desarrolloweb.NegocioApp.dto.venta.VentaRequestDTO;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.desarrolloweb.NegocioApp.dto.producto.ProductoRequestDTO;
import com.desarrolloweb.NegocioApp.entity.*;
import com.desarrolloweb.NegocioApp.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly=true)
public class AccessGuard {
  private final UsuarioRepository users;
  private final NegocioRepository business;
  private final VentaRepository sales;
  private final ProductoRepository products;
  private final DireccionRepository addresses;
  private final DetalleVentaRepository lines;
  private final ValoracionProductoRepository productRatings;
  private final ValoracionNegocioRepository businessRatings;
  public AccessGuard(UsuarioRepository u,NegocioRepository b,VentaRepository v,ProductoRepository p,
     DireccionRepository d,DetalleVentaRepository l,ValoracionProductoRepository pr,ValoracionNegocioRepository br) {
    users=u;business=b;sales=v;products=p;addresses=d;lines=l;productRatings=pr;businessRatings=br;
  }
  public Long uid() {
    Authentication auth=SecurityContextHolder.getContext().getAuthentication();
    if (auth==null || !auth.isAuthenticated()) throw forbidden();
    try { return Long.valueOf(auth.getName()); }
    catch (Exception e) { throw forbidden(); }
  }
  private Usuario active(){return users.findById(uid()).orElseThrow(this::forbidden);}
  private boolean has(String role){
    return active().getRoles().stream().anyMatch(r->role.equalsIgnoreCase(r.getNombre()) ||
      ("ADMIN".equals(role)&&"ADMINISTRADOR".equalsIgnoreCase(r.getNombre())));
  }
  private ResponseStatusException forbidden(){return new ResponseStatusException(HttpStatus.FORBIDDEN,"No autorizado");}
  private void check(boolean allowed){if(!allowed)throw forbidden();}
  public boolean admin(){return has("ADMIN");}
  public boolean merchant(){return has("NEGOCIO")||admin();}
  public boolean customer(){return has("CLIENTE")||admin();}
  public void adminOnly(){check(admin());}
  public void customerOnly(){check(customer());}
  public void selfOrAdmin(Long id){check(id!=null && (admin()||uid().equals(id)));}
  public void ownsBusiness(Long id){check(id!=null && business.findById(id).map(n -> n.estaAprobado() && (admin() || merchant() && n.getUsuario().getId().equals(uid()))).orElse(false));}
  // Los negocios nuevos solo se crean tras la aprobacion; clientes y comerciantes solicitan, no crean directamente.
  public void createsBusiness(NegocioRequestDTO req){adminOnly();check(req!=null && req.getUsuarioId()!=null);}
  public void updatesBusiness(Long id, NegocioRequestDTO req){ownsBusiness(id);check(req!=null && (req.getUsuarioId()==null || business.findById(id).map(x->x.getUsuario().getId().equals(req.getUsuarioId())).orElse(false)));}
  public void ownsProduct(Long id){check(id!=null && products.findById(id).map(p-> {try{ownsBusiness(p.getNegocio().getId());return true;}catch(ResponseStatusException e){return false;}}).orElse(false));}
  public void createsProduct(ProductoRequestDTO dto){check(dto!=null);ownsBusiness(dto.getNegocioId());}
  public void updatesProduct(Long id,ProductoRequestDTO dto){ownsProduct(id);if(dto!=null && dto.getNegocioId()!=null)ownsBusiness(dto.getNegocioId());}
  public void viewsSale(Long id){
    Venta v=sales.findById(id).orElseThrow(this::forbidden);
    if(admin() || customer() && v.getUsuario().getId().equals(uid()))return;
    ownsBusiness(v.getNegocio().getId());
  }
  public void ownsSaleBusiness(Long ventaId){
    if(ventaId==null)throw forbidden();
    Venta venta=sales.findById(ventaId).orElseThrow(this::forbidden);
    ownsBusiness(venta.getNegocio().getId());
  }
  public void saleOwner(Long id){
    Venta v=sales.findById(id).orElseThrow(this::forbidden);
    check(customer() && v.getUsuario().getId().equals(uid()) || admin());
  }
  public void createsSale(VentaRequestDTO dto){
    check(dto!=null && dto.getUsuarioId()!=null && dto.getDireccionId()!=null);
    selfOrAdmin(dto.getUsuarioId()); customerOnly();
    ownsAddressFor(dto.getDireccionId(),dto.getUsuarioId());
  }
  private void ownsAddressFor(Long addressId,Long userId){
    check(addresses.findById(addressId).map(d->userId.equals(d.getUsuarioId())).orElse(false));
  }
  public void readsAddress(Long id){
    check(id!=null && addresses.findById(id).map(d->admin()||uid().equals(d.getUsuarioId())).orElse(false));
  }
  public void createsAddress(DireccionRequestDTO dto){check(dto!=null);selfOrAdmin(dto.getUsuarioId());}
  public void updatesAddress(Long id,DireccionRequestDTO dto){readsAddress(id);check(dto!=null);check(addresses.findById(id).map(d->d.getUsuarioId().equals(dto.getUsuarioId())).orElse(false));}
  public void viewsDetail(Long id){ DetalleVenta d=lines.findById(id).orElseThrow(this::forbidden);viewsSale(d.getVenta().getId()); }
  public void managesDetail(Long id){ DetalleVenta d=lines.findById(id).orElseThrow(this::forbidden);saleOwner(d.getVenta().getId()); }
  public void createsDetail(DetalleVentaRequestDTO req){check(req!=null);saleOwner(req.getVentaId());}
  public void viewsProductDetails(Long id){ownsProduct(id);}
  public void createsProductRating(ValoracionProductoRequestDTO req){
    check(req != null && req.getUsuarioId() != null && req.getProductoId() != null);
    customerOnly();
    // La identidad proviene del JWT, no de un ID elegido por el cliente.
    check(uid().equals(req.getUsuarioId()));
    // Solo se puede valorar un producto de un pedido propio entregado.
    boolean entregado = sales.findByUsuarioId(uid()).stream()
      .anyMatch(v -> "ENTREGADO".equalsIgnoreCase(v.getEstado()) &&
        lines.existsByVentaIdAndProductoId(v.getId(), req.getProductoId()));
    check(entregado);
    // Una valoración por usuario y producto (independiente del número de compras).
    boolean repetida = productRatings.findByProductoId_Id(req.getProductoId()).stream()
      .anyMatch(r -> r.getUsuarioId() != null && uid().equals(r.getUsuarioId().getId()));
    if (repetida) throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya valoraste este producto");
  }
  public void deletesProductRating(Long id){
    check(id!=null && productRatings.findById(id).map(v->admin()||uid().equals(v.getUsuarioId().getId())).orElse(false));
  }
  public void createsBusinessRating(ValoracionNegocio req){
    check(req != null && req.getCliente() != null && req.getNegocio() != null);
    createsBusinessRating(req.getNegocio().getId(), req.getCliente().getId());
  }
  public void createsBusinessRating(Long negocioId, Long clienteId){
    check(negocioId != null && clienteId != null);customerOnly();
    check(uid().equals(clienteId));
    boolean entregado = sales.findByUsuarioId(uid()).stream()
      .anyMatch(v -> "ENTREGADO".equalsIgnoreCase(v.getEstado()) &&
        negocioId.equals(v.getNegocio().getId()));
    check(entregado);
    boolean repetida = businessRatings.findByNegocioId(negocioId).stream()
      .anyMatch(r -> r.getCliente() != null && uid().equals(r.getCliente().getId()));
    if(repetida)throw new ResponseStatusException(HttpStatus.CONFLICT,"Ya valoraste este negocio");
  }
  public void updatesBusinessRating(Long id){
    check(id!=null && businessRatings.findById(id).map(v->admin()||uid().equals(v.getCliente().getId())).orElse(false));
  }
  public Map<String,Object> perfil(){
    Usuario u=active();List<String> rs=u.getRoles().stream().map(Rol::getNombre).map(s->s.toUpperCase(Locale.ROOT)).toList();
    return Map.of("id",u.getId(),"nombre",u.getNombre(),"email",u.getEmail(),"roles",rs);
  }
}
