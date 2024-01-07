package com.siukatech.poc.react.backend.parent.business.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import com.siukatech.poc.react.backend.parent.util.EncryptionUtils;
import com.siukatech.poc.react.backend.parent.business.form.auth.LoginForm;
import com.siukatech.poc.react.backend.parent.business.form.auth.RefreshTokenForm;
import com.siukatech.poc.react.backend.parent.business.form.auth.TokenRes;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith({MockitoExtension.class
        , SpringExtension.class
})
@EnableConfigurationProperties
@ContextConfiguration(classes = {
//        RegistrationConfig.class, ProviderConfig.class
//        ,
        OAuth2ClientProperties.class
        , ParentAppProp.class
})
@TestPropertySource({"classpath:abstract-oauth2-tests.properties"
        , "classpath:global/parent-app-config-tests.properties"
})
public class AuthServiceTests extends AbstractUnitTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String CLIENT_NAME = "keycloak";


//    @Autowired
//    private RegistrationConfig registrationConfig;
//    @Autowired
//    private ProviderConfig providerConfig;

//    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
////    private Map<String, String> registrationMap;
//    private String registrationClientId;
////    private String clientSecret;
////    private String authorizationGrantType;
////    private String scope;
////    private String redirectUri;
////    @Value("spring.security.oauth2.client.provider.keycloak")
////    private Map<String, String> providerMap;

    /**
     * This is the OAuth2ClientProperties initiated with @TestPropertySource
     */
    @Autowired
    private OAuth2ClientProperties oAuth2ClientPropertiesForTests;
    @Autowired
    private ParentAppProp parentAppPropForTests;

    @InjectMocks
    private AuthService authService;

    /**
     * Cannot use @Mock, dont know why cannot mock this oAuth2ClientProperties.
     * If the AuthService.oAuth2ClientProperties changed to NOT final,
     * then using @Mock is ok
     * <p>
     * However, if AuthService.oAuth2ClientProperties is final,
     * then only @Spy is ok with doReturn().when()
     */
//    @Mock
    @Spy
    private OAuth2ClientProperties oAuth2ClientProperties;

    /**
     * Cannot use @Mock, dont know why cannot mock exchange method ?_?
     */
//    @Mock
    @Spy
    private RestTemplate oauth2ClientRestTemplate;
    @Spy
    private ObjectMapper objectMapper;
    @Spy
    private ParentAppProp parentAppProp;


//    private
////    OAuth2ClientProperties.Registration
//    Map<String, OAuth2ClientProperties.Registration>
//    prepareRegistration(String clientName) {
//        OAuth2ClientProperties.Registration registration = new OAuth2ClientProperties.Registration();
//        registration.setClientId(registrationConfig.getClientId());
//        registration.setClientSecret(registrationConfig.getClientSecret());
//        registration.setAuthorizationGrantType(registrationConfig.getAuthorizationGrantType());
//        registration.setScope(Set.of(registrationConfig.getScope()));
//        registration.setRedirectUri(registrationConfig.getRedirectUri());
////        return registration;
//        return Map.of(clientName, registration);
//    }
//    private Map<String, OAuth2ClientProperties.Provider> prepareProvider(String clientName) {
//        OAuth2ClientProperties.Provider provider = new OAuth2ClientProperties.Provider();
//        provider.setAuthorizationUri(providerConfig.getAuthorizationUri());
//        provider.setTokenUri(providerConfig.getTokenUri());
//        provider.setUserInfoUri(providerConfig.getUserInfoUri());
//        provider.setIssuerUri(providerConfig.getIssuerUri());
//        provider.setJwkSetUri(providerConfig.getJwkSetUri());
//        provider.setUserNameAttribute(providerConfig.getUserNameAttribute());
//        return Map.of(clientName, provider);
//    }

    private TokenRes prepareTokenRes() {
        TokenRes tokenRes = new TokenRes("accessToken"
                , "refreshToken", "expiresIn"
                , "refreshExpiresIn", "tokenType"
                , 1, "sessionState", "scope");
        return tokenRes;
    }

    @BeforeEach
    public void setup() {
//        logger.debug("setup - registrationConfig: [{}], providerConfig: [{}]"
//                        + ", registrationConfig.clientId: [{}]"
//                        + ", registrationConfig.scope: [{}]"
//                , this.registrationConfig, this.providerConfig
//                , this.registrationConfig.getClientId()
//                , this.registrationConfig.getScope()
//        );
//////        logger.debug("setup - registrationMap: [{}], providerMap: [{}]"
//////                , this.registrationMap, this.providerMap);
////        logger.debug("setup - registrationClientId: [{}]"
////                , this.registrationClientId);
        logger.debug("setup - oAuth2ClientPropertiesForTests.getRegistration.size: [{}]"
                , this.oAuth2ClientPropertiesForTests.getRegistration().size()
        );
    }

    private MyKeyDto prepareMyKeyDto_basic() throws NoSuchAlgorithmException {
        KeyPair keyPair = EncryptionUtils.generateRsaKeyPair();
        MyKeyDto myKeyDto = new MyKeyDto();
        myKeyDto.setLoginId("app-user-01");
//        myKeyDto.setName("App User 01");
//        myKeyDto.setPublicKey("public-key");
//        myKeyDto.setPrivateKey("private-key");
        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        myKeyDto.setPrivateKey(privateKeyBase64);
        myKeyDto.setPublicKey(publicKeyBase64);
        return myKeyDto;
    }

