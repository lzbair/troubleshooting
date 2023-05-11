package io.concurrent.basics;

import java.io.Closeable;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ObjectSync {
    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(3);
        SharedData sharedData = new SharedData();


        IntStream.range(0, 3)
                .forEach(count -> service.submit(sharedData::increment));

        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        System.out.println("Actual value: " + sharedData.value + ", Expected value: 1000");
        service.shutdown();
    }


}
