package com.siukatech.poc.react.backend.parent.security.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionControl {
    String appResourceId();
    String accessRight();

}
