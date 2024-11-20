package com.siukatech.poc.react.backend.core.business.form.encrypted;

public record EncryptedDetail(EncryptedReq encryptedReq
        , EncryptedInfo encryptedInfo, byte[] decryptedData) {
}
