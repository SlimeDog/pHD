package me.ford.periodicholographicdisplays;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

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

}