package me.GreatScott42.capyPass.Commands;

import me.GreatScott42.capyPass.CapyPass;
import me.GreatScott42.capyPass.Database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BattlePass implements CommandExecutor {

    private CapyPass plugin;
    private DatabaseManager db;
    //definitions
    private String pointWord;
    private String levelWord;
    public BattlePass(CapyPass plugin){
        this.plugin=plugin;
        this.pointWord = plugin.chatColor(plugin.getConfig().getString("point-word"));
        this.levelWord = plugin.chatColor(plugin.getConfig().getString("level-word"));
        this.db = plugin.getDatabase();
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;
        String playerUUID = String.valueOf(player.getUniqueId());
        boolean isfree = ((Integer) db.getPlayerAttribute(playerUUID,"free"))==1;
        if(args.length==0){
            //gui menu//
            Inventory gui = Bukkit.createInventory(player, plugin.getConfig().getInt("gui-size"), plugin.chatColor(plugin.getConfig().getString("gui-title")));
            //Menu Options(Items)

            //player info start
            ItemStack info = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta info_meta = info.getItemMeta();
            info_meta.setDisplayName(player.getName()+" Info");
            ArrayList<String> info_lore = new ArrayList<>();
            //points and level of player
            info_lore.add(pointWord+db.getPlayerAttribute(playerUUID,"points"));
            info_lore.add(levelWord+db.getPlayerAttribute(playerUUID,"level"));
            //
            if(isfree){
                info_lore.add(plugin.chatColor(plugin.getConfig().getString("current-status-free")));
            }else if(!isfree){
                info_lore.add(plugin.chatColor(plugin.getConfig().getString("current-status-premium")));
            }
            info_meta.setLore(info_lore);
            SkullMeta skullMeta = (SkullMeta) info_meta;
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
            info.setItemMeta(info_meta);
            //player info end
            //help head
            ItemStack help = new ItemStack(Material.PIGLIN_HEAD);
            ItemMeta help_meta = help.getItemMeta();
            help_meta.setDisplayName(plugin.chatColor(plugin.getConfig().getString("battlepass-gui-text")));
            help.setItemMeta(help_meta);
            //
            List<ItemStack> levelsfree = new ArrayList<>();
            for(int i = 0 ; i < plugin.getBattlePass().getConfigurationSection("battlepass.free").getKeys(false).size();i++){
                ItemStack level = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta level_meta = level.getItemMeta();

                ArrayList<String> level_lore = new ArrayList<>();
                ///////////////////////!!!!!!!!!!!!!!!!!////////////////////
                level_lore.add(plugin.chatColor(plugin.getConfig().getString("free-level-lore")));
                ///////////////////////!!!!!!!!!!!!!!!!!////////////////////

                //if reward claimed already
                if(plugin.getPlayersInfo().getBoolean("players."+player.getUniqueId()+".claimed.free.level"+(i+1))){
                    SkullMeta skullMeta1 = (SkullMeta) level_meta;
                    skullMeta1.setPlayerProfile(plugin.claimedProfileList(i));
                    level_lore.add(plugin.chatColor(plugin.getConfig().getString("reclaimed-word")));
                }else if(!plugin.getPlayersInfo().getBoolean("players."+player.getUniqueId()+".claimed.free.level"+(i+1))){
                    SkullMeta skullMeta1 = (SkullMeta) level_meta;
                    skullMeta1.setPlayerProfile(plugin.profileList(i));
                    level_lore.add(plugin.chatColor(plugin.getConfig().getString("unreclaimed-word")));
                }
                level_meta.setLore(level_lore);
                level_meta.setDisplayName(plugin.getConfig().getString("level-word")+(i+1));
                level.setItemMeta(level_meta);
                levelsfree.add(level);
            }
            List<ItemStack> levelspremium = new ArrayList<>();
            for(int i = 0 ; i < plugin.getBattlePass().getConfigurationSection("battlepass.premium").getKeys(false).size();i++){
                ItemStack level = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta level_meta = level.getItemMeta();

                ArrayList<String> level_lore = new ArrayList<>();
                level_lore.add(plugin.chatColor(plugin.getConfig().getString("premium-level-lore")));

                if(plugin.getPlayersInfo().getBoolean("players."+player.getUniqueId()+".claimed.premium.level"+(i+1))){
                    SkullMeta skullMeta1 = (SkullMeta) level_meta;
                    skullMeta1.setPlayerProfile(plugin.claimedProfileList(i));
                    level_lore.add(plugin.chatColor(plugin.getConfig().getString("reclaimed-word")));
                }else if(!plugin.getPlayersInfo().getBoolean("players."+player.getUniqueId()+".claimed.premium.level"+(i+1))){
                    SkullMeta skullMeta1 = (SkullMeta) level_meta;
                    skullMeta1.setPlayerProfile(plugin.profileList(i));
                    level_lore.add(plugin.chatColor(plugin.getConfig().getString("unreclaimed-word")));
                }

                level_meta.setLore(level_lore);
                level_meta.setDisplayName(plugin.chatColor(plugin.getConfig().getString("level-word")+(i+1)));
                level.setItemMeta(level_meta);
                levelspremium.add(level);
            }
            String backgroundMat = plugin.getConfig().getString("gui-background");
            ItemStack background = new ItemStack(Material.valueOf(backgroundMat));

            ItemMeta current_meta = background.getItemMeta();
            current_meta.setDisplayName(plugin.chatColor(plugin.getConfig().getString("background-item-name")));
            background.setItemMeta(current_meta);

            for(int i=0;i<gui.getSize();i++){
                gui.setItem(i,background);

            }

            for(int i=0;i<levelsfree.size();i++){
                gui.setItem(i+plugin.getConfig().getInt("free-level-start"),levelsfree.get(i));
            }

            for(int i=0;i<levelspremium.size();i++){
                gui.setItem(i+plugin.getConfig().getInt("premium-level-start"),levelspremium.get(i));
            }

            gui.setItem(9,info);
            gui.setItem(17,help);

            player.openInventory(gui);
            return true;
        }
        return true;
    }
}
