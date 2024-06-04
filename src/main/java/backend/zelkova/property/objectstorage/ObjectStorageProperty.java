package backend.zelkova.property.objectstorage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component

@Getter
@Setter
@ConfigurationProperties(prefix = "objectstorage.iwinv")
public class ObjectStorageProperty {
    private String accessKey;
    private String secretKey;
    private String region;
    private String endPoint;
    private String s3ForcePathStyle;
    private String bucketName;
}
