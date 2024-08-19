package com.siukatech.poc.react.backend.parent.data.repository;

import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.data.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.data.entity.UserPermissionEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@Slf4j
@DataJpaTest
public class UserPermissionRepositoryTests extends AbstractUnitTests {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserPermissionRepository userPermissionRepository;

    @BeforeEach
    public void setup(TestInfo testInfo) {
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
    }


    @Test
    @Sql(scripts = {
            "/scripts/users/01-setup.sql"
            , "/scripts/users/02-setup-view.sql"
//            , "/scripts/users/11-data-01-find-by-login-id.sql"
            , "/scripts/applications/01-setup.sql"
            , "/scripts/applications/11-data-01-find-all.sql"
            ,
            "/scripts/user-permissions/01-setup.sql"
            , "/scripts/user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findUserPermissionByLoginId_basic() {
        Optional<UserEntity> userEntityOptional = userRepository.findByLoginId("app-user-02");
        log.debug("findUserPermissionByLoginId_basic - userEntityOptional.get: [" + userEntityOptional.get() + "]");
        List<UserPermissionEntity> userPermissionEntityList = userPermissionRepository
                .findUserPermissionByLoginIdAndAppMid("app-user-02", "frontend-app");

        log.debug("findUserPermissionByLoginId_basic - userPermissionEntityList.size: [" + userPermissionEntityList.size()
                + "], userPermissionEntityList: [" + userPermissionEntityList
                + "]");
        Assertions.assertEquals(userPermissionEntityList.get(0).getLoginId(), "app-user-02");
        Assertions.assertEquals(userPermissionEntityList.get(0).getAppMid(), "frontend-app");
    }


}
