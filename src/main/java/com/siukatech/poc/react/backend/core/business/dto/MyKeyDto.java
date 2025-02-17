package com.siukatech.poc.react.backend.core.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//@EqualsAndHashCode(callSuper = true)
public class MyKeyDto implements Serializable {
    private String userId;
    private String publicKey;
    private String privateKey;
}
