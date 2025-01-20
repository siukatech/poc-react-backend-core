package com.siukatech.poc.react.backend.core.security.exception;

public class PermissionControlNotFoundException extends RuntimeException {
    public PermissionControlNotFoundException(String msg) {
        super(msg);
    }
}
