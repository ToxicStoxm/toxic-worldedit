package com.x_tornado10.craftattack.commands.save;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SaveCommandTabCompletion implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender,@NonNull Command command,@NonNull String s, @NonNull String[] args) {
        if (!(commandSender instanceof Player p)) return null;
        List<String> saveArgs = new ArrayList<>();
        Location location = p.getTargetBlock(null,5).getLocation();
        switch (args.length) {
            case 1, 4 -> saveArgs.add(location.getBlock().getType().isSolid() ? String.valueOf(location.getX()) : "~");
            case 2, 5 -> saveArgs.add(location.getBlock().getType().isSolid() ? String.valueOf(location.getY()) : "~");
            case 3, 6 -> saveArgs.add(location.getBlock().getType().isSolid() ? String.valueOf(location.getZ()) : "~");
            case 7 -> saveArgs.add("<Name>");
            case 8 -> {
                saveArgs.add("true");
                saveArgs.add("false");
            }
        }
        return saveArgs;
    }
}
