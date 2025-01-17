package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class RedisLockStockService {
    private final StockRepository stockRepository;

    public RedisLockStockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
