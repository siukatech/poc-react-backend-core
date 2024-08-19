package com.siukatech.poc.react.backend.parent.security.exception;

public class NoSuchPermissionException extends RuntimeException {
    public NoSuchPermissionException(String msg) {
        super(msg);
    }
}
