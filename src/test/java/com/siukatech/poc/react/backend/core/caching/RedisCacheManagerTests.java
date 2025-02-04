package com.siukatech.poc.react.backend.core.caching;

import com.siukatech.poc.react.backend.core.caching.config.RedisCachingConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

@Slf4j
@SpringBootTest(classes = {RedisCachingConfig.class}
    , properties = {
        "spring.cache.type=redis"
        , "logging.level.com.siukatech.poc.react.backend.core.caching=DEBUG"
    }
)
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class
        , RedisAutoConfiguration.class
})
public class RedisCacheManagerTests {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void test_redisCacheManager_basic() {
        log.debug("test_redisCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_redisCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);
    }

}
