package com.siukatech.poc.react.backend.parent.data;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

//@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaRepositoryTestsWithDataJpaTests {

    protected static final Logger log = LoggerFactory.getLogger(JpaRepositoryTestsWithDataJpaTests.class);

    @Test
    void contextLoads() {
        log.debug("contextLoads - testing logging");
    }

}
