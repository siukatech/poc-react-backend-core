package com.siukatech.poc.react.backend.parent.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * https://4youngpadawans.com/end-to-end-encryption-between-react-and-spring/
 */
@Slf4j
public class EncryptionUtils {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);

    public static final String ALGORITHM_RSA = "RSA";
    public static final String ALGORITHM_AES = "AES";
    public static final String TRANSFORMATION_RSA = "RSA";
    public static final String TRANSFORMATION_AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5Padding";
    public static final String TRANSFORMATION_AES_ECB_PKCS5PADDING = "AES/ECB/PKCS5Padding";
    public static final String TRANSFORMATION_AES_GCM_NOPADDING = "AES/GCM/NoPadding";
    private EncryptionUtils() {
    }

    /**
     * Reference:
     * https://stackoverflow.com/a/50381020
     *
     * @param length
     * @return
     */
    public static String generateRandomToken(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        String tokenStr = encoder.encodeToString(bytes);
        return tokenStr;
    }

    public static KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        SecureRandom secureRandom = new SecureRandom();
        //1024 bit long key
        //keyGen.initialize(1024);
        keyGen.initialize(2048, secureRandom);
        //generating RSA key pair (public and private)
        return keyGen.genKeyPair();
    }

    public static PublicKey getRsaPublicKey(String base64PublicKey) throws Exception {
        byte[] publicKey = Base64.getDecoder().decode(base64PublicKey);
        EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey getRsaPrivateKey(String base64PrivateKey) throws Exception {
        logger.debug("getRsaPrivateKey - base64PrivateKey: [" + base64PrivateKey + "]");
        byte[] privateKey = Base64.getDecoder().decode(base64PrivateKey);
        //PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    public static byte[] encryptWithRsaPublicKey(String text, String rsaPublicKeyBase64) throws Exception {
        //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, getRsaPublicKey(rsaPublicKeyBase64));
        return cipher.doFinal(text.getBytes());
    }

    public static byte[] decryptWithRsaPrivateKey(byte[] data, String rsaPrivateKeyBase64) throws Exception {
        //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_RSA);
        cipher.init(Cipher.DECRYPT_MODE, getRsaPrivateKey(rsaPrivateKeyBase64));
        return cipher.doFinal(data);
    }

    public static byte[] encryptWithAesCbcSecret(String str, byte[] secret, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_CBC_PKCS5PADDING);
        //Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES), new IvParameterSpec(iv));
        return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
    }
    public static byte[] decryptWithAesCbcSecret(byte[] data, byte[] secret, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_CBC_PKCS5PADDING);
        //Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES), new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    @Deprecated
    public static byte[] encryptWithAesEcbSecret(String str, byte[] secret) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES));
        return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
    }

    @Deprecated
    public static byte[] decryptWithAesEcbSecret(byte[] data, byte[] secret) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_ECB_PKCS5PADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES));
        return cipher.doFinal(data);
    }

    /**
     * Reference:
     * https://www.javainterviewpoint.com/java-aes-256-gcm-encryption-and-decryption/
     *
     * @param str
     * @param secret
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] encryptWithAesGcmSecret(String str, byte[] secret, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_GCM_NOPADDING);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES)
                , new GCMParameterSpec(16 * 8, iv));
        return cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
    }
    public static byte[] decryptWithAesGcmSecret(byte[] data, byte[] secret, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_AES_GCM_NOPADDING);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret, ALGORITHM_AES)
                , new GCMParameterSpec(16 * 8, iv));
        return cipher.doFinal(data);
    }


    public static byte[] encryptWithRsaPrivateKey(String text, String rsaPrivateKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getRsaPrivateKey(rsaPrivateKeyBase64));
        return cipher.doFinal(text.getBytes());
    }

    public static byte[] decryptWithRsaPublicKey(byte[] data, String rsaPublicKeyBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getRsaPublicKey(rsaPublicKeyBase64));
        return cipher.doFinal(data);
    }

}
