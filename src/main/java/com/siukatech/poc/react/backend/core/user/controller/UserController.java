package com.siukatech.poc.react.backend.core.user.controller;

import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.core.user.service.UserService;
import com.siukatech.poc.react.backend.core.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@ProtectedApiV1Controller
public class UserController {

    protected final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/{targetLoginId}/user-info")
    @PermissionControl(appResourceId = "core.user.getUserInfo", accessRight = "view")
    public ResponseEntity getUserInfo(@PathVariable
//                                                 // after upgrade to springboot >= 3.2.1
//                                                 // this can be fixed by update build.gradle or adding maven plugin
//                                                 // https://stackoverflow.com/a/77691302
//                                                 // https://github.com/spring-projects/spring-framework/wiki/Upgrading-to-Spring-Framework-6.x#parameter-name-retention
//                                                 (name = "targetLoginId")
                                         String targetLoginId) {
        UserDto userDto = this.userService.findByLoginId(targetLoginId);

        return ResponseEntity.ok(userDto);
    }

}
