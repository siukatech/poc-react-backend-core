package com.siukatech.poc.react.backend.core.caching.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Objects;

@Slf4j
@Configuration
@EnableCaching
@EnableRedisRepositories
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class RedisCachingConfig extends AbstractCachingConfig {

    @Value("${spring.cache.redis.time-to-live:10m}")
    private java.time.Duration timeToLive;

//    @Getter
//    @ConfigurationProperties(prefix = "spring.cache")
//    public static class RedisCacheProp {
//        private String host;
//        private int port;
//    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        if (Objects.nonNull(redisProperties.getPassword())) {
            redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        }
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        if (Objects.nonNull(redisProperties.getLettuce())) {
            // do something
        }
        log.debug("redisConnectionFactory - redisStandaloneConfiguration: [${}]"
                        + ", redisProperties: [{}]"
                , redisStandaloneConfiguration, redisProperties);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

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
