
# React-Backend-Parent
This is a lib-project for other microservices (ms-projects) to import.  
OAuth2 is integrated for the SSO with Keycloak as the development identity provider (IdP) server.  
End-to-End-Encryption (E2EE) is also introduced in this project.  


# Setup

## Gradle

Gradle is the dependency manager for these poc projects.  
The `java-library` is the plugins in build.gradle file, NOT the `java-platform`.

```groovy
plugins {
    ...
    id 'java-library'
    ...
}
```


By using `java-library`, the ms project used this lib-project which do not need to add the dependencies again.  
Because, the dependencies marked with `api` in this lib-project will be exported to the ms projects.  
This can align the library version along with all projects.  

*lib-project*
```groovy
dependencies {
    ...
    // Use JUnit Jupiter for testing.
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.1'

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api 'org.apache.commons:commons-math3:3.6.1'

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation 'com.google.guava:guava:31.1-jre'
    ...
    api 'org.springframework.boot:spring-boot-starter-data-jpa'
    api 'org.springframework.boot:spring-boot-starter-oauth2-client'
    api 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
    api 'org.springframework.boot:spring-boot-starter-security'
    api 'org.springframework.boot:spring-boot-starter-web'
    ...
}
```

*ms-project*
```groovy
dependencies {
	implementation 'com.siukatech.poc:react-backend-parent:0.0.1-SNAPSHOT'

//	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
//	implementation 'org.springframework.boot:spring-boot-starter-security'
//	implementation 'org.springframework.boot:spring-boot-starter-web'

//	implementation 'org.modelmapper:modelmapper:3.1.1'
//	implementation 'org.hibernate:hibernate-validator:6.0.13.Final'
////	implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2'

//	implementation 'org.projectlombok:lombok:1.18.26'
	annotationProcessor 'org.projectlombok:lombok:1.18.26'
//
	runtimeOnly 'org.postgresql:postgresql:42.6.0'
//
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'org.springframework.security:spring-security-test'
	testImplementation 'com.h2database:h2:2.1.214'
}
```

Besides, this is a library, not a bootable jar.  
Therefore, the configuration of plugin has been updated as below.  
**Reference**  
https://stackoverflow.com/a/55731664
```groovy
plugins {
  ...
  id 'org.springframework.boot' version '3.1.0' apply false
  ...
}
```
```groovy
dependencyManagement {
	imports {
		mavenBom org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
	}
}
```


## Sonatype Nexus (private maven repository) Setup
### Docker

Since I am using Macbook Pro M2 (ARM) with docker desktop as my development environment.  
Therefore this sonatype nexus version is not compatible with other x64 machine.  

