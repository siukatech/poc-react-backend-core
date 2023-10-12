package com.siukatech.poc.react.backend.parent.security.converter;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.security.authentication.MyAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private KeycloakJwtAuthenticationConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        // subject is the user-id
        String loginId = source.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME);
        logger.debug("convert - source.getId: [" + source.getId()
                + "], source.getClaims: [" + source.getClaims()
                + "], source.getHeaders: [" + source.getHeaders()
                + "], source.getAudience: [" + source.getAudience()
                + "], source.getExpiresAt: [" + source.getExpiresAt()
                + "], source.getIssuedAt: [" + source.getIssuedAt()
                + "], source.getNotBefore: [" + source.getNotBefore()
                + "], source.getSubject: [" + source.getSubject()
                + "], source.getTokenValue: [" + source.getTokenValue()
                + "], loginId: [" + loginId
                + "]");
        List<GrantedAuthority> convertedAuthorities = new ArrayList<>();
//        UserDetails userDetails = new User(
//                //source.getSubject()
//                loginId
//                , "", convertedAuthorities);
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken(userDetails
//                , source.getTokenValue(), userDetails.getAuthorities());

        UserDto userDto = userService.findByLoginId(loginId);

        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put(StandardClaimNames.PREFERRED_USERNAME, loginId);
        attributeMap.put(MyAuthenticationToken.ATTR_TOKEN_VALUE, source.getTokenValue());
        attributeMap.put(MyAuthenticationToken.ATTR_USER_ID, userDto.getId());
        OAuth2User oAuth2User = new DefaultOAuth2User(convertedAuthorities, attributeMap, StandardClaimNames.PREFERRED_USERNAME);
//        OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(oAuth2User, convertedAuthorities, "keycloak");
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(oAuth2User, convertedAuthorities, "keycloak");
        return authenticationToken;
    }

    //    @Override
    //    public <U> Converter<Jwt, U> andThen(Converter<? super AbstractAuthenticationToken, ? extends U> after) {
    //        return Converter.super.andThen(after);
    //    }

}

