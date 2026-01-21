package org.baoxdev.hotelbooking_test.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {


    @Bean
    public Cloudinary cloudinary(CloudinaryProperties props) {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", props.cloudName());
        config.put("api_key", props.apiKey());
        config.put("api_secret", props.apiSecret());
        return new Cloudinary(config);
    }
}