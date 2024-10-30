package com.siukatech.poc.react.backend.parent.web.helper;

import com.siukatech.poc.react.backend.parent.web.annotation.base.PublicController;

public class PublicControllerHelper {

    public static String resolveExcludePath() {
        return "/v*" + PublicController.REQUEST_MAPPING_URI_PREFIX + "/**";
    }

}
