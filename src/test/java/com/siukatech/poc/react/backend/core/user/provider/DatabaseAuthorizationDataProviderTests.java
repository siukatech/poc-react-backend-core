package com.siukatech.poc.react.backend.core.user.provider;

import com.siukatech.poc.react.backend.core.AbstractUnitTests;
import com.siukatech.poc.react.backend.core.global.config.AppCoreProp;
import com.siukatech.poc.react.backend.core.user.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.core.user.repository.UserRepository;
import com.siukatech.poc.react.backend.core.user.repository.UserViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DatabaseAuthorizationDataProviderTests extends AbstractUnitTests {

    @InjectMocks
    private DatabaseAuthorizationDataProvider databaseAuthorizationDataProvider;
    @Mock
    private AppCoreProp appCoreProp;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserPermissionRepository userPermissionRepository;
    @Mock
    private UserViewRepository userViewRepository;


    @Test
    void contextLoads() {
        // TODO
        Assertions.assertNotNull(log);
        log.debug("contextLoads - testing logging");
    }

}
