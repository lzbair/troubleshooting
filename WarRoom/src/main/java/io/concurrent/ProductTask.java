package io.concurrent;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class ProductTask implements Callable<Integer> {

    private final int[] values;


    public ProductTask(int... values) {
        this.values = values;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("Product of: " + Arrays.stream(values));
        return Arrays.stream(values).reduce(1, (left, right) -> left * right);
    }
}
