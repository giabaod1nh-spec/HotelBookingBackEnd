package org.baoxdev.hotelbooking_test.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.baoxdev.hotelbooking_test.configuration.RedissonConfig;
import org.baoxdev.hotelbooking_test.exception.AppException;
import org.baoxdev.hotelbooking_test.model.enums.ErrorCode;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@Slf4j(topic = "LOCK_SERVICE")
public class DistributedLockService {
     RedissonClient redissonClient;

    //Chay 1 doan code co khoa Redis bao ve
    //lockKey la lock cho 1 loai phong trong 1 ngay cu the
    //VD: booking:roomType:123:2026-02-01:2026-02-05
    //waitTime la thoi gian cho toi da de lay duoc lock -> tranh treo request
    //leaseTime la thoi gian timeout de lock tu unlock

    public <T> T executeWithLock(String lockKey , long waitTime,  Supplier<T> operation){
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean acquired = lock.tryLock(waitTime, TimeUnit.SECONDS);
            //Redis check lock exist , not -> create , exists -> waitTime
            if(!acquired){
                log.warn("Lock is busy: {}" , lockKey);
                throw new AppException(ErrorCode.LOCK_IS_BUSY);
            }
            log.debug("Lock acquired: {}" ,lockKey);

        }catch (InterruptedException e){
              Thread.currentThread().interrupt();
              log.error("Thread interrupted while waiting for lock: {}" , lockKey , e);
        }finally {
            //If lock hold by current thread then released lock
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
                log.debug("Log released:{}" , lockKey);
            }
        }
        return operation.get();
    }

    //Execute with default timeouts (10s wait , 30s release)

    public <T> T executeWithLock(String lockKey , Supplier<T> operation){
        return executeWithLock(lockKey , 10 , operation);
    }

}
