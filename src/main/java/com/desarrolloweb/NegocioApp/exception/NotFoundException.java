package com.desarrolloweb.NegocioApp.exception;


public class NotFoundException extends RuntimeException {

    public NotFoundException(String mensaje) { super(mensaje); }
    public NotFoundException() {}
}