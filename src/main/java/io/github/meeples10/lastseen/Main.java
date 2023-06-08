package io.github.meeples10.lastseen;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener {
    public static final String NAME = "LastSeen";
    public static final Map<String, PlayerData> CACHE = new HashMap<>();
    private static File df, cfg, playersFile;
    static String playersOnlyMessage;
    static String noPermissionMessage;
    static String playerNotFoundMessage;
    static String playerOnlineMessage;
    static String lastSeenMessage;
    static String playerLocationMessage;
    static DateFormat dateFormat;

    @Override
    public void onEnable() {
        df = Bukkit.getServer().getPluginManager().getPlugin(NAME).getDataFolder();
        cfg = new File(df, "config.yml");
        playersFile = new File(df, "players.yml");
        this.getCommand("seen").setExecutor(new CommandSeen());
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin(NAME));
        loadConfig();
    }

    @Override
    public void onDisable() {
        try {
            save();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        CACHE.put(e.getPlayer().getUniqueId().toString(), PlayerData.of(e.getPlayer()));
    }

    public static boolean loadConfig() {
        try {
            if(!df.exists()) {
                df.mkdirs();
            }
            if(!cfg.exists()) {
                Bukkit.getPluginManager().getPlugin(NAME).saveDefaultConfig();
            }
            FileConfiguration c = YamlConfiguration.loadConfiguration(cfg);
            dateFormat = new SimpleDateFormat(c.getString("date-format"));
            ConfigurationSection messages = c.getConfigurationSection("messages");
            noPermissionMessage = ChatColor.translateAlternateColorCodes('&',
                    messages.getString("no-permission", "You do not have permission to use this command."));
            playerNotFoundMessage = ChatColor.translateAlternateColorCodes('&',
                    messages.getString("player-not-found", "Player not found: %s"));
            playerOnlineMessage = ChatColor.translateAlternateColorCodes('&',
                    messages.getString("player-online", "%s is currently online"));
            lastSeenMessage = ChatColor.translateAlternateColorCodes('&',
                    messages.getString("player-last-seen", "%s was last seen %s"));
            playerLocationMessage = ChatColor.translateAlternateColorCodes('&',
                    messages.getString("player-location", "Last location: [%.1f, %.1f, %.1f] in %s"));
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void save() throws IOException {
        FileConfiguration c = playersFile.exists() ? YamlConfiguration.loadConfiguration(playersFile)
                : new YamlConfiguration();
        for(PlayerData pd : CACHE.values()) {
            pd.save(c);
        }
        c.save(playersFile);
    }

    public static PlayerData getPlayerData(String s) {
        for(Entry<String, PlayerData> entry : CACHE.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(s) || entry.getValue().name.equalsIgnoreCase(s)) return entry.getValue();
        }
        return load(s);
    }

    private static PlayerData load(String s) {
        if(!playersFile.exists()) return null;
        FileConfiguration c = YamlConfiguration.loadConfiguration(playersFile);
        for(String key : c.getKeys(false)) {
            if(key.equalsIgnoreCase(s)) {
                return PlayerData.load(c.getConfigurationSection(key));
            } else {
                PlayerData pd = PlayerData.load(c.getConfigurationSection(key));
                if(pd.name.equalsIgnoreCase(s)) return pd;
            }
        }
        return null;
    }

    public static Player getOnlinePlayer(String s) {
        for(Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(p.getName().equalsIgnoreCase(s) || p.getUniqueId().toString().equalsIgnoreCase(s)) return p;
        }
        return null;
    }
}
