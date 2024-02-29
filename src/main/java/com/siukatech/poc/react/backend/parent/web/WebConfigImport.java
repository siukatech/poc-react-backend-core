package com.siukatech.poc.react.backend.parent.web;

import com.siukatech.poc.react.backend.parent.data.config.DataConfig;
import com.siukatech.poc.react.backend.parent.web.config.NoopTracingConfig;
import com.siukatech.poc.react.backend.parent.web.config.WebComponentConfig;
import com.siukatech.poc.react.backend.parent.web.config.WebMvcConfig;
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
