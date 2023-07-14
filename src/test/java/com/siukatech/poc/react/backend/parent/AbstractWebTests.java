package com.siukatech.poc.react.backend.parent;

import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.web.context.EncryptedBodyContext;
import com.siukatech.poc.react.backend.parent.web.helper.EncryptedBodyAdviceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

public abstract class AbstractWebTests extends AbstractUnitTests {
    @SpyBean
    protected EncryptedBodyContext encryptedBodyContext;
    @MockBean
    protected EncryptedBodyAdviceHelper encryptedBodyAdviceHelper;
//    @MockBean
//    private InMemoryClientRegistrationRepository clientRegistrationRepository;

}
