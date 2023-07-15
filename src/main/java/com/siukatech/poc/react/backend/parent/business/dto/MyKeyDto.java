package com.siukatech.poc.react.backend.parent.business.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MyKeyDto {
    private String userId;
    private String publicKey;
    private String privateKey;
}
