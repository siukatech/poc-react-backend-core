package com.siukatech.poc.react.backend.core.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder  // Cannot be used for jackson.databind
public class UserDossierDto implements Serializable {
    private UserDto userDto;
    private MyKeyDto myKeyDto;
    private List<UserPermissionDto> userPermissionList;
}
