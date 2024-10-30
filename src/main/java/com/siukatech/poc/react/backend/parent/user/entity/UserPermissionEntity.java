package com.siukatech.poc.react.backend.parent.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
//@EqualsAndHashCode(callSuper = true)
@Entity
public class UserPermissionEntity {

    @Id
    private String id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_role_id")
    private String userRoleId;

    @Column(name = "application_id")
    private String applicationId;

    @Column(name = "app_resource_id")
    private String appResourceId;

    @Column(name = "access_right")
    private String accessRight;

}
