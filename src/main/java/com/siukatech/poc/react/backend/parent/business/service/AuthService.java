package com.siukatech.poc.react.backend.parent.business.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.GrantType;
import com.siukatech.poc.react.backend.parent.business.form.auth.*;
import com.siukatech.poc.react.backend.parent.util.URLEncoderUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final RestTemplate oauth2ClientRestTemplate;
    //    private final ParentAppProp parentAppProp;
    private final ObjectMapper objectMapper;

    public AuthService(OAuth2ClientProperties oAuth2ClientProperties
            , RestTemplate oauth2ClientRestTemplate
//            , ParentAppProp parentAppProp
            , ObjectMapper objectMapper
    ) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
//        this.parentAppProp = parentAppProp;
        this.objectMapper = objectMapper;
    }

//    public MyKeyDto resolveMyKeyInfo(String loginId) {
//        String myKeyInfoUrl = this.parentAppProp.getMyKeyInfoUrl();
//        MyKeyDto myKeyDto = null;
//        if (StringUtils.isNotEmpty(myKeyInfoUrl)) {
//            ResponseEntity<MyKeyDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
//                    myKeyInfoUrl, HttpMethod.POST, HttpEntity.EMPTY, MyKeyDto.class);
//            myKeyDto = responseEntity.getBody();
//            logger.debug("resolveMyKeyInfo - loginId: [{}], myKeyInfoUrl: [{}], myKeyDto.getLoginId: [{}]"
////                + ", responseEntity.getBody.toString: [{}]"
//                    , loginId, myKeyInfoUrl, myKeyDto.getLoginId()
////                , responseEntity.getBody().toString()
//            );
//            if (!loginId.equals(myKeyDto.getLoginId())) {
//                throw new EntityNotFoundException(
//                        "User does not match loginId: [%s], myKeyDto.getLoginId: [%s]"
//                                .formatted(loginId, myKeyDto.getLoginId()));
//            }
//        }
//        else {
//            logger.debug("resolveMyKeyInfo - loginId: [{}], myKeyInfoUrl: [{}]"
//                    , loginId, myKeyInfoUrl
//            );
//            throw new RuntimeException(
//                    "User with loginId: [%s] cannot be resolved because of the empty my-user-info"
//                            .formatted(loginId));
//        }
//        return myKeyDto;
//    }

    public String getAuthCodeLoginUrl(String clientName) {
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
        // https://stackoverflow.com/a/16066990
        String queryString =
//                URLEncodedUtils.format(nameValuePairList, Charset.forName(StandardCharsets.UTF_8.name()))
//                URLEncoder.encode(
                URLEncoderUtils.encodeToQueryString(nameValuePairList);
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
        logger.debug("getAuthCodeLoginUrl - clientName: [{}], authUrl: [{}]", clientName, authUrl);
        return authUrl;
    }

    private TokenRes resolveOAuth2TokenRes(String clientName, TokenReq tokenReq, HttpHeaders httpHeaders) {
        OAuth2ClientProperties.Provider provider = this.oAuth2ClientProperties.getProvider().get(clientName);

        String tokenUrl = provider.getTokenUri();
//        MultiValueMap<String, String> tokenReqMap = new LinkedMultiValueMap<String, String>();
//        tokenReqMap.add("client_id", registration.getClientId());
//        tokenReqMap.add("client_secret", registration.getClientSecret());
//        tokenReqMap.add("redirect_uri", registration.getRedirectUri());
//        tokenReqMap.add("grant_type", registration.getAuthorizationGrantType());
//        tokenReqMap.add("code", code);
        Map<String, String> tokenReqMap = this.objectMapper.convertValue(tokenReq, Map.class);
        //
        // IllegalArgumentException with message below will be thrown if converting to MultiValueMap directly
        // Manually convert to MultiValueMap is required
        // java.lang.IllegalArgumentException: Cannot find a deserializer for non-concrete Map type [map type; class org.springframework.util.MultiValueMap, [simple type, class java.lang.Object] -> [collection type; class java.util.List, contains [simple type, class java.lang.Object]]]
        MultiValueMap<String, String> tokenReqMultiValueMap = new LinkedMultiValueMap<>();
        tokenReqMap.forEach((key, value) -> {
            if (value != null) tokenReqMultiValueMap.add(key, value);
        });
        //
        HttpEntity<?> httpEntity = new HttpEntity<>(tokenReqMultiValueMap, httpHeaders);

        logger.debug("resolveOAuth2TokenRes - clientName: [" + clientName
                + "], tokenReq: [" + tokenReq
                + "], httpHeaders: [" + httpHeaders
                + "], oauth2ClientRestTemplate.toString: [" + oauth2ClientRestTemplate.toString()
                + "], oauth2ClientRestTemplate.getMessageConverters.size: [" + oauth2ClientRestTemplate.getMessageConverters().size()
                + "]");
        oauth2ClientRestTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
            logger.debug("resolveOAuth2TokenRes - httpMessageConverter.getClass.getName: [" + httpMessageConverter.getClass().getName() + "]");
        });

        ResponseEntity<TokenRes> responseEntity = oauth2ClientRestTemplate.exchange(tokenUrl
                , HttpMethod.POST, httpEntity, TokenRes.class);

        TokenRes tokenRes = responseEntity.getBody();
        logger.debug("resolveOAuth2TokenRes - clientName: [" + clientName
                + "], tokenReq: [" + tokenReq
                + "], responseEntity: [" + responseEntity
                + "]");

        return tokenRes;
    }

    public TokenRes resolveAuthCodeTokenRes(String clientName, String code) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
