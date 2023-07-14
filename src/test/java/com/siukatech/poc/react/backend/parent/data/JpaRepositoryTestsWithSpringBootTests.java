package com.siukatech.poc.react.backend.parent.data;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.test.context.TestPropertySource;


/**
 * This is not working at this moment
 */
//@Deprecated
@SpringBootTest
@OverrideAutoConfiguration(enabled = false)
@AutoConfigureDataJpa
//@AutoConfigureTestDatabase
////@TestPropertySource({
////		"classpath:application.yml"
////})
@TestPropertySource({"classpath:abstract-unit-tests.properties"
        , "classpath:abstract-jpa-tests.properties"
        , "classpath:abstract-oauth2-tests.properties"})
public class JpaRepositoryTestsWithSpringBootTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @MockBean
    @SpyBean
//    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Test
    void contextLoads() {
        logger.debug("contextLoads - testing logging");
    }

}
