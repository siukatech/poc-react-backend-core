package com.siukatech.poc.react.backend.core.user;

import com.siukatech.poc.react.backend.core.EnableReactBackend;
import com.siukatech.poc.react.backend.core.user.config.UserSupportConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableReactBackend
@Import(value = {
        UserSupportConfig.class
})
public @interface EnableUserSupport {
}
