package com.software.ott;

import com.software.ott.common.properties.KakaoProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(KakaoProperties.class)
public class OttApplication {

    public static void main(String[] args) {
        SpringApplication.run(OttApplication.class, args);
    }

}
