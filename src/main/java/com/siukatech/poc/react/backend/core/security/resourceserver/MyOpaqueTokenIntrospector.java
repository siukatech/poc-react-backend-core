package com.siukatech.poc.react.backend.core.security.resourceserver;

import com.siukatech.poc.react.backend.core.util.ResourceServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class MyOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp;
    private Map<String, OpaqueTokenIntrospector> opaqueTokenIntrospectorMap;

    public MyOpaqueTokenIntrospector(OAuth2ClientProperties oAuth2ClientProperties
            , OAuth2ResourceServerExtProp oAuth2ResourceServerExtProp) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.oAuth2ResourceServerExtProp = oAuth2ResourceServerExtProp;
        opaqueTokenIntrospectorMap = new HashMap<>();
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        try {
            String issuerUri = ResourceServerUtil.getIssuerUri(token);
            String clientName = oAuth2ClientProperties.getProvider().entrySet().stream()
                    .filter(entry -> entry.getValue().getIssuerUri().equals(issuerUri))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null)
                    ;
            OAuth2ClientProperties.Registration registration = oAuth2ClientProperties.getRegistration().get(clientName);
            OAuth2ResourceServerProperties.Opaquetoken opaqueToken = oAuth2ResourceServerExtProp.getOpaquetoken().entrySet().stream()
                    .filter(entry -> entry.getKey().equals(clientName))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElse(null)
                    ;
            OpaqueTokenIntrospector opaqueTokenIntrospector = opaqueTokenIntrospectorMap.get(clientName);
            if ( opaqueTokenIntrospector == null ) {
                opaqueTokenIntrospector = new SpringOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri()
                        , registration.getClientId(), registration.getClientSecret());
                opaqueTokenIntrospectorMap.put(clientName, opaqueTokenIntrospector);
            }
            log.debug("introspect - issuerUri: [{}], clientName: [{}], opaqueToken: [{}]"
                    , issuerUri, clientName, opaqueToken);
            return opaqueTokenIntrospector.introspect(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
