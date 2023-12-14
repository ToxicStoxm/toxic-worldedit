package com.x_tornado10.craftattack;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.commands.area_command.AreaCommandTabCompletion;
import com.x_tornado10.craftattack.commands.newarea.NewAreaCommand;
import com.x_tornado10.craftattack.commands.newarea.NewAreaCommandTabCompletion;
import com.x_tornado10.craftattack.commands.area_command.AreaCommand;
import com.x_tornado10.craftattack.plmsg.PlayerMessages;
import com.x_tornado10.craftattack.utils.statics.Paths;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class craftattack extends JavaPlugin {

    private static craftattack instance;
    public static craftattack getInstance() {return instance;}
    private Logger logger;
    private long start;
    private PlayerMessages plmsg;
    public PlayerMessages getPlmsg() {return plmsg;}
    public static List<Area> areaList;
    @Override
    public void onLoad() {
        instance = this;
        start = System.currentTimeMillis();
        logger = getLogger();
        plmsg = new PlayerMessages("[" + getDescription().getPrefix() + "]: ");
        saveDefaultConfig();
        areaList = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        long timeElapsed = System.currentTimeMillis() - start;
        PluginCommand saveCommand = Bukkit.getPluginCommand("new");
        if (saveCommand != null) {
            saveCommand.setExecutor(new NewAreaCommand());
            saveCommand.setTabCompleter(new NewAreaCommandTabCompletion());
        }
        PluginCommand AreaCommand = Bukkit.getPluginCommand("area");
        if (AreaCommand != null) {
            AreaCommand.setExecutor(new AreaCommand());
            AreaCommand.setTabCompleter(new AreaCommandTabCompletion());
        }
        logger.info("Successfully enabled (took " + timeElapsed / 1000 + "." + timeElapsed % 1000 + "s)");
        File areas = new File(Paths.areaSaveFile);
        if (!areas.exists()) {
            try {
               if (areas.createNewFile()) {
                   logger.info("areaSaveFile was successfully created!");
               }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
