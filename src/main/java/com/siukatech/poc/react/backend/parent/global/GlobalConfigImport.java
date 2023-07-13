package com.siukatech.poc.react.backend.parent.global;

import com.siukatech.poc.react.backend.parent.global.config.ParentAppConfig;
import org.springframework.context.annotation.Import;

@Import({ParentAppConfig.class})
public class GlobalConfigImport {
}
