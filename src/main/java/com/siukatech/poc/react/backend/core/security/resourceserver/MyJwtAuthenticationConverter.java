package com.siukatech.poc.react.backend.core.security.resourceserver;

import com.siukatech.poc.react.backend.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.core.security.model.MyAuthenticationToken;
import com.siukatech.poc.react.backend.core.security.model.MyGrantedAuthority;
import com.siukatech.poc.react.backend.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.core.util.ResourceServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
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
public class MyJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;
//    private final UserService userService;
    private final AuthorizationDataProvider authorizationDataProvider;
    private final OAuth2ClientProperties oAuth2ClientProperties;

    public MyJwtAuthenticationConverter(
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter,
//            UserService userService,
            AuthorizationDataProvider authorizationDataProvider, OAuth2ClientProperties oAuth2ClientProperties) {
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
//        this.userService = userService;
        this.authorizationDataProvider = authorizationDataProvider;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        // subject is the user-id
        String userId = source.getClaimAsString(StandardClaimNames.PREFERRED_USERNAME);
        String tokenValue = source.getTokenValue();
        log.debug("convert - userId: [" + userId
                + "], source.getId: [" + source.getId()
                + "], source.getClaims: [" + source.getClaims()
                + "], source.getHeaders: [" + source.getHeaders()
                + "], source.getAudience: [" + source.getAudience()
                + "], source.getExpiresAt: [" + source.getExpiresAt()
                + "], source.getIssuedAt: [" + source.getIssuedAt()
                + "], source.getNotBefore: [" + source.getNotBefore()
                + "], source.getSubject: [" + source.getSubject()
//                + "], source.getTokenValue: [" + source.getTokenValue()
                + "], tokenValue: [" + tokenValue
                + "]");
        //
        String issuerUri = source.getIssuer().toString();
        String clientName = ResourceServerUtil.getClientName(oAuth2ClientProperties, issuerUri);
        //
        log.debug("convert - userId: [{}], issuerUri: [{}], clientName: [{}]"
                , userId, issuerUri, clientName);
        //
        List<GrantedAuthority> convertedAuthorities = new ArrayList<>();
        // Extract authorities from jwt
        convertedAuthorities.addAll(jwtGrantedAuthoritiesConverter.convert(source));
//        UserDetails userDetails = new User(
//                //source.getSubject()
//                userId
//                , "", convertedAuthorities);
//        UsernamePasswordAuthenticationToken authenticationToken
//                = new UsernamePasswordAuthenticationToken(userDetails
//                , source.getTokenValue(), userDetails.getAuthorities());

        UserDossierDto userDossierDto = null;
        UserDto userDto = null;
        List<UserPermissionDto> userPermissionDtoList = null;

//        userDto = this.authorizationDataProvider.findUserByUserIdAndTokenValue(userId, tokenValue);
//        userPermissionDtoList = this.authorizationDataProvider.findPermissionsByUserIdAndTokenValue(userId, tokenValue);
        userDossierDto = this.authorizationDataProvider.findDossierByUserIdAndTokenValue(userId, tokenValue);
        userDto = userDossierDto.getUserDto();
        userPermissionDtoList = userDossierDto.getUserPermissionList();

        log.debug("convert - userId: [{}], userPermissionDtoList.size: [{}]", userId, userPermissionDtoList.size());
        userPermissionDtoList.forEach(userPermissionDto -> {
            convertedAuthorities.add(MyGrantedAuthority.builder()
                            .userRoleId(userPermissionDto.getUserRoleId())
                            .applicationId(userPermissionDto.getApplicationId())
                            .appResourceId(userPermissionDto.getAppResourceId())
                            .accessRight(userPermissionDto.getAccessRight())
                    .build());
//            log.debug("convert - userId: [{}], userPermissionDto: [{}]", userId, userPermissionDto);
        });

        Map<String, Object> attributeMap = new HashMap<>();
        // Extract claims from jwt
        attributeMap.putAll(source.getClaims());
        //
        attributeMap.put(StandardClaimNames.PREFERRED_USERNAME, userId);
        attributeMap.put(MyAuthenticationToken.ATTR_TOKEN_VALUE, tokenValue);
//        attributeMap.put(MyAuthenticationToken.ATTR_USER_ID, userDto.getId());
//        attributeMap.put(MyAuthenticationToken.ATTR_PUBLIC_KEY, userDto.getPublicKey());
        attributeMap.put(MyAuthenticationToken.ATTR_USER_DOSSIER_DTO, userDossierDto);
//        }
        log.debug("convert - userId: [{}], convertedAuthorities: [{}]"
                        + ", attributeMap.containsKey(PREFERRED_USERNAME): [{}]"
                        + ", attributeMap.containsKey(ATTR_TOKEN_VALUE): [{}]"
                        + ", attributeMap.containsKey(ATTR_USER_DOSSIER_DTO): [{}]"
                , userId
                , convertedAuthorities
                , attributeMap.containsKey(StandardClaimNames.PREFERRED_USERNAME)
                , attributeMap.containsKey(MyAuthenticationToken.ATTR_TOKEN_VALUE)
                , attributeMap.containsKey(MyAuthenticationToken.ATTR_USER_DOSSIER_DTO)
        );

        OAuth2User oAuth2User = new DefaultOAuth2User(convertedAuthorities, attributeMap, StandardClaimNames.PREFERRED_USERNAME);
//        OAuth2AuthenticationToken authenticationToken = new OAuth2AuthenticationToken(oAuth2User, convertedAuthorities, "keycloak");
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(oAuth2User, convertedAuthorities, clientName);
        return authenticationToken;
    }

//    @Override
//    public <U> Converter<Jwt, U> andThen(Converter<? super AbstractAuthenticationToken, ? extends U> after) {
//        return Converter.super.andThen(after);
//    }

}

