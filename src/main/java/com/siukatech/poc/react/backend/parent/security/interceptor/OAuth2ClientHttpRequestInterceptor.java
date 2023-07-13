package com.siukatech.poc.react.backend.parent.security.interceptor;

import com.siukatech.poc.react.backend.parent.security.config.WebSecurityConfig;
import com.siukatech.poc.react.backend.parent.security.converter.KeycloakJwtAuthenticationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.io.IOException;


public class OAuth2ClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // https://stackoverflow.com/a/47046477
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = null;
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            tokenValue = oAuth2AuthenticationToken.getPrincipal().getAttribute(KeycloakJwtAuthenticationConverter.ATTR_TOKEN_VALUE);
        }
        logger.debug("intercept - request.URI: [{}]"
                        + ", authentication.getName: [{}]"
                        + ", authentication.getCredentials: [{}]"
                        + ", authentication.getClass.getName: [{}]"
                        + ", tokenValue: [{}]"
                , request.getURI()
                , authentication.getName()
                , authentication.getCredentials()
                , authentication.getClass().getName()
                , tokenValue
        );
        if (tokenValue != null) {
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
        }
//        return null;
        return execution.execute(request, body);
    }

}
