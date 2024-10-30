package com.siukatech.poc.react.backend.parent.business.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
//@Builder  // Cannot be used for jackson.databind
public class UserPermissionInfoDto {
    private String loginId;
    private List<UserPermissionDto> userPermissionList;
}
