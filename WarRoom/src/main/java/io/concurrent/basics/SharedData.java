package io.concurrent.basics;

public class SharedData {

    static Object lock = new Object();

    int value = 0;

    // very bad synchronization : instance-method-sync doesn't work here, Put method as static
    synchronized void change() {
        value++;
    }

    // Safe
    void increment() {
        synchronized (lock) {
            value = value % 2;
            try {
                System.out.println(Thread.currentThread().toString());
                //lock.wait(240000L);
                Thread.sleep(99999999);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
