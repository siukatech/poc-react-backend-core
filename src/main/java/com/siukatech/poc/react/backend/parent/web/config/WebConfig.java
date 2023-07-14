package com.siukatech.poc.react.backend.parent.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

@Configuration
// https://www.concretepage.com/spring/spring-component-scan-include-and-exclude-filter-example
@ComponentScan(value = {"com.siukatech.poc.react.backend.parent.web"
        , "com.siukatech.poc.react.backend.parent.data"
        , "com.siukatech.poc.react.backend.parent.business"
        }
        , excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX
//          , classes = IUserService.class
//          , pattern = "com.concretepage.*.*Util"
            , pattern = "com.siukatech.poc.react.backend.parent.web.controller.*"
))
//@Import({
//        GlobalExceptionHandler.class
//        , CryptoContext.class
//        , EncryptedRequestBodyAdvice.class
//        , EncryptedResponseBodyAdvice.class
//})
public class WebConfig {

    /**
     * Reference:
     * https://stackoverflow.com/a/67973800
     * https://github.com/FasterXML/jackson-databind/issues/3262#issuecomment-909008472
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
//                .json()
//                .build();
//        objectMapper().registerModule(new JavaTimeModule());
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                // here is configured for encrypted data in EncryptedRequestBodyAdvice and EncryptedResponseBodyAdvice
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        return objectMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
