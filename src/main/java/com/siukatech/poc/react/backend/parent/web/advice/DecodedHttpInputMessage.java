package com.siukatech.poc.react.backend.parent.web.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

public class DecodedHttpInputMessage implements HttpInputMessage {
    private HttpHeaders httpHeaders;
    private InputStream body;

    public DecodedHttpInputMessage(HttpHeaders httpHeaders, InputStream body) {
        super();
        this.httpHeaders = httpHeaders;
        this.body = body;
    }

    @Override
    public InputStream getBody() throws IOException {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }
}
