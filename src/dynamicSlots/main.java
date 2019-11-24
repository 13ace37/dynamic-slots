package dynamicSlots;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {

    public void onEnable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "loaded " + ChatColor.GREEN + "successfully!");
        new playerEvent(this);
    }

}