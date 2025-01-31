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

    @GetMapping("/my/permissions")
    @PermissionControl(appResourceId = "core.my.getUserPermissions", accessRight = "view")
    public ResponseEntity getUserPermissions(@RequestHeader HttpHeaders httpHeaders
            , @RequestParam(required = true) String applicationId
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserPermissions - authentication: [{}], authenticationInSc: [{}], applicationId: [{}]"
                , authentication, authenticationInSc, applicationId);
        String userId = authentication.getName();
        List<UserPermissionDto> userPermissionDtoList = this.userService
                .findPermissionsByUserIdAndApplicationId(userId, applicationId);

        return ResponseEntity.ok(userPermissionDtoList);
    }

    @GetMapping("/my/permission-info")
    @PermissionControl(appResourceId = "core.my.getUserPermissionInfo", accessRight = "view")
    public ResponseEntity getUserPermissionInfo(@RequestHeader HttpHeaders httpHeaders
            , @RequestParam(required = true) String applicationId
            , Authentication authentication) {
        Authentication authenticationInSc = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaderUtils.logHttpHeaders(httpHeaders);
        log.debug("getUserPermissions - authentication: [{}], authenticationInSc: [{}], applicationId: [{}]"
                , authentication, authenticationInSc, applicationId);
        String userId = authentication.getName();
        List<UserPermissionDto> userPermissionDtoList = this.userService
                .findPermissionsByUserIdAndApplicationId(userId, applicationId);
//        UserPermissionInfoDto userPermissionInfoDto = UserPermissionInfoDto.builder()
//                .userId(userId)
//                .userPermissionList(userPermissionDtoList)
//                .build();
        UserPermissionInfoDto userPermissionInfoDto = new UserPermissionInfoDto();
        userPermissionInfoDto.setUserId(userId);
        userPermissionInfoDto.setUserPermissionList(userPermissionDtoList);

        return ResponseEntity.ok(userPermissionInfoDto);
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
