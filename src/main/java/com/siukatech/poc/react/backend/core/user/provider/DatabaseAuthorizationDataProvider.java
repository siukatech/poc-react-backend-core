package com.siukatech.poc.react.backend.core.user.provider;

import com.siukatech.poc.react.backend.core.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.core.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.core.user.entity.UserEntity;
import com.siukatech.poc.react.backend.core.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.core.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.core.user.repository.UserRepository;
import com.siukatech.poc.react.backend.core.user.repository.UserViewRepository;
import com.siukatech.poc.react.backend.core.global.config.AppCoreProp;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@Slf4j
@EntityScan(basePackages = {"com.siukatech.poc.react.backend.core.security.provider.database.entity"})  // "**" means all packages
@EnableJpaRepositories("com.siukatech.poc.react.backend.core.security.provider.database.repository")    // "**" means all packages
//@Service
public class DatabaseAuthorizationDataProvider implements AuthorizationDataProvider {

    private final AppCoreProp appCoreProp;
    private final ModelMapper modelMapper;
//    private final UserService userService;
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserViewRepository userViewRepository;

    public DatabaseAuthorizationDataProvider(
            AppCoreProp appCoreProp,
            ModelMapper modelMapper,
//            UserService userService
            UserRepository userRepository,
            UserPermissionRepository userPermissionRepository,
            UserViewRepository userViewRepository
    ) {
        log.debug("constructor");
        this.appCoreProp = appCoreProp;
        this.modelMapper = modelMapper;
//        this.userService = userService;
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userViewRepository = userViewRepository;
    }

    @Override
    public UserDto findUserByUserIdAndTokenValue(String targetUserId, String tokenValue) {
        log.debug("findByUserIdAndTokenValue - start");
//        UserDto userDto = userService.findByUserId(targetUserId);
        UserEntity userEntity = this.userRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(targetUserId)));
        log.debug("findByUserIdAndTokenValue - modelMapper: [" + this.modelMapper + "]");
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        log.debug("findByUserIdAndTokenValue - end");
        return userDto;
    }

    @Override
    public List<UserPermissionDto> findPermissionsByUserIdAndTokenValue(String targetUserId, String tokenValue) {
        log.debug("findPermissionsByUserIdAndTokenValue - start");
//        List<UserPermissionDto> userPermissionDtoList = userService
//                .findPermissionsByUserIdAndApplicationId(userId, appCoreProp.getApplicationId());
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository
                .findByUserIdAndApplicationId(targetUserId, appCoreProp.getApplicationId());
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        log.debug("findPermissionsByUserIdAndTokenValue - end");
        return userPermissionDtoList;
    }

    @Override
    public UserDossierDto findDossierByUserIdAndTokenValue(String targetUserId, String tokenValue) {
        UserEntity userEntity = this.userRepository.findByUserId(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found [%s]".formatted(targetUserId)));
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        MyKeyDto myKeyDto = this.modelMapper.map(userEntity, MyKeyDto.class);
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository
                .findByUserIdAndApplicationId(targetUserId, appCoreProp.getApplicationId());
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        UserDossierDto userDossierDto = new UserDossierDto(userDto, myKeyDto, userPermissionDtoList);
        return null;
    }

}
