package me.ford.periodicholographicdisplays;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.ratas.slimedogcore.api.SlimeDogPlugin;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.wrap.platform.HologramPlatform;
import me.ford.periodicholographicdisplays.holograms.wrap.provider.HologramProvider;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
import me.ford.periodicholographicdisplays.hooks.NPCHook;
import me.ford.periodicholographicdisplays.users.UserCache;

/**
 * IPeriodicHolographicDisplays
 */
public interface IPeriodicHolographicDisplays extends SlimeDogPlugin {

    public HologramProvider getHologramProvider();

    public HologramPlatform getHologramPlatform();

    public File getDataFolder();

    public File getWorldContainer();

    public Logger getLogger();

    public InputStream getResource(String name);

    public void saveResource(String resourcePath, boolean replace);

    public FileConfiguration getConfig();

    public List<World> getWorlds();

    public Player getPlayer(UUID id);

    public OfflinePlayer getOfflinePlayer(UUID id);

    public JavaPlugin asPlugin();

    public UserCache getUserCache();

    public NPCHook getNPCHook();

    public LuckPermsHook getLuckPermsHook();

    public List<ReloadIssue> reload();

    public List<ReloadIssue> reloadMyConfig();

    public HologramStorage getHolograms();

    public Settings getSettings();

    public Messages getMessages();

    public void debug(String message);
}