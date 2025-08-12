package com.dt.find_restaurant.global.config.db;//
//import com.google.cloud.NoCredentials;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//@Configuration
//@Profile("test") // 로컬 및 CI 테스트용
//public class GCSTestConfig {
//
//    @Bean
//    public Storage testStorage() {
//        return StorageOptions.newBuilder()
//                .setProjectId("dummy-project")
//                .setHost("http://localhost:4443") // fake-gcs-server endpoint
//                .setCredentials(NoCredentials.getInstance())
//                .build()
//                .getService();
//    }
//}
