package com.siukatech.poc.react.backend.core.web.helper;


import com.siukatech.poc.react.backend.core.web.annotation.v1.ProtectedApiV1Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@ProtectedApiV1Controller
public class ProtectedForTestController {

    @GetMapping("/test/{test}")
    public String testEncrypted(@PathVariable String test) {
        return "encrypted";
    }
}
