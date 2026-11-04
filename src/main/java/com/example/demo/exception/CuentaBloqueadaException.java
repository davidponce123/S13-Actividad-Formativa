package com.example.demo.exception;

public class CuentaBloqueadaException extends RuntimeException {
    public CuentaBloqueadaException() {
        super("Cuenta bloqueada");
    }
    
}
