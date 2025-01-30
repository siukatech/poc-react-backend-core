package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Data
@Entity(name = "user_roles")
@NamedEntityGraph(name = "UserRoleEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "userRoleUserEntities"
                , subgraph = "UserRoleEntity.userRoleUserEntities")
    }
    , subgraphs = {
        @NamedSubgraph(name = "UserRoleEntity.userRoleUserEntities", attributeNodes = {
                @NamedAttributeNode(value = "userEntity"
                    , subgraph = "UserRoleEntity.userRoleUserEntities.userEntity"
                )
        })
    }
)
public class UserRoleEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "userRoleEntity", fetch = FetchType.EAGER)
    private List<UserRoleUserEntity> userRoleUserEntities;

    @ToString.Exclude
    @OneToMany(mappedBy = "userRoleEntity", fetch = FetchType.EAGER)
    private List<UserRolePermissionEntity> userRolePermissionEntities;

}
