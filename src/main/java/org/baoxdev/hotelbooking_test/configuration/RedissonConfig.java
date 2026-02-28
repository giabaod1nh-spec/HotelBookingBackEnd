package org.baoxdev.hotelbooking_test.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

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

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            BookingExpirationListener listener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Listen to key expiration events on DB 0
        container.addMessageListener(listener,
                new PatternTopic("__keyevent@0__:expired"));

        return container;
    }


}
