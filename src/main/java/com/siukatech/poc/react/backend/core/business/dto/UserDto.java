package com.siukatech.poc.react.backend.core.business.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
//@EqualsAndHashCode(callSuper = true)
public class UserDto implements Serializable {
    private String id;
    private String userId;
    private String name;
    private String publicKey;
//    private String privateKey;
    private String createdBy;
    private LocalDateTime createdDatetime;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDatetime;
}

