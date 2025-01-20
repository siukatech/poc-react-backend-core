package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionInfoDto;
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
    public UserDto findByLoginIdAndTokenValue(String loginId, String tokenValue) {
        log.debug("findByLoginId - start");
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
                log.debug("findByLoginId - loginId: [{}], myUserInfoUrl: [{}]"
                                + ", userDto.getLoginId: [{}]"
//                                + ", responseEntity.getBody.toString: [{}]"
                        , loginId, myUserInfoUrl
                        , (userDto == null ? "NULL" : userDto.getLoginId())
//                        , responseEntity.getBody().toString()
                );
                if (!loginId.equals(userDto.getLoginId())) {
                    throw new EntityNotFoundException(
                            "User does not match loginId: [%s], userDto.getLoginId: [%s]"
                                    .formatted(loginId, userDto.getLoginId()));
                }
            } catch (Exception e) {
                throw new RuntimeException(
                        "Error occurred during calling api: [%s]".formatted(myUserInfoUrl)
                        , e);
            }
        } else {
            log.debug("findByLoginId - loginId: [{}], myUserInfoUrl: [{}]"
                    , loginId, myUserInfoUrl
            );
            throw new RuntimeException(
                    "User with loginId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(loginId));
        }
        log.debug("findByLoginId - end");
        return userDto;
    }

    @Override
    public List<UserPermissionDto> findPermissionsByLoginId(String loginId, String tokenValue) {
        log.debug("findPermissionsByLoginId - start");
        List<UserPermissionDto> userPermissionDtoList = new ArrayList<>();
        //
        String myPermissionInfoUrl = this.appCoreProp.getMyPermissionInfoUrl();
        UserPermissionInfoDto userPermissionInfoDto = null;
        if (StringUtils.isNotEmpty(myPermissionInfoUrl)) {
            UriComponentsBuilder myPermissionInfoUriBuilder = UriComponentsBuilder.fromUriString(myPermissionInfoUrl)
                    .queryParam("applicationId", this.appCoreProp.getApplicationId());
            String myPermissionInfoUriTemplate = myPermissionInfoUriBuilder.encode().toUriString();
            HttpHeaders httpHeaders = new HttpHeaders();
            prepareHttpHeaders(httpHeaders, tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<UserPermissionInfoDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                    myPermissionInfoUriTemplate, HttpMethod.GET
                    , httpEntity
//                    , new ParameterizedTypeReference<UserPermissionInfoDto>() {
//                    }
                    , UserPermissionInfoDto.class
                    );
            userPermissionInfoDto = responseEntity.getBody();
            log.debug("findPermissionsByLoginId - loginId: [{}], myPermissionInfoUriTemplate: [{}], userPermissionInfoDto.getLoginId: [{}], userPermissionDtoList.size: [{}]"
//                + ", responseEntity.getBody.toString: [{}]"
                    , loginId, myPermissionInfoUriTemplate, userPermissionInfoDto.getLoginId()
                    , userPermissionInfoDto.getUserPermissionList().size()
//                , responseEntity.getBody().toString()
            );
            userPermissionDtoList.addAll(userPermissionInfoDto.getUserPermissionList());
            log.debug("findPermissionsByLoginId - loginId: [{}], userPermissionDtoList: [{}]"
                    , loginId, userPermissionDtoList.size()
            );
            if (!loginId.equals(userPermissionInfoDto.getLoginId())) {
                throw new EntityNotFoundException(
                        "User does not match loginId: [%s], userPermissionInfoDto.getLoginId: [%s]"
                                .formatted(loginId, userPermissionInfoDto.getLoginId()));
            }
        } else {
            log.debug("findPermissionsByLoginId - loginId: [{}], myUserPermissionInfoUrl: [{}]"
                    , loginId, myPermissionInfoUrl
            );
            throw new RuntimeException(
                    "User with loginId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(loginId));
        }
        log.debug("findPermissionsByLoginId - end");
        return userPermissionDtoList;
    }

}
