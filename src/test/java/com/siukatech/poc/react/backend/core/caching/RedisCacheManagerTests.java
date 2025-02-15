package com.siukatech.poc.react.backend.core.caching;

import ch.qos.logback.classic.Level;
import com.siukatech.poc.react.backend.core.caching.config.RedisCachingConfig;
import com.siukatech.poc.react.backend.core.caching.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.util.List;

@Slf4j
@SpringBootTest(classes = {RedisCachingConfig.class
        , AddressService.class
    }
    , properties = {
        "spring.cache.type=redis"
        , "spring.cache.redis.time-to-live=1s"
        , "logging.level.com.siukatech.poc.react.backend.core.caching=DEBUG"
    }
)
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class
        , RedisAutoConfiguration.class
})
public class RedisCacheManagerTests {
//public class RedisCacheManagerTests extends AbstractCachingManagerTests {

    @Autowired
    private CacheManager cacheManager;

//    @BeforeEach
//    public void setup() {
//        this.initMemoryAppender(
//                List.of(
//                        Pair.with(RedisCacheManagerTests.class.getPackageName(), Level.DEBUG)
//                )
//        );
//        //
//        super.setup_cacheManager();
//    }

    @Test
    public void test_redisCacheManager_basic() {
        log.debug("test_redisCacheManager_basic - getCacheNames: [{}]"
                , this.cacheManager.getCacheNames());
        log.debug("test_redisCacheManager_basic - cacheManager: [{}]"
                , this.cacheManager);
        //
//        super.test_xxxCacheManager_basic("RedisCacheManager");
    }

}
