package com.siukatech.poc.react.backend.parent.web;

import com.siukatech.poc.react.backend.parent.data.config.DataConfig;
import com.siukatech.poc.react.backend.parent.web.config.WebConfig;
import com.siukatech.poc.react.backend.parent.web.config.WebMvcConfig;
import org.springframework.context.annotation.Import;

//@Configuration
@Import({
        DataConfig.class
        , WebConfig.class
        , WebMvcConfig.class
})
public class WebConfigImport {

}
