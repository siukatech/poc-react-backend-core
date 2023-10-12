package com.siukatech.poc.react.backend.parent.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;

public class MyAuthenticationToken extends OAuth2AuthenticationToken {

    public final static String ATTR_TOKEN_VALUE = "ATTR_TOKEN_VALUE";
    public final static String ATTR_USER_ID = "ATTR_USER_ID";

    /**
     * Constructs an {@code OAuth2AuthenticationToken} using the provided parameters.
     *
     * @param principal                      the users {@code Principal} registered with the OAuth 2.0 Provider
     * @param authorities                    the authorities granted to the users
     * @param authorizedClientRegistrationId the registration identifier of the
     *                                       {@link OAuth2AuthorizedClient Authorized Client}
     */
    public MyAuthenticationToken(OAuth2User principal, Collection<? extends GrantedAuthority> authorities, String authorizedClientRegistrationId) {
        super(principal, authorities, authorizedClientRegistrationId);
    }

    public String getTokenValue() {
        return this.getPrincipal().getAttribute(ATTR_TOKEN_VALUE);
    }

    public Long getUserId() {
        return this.getPrincipal().getAttribute(ATTR_USER_ID);
    }

}
