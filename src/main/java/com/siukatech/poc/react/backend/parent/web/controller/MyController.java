package com.siukatech.poc.react.backend.parent.web.controller;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@ProtectedApiV1Controller
public class MyController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected final UserService userService;
    public MyController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/my/public-key")
    public ResponseEntity<?> getPublicKey(Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("getPublicKey - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String userId = authentication.getName();
        UserDto userDto = this.userService.findByUserId(userId);

        String publicKeyBase64 = userDto.getPublicKey();
        return ResponseEntity.ok(publicKeyBase64);
    }

    @PostMapping("/my/user-info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("getUserInfo - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String userId = authentication.getName();
        UserDto userDto = this.userService.findByUserId(userId);

        return ResponseEntity.ok(userDto);
    }

}
