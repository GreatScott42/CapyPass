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
        if(args.length!=2){
            return false;
        }
        Player p = Bukkit.getPlayerExact(args[0]);
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
            default:
                try {
                    Integer.parseInt(args[1]);
                    plugin.getPlayersInfo().set("players."+p.getUniqueId()+".level",Integer.parseInt(args[1]));
                    plugin.saveplayersInfo();
                    p.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-level").replace("{player}",p.getName())+Integer.parseInt(args[1])));
                    player.sendMessage(plugin.chatColor(plugin.getConfig().getString("changed-player-level").replace("{player}",p.getName())+Integer.parseInt(args[1])));
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
        }
    }
}