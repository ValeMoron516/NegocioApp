package com.desarrolloweb.NegocioApp.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;
import com.desarrolloweb.NegocioApp.dto.negocio.NegocioRequestDTO;
import com.desarrolloweb.NegocioApp.dto.negocio.NegocioResponseDTO;
import com.desarrolloweb.NegocioApp.dto.negocio.DatosTransferenciaNegocioDTO;
import com.desarrolloweb.NegocioApp.entity.Usuario;
import com.desarrolloweb.NegocioApp.entity.Negocio;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
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
    public NegocioResponseDTO crear(NegocioRequestDTO request){
        validar(request, true);
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId()).orElseThrow(()->new NotFoundException("Usuario no encontrado"));
        Negocio negocio = new Negocio();
        negocio.setNombre(request.getNombre().trim());
        negocio.setDescripcion(request.getDescripcion() == null ? null : request.getDescripcion().trim());
        negocio.setImagenUrl(normalizar(request.getImagenUrl()));
        negocio.setAliasBancario(normalizar(request.getAliasBancario()));
        negocio.setTitularCuenta(normalizar(request.getTitularCuenta()));
        negocio.setWhatsappComercial(normalizar(request.getWhatsappComercial()));
        negocio.setUsuario(usuario);
        negocio.setEstadoAprobacion("APROBADO"); // Alta directa exclusiva de Administrador
        Negocio guardada = negocioRepository.save(negocio);
        return respuesta(guardada);
    }
    public NegocioResponseDTO buscarPorId(Long id){
        Negocio negocio = negocioRepository.findById(id).orElseThrow(()->new NotFoundException("Negocio no encontrado"));
        if(!negocio.estaAprobado())throw new NotFoundException("Negocio no disponible");
        return respuestaPublica(negocio);
    }
    public List<NegocioResponseDTO> buscarPorNombre(String nombre) {
    List<Negocio> negocios = negocioRepository.findByNombreContainingIgnoreCase(nombre).stream().filter(Negocio::estaAprobado).toList();
    List<NegocioResponseDTO> resultado = new ArrayList<>();

    for (Negocio negocio : negocios) {
        resultado.add(respuestaPublica(negocio));
        }
    return resultado;
    }
    public List<NegocioResponseDTO> buscarPorProducto(String productoNombre){
        List<Negocio> negocios =negocioRepository.findByProductoNombre(productoNombre).stream().filter(Negocio::estaAprobado).toList();
        List<NegocioResponseDTO> resultado = new ArrayList<>();
        for(Negocio negocio : negocios){
            resultado.add(respuestaPublica(negocio));
            }
            return resultado;
        }
    public NegocioResponseDTO actualizar(Long id, NegocioRequestDTO request){
        validar(request, false);
        Negocio negocio = negocioRepository.findById(id).orElseThrow(()-> new NotFoundException("Negocio no encontrado"));
        if(!negocio.estaAprobado())throw new NotFoundException("Negocio no habilitado");
        if (request.getNombre() != null) negocio.setNombre(request.getNombre().trim());
        if (request.getDescripcion() != null) negocio.setDescripcion(request.getDescripcion().trim());
        if (request.getImagenUrl() != null) negocio.setImagenUrl(normalizar(request.getImagenUrl()));
        if (request.getAliasBancario() != null) negocio.setAliasBancario(normalizar(request.getAliasBancario()));
        if (request.getTitularCuenta() != null) negocio.setTitularCuenta(normalizar(request.getTitularCuenta()));
        if (request.getWhatsappComercial() != null) negocio.setWhatsappComercial(normalizar(request.getWhatsappComercial()));
        Negocio actualizado = negocioRepository.save(negocio);
        return respuesta(actualizado);
    }
    public void eliminar(Long id){
        Negocio negocio = negocioRepository.findById(id).orElseThrow(()->new NotFoundException("Negocio no encontrado"));
        negocioRepository.delete(negocio);
    }

        public DatosTransferenciaNegocioDTO datosTransferencia(Long negocioId) {
        Negocio negocio = negocioRepository.findById(negocioId)
            .orElseThrow(() -> new NotFoundException("Negocio no encontrado"));
        if (!negocio.estaAprobado()) throw new NotFoundException("Negocio no disponible");
        return new DatosTransferenciaNegocioDTO(
            negocio.getAliasBancario(),
            negocio.getTitularCuenta(),
            negocio.getWhatsappComercial());
        }

     public List<NegocioResponseDTO> buscarPorUsuario(Long usuarioId) {
    List<Negocio> negocios = negocioRepository.findByUsuarioId(usuarioId).stream().filter(Negocio::estaAprobado).toList();
    List<NegocioResponseDTO> resultado = new ArrayList<>();

    for (Negocio negocio : negocios) {
        resultado.add(respuesta(negocio));
        }
    return resultado;
    }

        private void validar(NegocioRequestDTO request, boolean crear) {
        if (request == null) throw new BadRequestException("Datos del negocio obligatorios");
        if (crear && request.getUsuarioId() == null)
            throw new BadRequestException("Usuario propietario obligatorio");
        if ((crear && request.getNombre() == null)
                || (request.getNombre() != null && request.getNombre().isBlank()))
            throw new BadRequestException("Nombre de negocio obligatorio");
        if (request.getNombre() != null && request.getNombre().trim().length() > 150)
            throw new BadRequestException("Nombre de negocio demasiado largo");
        validarLongitud(request.getImagenUrl(), 500, "URL de imagen");
        validarLongitud(request.getAliasBancario(), 100, "Alias bancario");
        validarLongitud(request.getTitularCuenta(), 150, "Titular de la cuenta");
        validarLongitud(request.getWhatsappComercial(), 30, "WhatsApp comercial");
    }

    private void validarLongitud(String valor, int maximo, String campo) {
        if (valor != null && valor.trim().length() > maximo)
            throw new BadRequestException(campo + " demasiado largo");
    }

    private String normalizar(String valor) {
        if (valor == null) return null;
        String recortado = valor.trim();
        return recortado.isEmpty() ? null : recortado;
    }

    private NegocioResponseDTO respuesta(Negocio negocio) {
        return new NegocioResponseDTO(
                negocio.getId(),
                negocio.getUsuario().getId(),
                negocio.getNombre(),
                negocio.getDescripcion(),
                negocio.getImagenUrl(),
                negocio.getAliasBancario(),
                negocio.getTitularCuenta(),
                negocio.getWhatsappComercial());
    }

    private NegocioResponseDTO respuestaPublica(Negocio negocio) {
        return new NegocioResponseDTO(
                negocio.getId(),
                negocio.getUsuario().getId(),
                negocio.getNombre(),
                negocio.getDescripcion(),
                negocio.getImagenUrl());
    }
}

