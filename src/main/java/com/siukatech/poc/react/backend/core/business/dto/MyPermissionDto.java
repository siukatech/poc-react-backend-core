package com.siukatech.poc.react.backend.core.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPermissionDto implements Serializable {
    private String userId;
    List<UserPermissionDto> userPermissionList;
}
