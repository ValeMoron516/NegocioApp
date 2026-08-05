package com.desarrolloweb.NegocioApp.exception;


public class ConflictException extends RuntimeException {

    public ConflictException(String mensaje) {
        super(mensaje);
    }
}