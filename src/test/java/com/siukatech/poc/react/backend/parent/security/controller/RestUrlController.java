package com.siukatech.poc.react.backend.parent.security.controller;

import com.siukatech.poc.react.backend.parent.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.parent.web.annotation.base.RestApiController;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
public class RestUrlController {

    @GetMapping(path = "/rest-url/authorized")
    @PermissionControl(appResourceId = "parent.restUrl.authorized", accessRight = "view")
    public String authorized() {
        return "authorized";
    }

    @GetMapping(path = "/rest-url/access_denied")
    @PermissionControl(appResourceId = "parent.restUrl.accessDenied", accessRight = "view")
    public String accessDenied() {
        return "authorized";
    }

}
