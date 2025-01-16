package com.example.stock.transaction;

import com.example.stock.service.DefaultStockService;

public class TransactionStockService {
    private final DefaultStockService defaultStockService;

    public TransactionStockService(DefaultStockService defaultStockService) {
        this.defaultStockService = defaultStockService;
    }

    public void decrease(Long id, Long quantity) {
        startTransaction();

        defaultStockService.decrease(id, quantity);

        endTransaction();
    }

    private void startTransaction() {
    }

    private void endTransaction() {
    }
}
