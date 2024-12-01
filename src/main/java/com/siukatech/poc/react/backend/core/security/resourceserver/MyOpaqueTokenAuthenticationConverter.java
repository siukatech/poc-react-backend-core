package com.siukatech.poc.react.backend.core.security.resourceserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;
import org.springframework.stereotype.Component;

import javax.crypto.spec.PSource;

@Slf4j
@Component
public class MyOpaqueTokenAuthenticationConverter implements OpaqueTokenAuthenticationConverter {

    private final MyJwtAuthenticationConverter myJwtAuthenticationConverter;
    private final JwtDecoder jwtDecoder;

    public MyOpaqueTokenAuthenticationConverter(
            MyJwtAuthenticationConverter myJwtAuthenticationConverter
            , JwtDecoder jwtDecoder) {
        this.myJwtAuthenticationConverter = myJwtAuthenticationConverter;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
        Jwt source = jwtDecoder.decode(introspectedToken);
        AbstractAuthenticationToken authenticationToken = myJwtAuthenticationConverter.convert(source);
        log.debug("convert - authenticationToken: [{}]", authenticationToken);
        return authenticationToken;
    }
}
