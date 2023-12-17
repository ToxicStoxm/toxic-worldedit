package com.x_tornado10.craftattack;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.commands.area_command.AreaCommandTabCompletion;
import com.x_tornado10.craftattack.commands.area_command.AreaCommand;
import com.x_tornado10.craftattack.plmsg.PlayerMessages;
import com.x_tornado10.craftattack.utils.mgrs.SaveMgr;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
    private SaveMgr saveMgr;
    @Override
    public void onLoad() {
        instance = this;
        start = System.currentTimeMillis();
        logger = getLogger();
        plmsg = new PlayerMessages("[" + getDescription().getPrefix() + "]: ");
        saveDefaultConfig();
        areaList = new ArrayList<>();
        saveMgr = new SaveMgr();
    }

    @Override
    public void onEnable() {
        long timeElapsed = System.currentTimeMillis() - start;
        PluginCommand AreaCommand = Bukkit.getPluginCommand("area");
        AreaCommand areaCommand = new AreaCommand();
        if (AreaCommand != null) {
            AreaCommand.setExecutor(areaCommand);
            AreaCommand.setTabCompleter(new AreaCommandTabCompletion());
        }
        Bukkit.getPluginManager().registerEvents(areaCommand, this);
        saveMgr.createSaveFiles();
        saveMgr.load();
        logger.info("Successfully enabled (took " + timeElapsed / 1000 + "." + timeElapsed % 1000 + "s)");
    }

    @Override
    public void onDisable() {
        int block_count = 0;
        for (Area a : areaList) {
            block_count = block_count + a.getBlockCount();
            if (block_count > 500000) return;
        }
        saveMgr.save();
    }

    public SaveMgr getSaveMgr() {return saveMgr;}
}
