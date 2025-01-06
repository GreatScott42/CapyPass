package me.GreatScott42.capyPass.Events;

import me.GreatScott42.capyPass.CapyPass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class players implements Listener {

    private CapyPass plugin;
    public players(CapyPass plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void RegisterPlayer(PlayerJoinEvent e){
        if(!plugin.getPlayersInfo().contains(String.valueOf(e.getPlayer().getUniqueId()))){
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".points", 0);
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".level", 0);
            plugin.saveplayersInfo();
        }
    }
}