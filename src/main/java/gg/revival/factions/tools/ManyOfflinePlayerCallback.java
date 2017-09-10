package gg.revival.factions.tools;

import java.util.Map;
import java.util.UUID;

public interface ManyOfflinePlayerCallback {

    void onQueryDone(Map<UUID, String> result);

}
