package com.siukatech.poc.react.backend.parent.web.controller;

import com.siukatech.poc.react.backend.parent.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserViewDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.util.HttpHeaderUtils;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Slf4j
@ProtectedApiV1Controller
public class MyController {

    protected final UserService userService;
    public MyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my/public-key")
    public ResponseEntity<?> getPublicKey(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getPublicKey - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        MyKeyDto myKeyDto = this.userService.findKeyByLoginId(loginId);

        String publicKeyBase64 = myKeyDto.getPublicKey();
        return ResponseEntity.ok(publicKeyBase64);
    }

    @GetMapping("/my/key-info")
    public ResponseEntity<?> getKeyInfo(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getKeyInfo - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        MyKeyDto myKeyDto = this.userService.findKeyByLoginId(loginId);

        return ResponseEntity.ok(myKeyDto);
    }

    @GetMapping("/my/user-info")
    public ResponseEntity<?> getUserInfo(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserInfo - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        UserDto userDto = this.userService.findByLoginId(loginId);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/my/permissions")
    public ResponseEntity<?> getUserPermissions(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserPermissions - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        List<UserPermissionDto> userPermissionDtoList = this.userService
                .findPermissionsByLoginId(loginId);

        return ResponseEntity.ok(userPermissionDtoList);
    }

    @GetMapping("/my/user-view")
    public ResponseEntity<?> getUserView(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserView - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String loginId = authentication.getName();
        UserViewDto userViewDto = this.userService.findViewByLoginId(loginId);

        return ResponseEntity.ok(userViewDto);
    }

}
