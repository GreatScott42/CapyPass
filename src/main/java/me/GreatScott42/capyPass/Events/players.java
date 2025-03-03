package me.GreatScott42.capyPass.Events;

import me.GreatScott42.capyPass.CapyPass;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

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

        plugin.getDatabase().insertPlayer(String.valueOf(e.getPlayer().getUniqueId()));

        if(!plugin.getPlayersInfo().contains("players."+e.getPlayer().getUniqueId())){
            Bukkit.getLogger().info("[CapyPass] player "+e.getPlayer().getName()+" not found, creating register.");
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".points", 0);
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".level", 0);
            plugin.getPlayersInfo().set("players."+e.getPlayer().getUniqueId()+".free", true);
            plugin.saveplayersInfo();

            //
            for(EntityType entity : EntityType.values()){
                plugin.getStatistics().set("statistics."+".entities."+entity+"."+e.getPlayer().getUniqueId()+".kill",0);
            }
            plugin.savePlayersStatistics();
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
        new BukkitRunnable() {
            @Override
            public void run() {
                checkTimePlayed(e.getPlayer());
            }
            //(1sec=20ticks)20ticks*60sec=1m
        }.runTaskTimer(this.plugin, 0,20*60);


    }
    public void checkTimePlayed(Player player){
        int timeSec = player.getStatistic(Statistic.PLAY_ONE_MINUTE)/20;
        int timeMin = timeSec/60;
        int timeHor = timeMin/60;
        int timeDia = timeHor/25;
        int p = plugin.getPlayersInfo().getInt("players."+player.getUniqueId()+".points");
        int l = plugin.getPlayersInfo().getInt("players."+player.getUniqueId()+".level");
        plugin.getPlayersInfo().set("players."+player.getUniqueId()+".points", p+1);
        p++;
        plugin.saveplayersInfo();
        if(p>1000){
            player.sendMessage("level up!");
            plugin.getPlayersInfo().set("players."+player.getUniqueId()+".level", l+1);
            plugin.getPlayersInfo().set("players."+player.getUniqueId()+".points", 0);
        }
        plugin.saveplayersInfo();
        player.sendMessage("+1 point");
        //player.sendMessage("FELICIDADES, HAS JUGADO "+timeSec+" SEGUNDOS, "+timeMin+" MINUTOS, "+timeHor+" HORAS Y "+timeDia+" DIAS");
        //player.sendMessage("o tambien llevas: "+timeDia+"/"+timeHor%60+"/"+timeMin%60+"/"+timeSec%60);

    }

    @EventHandler
    public void Menu(InventoryClickEvent e){
        success=false;
        ItemStack item = e.getCurrentItem();
        int level;
        if(e.getCurrentItem()==null){
            return;
        }

        if(e.getView().getTitle().equalsIgnoreCase(plugin.chatColor(plugin.getConfig().getString("gui-title")))){
            level = Character.getNumericValue(item.getItemMeta().getDisplayName().charAt(item.getItemMeta().getDisplayName().length()-1));
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
                case PIGLIN_HEAD:
                    player.closeInventory();
                    player.playSound(player,Sound.ENTITY_PIGLIN_AMBIENT,1,1);
                    Inventory help = Bukkit.createInventory(player,9,"Marco te ayuda");

                    ItemStack marco = new ItemStack(Material.PIGLIN_HEAD);
                    ItemMeta help_meta = marco.getItemMeta();
                    help_meta.setDisplayName("Marco: Que necesitas saber?");
                    marco.setItemMeta(help_meta);

                    help.setItem(0,marco);

                    for(int i = 0; i<plugin.getHelpBooks().size();i++){
                        help.setItem(i+1,plugin.getHelpBooks().get(i));
                    }

                    player.openInventory(help);
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

        }else if(e.getView().getTitle().equalsIgnoreCase("Marco te ayuda")){
            Player player = (Player) e.getWhoClicked();

            switch(e.getCurrentItem().getType()){
                case PIGLIN_HEAD:
                    player.playSound(player,Sound.ENTITY_PIGLIN_AMBIENT,1,1);
                    e.setCancelled(true);
                    break;
                case BOOK:
                    if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("¿Como subo de nivel?")){
                        player.sendMessage("<Marco> Para subir de nivel debes conseguir mil puntos");
                    }
                    e.setCancelled(true);
                    break;
            }

        }
    }

}