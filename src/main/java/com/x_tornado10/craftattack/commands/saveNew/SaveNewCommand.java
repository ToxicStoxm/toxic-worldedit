package com.x_tornado10.craftattack.commands.saveNew;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.craftattack;
import com.x_tornado10.craftattack.plmsg.PlayerMessages;
import com.x_tornado10.craftattack.utils.id.Id;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class SaveNewCommand implements CommandExecutor {

    private craftattack plugin;
    private Logger logger;
    private PlayerMessages plmsg;

    public SaveNewCommand() {
        plugin = craftattack.getInstance();
        logger = plugin.getLogger();
        plmsg = plugin.getPlmsg();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender,@NonNull Command command,@NonNull String s,@NonNull String[] args) {
        if (!(commandSender instanceof Player p)) {
            logger.info("This command is not yet supported for console!");
            return true;
        }
        if (args.length != 7) {
            plmsg.msg(p, "Please provide valid arguments!");
            return true;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> coords = new ArrayList<>(List.of(args));
                coords.remove(args[0]);
                craftattack.a = new Area(args[0], getArea(p,coords));
                plmsg.msg(p,"DONE");
            }
        }.runTaskAsynchronously(plugin);

        return true;
    }

    private HashMap<Id, Block> getArea(Player p,  List<String> args) {
        List<Location> locs = getCoordinates(p, args);
        Location corner1 = locs.get(0);
        Location corner2 = locs.get(1);
        HashMap<Id, Block> result = new HashMap<>();

        List<Block> blocks = new ArrayList<>();

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    blocks.add(Objects.requireNonNull(corner1.getWorld()).getBlockAt(x,y,z));
                }
            }
        }
        Block origin = blocks.get(0);
        for (Block b : blocks) {
            if (nearOrigin(b,origin)) origin = b;
        }
        result.put(new Id(0,0,0), origin);
        blocks.remove(origin);
        for (Block b : blocks) {
            result.put(new Id(xDifToOrigin(b, origin), yDifToOrigin(b, origin), zDifToOrigin(b,origin)), b);
        }
        return result;
    }

    private boolean nearOrigin(Block b1, Block b2) {
        int x1 = b1.getX();
        int y1 = b1.getY();
        int z1 = b1.getZ();
        int x2 = b2.getX();
        int y2 = b2.getY();
        int z2 = b2.getZ();
        return x1 < x2 || y1 < y2 || z1 < z2;
    }
    private int xDifToOrigin(Block b, Block origin) {
        return b.getX() - origin.getX();
    }
    private int yDifToOrigin(Block b, Block origin) {
        return b.getY() - origin.getY();
    }
    private int zDifToOrigin(Block b, Block origin) {
        return b.getZ() - origin.getZ();
    }

    private List<Location> getCoordinates(Player p, List<String> args) {
        List<String> temp = new ArrayList<>(args);
        Location loc1;
        Location loc2;
        for (String s : temp) {
            int current = temp.indexOf(s);
            if (s.contains("~")) {
                String replacement = null;
                if (s.equals("~")) {
                    Location loc = p.getLocation();
                    switch (current) {
                        case 0, 3 -> replacement = String.valueOf(loc.getX());
                        case 1, 4 -> replacement = String.valueOf(loc.getY());
                        case 2, 5 -> replacement = String.valueOf(loc.getZ());
                    }
                } else {
                    String modifier = s.replace("~","");
                    Location loc = p.getLocation();
                    switch (current) {
                        case 0, 3 -> replacement = String.valueOf(loc.getX() + Double.parseDouble(modifier));
                        case 1, 4 -> replacement = String.valueOf(loc.getY() + Double.parseDouble(modifier));
                        case 2, 5 -> replacement = String.valueOf(loc.getZ() + Double.parseDouble(modifier));
                    }
                }
                temp.set(current, replacement);
            }
        }
        try {
            loc1 = new Location(p.getWorld(), Double.parseDouble(temp.get(0)), Double.parseDouble(temp.get(1)), Double.parseDouble(temp.get(2)));
            loc2 = new Location(p.getWorld(), Double.parseDouble(temp.get(3)), Double.parseDouble(temp.get(4)), Double.parseDouble(temp.get(5)));
        } catch (NullPointerException | NumberFormatException e) {
            return new ArrayList<>();
        }
        List<Location> locs = new ArrayList<>();
        locs.add(loc1);
        locs.add(loc2);
        return locs;
    }
}
