package com.siukatech.poc.react.backend.parent.web.advice;

import com.siukatech.poc.react.backend.parent.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.parent.web.context.EncryptedBodyContext;
import com.siukatech.poc.react.backend.parent.web.helper.EncryptedBodyAdviceHelper;
import com.siukatech.poc.react.backend.parent.business.form.encrypted.EncryptedInfo;
import com.siukatech.poc.react.backend.parent.business.form.encrypted.EncryptedDetail;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Reference:
 * http://www.javabyexamples.com/quick-guide-to-responsebodyadvice-in-spring-mvc
 */
@Slf4j
@Component
@RestControllerAdvice
public class EncryptedResponseBodyAdvice implements ResponseBodyAdvice {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String HEADER_X_DATA_ENC_INFO = "X-DATA-ENC-INFO";
    private final EncryptedBodyContext encryptedBodyContext;
//    private final UserRepository userRepository;
//    private final ObjectMapper objectMapper;
    private final EncryptedBodyAdviceHelper encryptedBodyAdviceHelper;

    private EncryptedResponseBodyAdvice(EncryptedBodyContext encryptedBodyContext
//            , UserRepository userRepository, ObjectMapper objectMapper
            , EncryptedBodyAdviceHelper encryptedBodyAdviceHelper
    ) {
        this.encryptedBodyContext = encryptedBodyContext;
//        this.userRepository = userRepository;
//        this.objectMapper = objectMapper;
        this.encryptedBodyAdviceHelper = encryptedBodyAdviceHelper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
//        boolean resultFromAnnotation = Arrays.stream(returnType.getMethod().getDeclaringClass().getDeclaredAnnotations())
//                .anyMatch(annotation -> annotation.annotationType().equals(EncryptedApiV1Controller.class));
//        //boolean resultFromPath = returnType.getParameter()

//        logger.debug("supports - returnType.getMethod.getName: [" + returnType.getMethod().getName()
//                + "], returnType.getMethod.getDeclaringClass.getName: [" + returnType.getMethod().getDeclaringClass().getName()
//                + "], resultFromAnnotation: [" + resultFromAnnotation
//                + "]");
//        Arrays.stream(returnType.getMethod().getDeclaringClass().getDeclaredAnnotations()).forEach(annotation -> {
//            logger.debug("supports - getDeclaringClass.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(returnType.getMethod().getDeclaringClass().getAnnotations()).forEach(annotation -> {
//            logger.debug("supports - getDeclaringClass.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(returnType.getMethod().getDeclaredAnnotations()).forEach(annotation -> {
//            logger.debug("supports - getMethod.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(returnType.getMethod().getAnnotations()).forEach(annotation -> {
//            logger.debug("supports - getMethod.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });

        boolean resultFromAnnotation = this.encryptedBodyAdviceHelper.isEncryptedApiController(returnType);
        return resultFromAnnotation;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType
            , MediaType selectedContentType, Class selectedConverterType
            , ServerHttpRequest request, ServerHttpResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        logger.debug("beforeBodyWrite - returnType.getMethod.getName: [" + returnType.getMethod().getName()
                + "], returnType.getParameterName: [" + returnType.getParameterName()
                + "], selectedContentType.getCharset: [" + (selectedContentType == null ? "NULL" : selectedContentType.getCharset())
                + "], selectedConverterType.getName: [" + selectedConverterType.getName()
                + "], object: [" + body.getClass().getName()
                + "], authentication.getName: [" + (authentication == null ? "NULL" : authentication.getName())
                + "], authentication.isAuthenticated: [" + (authentication == null ? "NULL" : authentication.isAuthenticated())
                + "], loginId: [" + loginId
                + "]");

//        UserEntity userEntity = this.encryptedBodyContext.getUserEntity();
        MyKeyDto myKeyDto = this.encryptedBodyContext.getMyKeyDto();
        EncryptedDetail encryptedDetail = this.encryptedBodyContext.getEncryptedDetail();
        String encryptedRsaDataBase64 = request.getHeaders().getFirst(HEADER_X_DATA_ENC_INFO);
        EncryptedInfo encryptedInfo = null;
        logger.debug("beforeBodyWrite - returnType.getMethod.getName: [" + returnType.getMethod().getName()
//                + "], userEntity.getId: [" + (userEntity == null ? "NULL" : userEntity.getId())
                + "], myKeyDto.getLoginId: [" + (myKeyDto == null ? "NULL" : myKeyDto.getLoginId())
                + "], encryptedBodyDetail.encryptedInfoModel.key: [" + (encryptedDetail == null ? "NULL" : encryptedDetail.encryptedInfo().key())
                + "], encryptedRsaDataBase64: [" + encryptedRsaDataBase64
                + "]");
//        if (userEntity == null) {
//            String finalLoginId = loginId;
//            userEntity = this.userRepository.findByLoginId(loginId)
//                    .orElseThrow(() -> new EntityNotFoundException("No such user [" + finalLoginId + "]"));
//        }
        if (myKeyDto == null) {
            myKeyDto = this.encryptedBodyAdviceHelper.resolveMyKeyInfo(loginId);
        }
        if (encryptedDetail == null) {
//            // should obtain from SecurityContext again
//            loginId = loginId == null ? "app-user-01" : loginId;
            try {
//                encryptedBodyDetail = this.encryptedBodyAdviceHelper
//                        .decryptRsaDataBase64ToBodyDetail(encryptedRsaDataBase64, userEntity);
                encryptedDetail = this.encryptedBodyAdviceHelper.decryptDataBase64ToBodyDetail(
                        encryptedRsaDataBase64
//                        , userEntity
                        , myKeyDto
                );
                encryptedInfo = encryptedDetail.encryptedInfo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
//            loginId = userEntity.getLoginId();
            encryptedInfo = encryptedDetail.encryptedInfo();
        }

        logger.debug("beforeBodyWrite - returnType.getMethod.getName: [" + returnType.getMethod().getName()
                + "], loginId: [" + loginId
                + "], encryptedInfoModel.key: [" + encryptedInfo.key()
                + "], encryptedInfoModel.iv: [" + encryptedInfo.iv()
                + "]");

        try {
//            String bodyStr = null;
////            ObjectMapper objectMapper = new ObjectMapper();
//            bodyStr = this.objectMapper.writeValueAsString(body);
////            Gson gson = new GsonBuilder()
////                    .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
////                        @Override
////                        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
////                            Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
////                            return LocalDate.ofInstant(instant, ZoneId.systemDefault());
////                        }
////                    })
////                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
////                        @Override
////                        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
////                            Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
////                            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
////                        }
////                    })
////                    .create();
////            bodyStr = gson.toJson(body);
//
////            byte[] encryptedAesData = CryptoUtil.encryptWithAesCbcSecret(bodyStr
////                    , Base64.getDecoder().decode(encryptedInfoModel.key())
////                    , Base64.getDecoder().decode(encryptedInfoModel.iv())
////            );
//            byte[] decodedKey = Base64.getDecoder().decode(encryptedInfoModel.key());
//            //byte[] decodedKey = encryptedInfoModel.key().getBytes(StandardCharsets.UTF_8);
//            byte[] encryptedAesData = CryptoUtil.encryptWithAesEcbSecret(bodyStr
//                    , decodedKey
//            );
//            String encryptedAesDataBase64 = Base64.getEncoder().encodeToString(encryptedAesData);
////            byte[] encryptedRsaData = CryptoUtil.encryptWithRsaPrivateKey(encryptedAesDataBase64, userEntity.getPrivateKey());
////            String encryptedRsaDataBase64 = Base64.getEncoder().encodeToString(encryptedRsaData);
//            logger.debug("beforeBodyWrite - bodyStr: [" + bodyStr
//                    + "], decodedKey.length: [" + decodedKey.length
//                    + "], encryptedAesData.length: [" + encryptedAesData.length
//                    + "], encryptedDataBase64: [" + encryptedAesDataBase64
////                    + "], encryptedRsaDataBase64: [" + encryptedRsaDataBase64
//                    + "]");
            String encryptedAesDataBase64 = this.encryptedBodyAdviceHelper
                    .encryptBodyToDataBase64(body, encryptedInfo);
            return encryptedAesDataBase64;
//            return encryptedRsaDataBase64;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
