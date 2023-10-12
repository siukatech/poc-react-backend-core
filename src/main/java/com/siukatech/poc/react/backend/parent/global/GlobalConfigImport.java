package com.siukatech.poc.react.backend.parent.global;

import com.siukatech.poc.react.backend.parent.global.config.MapperConfig;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppProp;
import org.springframework.context.annotation.Import;

@Import({ParentAppProp.class
        , MapperConfig.class
})
public class GlobalConfigImport {

}
