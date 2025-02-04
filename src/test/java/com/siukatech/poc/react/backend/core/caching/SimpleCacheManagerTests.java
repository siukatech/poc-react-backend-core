package com.siukatech.poc.react.backend.core.caching;

import com.siukatech.poc.react.backend.core.caching.config.SimpleCachingConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@Slf4j
@SpringBootTest(classes = {SimpleCachingConfig.class}
    , properties = {
        "spring.cache.type=simple"
        , "logging.level.com.siukatech.poc.react.backend.core.caching=DEBUG"
    }
)
//@ImportAutoConfiguration(classes = {
//    CacheAutoConfiguration.class
//})
public class SimpleCacheManagerTests {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void test_simpleCacheManager_basic() {
        log.debug("test_simpleCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_simpleCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);
    }

}
