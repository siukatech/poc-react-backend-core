package com.siukatech.poc.react.backend.core.caching.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {

    private String id;
    private String location;
    private String street;
    private String district;

}
