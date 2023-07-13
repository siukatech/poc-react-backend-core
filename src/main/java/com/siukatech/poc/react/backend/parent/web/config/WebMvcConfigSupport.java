package com.siukatech.poc.react.backend.parent.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Arrays;
import java.util.List;

public class WebMvcConfigSupport extends WebMvcConfigurationSupport {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void addCorsMappings(CorsRegistry registry) {
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
     * Reference: https://stackoverflow.com/q/36906877
     * disable feature WRITE_DATES_AS_TIMESTAMPS
     *
     * @param converters the list of configured converters to extend
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.debug("extendMessageConverters - getMessageConverters.size: [{}]", this.getMessageConverters().size());
        converters.stream().forEach(httpMessageConverter -> {
            logger.debug("extendMessageConverters - httpMessageConverter.getClass.getName: [{}]", httpMessageConverter.getClass().getName());
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
//                MappingJackson2HttpMessageConverter jacksonMessageConverter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
                ObjectMapper objectMapper = mappingJackson2HttpMessageConverter.getObjectMapper();
                logger.debug("extendMessageConverters - MappingJackson2HttpMessageConverter.getObjectMapper: [{}]"
                        , mappingJackson2HttpMessageConverter.getObjectMapper());
                // here is configured for non-encrypted data, general response body
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }
        });
    }

}
