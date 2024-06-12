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

    public void uploadAttachments(Long postId, List<MultipartFile> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return;
        }

        attachments.forEach(attachment -> this.upload(postId, attachment));
    }

    private void upload(Long postId, MultipartFile multipartFile) {

        if (Objects.isNull(multipartFile) || multipartFile.isEmpty()) {
            return;
        }

        String savePath = calculateSavePath(postId);
        ObjectMetadata objectMetadata = createObjectMetadata(multipartFile);

        try {
            objectStorage.putObject(objectStorageProperty.getBucketName(), savePath, multipartFile.getInputStream(),
                    objectMetadata);
        } catch (IOException ignore) {
            throw new CustomException(ExceptionStatus.FAIL_CONVERT);
        }
    }

    private static String calculateSavePath(Long postId) {
        StringJoiner stringJoiner = new StringJoiner(DELIMITER);

        stringJoiner.add("post");
        stringJoiner.add(postId.toString());
        stringJoiner.add(UUID.randomUUID().toString());

        return stringJoiner.toString();
    }

    private static ObjectMetadata createObjectMetadata(MultipartFile multipartFile) {
        String originalFilename = Objects.requireNonNull(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.addUserMetadata(ORIGINAL_FILE_NAME, URLEncoder.encode(originalFilename, StandardCharsets.UTF_8));

        return objectMetadata;
    }

    public List<AttachmentResponse> findFiles(Long postId) {
        return objectStorage.listObjectsV2(objectStorageProperty.getBucketName(), "post/" + postId)
                .getObjectSummaries().stream()
                .map(this::toResponse)
                .toList();
    }

    private AttachmentResponse toResponse(S3ObjectSummary s3ObjectSummary) {
        String key = s3ObjectSummary.getKey();
        ObjectMetadata objectMetadata = objectStorage.getObjectMetadata(objectStorageProperty.getBucketName(), key);
        String originalFileName = URLDecoder.decode(objectMetadata.getUserMetaDataOf(ORIGINAL_FILE_NAME),
                StandardCharsets.UTF_8);

        String url = getUrl(key);

        return new AttachmentResponse(originalFileName, key, url);
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

    public void deleteAll(Long postId) {
        objectStorage.listObjectsV2(objectStorageProperty.getBucketName(), "post/" + postId)
                .getObjectSummaries()
                .forEach(s3ObjectSummary -> delete(s3ObjectSummary.getKey()));
    }
}
