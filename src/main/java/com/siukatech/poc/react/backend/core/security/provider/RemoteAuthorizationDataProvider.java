package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.business.dto.MyPermissionDto;
import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.core.global.config.AppCoreProp;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RemoteAuthorizationDataProvider implements AuthorizationDataProvider {

    private final RestTemplate oauth2ClientRestTemplate;

    private final AppCoreProp appCoreProp;

    public RemoteAuthorizationDataProvider(
            AppCoreProp appCoreProp
            , RestTemplate oauth2ClientRestTemplate) {
        this.appCoreProp = appCoreProp;
        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
    }

    private void prepareHttpHeaders(HttpHeaders httpHeaders, String tokenValue) {
        // Special handling of adding token to oauth2ClientRestTemplate
        // authentication from SecurityContext is null when the process is KeycloakJwtAuthenticationConverter.
        // authentication is still preparing at that moment.
        // So header is required to configured at that moment.
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
    }

    @Override
    public UserDto findUserByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findUserByUserIdAndTokenValue - start");
        String myUserInfoUrl = this.appCoreProp.getMyUserInfoUrl();
        UserDto userDto = null;
        if (StringUtils.isNotEmpty(myUserInfoUrl)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            prepareHttpHeaders(httpHeaders, tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            try {
                ResponseEntity<UserDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                        myUserInfoUrl, HttpMethod.GET
//                    , HttpEntity.EMPTY
                        , httpEntity
                        , UserDto.class);
                userDto = responseEntity.getBody();
                log.debug("findUserByUserIdAndTokenValue - userId: [{}], myUserInfoUrl: [{}]"
                                + ", userDto.getUserId: [{}]"
//                                + ", responseEntity.getBody.toString: [{}]"
                        , userId, myUserInfoUrl
                        , (userDto == null ? "NULL" : userDto.getUserId())
//                        , responseEntity.getBody().toString()
                );
                if (!userId.equals(userDto.getUserId())) {
                    throw new EntityNotFoundException(
                            "User does not match userId: [%s], userDto.getUserId: [%s]"
                                    .formatted(userId, userDto.getUserId()));
                }
            } catch (Exception e) {
                throw new RuntimeException(
                        "Error occurred during calling api: [%s]".formatted(myUserInfoUrl)
                        , e);
            }
        } else {
            log.debug("findUserByUserIdAndTokenValue - userId: [{}], myUserInfoUrl: [{}]"
                    , userId, myUserInfoUrl
            );
            throw new RuntimeException(
                    "User with userId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(userId));
        }
        log.debug("findUserByUserIdAndTokenValue - end");
        return userDto;
    }

    @Override
    public List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String userId, String tokenValue) {
        log.debug("findPermissionsByUserId - start");
        List<UserPermissionDto> userPermissionDtoList = new ArrayList<>();
        //
        String myPermissionInfoUrl = this.appCoreProp.getMyPermissionInfoUrl();
        MyPermissionDto myPermissionDto = null;
        if (StringUtils.isNotEmpty(myPermissionInfoUrl)) {
            UriComponentsBuilder myPermissionInfoUriBuilder = UriComponentsBuilder.fromUriString(myPermissionInfoUrl)
                    .queryParam("applicationId", this.appCoreProp.getApplicationId());
            String myPermissionInfoUriTemplate = myPermissionInfoUriBuilder.encode().toUriString();
            HttpHeaders httpHeaders = new HttpHeaders();
            prepareHttpHeaders(httpHeaders, tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<MyPermissionDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                    myPermissionInfoUriTemplate, HttpMethod.GET
                    , httpEntity
//                    , new ParameterizedTypeReference<UserPermissionInfoDto>() {
//                    }
                    , MyPermissionDto.class
                    );
            myPermissionDto = responseEntity.getBody();
            log.debug("findPermissionsByUserId - userId: [{}], myPermissionInfoUriTemplate: [{}], userPermissionInfoDto.getUserId: [{}], userPermissionDtoList.size: [{}]"
//                + ", responseEntity.getBody.toString: [{}]"
                    , userId, myPermissionInfoUriTemplate, myPermissionDto.getUserId()
                    , myPermissionDto.getUserPermissionList().size()
//                , responseEntity.getBody().toString()
            );
            userPermissionDtoList.addAll(myPermissionDto.getUserPermissionList());
            log.debug("findPermissionsByUserId - userId: [{}], userPermissionDtoList: [{}]"
                    , userId, userPermissionDtoList.size()
            );
            if (!userId.equals(myPermissionDto.getUserId())) {
                throw new EntityNotFoundException(
                        "User does not match userId: [%s], userPermissionInfoDto.getUserId: [%s]"
                                .formatted(userId, myPermissionDto.getUserId()));
            }
        } else {
            log.debug("findPermissionsByUserId - userId: [{}], myUserPermissionInfoUrl: [{}]"
                    , userId, myPermissionInfoUrl
            );
            throw new RuntimeException(
                    "User with userId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(userId));
        }
        log.debug("findPermissionsByUserId - end");
        return userPermissionDtoList;
    }

    @Override
    public UserDossierDto findDossierByUserIdAndTokenValue(String userId, String tokenValue) {
        return null;
    }

}
