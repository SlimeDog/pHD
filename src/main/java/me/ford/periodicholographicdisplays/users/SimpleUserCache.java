package me.ford.periodicholographicdisplays.users;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;

/**
 * UserCache
 */
public class SimpleUserCache implements UserCache {
    private static final String USER_CACHE_NAME = "usercache.json";
    private final IPeriodicHolographicDisplays phd;
    private final Map<UUID, String> idToName = new HashMap<>();
    private final Map<String, UUID> nameToId = new HashMap<>(); // lower case here

    public SimpleUserCache(IPeriodicHolographicDisplays phd) {
        this.phd = phd;
        populateUserCache(JsonParser.parseString(readUserCache()));
    }

    private String readUserCache() {
        StringBuilder contentBuilder = new StringBuilder();
        File wc = phd.getWorldContainer();
        String folderPath;
        if (wc != null) {
            folderPath = wc.getPath();
        } else {
            folderPath = ".";
        }
        Path path = Paths.get(folderPath, USER_CACHE_NAME);
        if (!Files.exists(path)) {
            if (!folderPath.equalsIgnoreCase(".")) { // fall back to usercache in root
                folderPath = ".";
                path = Paths.get(folderPath, USER_CACHE_NAME);
                if (!Files.exists(path)) {
                    if (phd instanceof JavaPlugin)
                        phd.getLogger().warning(USER_CACHE_NAME + " not found!");
                    return "{}";
                }
            } else {
                if (phd instanceof JavaPlugin)
                    phd.getLogger().warning(USER_CACHE_NAME + " not found!");
                return "{}";
            }
        }
        phd.getLogger().info("Usercache found: " + path.toString());
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            phd.getLogger().warning("No user cache found!");
        }
        return contentBuilder.toString();
    }

    private void populateUserCache(JsonElement json) {
        if (!json.isJsonArray()) {
            phd.getLogger().info(USER_CACHE_NAME + " was either empty or misconfigured");
            return;
        }
        JsonArray arr = json.getAsJsonArray();
        for (JsonElement el : arr) {
            String name = el.getAsJsonObject().get("name").getAsString();
            String uuid = el.getAsJsonObject().get("uuid").getAsString();
            UUID id;
            try {
                id = UUID.fromString(uuid);
            } catch (IllegalArgumentException e) {
                phd.getLogger().severe("Unable to parse UUID from " + USER_CACHE_NAME + ": " + uuid);
                continue;
            }
            addOnStartup(id, name);
        }
    }

    void addOnStartup(UUID id, String name) {
        put(id, name, true);
    }

    private void put(UUID id, String name, boolean atStartup) {
        idToName.put(id, name);
        nameToId.put(name.toLowerCase(), id);
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
    public void setAll(Map<UUID, String> map) {
        for (Entry<UUID, String> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
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
    public Map<UUID, String> getEntireCache() {
        return new HashMap<>(idToName);
    }

}