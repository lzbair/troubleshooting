package io.concurrent.scheduling;

import java.util.ArrayList;
import java.util.List;

public class TaskScheduler {

    final List<Task> task;

    public TaskScheduler(List<Task> task) {
        this.task = task;
    }

    static void allocateCpu(){

    }
}
