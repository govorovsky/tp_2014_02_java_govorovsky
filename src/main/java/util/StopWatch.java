package util;

/**
 * Created by Andrew Govorovsky on 02.04.14
 */


public class StopWatch {

    private long startTime = 0;
    private long stopTime = 0;
    private boolean running = false;


    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }


    public void stop() {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }

    public long getElapsedTime() {
        if (running) {
            return (System.currentTimeMillis() - startTime);
        } else {
            return stopTime - startTime;
        }
    }

    public boolean isRunning() {
        return running;
    }
}

