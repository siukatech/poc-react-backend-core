package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Data
//@Entity(name = "applications")
@Entity
@Table(name = "applications")
@NamedEntityGraphs(value = {
    @NamedEntityGraph(name = "ApplicationEntity.appResourceEntities"
        , attributeNodes = {
            @NamedAttributeNode(value = "appResourceEntities")
//        , @NamedAttributeNode(value = "userRolePermissionEntities")
    })
    , @NamedEntityGraph(name = "ApplicationEntity.userRolePermissionEntities"
        , attributeNodes = {
            @NamedAttributeNode(value = "userRolePermissionEntities")
    })
})
public class ApplicationEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "applicationEntity"
//            , fetch = FetchType.EAGER
    )
//    @Fetch(FetchMode.SUBSELECT)
    private List<AppResourceEntity> appResourceEntities;

    @ToString.Exclude
    @OneToMany(mappedBy = "applicationEntity", fetch = FetchType.EAGER)
    private List<UserRolePermissionEntity> userRolePermissionEntities;

}
