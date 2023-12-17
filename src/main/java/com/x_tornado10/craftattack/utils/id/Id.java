package com.x_tornado10.craftattack.utils.id;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public record Id(int x, int y, int z) implements ConfigurationSerializable {

    @Override
    @NonNull
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("x", x);
        data.put("y", y);
        data.put("z", z);
        return data;
    }
}
