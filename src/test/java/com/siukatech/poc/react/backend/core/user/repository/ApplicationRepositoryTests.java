package com.siukatech.poc.react.backend.core.user.repository;

import com.siukatech.poc.react.backend.core.AbstractJpaTests;
import com.siukatech.poc.react.backend.core.user.entity.AppResourceEntity;
import com.siukatech.poc.react.backend.core.user.entity.ApplicationEntity;
import com.siukatech.poc.react.backend.core.user.entity.UserRolePermissionEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@Slf4j
@DataJpaTest
@TestPropertySource(properties = {
        "logging.level.org.hibernate.SQL=DEBUG"
        , "logging.level.org.hibernate.orm.jdbc.bind=TRACE"
        , "logging.level.com.siukatech.poc.react.backend.core.data.listener=INFO"
        , "logging.level.org.springframework.jdbc.datasource.init=INFO"
//        // Reference:
//        // https://spring.io/blog/2024/08/23/structured-logging-in-spring-boot-3-4
//        , "logging.structured.format.console=ecs"
})
public class ApplicationRepositoryTests extends AbstractJpaTests {

    @Autowired
    public ApplicationRepository applicationRepository;

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
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findWithGraphAttrPathById_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithGraphAttrPathById("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getId(), "frontend-app");

        log.debug("findWithGraphAttrPathById - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserRolePermissionEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserRolePermissionEntities().size()
        );
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findWithJpqlAppResourcesById_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithJpqlAppResourcesById("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getId(), "frontend-app");

        log.debug("findWithJpqlAppResourcesById - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserRolePermissionEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserRolePermissionEntities().size()
        );
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findWithJpqlUserRolePermissionEntitiesById_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithJpqlUserRolePermissionEntitiesById("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getId(), "frontend-app");

        log.debug("findWithJpqlUserRolePermissionEntitiesById - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserRolePermissionEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserRolePermissionEntities().size()
        );
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findWithAppResourceEntitiesById_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithAppResourceEntitiesById("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getId(), "frontend-app");

        log.debug("findWithAppResourceEntitiesById_basic - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserRolePermissionEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserRolePermissionEntities().size()
        );
//        if (applicationEntity.getAppResourceEntities() != null) {
//            applicationEntity.getAppResourceEntities().forEach(appResourceEntity -> {
//                log.debug("findWithAppResourceEntitiesById_basic - appResourceEntity.getId: [{}]"
//                        , appResourceEntity.getId());
//            });
//        }
        String appResourceEntityIdsStr = StringUtils.join(
                applicationEntity.getAppResourceEntities().stream()
                        .map(AppResourceEntity::getId).toList()
        );
        log.debug("findWithAppResourceEntitiesById_basic - appResourceEntityIdsStr: [{}]"
                , appResourceEntityIdsStr);
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/10-users/02-setup-view.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findWithUserRolePermissionEntitiesById_basic() {
        ApplicationEntity applicationEntity = applicationRepository.findWithUserRolePermissionEntitiesById("frontend-app")
                .orElseThrow(() -> new EntityNotFoundException("Application not found [%s]"));
        Assertions.assertEquals(applicationEntity.getId(), "frontend-app");

        log.debug("findWithUserRolePermissionEntitiesById_basic - applicationEntity.getAppResourceEntities.size: [{}]"
                        + ", applicationEntity.getUserRolePermissionEntities.size: [{}]"
                , applicationEntity.getAppResourceEntities().size()
                , applicationEntity.getUserRolePermissionEntities().size()
        );
        if (applicationEntity.getUserRolePermissionEntities() != null) {
//            applicationEntity.getUserRolePermissionEntities().forEach(userRolePermissionEntity -> {
//                log.debug("findWithUserRolePermissionEntitiesById_basic - userRolePermissionEntity.getId: [{}]"
//                        , userRolePermissionEntity.getId());
//            });
            String userRolePermissionEntityIdsStr = StringUtils.join(
                    applicationEntity.getUserRolePermissionEntities().stream()
                            .map(UserRolePermissionEntity::getId).toList()
            );
            log.debug("findWithUserRolePermissionEntitiesById_basic - userRolePermissionEntityIdsStr: [{}]"
                    , userRolePermissionEntityIdsStr);
        }
    }


}
