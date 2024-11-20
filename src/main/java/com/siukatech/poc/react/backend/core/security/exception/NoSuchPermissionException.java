package com.siukatech.poc.react.backend.core.security.exception;

public class NoSuchPermissionException extends RuntimeException {
    public NoSuchPermissionException(String msg) {
        super(msg);
    }
}
