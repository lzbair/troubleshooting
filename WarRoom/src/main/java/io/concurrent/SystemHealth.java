package io.concurrent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class SystemHealth {

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, new DummyThreadFactory());

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Thread.currentThread().setName("IDE main thread");

        new SystemHealth().check();

        latch.await();

    }

    void check() throws InterruptedException {
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(() -> {
            String code = UUID.randomUUID().toString();
            System.out.println(timestamp() + " ---> Health chek started: " + code);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(timestamp() + " ---> Health chek finished: " + code);
        }, 500, 5000, TimeUnit.MILLISECONDS);
    }

    String timestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
    }


    class DummyThreadFactory implements ThreadFactory {
        public Thread newThread(Runnable r) {
            return new Thread(r, "dummy pool - ");
        }
    }

}
