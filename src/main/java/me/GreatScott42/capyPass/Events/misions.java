package me.GreatScott42.capyPass.Events;

import me.GreatScott42.capyPass.CapyPass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class misions implements Listener, CommandExecutor {
    private CapyPass plugin;
    private boolean success;
    public misions(CapyPass plugin){
        this.plugin=plugin;
    }

    @EventHandler
    public void killsChecker(EntityDeathEvent e){
        Player player;
        if(e.getEntity().getKiller() instanceof Player) {
            player = e.getEntity().getKiller();
            plugin.getStatistics().set("statistics."+".entities."+e.getEntity().getType()+"."+player.getUniqueId()+".kill",plugin.getStatistics().getInt("statistics."+".entities."+e.getEntity().getType()+"."+player.getUniqueId()+".kill")+1);
            plugin.savePlayersStatistics();
        }else{
            return;
        }

        for(String mision : plugin.getMisions().getConfigurationSection("misions").getKeys(false)){
            if(plugin.getMisions().getString("misions."+mision+".objective").equalsIgnoreCase("KILL_ENTITY")){
                    List<String> players = plugin.getMisions().getStringList("misions."+mision+".finishers");
                    if(players.contains(String.valueOf(player.getUniqueId()))){
                        return;
                    }

                    int amount = plugin.getMisions().getInt("misions."+mision+".amount");
                    String mob = plugin.getMisions().getString("misions."+mision+".mob");
                    int killedByPlayer = plugin.getStatistics().getInt("statistics."+".entities."+mob+"."+player.getUniqueId()+".kill");

                    if(killedByPlayer%(amount)==0&&killedByPlayer>0){
                        player.sendMessage(plugin.chatColor(plugin.getMisions().getString("misions."+mision+".finish-message").replace("{title}",plugin.getMisions().getString("misions."+mision+".title"))));
                        //reset statistic
                        plugin.getStatistics().set("statistics.entities."+mob+"."+player.getUniqueId()+".kill",0);
                        plugin.savePlayersStatistics();
                        plugin.getPlayersInfo().set("players."+player.getUniqueId()+".points",plugin.getPlayersInfo().getInt("players."+player.getUniqueId()+".points")+plugin.getMisions().getInt("misions."+mision+".reward"));
                        plugin.saveplayersInfo();

                        players.add(String.valueOf(player.getUniqueId()));
                        plugin.getMisions().set("misions."+mision+".finishers",players);
                        plugin.saveMisions();
                        player.sendMessage("ganaste "+plugin.getMisions().getInt("misions."+mision+".reward")+" puntos");
                    }
            }
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(args.length==0){
            Inventory gui = Bukkit.createInventory(player, 27, "misiones");

            List<ItemStack> misions = new ArrayList<>();
            player.sendMessage(String.valueOf(plugin.getMisions().getConfigurationSection("misions").getKeys(false).size()));

            for(int i = 0; i < plugin.getMisions().getConfigurationSection("misions").getKeys(false).size(); i++){
                Bukkit.getLogger().info(plugin.getMisions().getString("misions.mision"+String.valueOf((i+1))+".item"));
                ItemStack mision = new ItemStack(Material.valueOf(plugin.getMisions().getString("misions.mision"+String.valueOf((i+1))+".item")));
                ItemMeta mision_meta = mision.getItemMeta();
                mision_meta.setDisplayName(plugin.getMisions().getString("misions.mision"+String.valueOf((i+1))+".title"));
                mision.setItemMeta(mision_meta);
                gui.setItem(i,mision);
                player.openInventory(gui);

            }
            return true;
        }
        return false;
    }
    @EventHandler
    public void gui(InventoryClickEvent e){
        if(e.getView().getTitle().equalsIgnoreCase("misiones")){
            e.setCancelled(true);
        }
    }
}