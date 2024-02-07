package com.siukatech.poc.react.backend.parent.global;

import com.siukatech.poc.react.backend.parent.global.config.MapperConfig;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import com.siukatech.poc.react.backend.parent.global.config.PostAppConfig;
import org.springframework.context.annotation.Import;

@Import({ParentAppProp.class
        , MapperConfig.class
        , PostAppConfig.class
})
public class GlobalConfigImport {

}
