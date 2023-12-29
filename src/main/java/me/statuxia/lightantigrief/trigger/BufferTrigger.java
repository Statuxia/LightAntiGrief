package me.statuxia.lightantigrief.trigger;

import me.statuxia.lightantigrief.config.LAGConfig;
import me.statuxia.lightantigrief.trigger.actions.GriefAction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public record BufferTrigger(UUID playerUUID, GriefAction action) {

    public static final Map<BufferTrigger, Trigger> BUFFER = new HashMap<>();

    public static void trigger(BufferTrigger buffer) {
        Trigger trigger = BUFFER.getOrDefault(buffer, new Trigger(buffer));
        BUFFER.put(buffer, trigger.incrementTriggers());
    }

    public static float getTotal(BufferTrigger buffer) {
        Trigger trigger = BUFFER.getOrDefault(buffer, new Trigger(buffer));
        return trigger.getTotalTriggered();
    }

    public static boolean isLimitReached(BufferTrigger buffer) {
        Trigger trigger = BUFFER.getOrDefault(buffer, new Trigger(buffer));
        return trigger.getTotalTriggered() >= buffer.action.getLimitTriggers() + (LAGConfig.getTriggerRandomBonus() ? ThreadLocalRandom.current().nextInt(7) : 0);
    }
}
