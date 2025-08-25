package com.dt.find_restaurant.image.application;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import jakarta.validation.constraints.NotNull;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final AmazonS3Client amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public Map<String, String> getPresignedUrl(String prefix, String fileName) {
        if (!prefix.isEmpty()) {
            fileName = createPath(prefix, fileName);
        }

        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedPutUrlRequest(bucketName,
                fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return Map.of("url", url.toString());
    }


    public Map<String, String> getPresignedUrl(String images, @NotNull List<String> fileNames) {
        if (fileNames.isEmpty()) {
            throw new IllegalArgumentException("파일 이름 리스트는 비어 있을 수 없습니다.");
        }

        Map<String, String> res = new HashMap<>();
        int index = 1;

        for (String fileName : fileNames) {
            String path = createPath(images, fileName);
            GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedPutUrlRequest(bucketName,
                    path);
            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            res.put("url" + index, url.toString());
            index++;
        }

        return res;
    }

    public Map<String, String> getDeletePreSingedUrl(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedDeleteUrlRequest(bucketName,
                fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return Map.of("url", url.toString());
    }

    private GeneratePresignedUrlRequest getGeneratePresignedPutUrlRequest(String bucket, String fileName) {
        return new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(getPresignedUrlExpiration());
    }

    public GeneratePresignedUrlRequest getGeneratePresignedDeleteUrlRequest(String bucketName, String objectKey) {
        // URL 만료 시간 설정
        Date expiration = new Date(System.currentTimeMillis() + 120000); // 2분 후 만료

        return new GeneratePresignedUrlRequest(bucketName, objectKey)
                .withMethod(HttpMethod.DELETE)
                .withExpiration(expiration);
    }

    private Date getPresignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2; // 2분
        expiration.setTime(expTimeMillis);

        return expiration;
    }

    private String createFileId() {
        return UUID.randomUUID().toString();
    }

    private String createPath(String prefix, String fileName) {
        String fileId = createFileId();
        return String.format("%s/%s", prefix, fileId + "-" + fileName);
    }

}
