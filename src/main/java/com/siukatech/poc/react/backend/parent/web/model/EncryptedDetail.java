package com.siukatech.poc.react.backend.parent.web.model;

public record EncryptedDetail(EncryptedReq encryptedReq
        , EncryptedInfo encryptedInfo, byte[] decryptedData) {
}
