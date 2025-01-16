package com.example.stock.facade;

import com.example.stock.service.DefaultStockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component

public class RedissonLockStockFacade {
    private RedissonClient redissonClient;

    private DefaultStockService defaultStockService;

    public RedissonLockStockFacade(RedissonClient redissonClient, DefaultStockService defaultStockService) {
        this.redissonClient = redissonClient;
        this.defaultStockService = defaultStockService;
    }

    public void decrease(Long id, Long quantity) {
        RLock lock = redissonClient.getLock(id.toString());

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

            if(!available) {
                System.out.println("Lock not available");
                return;
            }

            defaultStockService.decrease(id, quantity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
