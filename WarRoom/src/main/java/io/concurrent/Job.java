package io.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Job {

    private final ExecutorService exec = Executors.newFixedThreadPool(2);

    void run() throws Exception {
        List<Callable<Integer>> tasks = Arrays.asList(
                new SumTask(1, 2, 3),
                new ProductTask(4, 5, 6));


        for (Future<Integer> integerFuture : exec.invokeAll(tasks)) {
            Integer integer = integerFuture.get();
            System.out.println(integer);
        }

        System.out.println("Job Done");


    }

    public static void main(String[] args) throws Exception {
        new Job().run();
    }
}
