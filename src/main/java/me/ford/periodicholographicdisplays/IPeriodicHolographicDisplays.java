package me.ford.periodicholographicdisplays;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.Settings.SettingIssue;
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

    public UserStorage getUserStorage();

    public NPCHook getNPCHook();

    public LuckPermsHook getLuckPermsHook();

    public List<ReloadIssue> reload();

    public Map<SettingIssue, String> reloadMyConfig();

}