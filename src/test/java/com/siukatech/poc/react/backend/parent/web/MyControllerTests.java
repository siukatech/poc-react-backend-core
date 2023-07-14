package com.siukatech.poc.react.backend.parent.web;

import com.siukatech.poc.react.backend.parent.AbstractWebTests;
import com.siukatech.poc.react.backend.parent.business.dto.UserDto;
import com.siukatech.poc.react.backend.parent.business.service.UserService;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import com.siukatech.poc.react.backend.parent.web.context.EncryptedBodyContext;
import com.siukatech.poc.react.backend.parent.web.controller.MyController;
import com.siukatech.poc.react.backend.parent.web.helper.EncryptedBodyAdviceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {MyController.class})
@AutoConfigureMockMvc(addFilters = false)
public class MyControllerTests extends AbstractWebTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired

    /**
     * Reference:
     * https://stackoverflow.com/a/72086318
     * https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/setup.html#test-mockmvc-setup
     * https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/authentication.html
     *
     * When we inject authentication or other security related bean to our controller methods.
     * The spring-security must be set up during the MockMvc creation.
     * We need to inject the WebApplicationContext for the custom MockMvc creation.
     * Therefore, the @Autowired annotation for MockMvc cannot be used in these situations.
     */
//    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;


    private UserDto prepareUserDto_basic() {
        UserDto userDto = new UserDto();
        userDto.setUserId("app-user-01");
        userDto.setName("App-User-01");
        userDto.setPublicKey("public-key");
        userDto.setPrivateKey("private-key");
        return userDto;
    }

    private UsernamePasswordAuthenticationToken prepareAuthenticationToken_basic() {
        List<GrantedAuthority> convertedAuthorities = new ArrayList<GrantedAuthority>();
        UserDetails userDetails = new User("app-user-01", "", convertedAuthorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        return authenticationToken;
    }

//    @BeforeAll
//    public static void init() {
//    }

    @BeforeEach
    public void setup(TestInfo testInfo) {
        Method method = testInfo.getTestMethod().get();
        switch (method.getName()) {
            case "getPublicKey_basic":
            case "getUserInfo_basic":
            default:
        }
        //
        // We dont need to setup the authentication in @BeforeEach method.
        // After applying .with(authentication([mock-authentication-object]))
        // This will also add the mock-authentication-object to the SecurityContext
//        UsernamePasswordAuthenticationToken authenticationToken = prepareAuthenticationToken_basic();
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //
        // Refer to the explanation of MockMvc above.
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .apply(springSecurity())
                    .build();
        }
        //
        logger.debug("setup - SecurityContextHolder.getContext.getAuthentication: [{}]"
                , SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
//    @WithMockUser("app-user-01")
    public void getPublicKey_basic() throws Exception {
        // given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("getPublicKey_basic - authentication: [" + authentication + "]");

        UserDto userDto = this.prepareUserDto_basic();
        when(userService.findByUserId(anyString())).thenReturn(userDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/public-key")
                .with(authentication(prepareAuthenticationToken_basic()))
                .with(csrf())
                //.with(SecurityMockMvcRequestPostProcessors.user((UserDetails) authentication.getPrincipal()))
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("public-key"))
                .andReturn();

        // result
        logger.debug("getPublicKey_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");

    }

    @Test
    public void getUserInfo_basic() throws Exception {
        // given
        UserDto userDto = this.prepareUserDto_basic();
        when(userService.findByUserId(anyString())).thenReturn(userDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/user-info")
                .with(authentication(prepareAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{userId: \"app-user-01\"}"))
                .andReturn();

        // result
        logger.debug("getUserInfo_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");

    }

}
