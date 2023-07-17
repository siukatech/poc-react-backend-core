package com.siukatech.poc.react.backend.parent.web.controller;


import com.siukatech.poc.react.backend.parent.business.service.AuthService;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.PublicApiV1Controller;
import com.siukatech.poc.react.backend.parent.web.model.LoginForm;
import com.siukatech.poc.react.backend.parent.web.model.TokenRes;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public void doAuthCodeLogin(HttpServletResponse response, @PathVariable String clientName) {
        String authCodeLoginUrl = this.authService.getAuthCodeLoginUrl(clientName);
        logger.debug("doAuthCodeLogin - clientName: [{}], authCodeLoginUrl: [{}]"
                , clientName, authCodeLoginUrl);
        response.setHeader(HttpHeaders.LOCATION, authCodeLoginUrl);
        response.setStatus(HttpStatus.FOUND.value());
    }

    @PostMapping(value = "/auth/token/{clientName}/{code}")
    public ResponseEntity<?> doAuthCodeToken(@PathVariable String clientName, @PathVariable String code) {
        TokenRes tokenRes = this.authService.resolveAuthCodeTokenRes(clientName, code);
        logger.debug("doAuthCodeToken - clientName: [" + clientName
                + "], code: [" + code
                + "], tokenRes: [" + tokenRes
                + "]");
        return ResponseEntity.ok(tokenRes);
    }

    @PostMapping(value = "/auth/login/{clientName}")
    public ResponseEntity<?> doPasswordLogin(@PathVariable String clientName, @RequestBody @Valid LoginForm loginForm) {
        TokenRes tokenRes = this.authService.resolvePasswordTokenRes(clientName, loginForm);
        logger.debug("doPasswordLogin - clientName: [" + clientName
                + "], loginForm.getUsername: [" + loginForm.getUsername()
                + "], tokenRes: [" + tokenRes
                + "]");
        return ResponseEntity.ok(tokenRes);
    }

}
