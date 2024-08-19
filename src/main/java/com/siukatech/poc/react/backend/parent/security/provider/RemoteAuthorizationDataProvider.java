package com.siukatech.poc.react.backend.parent.security.provider;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionInfoDto;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
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

    private final ParentAppProp parentAppProp;

    public RemoteAuthorizationDataProvider(
            RestTemplate oauth2ClientRestTemplate
            , ParentAppProp parentAppProp) {
        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
        this.parentAppProp = parentAppProp;
    }

    @Override
    public UserDto findByLoginIdAndTokenValue(String loginId, String tokenValue) {
        log.debug("findByLoginId - start");
        String myUserInfoUrl = this.parentAppProp.getMyUserInfoUrl();
        UserDto userDto = null;
        if (StringUtils.isNotEmpty(myUserInfoUrl)) {
            // Special handling of adding token to oauth2ClientRestTemplate
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<UserDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                    myUserInfoUrl, HttpMethod.GET
//                    , HttpEntity.EMPTY
                    , httpEntity
                    , UserDto.class);
            userDto = responseEntity.getBody();
            log.debug("findByLoginId - loginId: [{}], myUserInfoUrl: [{}], userDto.getLoginId: [{}]"
//                + ", responseEntity.getBody.toString: [{}]"
                    , loginId, myUserInfoUrl, userDto.getLoginId()
//                , responseEntity.getBody().toString()
            );
            if (!loginId.equals(userDto.getLoginId())) {
                throw new EntityNotFoundException(
                        "User does not match loginId: [%s], userDto.getLoginId: [%s]"
                                .formatted(loginId, userDto.getLoginId()));
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
        String myUserPermissionInfoUrl = this.parentAppProp.getMyUserPermissionInfoUrl();
        UserPermissionInfoDto userPermissionInfoDto = null;
        if (StringUtils.isNotEmpty(myUserPermissionInfoUrl)) {
            UriComponentsBuilder myUserPermissionInfoUriBuilder = UriComponentsBuilder.fromUriString(myUserPermissionInfoUrl)
                    .queryParam("appMid", this.parentAppProp.getAppMid())
                    ;
            String myUserPermissionInfoUriTemplate = myUserPermissionInfoUriBuilder.encode().toUriString();
            // Special handling of adding token to oauth2ClientRestTemplate
            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
            HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
            ResponseEntity<UserPermissionInfoDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                    myUserPermissionInfoUriTemplate, HttpMethod.GET
                    , httpEntity
                    , new ParameterizedTypeReference<UserPermissionInfoDto>() {});
            userPermissionInfoDto = responseEntity.getBody();
            log.debug("findPermissionsByLoginId - loginId: [{}], myUserPermissionInfoUriTemplate: [{}], userPermissionInfoDto.getLoginId: [{}], userPermissionDtoList.size: [{}]"
//                + ", responseEntity.getBody.toString: [{}]"
                    , loginId, myUserPermissionInfoUriTemplate, userPermissionInfoDto.getLoginId()
                    , userPermissionInfoDto.getUserPermissionList().size()
//                , responseEntity.getBody().toString()
            );
            if (!loginId.equals(userPermissionInfoDto.getLoginId())) {
                throw new EntityNotFoundException(
                        "User does not match loginId: [%s], userPermissionInfoDto.getLoginId: [%s]"
                                .formatted(loginId, userPermissionInfoDto.getLoginId()));
            }
        } else {
            log.debug("findPermissionsByLoginId - loginId: [{}], myUserPermissionInfoUrl: [{}]"
                    , loginId, myUserPermissionInfoUrl
            );
            throw new RuntimeException(
                    "User with loginId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(loginId));
        }
        log.debug("findPermissionsByLoginId - end");
        return userPermissionDtoList;
    }

}
