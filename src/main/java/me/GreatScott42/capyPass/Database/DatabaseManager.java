package me.GreatScott42.capyPass.Database;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.*;

public class DatabaseManager {
    private Connection connection;
    private JavaPlugin plugin;

    public DatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        try {
            File dbFile = new File(plugin.getDataFolder(), "data.db");
            if (!dbFile.exists()) {
                plugin.getDataFolder().mkdirs();
            }
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);
            plugin.getLogger().info("[CappyPass] connecting to SQLite db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                plugin.getLogger().info("[CappyPass] disconnecting from SQLite db");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS players (" +
                "uuid TEXT PRIMARY KEY, " +
                "points INTEGER DEFAULT 0, " +
                "level INTEGER DEFAULT 0, " +
                "free INTEGER DEFAULT 0 " +
                ");";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPlayer(String uuid) {
        String sql = "INSERT INTO players (uuid) VALUES (?) " +
                "ON CONFLICT(uuid) DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object getPlayerAttribute(String uuid, String attribute) {
        String sql = "SELECT " +attribute+ " FROM players WHERE uuid = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getObject(attribute);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updatePlayerAttribute(String uuid, String attribute, Object value) {

        String sql = "UPDATE players SET " + attribute + " = ? WHERE uuid = ?;";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, value);
            stmt.setString(2, uuid);
            stmt.executeUpdate();
            //System.out.println("Atributo actualizado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}