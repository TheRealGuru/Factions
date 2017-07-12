package gg.revival.factions.pillars;

import gg.revival.factions.tools.Configuration;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pillar {

    @Getter @Setter UUID displayedTo;
    @Getter @Setter Location location;
    @Getter @Setter Material blockType;
    @Getter @Setter byte blockData;
    @Getter @Setter List<Location> displayedBlocks;

    public Pillar(UUID displayedTo, Location location, Material blockType, byte blockData) {
        this.displayedTo = displayedTo;
        this.location = location;
        this.blockType = blockType;
        this.blockData = blockData;
        this.displayedBlocks = new ArrayList<>();
    }

    public void build() {
        if(Bukkit.getPlayer(this.displayedTo) == null || this.location == null)
            return;

        Player player = Bukkit.getPlayer(this.displayedTo);

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double topY = y + Configuration.PILLAR_HEIGHT;

        for(double i = y; i < topY; i = i + 1.0) {
            Location blockLocation = new Location(location.getWorld(), x, i, z);

            if(blockLocation.getBlock() != null && !blockLocation.getBlock().getType().equals(Material.AIR)) continue;

            displayedBlocks.add(blockLocation);

            if(i % 2 == 0) {
                player.sendBlockChange(blockLocation, Material.GLASS, (byte)0);
            }

            else {
                player.sendBlockChange(blockLocation, blockType, blockData);
            }
        }
    }

    public void remove() {
        if(Bukkit.getPlayer(this.displayedTo) == null)
            return;

        if(this.displayedBlocks.isEmpty())
            return;

        for(Location locations : this.displayedBlocks) {
            if(locations.getBlock() != null && !locations.getBlock().getType().equals(Material.AIR)) continue;

            locations.getBlock().getState().update();
        }

        displayedBlocks.clear();
    }

}
