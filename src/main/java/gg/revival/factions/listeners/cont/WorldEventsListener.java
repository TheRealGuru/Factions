package gg.revival.factions.listeners.cont;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class WorldEventsListener implements Listener {

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if(!event.getBlock().getType().equals(Material.FIRE))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockChange(BlockFromToEvent event) {
        Block block = event.getToBlock();

        if(block.getType().equals(Material.OBSIDIAN) || block.getType().equals(Material.COBBLESTONE) || block.getType().equals(Material.STONE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

}
