package com.siukatech.poc.react.backend.core.caching.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
//@EnableCaching  // This is not working.
public class AbstractCachingConfig {

    @Value("${spring.cache.cache-names:"
            + CachingConstant.CACHE_NAME_DEFAULT
            + ","
            + CachingConstant.CACHE_NAME_AUTH
            + ","
            + CachingConstant.CACHE_NAME_IDEMPOTENCY
            + "}")
    protected List<String> cacheNames;

}
