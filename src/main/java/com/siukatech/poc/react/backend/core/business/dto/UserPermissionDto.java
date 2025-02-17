package com.siukatech.poc.react.backend.core.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//@EqualsAndHashCode(callSuper = true)
public class UserPermissionDto implements Serializable {
    private String userId;
    private String userRoleId;
    private String applicationId;
    private String appResourceId;
    private String accessRight;

}
