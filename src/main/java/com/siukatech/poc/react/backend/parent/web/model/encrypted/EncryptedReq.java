package com.siukatech.poc.react.backend.parent.web.model.encrypted;


import com.siukatech.poc.react.backend.parent.web.model.encrypted.EncryptedInfo;

public record EncryptedReq(String cipher, EncryptedInfo info) {}
