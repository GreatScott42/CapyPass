package me.GreatScott42.capyPass.Events;

import me.GreatScott42.capyPass.CapyPass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class players implements Listener {

    private CapyPass plugin;
    private boolean success;
    public players(CapyPass plugin){
        this.plugin=plugin;
    }
    public void executeCommands(String level,Player pl, Boolean free){

        if(free){
            if(plugin.getPlayersInfo().getBoolean("players."+pl.getUniqueId()+".claimed"+".free"+".level"+level)){
                pl.sendMessage(plugin.chatColor(plugin.getConfig().getString("claimed-reward-message")));
                return;
            }else if(!plugin.getPlayersInfo().getBoolean("players."+pl.getUniqueId()+".claimed"+".free"+".level"+level)){
                plugin.getPlayersInfo().set("players."+pl.getUniqueId()+".claimed"+".free."+"level"+level,true);
                plugin.saveplayersInfo();
            }
            for(String command : plugin.getBattlePass().getStringList("battlepass.free.level"+level+".commands")){
                command = command.replace("{player}",pl.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                success=true;
            }
        }else if(!free){
            if(plugin.getPlayersInfo().getBoolean("players."+pl.getUniqueId()+".claimed"+".premium"+".level"+level)){
                pl.sendMessage(plugin.chatColor(plugin.getConfig().getString("claimed-reward-message")));
                return;
            }else if(!plugin.getPlayersInfo().getBoolean("players."+pl.getUniqueId()+".claimed"+".premium"+".level"+level)){
                plugin.getPlayersInfo().set("players."+pl.getUniqueId()+".claimed"+".premium"+".level"+level,true);
                plugin.saveplayersInfo();
            }
            for(String command : plugin.getBattlePass().getStringList("battlepass.premium.level"+level+".commands")){
                command = command.replace("{player}",pl.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                success=true;
            }
        }

    }
    public boolean CheckPlayerLvl(Player pl, int level){
        if(plugin.getPlayersInfo().getInt("players."+pl.getUniqueId()+".level")>=level){
            return true;
        }
        return false;
    }
    public boolean CheckPlayerFree(Player player){
        if(plugin.getPlayersInfo().getBoolean("players."+player.getUniqueId()+".free")){
            return true;
        }
        return false;
    }

    @EventHandler
    public void RegisterPlayer(PlayerJoinEvent e){
        if(!plugin.getPlayersInfo().contains(String.valueOf(e.getPlayer().getUniqueId()))){
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".points", 0);
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".level", 0);
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".free", true);
            plugin.saveplayersInfo();
        }
        for(String k : plugin.getBattlePass().getConfigurationSection("battlepass.free").getKeys(false)){
            if(!plugin.getPlayersInfo().contains("players."+e.getPlayer().getUniqueId()+".claimed.free."+k)){
                plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".claimed.free."+k,false);
            }
        }
        for(String k : plugin.getBattlePass().getConfigurationSection("battlepass.premium").getKeys(false)){
            if(!plugin.getPlayersInfo().contains("players."+e.getPlayer().getUniqueId()+".claimed.premium."+k)){
                plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".claimed.premium."+k,false);
            }
        }
        plugin.saveplayersInfo();
    }
    @EventHandler
    public void Menu(InventoryClickEvent e){
        success=false;
        ItemStack item = e.getCurrentItem();
        int level;
        if(e.getCurrentItem()==null){
            return;
        }
        level = Character.getNumericValue(item.getItemMeta().getDisplayName().charAt(item.getItemMeta().getDisplayName().length()-1));
        if(e.getView().getTitle().equalsIgnoreCase(plugin.chatColor(plugin.getConfig().getString("gui-title")))){
            Player player = (Player) e.getWhoClicked();

            switch(e.getCurrentItem().getType()){
                case PLAYER_HEAD:
                    boolean free;
                    if(item.getItemMeta().getLore().get(0).equalsIgnoreCase(plugin.chatColor(plugin.getConfig().getString("free-level-lore")))){
                        free = true;
                    }else{
                        free = false;
                    }
                    if(CheckPlayerFree(player)){
                        if(free){
                            if(CheckPlayerLvl(player,level)){
                                executeCommands(String.valueOf(level),player,free);

                            }else{
                                player.sendMessage(plugin.chatColor(plugin.getConfig().getString("no-levels-required")));
                            }
                        }else{
                            player.sendMessage(plugin.chatColor(plugin.getConfig().getString("no-premium-pass")));
                        }
                    }else if(!CheckPlayerFree(player)){
                        if(CheckPlayerLvl(player,level)){
                            executeCommands(String.valueOf(level),player,free);

                        }else{
                            player.sendMessage(plugin.chatColor(plugin.getConfig().getString("no-levels-required")));
                        }
                    }
                    break;
                default:
                    break;
            }

            e.setCancelled(true);
            if(success){
                ItemMeta itemMeta = item.getItemMeta();
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                skullMeta.setPlayerProfile(plugin.claimedProfileList(level-1));
                List<String> lore = itemMeta.getLore();
                lore.set(1,plugin.chatColor(plugin.getConfig().getString("reclaimed-word")));
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);

                e.getView().setItem(e.getSlot(), item);
            }

        }
    }

}