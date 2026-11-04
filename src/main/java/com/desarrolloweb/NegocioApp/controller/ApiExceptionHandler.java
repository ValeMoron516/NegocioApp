package com.desarrolloweb.NegocioApp.controller;

import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import com.desarrolloweb.NegocioApp.exception.BadRequestException;
import com.desarrolloweb.NegocioApp.exception.ConflictException;
import com.desarrolloweb.NegocioApp.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private ResponseEntity<Map<String,Object>> error(HttpStatusCode status, String message) {
        return ResponseEntity.status(status).body(Map.of(
            "status", status.value(), "error", message, "timestamp", Instant.now().toString()));
    }

    // Evita exponer trazas internas en respuestas HTTP 401/403/404, etc.
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,Object>> statusException(ResponseStatusException ex) {
        int codigo = ex.getStatusCode().value();
        String mensaje = switch (codigo) {
            case 400 -> "Datos de solicitud invalidos";
            case 401 -> "No autenticado";
            case 403 -> "Acceso denegado";
            case 404 -> "Recurso no encontrado";
            case 409 -> "Operacion en conflicto";
            default -> "Solicitud rechazada";
        };
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
            "status", codigo,
            "error", mensaje,
            "timestamp", Instant.now().toString()
        ));
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class,
                       HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
                       MissingServletRequestParameterException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String,Object>> invalid(Exception ex) {
        return error(HttpStatus.BAD_REQUEST, "Datos de solicitud invalidos");
    }

    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<Map<String,Object>> missing(Exception ex) {
        return error(HttpStatus.NOT_FOUND, "Recurso no encontrado");
    }

    @ExceptionHandler({ConflictException.class,
                       DataIntegrityViolationException.class})
    public ResponseEntity<Map<String,Object>> conflict(Exception ex) {
        return error(HttpStatus.CONFLICT, "Operacion en conflicto con el estado actual");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> denied(AccessDeniedException ex) {
        return error(HttpStatus.FORBIDDEN, "Acceso denegado");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String,Object>> unauthenticated(AuthenticationException ex) {
        return error(HttpStatus.UNAUTHORIZED, "No autenticado");
    }

    // Los errores de Spring MVC conservan su codigo (404, 405, 415, etc.).
    // No transformar los errores de cliente en 500 por accidente.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> unexpected(Exception ex) {
        if (ex instanceof ErrorResponse mvcError) {
            HttpStatusCode status = mvcError.getStatusCode();
            if (status.is4xxClientError()) {
                String mensaje = switch (status.value()) {
                    case 400 -> "Datos de solicitud invalidos";
                    case 401 -> "No autenticado";
                    case 403 -> "Acceso denegado";
                    case 404 -> "Recurso no encontrado";
                    case 405 -> "Metodo HTTP no permitido";
                    case 415 -> "Tipo de contenido no soportado";
                    default -> "Solicitud rechazada";
                };
                return error(status, mensaje);
            }
        }
        // Un fallo inesperado sigue siendo 500: log del servidor, sin traza en el JSON.
        log.error("Fallo interno no controlado en NegocioApp", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }
}
