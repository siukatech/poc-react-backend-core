package com.siukatech.poc.react.backend.parent.global.config;

import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppConfig;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
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
@ExtendWith(SpringExtension.class)
//@EnableConfigurationProperties(value = ParentAppConfig.class)
@EnableConfigurationProperties
@ContextConfiguration(classes = ParentAppConfig.class)
//@ContextConfiguration(classes = ParentAppConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
//@TestPropertySource("classpath:global/parent-app-config-tests.yml")
@TestPropertySource("classpath:global/parent-app-config-tests.properties")
//@TestPropertySource(properties = {"spring.config.location = classpath:global/parent-app-config-tests.properties"})
//@TestPropertySource(locations= {"classpath:global/parent-app-config-tests.yml"})
public class ParentAppConfigTests extends AbstractUnitTests {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ParentAppConfig parentAppConfig;

    @BeforeAll
    public static void init() {
//        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);

        AbstractUnitTests.init();
    }

    @Test
    public void parentAppConfig_basic() {
        logger.debug("parentAppConfig_basic - hostName: [{}]"
                + ", myInfo: [{}]"
                , parentAppConfig.getHostName()
                , parentAppConfig.getApi().getMyUserInfo()
        );
        assertThat(parentAppConfig.getHostName()).isEqualTo("http://localhost:28080");
        assertThat(parentAppConfig.getApi().getMyUserInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
        assertThat(parentAppConfig.getApi().getMyUserInfo()).contains("/my/user-info");
//        assertThat(parentAppConfig.getApp().getHostName()).isEqualTo("http://localhost:28080");
//        assertThat(parentAppConfig.getApi().getMyUserInfo()).contains(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX);
//        assertThat(parentAppConfig.getApi().getMyUserInfo()).contains("/my/user-info");
    }
}
