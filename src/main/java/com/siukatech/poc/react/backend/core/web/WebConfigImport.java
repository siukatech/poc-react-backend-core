package com.siukatech.poc.react.backend.core.web;

import com.siukatech.poc.react.backend.core.data.config.DataConfig;
import com.siukatech.poc.react.backend.core.web.config.NoopTracingConfig;
import com.siukatech.poc.react.backend.core.web.config.WebComponentConfig;
import com.siukatech.poc.react.backend.core.web.config.WebMvcConfig;
import org.springframework.context.annotation.Import;

//@Configuration
@Import({
        DataConfig.class
        , WebComponentConfig.class
        , NoopTracingConfig.class
        , WebMvcConfig.class
})
public class WebConfigImport {

}
