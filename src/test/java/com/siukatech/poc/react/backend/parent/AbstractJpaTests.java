package com.siukatech.poc.react.backend.parent;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@TestPropertySource(
//        properties = {
////        "logging.level.org.springframework.web=TRACE"
////        , "logging.level.com.siukatech.poc.react.backend.parent=TRACE"
//        "spring.jpa.show-sql: true"
//        , "spring.jpa.properties.hibernate.format_sql: true"
//        , "logging.level.org.springframework.data: TRACE"
//}
        locations = {"classpath:abstract-jpa-tests.properties"}
)
// yml is NOT supported by @TestPropertySource
//@TestPropertySource("classpath:application.yml")
public class AbstractJpaTests extends AbstractUnitTests {

}
