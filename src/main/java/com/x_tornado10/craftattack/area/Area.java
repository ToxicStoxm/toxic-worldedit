package com.x_tornado10.craftattack.area;

import org.bukkit.Location;

public class Area {
    private Location corner1;
    private Location corner2;
    private String name;

    public Area(Location corner1, Location corner2, String name) {
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.name = name;
    }

    public Location getCorner1() {
        return corner1;
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
