package backend.zelkova.attachment.operator;

import backend.zelkova.attachment.dto.response.AttachmentResponse;
import backend.zelkova.exception.CustomException;
import backend.zelkova.exception.ExceptionStatus;
import backend.zelkova.property.objectstorage.ObjectStorageProperty;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class AttachmentManager {

    private static final String DELIMITER = "/";
    private static final String ORIGINAL_FILE_NAME = "original-file-name";

    private final ObjectStorageProperty objectStorageProperty;
    private final AmazonS3 objectStorage;

    @Value("${area.objectstorage}")
    private String area;

    public void uploadAttachments(String domain, Long domainId, List<MultipartFile> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }

        attachments.forEach(attachment -> this.upload(domain, domainId, attachment));
    }

    private void upload(String domain, Long domainId, MultipartFile multipartFile) {

        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            return;
        }

        String savePath = calculateSavePath(domain, domainId);
        ObjectMetadata objectMetadata = createObjectMetadata(multipartFile);

        try {
            objectStorage.putObject(objectStorageProperty.getBucketName(), savePath, multipartFile.getInputStream(),
                    objectMetadata);
        } catch (IOException ignore) {
            throw new CustomException(ExceptionStatus.FAIL_CONVERT);
        }
    }

    private String calculateSavePath(String domain, Long domainId) {
        String prefix = calculatePrefix(domain, domainId);
        return prefix + UUID.randomUUID();
    }

    private String calculatePrefix(String domain, Long domainId) {
        StringJoiner stringJoiner = new StringJoiner(DELIMITER);

        stringJoiner.add(area);
        stringJoiner.add(domain);
        stringJoiner.add(domainId.toString());

        return stringJoiner + DELIMITER;
    }

    private static ObjectMetadata createObjectMetadata(MultipartFile multipartFile) {
        String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.addUserMetadata(ORIGINAL_FILE_NAME, URLEncoder.encode(originalFilename, StandardCharsets.UTF_8));

        return objectMetadata;
    }

    public List<AttachmentResponse> findFiles(String domain, Long domainId) {
        return objectStorage.listObjectsV2(objectStorageProperty.getBucketName(), calculatePrefix(domain, domainId))
                .getObjectSummaries().stream()
                .map(this::toResponse)
                .toList();
    }

    private AttachmentResponse toResponse(S3ObjectSummary s3ObjectSummary) {
        String key = s3ObjectSummary.getKey();
        ObjectMetadata objectMetadata = objectStorage.getObjectMetadata(objectStorageProperty.getBucketName(), key);
        String originalFileNameMetaData = objectMetadata.getUserMetaDataOf(ORIGINAL_FILE_NAME);

        String originalFileName = getOriginalFileName(originalFileNameMetaData, key);
        String url = getUrl(key);

        return new AttachmentResponse(originalFileName, key, url);
    }

    private String getOriginalFileName(String originalFileNameMetaData, String key) {
        if (Objects.isNull(originalFileNameMetaData)) {
            int lastDelimiterIndex = key.lastIndexOf(DELIMITER);
            return key.substring(lastDelimiterIndex + 1);
        }

        return URLDecoder.decode(originalFileNameMetaData, StandardCharsets.UTF_8);
    }

    private String getUrl(String key) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(objectStorageProperty.getBucketName(), key)
                        .withMethod(HttpMethod.GET);

        URL url = objectStorage.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    public void deleteAttachments(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }

        keys.forEach(this::delete);
    }

    private void delete(String key) {
        objectStorage.deleteObject(objectStorageProperty.getBucketName(), key);
    }

    public void deleteAll(String domain, Long domainId) {
        objectStorage.listObjectsV2(objectStorageProperty.getBucketName(), calculatePrefix(domain, domainId))
                .getObjectSummaries()
                .forEach(s3ObjectSummary -> delete(s3ObjectSummary.getKey()));
    }
}
