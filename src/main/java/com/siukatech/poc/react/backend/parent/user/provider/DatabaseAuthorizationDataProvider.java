package com.siukatech.poc.react.backend.parent.user.provider;

import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.user.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.parent.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.parent.user.repository.UserRepository;
import com.siukatech.poc.react.backend.parent.user.repository.UserViewRepository;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@Slf4j
@EntityScan(basePackages = {"com.siukatech.poc.react.backend.parent.security.provider.database.entity"})  // "**" means all packages
@EnableJpaRepositories("com.siukatech.poc.react.backend.parent.security.provider.database.repository")    // "**" means all packages
//@Service
public class DatabaseAuthorizationDataProvider implements AuthorizationDataProvider {

    private final ParentAppProp parentAppProp;
    private final ModelMapper modelMapper;
//    private final UserService userService;
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    private final UserViewRepository userViewRepository;

    public DatabaseAuthorizationDataProvider(
            ParentAppProp parentAppProp,
            ModelMapper modelMapper,
//            UserService userService
            UserRepository userRepository,
            UserPermissionRepository userPermissionRepository,
            UserViewRepository userViewRepository
    ) {
        log.debug("constructor");
        this.parentAppProp = parentAppProp;
        this.modelMapper = modelMapper;
//        this.userService = userService;
        this.userRepository = userRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.userViewRepository = userViewRepository;
    }

    @Override
    public UserDto findByLoginIdAndTokenValue(String targetLoginId, String tokenValue) {
        log.debug("findByLoginIdAndTokenValue - start");
//        UserDto userDto = userService.findByLoginId(targetLoginId);
        UserEntity userEntity = this.userRepository.findByLoginId(targetLoginId)
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]".formatted(targetLoginId)));
        log.debug("findByLoginId - modelMapper: [" + this.modelMapper + "]");
        UserDto userDto = this.modelMapper.map(userEntity, UserDto.class);
        log.debug("findByLoginIdAndTokenValue - end");
        return userDto;
    }

    @Override
    public List<UserPermissionDto> findPermissionsByLoginId(String targetLoginId, String tokenValue) {
        log.debug("findPermissionsByLoginId - start");
//        List<UserPermissionDto> userPermissionDtoList = userService
//                .findPermissionsByLoginIdAndApplicationId(loginId, parentAppProp.getApplicationId());
        List<UserPermissionEntity> userPermissionEntityList = this.userPermissionRepository.findUserPermissionByLoginIdAndApplicationId(targetLoginId, parentAppProp.getApplicationId());
        List<UserPermissionDto> userPermissionDtoList = userPermissionEntityList.stream()
                .map(userPermissionEntity -> this.modelMapper
                        .map(userPermissionEntity, UserPermissionDto.class))
                .toList();
        log.debug("findPermissionsByLoginId - end");
        return userPermissionDtoList;
    }

}
