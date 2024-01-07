package com.siukatech.poc.react.backend.parent.web.controller;

import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@ProtectedApiV1Controller
public class MyController {

    protected final UserService userService;
    public MyController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/my/public-key")
    public ResponseEntity<?> getPublicKey(Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        log.debug("getPublicKey - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        MyKeyDto myKeyDto = this.userService.findKeyByLoginId(loginId);

        String publicKeyBase64 = myKeyDto.getPublicKey();
        return ResponseEntity.ok(publicKeyBase64);
    }

    @PostMapping("/my/key-info")
    public ResponseEntity<?> getKeyInfo(Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        log.debug("getKeyInfo - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        MyKeyDto myKeyDto = this.userService.findKeyByLoginId(loginId);

        return ResponseEntity.ok(myKeyDto);
    }

    @PostMapping("/my/user-info")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        log.debug("getUserInfo - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        UserDto userDto = this.userService.findByLoginId(loginId);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/my/permissions")
    public ResponseEntity<?> getUserPermissions(Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        log.debug("getUserPermissions - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        List<UserPermissionDto> userPermissionDtoList = this.userService
                .findPermissionsByLoginId(loginId);

        return ResponseEntity.ok(userPermissionDtoList);
    }

}
