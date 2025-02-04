package com.siukatech.poc.react.backend.core.user.controller;

import com.siukatech.poc.react.backend.core.business.dto.*;
import com.siukatech.poc.react.backend.core.security.annotation.PermissionControl;
import com.siukatech.poc.react.backend.core.user.service.UserService;
import com.siukatech.poc.react.backend.core.util.HttpHeaderUtils;
import com.siukatech.poc.react.backend.core.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@ProtectedApiV1Controller
public class MyController {

    protected final UserService userService;
    public MyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my/public-key")
    @PermissionControl(appResourceId = "core.my.getPublicKey", accessRight = "view")
    public ResponseEntity getPublicKey(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getPublicKey - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String userId = authentication.getName();
        MyKeyDto myKeyDto = this.userService.findKeyByUserId(userId);

        String publicKeyBase64 = myKeyDto.getPublicKey();
        return ResponseEntity.ok(publicKeyBase64);
    }

    @GetMapping("/my/key-info")
    @PermissionControl(appResourceId = "core.my.getKeyInfo", accessRight = "view")
    public ResponseEntity getKeyInfo(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getKeyInfo - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String userId = authentication.getName();
        MyKeyDto myKeyDto = this.userService.findKeyByUserId(userId);

        return ResponseEntity.ok(myKeyDto);
    }

    @GetMapping("/my/user-info")
    @PermissionControl(appResourceId = "core.my.getUserInfo", accessRight = "view")
    public ResponseEntity getUserInfo(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserInfo - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String userId = authentication.getName();
        UserDto userDto = this.userService.findUserByUserId(userId);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/my/permission-info")
    @PermissionControl(appResourceId = "core.my.getPermissionInfo", accessRight = "view")
    public ResponseEntity getPermissionInfo(@RequestHeader HttpHeaders httpHeaders
            , @RequestParam(required = true) String applicationId
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getPermissionInfo - authentication: [{}], authenticationInSc: [{}], applicationId: [{}]"
                , authentication, authenticationInSc, applicationId);
        String userId = authentication.getName();
        List<UserPermissionDto> userPermissionDtoList = this.userService
                .findPermissionsByUserIdAndApplicationId(userId, applicationId);
        MyPermissionDto myPermissionDto = new MyPermissionDto(userId, userPermissionDtoList);

        return ResponseEntity.ok(myPermissionDto);
    }

    @GetMapping("/my/user-dossier")
    @PermissionControl(appResourceId = "core.my.getUserDossier", accessRight = "view")
    public ResponseEntity getUserDossier(@RequestHeader HttpHeaders httpHeaders
            , @RequestParam(required = true) String applicationId
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserDossier - authentication: [{}], authenticationInSc: [{}], applicationId: [{}]"
                , authentication, authenticationInSc, applicationId);
        String userId = authentication.getName();
        UserDossierDto userDossierDto = this.userService
                .findUserDossierByUserIdAndApplicationId(userId, applicationId);

        return ResponseEntity.ok(userDossierDto);
    }

    @GetMapping("/my/user-view")
    @PermissionControl(appResourceId = "core.my.getUserView", accessRight = "view")
    public ResponseEntity getUserView(@RequestHeader HttpHeaders httpHeaders
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserView - authentication: [{}], authenticationInSc: [{}]"
                , authentication, authenticationInSc);
        String userId = authentication.getName();
        UserViewDto userViewDto = this.userService.findViewByUserId(userId);

        return ResponseEntity.ok(userViewDto);
    }

}
