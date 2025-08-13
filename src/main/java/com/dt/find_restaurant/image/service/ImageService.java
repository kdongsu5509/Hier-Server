package com.dt.find_restaurant.image.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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

    public String uploadImage(MultipartFile file) {
        validateFile(file.getOriginalFilename());
        // 파일 업로드
        return uploadImageToS3(file);
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
    private String uploadImageToS3(MultipartFile file) {
        // 변경된 파일
        String s3FileName = UUID.randomUUID().toString();

        //metadata 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // 이미지 파일 -> InputStream 변환
        try {
            PutObjectRequest formattedImage = new PutObjectRequest(bucketName, s3FileName, file.getInputStream(),
                    metadata);
            amazonS3.putObject(formattedImage);

            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, amazonS3.getRegionName(),
                    s3FileName);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }
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
}
