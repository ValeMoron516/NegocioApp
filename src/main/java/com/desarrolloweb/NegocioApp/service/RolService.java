package com.desarrolloweb.NegocioApp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.MetaDTO;
import com.desarrolloweb.NegocioApp.dtos.paginacionDTO.PaginacionDTO;
import com.desarrolloweb.NegocioApp.dtos.rolDTO.RolRequestDTO;
import com.desarrolloweb.NegocioApp.dtos.rolDTO.RolResponseDTO;
import com.desarrolloweb.NegocioApp.entity.Rol;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import com.desarrolloweb.NegocioApp.repository.RolRepository;
import com.desarrolloweb.NegocioApp.repository.UsuarioRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public RolService(RolRepository rolRepository, UsuarioRepository usuarioRepository) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ##################################################

    // Obtener lista de roles (paginados)
    public PaginacionDTO<RolResponseDTO> obtenerTodosRoles(Integer page, Integer limit) {

        if (page == null || page < 1 || limit == null || limit < 1) {
            throw new BadRequestException("Parametros de paginacion invalidos");
        }

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Rol> paginaRoles = rolRepository.findAll(pageable);

        // Mapear a DTO
        List<RolResponseDTO> dtos = new ArrayList<>();
        for (Rol r : paginaRoles.getContent()) {
            dtos.add(aDTO(r));
        }

        MetaDTO meta = new MetaDTO(
            paginaRoles.getTotalElements(),     // totalItems   (elementos totales)
            paginaRoles.getNumberOfElements(),  // itemCount    (elementos en la página actual)
            paginaRoles.getSize(),              // itemsPerPage (elementos maximos por pagina)
            paginaRoles.getTotalPages(),        // totalPages   (total de paginas)
            paginaRoles.getNumber() + 1         // currentPage  (pagina actual)
        );

        return new PaginacionDTO<>(dtos, meta);
    }

    // ##################################################

    // Obtener Rol por ID
    public RolResponseDTO obtenerRolPorId(Long id) {
        Rol r = rolRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("El rol solicitado no existe"));
        return aDTO(r);
    }

    // ##################################################

    // Crear nuevo Rol
    public RolResponseDTO crearRol(RolRequestDTO rDTO) {

        String nombre = normalizarNombre(rDTO.getNombre());

        if (rolRepository.existsByNombre(nombre)) {
            throw new ConflictException("Ya existe un rol con el nombre provisto");
        }

        Rol newR = new Rol();
        newR.setNombre(nombre);

        return aDTO(rolRepository.save(newR));
    }

    // ##################################################

    // Actualizar Rol por ID
    public RolResponseDTO actualizarRolPorId(Long id, RolRequestDTO rDTO) {

        Rol r = rolRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("El rol con el ID provisto no existe"));

        // Solo se actualiza el nombre si fue enviado
        if (rDTO.getNombre() != null) {
            String nombre = normalizarNombre(rDTO.getNombre());

            // Conflicto solo si el nombre pertenece a OTRO rol
            if (!nombre.equals(r.getNombre()) && rolRepository.existsByNombre(nombre)) {
                throw new ConflictException("El nombre provisto ya existe");
            }
            r.setNombre(nombre);
        }

        return aDTO(rolRepository.save(r));
    }

    // ##################################################

    // Borrar Rol por ID
    public void borrarRolPorId(Long id) {

        if (!rolRepository.existsById(id)) {
            throw new NotFoundException("El rol con el ID provisto no existe");
        }

        // Buscar usuarios asociados al rol
        if (usuarioRepository.existsByRolId(id)) {
            throw new ConflictException("El rol provisto no puede eliminarse debido a que tiene usuarios asociados");
        }

        rolRepository.deleteById(id);
    }

    // ##################################################

    // Valida el nombre y lo guarda siempre en MAYUSCULAS (evita duplicados tipo "cliente" / "CLIENTE")
    private String normalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new BadRequestException("Nombre invalido");
        }
        String normalizado = nombre.trim().toUpperCase(Locale.ROOT);
        if (normalizado.length() > 50) {
            throw new BadRequestException("Nombre invalido: maximo 50 caracteres");
        }
        return normalizado;
    }

    private RolResponseDTO aDTO(Rol r) {
        return new RolResponseDTO(r.getId(), r.getNombre());
    }
}
