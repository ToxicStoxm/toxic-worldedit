package com.x_tornado10.craftattack;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class craftattack extends JavaPlugin {

    private static craftattack instance;
    public static craftattack getInstance() {return instance;}
    private Logger logger;
    private long start;
    @Override
    public void onLoad() {
        instance = this;
        start = System.currentTimeMillis();
        logger = getLogger();
    }

    @Override
    public void onEnable() {
        long timeElapsed = System.currentTimeMillis() - start;
        logger.info("Successfully enabled (took " + timeElapsed / 1000 + "." + timeElapsed % 1000 + "s)");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
