package com.siukatech.poc.react.backend.parent.web.controller;

import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@ProtectedApiV1Controller
public class UserController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/{targetLoginId}/user-info")
    public ResponseEntity<?> getUserInfo(@PathVariable String targetLoginId) {
        UserDto userDto = this.userService.findByLoginId(targetLoginId);

        return ResponseEntity.ok(userDto);
    }

}