- x64: [sonatype/nexus3](https://hub.docker.com/r/sonatype/nexus3/tags)  
- ARM: [klo2k/nexus3:3.43.0](https://hub.docker.com/r/klo2k/nexus3/tags)  


### Nexus (ARM) installation steps

Before pulling the docker image, I will create a local folder (persistence storage) as the volume for the image to mount.  
```shell
echo 'export NEXUS_HOME="~/Documents/development/artifact/nexus"' >> ~/.zshrc
source ~/.zshrc
```

```shell
docker pull klo2k/nexus3:3.43.0
docker run -d -p 38081:8081 --name klo2k-nexus3-01 -v $NEXUS_HOME/klo2k-nexus3:/nexus-data klo2k/nexus3:3.43.0
```

Replace ***[container_id]*** to actual container-id by using `docker ps -a` to check.  
And password can be found in the admin.password file to check the initial admin password.  
```shell
docker exec -it [container_id] /bin/bash
more /nexus-data/admin.password
```

After the launch, u can directly go to the url (http://localhost:38081) to access the nexus repository.  
Or perform a `curl http://localhost:38081/nexus/service/local/status` to check status.  




### Gradle

```groovy
plugins {
    ...
    id 'maven-publish'
    ...
}
```

```groovy
repositories {
	mavenCentral()
}
```

```groovy
publishing {
	publications {
		privateMaven(MavenPublication) {
			def artifactIdStr = "$project.name"
			groupId "$project.group"
			version "$project.version"
			artifactId "$artifactIdStr"
			from components.java
			versionMapping {
				usage('java-api') {
					fromResolutionOf('runtimeClasspath')
				}
				usage('java-runtime') {
					fromResolutionResult()
				}
			}
			pom {
				name = "$artifactId"
				description = "$artifactId"
			}
		}
	}
	repositories {
		maven {
			name = '[repository-name]'
			allowInsecureProtocol = true
			credentials(PasswordCredentials)
			url = uri("$[repository-name]Uri")
		}
	}
}
```

The `platformSnapshot` here is a name that can lookup the login and password from ~/.gradle/gradle.properties.  
Here is the reference from gradle official website.  
https://docs.gradle.org/current/samples/sample_publishing_credentials.html  

Snippet of ~/.gradle/gradle.properties
```shell
...
[repository-name]Username=[user-login]
[repository-name]Password=[user-password]
[repository-name]Uri=[repository-url]
...
```

After the setup of above configuration, our gradle task list will be updated.  
New folder `publishing` is added. Tasks `publishXXX` are used to publish the artifacts to maven repository.  
The [repository-name] on screen-capture is `platformSnapshot`.  
![Gradle Publishing 01](./assets/gradle-01-publishing-01.png)

Once `publishToMavenLocal` is clicked, the artifact will be published to your maven local. (~/.m2/repository/)  


# Design

## Dependencies

*api*
- OAuth2 is required because our ms-projects are linked to an IdP.  
As a result, the spring-boot-starter oauth2-client and oauth2-resource-server are selected.  
- spring-boot-starter data-jpa is selected for the data persistence.  
Maybe will add Mybatis later to do some poc.  
- spring-boot-starter spring-security is the must to protect the resources.  
- Others like modelmapper and jackson (objectmapper) are included for data binding between objects.  
- Apache common-lang3 is the utility library to improve our productivity.  

*testImplementation*
- spring-boot-starter-test is the main testing framework
- spring-security-test is required for authorization and authentication testing
- h2database is used to work as database during testing the repository components.  


## Package
- main/java
  - business: Business services
  - data: Data layer, repositories and entities
  - global: Global configuration for application
  - security: Security configuration and related classes
  - util: Utility classes
  - web
    - advice: ControllerAdvice classes
    - annotation: Custom annotations for web
    - config: Web configurations
    - context: Custom context for request-scope
    - controller: Web controller
    - helper: Helper to perform some specific logics
    - model: Form models, request models
- test/java
  - business
  - data
  - global 
  - util
  - web
  - AbstractJpaTests: Abstract class with Jpa testing specific properties
  - AbstractUnitTests: Abstract class with some common unit test configuration


## Annotation
There are several annotations created for rest controller.
- base
  - PublicController
  - ProtectedController
  - EncryptedController
- v1
  - ApiV1Controller
  - PublicApiV1Controller
  - ProtectedApiV1Controller
  - EncryptedApiV1Controller

And in the coming future, more will be added like.  
- v**2**
  - ApiV**2**Controller
  - PublicApiV**2**Controller
  - ProtectedApiV**2**Controller
  - EncryptedApiV**2**Controller
- and etc...

The idea here is that when there is a new api version coming out which means there are some breaking changes.  
The corresponding annotations are required to created in this project.  
Since annotation has a limitation that does not support inheritance (Reference: https://stackoverflow.com/a/7761568).  
The annotations in `base` are defined as baseline, common usage which are generic approach and required to attach to `[XXX]ApiV[X]Controller` series annotations.  
Some embedded interceptors are planning to develop to cross-check the `base` annotations for security control.  


# End-to-End-Encryption (E2EE)
Besides the annotation control, I am trying the end-to-end-encryption (E2EE) in this project by using the `EncryptedController` annotation.  
Two encryption algorithms are used for the solution.  
- RSA with key-size 2048 bytes
- AES (AES/CBC/PKCS5Padding) with secret-size 512 bytes

## Sequence flow after login
1. frontend gets `user-public-key` from backend (/v1/protected/my/public-key)
2. frontend encrypts `original-payload-str` by `crypto-js` (AES/CBC/PKCS5Padding) to `aes-cbc-str`
3. frontend encrypts `aes-cbc-str` and `aes-key` by `jsencrypt` with `user-public-key` to `request-body-str`
4. backend decrypts `request-body-str` by `user-private-key` to `aes-cbc-str` in `RequestBodyAdviceAdapter.beforeBodyRead`
5. backend decrypts `aes-cbc-str` by `aes-key` (AES/CBC/PKCS5Padding) to `original-payload-str`
6. backend passes `original-payload-str` to controller through the return of `RequestBodyAdvice.beforeBodyRead`
7. backend encrypts `original-response-entity` by `aes-key` to ~~`aes-ecb-str`~~ `aes-cbc-str` (~~AES/ECB/PKCS7Padding~~ AES/CBC/PKCS7Padding)
8. backend returns ~~`aes-ecb-str`~~ `aes-cbc-str` as response through `ResponseBodyAdvice.beforeBodyWrite`
9. frontend receives ~~`aes-ecb-str`~~ `aes-cbc-str` from response
10. frontend decrypts ~~`aes-ecb-str`~~ `aes-cbc-str` by `crypto-js` (~~AES/ECB/PKCS7Padding~~ AES/CBC/PKCS7Padding) to `response-entity-str`
11. frontend parses `response-entity-str` to `response-entity`

## Findings
The `jsencrypt` does not support using user-public-key to decrypt.  
The better approach should be using user-private-key to encrypt at backend and frontend uses the user-public-key to decrypt.  
~~On frontend, the `crypto-js` (AES/CBC) is hard to decrypt the java `aes-str` (AES/CBC).~~  
~~Finally only the `crypto-js` (AES/ECB) can decrypt java `aes-str` (AES/ECB).~~  
On frontend, the `crypto-js` (AES/CBC) can decrypt the java `aes-str` (AES/CBC) with correct `iv`.  
The `iv` is required the byte array format which means the decoding should be happened before.  
The `CryptoJS.enc.Base64.parse([base64-str])` decodes `base64-str` to `byte-array`.  

**Reference**  
https://github.com/kyungw00k/encrypt-something-in-java-and-decrypt-it-in-javascript-by-example  

```javascript
    ...
    const decryptedDataBase64 = dataRet;
    const encryptedAesData = CryptoJS.enc.Base64.parse(decryptedDataBase64);
    const decodedKey = CryptoJS.enc.Base64.parse(cipherInfo.key);
    const decodedKeyStr = decodedKey.toString(CryptoJS.format.Utf8);
    const decodedIv = CryptoJS.enc.Base64.parse(cipherInfo.iv);
    const decryptedData = CryptoJS.AES.decrypt(
      { ciphertext: encryptedAesData },
      decodedKey,
      {
        // mode: CryptoJS.mode.ECB,
        mode: CryptoJS.mode.CBC,
        iv: decodedIv,
      }
    );
    const dataStr = decryptedData.toString(CryptoJS.enc.Utf8);
    dataRet = JSON.parse(dataStr);
    ...
```

Besides, there is a RSA key length limitation. 
The RSA key cannot encrypt content that exceeds the defined length (key-size minuses some factors).  
As a result, RSA is designed to encrypt small content, such as `aes-key`.  
The proper way should be similar to reference below.  
1. Generate a 256-bit random `key-str`  
2. Encrypt data with `AES/CBC` as `aes-data` with `key-str`
3. Encrypt `key-str` with RSA key-pair, either `public-key` or `private-key`
4. Send encrypted `key-str` and `aes-data` to the other side  

Since the length of `aes-key` is fixed, currently is `344` for this setup.  
Then I composed the `aes-key` and `aes-data` together.  
And split them by the fixed length rather than adding separator.  

Moreover, there is not payload embedded in GET method calls.  
The `aes-key` is also attached to `http-header` for `ResponseBodyAdvice.beforeBodyWrite` to perform the `aes-key` decryption and `response-body` encryption.

I also tried the AES/GCM on backend but failed on frontend.  
So the AES/CBC is default encryption algorithm.  

**Reference**  
https://mbed-tls.readthedocs.io/en/latest/kb/cryptography/rsa-encryption-maximum-data-size/

