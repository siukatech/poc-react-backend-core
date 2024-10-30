package com.siukatech.poc.react.backend.parent.user.config;

import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import com.siukatech.poc.react.backend.parent.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.user.provider.DatabaseAuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.parent.user.repository.UserRepository;
import com.siukatech.poc.react.backend.parent.user.repository.UserViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@Configuration
//@EntityScan(basePackages = {"com.siukatech.poc.react.backend.parent.security.provider.database.entity"})  // "**" means all packages
//@EnableJpaRepositories("com.siukatech.poc.react.backend.parent.security.provider.database.repository")    // "**" means all packages
@EntityScan(basePackages = {"com.siukatech.poc.react.backend.parent.user.entity"})  // "**" means all packages
@EnableJpaRepositories("com.siukatech.poc.react.backend.parent.user.repository")    // "**" means all packages
@ComponentScan(value = {"com.siukatech.poc.react.backend.parent.user"})
public class UserSupportConfig {

    @Bean
    @Primary
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
