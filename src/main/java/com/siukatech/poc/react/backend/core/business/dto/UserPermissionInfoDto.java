package com.siukatech.poc.react.backend.core.business.dto;

import lombok.Data;

import java.util.List;

@Data
//@Builder  // Cannot be used for jackson.databind
public class UserPermissionInfoDto {
    private String loginId;
    private List<UserPermissionDto> userPermissionList;
}