//    @Test
//    public void resolveMyKeyInfo_basic() throws NoSuchAlgorithmException {
//        logger.debug("resolveMyKeyInfo_basic - parentAppPropForTests.myUserInfoUrl: [{}]"
//                        + ", parentAppProp.myUserInfoUrl: [{}]"
//                        + ", parentAppProp.getMyKeyInfoUrl: [{}]"
//                        + ", parentAppProp.getMyKeyInfoUrl: [{}]"
//                , this.parentAppPropForTests.getMyUserInfoUrl()
//                , this.parentAppProp.getMyUserInfoUrl()
//                , this.parentAppPropForTests.getMyKeyInfoUrl()
//                , this.parentAppProp.getMyKeyInfoUrl()
//        );
//
//        // given
//        MyKeyDto myKeyDto = prepareMyKeyDto_basic();
//        String loginId = myKeyDto.getLoginId();
//        when(this.parentAppProp.getMyKeyInfoUrl())
//                .thenReturn(this.parentAppPropForTests.getMyKeyInfoUrl());
////        when(oauth2ClientRestTemplate.exchange(anyString()
////                , eq(HttpMethod.POST), eq(HttpEntity.EMPTY), eq(MyKeyDto.class)))
////                .thenReturn(ResponseEntity.ok(prepareMyKeyDto_basic()));
//        doReturn(ResponseEntity.ok(myKeyDto))
//                .when(this.oauth2ClientRestTemplate).exchange(anyString(), eq(HttpMethod.POST)
//                        , ArgumentMatchers.any(HttpEntity.class), eq(MyKeyDto.class))
//        ;
//
//        // when
//        MyKeyDto myKeyRet = this.authService.resolveMyKeyInfo(loginId);
//
//        // then
//        logger.debug("resolveMyKeyInfo_basic - myKeyRet: [{}]", myKeyRet);
//        assertThat(myKeyRet).hasFieldOrProperty("privateKey")
//                .has(new Condition<>(u -> u.getPrivateKey().contains(myKeyDto.getPrivateKey())
//                        , "Has %s", "private-key"))
//        ;
//
//    }

    @Test
    public void getAuthCodeLoginUrl_basic() {
        // given
        String clientName = CLIENT_NAME;
////        when(this.oAuth2ClientProperties.getRegistration())
////                .thenReturn(prepareRegistration(clientName));
////        when(this.oAuth2ClientProperties.getProvider())
////                .thenReturn(prepareProvider(clientName));
//        doReturn(prepareRegistration(clientName))
//                .when(this.oAuth2ClientProperties).getRegistration();
//        doReturn(prepareProvider(clientName))
//                .when(this.oAuth2ClientProperties).getProvider();
        doReturn(this.oAuth2ClientPropertiesForTests.getRegistration())
                .when(this.oAuth2ClientProperties).getRegistration();
        doReturn(this.oAuth2ClientPropertiesForTests.getProvider())
                .when(this.oAuth2ClientProperties).getProvider();
        logger.debug("getAuthCodeLoginUrl_basic - oAuth2ClientPropertiesForTests.getRegistration.size: [{}]"
                , this.oAuth2ClientPropertiesForTests.getRegistration().size()
        );

        // when
        String authCodeLoginUrl = this.authService.getAuthCodeLoginUrl(clientName);

        // then
        assertThat(authCodeLoginUrl).contains("response_type");
    }

    @Test
    public void resolveAuthCodeTokenRes_basic() {
        // given
        String clientName = CLIENT_NAME;
        String code = "this-is-an-unit-test-code";
//        doReturn(prepareRegistration(clientName))
//                .when(this.oAuth2ClientProperties).getRegistration();
//        doReturn(prepareProvider(clientName))
//                .when(this.oAuth2ClientProperties).getProvider();
////        when(oauth2ClientRestTemplate.exchange(anyString()
////                , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class)))
////                .thenReturn(ResponseEntity.ok(prepareTokenRes()));
        doReturn(oAuth2ClientPropertiesForTests.getRegistration())
                .when(oAuth2ClientProperties).getRegistration();
        doReturn(oAuth2ClientPropertiesForTests.getProvider())
                .when(oAuth2ClientProperties).getProvider();
        doReturn(ResponseEntity.ok(prepareTokenRes()))
                .when(this.oauth2ClientRestTemplate).exchange(anyString()
                        , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class))
        ;

        // when
        TokenRes tokenRes = this.authService.resolveAuthCodeTokenRes(clientName, code);

        // then
        assertThat(tokenRes)
                .hasFieldOrProperty("accessToken")
                .has(new Condition<>(x -> {
                    return "accessToken".equals(x.accessToken());
                }, "Has value:%s", List.of("accessToken")))
        ;
    }

    @Test
    public void resolvePasswordTokenRes_basic() {
        // given
        String clientName = CLIENT_NAME;
        LoginForm loginForm = new LoginForm();
        loginForm.setUsername("username");
        loginForm.setPassword("password");
        doReturn(oAuth2ClientPropertiesForTests.getRegistration())
                .when(oAuth2ClientProperties).getRegistration();
        doReturn(oAuth2ClientPropertiesForTests.getProvider())
                .when(oAuth2ClientProperties).getProvider();
        doReturn(ResponseEntity.ok(prepareTokenRes()))
                .when(this.oauth2ClientRestTemplate).exchange(anyString()
                        , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class))
        ;

        // when
        TokenRes tokenRes = this.authService.resolvePasswordTokenRes(clientName, loginForm);

        // then
        assertThat(tokenRes)
                .hasFieldOrProperty("accessToken")
                .has(new Condition<>(x -> {
                    return "accessToken".equals(x.accessToken());
                }, "Has value:%s", List.of("accessToken")))
        ;
    }

    @Test
    public void resolveRefreshTokenTokenRes_basic() {
        // given
        String clientName = CLIENT_NAME;
        RefreshTokenForm refreshTokenForm = new RefreshTokenForm();
        refreshTokenForm.setAccessToken("access_token");
        refreshTokenForm.setRefreshToken("refresh_token");
        doReturn(oAuth2ClientPropertiesForTests.getRegistration())
                .when(oAuth2ClientProperties).getRegistration();
        doReturn(oAuth2ClientPropertiesForTests.getProvider())
                .when(oAuth2ClientProperties).getProvider();
        doReturn(ResponseEntity.ok(prepareTokenRes()))
                .when(this.oauth2ClientRestTemplate).exchange(anyString()
                        , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class))
        ;

        // when
        TokenRes tokenRes = this.authService.resolveRefreshTokenTokenRes(clientName, refreshTokenForm);

        // then
        assertThat(tokenRes)
                .hasFieldOrProperty("accessToken")
                .has(new Condition<>(x -> {
                    return "accessToken".equals(x.accessToken());
                }, "Has value:%s", List.of("accessToken")))
        ;
    }

    @Test
    public void doAuthLogout_basic() throws URISyntaxException {
//        public HttpStatusCode doAuthLogout(String logoutApi) {
        // given
        String logoutApi = "http://localhost:8080/logout";
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).build();
//        when(oauth2ClientRestTemplate.getForEntity(any(URI.class), eq(Map.class))).thenReturn(responseEntity);
        doReturn(responseEntity).when(oauth2ClientRestTemplate).getForEntity(any(URI.class), eq(Map.class));

        // when
        HttpStatusCode httpStatusCode = this.authService.doAuthLogout(logoutApi);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(HttpStatus.OK.value()));
    }

}
