package gg.revival.factions.tools;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCache {

    @Getter
    public static Map<UUID, String> cachedUsernames = new HashMap<UUID, String>();

    public static boolean isCached(UUID uuid) {
        return cachedUsernames.containsKey(uuid);
    }

    public static boolean isCached(String str) {
        for (String values : cachedUsernames.values()) {
            if (values.equalsIgnoreCase(str)) {
                return true;
            }
        }

        return false;
    }

    public static UUID getUUID(String username) {
        if (!isCached(username))
            return null;

        for (UUID uuid : cachedUsernames.keySet()) {
            if (cachedUsernames.get(uuid).equalsIgnoreCase(username)) {
                return uuid;
            }
        }

        return null;
    }

    public static String getUsername(UUID uuid) {
        if (!isCached(uuid))
            return null;

        return cachedUsernames.get(uuid);
    }
}
