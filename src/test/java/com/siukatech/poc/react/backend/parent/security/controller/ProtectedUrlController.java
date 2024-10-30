package com.siukatech.poc.react.backend.parent.security.controller;

import com.siukatech.poc.react.backend.parent.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import org.springframework.web.bind.annotation.GetMapping;

@ProtectedApiV1Controller
public class ProtectedUrlController {

    @GetMapping(path = "/protected-url/authorized")
    @PermissionControl(appResourceId = "parent.protectedUrl.authorized", accessRight = "view")
    public String authorized() {
        return "authorized";
    }

    @GetMapping(path = "/protected-url/access_denied")
    @PermissionControl(appResourceId = "parent.protectedUrl.accessDenied", accessRight = "view")
    public String accessDenied() {
        return "authorized";
    }

}
