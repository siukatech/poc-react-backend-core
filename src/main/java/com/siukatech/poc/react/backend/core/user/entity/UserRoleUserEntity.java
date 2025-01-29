package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity(name = "user_role_users")
public class UserRoleUserEntity extends AbstractEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_role_id", referencedColumnName = "id")
    private UserRoleEntity userRoleEntity;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

}
