package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.DefaultStockService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class NamedLockStockFacade {
    private final LockRepository lockRepository;

    private final DefaultStockService defaultStockService;

    public NamedLockStockFacade(LockRepository lockRepository, DefaultStockService defaultStockService) {
        this.lockRepository = lockRepository;
        this.defaultStockService = defaultStockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            defaultStockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }
}
