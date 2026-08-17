package com.various_food.client.hud;

public class HUDTimerManager {
    private static int remainingTicks = 0;

    public static void show(int ticks) {
        remainingTicks = ticks;
    }

    public static void tick() {
        if (remainingTicks > 0) {
            remainingTicks--;
        }
    }

    public static boolean shouldDisplay() {
        return remainingTicks > 0;
    }
}
