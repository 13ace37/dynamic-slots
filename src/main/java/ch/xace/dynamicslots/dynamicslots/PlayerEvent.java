package ch.xace.dynamicslots.dynamicslots;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class PlayerEvent implements Listener {
  private DynamicSlots dynamicSlots;
  private Field maxPlayersField;

  public PlayerEvent(DynamicSlots dynamicSlots) {
    this.dynamicSlots = dynamicSlots;
    Bukkit.getPluginManager().registerEvents(this, dynamicSlots);
    try {
      initChangeSlots();
    } catch (ReflectiveOperationException e) {}

  }

  public void initChangeSlots() throws ReflectiveOperationException {
    int Slots = dynamicSlots.getServer().getOnlinePlayers().size() + dynamicSlots.config.getInt("dynamicSlotsSize");
    changeSlots(Slots);
    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "Plugin load or reload detected. Making sure to set the slot amount.");
    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "Slots set to " + dynamicSlots.getServer().getMaxPlayers());
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) throws ReflectiveOperationException {
    int Slots = dynamicSlots.getServer().getOnlinePlayers().size() + dynamicSlots.config.getInt("dynamicSlotsSize");
    changeSlots(Slots);
    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "Slots set to " + dynamicSlots.getServer().getMaxPlayers());
  }
  @EventHandler
  public void onLeave(PlayerQuitEvent event) throws ReflectiveOperationException {
    int Slots = dynamicSlots.getServer().getOnlinePlayers().size() + (dynamicSlots.config.getInt("dynamicSlotsSize") - 1);
    changeSlots(Slots);
    Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[Dynamic Slots] " + ChatColor.GRAY + "Slots set to " + dynamicSlots.getServer().getMaxPlayers());
  }

  /**
   * Change the max players of the Bukkit server.
   *
   * @param slots the amount of players the server should allow
   * @throws ReflectiveOperationException if an error occurs
   */
  public void changeSlots(int slots) throws ReflectiveOperationException {
    Method serverGetHandle = dynamicSlots.getServer().getClass().getDeclaredMethod("getHandle");
    Object playerList = serverGetHandle.invoke(dynamicSlots.getServer());

    if (this.maxPlayersField == null) {
      this.maxPlayersField = getMaxPlayersField(playerList);
    }

    this.maxPlayersField.setInt(playerList, slots);
  }

  private Field getMaxPlayersField(Object playerList) throws ReflectiveOperationException {
    Class<?> playerListClass = playerList.getClass().getSuperclass();

    try {
      Field field = playerListClass.getDeclaredField("maxPlayers");
      field.setAccessible(true);
      return field;
    } catch (NoSuchFieldException e) {
      for (Field field : playerListClass.getDeclaredFields()) {
        if (field.getType() != int.class) {
          continue;
        }

        field.setAccessible(true);

        if (field.getInt(playerList) == dynamicSlots.getServer().getMaxPlayers()) {
          return field;
        }
      }

      throw new NoSuchFieldException("Unable to find maxPlayers field in " + playerListClass.getName());
    }
  }

  private void updateServerProperties() {
    Properties properties = new Properties();
    File propertiesFile = new File("server.properties");

    try {
      try (InputStream is = new FileInputStream(propertiesFile)) {
        properties.load(is);
      }

      String maxPlayers = Integer.toString(dynamicSlots.getServer().getMaxPlayers());

      if (properties.getProperty("max-players").equals(maxPlayers)) {
        return;
      }

      dynamicSlots.getLogger().info("Saving max players to server.properties...");
      properties.setProperty("max-players", maxPlayers);

      try (OutputStream os = new FileOutputStream(propertiesFile)) {
        properties.store(os, "Minecraft server properties");
      }
    } catch (IOException e) {
      dynamicSlots.getLogger().log(Level.SEVERE, "An error occurred while updating the server properties", e);
    }
  }
}
