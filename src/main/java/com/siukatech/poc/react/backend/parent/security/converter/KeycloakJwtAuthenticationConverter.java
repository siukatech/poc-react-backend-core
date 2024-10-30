package com.siukatech.poc.react.backend.parent.security.converter;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.security.authentication.MyAuthenticationToken;
import com.siukatech.poc.react.backend.parent.security.authority.MyGrantedAuthority;
import com.siukatech.poc.react.backend.parent.security.provider.AuthorizationDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
//@Component(value = "JwtAuthenticationConverter")
//@ConditionalOnProperty(name = "spring.security.oauth2.provider.keycloak.issuer-uri")
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
//    private final UserService userService;
    private final AuthorizationDataProvider authorizationDataProvider;

    public KeycloakJwtAuthenticationConverter(
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter,
//            UserService userService,
            AuthorizationDataProvider authorizationDataProvider) {
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
//        this.userService = userService;
        this.authorizationDataProvider = authorizationDataProvider;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        // subject is the user-id
        String loginId = source.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME);
        String tokenValue = source.getTokenValue();
        log.debug("convert - source.getId: [" + source.getId()
                + "], source.getClaims: [" + source.getClaims()
                + "], source.getHeaders: [" + source.getHeaders()
                + "], source.getAudience: [" + source.getAudience()
                + "], source.getExpiresAt: [" + source.getExpiresAt()
                + "], source.getIssuedAt: [" + source.getIssuedAt()
                + "], source.getNotBefore: [" + source.getNotBefore()
                + "], source.getSubject: [" + source.getSubject()
//                + "], source.getTokenValue: [" + source.getTokenValue()
                + "], tokenValue: [" + tokenValue
                + "], loginId: [" + loginId
                + "]");
        List<GrantedAuthority> convertedAuthorities = new ArrayList<>();
        // Extract authorities from jwt
        convertedAuthorities.addAll(jwtGrantedAuthoritiesConverter.convert(source));
//        UserDetails userDetails = new User(
//                //source.getSubject()
//                loginId
//                , "", convertedAuthorities);
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken(userDetails
//                , source.getTokenValue(), userDetails.getAuthorities());

        UserDto userDto = null;
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//        userDto = userService.findByLoginId(loginId);
            userDto = authorizationDataProvider.findByLoginIdAndTokenValue(loginId, tokenValue);

        List<UserPermissionDto> userPermissionDtoList = authorizationDataProvider.findPermissionsByLoginId(loginId, tokenValue);
        userPermissionDtoList.stream().forEach(userPermissionDto -> {
            convertedAuthorities.add(MyGrantedAuthority.builder()
                            .userRoleId(userPermissionDto.getUserRoleId())
                            .applicationId(userPermissionDto.getApplicationId())
                            .appResourceId(userPermissionDto.getAppResourceId())
                            .accessRight(userPermissionDto.getAccessRight())
                    .build());
        });

        Map<String, Object> attributeMap = new HashMap<>();
        // Extract claims from jwt
        attributeMap.putAll(source.getClaims());
        //
        attributeMap.put(StandardClaimNames.PREFERRED_USERNAME, loginId);
        attributeMap.put(MyAuthenticationToken.ATTR_TOKEN_VALUE, tokenValue);
        attributeMap.put(MyAuthenticationToken.ATTR_USER_ID, userDto.getId());
        attributeMap.put(MyAuthenticationToken.ATTR_PUBLIC_KEY, userDto.getPublicKey());
//        }
        log.debug("convert - loginId: [{}], convertedAuthorities: [{}], attributeMap: [{}]", loginId, convertedAuthorities, attributeMap);

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

