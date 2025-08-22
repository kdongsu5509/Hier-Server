package com.dt.find_restaurant;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@OpenAPIDefinition(
        info = @Info(
                title = "거제 맛집 API 명세서",
                description = "거제 맛집 서비스 Backend에서의 API에 대한 명세서입니다.",
                version = "v1.0.0"
        )
)
@SpringBootApplication(scanBasePackages = "com.dt.find_restaurant")
@ConfigurationPropertiesScan
public class FindRestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindRestaurantApplication.class, args);
    }

}
