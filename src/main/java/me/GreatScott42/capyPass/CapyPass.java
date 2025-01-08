package me.GreatScott42.capyPass;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.GreatScott42.capyPass.Commands.CapyPassCommand;
import me.GreatScott42.capyPass.Commands.CapyPassTabCompleter;
import me.GreatScott42.capyPass.Commands.battlePass;
import me.GreatScott42.capyPass.Events.misions;
import me.GreatScott42.capyPass.Events.players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class CapyPass extends JavaPlugin {

    private File playersInfoFile;
    private File battlePassFile;
    private File misionsFile;
    private File playersStatisticsFile;
    private FileConfiguration playersInfo;
    private FileConfiguration battlePass;
    private FileConfiguration misionsInfo;
    private FileConfiguration playersStatistics;
    private List<PlayerProfile> profiles = new ArrayList<>();
    private List<PlayerProfile> claimedProfiles = new ArrayList<>();
    private List<ItemStack> helpBooks = new ArrayList<>();

    @Override
    public void onEnable() {
        //creacion de libros de ayuda de marco
        createHelpBooks();
        //creaicon de Skins para cabezas
        createPlayerProfiles();
        createPlayerClaimedProfiles();
        //creacion configFile
        saveDefaultConfig();
        //creacion de archivo de informacion de plugin
        createPlayerInfoFile();
        createBattlePassFile();
        createMisionsFile();
        createPlayerStatisticsFile();
        //registro de eventos
        getServer().getPluginManager().registerEvents(new players(this),this);
        getServer().getPluginManager().registerEvents(new misions(this),this);
        //registro de comandos
        getCommand("battlepass").setExecutor(new battlePass(this));
        getCommand("capypass").setExecutor(new CapyPassCommand(this));
        getCommand("misions").setExecutor(new misions(this));
        getCommand("capypass").setTabCompleter(new CapyPassTabCompleter());
        Bukkit.getLogger().info("[CapyPass] CapyPass is enabled");
        /*File config = new File(getDataFolder(),"playersinfo.yml");
        File configBackup = new File(getDataFolder(),"playersinfoBU.yml");
        try {
            Files.copy(config.toPath(), configBackup.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[CapyPass] CapyPass is disabled");

    }
    public void createHelpBooks(){
        //b1
        ItemStack b1 = new ItemStack(Material.BOOK);
        ItemMeta b1M = b1.getItemMeta();
        b1M.setDisplayName("¿Como subo de nivel?");

        ArrayList<String> b1_lore = new ArrayList<>();
        b1_lore.add("Para subir de nivel debes conseguir mil puntos");
        b1M.setLore(b1_lore);

        b1.setItemMeta(b1M);
        helpBooks.add(b1);

    }
    public List<ItemStack> getHelpBooks(){
        return helpBooks;
    }

    //metodos para informacion de jugadores
    public FileConfiguration getPlayersInfo() {
        return this.playersInfo;
    }
    public FileConfiguration getBattlePass() {
        return this.battlePass;
    }
    public FileConfiguration getMisions() {
        return this.misionsInfo;
    }
    public FileConfiguration getStatistics() {
        return this.playersStatistics;
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
    public void saveMisions(){
        try {
            misionsInfo.save(misionsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void savePlayersStatistics(){
        try {
            playersStatistics.save(playersStatisticsFile);
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
    private void createMisionsFile() {
        misionsFile = new File(getDataFolder(), "misions.yml");
        if (!misionsFile.exists()) {
            misionsFile.getParentFile().mkdirs();
            saveResource("misions.yml", false);
        }
        misionsInfo = new YamlConfiguration();
        try {
            misionsInfo.load(misionsFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    private void createPlayerStatisticsFile() {
        playersStatisticsFile = new File(getDataFolder(), "statistics.yml");
        if (!playersStatisticsFile.exists()) {
            playersStatisticsFile.getParentFile().mkdirs();
            saveResource("statistics.yml", false);
        }
        playersStatistics = new YamlConfiguration();
        try {
            playersStatistics.load(playersStatisticsFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    private void createPlayerProfiles(){
        //profiles with textures 1-9
        PlayerProfile n1 = Bukkit.createProfile(UUID.randomUUID());
        n1.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg0MzJhNTc1NmEwNGViZjA2MmQ3MmE2ZjMxYmQ2MmU4ZjRkODJhOTIxMjAzMzZhZTE5NzJmZTE4ZDM4NzBiYSJ9fX0="));
        profiles.add(n1);
        //
        PlayerProfile n2 = Bukkit.createProfile(UUID.randomUUID());
        n2.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2U1MGM3MDk3OTk0MzEzZDk0MzIxNDJkYTc2NTFkYzZkZDYzMzU4N2UyZTFkZDlhNTYyYWJiYzc4NzhlZmI2NSJ9fX0="));
        profiles.add(n2);
        //
        PlayerProfile n3 = Bukkit.createProfile(UUID.randomUUID());
        n3.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRkMjJkYjhjNmUyMzhmYjhjYzA4MTlkMDJhNjU0MDMyOTdkNjNiNjdjNmM3Y2U2YjQzYmM4MjkxODk4MzdmNCJ9fX0="));
        profiles.add(n3);
        //
        PlayerProfile n4 = Bukkit.createProfile(UUID.randomUUID());
        n4.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU0YzFkZWQ5MjMxOWJkODM1NzNmMGYwMDQxZTczMDMzOGViN2JiNzk5N2ViNzFmZjU4M2MyOTA4MzIzODg4ZSJ9fX0="));
        profiles.add(n4);
        //
        PlayerProfile n5 = Bukkit.createProfile(UUID.randomUUID());
        n5.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRkYWM3Y2YyMDE3YTJhZWZjZGYyOWRjMzgzMmQ0MDdjYmQ5YzhiNmJhMGU1MWEwYTMxNjlmNmZmYjYyYzAxNSJ9fX0="));
        profiles.add(n5);
        //
        PlayerProfile n6 = Bukkit.createProfile(UUID.randomUUID());
        n6.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGVkOWJlZGNjMWQxYzQ4Y2FlZTU3MjhlMWVmOWIwMDhkNWE1ZDMwZDJlMTRjMWM3Yjg0OWU4ZDg1NTNiNTI1NyJ9fX0="));
        profiles.add(n6);
        //
        PlayerProfile n7 = Bukkit.createProfile(UUID.randomUUID());
        n7.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTBiYmE4ZDU2YmI2YjkwNjY4M2IxOGZiYTQ4MWQ1OGZhMGJiN2QzYjNiMThjNjQ1MmU5MjU3ZGY1NDJmNTNhYSJ9fX0="));
        profiles.add(n1);
        //
        PlayerProfile n8 = Bukkit.createProfile(UUID.randomUUID());
        n8.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5Y2Q3NGMwYThhMmYwMTA1NTg4NmY4MTVmOGUxZWQ2ZDdkZTZlNzg4YWFlOWY4NmJkMzdlMmQ0ZTQ2MjFjNiJ9fX0="));
        profiles.add(n8);
        //
        PlayerProfile n9 = Bukkit.createProfile(UUID.randomUUID());
        n9.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTljZDcyNjlhMzE0MDQ4YmM3Zjc5Mjk0NDhhYWJmNGJiYzlkNjk4MTdhOTJmZjUwNTZiMDY1YWRkOTQwODU4OSJ9fX0="));
        profiles.add(n9);
        //
    }
    private void createPlayerClaimedProfiles(){
        //profiles with textures 1-9
        PlayerProfile n1 = Bukkit.createProfile(UUID.randomUUID());
        n1.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2NkNjQ2OTQxMGY1ZjE2MjlkYzBkZTI4NGZjZWJmZmY1M2M2NDRmMGNiNWE0ZTJiYmE5NWE3ZjE2ZjVkIn19fQ=="));
        claimedProfiles.add(n1);
        //
        PlayerProfile n2 = Bukkit.createProfile(UUID.randomUUID());
        n2.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmY5MTE0ODc2N2MzOGI1Njc1ZDRiMzY4N2FiOGZiZjhhN2FjODBiNTQ4N2JlZDg4MTM4M2QxMTRmNTRkZjcwIn19fQ=="));
        claimedProfiles.add(n2);
        //
        PlayerProfile n3 = Bukkit.createProfile(UUID.randomUUID());
        n3.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGM0MmFkYjEzMmQ4MWM3YmNjMzMzNjk4ZDJmN2M3MDMzZmQ4ZjM4MTU3Yjg0YTVhOWU3MzA3ZGZlZjkxMSJ9fX0="));
        claimedProfiles.add(n3);
        //
        PlayerProfile n4 = Bukkit.createProfile(UUID.randomUUID());
        n4.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTkxMmYyNTk4MjhmM2Q0MDEzOThlYjFjNTRhMDNlYzIwOTM2NGUyMWIzODUxODg4M2JiNTRiNjdlNzVlMSJ9fX0="));
        claimedProfiles.add(n4);
        //
        PlayerProfile n5 = Bukkit.createProfile(UUID.randomUUID());
        n5.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI3MzFmYjMzOGExNDQ4MjJkZDUzNzNlNmFkMjQ1NWM5OWZhZmEyMWE5ODkyYWM4ZGQ4NTM5NmI4NzJiODk1MSJ9fX0="));
        claimedProfiles.add(n5);
        //
        PlayerProfile n6 = Bukkit.createProfile(UUID.randomUUID());
        n6.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE4MGRiOGVmZmMwNzNjNjc5YzVkNGNmYWNlZDQ4M2ZlNWViNGQ3MWVkYWNkNjQ2MTFjZGY3MzVmMTI5NDcifX19"));
        claimedProfiles.add(n6);
        //
        PlayerProfile n7 = Bukkit.createProfile(UUID.randomUUID());
        n7.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMDg3MmRlNWU1MmEyZTJiMzVmZTkyYzkzYTVkZjZhZjZkM2E5NmQ3N2IxZWQ2N2Q0ZDM1ZDg3NTM5YjQ1NiJ9fX0="));
        claimedProfiles.add(n1);
        //
        PlayerProfile n8 = Bukkit.createProfile(UUID.randomUUID());
        n8.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWJlZDhjNTAzZTZhNzQyMTljZjYzYmE4MTI0OTU3NDk0ZWRjNWZkYTFkNGMxM2UzMTA5MTlkN2NlZjg3NWUifX19"));
        claimedProfiles.add(n8);
        //
        PlayerProfile n9 = Bukkit.createProfile(UUID.randomUUID());
        n9.setProperty(new ProfileProperty("textures","eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDdkMGI2ZTZkZmFmNmJlMjdhMzdhZjE0ODNlOWRlNjBmZmZmNWU0OGY1ZmM3YzI1YmQ0ODg0NjMzYWEzZCJ9fX0="));
        claimedProfiles.add(n9);
        //
    }
    public PlayerProfile profileList(int i){
        return profiles.get(i);
    }
    public PlayerProfile claimedProfileList(int i){
        return claimedProfiles.get(i);
    }

    public String chatColor(String original){
        for (int i = 0; i <= 15; i++) {
            if(i>9){
                switch (i){
                    case 10:
                        original = original.replace("%" + "a", ChatColor.getByChar(String.valueOf("a")).toString());
                        continue;
                    case 11:
                        original = original.replace("%" + "b", ChatColor.getByChar(String.valueOf("b")).toString());
                        continue;
                    case 12:
                        original = original.replace("%" + "c", ChatColor.getByChar(String.valueOf("c")).toString());
                        continue;
                    case 13:
                        original = original.replace("%" + "d", ChatColor.getByChar(String.valueOf("d")).toString());
                        continue;
                    case 14:
                        original = original.replace("%" + "e", ChatColor.getByChar(String.valueOf("e")).toString());
                        continue;
                    case 15:
                        original = original.replace("%" + "f", ChatColor.getByChar(String.valueOf("f")).toString());
                        continue;
                    default:
                        continue;
                }
            }else{
                original = original.replace("%" + i, ChatColor.getByChar(String.valueOf(i)).toString());
            }

        }

        return ChatColor.translateAlternateColorCodes('&', original);
    }
}
