package me.GreatScott42.capyPass.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CapyPassTabCompleter implements TabCompleter{
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        List<String> commands = new ArrayList<>();
        if(strings.length==2){
            List<String> suggestions = new ArrayList<>();
            suggestions.add("premium");
            suggestions.add("free");
            suggestions.add("<1-9>");

            for (String c : suggestions){
                if(c.toLowerCase().startsWith(strings[1].toLowerCase())){
                    commands.add(c);
                }
            }
            return commands;
        }
        return null;
    }
}
