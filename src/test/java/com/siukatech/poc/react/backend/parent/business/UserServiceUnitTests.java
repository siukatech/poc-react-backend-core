package com.siukatech.poc.react.backend.parent.business;

import com.siukatech.poc.react.backend.parent.AbstractUnitTests;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.data.entity.UserEntity;
import com.siukatech.poc.react.backend.parent.data.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@ContextConfiguration(classes = {WebConfig.class})
public class UserServiceUnitTests extends AbstractUnitTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @InjectMocks
    private UserService userService;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private UserRepository userRepository;

    @BeforeAll
    public static void init() {
//        final ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
//        rootLogger.setLevel(Level.ALL);

        // If sub-class has her own init, then super-class's init is required to trigger manually
        AbstractUnitTests.init();
    }

    @AfterAll()
    public static void terminate() {
        AbstractUnitTests.terminate();
    }

    @BeforeEach
    public void setup() {
//        // get Logback Logger
//        Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
//
//        // create and start a ListAppender
//        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
//        listAppender.start();
//
//        // add the appender to the logger
//        logger.addAppender(listAppender);
//        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        logger.setLevel(Level.ALL);

        logger.debug("setup");
    }

    @AfterEach()
    public void teardown() {
        logger.debug("teardown");
    }

    @Test
    public void findByUserId_basic() {
        // given
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("app-user-01");
        userEntity.setName("App-User-01");
        userEntity.setPublicKey("public-key");
        userEntity.setPrivateKey("private-key");
        userEntity.setVersionNo(1L);
        when(this.userRepository.findByUserId(anyString())).thenReturn(Optional.of(userEntity));

        // when
        UserDto userDtoActual = this.userService.findByUserId("app-user-01");

        // then / verify
        assertThat(userDtoActual.getUserId()).isEqualTo("app-user-01");
    }

}
