package com.siukatech.poc.react.backend.core.user.caching;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

@Slf4j
public class UserPermissionCacheKeyGenerator implements KeyGenerator {

    public static final String CACHE_KEY_findPermissionsByUserIdAndApplicationId = "UserService.findPermissionsByUserIdAndApplicationId_";

    @Override
    public Object generate(Object target, Method method, Object... params) {
        log.debug("generate - target: [{}], method: [{}], params: [{}]", target, method, params);
        String targetUserId = (String) params[0];
        String applicationId = (String) params[1];
        String key = CACHE_KEY_findPermissionsByUserIdAndApplicationId + targetUserId + "_" + applicationId;
        log.debug("generate - target: [{}], targetUserId: [{}], applicationId: [{}], key: [{}]"
                , target, targetUserId, applicationId, key);
        return key;
    }

}
