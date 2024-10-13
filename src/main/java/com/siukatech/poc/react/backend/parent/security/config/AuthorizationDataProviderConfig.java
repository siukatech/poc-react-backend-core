package com.siukatech.poc.react.backend.parent.security.config;

import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserViewRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.service.UserService;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import com.siukatech.poc.react.backend.parent.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.security.provider.DatabaseAuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.security.provider.RemoteAuthorizationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
            ParentAppProp parentAppProp
            , RestTemplate oauth2ClientRestTemplate
    ) {
        log.debug("remoteAuthorizationDataProvider");
//        return new DatabaseAuthorizationDataProvider(userService);
        return new RemoteAuthorizationDataProvider(parentAppProp, oauth2ClientRestTemplate);
    }

    @Bean("authorizationDataProvider")
    @ConditionalOnMissingBean
    public AuthorizationDataProvider databaseAuthorizationDataProvider(
            ParentAppProp parentAppProp
            , ModelMapper modelMapper
//            , UserService userService
            , UserRepository userRepository
            , UserPermissionRepository userPermissionRepository
            , UserViewRepository userViewRepository
    ) {
        log.debug("databaseAuthorizationDataProvider");
        return new DatabaseAuthorizationDataProvider(parentAppProp
                , modelMapper
//                , userService
                , userRepository
                , userPermissionRepository
                , userViewRepository
        );
    }

}
