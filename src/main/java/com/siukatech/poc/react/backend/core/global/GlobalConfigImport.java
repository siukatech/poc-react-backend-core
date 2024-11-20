package com.siukatech.poc.react.backend.core.global;

import com.siukatech.poc.react.backend.core.global.config.MapperConfig;
import com.siukatech.poc.react.backend.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.core.global.config.PostAppConfig;
import org.springframework.context.annotation.Import;

@Import({AppCoreProp.class
        , MapperConfig.class
        , PostAppConfig.class
})
public class GlobalConfigImport {

}
