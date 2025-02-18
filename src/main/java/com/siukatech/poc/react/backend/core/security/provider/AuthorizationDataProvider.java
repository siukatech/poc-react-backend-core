package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.business.dto.UserDossierDto;

public interface AuthorizationDataProvider {

    String CACHE_KEY_findUserByUserIdAndTokenValue
            = "AuthorizationDataProvider.findUserByUserIdAndTokenValue_";
    String CACHE_KEY_findPermissionsByUserIdAndTokenValue
            = "AuthorizationDataProvider.findPermissionsByUserIdAndTokenValue_";
    String CACHE_KEY_findDossierByUserIdAndTokenValue
            = "AuthorizationDataProvider.findDossierByUserIdAndTokenValue_";

//    UserDto findUserByUserIdAndTokenValue(String userId, String tokenValue);
//    List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String userId, String tokenValue);

    UserDossierDto findDossierByUserIdAndTokenValue(String userId, String tokenValue);

}
