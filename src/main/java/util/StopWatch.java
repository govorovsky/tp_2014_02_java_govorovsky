package util;

/**
 * Created by Andrew Govorovsky on 02.04.14
 */


public class StopWatch {

    private long startTime = 0;
    private boolean running = false;


    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }


    public void stop() {
        this.running = false;
    }

    //elaspsed time in milliseconds
    public long getElapsedTime() {
        long elapsed = 0;
        if (running) {
            elapsed = (System.currentTimeMillis() - startTime);
        }
        return elapsed;
    }
}

