package com.siukatech.poc.react.backend.parent.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.PublicApiV1Controller;
import com.siukatech.poc.react.backend.parent.web.model.AuthReq;
import com.siukatech.poc.react.backend.parent.web.model.TokenReq;
import com.siukatech.poc.react.backend.parent.web.model.TokenRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;
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

import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@PublicApiV1Controller
public class AuthController {

//    @Value("${security.oauth2.client.registration.keycloak.client-id}")
//    private String clientId;
//    @Value("${security.oauth2.client.registration.keycloak.client-secret}")
//    private String clientSceret;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final RestTemplate oauth2ClientRestTemplate;
    private final ObjectMapper objectMapper;

    public AuthController(
            OAuth2ClientProperties oAuth2ClientProperties
            , RestTemplate oauth2ClientRestTemplate
            , ObjectMapper objectMapper) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
        this.objectMapper = objectMapper;
    }


    @GetMapping(value = "/auth/login/{clientName}")
    public void auth(HttpServletResponse response, @PathVariable String clientName) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
        OAuth2ClientProperties.Provider provider = this.oAuth2ClientProperties.getProvider().get(clientName);
//        response_type=code&client_id=react-backend-client-01&scope=openid&redirect_uri=http://localhost:3000/redirect

        AuthReq authReq = new AuthReq(
                "code"
                , registration.getClientId()
                , String.join(",", registration.getScope())
                , registration.getRedirectUri()
        );
//        Map authReqMap = this.objectMapper.convertValue(authReq, Map.class);
//        List<Map.Entry<String, String>> entryList = authReqMap.entrySet().stream().toList();
//        List<NameValuePair> nameValuePairList = entryList.stream()
//                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
//                .collect(Collectors.toList());
        Map<String, String> authReqMap = this.objectMapper.convertValue(authReq, Map.class);
        List<NameValuePair> nameValuePairList = authReqMap.entrySet().stream()
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        // https://stackoverflow.com/a/2810434
        String queryString = URLEncodedUtils.format(nameValuePairList, Charset.forName(StandardCharsets.UTF_8.name()));
        String authUrl = new StringBuffer()
                .append(provider.getAuthorizationUri())
                .append("?")
//                .append("response_type").append("=").append("code")
//                .append("&")
//                .append("client_id").append("=").append(registration.getClientId())
//                .append("&")
//                .append("scope").append("=").append(String.join(",", registration.getScope()))
//                .append("&")
//                .append("redirect_uri").append("=").append(registration.getRedirectUri())
                .append(queryString)
                .toString();
        logger.debug("auth - clientName: [{}], authUrl: [{}]", clientName, authUrl);
        response.setHeader("Location", authUrl);
        response.setStatus(302);
    }

    @PostMapping(value = "/auth/token/{clientName}/{code}")
    public ResponseEntity<?> token(@PathVariable String clientName, @PathVariable String code) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
        OAuth2ClientProperties.Provider provider = this.oAuth2ClientProperties.getProvider().get(clientName);

        // headers.set('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
        // headers.set('Authorization', `Basic ${Buffer.from(`${client_id}:${client_secret}`)}`);

        String tokenUrl = provider.getTokenUri();
        HttpHeaders httpHeaders = new HttpHeaders();
        String basicAuthorization = registration.getClientId() + ":" + registration.getClientSecret();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(basicAuthorization.getBytes(StandardCharsets.UTF_8)) + "");
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//        MultiValueMap<String, String> tokenReqMap = new LinkedMultiValueMap<String, String>();
//        tokenReqMap.add("client_id", registration.getClientId());
//        tokenReqMap.add("client_secret", registration.getClientSecret());
//        tokenReqMap.add("redirect_uri", registration.getRedirectUri());
//        tokenReqMap.add("grant_type", registration.getAuthorizationGrantType());
//        tokenReqMap.add("code", code);
        TokenReq tokenReq = new TokenReq(registration.getClientId(), registration.getClientSecret()
                , registration.getRedirectUri(), registration.getAuthorizationGrantType(), code);
        Map<String, String> tokenReqMap = this.objectMapper.convertValue(tokenReq, Map.class);
        //
        // IllegalArgumentException with message below will be thrown if converting to MultiValueMap directly
        // Manually convert to MultiValueMap is required
        // java.lang.IllegalArgumentException: Cannot find a deserializer for non-concrete Map type [map type; class org.springframework.util.MultiValueMap, [simple type, class java.lang.Object] -> [collection type; class java.util.List, contains [simple type, class java.lang.Object]]]
        MultiValueMap<String, String> tokenReqMultiValueMap = new LinkedMultiValueMap<>();
        tokenReqMap.forEach((key, value) -> {
            tokenReqMultiValueMap.add(key, value);
        });
        //
        HttpEntity<?> httpEntity = new HttpEntity<>(tokenReqMultiValueMap, httpHeaders);

        logger.debug("token - clientName: [" + clientName
                + "], code: [" + code
                + "], tokenReq: [" + tokenReq
                + "], httpHeaders: [" + httpHeaders
                + "], oauth2ClientRestTemplate.toString: [" + oauth2ClientRestTemplate.toString()
                + "], oauth2ClientRestTemplate.getMessageConverters.size: [" + oauth2ClientRestTemplate.getMessageConverters().size()
                + "]");
        oauth2ClientRestTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
            logger.debug("token - httpMessageConverter.getClass.getName: [" + httpMessageConverter.getClass().getName() + "]");
        });

        ResponseEntity<TokenRes> responseEntity = oauth2ClientRestTemplate.exchange(tokenUrl
                , HttpMethod.POST, httpEntity, TokenRes.class);

        logger.debug("token - clientName: [" + clientName
                + "], code: [" + code
                + "], tokenReq: [" + tokenReq
                + "], responseEntity: [" + responseEntity
                + "]");

        return ResponseEntity.ok(responseEntity.getBody());
    }

}
