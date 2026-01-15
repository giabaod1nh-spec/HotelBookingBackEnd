package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class RedisTokenService {
    StringRedisTemplate redisTemplate;

    public void blackListToken(String token , long timeToLive){
        //Key: token , value la "logout_token" , timeToLive là thời gian còn lại đến khi expired
        redisTemplate.opsForValue().set(token , "logged_out", timeToLive );

    }

    public boolean isTokenBlackListed(String token){

        return redisTemplate.hasKey(token);
    }
}
