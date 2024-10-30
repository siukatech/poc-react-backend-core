package com.siukatech.poc.react.backend.parent.user.controller;

import com.siukatech.poc.react.backend.parent.AbstractWebTests;
import com.siukatech.poc.react.backend.parent.business.dto.*;
import com.siukatech.poc.react.backend.parent.user.controller.MyController;
import com.siukatech.poc.react.backend.parent.user.service.UserService;
import com.siukatech.poc.react.backend.parent.security.authentication.MyAuthenticationToken;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = {MyController.class})
@AutoConfigureMockMvc(addFilters = false)
//@AutoConfigureWebClient
public class MyControllerTests extends AbstractWebTests {

    /**
     * Reference:
     * https://stackoverflow.com/a/72086318
     * https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/setup.html#test-mockmvc-setup
     * https://docs.spring.io/spring-security/reference/servlet/test/mockmvc/authentication.html
     * <p>
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
    @MockBean
    private JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

//    @MockBean
//    private ParentAppProp parentAppProp;
//    @MockBean
//    private RestTemplateBuilder restTemplateBuilder;
//    @MockBean
//    private RestTemplate oauth2ClientRestTemplate;


    private UserDto prepareUserDto_basic() {
        UserDto userDto = new UserDto();
        userDto.setLoginId("app-user-01");
        userDto.setName("App-User-01");
        userDto.setPublicKey("public-key");
//        userDto.setPrivateKey("private-key");
        return userDto;
    }

    private UserViewDto prepareUserViewDto_basic() {
        UserViewDto userViewDto = new UserViewDto();
        userViewDto.setLoginId("app-user-01");
        userViewDto.setName("App-User-01");
        userViewDto.setPublicKey("public-key");
//        userDto.setPrivateKey("private-key");
        return userViewDto;
    }

    private MyKeyDto prepareMyKeyDto_basic() {
        MyKeyDto myKeyDto = new MyKeyDto();
        myKeyDto.setLoginId("app-user-01");
        myKeyDto.setPublicKey("public-key");
        myKeyDto.setPrivateKey("private-key");
        return myKeyDto;
    }


    private List<UserPermissionDto> prepareUserPermissions_basic() {
        String[][] userPermissionTempsArr = new String[][]{
                new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.home", "view"}
                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.items", "*"}
//                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.shops", "view"}
                , new String[]{"app-user-01", "1", "role-users-01", "frontend-app", "menu.merchants", "view"}
        };
        List<UserPermissionDto> userPermissionDtoList = new ArrayList<>();
        for (String[] userPermissionTemps : userPermissionTempsArr) {
            userPermissionDtoList.add(new UserPermissionDto());
            userPermissionDtoList.get(userPermissionDtoList.size() - 1).setLoginId(userPermissionTemps[0]);
            userPermissionDtoList.get(userPermissionDtoList.size() - 1).setUserId(userPermissionTemps[1]);
            userPermissionDtoList.get(userPermissionDtoList.size() - 1).setUserRoleId(userPermissionTemps[2]);
            userPermissionDtoList.get(userPermissionDtoList.size() - 1).setApplicationId(userPermissionTemps[3]);
            userPermissionDtoList.get(userPermissionDtoList.size() - 1).setAppResourceId(userPermissionTemps[4]);
            userPermissionDtoList.get(userPermissionDtoList.size() - 1).setAccessRight(userPermissionTemps[5]);
        }
        return userPermissionDtoList;
    }

    private UsernamePasswordAuthenticationToken prepareUsernamePasswordAuthenticationToken_basic() {
        return prepareUsernamePasswordAuthenticationToken("app-user-01");
    }

    private MyAuthenticationToken prepareMyAuthenticationToken_basic() {
        return prepareMyAuthenticationToken("app-user-01", 1L);
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
        log.debug("setup - SecurityContextHolder.getContext.getAuthentication: [{}]"
                , SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
//    @WithMockUser("app-user-01")
    public void getPublicKey_basic() throws Exception {
        // given
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("getPublicKey_basic - authentication: [" + authentication + "]");

        MyKeyDto myKeyDto = this.prepareMyKeyDto_basic();
        when(userService.findKeyByLoginId(anyString())).thenReturn(myKeyDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
//                        + "/my/public-key")
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/public-key")
//                .with(authentication(prepareUsernamePasswordAuthenticationToken_basic()))
                .with(authentication(prepareMyAuthenticationToken_basic()))
                .with(csrf())
                //.with(SecurityMockMvcRequestPostProcessors.users((UserDetails) authentication.getPrincipal()))
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("public-key"))
                .andReturn();

        // result
        log.debug("getPublicKey_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");

    }

    @Test
    public void getKeyInfo_basic() throws Exception {
        // given
        MyKeyDto myKeyDto = this.prepareMyKeyDto_basic();
        when(userService.findKeyByLoginId(anyString())).thenReturn(myKeyDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
//                        + "/my/key-info")
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/key-info")
                .with(authentication(prepareUsernamePasswordAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{loginId: \"app-user-01\"}"))
                .andReturn();

        // result
        log.debug("getKeyInfo_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");

    }

    @Test
    public void getUserInfo_basic() throws Exception {
        // given
        UserDto userDto = this.prepareUserDto_basic();
        when(userService.findByLoginId(anyString())).thenReturn(userDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .post(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
//                        + "/my/user-info")
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/user-info")
                .with(authentication(prepareUsernamePasswordAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{loginId: \"app-user-01\"}"))
                .andReturn();

        // result
        log.debug("getUserInfo_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");

    }

    @Test
    public void getUserPermissions_basic() throws Exception {
        // given
        List<UserPermissionDto> userPermissionDtoList = this.prepareUserPermissions_basic();
        when(userService.findPermissionsByLoginIdAndApplicationId(anyString(), anyString())).thenReturn(userPermissionDtoList);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX + "/my/permissions?applicationId={applicationId}", "frontend-app")
                .with(authentication(prepareUsernamePasswordAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
//                .andExpect(content().json())
                .andExpect(content().string(containsString("appResourceId")))
                .andReturn();

        // result
        log.debug("getUserPermissions_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");
    }

    @Test
    public void getUserPermissionInfo_basic() throws Exception {
        // given
        List<UserPermissionDto> userPermissionDtoList = this.prepareUserPermissions_basic();
        when(userService.findPermissionsByLoginIdAndApplicationId(anyString(), anyString())).thenReturn(userPermissionDtoList);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX + "/my/permission-info?applicationId={applicationId}", "frontend-app")
                .with(authentication(prepareUsernamePasswordAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().json())
                .andExpect(content().string(containsString("appResourceId")))
                .andReturn();

        // result
        log.debug("getUserPermissionInfo_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");
    }

    @Test
    public void getUserView_basic() throws Exception {
        // given
        UserViewDto userViewDto = this.prepareUserViewDto_basic();
        when(userService.findViewByLoginId(anyString())).thenReturn(userViewDto);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX
                        + "/my/user-view")
                .with(authentication(prepareUsernamePasswordAuthenticationToken_basic()))
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        // then / verify
        MvcResult mvcResult = this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().json("{loginId: \"app-user-01\"}"))
                .andReturn();

        // result
        log.debug("getUserView_basic - end - mvcResult.getResponse.getContentAsString: [" + mvcResult.getResponse().getContentAsString() + "]");

    }

}
