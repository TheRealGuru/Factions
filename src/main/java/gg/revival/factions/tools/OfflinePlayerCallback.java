package gg.revival.factions.tools;

import java.util.UUID;

public interface OfflinePlayerCallback
{

    void onQueryDone(UUID uuid, String username);

}
