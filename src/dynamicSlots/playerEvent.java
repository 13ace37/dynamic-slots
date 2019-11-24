package dynamicSlots;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class playerEvent implements Listener {
    private main plugin;

    public playerEvent(main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws ReflectiveOperationException {
        changeSlots(plugin.getServer().getOnlinePlayers().size() + 1);
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "Slots set to " + plugin.getServer().getMaxPlayers());
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) throws ReflectiveOperationException {
        changeSlots(plugin.getServer().getOnlinePlayers().size());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "Slots set to " + plugin.getServer().getMaxPlayers());
    }

    private void changeSlots(int slots) throws ReflectiveOperationException {
        Method serverGetHandle = plugin.getServer().getClass().getDeclaredMethod("getHandle");

        Object playerList = serverGetHandle.invoke(plugin.getServer());
        Field maxPlayersField = playerList.getClass().getSuperclass().getDeclaredField("maxPlayers");

        maxPlayersField.setAccessible(true);
        maxPlayersField.set(playerList, slots);
    }
}

