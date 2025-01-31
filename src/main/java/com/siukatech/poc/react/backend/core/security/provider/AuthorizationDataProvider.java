package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;

import java.util.List;

public interface AuthorizationDataProvider {
    UserDto findByUserIdAndTokenValue(String userId, String tokenValue);
    List<UserPermissionDto> findPermissionsByUserId(String userId, String tokenValue);

}
