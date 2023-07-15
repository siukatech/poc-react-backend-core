package com.siukatech.poc.react.backend.parent;


import com.siukatech.poc.react.backend.parent.global.GlobalConfigImport;
import com.siukatech.poc.react.backend.parent.security.SecurityConfigImport;
import com.siukatech.poc.react.backend.parent.web.WebConfigImport;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        GlobalConfigImport.class,
        WebConfigImport.class
        , SecurityConfigImport.class
})
public @interface EnableReactBackendParent {
}
