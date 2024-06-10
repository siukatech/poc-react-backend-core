package com.siukatech.poc.react.backend.parent.business.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
//@EqualsAndHashCode(callSuper = true)
public class MyKeyDto {
    private String loginId;
    private String publicKey;
    private String privateKey;
}
