package com.siukatech.poc.react.backend.core.security.provider;

import com.siukatech.poc.react.backend.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.core.global.config.AppCoreProp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class RemoteAuthorizationDataProviderTests extends AbstractUnitTests {

    @InjectMocks
    private RemoteAuthorizationDataProvider remoteAuthorizationDataProvider;

    @Mock
    private RestTemplate oauth2ClientRestTemplate;

    @Mock
    private AppCoreProp appCoreProp;


    @Test
    void contextLoads() {
        // TODO
        Assertions.assertNotNull(log);
        log.debug("contextLoads - testing logging");
    }

}
