package com.siukatech.poc.react.backend.parent.business.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper=false)
public class UserDto {
    private String userId;
    private String name;
    private String publicKey;
    private String privateKey;
    private String createdBy;
    private LocalDateTime createdDatetime;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDatetime;
}

