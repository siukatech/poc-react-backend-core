package com.siukatech.poc.react.backend.parent.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
//@EqualsAndHashCode(callSuper = true)
@Entity(name = "users_view")
public class UserViewEntity extends AbstractEntity<Long> {

    @Column(name = "login_id")
    protected String loginId;
    @Column
    protected String name;
    @Column(name = "public_key")
    protected String publicKey;
    @Column(name = "private_key")
    protected String privateKey;
    @Column(name = "current_timestamp")
    protected LocalDateTime dbDatetime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

}
