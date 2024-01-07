package com.siukatech.poc.react.backend.parent;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;


//@Slf4j
@TestPropertySource(
//        properties = {
//        "client-id=XXX"
//        , "client-secret=XXX"
//        , "client-realm=react-backend-realm"
//        , "oauth2-client-keycloak=http://keycloak-host-name"
//        , "oauth2-client-redirect-uri=http://redirect-host-name/redirect"
//        , "spring.profiles.active=dev"
//        , "logging.level.org.springframework.web: TRACE"
//        , "logging.level.com.siukatech.poc.react.backend.parent: DEBUG"
//}
        locations = {"classpath:abstract-unit-tests.properties"}
)
public abstract class AbstractUnitTests {

    protected static final org.slf4j.Logger log = LoggerFactory.getLogger(AbstractUnitTests.class);

    @BeforeAll
    public static void init() {
        final Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);
        rootLogger.setLevel(Level.DEBUG);

        log.debug("AbstractUnitTests.init............");

    }

    @AfterAll
    public static void terminate() {
        log.debug("AbstractUnitTests.terminate............");
    }

}
