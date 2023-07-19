package com.siukatech.poc.react.backend.parent.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity(name = "users")
public class UserEntity extends AbstractEntity {

    @Column(name = "login_id")
    protected String loginId;
    @Column
    protected String name;
    @Column(name = "public_key")
    protected String publicKey;
    @Column(name = "private_key")
    protected String privateKey;

}
