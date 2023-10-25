package me.kyroclient.util;

public class MilliTimer
{
    private long time;

    public MilliTimer() {
        this.reset();
    }

    public long getTime() {
        return this.time;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.time;
    }

    public boolean hasTimePassed(final long milliseconds) {
        return System.currentTimeMillis() - this.time >= milliseconds;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public void reset(final long time) {
        this.time = System.currentTimeMillis() - time;
    }
    public void setTime(long time)
    {
        this.time = time;
    }
}
