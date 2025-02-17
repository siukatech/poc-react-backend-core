package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;

import java.util.List;

public interface AuthorizationDataProvider {

    String CACHE_KEY_USER_DTO = "CACHE_KEY_USER_DTO_";
    String CACHE_KEY_USER_PERMISSION_DTO = "CACHE_KEY_USER_PERMISSION_DTO_";
    String CACHE_KEY_USER_DOSSIER_DTO = "CACHE_KEY_USER_DOSSIER_DTO_";

//    UserDto findUserByUserIdAndTokenValue(String userId, String tokenValue);
//    List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String userId, String tokenValue);

    UserDossierDto findDossierByUserIdAndTokenValue(String userId, String tokenValue);

}