//        OAuth2ClientProperties.Provider provider = this.oAuth2ClientProperties.getProvider().get(clientName);

        // headers.set('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
        // headers.set('Authorization', `Basic ${Buffer.from(`${client_id}:${client_secret}`)}`);

//        String tokenUrl = provider.getTokenUri();
        HttpHeaders httpHeaders = new HttpHeaders();
//        String basicAuthorization = registration.getClientId() + ":" + registration.getClientSecret();
//        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString(basicAuthorization.getBytes(StandardCharsets.UTF_8)) + "");
//        httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

//        MultiValueMap<String, String> tokenReqMap = new LinkedMultiValueMap<String, String>();
//        tokenReqMap.add("client_id", registration.getClientId());
//        tokenReqMap.add("client_secret", registration.getClientSecret());
//        tokenReqMap.add("grant_type", registration.getAuthorizationGrantType());
//        tokenReqMap.add("redirect_uri", registration.getRedirectUri());
//        tokenReqMap.add("code", code);
        TokenReq tokenCodeReq = TokenCodeReq.builder()
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .grantType(registration.getAuthorizationGrantType())
                .redirectUri(registration.getRedirectUri())
                .code(code)
                .build();
//        Map<String, String> tokenReqMap = this.objectMapper.convertValue(tokenCodeReq, Map.class);
//        //
//        // IllegalArgumentException with message below will be thrown if converting to MultiValueMap directly
//        // Manually convert to MultiValueMap is required
//        // java.lang.IllegalArgumentException: Cannot find a deserializer for non-concrete Map type [map type; class org.springframework.util.MultiValueMap, [simple type, class java.lang.Object] -> [collection type; class java.util.List, contains [simple type, class java.lang.Object]]]
//        MultiValueMap<String, String> tokenReqMultiValueMap = new LinkedMultiValueMap<>();
//        tokenReqMap.forEach((key, value) -> {
//            if (value != null) tokenReqMultiValueMap.add(key, value);
//        });
//        //
//        HttpEntity<?> httpEntity = new HttpEntity<>(tokenReqMultiValueMap, httpHeaders);
//
//        logger.debug("resolveTokenRes - clientName: [" + clientName
//                + "], code: [" + code
//                + "], tokenCodeReq: [" + tokenCodeReq
//                + "], httpHeaders: [" + httpHeaders
//                + "], oauth2ClientRestTemplate.toString: [" + oauth2ClientRestTemplate.toString()
//                + "], oauth2ClientRestTemplate.getMessageConverters.size: [" + oauth2ClientRestTemplate.getMessageConverters().size()
//                + "]");
//        oauth2ClientRestTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
//            logger.debug("token - httpMessageConverter.getClass.getName: [" + httpMessageConverter.getClass().getName() + "]");
//        });
//
//        ResponseEntity<TokenRes> responseEntity = oauth2ClientRestTemplate.exchange(tokenUrl
//                , HttpMethod.POST, httpEntity, TokenRes.class);
//
//        TokenRes tokenRes = responseEntity.getBody();
//        logger.debug("resolveTokenRes - clientName: [" + clientName
//                + "], code: [" + code
//                + "], tokenCodeReq: [" + tokenCodeReq
//                + "], responseEntity: [" + responseEntity
//                + "]");

        TokenRes tokenRes = this.resolveOAuth2TokenRes(clientName, tokenCodeReq, httpHeaders);
        return tokenRes;
    }

    public TokenRes resolvePasswordTokenRes(String clientName, LoginForm loginForm) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
        HttpHeaders httpHeaders = new HttpHeaders();
        TokenReq tokenPasswordReq = TokenPasswordReq.builder()
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .grantType(GrantType.PASSWORD.getValue())
//                .redirectUri(registration.getRedirectUri())
//                .grantType(registration.getAuthorizationGrantType())
                .username(loginForm.getUsername())
                .password(loginForm.getPassword())
                .build();
        TokenRes tokenRes = this.resolveOAuth2TokenRes(clientName, tokenPasswordReq, httpHeaders);
        return tokenRes;
    }

    public TokenRes resolveRefreshTokenTokenRes(String clientName, RefreshTokenForm refreshTokenForm) {
        OAuth2ClientProperties.Registration registration = this.oAuth2ClientProperties.getRegistration().get(clientName);
        HttpHeaders httpHeaders = new HttpHeaders();
        TokenReq tokenRefreshTokenReq = TokenRefreshTokenReq.builder()
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .grantType(GrantType.REFRESH_TOKEN.getValue())
                .refreshToken(refreshTokenForm.getRefreshToken())
//                .accessToken(refreshTokenForm.getAccessToken())
                .build();
        TokenRes tokenRes = this.resolveOAuth2TokenRes(clientName, tokenRefreshTokenReq, httpHeaders);
        return tokenRes;
    }

    public HttpStatusCode doAuthLogout(String logoutApi) throws URISyntaxException {
        ResponseEntity<Map> responseEntity = this.oauth2ClientRestTemplate.getForEntity(new URI(logoutApi), Map.class);
        Map map = responseEntity.getBody();
        logger.debug("doAuthLogout - map: [{}], responseEntity: [{}]"
                , map, responseEntity);
        return responseEntity.getStatusCode();
    }
}
