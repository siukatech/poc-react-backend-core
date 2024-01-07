package com.siukatech.poc.react.backend.parent.global.config;

import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Reference:
 * https://stackoverflow.com/a/26840503
 *
 * @Test only supports ".properties" files only.
 *
 * @ConfigDataApplicationContextInitializer.class is the replacement of @ConfigFileApplicationContextInitializer.class.
 * @ConfigDataApplicationContextInitializer.class activates regular Spring Boot loading sequence.
 * As a result, yml in "main/resources" will be loaded instead of "test/resources"
 *
 * NOT recommended.
 *
 */
@Slf4j
@ExtendWith(SpringExtension.class)
//@EnableConfigurationProperties(value = ParentAppProp.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = ParentAppProp.class)
//@ContextConfiguration(classes = ParentAppProp.class, initializers = ConfigDataApplicationContextInitializer.class)
//@TestPropertySource("classpath:global/parent-app-config-tests.yml")
@TestPropertySource("classpath:global/parent-app-config-tests.properties")
//@TestPropertySource(properties = {"spring.config.location = classpath:global/parent-app-config-tests.properties"})
//@TestPropertySource(locations= {"classpath:global/parent-app-config-tests.yml"})
public class ParentAppPropTests extends AbstractUnitTests {

    @Autowired
    private ParentAppProp parentAppProp;

    @BeforeAll
    public static void init() {
//        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);

        AbstractUnitTests.init();
    }

    @Test
    public void parentAppProp_basic() {
        log.debug("parentAppProp_basic - hostName: [{}]"
                        + ", myUserInfo: [{}]"
                        + ", myKeyInfo: [{}]"
                , parentAppProp.getHostName()
                , parentAppProp.getApi().getMyUserInfo()
                , parentAppProp.getApi().getMyKeyInfo()
        );
        assertThat(parentAppProp.getHostName()).isEqualTo("http://localhost:28080");
        assertThat(parentAppProp.getApi().getMyUserInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
        assertThat(parentAppProp.getApi().getMyUserInfo()).contains("/my/user-info");
//        assertThat(parentAppProp.getApp().getHostName()).isEqualTo("http://localhost:28080");
//        assertThat(parentAppProp.getApi().getMyUserInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
//        assertThat(parentAppProp.getApi().getMyUserInfo()).contains("/my/user-info");
        assertThat(parentAppProp.getApi().getMyKeyInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
        assertThat(parentAppProp.getApi().getMyKeyInfo()).contains("/my/key-info");
    }

}
