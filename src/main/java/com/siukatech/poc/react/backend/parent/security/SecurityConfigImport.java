package com.siukatech.poc.react.backend.parent.security;

import com.siukatech.poc.react.backend.parent.security.config.AuthorizationDataProviderConfig;
import com.siukatech.poc.react.backend.parent.security.config.Oauth2ClientRestTemplateConfig;
import com.siukatech.poc.react.backend.parent.security.config.WebSecurityConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan({"com.siukatech.poc.react.backend.parent.security"})
@Import({
        Oauth2ClientRestTemplateConfig.class
        , AuthorizationDataProviderConfig.class
        , WebSecurityConfig.class
})
//@Import({
//        ExternalizedJwtAuthenticationConverter.class
//        , KeycloakLogoutHandler.class
//        , SecurityConfig.class
//})
public class SecurityConfigImport {

}
