package com.siukatech.poc.react.backend.parent.web.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

//public class WebMvcConfig extends WebMvcConfigurationSupport {
public class WebMvcConfig implements WebMvcConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;

    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.debug("addCorsMappings - start");
        registry
                .addMapping("/**")
//                .allowedMethods(HttpMethod.HEAD.name()
//                        , HttpMethod.GET.name()
//                        , HttpMethod.POST.name()
//                        , HttpMethod.PUT.name()
//                        , HttpMethod.DELETE.name()
//                        , HttpMethod.PATCH.name()
//                        , HttpMethod.OPTIONS.name()
//                )
                .allowedMethods(Arrays.stream(HttpMethod.values()).map(HttpMethod::name).toArray(String[]::new))
                //.allowedOrigins("http://localhost:3000/")
                .allowedOrigins("*")
        ;
        logger.debug("addCorsMappings - end");
    }

//    private static final String dateFormat = "yyyy-MM-dd";
//    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
//
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//        return builder -> {
//            builder.simpleDateFormat(dateTimeFormat);
//            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
//            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
//        };
//    }

    /**
     * Reference:
     * https://stackoverflow.com/q/36906877
     * disable feature WRITE_DATES_AS_TIMESTAMPS
     *
     * @param converters the list of configured converters to extend
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.debug("extendMessageConverters - start");
        logger.debug("extendMessageConverters - converters.size: [{}]", converters.size());
        converters.stream().forEach(httpMessageConverter -> {
            logger.debug("extendMessageConverters - httpMessageConverter.getClass.getName: [{}]", httpMessageConverter.getClass().getName());
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
////                MappingJackson2HttpMessageConverter jacksonMessageConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
//                ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
//                logger.debug("extendMessageConverters - MappingJackson2HttpMessageConverter.getObjectMapper: [{}]"
//                        , mappingJackson2HttpMessageConverter.getObjectMapper());
//                objectMapper =
//                        // here is configured for non-encrypted data, general response body
//                        objectMapper
//                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//
//                                // ignore unknown json properties to prevent HttpMessageNotReadableException
//                                // https://stackoverflow.com/a/5455563
////                objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
//                                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
////                                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
////                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
////                                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//                ;
//                objectMapper.getDeserializationConfig().without(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

                mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
            }
        });
        logger.debug("extendMessageConverters - end");
    }

}
