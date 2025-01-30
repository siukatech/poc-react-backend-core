package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Add ToString.Exclude to prevent circular reference between ToString.
 *
 * Reference:
 * https://stackoverflow.com/a/54654008
 */
@Data
@Entity
@Table(name = "user_role_permissions")
@NamedEntityGraph(name = "UserRolePermissionEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "userRoleEntity")
        , @NamedAttributeNode(value = "applicationEntity")
        , @NamedAttributeNode(value = "appResourceEntity")
})
public class UserRolePermissionEntity extends AbstractEntity<String> {

    @Id
    private String id;

    @Column(name = "access_right")
    private String accessRight;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_role_id", referencedColumnName = "id")
    private UserRoleEntity userRoleEntity;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private ApplicationEntity applicationEntity;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "app_resource_id", referencedColumnName = "id")
    private AppResourceEntity appResourceEntity;

}
