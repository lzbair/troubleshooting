package io.concurrent.scheduling;

public class Cpu {

    State state = State.FREE;

    enum State {
        FREE, BUSY;
    }
}
