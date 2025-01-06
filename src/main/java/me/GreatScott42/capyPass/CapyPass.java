package me.GreatScott42.capyPass;

import me.GreatScott42.capyPass.Commands.battlePass;
import me.GreatScott42.capyPass.Events.players;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class CapyPass extends JavaPlugin {

    private File playersInfoFile;
    private File battlePassFile;
    private FileConfiguration playersInfo;
    private FileConfiguration battlePass;

    @Override
    public void onEnable() {
        //creacion de archivo de informacion de jugadores
        createPlayerInfoFile();
        createBattlePassFile();
        //registro de eventos
        getServer().getPluginManager().registerEvents(new players(this),this);
        //registro de comandos
        getCommand("battlepass").setExecutor(new battlePass(this));
        Bukkit.getLogger().info("[CapyPass] CapyPass is enabled");

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[CapyPass] CapyPass is disabled");

    }

    //metodos para informacion de jugadores
    public FileConfiguration getPlayersInfo() {
        return this.playersInfo;
    }
    public FileConfiguration getBattlePass() {
        return this.battlePass;
    }

    public void saveplayersInfo(){
        try {
            playersInfo.save(playersInfoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveBattlePass(){
        try {
            battlePass.save(battlePassFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPlayerInfoFile() {
        playersInfoFile = new File(getDataFolder(), "playersInfo.yml");
        if (!playersInfoFile.exists()) {
            playersInfoFile.getParentFile().mkdirs();
            saveResource("playersInfo.yml", false);
        }
        playersInfo = new YamlConfiguration();
        try {
            playersInfo.load(playersInfoFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    private void createBattlePassFile() {
        battlePassFile = new File(getDataFolder(), "battlePass.yml");
        if (!battlePassFile.exists()) {
            battlePassFile.getParentFile().mkdirs();
            saveResource("battlePass.yml", false);
        }
        battlePass = new YamlConfiguration();
        try {
            battlePass.load(battlePassFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
