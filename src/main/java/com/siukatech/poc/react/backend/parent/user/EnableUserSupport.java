package com.siukatech.poc.react.backend.parent.user;

import com.siukatech.poc.react.backend.parent.EnableReactBackend;
import com.siukatech.poc.react.backend.parent.user.config.UserSupportConfig;
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
