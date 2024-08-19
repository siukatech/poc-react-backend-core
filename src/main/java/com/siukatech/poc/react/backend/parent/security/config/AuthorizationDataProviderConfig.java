package com.siukatech.poc.react.backend.parent.security.config;

import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import com.siukatech.poc.react.backend.parent.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.security.provider.DatabaseAuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.security.provider.RemoteAuthorizationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * The following beans are dependencies of beans in {@link WebSecurityConfig}.
 * <ul>
 *   <li>oauth2ClientRestTemplate</li>
 *   <li>remoteAuthorizationDataProvider</li>
 *   <li>databaseAuthorizationDataProvider</li>
 * </ul>
 *
 * As a result, this class has been introduced to handle that and prevent circular reference.
 *
 */
@Slf4j
@Configuration
public class AuthorizationDataProviderConfig {

//    private final ParentAppProp parentAppProp;
//    private final UserService userService;
//    private final RestTemplate oauth2ClientRestTemplate;

    public AuthorizationDataProviderConfig(
//            ParentAppProp parentAppProp
//            , UserService userService
//            , RestTemplate oauth2ClientRestTemplate
    ) {
//        this.parentAppProp = parentAppProp;
//        this.userService = userService;
//        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
        log.debug("constructor");
    }

    @Bean("authorizationDataProvider")
    @ConditionalOnProperty("app.api.my-user-info")
    public AuthorizationDataProvider remoteAuthorizationDataProvider(
            RestTemplate oauth2ClientRestTemplate
            , ParentAppProp parentAppProp
    ) {
        log.debug("remoteAuthorizationDataProvider");
//        return new DatabaseAuthorizationDataProvider(userService);
        return new RemoteAuthorizationDataProvider(oauth2ClientRestTemplate, parentAppProp);
    }

    @Bean("authorizationDataProvider")
    @ConditionalOnMissingBean
    public AuthorizationDataProvider databaseAuthorizationDataProvider(
            UserService userService
            , ParentAppProp parentAppProp
    ) {
        log.debug("databaseAuthorizationDataProvider");
        return new DatabaseAuthorizationDataProvider(userService, parentAppProp);
    }

}
