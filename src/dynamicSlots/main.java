package dynamicSlots;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "loaded " + ChatColor.GREEN + "successfully!");

        config.addDefault("dynamicSlotsSize", 1);
        config.options().copyDefaults(true);
        saveConfig();

        new playerEvent(this);
    }

}