package com.siukatech.poc.react.backend.parent.business.service;

import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserPermissionDto;
import com.siukatech.poc.react.backend.parent.business.dto.UserViewDto;
import com.siukatech.poc.react.backend.parent.security.provider.database.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.security.provider.database.entity.UserPermissionEntity;
import com.siukatech.poc.react.backend.parent.security.provider.database.entity.UserViewEntity;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserViewRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
//@ContextConfiguration(classes = {WebConfig.class})
public class UserServiceTests extends AbstractUnitTests {

    @InjectMocks
    private UserService userService;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPermissionRepository userPermissionRepository;
    @Mock
    private UserViewRepository userViewRepository;

    @BeforeAll
    public static void init() {
//        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);

        // If sub-class has her own init, then super-class's init is required to trigger manually
        AbstractUnitTests.init();
    }

    @AfterAll()
    public static void terminate() {
        AbstractUnitTests.terminate();
    }

    @BeforeEach
    public void setup() {
//        // get Logback Logger
//        Logger log = (Logger) LoggerFactory.getLogger(this.getClass());
//
//        // create and start a ListAppender
//        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
//        listAppender.start();
//
//        // add the appender to the log
//        log.addAppender(listAppender);
//        final Logger log = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        log.setLevel(Level.ALL);

        log.debug("setup");
    }

    @AfterEach()
    public void teardown() {
        log.debug("teardown");
    }

    private List<UserPermissionEntity> prepareUserPermissions_basic() {
        String[][] userPermissionTempsArr = new String[][]{
                new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.home", "view"}
                , new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.items", "*"}
//                , new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.shops", "view"}
                , new String[]{"app-user-01", "1", "role-user-01", "frontend-app", "menu.merchants", "view"}
        };
        List<UserPermissionEntity> userPermissionEntityList = new ArrayList<>();
        for (String[] userPermissionTemps : userPermissionTempsArr) {
            UserPermissionEntity userPermissionEntity = new UserPermissionEntity();
            userPermissionEntity.setLoginId(userPermissionTemps[0]);
            userPermissionEntity.setUserId(Long.valueOf(userPermissionTemps[1]));
            userPermissionEntity.setUserRoleMid(userPermissionTemps[2]);
            userPermissionEntity.setAppMid(userPermissionTemps[3]);
            userPermissionEntity.setResourceMid(userPermissionTemps[4]);
            userPermissionEntity.setAccessRight(userPermissionTemps[5]);
            userPermissionEntityList.add(userPermissionEntity);
        }
        return userPermissionEntityList;
    }

    private UserEntity prepareUserEntity_basic() {
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId("app-user-01");
        userEntity.setName("App-User-01");
        userEntity.setPublicKey("public-key");
        userEntity.setPrivateKey("private-key");
        userEntity.setVersionNo(1L);
        return userEntity;
    }

    private UserViewEntity prepareUserViewEntity_basic() {
        UserViewEntity userViewEntity = new UserViewEntity();
        userViewEntity.setLoginId("app-user-01");
        userViewEntity.setName("App-User-01");
        userViewEntity.setPublicKey("public-key");
        userViewEntity.setPrivateKey("private-key");
        userViewEntity.setVersionNo(1L);
        return userViewEntity;
    }

    @Test
    public void findByLoginId_basic() {
        // given
        UserEntity userEntity = this.prepareUserEntity_basic();
        when(this.userRepository.findByLoginId(anyString())).thenReturn(Optional.of(userEntity));

        // when
        UserDto userDtoActual = this.userService.findByLoginId("app-user-01");

        // then / verify
        assertThat(userDtoActual.getLoginId()).isEqualTo("app-user-01");
    }

    @Test
    public void findUserPermissionByLoginIdAndAppMid_basic() {
        // given
        List<UserPermissionEntity> userPermissionEntityListTemp = prepareUserPermissions_basic();
        when(this.userPermissionRepository.findUserPermissionByLoginIdAndAppMid(anyString(), anyString())).thenReturn(userPermissionEntityListTemp);

        // when
        List<UserPermissionDto> userPermissionDtoListActual = this.userService.findPermissionsByLoginIdAndAppMid("app-user-01", "frontend-app");

        // then / verify
        assertThat(userPermissionDtoListActual.get(0).getLoginId()).isEqualTo("app-user-01");
        assertThat(userPermissionDtoListActual.get(0).getAppMid()).isEqualTo("frontend-app");
    }

    @Test
    public void findViewByLoginId_basic() {
        // given
        UserViewEntity userViewEntity = this.prepareUserViewEntity_basic();
        when(this.userViewRepository.findByLoginId(anyString())).thenReturn(Optional.of(userViewEntity));

        // when
        UserViewDto userViewDtoActual = this.userService.findViewByLoginId("app-user-01");

        // then / verify
        assertThat(userViewDtoActual.getLoginId()).isEqualTo("app-user-01");
    }

}
