package com.siukatech.poc.react.backend.parent.security.provider.database.entity;

import com.siukatech.poc.react.backend.parent.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
public class UserEntity extends AbstractEntity<Long> {

    @Column(name = "login_id")
    protected String loginId;
    @Column
    protected String name;
    @Column(name = "public_key")
    protected String publicKey;
    @Column(name = "private_key")
    protected String privateKey;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

}
