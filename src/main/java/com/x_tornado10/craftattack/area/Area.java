package com.x_tornado10.craftattack.area;

import com.x_tornado10.craftattack.utils.CustomBlock;
import com.x_tornado10.craftattack.utils.id.Id;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Area {
    private final String name;
    private final int blockCount;
    private final int height;
    private final HashMap<Id, CustomBlock> blocks;
    public Area(String name, HashMap<Id, CustomBlock> blocks) {
        this.name = name;
        this.blocks = blocks;
        blockCount = blocks.size();
        int maxValue = 0;
        for (Map.Entry<Id, CustomBlock> entry : blocks.entrySet()) {
            int y = entry.getKey().y();
            if (y > maxValue) maxValue = y;
        }
        height = maxValue + 1;
    }

    public String getName() {
        return name;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public int getHeight() {
        return height;
    }

    public HashMap<Id, CustomBlock> getBlocks() {
        return blocks;
    }
    public CustomBlock getBlock(@NonNull Id id) {
        return blocks.get(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Area)) return false;
        return ((Area) obj).getName().equals(name);
    }
}
