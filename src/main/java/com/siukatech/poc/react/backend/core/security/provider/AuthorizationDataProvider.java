package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;

import java.util.List;

public interface AuthorizationDataProvider {
    UserDto findByLoginIdAndTokenValue(String loginId, String tokenValue);
    List<UserPermissionDto> findPermissionsByLoginId(String loginId, String tokenValue);

}
