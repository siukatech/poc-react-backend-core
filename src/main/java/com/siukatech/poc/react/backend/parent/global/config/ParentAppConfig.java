package com.siukatech.poc.react.backend.parent.global.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "app")
//@ConfigurationProperties
public class ParentAppConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Bean(name = "securityRestTemplate")
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    private String hostName;
//    private App app;
    private Api api;

    public String getMyInfoUrl() {
        logger.debug("getMyInfoUrl - getHostName: [{}], myInfo: [{}]"
                , this.getHostName(), this.getApi().getMyInfo()
        );
        return this.getHostName() + this.getApi().getMyInfo();
//        return null;
    }

//    @Data
//    public static class App {
//        private String hostName;
//        private Api api;
//    }

    @Data
    public static class Api {
        private String myInfo;
    }
}
