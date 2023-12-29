package me.statuxia.lightantigrief.trigger.actions;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import static me.statuxia.lightantigrief.config.LAGConfig.*;

public enum GriefAction {

    FIRE_CHARGE(getFireCharge(), Component.text(" sets ")),
    GET_ITEM(getGetItem(), Component.text(" took ")),
    PUT_ITEM(getPutItem(), Component.text(" put ")),
    BREAK_BLOCK(getBreakBlock(), Component.text(" broke ")),
    PLACE_BLOCK(getPlaceBlock(), Component.text(" placed ")),
    MINECART(getMinecart(), Component.text(" placed ")),
    EXPLODE(getExplode(), Component.text(" exploded "));

    private final int limit;
    @Getter
    private final Component message;

    GriefAction(int limit, Component message) {
        this.limit = limit;
        this.message = message;
    }

    public int getLimitTriggers() {
        return limit;
    }

}
