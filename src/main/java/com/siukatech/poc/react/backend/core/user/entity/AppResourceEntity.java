package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity(name = "app_resources")
@NamedEntityGraph(name = "AppResourceEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "applicationEntity")
        , @NamedAttributeNode(value = "userRolePermissionEntities")
})
public class AppResourceEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

//    @Column(name = "access_right")
//    private String accessRight;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private ApplicationEntity applicationEntity;

    @ToString.Exclude
    @OneToMany(mappedBy = "appResourceEntity", fetch = FetchType.EAGER)
    private List<UserRolePermissionEntity> userRolePermissionEntities;

}
