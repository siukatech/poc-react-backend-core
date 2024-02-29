package com.siukatech.poc.react.backend.parent.security.provider;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;

public interface AuthorizationDataProvider {
    UserDto findByLoginIdAndTokenValue(String loginId, String tokenValue);

}
