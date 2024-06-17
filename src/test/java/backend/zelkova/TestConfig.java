package backend.zelkova;

import backend.zelkova.property.objectstorage.ObjectStorageProperty;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@TestConfiguration
@EnableConfigurationProperties(ObjectStorageProperty.class)
public class TestConfig {

    @Bean
    public ObjectStorageProperty objectStorageProperty() {
        ObjectStorageProperty properties = new ObjectStorageProperty();
        properties.setAccessKey("test-access-key");
        properties.setSecretKey("test-secret-key");
        properties.setRegion("test-region");
        properties.setEndPoint("http://test-endpoint.com");
        properties.setS3ForcePathStyle("true");
        properties.setBucketName("test-bucket-name");
        return properties;
    }

    @MockBean
    public AmazonS3 objectStorage;

    @MockBean
    public ClientRegistrationRepository clientRegistrationRepository;
}
