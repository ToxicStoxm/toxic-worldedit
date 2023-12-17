package com.x_tornado10.craftattack.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;


public class CustomBlock implements ConfigurationSerializable {
    private final BlockData data;
    private final Location location;

    public CustomBlock(Block block) {
        this.data = block.getBlockData();
        this.location = block.getLocation().clone();
    }
    public CustomBlock(BlockData data, Location location) {
        this.data = data;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }


    public BlockData getBlockData() {
        return data;
    }

    @Override
    @NonNull
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("data", this.data.getAsString());
        data.put("location", location.serialize());
        return data;
    }
}
