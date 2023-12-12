package com.x_tornado10.craftattack.commands.save;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.area.AreaStorage;
import com.x_tornado10.craftattack.craftattack;
import com.x_tornado10.craftattack.plmsg.PlayerMessages;
import com.x_tornado10.craftattack.utils.statics.Paths;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SaveCommand implements CommandExecutor {

    private final craftattack plugin;
    private final Logger logger;
    private final PlayerMessages plmsg;
    private final AreaStorage areaStorage;
    public SaveCommand() {
        plugin = craftattack.getInstance();
        logger = plugin.getLogger();
        plmsg = plugin.getPlmsg();
        areaStorage = new AreaStorage();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender,@NonNull Command command,@NonNull String s,@NonNull String[] args) {
        if (commandSender instanceof Player p) {
            if (args.length == 7 || args.length == 8) {
                List<Location> locs = getCoordinates(p, List.of(args));
                if (locs == null) {
                    plmsg.msg(p,"Please provide valid coordinates for two corners!");
                    return true;
                }
                try {
                    areaStorage.saveArea(new Area(locs.get(0), locs.get(1), args[6]), Paths.areaSaveFile, args.length == 8 && Boolean.parseBoolean(args[7]));
                } catch (IOException | InvalidConfigurationException e) {
                    plmsg.msg(p, ChatColor.RED + "Something went wrong wile saving, please make sure you provided valid arguments!");
                    return true;
                }
            } else {
                plmsg.msg(p, "Please provide valid coordinates for two corners!");
            }
        } else {
            logger.info("You need to be a player to execute this!");
        }
        return true;
    }

    private List<Location> getCoordinates(Player p, List<String> args) {
        List<String> temp = new ArrayList<>(args);
        Location loc1;
        Location loc2;
        for (String s : temp) {
            int current = temp.indexOf(s);
            if (s.contains("~")) {
                if (s.equals("~")) {
                    String replacement = null;
                    Location loc = p.getLocation();
                    switch (current) {
                        case 0, 3 -> replacement = String.valueOf(loc.getX());
                        case 1, 4 -> replacement = String.valueOf(loc.getY());
                        case 2, 5 -> replacement = String.valueOf(loc.getZ());
                    }
                    temp.set(current, replacement);
                } else {
                    String replacement = null;
                    String modifier = s.replace("~","");
                    Location loc = p.getLocation();
                    switch (current) {
                        case 0, 3 -> replacement = String.valueOf(loc.getX() + Double.parseDouble(modifier));
                        case 1, 4 -> replacement = String.valueOf(loc.getY() + Double.parseDouble(modifier));
                        case 2, 5 -> replacement = String.valueOf(loc.getZ() + Double.parseDouble(modifier));
                    }
                    temp.set(current, replacement);
                }
            }
        }
        try {
            loc1 = new Location(p.getWorld(), Double.parseDouble(temp.get(0)), Double.parseDouble(temp.get(1)), Double.parseDouble(temp.get(2)));
            loc2 = new Location(p.getWorld(), Double.parseDouble(temp.get(3)), Double.parseDouble(temp.get(4)), Double.parseDouble(temp.get(5)));
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
        List<Location> locs = new ArrayList<>();
        locs.add(loc1);
        locs.add(loc2);
        return locs;
    }
}
