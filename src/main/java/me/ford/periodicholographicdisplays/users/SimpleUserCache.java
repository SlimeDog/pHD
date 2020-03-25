package me.ford.periodicholographicdisplays.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.util.StringUtil;

/**
 * UserCache
 */
public class SimpleUserCache implements UserCache {
    private final Map<UUID, String> idToName = new HashMap<>();
    private final Map<String, UUID> nameToId = new HashMap<>();
    private final Map<UUID, String> toSave = new HashMap<>();

    void addOnStartup(UUID id, String name) {
        put(id, name, true);
    }

    private void put(UUID id, String name, boolean atStartup) {
        idToName.put(id, name);
        nameToId.put(name, id);
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
        return nameToId.get(name);
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
        if (start.length() < MIN_NAME_MATCH) return list; 
        return StringUtil.copyPartialMatches(start, nameToId.keySet(), list);
    }

    @Override
    public Map<UUID, String> getToSave() {
        return new HashMap<>(toSave);
    }

    @Override
    public void markSaved() {
        toSave.clear();
    }
    
}