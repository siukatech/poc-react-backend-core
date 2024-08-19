package com.siukatech.poc.react.backend.parent.security.provider;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
//@Service
public class DatabaseAuthorizationDataProvider implements AuthorizationDataProvider {

    private final UserService userService;
    private final ParentAppProp parentAppProp;

    public DatabaseAuthorizationDataProvider(UserService userService, ParentAppProp parentAppProp) {
        log.debug("constructor");
        this.userService = userService;
        this.parentAppProp = parentAppProp;
    }

    @Override
    public UserDto findByLoginIdAndTokenValue(String loginId, String tokenValue) {
        log.debug("findByLoginIdAndTokenValue - start");
        UserDto userDto = userService.findByLoginId(loginId);
        log.debug("findByLoginIdAndTokenValue - end");
        return userDto;
    }

    @Override
    public List<UserPermissionDto> findPermissionsByLoginId(String loginId, String tokenValue) {
        log.debug("findPermissionsByLoginId - start");
        List<UserPermissionDto> userPermissionDtoList = userService
                .findPermissionsByLoginIdAndAppMid(loginId, parentAppProp.getAppMid());
        log.debug("findPermissionsByLoginId - end");
        return userPermissionDtoList;
    }

}
