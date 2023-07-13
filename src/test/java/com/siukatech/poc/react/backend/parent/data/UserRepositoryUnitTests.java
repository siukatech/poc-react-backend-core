package com.siukatech.poc.react.backend.parent.data;

import com.siukatech.poc.react.backend.parent.AbstractJpaTests;
import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.data.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.data.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.lang.reflect.Method;

@DataJpaTest
//        (properties = {
////                "logging.level.org.springframework.web=TRACE"
////                , "logging.level.com.siukatech.poc.react.backend.parent=TRACE"
//                "spring.jpa.show-sql: true"
//                , "spring.jpa.properties.hibernate.format_sql: true"
//                , "logging.level.org.springframework.data: TRACE"
//        })
////@TestPropertySource("classpath:application.yml")
public class UserRepositoryUnitTests extends AbstractJpaTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserRepository userRepository;

    private UserEntity prepareUserEntity_basic() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("app-user-01");
        userEntity.setName("App-User-01");
        userEntity.setId(1L);
        userEntity.setVersionNo(1L);
        userEntity.setPublicKey("public-key");
        userEntity.setPrivateKey("private-key");
        return userEntity;
    }

    /**
     * Reference
     * https://stackoverflow.com/a/58623532
     */
    @BeforeEach()
    public void setup(TestInfo testInfo) {
        Method method = testInfo.getTestMethod().get();
        logger.debug("setup - testInfo: [" + testInfo
                + "], method: [" + method.getName()
                + "]");
        UserEntity userEntity = null;
        switch (method.getName()) {
            case "findByUserId_basic":
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
        logger.debug("teardown - testInfo: [" + testInfo
                + "], method: [" + method.getName()
                + "]");
        UserEntity userEntity = userEntity = this.userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new RuntimeException());
        switch (method.getName()) {
            case "findByUserId_basic":
            case "updateUser_version_updated":
            case "updateUser_version_not_match":
        }
        if (userEntity != null) {
            this.userRepository.delete(userEntity);
        }
    }

    @Test
    @Tag("basic")
    public void findByUserId_basic() {
        UserEntity userEntity = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new RuntimeException());
        Assertions.assertEquals(userEntity.getUserId(), "app-user-01");
    }

    @Test
    @Tag("version_updated")
    public void updateUser_version_updated() {
        UserEntity userEntity = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new RuntimeException());
        userEntity.setName("App-User-01-version-updated");
        this.userRepository.save(userEntity);
        UserEntity userEntityAfterUpdate = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new RuntimeException());
        logger.debug("updateUser_version_updated - userEntity.getVersionNo: [" + userEntity.getVersionNo()
                + "], userEntityAfterUpdate.getVersionNo: [" + userEntityAfterUpdate.getVersionNo()
                + "]");
        Assertions.assertEquals(userEntity.getVersionNo(), 2L);
        Assertions.assertEquals(userEntityAfterUpdate.getVersionNo(), 2L);
        Assertions.assertEquals(userEntity.getUserId(), "app-user-01");
    }

    @Test
    @Tag("version_not_match")
    public void updateUser_version_not_match() {
        UserEntity userEntity = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new RuntimeException());
        userEntity.setName("App-User-01-version_updated");
        this.userRepository.save(userEntity);

        UserEntity userEntityClone = new UserEntity();
        userEntityClone.setId(userEntity.getId());
        userEntityClone.setUserId(userEntity.getUserId());
        userEntityClone.setName(userEntity.getName());
        userEntityClone.setPublicKey(userEntity.getPublicKey());
        userEntityClone.setPrivateKey(userEntity.getPrivateKey());
        userEntityClone.setName("App-User-01-version_not_match");
        userEntityClone.setVersionNo(1L);

        UserEntity userEntityAfterUpdate = userRepository.findByUserId("app-user-01")
                .orElseThrow(() -> new RuntimeException());
        logger.debug("updateUser_version_not_match - 1 - userEntity.getId: [" + userEntity.getId()
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
        logger.debug("updateUser_version_not_match - 2 - userEntity.getId: [" + userEntity.getId()
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
