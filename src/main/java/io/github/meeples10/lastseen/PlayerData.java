package io.github.meeples10.lastseen;

import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class PlayerData {
    public final UUID uuid;
    public String name;
    public long lastSeen;
    public double x;
    public double y;
    public double z;
    public String world;

    private PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public static PlayerData load(ConfigurationSection section) {
        PlayerData pd = new PlayerData(UUID.fromString(section.getName()));
        pd.name = section.getString("name");
        pd.lastSeen = section.getLong("last-seen");
        pd.x = section.getDouble("x");
        pd.y = section.getDouble("y");
        pd.z = section.getDouble("z");
        pd.world = section.getString("world");
        return pd;
    }

    public static PlayerData of(Player p) {
        PlayerData pd = new PlayerData(p.getUniqueId());
        pd.name = p.getName();
        pd.lastSeen = System.currentTimeMillis();
        pd.x = p.getLocation().x();
        pd.y = p.getLocation().y();
        pd.z = p.getLocation().z();
        pd.world = p.getLocation().getWorld().getName();
        return pd;
    }

    public void save(FileConfiguration fc) {
        String u = uuid.toString();
        if(fc.getConfigurationSection(u) == null) {
            fc.createSection(u);
        }
        ConfigurationSection c = fc.getConfigurationSection(u);
        c.set("name", name);
        c.set("last-seen", lastSeen);
        c.set("x", x);
        c.set("y", y);
        c.set("z", z);
        c.set("world", world);
    }
}
