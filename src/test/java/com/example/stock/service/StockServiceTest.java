package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.facade.LettuceLockStockFacade;
import com.example.stock.facade.NamedLockStockFacade;
import com.example.stock.facade.OptimisticLockStockFacade;
import com.example.stock.facade.RedissonLockStockFacade;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class StockServiceTest {

    @Autowired
    private DefaultStockService defaultStockService;

    @Autowired
    private SynchronizedStockService synchronizedStockService;

    @Autowired
    private PessimisticLockStockService pessimisticLockStockService;

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @Autowired
    private NamedLockStockFacade namedLockStockFacade;

    @Autowired
    private LettuceLockStockFacade lettuceLockStockFacade;

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    public void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    public void after() {
        stockRepository.deleteAll();
    }

    @Test
    @DirtiesContext
    public void Default_동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    defaultStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertNotEquals(0L, stock.getQuantity());
    }

    @Test
    @DirtiesContext
    public void Synchronized_동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    synchronizedStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

    @Test
    @DirtiesContext
    public void PessimisticLock_동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pessimisticLockStockService.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

    @Test
    @DirtiesContext
    public void OptimisticLock_동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    optimisticLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

    @Test
    @DirtiesContext
    public void NamedLock_동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    namedLockStockFacade.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

    @Test
    @DirtiesContext
    public void LettuceLock_동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    lettuceLockStockFacade.decrease(1L, 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }

    @Test
    @DirtiesContext
    public void RedissonLock_동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockStockFacade.decrease(1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L).orElseThrow();

        assertEquals(0L, stock.getQuantity());
    }
}
