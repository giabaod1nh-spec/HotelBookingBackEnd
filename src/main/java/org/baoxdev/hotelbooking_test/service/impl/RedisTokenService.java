package org.baoxdev.hotelbooking_test.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class RedisTokenService {
    StringRedisTemplate redisTemplate;


    public void blackListToken(String token , long timeToLive){
        //Key: token , value la "logout_token" , timeToLive là thời gian còn lại đến khi expired
        redisTemplate.opsForValue().set(token , "logged_out", timeToLive , TimeUnit.MILLISECONDS);

    }

    public boolean isTokenBlackListed(String token) throws ParseException, JOSEException {

        return redisTemplate.hasKey(token);
    }
}
