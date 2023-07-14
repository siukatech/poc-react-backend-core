package com.siukatech.poc.react.backend.parent.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.parent.business.service.AuthService;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.PublicApiV1Controller;
import com.siukatech.poc.react.backend.parent.web.model.TokenReq;
import com.siukatech.poc.react.backend.parent.web.model.TokenRes;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
@PublicApiV1Controller
public class AuthController {

//    @Value("${security.oauth2.client.registration.keycloak.client-id}")
//    private String clientId;
//    @Value("${security.oauth2.client.registration.keycloak.client-secret}")
//    private String clientSceret;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final OAuth2ClientProperties oAuth2ClientProperties;
//    private final RestTemplate oauth2ClientRestTemplate;
//    private final ObjectMapper objectMapper;
    private final AuthService authService;

    public AuthController(
//            OAuth2ClientProperties oAuth2ClientProperties
//            , RestTemplate oauth2ClientRestTemplate
//            , ObjectMapper objectMapper
//            ,
            AuthService authService) {
//        this.oAuth2ClientProperties = oAuth2ClientProperties;
//        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
//        this.objectMapper = objectMapper;
        this.authService = authService;
    }


    @GetMapping(value = "/auth/login/{clientName}")
    public void auth(HttpServletResponse response, @PathVariable String clientName) {
        String authUrl = this.authService.getAuthUrl(clientName);
        logger.debug("auth - clientName: [{}], authUrl: [{}]", clientName, authUrl);
        response.setHeader(HttpHeaders.LOCATION, authUrl);
        response.setStatus(HttpStatus.FOUND.value());
    }

    @PostMapping(value = "/auth/token/{clientName}/{code}")
    public ResponseEntity<?> token(@PathVariable String clientName, @PathVariable String code) {
        TokenRes tokenRes = this.authService.resolveTokenRes(clientName, code);
        logger.debug("token - clientName: [" + clientName
                + "], code: [" + code
                + "], tokenRes: [" + tokenRes
                + "]");
        return ResponseEntity.ok(tokenRes);
    }

}
