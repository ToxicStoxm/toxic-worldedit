package com.x_tornado10.craftattack.area;

import com.x_tornado10.craftattack.utils.id.Id;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Area {
    private final String name;
    private final int blockCount;
    private final int height;
    private final HashMap<Id, Block> blocks;
    public Area(String name, HashMap<Id, Block> blocks) {
        this.name = name;
        this.blocks = blocks;
        blockCount = blocks.size();
        int maxValue = 0;
        for (Map.Entry<Id, Block> entry : blocks.entrySet()) {
            int y = entry.getKey().getY();
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

    public HashMap<Id, Block> getBlocks() {
        return blocks;
    }
    public Block getBlock(@NonNull Id id) {
        return blocks.get(id);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Area Details:\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Block Count: ").append(blockCount).append("\n");
        sb.append("Height: ").append(height).append("\n");
        sb.append("Blocks:\n");

        for (Map.Entry<Id, Block> entry : blocks.entrySet()) {
            Id id = entry.getKey();
            Block block = entry.getValue();
            sb.append("ID: ").append(id.toString()).append(", Block: ").append(block.toString()).append("\n");
        }

        return sb.toString();
    }
}
