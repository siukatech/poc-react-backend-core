package com.siukatech.poc.react.backend.parent.data;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * This is not working at this moment
 */
//@Deprecated
@SpringBootTest
@OverrideAutoConfiguration(enabled = false)
@AutoConfigureDataJpa
//@AutoConfigureTestDatabase
//@TestPropertySource({
//		"classpath:application.yml"
//})
public class JpaRepositoryTestsWithSpringBootTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    void contextLoads() {
        logger.debug("contextLoads - testing logging");
    }

}
