package me.vihara.core.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Storage {
    protected static final ExecutorService EXECUTOR = ForkJoinPool.commonPool();
    protected static final AtomicBoolean TERMINATED = new AtomicBoolean();
    protected static final Map<StorageCredentials, StorageCredentialsManager> REGISTRY
            = new ConcurrentHashMap<>();

    StorageCredentials credentials;

    public static void terminate() {
        TERMINATED.set(true);
        EXECUTOR.shutdown();

        try {
            EXECUTOR.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
