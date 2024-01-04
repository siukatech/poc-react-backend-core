package com.siukatech.poc.react.backend.parent.util;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class URLEncoderUtilsTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    void contextLoads() {
    }

    /**
     * https://stackoverflow.com/a/44474607
     * openssl genrsa -out private_pkcs8.key 2048
     * openssl rsa -in private_pkcs8.key -pubout -out public_x509.crt
     *
     * @throws Exception
     */
    @Test
    void test_urlEncoderUtils_encodeToQueryString() throws Exception {
        List<NameValuePair> nameValuePairList = Arrays.asList(
                new BasicNameValuePair("name1", "https://www.google.com/")
                , new BasicNameValuePair("name2", "https://www.google.com/")
                , new BasicNameValuePair("name3", "https://www.google.com/")
        );
        String queryString = URLEncoderUtils.encodeToQueryString(nameValuePairList);
        String queryStringExpected = "name1=https%3A%2F%2Fwww.google.com%2F"
                + "&" + "name2=https%3A%2F%2Fwww.google.com%2F"
                + "&" + "name3=https%3A%2F%2Fwww.google.com%2F";
        assertEquals("queryString should be encoded", queryString, queryStringExpected);
    }

}
