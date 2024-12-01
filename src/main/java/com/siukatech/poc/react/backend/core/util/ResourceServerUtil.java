package com.siukatech.poc.react.backend.core.util;

import com.nimbusds.jwt.SignedJWT;
import com.siukatech.poc.react.backend.core.security.resourceserver.OAuth2ResourceServerExtProp;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.text.ParseException;

public class ResourceServerUtil {

    public static SignedJWT getSignedJWT(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT;
    }

    public static String getIssuerUri(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getIssuer();
    }

    public static String getClientName(OAuth2ClientProperties oAuth2ClientProperties, String issuerUri) {
        String clientName = oAuth2ClientProperties.getProvider().entrySet().stream()
                .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                .map(entry -> entry.getKey())
                .findFirst()
                .orElse(null)
                ;
        return clientName;
    }

    public static OAuth2ResourceServerProperties.Jwt getResourceServerPropJwt(
            OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp, String clientName) {
        OAuth2ResourceServerProperties.Jwt jwt = oAuth2ResourceServerExtProp.getJwt().entrySet().stream()
                .filter(entry -> entry.getKey().equals(clientName))
                .map(entry -> entry.getValue())
                .findFirst()
                .orElse(null)
                ;
        return jwt;
    }

}
