package com.siukatech.poc.react.backend.parent.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.AbstractWebTests;
import com.siukatech.poc.react.backend.parent.web.controller.AuthController;
import com.siukatech.poc.react.backend.parent.web.model.TokenRes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.security.oauth2.client.registration.keycloak.client-id: ${client-id}"
        , "spring.security.oauth2.client.registration.keycloak.client-secret: ${client-secret}"
        , "spring.security.oauth2.client.registration.keycloak.authorization-grant-type: authorization_code"
        , "spring.security.oauth2.client.registration.keycloak.scope: openid"
        , "spring.security.oauth2.client.registration.keycloak.redirect-uri: ${oauth2-client-redirect-uri}"
        //
        , "spring.security.oauth2.client.provider.keycloak.authorization-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/auth"
        , "spring.security.oauth2.client.provider.keycloak.token-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/token"
        , "spring.security.oauth2.client.provider.keycloak.user-info-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/userinfo"
        , "spring.security.oauth2.client.provider.keycloak.issuer-uri: ${oauth2-client-keycloak}/realms/${client-realm}"
        , "spring.security.oauth2.client.provider.keycloak.jwk-set-uri: ${oauth2-client-keycloak}/realms/${client-realm}/protocol/openid-connect/certs"
        , "spring.security.oauth2.client.provider.keycloak.user-name-attribute: preferred_username"
})
public class AuthControllerTests extends AbstractWebTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Provided by WebMvcTest, therefore we can use @Autowired.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * @Autowired is defined because the OAuth2ClientProperties will be created based on the properties.
     * Those oauth2 properties are provided by @TestPropertiesSource
     */
//    @MockBean
    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    /**
     * @MockBean is defined because we dont need real rest call in our test.
     * Mock the oauth2ClientRestTemplate.exchange will be fine.
     */
    @MockBean
    private RestTemplate oauth2ClientRestTemplate;

    /**
     * ~~@SpyBean is defined because the objectMapper has been used in 'auth' and 'token' methods.~~
     * ~~There are some object type conversions happened in above-mentioned methods.~~
     * ~~So we need a semi-real bean here for our action.~~
     * @Autowired is defined because we need the real objectMapper to perform object conversion.
     */
//    @SpyBean
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @MockBean is defined for InMemoryClientRegistrationRepository.
     * Because we dont need the real clientRegistrationRepository.
     * The reason behind is that during the initialization of InMemoryClientRegistrationRepository in OAuth2ClientRegistrationRepositoryConfiguration,
     * Spring will make a real rest call to the registered issuer-uri in OAuth2ClientPropertiesMapper.asClientRegistration.
     * -> OAuth2ClientPropertiesMapper.getClientRegistration
     * -> OAuth2ClientPropertiesMapper.getBuilderFromIssuerIfPossible
     * -> ClientRegistrations.fromIssuerLocation
     * -> ClientRegistrations.oidc
     *
     * private static Supplier<ClientRegistration.Builder> oidc(URI issuer) {
     * 	// @formatter:off
     * 	URI uri = UriComponentsBuilder.fromUri(issuer)
     * 	    .replacePath(issuer.getPath() + OIDC_METADATA_PATH)
     *  	.build(Collections.emptyMap());
     *  // @formatter:on
     *  return () -> {
     *      RequestEntity<Void> request = RequestEntity.get(uri).build();
     * 		Map<String, Object> configuration = rest.exchange(request, typeReference).getBody();
     * 		OIDCProviderMetadata metadata = parse(configuration, OIDCProviderMetadata::parse);
     * 		ClientRegistration.Builder builder = withProviderConfiguration(metadata, issuer.toASCIIString())
     * 				.jwkSetUri(metadata.getJWKSetURI().toASCIIString());
     * 		if (metadata.getUserInfoEndpointURI() != null) {
     * 			builder.userInfoUri(metadata.getUserInfoEndpointURI().toASCIIString());
     *      }
     *		return builder;
     *   };
     * }
     *
     * As a see, an Exception will be thrown at this stage.
     *
     */
    @MockBean
//    @Autowired
    private InMemoryClientRegistrationRepository clientRegistrationRepository;

    private final String CLIENT_NAME = "keycloak";

    @BeforeEach
    public void setup(TestInfo testInfo) {
//        OAuth2ClientProperties.Registration registration = new OAuth2ClientProperties.Registration();
//        registration.setClientId("");
//        registration.setAuthorizationGrantType("");
//        registration.setProvider("");
//        registration.setClientName("");
//        registration.setScope(Set.of(""));
//        registration.setClientAuthenticationMethod("");
//        registration.setClientSecret("");
//        registration.setRedirectUri("");
//        OAuth2ClientProperties.Provider provider = new OAuth2ClientProperties.Provider();
//        provider.setAuthorizationUri("");
//        provider.setIssuerUri("");
//        provider.setJwkSetUri("");
//        provider.setTokenUri("");
//        provider.setUserInfoUri("");
//        provider.setUserInfoAuthenticationMethod("");
//        provider.setUserNameAttribute("");
//        oAuth2ClientProperties = new OAuth2ClientProperties();
//        oAuth2ClientProperties.getRegistration().put("keycloak", registration);
//        oAuth2ClientProperties.getProvider().put("keycloak", provider);
    }

    @Test
    public void auth_basic() throws Exception {
        // given


        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v1/public" + "/auth/login/{clientName}", CLIENT_NAME)
                .with(csrf())
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn()
                ;

    }

    @Test
    public void token_basic() throws Exception {
        // given
        String code = "this-is-an-unit-test-code";
        TokenRes tokenRes = new TokenRes("accessToken"
                , "refreshToken", "expiresIn"
                , "refreshExpiresIn", "tokenType"
                , 1, "sessionState", "scope");
        when(oauth2ClientRestTemplate.exchange(anyString()
                , eq(HttpMethod.POST), any(HttpEntity.class), eq(TokenRes.class)))
                .thenReturn(ResponseEntity.ok(tokenRes));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/v1/public" + "/auth/token/{clientName}/{code}", CLIENT_NAME, code)
                .with(csrf())
                ;

        // then
        MvcResult mvcResult = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("access_token")))
                .andReturn()
                ;
    }

}
