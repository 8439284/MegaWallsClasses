package org.ajls.megawallsclasses.container;

import java.util.UUID;

public class WardenDarknessTargetTimestamp {
    UUID targetUUID;
    int timeStamp;
    public WardenDarknessTargetTimestamp(UUID targetUUID, int timeStamp) {
        this.targetUUID = targetUUID;
        this.timeStamp = timeStamp;
    }

    public UUID getTargetUUID() {
        return targetUUID;
    }
    public void setTargetUUID(UUID targetUUID) {
        this.targetUUID = targetUUID;
    }
    public int getTimestamp() {
        return timeStamp;
    }
    public void setTimestamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

}
