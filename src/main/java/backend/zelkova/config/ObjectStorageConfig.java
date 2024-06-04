package backend.zelkova.config;

import backend.zelkova.property.objectstorage.ObjectStorageProperty;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ObjectStorageConfig {

    private final ObjectStorageProperty objectStorageProperty;

    @Bean
    public AmazonS3 objectStorage() {
        EndpointConfiguration endpointConfig =
                new EndpointConfiguration(objectStorageProperty.getEndPoint(), objectStorageProperty.getRegion());
        AWSCredentials credentials =
                new BasicAWSCredentials(objectStorageProperty.getAccessKey(), objectStorageProperty.getSecretKey());

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfig)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
