package com.siukatech.poc.react.backend.parent.web.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siukatech.poc.react.backend.parent.business.dto.MyKeyDto;
import com.siukatech.poc.react.backend.parent.business.service.AuthService;
import com.siukatech.poc.react.backend.parent.global.config.ParentAppConfig;
import com.siukatech.poc.react.backend.parent.util.EncryptionUtil;
import com.siukatech.poc.react.backend.parent.web.annotation.base.EncryptedController;
import com.siukatech.poc.react.backend.parent.web.model.encrypted.EncryptedDetail;
import com.siukatech.poc.react.backend.parent.web.model.encrypted.EncryptedInfo;
import com.siukatech.poc.react.backend.parent.web.model.encrypted.EncryptedReq;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EncryptedBodyAdviceHelper {

    //    private final String CIPHER_SEPARATOR = "|||";
    private final String CIPHER_SEPARATOR = "";
    private final Integer CIPHER_INFO_LENGTH = 344;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;
    private final RestTemplate oauth2ClientRestTemplate;
    private final ParentAppConfig parentAppConfig;
//    private final AuthService authService;


    public EncryptedBodyAdviceHelper(ObjectMapper objectMapper
            , RestTemplate oauth2ClientRestTemplate
            , ParentAppConfig parentAppConfig
//            , AuthService authService
    ) {
        this.objectMapper = objectMapper;
        this.oauth2ClientRestTemplate = oauth2ClientRestTemplate;
        this.parentAppConfig = parentAppConfig;
//        this.authService = authService;
    }

    @Deprecated
    public EncryptedDetail decryptRsaDataBase64ToBodyDetail(
            String encryptedRsaDataBase64
//            , UserEntity userEntity
            , MyKeyDto myKeyDto
    ) throws Exception {
//        String userId = userEntity.getUserId();
        String userId = myKeyDto.getUserId();
        logger.debug("decryptRsaDataBase64ToBodyDetail - userId: [" + userId
                + "], start");
        byte[] decryptedBodyData = EncryptionUtil.decryptWithRsaPrivateKey(
                Base64.getDecoder().decode(encryptedRsaDataBase64)
//                , userEntity.getPrivateKey()
                , myKeyDto.getPrivateKey()
        );
        String decryptedBodyStr = new String(decryptedBodyData);
        logger.debug("decryptRsaDataBase64ToBodyDetail - encryptedRsaDataBase64: [" + encryptedRsaDataBase64
                + "], decryptedBodyStr: [" + decryptedBodyStr
                + "]");
        EncryptedReq encryptedReq = null;
//            ObjectMapper objectMapper = new ObjectMapper();
        encryptedReq = this.objectMapper
                .readValue(decryptedBodyStr, EncryptedReq.class);
//            Gson gson = new Gson();
//            encryptedRequestModel = gson
//                    .fromJson(decryptedBodyStr, EncryptedRequestModel.class);
        EncryptedInfo encryptedInfo = encryptedReq.info();

//            byte[] aesKey = CryptoUtil.decryptWithRsaPrivateKey(
//                    Base64.getDecoder().decode(encryptedRequestModel.key())
//                    , userEntity.getPrivateKey()
//            );
//            byte[] decryptedBody = CryptoUtil.decryptWithAesKey(
//                    Base64.getDecoder().decode(encryptedRequestModel.cipher())
//                    , aesKey);
        byte[] decryptedData = null;
        if (encryptedReq.cipher() != null) {
            decryptedData = EncryptionUtil.decryptWithAesCbcSecret(
                    Base64.getDecoder().decode(encryptedReq.cipher())
//                    , aesKey
                    , Base64.getDecoder().decode(encryptedInfo.key())
                    , Base64.getDecoder().decode(encryptedInfo.iv())
            );
        }
        EncryptedDetail encryptedDetail = new EncryptedDetail(
                encryptedReq, encryptedInfo, decryptedData);
        return encryptedDetail;
    }

    public String encryptBodyToDataBase64(Object body
            , EncryptedInfo encryptedInfo) throws Exception {
        String bodyStr = null;
//            ObjectMapper objectMapper = new ObjectMapper();
        bodyStr = this.objectMapper.writeValueAsString(body);
//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
//                        @Override
//                        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//                            Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
//                            return LocalDate.ofInstant(instant, ZoneId.systemDefault());
//                        }
//                    })
//                    .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
//                        @Override
//                        public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//                            Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
//                            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//                        }
//                    })
//                    .create();
//            bodyStr = gson.toJson(body);

//            byte[] encryptedAesData = CryptoUtil.encryptWithAesCbcSecret(bodyStr
//                    , Base64.getDecoder().decode(encryptedInfoModel.key())
//                    , Base64.getDecoder().decode(encryptedInfoModel.iv())
//            );
        byte[] decodedKey = Base64.getDecoder().decode(encryptedInfo.key());
        byte[] decodedIv = Base64.getDecoder().decode(encryptedInfo.iv());
        //byte[] decodedKey = encryptedInfoModel.key().getBytes(StandardCharsets.UTF_8);
//        byte[] encryptedAesData = EncryptionUtil.encryptWithAesEcbSecret(bodyStr
//                , decodedKey
//        );
        byte[] encryptedAesData = EncryptionUtil.encryptWithAesCbcSecret(bodyStr
                , decodedKey, decodedIv
        );
        String encryptedAesDataBase64 = Base64.getEncoder().encodeToString(encryptedAesData);
//            byte[] encryptedRsaData = CryptoUtil.encryptWithRsaPrivateKey(encryptedAesDataBase64, userEntity.getPrivateKey());
//            String encryptedRsaDataBase64 = Base64.getEncoder().encodeToString(encryptedRsaData);
        logger.debug("encryptBodyToDataBase64 - decodedKey.length: [" + (decodedKey == null ? "NULL" : decodedKey.length)
//                + "], bodyStr: [" + bodyStr
                + "], encryptedAesData.length: [" + (encryptedAesData == null ? "NULL" : encryptedAesData.length)
                + "], encryptedDataBase64: [" + encryptedAesDataBase64
//                    + "], encryptedRsaDataBase64: [" + encryptedRsaDataBase64
                + "]");
        return encryptedAesDataBase64;
//            return encryptedRsaDataBase64;

    }

    private String[] resolveRsaInfoAesContent(String encryptedDataStr) {
        List<String> encryptedDataList = new ArrayList<>();
//        String[] encryptedDataArr = StringUtils.split(encryptedDataStr, CIPHER_SEPARATOR);
//        encryptedDataList.add(encryptedDataArr[0]);
//        if (encryptedDataArr.length > 1) {
//            encryptedDataList.add(encryptedDataArr[1]);
//        }
//        else {
//            encryptedDataList.add(null);
//        }
        String encryptedRsaInfo = encryptedDataStr.substring(0, CIPHER_INFO_LENGTH);
        String encryptedAesContent = encryptedDataStr.substring(CIPHER_INFO_LENGTH);
        encryptedDataList.add(encryptedRsaInfo);
        encryptedDataList.add(encryptedAesContent);
        return encryptedDataList.toArray(new String[encryptedDataList.size()]);
    }

    public EncryptedDetail decryptDataBase64ToBodyDetail(
            String encryptedDataBase64
//            , UserEntity userEntity
            , MyKeyDto myKeyDto
    ) throws Exception {
//        String userId = userEntity.getUserId();
        String userId = myKeyDto.getUserId();
        logger.debug("decryptDataBase64ToBodyDetail - userId: [" + userId
                + "], start");
        byte[] encryptedData = Base64.getDecoder().decode(encryptedDataBase64.getBytes(StandardCharsets.UTF_8));
        String encryptedDataStr = new String(encryptedData);
//        String[] encryptedDataArr = StringUtils.split(encryptedDataStr, CIPHER_SEPARATOR);
        String[] encryptedDataArr = this.resolveRsaInfoAesContent(encryptedDataStr);
        String encryptedRsaInfo = encryptedDataArr[0];
        String encryptedAesContent = null;
        if (encryptedDataArr.length > 1) {
            encryptedAesContent = encryptedDataArr[1];
        }
        logger.debug("decryptDataBase64ToBodyDetail - userId: [" + userId
                + "], encryptedRsaInfo: [" + encryptedRsaInfo
                + "], encryptedAesContent: [" + encryptedAesContent
                + "]");
        byte[] decryptedRsaInfoData = EncryptionUtil.decryptWithRsaPrivateKey(
                Base64.getDecoder().decode(encryptedRsaInfo)
//                , userEntity.getPrivateKey()
                , myKeyDto.getPrivateKey()
        );
        String decryptedRsaInfoStr = new String(decryptedRsaInfoData);
        logger.debug("decryptDataBase64ToBodyDetail - userId: [" + userId
                + "], decryptedRsaInfoStr: [" + decryptedRsaInfoStr
                + "]");
        EncryptedInfo encryptedInfo = this.objectMapper
                .readValue(decryptedRsaInfoStr, EncryptedInfo.class);
        byte[] decryptedAesContentData = null;
        String decryptedAesContentStr = null;
        if (encryptedAesContent != null) {
            decryptedAesContentData = EncryptionUtil.decryptWithAesCbcSecret(
                    Base64.getDecoder().decode(encryptedAesContent)
                    , Base64.getDecoder().decode(encryptedInfo.key())
                    , Base64.getDecoder().decode(encryptedInfo.iv())
            );
            decryptedAesContentStr = new String(decryptedAesContentData);
        }
        logger.debug("decryptDataBase64ToBodyDetail - userId: [" + userId
                + "], decryptedAesContentData.length: [" + (decryptedAesContentData == null ? "NULL" : decryptedAesContentData.length)
                + "], decryptedAesContentStr: [" + decryptedAesContentStr
                + "]");
        EncryptedDetail encryptedDetail = new EncryptedDetail(
                null, encryptedInfo, decryptedAesContentData);
        return encryptedDetail;
    }

    public MyKeyDto resolveMyKeyInfo(String userId) {
        String myKeyInfoUrl = this.parentAppConfig.getMyKeyInfoUrl();
        MyKeyDto myKeyDto = null;
        if (StringUtils.isNotEmpty(myKeyInfoUrl)) {
            ResponseEntity<MyKeyDto> responseEntity = this.oauth2ClientRestTemplate.exchange(
                    myKeyInfoUrl, HttpMethod.POST, HttpEntity.EMPTY, MyKeyDto.class);
            myKeyDto = responseEntity.getBody();
            logger.debug("resolveMyKeyInfo - userId: [{}], myKeyInfoUrl: [{}], myKeyDto.getUserId: [{}]"
//                + ", responseEntity.getBody.toString: [{}]"
                    , userId, myKeyInfoUrl, myKeyDto.getUserId()
//                , responseEntity.getBody().toString()
            );
            if (!userId.equals(myKeyDto.getUserId())) {
                throw new EntityNotFoundException(
                        "User does not match userId: [%s], myKeyDto.getUserId: [%s]"
                                .formatted(userId, myKeyDto.getUserId()));
            }
        }
        else {
            logger.debug("resolveMyKeyInfo - userId: [{}], myKeyInfoUrl: [{}]"
                    , userId, myKeyInfoUrl
            );
            throw new RuntimeException(
                    "User with userId: [%s] cannot be resolved because of the empty my-user-info"
                            .formatted(userId));
        }
        return myKeyDto;
//        return this.authService.resolveMyKeyInfo(userId);
    }

    public boolean isEncryptedApiController(MethodParameter methodParameter) {
        boolean resultFromAnnotation = false;
        List<Annotation> annotationList = null;
////        resultFromAnnotation = Arrays.stream(returnType.getMethod().getDeclaringClass().getDeclaredAnnotations())
////                .filter(annotation -> annotation.annotationType().equals(EncryptedApiV1Controller.class)).count() > 0;
//        resultFromAnnotation = Arrays.stream(methodParameter.getMethod().getDeclaringClass().getDeclaredAnnotations())
//                .anyMatch(annotation -> annotation.annotationType().equals(EncryptedController.class));
////        boolean resultFromAnnotation = Arrays.stream(returnType.getMethod().getDeclaringClass().getDeclaredAnnotations())
////                .anyMatch(annotation -> annotation.annotationType().equals(EncryptedApiV1Controller.class));
        annotationList = Arrays.stream(methodParameter.getMethod().getDeclaringClass().getAnnotations())
                // The flatMap is added to collect the nested annotations from annotation.
                .flatMap(annotation -> Arrays.stream(annotation.annotationType().getAnnotations()))
                .collect(Collectors.toList());
//        resultFromAnnotation = annotationList.stream()
//                .anyMatch(annotation -> annotation.annotationType().equals(EncryptedController.class));
        Annotation encryptedControllerAnnotation = AnnotationUtils.findAnnotation(
                methodParameter.getDeclaringClass(), EncryptedController.class);
        resultFromAnnotation = (encryptedControllerAnnotation != null);

        logger.debug("isEncryptedApiController - methodParameter.getMethod.getName: [" + methodParameter.getMethod().getName()
                + "], methodParameter.getParameterType.getName: [" + methodParameter.getParameterType().getName()
                + "], methodParameter.getMember.getName: [" + methodParameter.getMember().getName()
//                + "], methodParameter.getConstructor.getName: [" + methodParameter.getConstructor().getName()
                + "], methodParameter.getMethod.getDeclaringClass.getName: [" + methodParameter.getMethod().getDeclaringClass().getName()
                + "], encryptedControllerAnnotation: [" + encryptedControllerAnnotation
                + "], resultFromAnnotation: [" + resultFromAnnotation
                + "]");

//        Arrays.stream(methodParameter.getMethod().getDeclaringClass().getDeclaredAnnotations()).forEach(annotation -> {
//            logger.debug("isEncryptedApiController - getDeclaringClass.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(methodParameter.getMethod().getDeclaringClass().getAnnotations()).forEach(annotation -> {
//            logger.debug("isEncryptedApiController - getDeclaringClass.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(methodParameter.getMethod().getDeclaredAnnotations()).forEach(annotation -> {
//            logger.debug("isEncryptedApiController - getMethod.getDeclaredAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
//        Arrays.stream(methodParameter.getMethod().getAnnotations()).forEach(annotation -> {
//            logger.debug("isEncryptedApiController - getMethod.getAnnotations - annotation: [" + annotation.annotationType().getName() + "]");
//        });
        annotationList.stream().forEach(annotation -> {
            logger.debug("isEncryptedApiController - annotationList - annotation: [" + annotation.annotationType().getName() + "]");
        });

        return resultFromAnnotation;
    }
}

