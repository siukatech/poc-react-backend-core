package com.siukatech.poc.react.backend.parent;

import com.siukatech.poc.react.backend.parent.data.config.DataConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.test.autoconfigure.OverrideAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTypeExcludeFilter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

/**
 * To start the SpringBootTest with JPA
 * There are two ways to configure the Test class
 * 1. Use the following annotations without @SpringBootTest
 * 		@DataJpaTest
 * 		@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
 *
 * 2. Use the @SpringBootTest with following annotations
 * 		@OverrideAutoConfiguration(enabled = false)
 * 		@AutoConfigureDataJpa
 *
 * TestPropertySource({"classpath:application.yml"}) is NOT required.
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

	@Test
	void contextLoads() {
	}

}
