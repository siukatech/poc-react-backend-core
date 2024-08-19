package com.siukatech.poc.react.backend.parent.business.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserPermissionInfoDto {
    private String loginId;
    private List<UserPermissionDto> userPermissionList;
}
