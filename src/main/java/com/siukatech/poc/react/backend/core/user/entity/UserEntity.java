package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
//@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
public class UserEntity extends AbstractUserEntity {

    @ToString.Exclude
    @OneToMany(mappedBy = "userEntity", fetch = FetchType.EAGER)
    private List<UserRoleUserEntity> userRoleUserEntities;

}
