package com.example.demo.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;


public class KeyValueConcurrencyTests {

    private KeyValueStore store;
    private ExecutorService executor;

    @BeforeEach 
    void setUp() {
        store = new ConcurrentHashMapStore();
        executor = Executors.newFixedThreadPool(32);
    }

    @AfterEach 
    void tearDown() throws InterruptedException {
        executor.shutdown(); 
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConcurrentPuts() throws InterruptedException {
        int threadCount = 32;
        int keysPerThread = 100;

        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);

        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();


                    for(int j = 0; j < keysPerThread; j++) {
                        String key = "key_" + threadId + "_" + j;
                        String value = "value_" + threadId + "_" + j;
                        store.put(key, value);
                    }
                } catch(Throwable t) {
                    exceptions.add(t);
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        finishLatch.await();

        assertTrue(exceptions.isEmpty(), "Errors occurred in background threads: " + exceptions);

        int expectedSize = threadCount * keysPerThread;
        assertEquals(expectedSize, store.size(), "Storage size does not match the expected value!");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConcurrentReadWrite() throws InterruptedException {
        int prePopulatedCount = 500;

        for(int i = 0; i < prePopulatedCount; i++) {
            String baseKey = "base_key_" + i;
            String baseVal = "base_value_" + i;
            store.put(baseKey, baseVal);
        }

        int threadCount = 32;
        int keysPerThread = 100;

        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);

        ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<>();

        for(int i = 0; i < threadCount; i++) {
            boolean isWriter = (i % 2 == 0);
            final int threadId = i;

            executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();

                    if(isWriter) {
                        for(int j = 0; j < keysPerThread; j++) {
                            String key = "writer_key_" + threadId + "_" + j;
                            String value = "writer_value_" + threadId + "_" + j;
                            store.put(key, value);
                        }
                    } else {
                        for(int j = 0; j < keysPerThread; j++) {
                            int keyIndex = j % prePopulatedCount;
                            Optional<String> val = store.get("base_key_" + keyIndex);
                            if (val.isEmpty() || !val.get().equals("base_value_" + keyIndex)) {
                                throw new AssertionError("Unexpected value for base_key_" + keyIndex);
                            }
                        }
                    }
                } catch(Throwable t) {
                    exceptions.add(t);
                } finally {
                    finishLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        finishLatch.await();

        assertTrue(exceptions.isEmpty(), "Errors occurred in background threads: " + exceptions);

        int writersCount = threadCount / 2;
        int expectedSize = prePopulatedCount + (writersCount * keysPerThread);
        assertEquals(expectedSize, store.size(), "Storage size does not match the expected value!");
    }

}
