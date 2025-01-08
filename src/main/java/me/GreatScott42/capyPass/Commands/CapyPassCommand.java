package me.GreatScott42.capyPass.Commands;

import me.GreatScott42.capyPass.CapyPass;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CapyPassCommand implements CommandExecutor {

    private CapyPass plugin;
    public CapyPassCommand(CapyPass plugin){
        this.plugin=plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        Player p = Bukkit.getPlayerExact(args[0]);
        //lop=level or points
        int lop=0;
        if(args[2]!=null){
            lop = Integer.parseInt(args[2]);
        }
        switch (args[1]){
            case "premium":
                plugin.getPlayersInfo().set("players."+p.getUniqueId()+".free",false);
                plugin.saveplayersInfo();
                p.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-status-to-premium").replace("{player}",p.getName())));
                player.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-status-to-premium").replace("{player}",p.getName())));
                return true;
            case "free":
                plugin.getPlayersInfo().set("players."+p.getUniqueId()+".free",true);
                plugin.saveplayersInfo();
                p.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-status-to-free").replace("{player}",p.getName())));
                player.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-status-to-free").replace("{player}",p.getName())));
                return true;
            case "level":
                try {
                    Integer.parseInt(args[2]);
                    plugin.getPlayersInfo().set("players."+p.getUniqueId()+".level",lop);
                    plugin.saveplayersInfo();
                    p.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-level").replace("{player}",p.getName())+lop));
                    player.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-level").replace("{player}",p.getName())+lop));
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case "points":
                try {
                    Integer.parseInt(args[2]);
                    plugin.getPlayersInfo().set("players."+p.getUniqueId()+".points",lop);
                    plugin.saveplayersInfo();
                    p.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-points").replace("{player}",p.getName())+lop));
                    player.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-points").replace("{player}",p.getName())+lop));
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            default:
                return false;
        }
    }
}