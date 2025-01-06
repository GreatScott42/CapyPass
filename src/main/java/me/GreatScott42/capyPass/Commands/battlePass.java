package me.GreatScott42.capyPass.Commands;

import me.GreatScott42.capyPass.CapyPass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class battlePass implements CommandExecutor {

    private CapyPass plugin;
    public battlePass(CapyPass plugin){
        this.plugin=plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        if(args.length==0){
            for(String level : plugin.getBattlePass().getConfigurationSection("battlepass").getKeys(false)){
                player.sendMessage(level+" reward:"+plugin.getBattlePass().get("battlepass."+level+".reward")+" amount:"+plugin.getBattlePass().get("battlepass."+level+".amount"));

            }
            return true;
        }
        if(args[0]=="claim"){
            if(args[1]=="1"){
                if(checkPlayerLvl(player,1)){
                    player.sendMessage("level 1 reward claimed");
                }else{
                    player.sendMessage("you have not the level");
                }
            }
        }

        return true;
    }
    public boolean checkPlayerLvl(Player pl, int i){
        if((int)plugin.getPlayersInfo().get(pl.getUniqueId()+".level")==i){
            return true;
        }
        return false;
    }
}
