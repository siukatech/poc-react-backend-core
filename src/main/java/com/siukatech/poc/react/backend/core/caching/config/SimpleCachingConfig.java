package com.siukatech.poc.react.backend.core.caching.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Reference:
 * https://baeldung.com/spring-cache-tutorial
 */
@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "simple")
public class SimpleCachingConfig extends AbstractCachingConfig {

    @Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return (cacheManager) -> cacheManager.setAllowNullValues(false);
    }

    @Bean(name = "cacheManager")
    public CacheManager simpleCacheManager() {
        log.debug("simpleCacheManager - cacheNames: [{}]", cacheNames);
        ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager(this.cacheNames.toArray(String[]::new));
        return concurrentMapCacheManager;
    }

}
