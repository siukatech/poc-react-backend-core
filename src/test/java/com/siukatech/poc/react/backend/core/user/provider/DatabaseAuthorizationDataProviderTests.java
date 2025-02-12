package com.siukatech.poc.react.backend.core.user.provider;

import com.siukatech.poc.react.backend.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.core.business.dto.UserDossierDto;
import com.siukatech.poc.react.backend.core.business.dto.UserDto;
import com.siukatech.poc.react.backend.core.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.core.user.entity.UserEntity;
import com.siukatech.poc.react.backend.core.user.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.core.global.helper.UserTestDataHelper;
import com.siukatech.poc.react.backend.core.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.core.user.repository.UserRepository;
import com.siukatech.poc.react.backend.core.user.repository.UserViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DatabaseAuthorizationDataProviderTests extends AbstractUnitTests {

    @InjectMocks
    private DatabaseAuthorizationDataProvider databaseAuthorizationDataProvider;
    @Mock
    private AppCoreProp appCoreProp;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPermissionRepository userPermissionRepository;
    @Mock
    private UserViewRepository userViewRepository;

    @Spy
    private UserTestDataHelper userTestDataHelper;


    @Test
    void contextLoads() {
        // TODO
        Assertions.assertNotNull(log);
        log.debug("contextLoads - testing logging");
    }

    @Test
    void test_findUserByUserIdAndTokenValue_basic() {
        // given
        UserEntity userEntity = this.userTestDataHelper.prepareUserEntity_basic(false);
        String targetUserId = userEntity.getUserId();
        String tokenValue = "tokenValue";
        //
        when(this.userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));

        // when
        UserDto userDto = this.databaseAuthorizationDataProvider
                .findUserByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
        assertThat(userDto.getUserId()).isEqualTo(userEntity.getUserId());
    }

    @Test
    void test_findPermissionsByUserIdAndTokenValue_basic() {
        // given
        UserEntity userEntity = this.userTestDataHelper.prepareUserEntity_basic(false);
        List<UserPermissionEntity> userPermissionEntityList = this.userTestDataHelper
                .prepareUserPermissionEntityList_basic(false);
        String targetUserId = userEntity.getUserId();
        String tokenValue = "tokenValue";
        String applicationId = "applicationId";
        //
        when(this.appCoreProp.getApplicationId()).thenReturn(applicationId);
        when(this.userPermissionRepository.findByUserIdAndApplicationId(anyString()
//                , isNull()
                , anyString()
        )).thenReturn(userPermissionEntityList);

        // when
        List<UserPermissionDto> userPermissionDtoList = this.databaseAuthorizationDataProvider
                .findPermissionsByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
        assertThat(userPermissionDtoList.size()).isEqualTo(userPermissionEntityList.size());
    }

    @Test
    void test_findDossierByUserIdAndTokenValue_basic() {
        // given
        UserEntity userEntity = this.userTestDataHelper.prepareUserEntity_basic(false);
        List<UserPermissionEntity> userPermissionEntityList = this.userTestDataHelper
                .prepareUserPermissionEntityList_basic(false);
        String targetUserId = userEntity.getUserId();
        String tokenValue = "tokenValue";
        String applicationId = "applicationId";
        //
        when(this.appCoreProp.getApplicationId()).thenReturn(applicationId);
        when(this.userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));
        when(this.userPermissionRepository.findByUserIdAndApplicationId(anyString()
//                , isNull()
                , anyString()
        )).thenReturn(userPermissionEntityList);

        // when
        UserDossierDto userDossierDto = this.databaseAuthorizationDataProvider
                .findDossierByUserIdAndTokenValue(targetUserId, tokenValue);

        // then
        assertThat(userDossierDto.getUserPermissionList().size())
                .isEqualTo(userPermissionEntityList.size());

    }

}
