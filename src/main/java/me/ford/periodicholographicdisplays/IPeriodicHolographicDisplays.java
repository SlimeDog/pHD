package me.ford.periodicholographicdisplays;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitTask;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.Settings.SettingIssue;
import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.hooks.LuckPermsHook;
import me.ford.periodicholographicdisplays.hooks.NPCHook;
import me.ford.periodicholographicdisplays.users.UserStorage;

/**
 * IPeriodicHolographicDisplays
 */
public interface IPeriodicHolographicDisplays {

    public File getDataFolder();

    public Logger getLogger();

    public InputStream getResource(String name);

    public void saveResource(String resourcePath, boolean replace);

    public FileConfiguration getConfig();

    public UserStorage getUserStorage();

    public NPCHook getNPCHook();

    public LuckPermsHook getLuckPermsHook();

    public List<ReloadIssue> reload();

    public Map<SettingIssue, String> reloadMyConfig();

    public HologramStorage getHolograms();

    public Settings getSettings();

    public Messages getMessages();

    public void debug(String message);

    public BukkitTask runTask(Runnable runnable);

    public BukkitTask runTaskLater(Runnable runnable, long delay);

    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period);

    public BukkitTask runTaskAsynchronously(Runnable runnable);

    public BukkitTask runTaskLaterAsynchronously(Runnable runnable, long delay);

    public BukkitTask runTaskTimerAsynchronously(Runnable runnable, long delay, long period);

}