package com.siukatech.poc.react.backend.core.caching.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisCachingConfig extends AbstractCachingConfig {

    /**
     * Reference:
     * https://docs.spring.io/spring-boot/reference/io/caching.html#io.caching.provider.redis
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            cacheNames.forEach(cacheName -> {
                log.debug("redisCacheManagerBuilderCustomizer - cacheName: [{}]", cacheName);
                builder.withCacheConfiguration(cacheName
                        , RedisCacheConfiguration
                                .defaultCacheConfig()
                                .entryTtl(timeToLive)
                );
            });
        };
    }

    @Bean(name = "cacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory);
        this.cacheNames.forEach(cacheName -> {
            log.debug("redisCacheManager - cacheName: [{}]", cacheName);
            builder.withCacheConfiguration(cacheName
                    , RedisCacheConfiguration
                            .defaultCacheConfig()
                            .entryTtl(this.timeToLive));
        });
        CacheManager cacheManager = builder.build();
        return cacheManager;
    }

}
