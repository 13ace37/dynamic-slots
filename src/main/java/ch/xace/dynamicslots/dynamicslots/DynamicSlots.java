package ch.xace.dynamicslots.dynamicslots;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DynamicSlots extends JavaPlugin implements Listener {

  FileConfiguration config = getConfig();

  @Override
  public void onEnable() {
    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "loaded " + ChatColor.GREEN + "successfully!");

    Bukkit.getPluginManager().registerEvents(this, this);
    config.addDefault("dynamicSlotsSize", 1);
    config.options().copyDefaults(true);
    saveConfig();

    new PlayerEvent(this);
  }

}
