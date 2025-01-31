package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
//@EqualsAndHashCode(callSuper = true)
//@Entity(name = "users_view")
@Entity
@Table(name = "users_view")
public class UserViewEntity extends AbstractUserEntity {

    @Column(name = "current_ts")
    private LocalDateTime dbDatetime;

}
