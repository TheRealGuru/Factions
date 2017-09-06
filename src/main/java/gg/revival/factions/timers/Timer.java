package gg.revival.factions.timers;

import lombok.Getter;
import lombok.Setter;

public class Timer {

    @Getter TimerType type;
    @Getter @Setter long expire;
    @Getter @Setter long pauseDiff;
    @Getter @Setter boolean paused;

    public Timer(TimerType type, long expire) {
        this.type = type;
        this.expire = expire;
    }
}
