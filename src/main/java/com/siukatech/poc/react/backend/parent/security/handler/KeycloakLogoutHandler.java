package com.siukatech.poc.react.backend.parent.security.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * Reference:
 * https://stackoverflow.com/q/76497592
 * https://stackoverflow.com/q/75910767
 */
@Component
public class KeycloakLogoutHandler implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakLogoutHandler.class);
    //@Autowired @Qualifier("securityRestTemplate")
    private RestTemplate restTemplate;

    // this is not working, will cause a circular-dependencies
//    public KeycloakLogoutHandler(@Qualifier("securityRestTemplate") RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    // this is not working, will cause a circular-dependencies
    @Bean(name = "keycloakRestTemplate")
    public RestTemplate keycloakRestTemplate() {
        return new RestTemplate();
    }

    public KeycloakLogoutHandler() {
        this.restTemplate = this.keycloakRestTemplate();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logger.debug("logout - authentication: [" + (authentication == null ? "NULL" : authentication) + "]");
        logoutFromKeycloak((OidcUser) authentication.getPrincipal());
    }

    private void logoutFromKeycloak(OidcUser user) {
        String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(endSessionEndpoint)
                .queryParam("id_token_hint", user.getIdToken().getTokenValue());
        String uriComponentsStr = builder.toUriString();

        logger.info("logoutFromKeycloak - endSessionEndpoint: [" + endSessionEndpoint
                + "], uriComponentsStr: [" + uriComponentsStr
                + "]");

        ResponseEntity<String> logoutResponse = restTemplate.getForEntity(
                uriComponentsStr, String.class);
        if (logoutResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("logoutFromKeycloak - Successfully logged out from Keycloak");
        } else {
            logger.error("logoutFromKeycloak - Could not propagate logout to Keycloak");
        }
    }

}

