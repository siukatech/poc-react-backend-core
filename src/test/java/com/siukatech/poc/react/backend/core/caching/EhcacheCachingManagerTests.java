package com.siukatech.poc.react.backend.core.caching;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.siukatech.poc.react.backend.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.core.caching.config.EhcacheCachingConfig;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.List;

@Slf4j
@SpringBootTest(classes = {EhcacheCachingConfig.class}
    , properties = {
        "spring.cache.type=ehcache"
        , "logging.level.com.siukatech.poc.react.backend.core.caching=DEBUG"
    }
)
//@ImportAutoConfiguration(classes = {
//        CacheAutoConfiguration.class
//})
public class EhcacheCachingManagerTests extends AbstractUnitTests {

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setup() {
        this.initMemoryAppender(
                List.of(
                        Pair.with(EhcacheCachingManagerTests.class.getPackageName(), Level.DEBUG)
                )
        );
    }

    @Test
    public void test_ehcacheCacheManager_basic() {
        log.debug("test_ehcacheCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_ehcacheCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);

        List<ILoggingEvent> loggingEventList = this.getMemoryAppender().search("JCacheCacheManager", Level.DEBUG);
        Assertions.assertTrue((!loggingEventList.isEmpty()), "JCacheCacheManager not found");
    }

}
