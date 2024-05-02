package com.angrydu.plumbingstore;

import com.angrydu.plumbingstore.security.RsaKeyProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperty.class)
@SpringBootApplication
public class PlumbingStoreAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlumbingStoreAuthApplication.class, args);
    }

}
