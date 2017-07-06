package gg.revival.factions.timers;

import lombok.Getter;
import lombok.Setter;

public class Timer {

    @Getter @Setter TimerType type;
    @Getter @Setter long expire;

    public Timer(TimerType type, long expire) {
        this.type = type;
        this.expire = expire;
    }
}
