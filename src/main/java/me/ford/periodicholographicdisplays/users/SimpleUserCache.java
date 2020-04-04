package me.ford.periodicholographicdisplays.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;

/**
 * UserCache
 */
public class SimpleUserCache implements UserCache {
    private final Map<UUID, String> idToName = new HashMap<>();
    private final Map<String, UUID> nameToId = new HashMap<>(); // lower case here
    private final Map<UUID, String> toSave = new HashMap<>();
    PeriodicHolographicDisplays phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);

    void addOnStartup(UUID id, String name) {
        put(id, name, true);
    }

    private void put(UUID id, String name, boolean atStartup) {
        idToName.put(id, name);
        nameToId.put(name.toLowerCase(), id);
        phd.debug(String.format("Putting to cache: %s -> %s %s", id.toString(), name, atStartup ? ":" : "(and marking this as needing saved)"));
        if (!atStartup) {
            toSave.put(id, name);
        }
    }

    @Override
    public boolean isEmpty() {
        return idToName.isEmpty();
    }

    @Override
    public UUID getUuid(String name) {
        return nameToId.get(name.toLowerCase());
    }

    @Override
    public String getName(UUID id) {
        return idToName.get(id);
    }

    @Override
    public void set(UUID id, String name) {
        put(id, name, false);
    }

    @Override
    public List<String> getNamesStartingWith(String start) {
        List<String> list = new ArrayList<>();
        if (start.length() < MIN_NAME_MATCH)
            return list;
        return StringUtil.copyPartialMatches(start, idToName.values(), list);
    }

    @Override
    public Map<UUID, String> getToSave() {
        return new HashMap<>(toSave);
    }

    @Override
    public void markSaved() {
        phd.debug("Marking UserCache as saved (i.e clearing the ones that previously needed saving). Had:" + toSave);
        toSave.clear();
    }

    public Map<UUID, String> getEntireCache() {
        return new HashMap<>(idToName);
    }

}