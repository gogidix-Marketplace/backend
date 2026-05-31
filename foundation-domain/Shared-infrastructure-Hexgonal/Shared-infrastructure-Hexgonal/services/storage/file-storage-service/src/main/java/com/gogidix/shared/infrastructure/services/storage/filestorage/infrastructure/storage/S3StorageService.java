package com.gogidix.shared.infrastructure.services.storage.filestorage.infrastructure.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * S3 Storage Service.
 * <p>
 * Handles file storage operations with AWS S3.
 */
@Service
public class S3StorageService {

    private static final Logger log = LoggerFactory.getLogger(S3StorageService.class);

    private final S3Client s3Client;

    @Value("${storage.s3.bucket-name:gogidix-files}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Uploads a file to S3.
     *
     * @param key the S3 key
     * @param inputStream the file input stream
     * @param contentType the content type
     * @param contentLength the content length
     * @return the etag of the uploaded file
     */
    public String uploadFile(String key, InputStream inputStream, String contentType, long contentLength) {
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(contentLength)
                    .build();

            RequestBody requestBody = RequestBody.fromInputStream(inputStream, contentLength);
            PutObjectResponse response = s3Client.putObject(putRequest, requestBody);

            log.info("File uploaded to S3: key={}, etag={}", key, response.eTag());

            return response.eTag();

        } catch (Exception e) {
            log.error("Failed to upload file to S3: key={}", key, e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    /**
     * Downloads a file from S3.
     *
     * @param key the S3 key
     * @return the file input stream
     */
    public InputStream downloadFile(String key) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            return s3Client.getObject(getRequest);

        } catch (Exception e) {
            log.error("Failed to download file from S3: key={}", key, e);
            throw new RuntimeException("Failed to download file from S3", e);
        }
    }

    /**
     * Deletes a file from S3.
     *
     * @param key the S3 key
     */
    public void deleteFile(String key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

            log.info("File deleted from S3: key={}", key);

        } catch (Exception e) {
            log.error("Failed to delete file from S3: key={}", key, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    /**
     * Generates a presigned URL for a file.
     *
     * @param key the S3 key
     * @param expirationMinutes the expiration time in minutes
     * @return the presigned URL
     */
    public String generatePresignedUrl(String key, int expirationMinutes) {
        // TODO: Implement presigned URL generation
        log.info("Presigned URL generation not yet implemented for key: {}", key);
        return null;
    }
}
