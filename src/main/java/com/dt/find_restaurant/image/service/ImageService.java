package com.dt.find_restaurant.image.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final AmazonS3Client amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    public List<String> uploadImages(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            // 파일 검증
            validateFile(file.getOriginalFilename());
        }
        // 파일 업로드
        return files.stream()
                .map(this::uploadSingleImageToS3) // 리스트의 각 파일을 단일 업로드 메서드로 전달
                .collect(Collectors.toList());
    }

    private void validateFile(String filename) {
        // 파일 존재 유무 검증
        if (filename == null || filename.isEmpty()) {
            throw new RuntimeException("파일이 존재하지 않습니다.");
        }

        // 확장자 존재 유무 검증
        log.info("검증하는 파일 이름 : {}", filename);
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new RuntimeException("파일 확장자가 존재하지 않습니다.");
        }

        // 허용되지 않는 확장자 검증
//        String extension = URLConnection.guessContentTypeFromName(filename);
        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        log.info("확장자 검증 : {}", extension);

        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "gif");
        if (extension.isEmpty() || !allowedExtentionList.contains(extension)) {
            throw new RuntimeException("허용되지 않는 파일 확장자입니다. 허용된 확장자: " + allowedExtentionList);
        }
    }

    // [private 메서드] 직접적으로 S3에 업로드
    private String uploadSingleImageToS3(MultipartFile file) {
        // 1. 파일이 비어있는 경우 예외를 발생시킵니다.
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }

        // 2. 각 파일에 대해 고유한 파일 이름을 생성합니다. (원본 파일 확장자 유지)
        String originalFileName = file.getOriginalFilename();
        String s3FileName = UUID.randomUUID().toString() + getFileExtension(originalFileName);

        // 3. 파일 메타데이터를 설정합니다.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // 4. S3에 파일을 업로드합니다.
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName, s3FileName, file.getInputStream(), metadata
            );
            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            log.error("S3 파일 업로드 중 입출력 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("이미지 업로드에 실패했습니다.", e);
        }

        // 5. 업로드된 파일의 S3 URL을 생성하여 반환합니다.
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteImage(String fileUrl) {
        // S3에서 파일 삭제
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            amazonS3.deleteObject(bucketName, fileName);
        } catch (Exception exception) {
            log.error("이미지 삭제에 실패했습니다: {}", exception.getMessage());
            throw new RuntimeException("이미지 삭제에 실패했습니다.");
        }
    }


    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 형식의 파일 이름입니다: %s", fileName));
        }
    }
}
