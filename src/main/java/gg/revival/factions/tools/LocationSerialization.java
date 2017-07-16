package gg.revival.factions.tools;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerialization {

    public static String serializeLocation(Location location) {
        StringBuilder locationBuilder = new StringBuilder();

        locationBuilder.append("@w;" + location.getWorld().getName())
                .append(":@x;" + location.getX())
                .append(":@y;" + location.getY())
                .append(":@z;" + location.getZ())
                .append(":@p;" + location.getPitch())
                .append(":@yaw;" + location.getYaw());

        if(locationBuilder.length() > 0) {
            return locationBuilder.toString();
        }

        return null;
    }

    public static Location deserializeLocation(String locationString) {
        String[] att;
        String[] arr$ = att = locationString.split(":");

        Location location = new Location(Bukkit.getWorlds().get(0), 0.0, 0.0, 0.0);

        int len$ = att.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String attribute = arr$[i$];
            String[] split = attribute.split(";");

            if (split[0].equalsIgnoreCase("@w")) {
                location.setWorld(Bukkit.getWorld(split[1]));
            }

            if (split[0].equalsIgnoreCase("@x")) {
                location.setX(Double.parseDouble(split[1]));
            }

            if (split[0].equalsIgnoreCase("@y")) {
                location.setY(Double.parseDouble(split[1]));
            }

            if (split[0].equalsIgnoreCase("@z")) {
                location.setZ(Double.parseDouble(split[1]));
            }

            if (split[0].equalsIgnoreCase("@p")) {
                location.setPitch(Float.parseFloat(split[1]));
            }

            if (split[0].equalsIgnoreCase("@yaw")) {
                location.setYaw(Float.parseFloat(split[1]));
            }
        }

        return location;
    }

}
