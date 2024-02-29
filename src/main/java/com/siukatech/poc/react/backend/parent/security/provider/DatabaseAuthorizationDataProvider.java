package com.siukatech.poc.react.backend.parent.security.provider;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Service
public class DatabaseAuthorizationDataProvider implements AuthorizationDataProvider {

    private final UserService userService;

    public DatabaseAuthorizationDataProvider(UserService userService) {
        log.debug("constructor");
        this.userService = userService;
    }

    @Override
    public UserDto findByLoginIdAndTokenValue(String loginId, String tokenValue) {
        log.debug("findByLoginIdAndTokenValue - start");
        UserDto userDto = userService.findByLoginId(loginId);
        log.debug("findByLoginIdAndTokenValue - end");
        return userDto;
    }
}
