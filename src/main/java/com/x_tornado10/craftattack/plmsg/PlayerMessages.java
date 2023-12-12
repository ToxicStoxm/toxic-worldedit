package com.x_tornado10.craftattack.plmsg;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerMessages {
    private final String prefix;
    public PlayerMessages(String prefix) {
        this.prefix = prefix;
    }

    public void msg(Player p, String message) {
        p.sendMessage( String.valueOf(ChatColor.GRAY) + ChatColor.BOLD + prefix + ChatColor.RESET + ChatColor.GRAY + message);
    }
}
