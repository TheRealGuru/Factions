package gg.revival.factions.timers;

public enum TimerType {

    // Values are assigned for the scoreboard
    // 0 = Not displayed on the scoreboard

    DISBAND_COOLDOWN(0),
    RENAME_COOLDOWN(0),
    EVENT(10),
    LOGOUT_WARMUP(9),
    HOME_WARMUP(8),
    STUCK_WARMUP(7),
    CLASS_WARMUP(6),
    COMBAT_TAG(5),
    ENDERPEARL_COOLDOWN(4),
    PVPPROT(3),
    PVPSAFETY(2);

    private int val;

    TimerType(int val) {
        this.val = val;
    }

    public int getValue() {
        return this.val;
    }

}
