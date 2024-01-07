package com.siukatech.poc.react.backend.parent.business.form.encrypted;

public record EncryptedDetail(EncryptedReq encryptedReq
        , EncryptedInfo encryptedInfo, byte[] decryptedData) {
}
