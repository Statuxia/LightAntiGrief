package me.statuxia.lightantigrief.trigger;

public class Trigger {

    private final BufferTrigger buffer;
    private int totalTriggered = 1;
    private long lastTriggered = System.currentTimeMillis();

    public Trigger(BufferTrigger buffer) {
        this.buffer = buffer;
    }

    public float getTotalTriggered() {
        return totalTriggered;
    }

    public Trigger incrementTriggers() {
        return incrementTriggers(1);
    }

    public Trigger incrementTriggers(int price) {
        if (lastTriggered + 1000L * 60 * 20 < System.currentTimeMillis()) {
            return new Trigger(buffer).incrementTriggers(price);
        }
        totalTriggered += price;
        lastTriggered = System.currentTimeMillis();
        return this;
    }
}