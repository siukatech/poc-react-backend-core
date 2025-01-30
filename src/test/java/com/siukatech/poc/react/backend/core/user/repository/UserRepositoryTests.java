package com.siukatech.poc.react.backend.core.user.repository;

import com.siukatech.poc.react.backend.core.AbstractJpaTests;
import com.siukatech.poc.react.backend.core.user.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@DataJpaTest
//        (properties = {
////                "logging.level.org.springframework.web=TRACE"
////                , "logging.level.com.siukatech.poc.react.backend.core=TRACE"
//                "spring.jpa.show-sql: true"
//                , "spring.jpa.properties.hibernate.format_sql: true"
//                , "logging.level.org.springframework.data: TRACE"
//        })
////@TestPropertySource("classpath:application.yml")
@TestPropertySource(properties = {
        "logging.level.org.hibernate.SQL=DEBUG"
        , "logging.level.org.hibernate.orm.jdbc.bind=TRACE"
        , "logging.level.com.siukatech.poc.react.backend.core.data.listener=INFO"
})
public class UserRepositoryTests extends AbstractJpaTests {

    @Autowired
    public UserRepository userRepository;

    private UserEntity prepareUserEntity_basic() {
        UserEntity userEntity = new UserEntity();
        userEntity.setLoginId("app-user-01");
        userEntity.setName("App-User-01");
//        userEntity.setId(1L);
        userEntity.setId(UUID.randomUUID().toString());
        userEntity.setVersionNo(1L);
        userEntity.setPublicKey("public-key");
        userEntity.setPrivateKey("private-key");
        userEntity.setCreatedBy("admin");
        userEntity.setLastModifiedBy("admin");
        userEntity.setCreatedDatetime(LocalDateTime.now());
        userEntity.setLastModifiedDatetime(LocalDateTime.now());
        return userEntity;
    }

    /**
     * Reference:
     * https://stackoverflow.com/a/58623532
     */
    @BeforeEach
    public void setup(TestInfo testInfo) {
        Method method = testInfo.getTestMethod().get();
        log.debug("setup - testInfo: [" + testInfo
                + "], method.getName: [" + method.getName()
                + "]");
        UserEntity userEntity = null;
        switch (method.getName()) {
            case "findByLoginId_basic":
                userEntity = this.prepareUserEntity_basic();
            case "updateUser_version_updated":
                userEntity = this.prepareUserEntity_basic();
            case "updateUser_version_not_match":
                userEntity = this.prepareUserEntity_basic();
        }
        if (userEntity != null) {
            this.userRepository.save(userEntity);
        }
    }

    @AfterEach
    public void teardown(TestInfo testInfo) {
        Method method = testInfo.getTestMethod().get();
        String methodName = method.getName();
        log.debug("teardown - testInfo: [" + testInfo
                + "], methodName: [" + methodName
                + "]");
        List<String> methodNameList = List.of(
                "findByLoginId_basic"
                , "updateUser_version_updated"
                , "updateUser_version_not_match"
        );
        if (methodNameList.contains(methodName)) {
            Optional<UserEntity> userEntityOptional = this.userRepository.findByLoginId("app-user-01");
            userEntityOptional.ifPresent(userEntity -> this.userRepository.delete(userEntity));
        }
    }

    @Test
    @Tag("basic")
    public void findByLoginId_basic() {
        UserEntity userEntity = userRepository.findByLoginId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]"));
        Assertions.assertEquals(userEntity.getLoginId(), "app-user-01");
    }

    @Test
    @Sql(scripts = {
            "/scripts/00-prerequisite/01-setup.sql"
            , "/scripts/10-users/01-setup.sql"
            , "/scripts/20-applications/01-setup.sql"
            , "/scripts/20-applications/11-data-01-find-all.sql"
            , "/scripts/30-user-permissions/01-setup.sql"
            , "/scripts/30-user-permissions/11-data-01-find-by-login-id.sql"
    })
    public void findByLoginId_complex() {
        UserEntity userEntity = userRepository.findByLoginId("app-user-02")
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]"));
        Assertions.assertEquals(userEntity.getLoginId(), "app-user-02");
    }

    @Test
    @Tag("version_updated")
    public void updateUser_version_updated() {
        UserEntity userEntity = userRepository.findByLoginId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]"));
        userEntity.setName("App-User-01-version-updated");
        this.userRepository.save(userEntity);
        UserEntity userEntityAfterUpdate = userRepository.findByLoginId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]"));
        log.debug("updateUser_version_updated - userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
                + "]");
        Assertions.assertEquals(userEntity.getVersionNo(), 2L);
        Assertions.assertEquals(userEntityAfterUpdate.getVersionNo(), 2L);
        Assertions.assertEquals(userEntity.getLoginId(), "app-user-01");
    }

    @Test
    @Tag("version_not_match")
    public void updateUser_version_not_match() {
        UserEntity userEntity = userRepository.findByLoginId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]"));
        userEntity.setName("App-User-01-version_updated");
        this.userRepository.save(userEntity);

        UserEntity userEntityClone = new UserEntity();
        userEntityClone.setId(userEntity.getId());
        userEntityClone.setLoginId(userEntity.getLoginId());
        userEntityClone.setName(userEntity.getName());
        userEntityClone.setPublicKey(userEntity.getPublicKey());
        userEntityClone.setPrivateKey(userEntity.getPrivateKey());
        userEntityClone.setName("App-User-01-version_not_match");
        userEntityClone.setVersionNo(1L);

        UserEntity userEntityAfterUpdate = userRepository.findByLoginId("app-user-01")
                .orElseThrow(() -> new EntityNotFoundException("No such user [%s]"));
        log.debug("updateUser_version_not_match - 1 - userEntity.getId: [" + userEntity.getId()
                + "], userEntity.getName: [" + userEntity.getName()
                + "], userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getId: [" + userEntityAfterUpdate.getId()
                + "], userEntityAfterUpdate.getName: [" + userEntityAfterUpdate.getName()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
                + "], userEntityClone.getId: [" + userEntityClone.getId()
                + "], userEntityClone.getName: [" + userEntityClone.getName()
                + "], userEntityClone.getVersionNo: [" + userEntityClone.getVersionNo()
                + "]");

        Exception objectOptimisticLockingFailureException = Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            this.userRepository.save(userEntityClone);
        });
        log.debug("updateUser_version_not_match - 2 - userEntity.getId: [" + userEntity.getId()
                + "], userEntity.getName: [" + userEntity.getName()
                + "], userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getId: [" + userEntityAfterUpdate.getId()
                + "], userEntityAfterUpdate.getName: [" + userEntityAfterUpdate.getName()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
                + "], userEntityClone.getId: [" + userEntityClone.getId()
                + "], userEntityClone.getName: [" + userEntityClone.getName()
                + "], userEntityClone.getVersionNo: [" + userEntityClone.getVersionNo()
                + "], objectOptimisticLockingFailureException.getMessage: [" + (objectOptimisticLockingFailureException == null ? "NULL" : objectOptimisticLockingFailureException.getMessage())
                + "]");

        Assertions.assertNotNull(objectOptimisticLockingFailureException);
        Assertions.assertTrue(objectOptimisticLockingFailureException.getMessage().contains("Row was updated or deleted by another transaction"));
    }

}
