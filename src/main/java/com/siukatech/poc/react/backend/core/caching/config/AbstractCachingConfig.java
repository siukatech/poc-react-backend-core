package com.siukatech.poc.react.backend.core.caching.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
//@EnableCaching  // This is not working.
public class AbstractCachingConfig {

    @Value("${spring.cache.cache-names:default}")
    protected List<String> cacheNames;

    /**
     * Reference:
     * https://jdriven.com/blog/2024/10/Spring-Boot-Sweets-Using-Duration-Type-With-Configuration-Properties
     */
    @Value("${spring.cache.redis.time-to-live:10m}")
    protected Duration timeToLive;

}
