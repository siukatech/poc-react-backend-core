package com.siukatech.poc.react.backend.core.user.mapper;

import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.core.business.dto.UserViewDto;
import com.siukatech.poc.react.backend.core.business.mapper.AbstractMapper;
import com.siukatech.poc.react.backend.core.user.entity.UserPermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserPermissionMapper extends AbstractMapper {

    public UserPermissionMapper INSTANCE = Mappers.getMapper(UserPermissionMapper.class);

    UserPermissionDto convertEntityToDto(UserPermissionEntity userPermissionEntity);

}
