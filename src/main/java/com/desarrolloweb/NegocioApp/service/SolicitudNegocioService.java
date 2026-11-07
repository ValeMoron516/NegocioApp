package com.desarrolloweb.NegocioApp.service;

import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Rol;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.repository.RolRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SolicitudNegocioService {
    private final NegocioRepository negocios;
    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final AccessGuard guard;
    public SolicitudNegocioService(NegocioRepository negocios, UsuarioRepository usuarios,
            RolRepository roles, AccessGuard guard) {
        this.negocios=negocios;this.usuarios=usuarios;this.roles=roles;this.guard=guard;
    }
    public record NuevaSolicitud(String nombre, String descripcion){}
    public record Rechazo(String motivo){}
    // Se mantiene el contrato con el frontend existente, sin tabla de solicitudes.
    public record SolicitudVista(Long id,Long solicitanteId,String nombre,String descripcion,
        String estado,LocalDateTime fechaSolicitud,LocalDateTime fechaDecision,
        Long revisadaPorId,String motivoRechazo,Long negocioAprobadoId){}

    private ResponseStatusException error(HttpStatus code,String msg){return new ResponseStatusException(code,msg);}
    private String estadoVista(Negocio n) {
        return switch(n.getEstadoAprobacion()) {
            case "APROBADO" -> "APROBADA";
            case "RECHAZADO" -> "RECHAZADA";
            default -> "PENDIENTE";
        };
    }
    private SolicitudVista vista(Negocio n) {
        return new SolicitudVista(n.getId(),n.getUsuario().getId(),n.getNombre(),n.getDescripcion(),
          estadoVista(n),n.getFechaSolicitud(),n.getFechaDecision(),n.getRevisadaPorId(),
          n.getMotivoRechazo(),n.estaAprobado()?n.getId():null);
    }
    private List<SolicitudVista> vistas(List<Negocio> datos){return datos.stream().map(this::vista).toList();}

    @Transactional
    public SolicitudVista solicitar(NuevaSolicitud request){
        if(!guard.customer() && !guard.merchant())throw error(HttpStatus.FORBIDDEN,"Cuenta no autorizada");
        if(request==null || request.nombre()==null || request.nombre().isBlank())
            throw error(HttpStatus.BAD_REQUEST,"El nombre del negocio es obligatorio");
        String nombre=request.nombre().trim();
        String descripcion=request.descripcion()==null?"":request.descripcion().trim();
        if(nombre.length()<2 || nombre.length()>150 || descripcion.length()>2000)
            throw error(HttpStatus.BAD_REQUEST,"Nombre o descripcion invalidos");
        Long uid=guard.uid();
        // Los rechazados no bloquean una nueva solicitud corregida.
        if(negocios.findByUsuarioId(uid).stream().anyMatch(n ->
                n.getNombre().equalsIgnoreCase(nombre) && !"RECHAZADO".equals(n.getEstadoAprobacion())))
            throw error(HttpStatus.CONFLICT,"Ya tenes un comercio o solicitud pendiente con ese nombre");
        Usuario propietario=usuarios.findById(uid).orElseThrow(()->error(HttpStatus.FORBIDDEN,"Cuenta inexistente"));
        Negocio n=new Negocio();
        n.setUsuario(propietario);n.setNombre(nombre);n.setDescripcion(descripcion);
        n.setEstadoAprobacion("PENDIENTE");n.setFechaSolicitud(LocalDateTime.now());
        return vista(negocios.save(n));
    }
    @Transactional(readOnly=true)
    public List<SolicitudVista> mias(){return vistas(negocios.solicitudesDelUsuario(guard.uid()));}
    @Transactional(readOnly=true)
    public List<SolicitudVista> listar(String estado){
        guard.adminOnly();
        if(estado==null || estado.isBlank())return vistas(negocios.solicitudesOrdenadas());
        String filtro=estado.trim().toUpperCase(Locale.ROOT);
        if(!Set.of("PENDIENTE","APROBADA","RECHAZADA").contains(filtro))
            throw error(HttpStatus.BAD_REQUEST,"Estado desconocido");
        String interno=switch(filtro) {case "APROBADA" -> "APROBADO";case "RECHAZADA" -> "RECHAZADO";default -> "PENDIENTE";};
        return vistas(negocios.solicitudesPorEstado(interno));
    }
    @Transactional
    public SolicitudVista aprobar(Long id){
        guard.adminOnly();
        Negocio n=negocios.solicitudBloqueada(id).orElseThrow(()->error(HttpStatus.NOT_FOUND,"Solicitud inexistente"));
        if(!"PENDIENTE".equals(n.getEstadoAprobacion()))throw error(HttpStatus.CONFLICT,"La solicitud ya fue resuelta");
        Usuario propietario=usuarios.findById(n.getUsuario().getId()).orElseThrow();
        Rol cliente=roles.findByNombre("CLIENTE").orElseGet(()->roles.save(new Rol(null,"CLIENTE")));
        Rol comerciante=roles.findByNombre("NEGOCIO").orElseGet(()->roles.save(new Rol(null,"NEGOCIO")));
        propietario.getRoles().add(cliente);propietario.getRoles().add(comerciante);
        usuarios.save(propietario);
        n.setEstadoAprobacion("APROBADO");n.setFechaDecision(LocalDateTime.now());
        n.setRevisadaPorId(guard.uid());
        return vista(negocios.save(n));
    }
    @Transactional
    public SolicitudVista rechazar(Long id,Rechazo body){
        guard.adminOnly();
        Negocio n=negocios.solicitudBloqueada(id).orElseThrow(()->error(HttpStatus.NOT_FOUND,"Solicitud inexistente"));
        if(!"PENDIENTE".equals(n.getEstadoAprobacion()))throw error(HttpStatus.CONFLICT,"La solicitud ya fue resuelta");
        String motivo=body==null || body.motivo()==null?"":body.motivo().trim();
        if(motivo.isBlank() || motivo.length()>500)
            throw error(HttpStatus.BAD_REQUEST,"Indica un motivo de rechazo (maximo 500 caracteres)");
        n.setMotivoRechazo(motivo);n.setEstadoAprobacion("RECHAZADO");
        n.setFechaDecision(LocalDateTime.now());n.setRevisadaPorId(guard.uid());
        return vista(negocios.save(n));
    }
}
