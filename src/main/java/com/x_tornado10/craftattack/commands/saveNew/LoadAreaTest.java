package com.x_tornado10.craftattack.commands.saveNew;

import com.x_tornado10.craftattack.area.Area;
import com.x_tornado10.craftattack.craftattack;
import com.x_tornado10.craftattack.plmsg.PlayerMessages;
import com.x_tornado10.craftattack.utils.id.Id;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.logging.Logger;

public class LoadAreaTest implements CommandExecutor {

    private craftattack plugin;
    private Logger logger;
    private PlayerMessages plmsg;

    public LoadAreaTest() {
        plugin = craftattack.getInstance();
        logger = plugin.getLogger();
        plmsg = plugin.getPlmsg();
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player p)) {
            logger.info("This command is not yet supported for console!");
            return true;
        }
        if (args.length != 4) {
            plmsg.msg(p, "Please provide valid arguments!");
            return true;
        }

        Location loc = new Location(p.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));

        Area a = craftattack.a;
        World w = p.getWorld();
        for (Map.Entry<Id, Block> e : a.getBlocks().entrySet()) {
            Id id = e.getKey();
            w.getBlockAt(getCoords(id, loc)).setBlockData(e.getValue().getBlockData());
        }

        return true;
    }
    private Location getCoords(Id id, Location referencePoint) {
        return new Location(null, referencePoint.getX() + id.getX(), referencePoint.getY() + id.getY(), referencePoint.getZ() + id.getZ());
    }
}
