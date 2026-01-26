package org.baoxdev.hotelbooking_test.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;


    //Config de tao 1 Redission
    @Bean
    public RedissonClient redissonClient(){
        Config config  = new Config();

        config.useSingleServer()
                .setAddress("redis://" + redisHost +":" + redisPort)
                .setPassword(redisPassword)
                .setConnectionPoolSize(50)
                .setConnectionMinimumIdleSize(10)
                .setTimeout(3000)
                .setRetryAttempts(3);

        return Redisson.create(config);
    }
}
