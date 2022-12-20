package com.nemogz.mantracounter.vibrations;

public class LongPressThread implements Runnable{

    long startTime;
    long waitTime;

    public LongPressThread(long startTime, long waitTime) {
        this.startTime = startTime;
        this.waitTime = waitTime;
    }

    @Override
    public void run() {

    }
}
