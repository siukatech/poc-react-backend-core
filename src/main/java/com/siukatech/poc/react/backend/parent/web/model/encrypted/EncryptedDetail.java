package com.siukatech.poc.react.backend.parent.web.model.encrypted;

public record EncryptedDetail(EncryptedReq encryptedReq
        , EncryptedInfo encryptedInfo, byte[] decryptedData) {
}
