package com.siukatech.poc.react.backend.parent.security.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.siukatech.poc.react.backend.parent.security.converter.KeycloakJwtAuthenticationConverter;
import com.siukatech.poc.react.backend.parent.security.handler.KeycloakLogoutHandler;
import com.siukatech.poc.react.backend.parent.security.interceptor.OAuth2ClientHttpRequestInterceptor;
import com.siukatech.poc.react.backend.parent.web.annotation.base.PublicController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatchers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
//@ConditionalOnProperty(name = "", havingValue = "http", matchIfMissing = true)
public class WebSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KeycloakLogoutHandler keycloakLogoutHandler;
    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;
    private final KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter;

    public WebSecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler
            , OAuth2ResourceServerProperties oAuth2ResourceServerProperties
            , KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter
    ) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
        this.keycloakJwtAuthenticationConverter = keycloakJwtAuthenticationConverter;
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public RestTemplate oauth2ClientRestTemplate() {
        // This is not working
//        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
//        restTemplateBuilder.additionalInterceptors(new ClientHttpRequestInterceptor() {
////            private final Logger logger = LoggerFactory.getLogger(this.getClass());
//            private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
//
//            @Override
//            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//                logger.debug("oauth2ClientRestTemplate - RestTemplateBuilder - ClientHttpRequestInterceptor - intercept - "
//                                + "authentication.getName: [{}], authentication.getCredentials: [{}]"
//                        , authentication.getName(), authentication.getCredentials());
////                String jwtToken = authentication.getCredentials().toString();
////                request.getHeaders().set(HttpHeaders.AUTHORIZATION, jwtToken);
////                return null;
//                return execution.execute(request, body);
//            }
//        });
//        RestTemplate restTemplate = restTemplateBuilder.build();
        RestTemplate restTemplate = new RestTemplate();
        AtomicInteger formHttpMessageConverterCount = new AtomicInteger();
        // This is not working
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter(this.objectMapper));
        restTemplate.getMessageConverters().stream().forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
//                ((MappingJackson2HttpMessageConverter) httpMessageConverter)
                mappingJackson2HttpMessageConverter
                        .getObjectMapper()
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                ;
            }
            if (httpMessageConverter instanceof FormHttpMessageConverter) {
                formHttpMessageConverterCount.getAndIncrement();
            }
        });
        if (formHttpMessageConverterCount.get() == 0) {
            restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        }
        restTemplate.getInterceptors().add(new OAuth2ClientHttpRequestInterceptor());
        logger.debug("oauth2ClientRestTemplate - formHttpMessageConverterCount.get: [{}]"
                + ", restTemplate.toString: [{}]"
                , formHttpMessageConverterCount.get(), restTemplate.toString()
        );
        return restTemplate;
    }


    /**
     * Configure corsConfigurationSource instead of corsFilter
     * client-app does not need to implement this
     *
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(
                //Arrays.asList("http://localhost:3000", "http://localhost:28080")
                Arrays.asList("*")
        );
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedMethods(Arrays.asList(HttpMethod.HEAD.name()
//                , HttpMethod.GET.name()
//                , HttpMethod.POST.name()
//                , HttpMethod.PUT.name()
//                , HttpMethod.DELETE.name()
//                , HttpMethod.PATCH.name()
//                , HttpMethod.OPTIONS.name()));
        corsConfiguration.setAllowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.toList()));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        final CorsConfiguration config = new CorsConfiguration();
//        //config.setAllowCredentials(true);
//        config.applyPermitDefaultValues();
////        config.setAllowedOrigins(Collections.singletonList("*"));
////        config.setAllowedHeaders(Collections.singletonList("*"));
//        config.setAllowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).collect(Collectors.toList()));
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.csrf().disable();
//        http.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
//                httpSecurityCsrfConfigurer.disable();
//            }
//        });
        // can be rewritten in lambda way
        http.csrf(csrfConfigurer -> csrfConfigurer.disable());

        http.authorizeHttpRequests(requests -> requests
//                .requestMatchers(
//                        "/"
//                        , "/login"
//                        , "/v*/public/**"
//                )
                        .requestMatchers(RequestMatchers.anyOf(
                                AntPathRequestMatcher.antMatcher("/")
                                , AntPathRequestMatcher.antMatcher("/login")
                                , AntPathRequestMatcher.antMatcher("/error")

                                // Only /v*/public/** is allowed to permit without security checking
                                , AntPathRequestMatcher.antMatcher("/v*" + PublicController.REQUEST_MAPPING_URI_PREFIX + "/**")

                                // This is required to add HttpMethod.OPTIONS here
                                // Preflight-request failed reason is missing WebMvcConfigSupport
                                , AntPathRequestMatcher.antMatcher(HttpMethod.OPTIONS, "/v*/**")

                        ))
                        .permitAll()
                        .anyRequest()
                        //.fullyAuthenticated()
                        .authenticated()
        )
//                .antMatchers("/customers*")
//                .hasRole("USER")
//                .anyRequest()
//                .permitAll()
        ;
//        http.oauth2Login()
//                .and()
//                .logout()
//                .addLogoutHandler(keycloakLogoutHandler)
//                .logoutSuccessUrl("/login")
//                .permitAll()
//        ;
        http.logout(logoutConfigurer -> logoutConfigurer
                .addLogoutHandler(keycloakLogoutHandler)
                .logoutSuccessUrl("/login")
                .permitAll()
        );
//        http.oauth2Login(new Customizer<OAuth2LoginConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(OAuth2LoginConfigurer<HttpSecurity> httpSecurityOAuth2LoginConfigurer) {
//                httpSecurityOAuth2LoginConfigurer
//                        .permitAll();
//            }
//        });
        http.oauth2Login(auth2LoginConfigurer -> {
            auth2LoginConfigurer.authorizationEndpoint(authorizationEndpointConfig -> {
                //authorizationEndpointConfig.baseUri("/**");
            })
            ;
            try {
                auth2LoginConfigurer.init(http);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
//        //http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//        http.oauth2ResourceServer()
//                .jwt()
//                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
//        ;
//        http.oauth2ResourceServer(new Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>>() {
//            @Override
//            public void customize(OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer) {
//                httpSecurityOAuth2ResourceServerConfigurer.jwt(new Customizer<OAuth2ResourceServerConfigurer<org.springframework.security.config.annotation.web.builders.HttpSecurity>.JwtConfigurer>() {
//                    @Override
//                    public void customize(OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer jwtConfigurer) {
//                        jwtConfigurer
//                                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
//                                ;
//                    }
//                });
//            }
//        });
        http.oauth2ResourceServer(oAuth2ResourceServerConfigurer ->
                oAuth2ResourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(keycloakJwtAuthenticationConverter)
                        )
        );
        http.sessionManagement(sessionManagementConfigurer ->
                sessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(
                oAuth2ResourceServerProperties.getJwt().getIssuerUri()
        );
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
//                .withIssuerLocation(oAuth2ResourceServerProperties.getJwt().getIssuerUri())
//                .jwsAlgorithm(SignatureAlgorithm.RS512)
//                .build();
        OAuth2TokenValidator<Jwt> withIssuerJwtTokenValidator = JwtValidators.createDefaultWithIssuer(
                oAuth2ResourceServerProperties.getJwt().getIssuerUri()
        );
        OAuth2TokenValidator<Jwt> jwtDelegatingOAuth2TokenValidator = new DelegatingOAuth2TokenValidator<Jwt>(withIssuerJwtTokenValidator);
        jwtDecoder.setJwtValidator(jwtDelegatingOAuth2TokenValidator);
        return jwtDecoder;
    }


}

