package com.x_tornado10.craftattack.commands.area_command;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.craftattack;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AreaCommandTabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender,@NonNull Command command,@NonNull String s,@NonNull String[] args) {
        if (!(commandSender instanceof Player p)) return null;
        List<String> areaArgs = new ArrayList<>();
        Location location = p.getTargetBlock(null,5).getLocation();
        switch (args.length) {
            case 0 -> {
                areaArgs.add("read");
                areaArgs.add("load");
            }
            case 1 -> {
                switch (args[0].toLowerCase()) {
                    case "" -> {
                        areaArgs.add("read");
                        areaArgs.add("load");
                    }
                    case "r","re","rea" -> areaArgs.add("read");
                    case "l","lo","loa" -> areaArgs.add("load");
                }
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("read")) {
                    areaArgs.add("<Name>");
                } else {
                    List<String> areaNames = getAreaNames();
                    if (args[1].equalsIgnoreCase("")) {
                        areaArgs.addAll(areaNames);
                    }
                    for (String s0 : areaNames) {
                        if (s.contains(args[1].toLowerCase())) areaArgs.add(s0);
                    }
                }
            }
            /*
            case 3, 6 -> {
                Location location = p.getTargetBlock(null,5).getLocation();
                if (location.getBlock().getType().isSolid()) {
                    if (args[2].isEmpty()) {
                        areaArgs.add(locToStringXYZ(location));
                        areaArgs.add(locToStringXY(location));
                        areaArgs.add(locToStringX(location));
                    } else {
                        areaArgs.add(args[2] + " " + locToStringY(location));
                        areaArgs.add(args[2] + " " + locToStringYZ(location));
                    }
                } else {
                    if (args[2].isEmpty()) {
                        areaArgs.add("~");
                        areaArgs.add("~ ~");
                        areaArgs.add("~ ~ ~");
                    } else {
                        areaArgs.add(args[2] + " ~");
                        areaArgs.add(args[2] + " ~ ~");
                    }
                }
            }
            case 4 -> {
                Location location = p.getTargetBlock(null,5).getLocation();
                if (location.getBlock().getType().isSolid()) {
                    if (args[3].isEmpty()) {
                        areaArgs.add(args[2] + " " + locToStringY(location));
                        areaArgs.add(args[2] + " " + locToStringYZ(location));
                    } else {
                        areaArgs.add(args[2] + " " + args[3] + locToStringZ(location));
                    }
                } else {
                    if (args[3].isEmpty()) {
                        areaArgs.add(args[2] + " ~");
                        areaArgs.add(args[2] + " ~ ~");
                    } else {
                        areaArgs.add(args[2] + " " + args[3] + " ~");
                    }
                }
            }
            case 7 -> {
                Location location = p.getTargetBlock(null,5).getLocation();
                if (location.getBlock().getType().isSolid()) {
                    if (args[6].isEmpty()) {
                        areaArgs.add(args[5] + " " +  locToStringY(location));
                        areaArgs.add(args[5]  + " " + locToStringYZ(location));
                    } else {
                        areaArgs.add(args[5] + " " + args[6] + " " + locToStringZ(location));
                    }
                }
                else {
                    if (args[6].isEmpty()) {
                        areaArgs.add(args[5] + " ~");
                        areaArgs.add(args[5] + " ~ ~");
                    } else {
                        areaArgs.add(args[5] + " " + args[6] + " ~");
                    }
                }
            }
            case 5 -> {
                Location location = p.getTargetBlock(null,5).getLocation();
                if (location.getBlock().getType().isSolid()) {
                    if (args[4].isEmpty()) areaArgs.add(args[2] + " " + args[3] + " " + locToStringZ(location));
                }
                else {
                    if (args[4].isEmpty()) areaArgs.add(args[2] + " " + args[3] + " ~");
                }
            }
            case 8 -> {
                Location location = p.getTargetBlock(null,5).getLocation();
                if (location.getBlock().getType().isSolid()) {
                    if (args[7].isEmpty()) areaArgs.add(args[5] + " " + args[6] + " " + locToStringZ(location));
                }
                else {
                    if (args[7].isEmpty()) areaArgs.add(args[5] + " " + args[6] + " ~");
                    areaArgs.add(args[6] + " ~ ~");
                }
            }
             */
            case 3, 6 -> areaArgs.add(location.getBlock().getType().isSolid() ? String.valueOf(location.getX()) : "~");
            case 4, 7 -> areaArgs.add(location.getBlock().getType().isSolid() ? String.valueOf(location.getY()) : "~");
            case 5, 8 -> areaArgs.add(location.getBlock().getType().isSolid() ? String.valueOf(location.getZ()) : "~");
        }
        return  areaArgs;
    }

    private String locToStringXYZ(Location loc) {
        return loc.getX() + " " + loc.getY() + " " + loc.getZ();
    }
    private String locToStringXY(Location loc) {
        return loc.getX() + " " + loc.getY();
    }
    private String locToStringX(Location loc) {
        return String.valueOf(loc.getX());
    }
    private String locToStringY(Location loc) {
        return String.valueOf(loc.getY());
    }
    private String locToStringZ(Location loc) {
        return String.valueOf(loc.getZ());
    }
    private String locToStringYZ(Location loc) {
        return loc.getY() + " " + loc.getZ();
    }

    private List<String> getAreaNames() {
        List<String> result = new ArrayList<>();
        for (Area a : craftattack.areaList) {
            result.add(a.getName());
        }
        return result;
    }
}
