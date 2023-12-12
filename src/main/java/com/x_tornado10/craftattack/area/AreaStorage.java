package com.x_tornado10.craftattack.area;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Set;
import java.util.UUID;

public class AreaStorage {
    public boolean saveArea(Area area, String fileName, boolean overwrite) throws IOException, InvalidConfigurationException {
        YamlConfiguration areas = new YamlConfiguration();
        areas.load(fileName);
        Set<String> s = areas.getKeys(false);
        if (s.contains(area.getName())) if (!overwrite) return false;
        Location loc1 = area.getCorner1();
        Location loc2 = area.getCorner2();
        Block b1 = loc1.getBlock();
        Block b2 = loc2.getBlock();
        String sep = "$";
        World w1 = loc1.getWorld();
        World w2 = loc2.getWorld();
        if (w1 == null || w2 == null) return false;
        String corner1 = w1.getUID() + sep + b1.getX() + sep + b1.getY() + sep + b1.getZ();
        String corner2 = w2.getUID() + sep + b2.getX() + sep + b2.getY() + sep + b2.getZ();
        areas.set(area.getName() + ".1", corner1);
        areas.set(area.getName() + ".2", corner2);
        areas.save(fileName);
        return true;
    }

    public Area loadArea(String fileName, String areaName) throws IOException, InvalidConfigurationException {
        YamlConfiguration areas = new YamlConfiguration();
        areas.load(fileName);
        if (!areas.contains(areaName)) return null;
        String loc1 = (String) areas.get(areaName + ".1");
        String loc2 = (String) areas.get(areaName + ".2");
        if (loc1 == null || loc2 == null) return null;
        String[] parts1 = loc1.split("\\$");
        String[] parts2 = loc1.split("\\$");
        Area area;
        try {
            area = new Area(new Location(Bukkit.getWorld(UUID.fromString(parts1[0])), Double.parseDouble(parts1[1]), Double.parseDouble(parts1[2]), Double.parseDouble(parts1[3])),
                    new Location(Bukkit.getWorld(UUID.fromString(parts2[0])), Double.parseDouble(parts2[1]), Double.parseDouble(parts2[2]), Double.parseDouble(parts2[3])),
                    areaName);
        } catch (NullPointerException | IllegalArgumentException e) {
            return null;
        }
        return area;
    }
}