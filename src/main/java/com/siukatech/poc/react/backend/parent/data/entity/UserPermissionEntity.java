package com.siukatech.poc.react.backend.parent.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class UserPermissionEntity {

    @Id
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_role_mid")
    private String userRoleMid;

    @Column(name = "app_mid")
    private String appMid;

    @Column(name = "resource_mid")
    private String resourceMid;

    @Column(name = "access_right")
    private String accessRight;

}
