package com.siukatech.poc.react.backend.parent.business.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.business.config.ProviderConfig;
import com.siukatech.poc.react.backend.parent.business.config.RegistrationConfig;
import com.siukatech.poc.react.backend.parent.business.service.AuthService;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppConfig;
import com.siukatech.poc.react.backend.parent.web.model.TokenRes;
import lombok.Data;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class
        , SpringExtension.class
})
@EnableConfigurationProperties
@ContextConfiguration(classes = {
//        RegistrationConfig.class, ProviderConfig.class
//        ,
        OAuth2ClientProperties.class
})
@TestPropertySource("classpath:abstract-oauth2-tests.properties")
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

    @InjectMocks
    private AuthService authService;

    /**
     * Cannot use @Mock, dont know why cannot mock this oAuth2ClientProperties.
     * If the AuthService.oAuth2ClientProperties changed to NOT final,
     * then using @Mock is ok
     *
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

    @Test
    public void getAuthUrl_basic() {
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
        logger.debug("getAuthUrl_basic - oAuth2ClientPropertiesForTests.getRegistration.size: [{}]"
                , this.oAuth2ClientPropertiesForTests.getRegistration().size()
        );

        // when
        String authUrl = this.authService.getAuthUrl(clientName);

        // then
        assertThat(authUrl).contains("response_type");
    }

    @Test
    public void resolveTokenRes_basic() {
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
        TokenRes tokenRes = this.authService.resolveTokenRes(clientName, code);

        // then
        assertThat(tokenRes)
                .hasFieldOrProperty("accessToken")
                .has(new Condition<>(x -> {
                    return "accessToken".equals(x.accessToken());
                }, "Has value:%s", List.of("accessToken")))
        ;
    }

}
