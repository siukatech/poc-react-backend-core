package com.siukatech.poc.react.backend.parent;

import com.siukatech.poc.react.backend.parent.security.provider.AuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.security.provider.DatabaseAuthorizationDataProvider;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserPermissionRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserRepository;
import com.siukatech.poc.react.backend.parent.security.provider.database.repository.UserViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * <pre>
 * To start the SpringBootTest with JPA.
 * There are two ways to configure the Test class.
 * 1. Use the following annotations without {@literal @}SpringBootTest
 *        {@literal @}DataJpaTest
 *        {@literal @}AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 *
 * 2. Use the {@literal @}SpringBootTest with following annotations
 *        {@literal @}OverrideAutoConfiguration(enabled = false)
 *        {@literal @}AutoConfigureDataJpa
 *
 * TestPropertySource({"classpath:application.yml"}) is NOT required.
 *
 * </pre>
 *
 * // Reference:
 * // https://www.jvt.me/posts/2022/02/01/resttemplate-integration-test/
 * @AutoConfigureWebClient(registerRestTemplate = true)
 *
 */

@Slf4j
@SpringBootTest(
////	properties = {
////		"client-id=XXX"
////		, "client-secret=XXX"
////		, "client-realm=react-backend-realm"
////		, "spring.profiles.active=dev"
//////		, "oauth2.client.keycloak=http://localhost:38180"
////		, "oauth2.client.keycloak=XXX"
////	}
)
//@EntityScan("com.siukatech.poc.react.backend.parent.data.entity")
//@EnableJpaRepositories("com.siukatech.poc.react.backend.parent.data.repository")
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//
//@ExtendWith(SpringExtension.class)
@OverrideAutoConfiguration(enabled = false)
//@TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
//@Transactional
//@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureWebClient(registerRestTemplate = true)
//@AutoConfigureTestDatabase
//@AutoConfigureTestEntityManager
////@TestPropertySource({
////		"classpath:application.yml"
////})
//@TestPropertySource({"classpath:abstract-jpa-tests.properties"
//		, "classpath:abstract-oauth2-tests.properties"})
public class ReactBackendParentTests {

    @MockBean
//    @SpyBean
//    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserPermissionRepository userPermissionRepository;
    @MockBean
    private UserViewRepository userViewRepository;

    // not working, start
//    @MockBean
//    private TestRestTemplate oauth2ClientRestTemplate;
    // not working, end

    @Test
    void contextLoads() {
        Assertions.assertNotNull(oAuth2ClientProperties);
    }

}
