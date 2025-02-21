package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.business.dto.UserDossierDto;

public interface AuthorizationDataProvider {

//    UserDto findUserByUserIdAndTokenValue(String userId, String tokenValue);
//    List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String userId, String tokenValue);

    UserDossierDto findDossierByUserIdAndTokenValue(String userId, String tokenValue);

}
