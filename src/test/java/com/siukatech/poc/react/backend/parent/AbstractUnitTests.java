package com.siukatech.poc.react.backend.parent;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(properties = {
        "client-id=XXX"
        , "client-secret=XXX"
        , "client-realm=react-backend-realm"
        , "oauth2-client-keycloak=http://keycloak-host-name"
        , "oauth2-client-redirect-uri=http://redirect-host-name/redirect"
        , "spring.profiles.active=dev"
        , "logging.level.org.springframework.web: TRACE"
        , "logging.level.com.siukatech.poc.react.backend.parent: DEBUG"
})
public abstract class AbstractUnitTests {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractUnitTests.class);

    @BeforeAll
    public static void init() {
        final Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);
        rootLogger.setLevel(Level.DEBUG);

        logger.debug("AbstractUnitTests.init............");

    }

    @AfterAll
    public static void terminate() {
        logger.debug("AbstractUnitTests.terminate............");
    }

}
