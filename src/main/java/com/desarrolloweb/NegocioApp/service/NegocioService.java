package com.desarrolloweb.NegocioApp.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.dto.NegocioRequestDto;
import com.desarrolloweb.NegocioApp.dto.NegocioResponseDto;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import java.util.ArrayList;
import java.util.List;
@Service
public class NegocioService {
    private final NegocioRepository negocioRepository;
    private final UsuarioRepository usuarioRepository;
    @Autowired
    public NegocioService(NegocioRepository negocioRepository, UsuarioRepository usuarioRepository){
        this.negocioRepository = negocioRepository;
        this.usuarioRepository = usuarioRepository;
    } 
    public NegocioResponseDto crear(NegocioRequestDto request){
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId()).orElseThrow(()->new RuntimeException("Usuario no encontrado"));
        Negocio negocio = new Negocio();
        negocio.setNombre(request.getNombre());
        negocio.setDescripcion(request.getDescripcion());
        negocio.setUsuario(usuario);
        Negocio guardada = negocioRepository.save(negocio);
        return new NegocioResponseDto(
            guardada.getId(),
            guardada.getUsuario().getId(),
            guardada.getNombre(),
            guardada.getDescripcion()

        );
    }
    public NegocioResponseDto buscarPorId(Long id){
        Negocio negocio = negocioRepository.findById(id).orElseThrow(()->new RuntimeException("Negocio no encontrado"));
        return new NegocioResponseDto(
            negocio.getId(),
            negocio.getUsuario().getId(),
            negocio.getNombre(),
            negocio.getDescripcion()
        );
    }
    public List<NegocioResponseDto> buscarPorNombre(String nombre) {
    List<Negocio> negocios = negocioRepository.findByNombreContainingIgnoreCase(nombre);
    List<NegocioResponseDto> resultado = new ArrayList<>();

    for (Negocio negocio : negocios) {
        resultado.add(new NegocioResponseDto(
                negocio.getId(),
                negocio.getUsuario().getId(),
                negocio.getNombre(),
                negocio.getDescripcion()
            ));
        }
    return resultado;
    }
    public List<NegocioResponseDto> buscarPorProducto(String productoNombre){
        List<Negocio> negocios =negocioRepository.findByProductoNombre(productoNombre);
        List<NegocioResponseDto> resultado = new ArrayList<>();
        for(Negocio negocio : negocios){
            resultado.add( new NegocioResponseDto(
                negocio.getId(),
                negocio.getUsuario().getId(),
                negocio.getNombre(),
                negocio.getDescripcion()
            ));
            }
            return resultado;
        }
    public NegocioResponseDto actualizar(Long id, NegocioRequestDto request){
        Negocio negocio = negocioRepository.findById(id).orElseThrow(()-> new RuntimeException("Negocio no encontrado"));
        negocio.setNombre(request.getNombre());
        negocio.setDescripcion(request.getDescripcion());
        Negocio actualizado = negocioRepository.save(negocio);

        return new NegocioResponseDto(
            actualizado.getId(),
            actualizado.getUsuario().getId(),
            actualizado.getNombre(),
            actualizado.getDescripcion()
        );
    }
    public void eliminar(Long id){
        Negocio negocio = negocioRepository.findById(id).orElseThrow(()->new RuntimeException("Negocio no encontrado"));
        negocioRepository.delete(negocio);
    }

     public List<NegocioResponseDto> buscarPorUsuario(Long usuarioId) {
    List<Negocio> negocios = negocioRepository.findByUsuarioId(usuarioId);
    List<NegocioResponseDto> resultado = new ArrayList<>();

    for (Negocio negocio : negocios) {
        resultado.add(new NegocioResponseDto(
                negocio.getId(),
                negocio.getUsuario().getId(),
                negocio.getNombre(),
                negocio.getDescripcion()
            ));
        }
    return resultado;
    }

    }

