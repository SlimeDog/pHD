package me.ford.periodicholographicdisplays.users;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * UserManager
 */
public interface UserStorage {

    public UserCache getCache();

    public void save(boolean inSync);

    public default void populate() {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            getCache().set(player.getUniqueId(), player.getName());
        }
        save(false);
    }

}