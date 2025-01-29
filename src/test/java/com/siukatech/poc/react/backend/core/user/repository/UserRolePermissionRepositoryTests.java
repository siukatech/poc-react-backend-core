package com.siukatech.poc.react.backend.core.user.repository;

import com.siukatech.poc.react.backend.core.AbstractJpaTests;
import com.siukatech.poc.react.backend.core.user.entity.ApplicationEntity;
import com.siukatech.poc.react.backend.core.user.entity.UserEntity;
import com.siukatech.poc.react.backend.core.user.entity.UserRolePermissionEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

@Slf4j
@DataJpaTest
// Reference:
// https://stackoverflow.com/a/1713464
// https://stackoverflow.com/a/74862954
@TestPropertySource(properties = {
        "logging.level.org.hibernate.SQL=DEBUG"
//        , "logging.level.org.hibernate.type=TRACE"
        , "logging.level.org.hibernate.orm.jdbc.bind=TRACE"
        , "logging.level.com.siukatech.poc.react.backend.core.data.listener=INFO"
        , "spring.h2.console.enabled=true"
})
public class UserRolePermissionRepositoryTests extends AbstractJpaTests {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ApplicationRepository applicationRepository;

    @Autowired
    public UserRolePermissionRepository userRolePermissionRepository;

    @BeforeEach
    public void setup(TestInfo testInfo) {
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
    }


    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
//            , "/scripts/users/11-data-01-find-by-login-id.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            ,
            "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginIdAndApplicationEntityId_basic() {
        Optional<UserEntity> userEntityOptional = userRepository.findByLoginId("app-user-02");
        Optional<ApplicationEntity> applicationEntityOptional = applicationRepository.findById("frontend-app");
        log.debug("findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginIdAndApplicationEntityId_basic - userEntityOptional.get: [{}], applicationEntityOptional: [{}]"
                , userEntityOptional.get(), applicationEntityOptional.get());
        List<UserRolePermissionEntity> userRolePermissionEntityList_LoginId = userRolePermissionRepository
                .findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginId("app-user-02");
        List<UserRolePermissionEntity> userRolePermissionEntityList_ApplicationEntityId = userRolePermissionRepository
                .findByApplicationEntityId("frontend-app");
        log.debug("findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginIdAndApplicationEntityId_basic - "
                        + "userRolePermissionEntityList_LoginId.size: [{}], userRolePermissionEntityList_ApplicationEntityId.size: [{}]"
                , userRolePermissionEntityList_LoginId.size(), userRolePermissionEntityList_ApplicationEntityId.size());
        List<UserRolePermissionEntity> userRolePermissionEntityList = userRolePermissionRepository
                .findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginIdAndApplicationEntityId("app-user-02", "frontend-app");

        log.debug("findByUserRoleEntityUserRoleUserEntitiesUserEntityLoginIdAndApplicationEntityId_basic - userPermissionEntityList.size: [" + userRolePermissionEntityList.size()
                + "], userPermissionEntityList: [" + userRolePermissionEntityList
                + "]");
        Assertions.assertEquals(userRolePermissionEntityList.get(0).getUserRoleEntity().getUserRoleUserEntities().get(0).getUserEntity().getLoginId(), "app-user-02");
        Assertions.assertEquals(userRolePermissionEntityList.get(0).getApplicationEntity().getId(), "frontend-app");
    }


}
