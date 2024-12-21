package org.ajls.megawallsclasses.container;

import org.ajls.lib.advanced.IntegerA;

public class SpiderEnergyD {
    IntegerA times = new IntegerA();
//    int activeTimes;
    int amount;
    int delay;
    public SpiderEnergyD(int activeTimes, int amount, int delay) {
//        this.activeTimes = activeTimes;
        times.setUpperBound(activeTimes);
        this.amount = amount;
        this.delay = delay;
    }

    public int getAmount() {
        return amount;
    }
    public int getDelay() {
        return delay;
    }

    public boolean increment() {
        return times.incremented();
    }
}
