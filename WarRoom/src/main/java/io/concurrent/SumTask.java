package io.concurrent;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class SumTask implements Callable<Integer> {

    private final int[] values;


    public SumTask(int... values) {
        this.values = values;
    }

    @Override
    public Integer call() throws Exception {
        while(check()) {
            //
        }
        System.out.println("Sum of: " + Arrays.stream(values));
        return Arrays.stream(values).sum();
    }

    private boolean check() {
        return true;
    }
}
