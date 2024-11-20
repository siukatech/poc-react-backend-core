package com.siukatech.poc.react.backend.core.user.entity;

import com.siukatech.poc.react.backend.core.data.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
//@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
public class UserEntity extends AbstractEntity<String> {

    @Column(name = "login_id")
    protected String loginId;
    @Column
    protected String name;
    @Column(name = "public_key")
    protected String publicKey;
    @Column(name = "private_key")
    protected String privateKey;

    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

}
