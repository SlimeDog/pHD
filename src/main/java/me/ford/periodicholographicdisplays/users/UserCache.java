package me.ford.periodicholographicdisplays.users;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * UserManager
 */
public interface UserCache {
    public static final int MIN_NAME_MATCH = 2;

    public boolean isEmpty();

    public UUID getUuid(String name);

    public String getName(UUID id);

    public void set(UUID id, String name);

    public List<String> getNamesStartingWith(String start);

    public Map<UUID, String> getToSave();

    public void markSaved();

    public Map<UUID, String> getEntireCache();

}