package com.siukatech.poc.react.backend.parent.business.service;

import com.siukatech.poc.react.backend.parent.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.data.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.data.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.parent.data.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.parent.data.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;

    private UserService(ModelMapper modelMapper
            , UserRepository userRepository
            , UserPermissionRepository userPermissionRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
    }

    public UserDto findByLoginId(String targetLoginId) {
        UserEntity userEntity = this.userRepository.findByLoginId(targetLoginId)
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]".formatted(targetLoginId)));
        logger.debug("findByLoginId - modelMapper: [" + this.modelMapper + "]");
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        return userDto;
    }

    public MyKeyDto findKeyByLoginId(String targetLoginId) {
        UserEntity userEntity = this.userRepository.findByLoginId(targetLoginId)
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]".formatted(targetLoginId)));
        logger.debug("findKeyByLoginId - modelMapper: [" + this.modelMapper + "]");
        MyKeyDto myKeyDto = this.modelMapper.map(userEntity, MyKeyDto.class);
        return myKeyDto;
    }

    public List<UserPermissionDto> findPermissionsByLoginId(String targetLoginId) {
//        List<UserPermissionDto> userPermissionDtoList = this.userRepository.findUserPermissionByLoginId(targetLoginId);
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository.findUserPermissionByLoginId(targetLoginId);
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        return userPermissionDtoList;
    }

}
