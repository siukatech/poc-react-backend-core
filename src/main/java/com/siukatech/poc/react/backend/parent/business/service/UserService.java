package com.siukatech.poc.react.backend.parent.business.service;

import com.siukatech.poc.react.backend.parent.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserViewDto;
import com.siukatech.poc.react.backend.parent.data.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.data.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.parent.data.entity.UserViewEntity;
import com.siukatech.poc.react.backend.parent.data.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.parent.data.repository.UserRepository;
import com.siukatech.poc.react.backend.parent.data.repository.UserViewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserViewRepository userViewRepository;

    private UserService(ModelMapper modelMapper
            , UserRepository userRepository
            , UserPermissionRepository userPermissionRepository, UserViewRepository userViewRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userViewRepository = userViewRepository;
    }

//    public UserDto findByLoginId(String targetLoginId) {
//        UserEntity userEntity = this.userRepository.findByLoginId(targetLoginId)
//                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]".formatted(targetLoginId)));
//        log.debug("findByLoginId - modelMapper: [" + this.modelMapper + "]");
//        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
//        userDto.setCurrentDatetime(LocalDateTime.now());
//        return userDto;
//    }

    public UserDto findByLoginId(String targetLoginId) {
        UserEntity userEntity = this.userRepository.findByLoginId(targetLoginId)
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]".formatted(targetLoginId)));
        log.debug("findByLoginId - modelMapper: [" + this.modelMapper + "]");
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        return userDto;
    }

    public MyKeyDto findKeyByLoginId(String targetLoginId) {
        UserEntity userEntity = this.userRepository.findByLoginId(targetLoginId)
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]".formatted(targetLoginId)));
        log.debug("findKeyByLoginId - modelMapper: [" + this.modelMapper + "]");
        MyKeyDto myKeyDto = this.modelMapper.map(userEntity, MyKeyDto.class);
        return myKeyDto;
    }

    public List<UserPermissionDto> findPermissionsByLoginIdAndAppMid(String targetLoginId, String appMid) {
//        List<UserPermissionDto> userPermissionDtoList = this.userRepository.findUserPermissionByLoginId(targetLoginId);
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository.findUserPermissionByLoginIdAndAppMid(targetLoginId, appMid);
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        return userPermissionDtoList;
    }

    public UserViewDto findViewByLoginId(String targetLoginId) {
        UserViewEntity userViewEntity = this.userViewRepository.findByLoginId(targetLoginId)
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]".formatted(targetLoginId)));
//        log.debug("findViewByLoginId - modelMapper: [" + this.modelMapper + "]");
        UserViewDto userViewDto = this.modelMapper.map(userViewEntity, UserViewDto.class);
        userViewDto.setAppDatetime(LocalDateTime.now());
        userViewDto.setTimeZone(TimeZone.getDefault());
        log.debug("findViewByLoginId - targetLoginId: [{}], userViewDto: [{}]", targetLoginId, userViewDto);
        return userViewDto;
    }

}
