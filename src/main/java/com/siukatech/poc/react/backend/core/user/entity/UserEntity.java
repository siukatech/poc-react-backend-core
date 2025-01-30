package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;


@Data
//@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@NamedEntityGraph(name = "UserEntity.basic"
    , attributeNodes = {
        @NamedAttributeNode(value = "userRoleUserEntities"
            , subgraph = "UserEntity.userRoleUserEntities")
    }
    , subgraphs = {
        @NamedSubgraph(name = "UserEntity.userRoleUserEntities", attributeNodes = {
                @NamedAttributeNode(value = "userRoleEntity"
                        , subgraph = "UserEntity.userRoleUserEntities.userRoleEntity"
                )
        })
////        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity"
////            , attributeNodes = {
////                @NamedAttributeNode(value = "userRolePermissionEntities")
////        })
//        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity"
//            , attributeNodes = {
//                @NamedAttributeNode(value = "userRolePermissionEntities"
//                    , subgraph = "UserEntity.userRoleUserEntities.userRoleEntity.userRolePermissionEntities")
//        })
//        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity.userRolePermissionEntities"
//            , attributeNodes = {
//                @NamedAttributeNode(value = "applicationEntity"
//                    , subgraph = "UserEntity.userRoleUserEntities.userRoleEntity.userRolePermissionEntities.applicationEntity")
//                , @NamedAttributeNode(value = "appResourceEntity")
//        })
//        , @NamedSubgraph(name = "UserEntity.userRoleUserEntities.userRoleEntity.userRolePermissionEntities.applicationEntity"
//            , attributeNodes = {
//                @NamedAttributeNode(value = "appResourceEntities")
//        })
    }
)
public class UserEntity extends AbstractUserEntity {

    @ToString.Exclude
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.EAGER)
    private List<UserRoleUserEntity> userRoleUserEntities;

}
