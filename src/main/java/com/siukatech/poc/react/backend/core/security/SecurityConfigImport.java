package com.siukatech.poc.react.backend.core.security;

import com.siukatech.poc.react.backend.core.security.config.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan({"com.siukatech.poc.react.backend.core.security"})
@Import({
        Oauth2ClientRestTemplateConfig.class
        , AuthorizationDataProviderConfig.class
        , OAuth2ResourceServerConfig.class
//        , AuthenticationEntryPointConfig.class
        , WebSecurityConfig.class
})
//@Import({
//        ExternalizedJwtAuthenticationConverter.class
//        , KeycloakLogoutHandler.class
//        , SecurityConfig.class
//})
public class SecurityConfigImport {

}
