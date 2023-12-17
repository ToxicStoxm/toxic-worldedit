package com.x_tornado10.craftattack.commands.area_command;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.craftattack;
import com.x_tornado10.craftattack.plmsg.PlayerMessages;
import com.x_tornado10.craftattack.utils.CustomBlock;
import com.x_tornado10.craftattack.utils.id.Id;
import com.x_tornado10.craftattack.utils.mgrs.SaveMgr;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getServer;

public class AreaCommand implements CommandExecutor, Listener {

    private final craftattack plugin;
    private final Logger logger;
    private final PlayerMessages plmsg;
    private HashMap<Location, BlockData> toDo;
    private boolean loading = false;
    private final SaveMgr saveMgr;

    public AreaCommand() {
        plugin = craftattack.getInstance();
        logger = plugin.getLogger();
        plmsg = plugin.getPlmsg();
        saveMgr = plugin.getSaveMgr();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender,@NonNull Command command,@NonNull String s,@NonNull String[] args) {
        if (!(commandSender instanceof Player p)) {
            logger.info("This command is not yet supported for console!");
            return true;
        }
        switch (args.length) {
            case 5 -> {
                if (!args[0].equalsIgnoreCase("load")) {
                    plmsg.msg(p, "Please provide valid arguments!");
                    return true;
                }
                Location loc;
                try {
                    List<String> temp = new ArrayList<>();
                    temp.add(args[2]);
                    temp.add(args[3]);
                    temp.add(args[4]);
                    temp.add("0");
                    temp.add("0");
                    temp.add("0");
                    List<Location> locs = getCoordinates(p, temp);
                    loc = locs.get(0);
                } catch (NullPointerException | NumberFormatException e) {
                    plmsg.msg(p, "Please provide valid arguments!");
                    return true;
                }
                int indexOfArea = getIndexOfArea(args[1]);
                if (indexOfArea == -1) {
                    plmsg.msg(p, "Please provide valid arguments!");
                    return true;
                }
                Area a = craftattack.areaList.get(indexOfArea);
                World w = p.getWorld();
                toDo = new HashMap<>();
                for (Map.Entry<Id, CustomBlock> e : a.getBlocks().entrySet()) {
                    Id id = e.getKey();
                    Location loc0 = getCoords(id, loc);
                    BlockData bdata = e.getValue().getBlockData();
                    toDo.put(loc0,bdata);
                }
                loading = true;
                for (Map.Entry<Location, BlockData> entry : toDo.entrySet()) {
                    w.getBlockAt(entry.getKey()).setBlockData(entry.getValue());
                }
                loading = false;
                plmsg.msg(p, "Successfully loaded " + a.getBlockCount() + " blocks!");
                return true;
            }
            case 8 -> {
                if (!args[0].equalsIgnoreCase("read")) {
                    plmsg.msg(p, "Please provide valid arguments!");
                    return true;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        List<String> coords = new ArrayList<>(List.of(args));
                        coords.remove(args[0]);
                        coords.remove(args[1]);
                        Area a = new Area(args[1], getArea(p,coords));
                        craftattack.areaList.remove(a);
                        craftattack.areaList.add(a);
                        a = null;
                        plmsg.msg(p,"DONE");
                        preSave();
                    }
                }.runTaskAsynchronously(plugin);
                return true;
            }
            default -> {
                plmsg.msg(p, "Please provide valid arguments!");
                return true;
            }

        }
    }

    private int getIndexOfArea(String arg) {
        for (Area a : craftattack.areaList) {
            if (a.getName().equals(arg)) return craftattack.areaList.indexOf(a);
        }
        return -1;
    }

    private Location getCoords(Id id, Location referencePoint) {
        return new Location(null, referencePoint.getX() + id.x(), referencePoint.getY() + id.y(), referencePoint.getZ() + id.z());
    }

    private HashMap<Id, CustomBlock> getArea(Player p, List<String> args) {
        List<Location> locs = getCoordinates(p, args);
        Location corner1 = locs.get(0);
        Location corner2 = locs.get(1);
        HashMap<Id, CustomBlock> result = new HashMap<>();

        List<CustomBlock> blocks = new ArrayList<>();

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());

        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    blocks.add(new CustomBlock(Objects.requireNonNull(corner1.getWorld()).getBlockAt(x, y, z)));
                }
            }
        }
        CustomBlock origin = new CustomBlock(p.getLocation().getBlock());
        /*for (CustomBlock b : blocks) {
            if (nearOrigin(b,origin)) origin = b;
        }*/
        result.put(new Id(0,0,0), origin);
        blocks.remove(origin);
        for (CustomBlock b : blocks) {
            result.put(new Id(xDifToOrigin(b, origin), yDifToOrigin(b, origin), zDifToOrigin(b,origin)), b);
        }
        return result;
    }

    private boolean nearOrigin(CustomBlock b1, CustomBlock b2) {
        Location loc1 = b1.getLocation();
        Location loc2 = b2.getLocation();
        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();
        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();
        return x1 < x2 || y1 < y2 || z1 < z2;
    }
    private int xDifToOrigin(CustomBlock b, CustomBlock origin) {
        return b.getLocation().getBlockX() - origin.getLocation().getBlockX();
    }
    private int yDifToOrigin(CustomBlock b, CustomBlock origin) {
        return b.getLocation().getBlockY() - origin.getLocation().getBlockY();
    }
    private int zDifToOrigin(CustomBlock b, CustomBlock origin) {
        return b.getLocation().getBlockZ() - origin.getLocation().getBlockZ();
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
    private void preSave() {
        getServer().getScheduler().runTaskAsynchronously(plugin, saveMgr::save);
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (toDo.containsValue(e.getBlock().getBlockData()) && loading) {
            e.setCancelled(true);
        }
    }
}
