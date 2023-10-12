package com.siukatech.poc.react.backend.parent.business.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserPermissionDto {
    private String loginId;
    private Long userId;
    private String userRoleMid;
    private String appMid;
    private String resourceMid;
    private String accessRight;

}
