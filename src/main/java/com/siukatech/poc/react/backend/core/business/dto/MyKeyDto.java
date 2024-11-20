package com.siukatech.poc.react.backend.core.business.dto;

import lombok.Data;

@Data
//@EqualsAndHashCode(callSuper = true)
public class MyKeyDto {
    private String loginId;
    private String publicKey;
    private String privateKey;
}
