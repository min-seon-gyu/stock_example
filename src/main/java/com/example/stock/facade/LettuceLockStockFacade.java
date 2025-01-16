package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.DefaultStockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {
    private final RedisLockRepository redisLockRepository;

    private DefaultStockService defaultStockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, DefaultStockService defaultStockService) {
        this.redisLockRepository = redisLockRepository;
        this.defaultStockService = defaultStockService;
    }

    public void decrease(Long id, Long quantity) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }

        try {
            defaultStockService.decrease(id, quantity);
        } finally {
            redisLockRepository.unlock(id);
        }
    }
}
