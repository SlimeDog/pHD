package me.ford.periodicholographicdisplays;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.ford.periodicholographicdisplays.holograms.MCTimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * Messages
 */
public class Messages extends CustomConfigHandler {
    private final static String FILE_NAME = "messages.yml";
    private final PeriodicHolographicDisplays phd;

    public Messages(PeriodicHolographicDisplays phd) {
        super(phd, FILE_NAME);
        saveDefaultConfig();
        this.phd = phd;
    }

    public String getHDHologramNotFoundMessage(String name) {
        return getMessage("hd-hologram-not-found", "HolographicDisplays hologram by the name of {name} was not found")
                        .replace("{name}", name);
    }

    public String getHologramNotFoundMessage(String name, PeriodicType type) {
        return getMessage("hologram-not-found", "Hologram not found: {name} of type {type}")
                        .replace("{name}", name).replace("{type}", type.name());
    }

	public String getTypeNotRecognizedMessage(String type) {
		return getMessage("type-not-recognized", "This type for a periodic hologram was not recognized: {type}").replace("{type}", type);
	}

    public String getNeedANumberMessage(String msg) {
        return getMessage("need-a-number", "Expected a number, got {msg}").replace("{msg}", msg);
    }

    public String getIllegalTimeMessage(String msg) {
        return getMessage("illega-time", "Time must be specified as e.g '1d' or '10h30m'. Available units: y, mo, d, h, m, s. Got {msg} instead")
                        .replace("{msg}", msg);
    }

    public String getNeedAPlayerMessage() {
        return getMessage("need-a-player", "Only a player can use this command!");
    }

    public String getWorldNotFoundMessage(String name) {
        return getMessage("world-not-found", "World not found: {name}").replace("{name}", name);
    }

    public String getHologramListMessage(List<String> names) {
        return getMessage("hologram-list", "Holograms: {holograms}").replace("{holograms}", String.join(", ", names));
    }

    public String getHologramInfoMessage(PeriodicHologramBase hologram) {
        String typeinfo;
        switch(hologram.getType()) {
            case MCTIME:
            typeinfo = getMCTimeTypeInfo((MCTimeHologram) hologram);
            break;
            case NTIMES:
            typeinfo = getNTimesTypeInfo((NTimesHologram) hologram);
            break;
            case ALWAYS:
            typeinfo = getNTimesTypeInfo((NTimesHologram) hologram, true);
            break;
            default:
            typeinfo = "N/A"; // this shouldn't happen!
            phd.getLogger().warning("Unable to get info for hologram of type " + hologram.getType() + " - " + hologram);
        }
        return getMessage("hologram-info", "A hologram named '{name}':\nWorld: {world}\nType:{type}\nShowTime:{time}s\nActivationDistnace:{distance}\nPermission:{perms}\nTypeInfo:{typeinfo}")
                        .replace("{name}", hologram.getName()).replace("{world}", hologram.getLocation().getWorld().getName())
                        .replace("{type}", hologram.getType().name()).replace("{time}", String.valueOf(hologram.getShowTimeTicks()/20))
                        .replace("{typeinfo}", typeinfo).replace("{distance}", String.format("%3.2f", hologram.getActivationDistance()))
                        .replace("{perms}", hologram.hasPermissions() ? hologram.getPermissions() : "");
    }

    private String getMCTimeTypeInfo(MCTimeHologram hologram) {
        return getMessage("MCTIME", "Shown at: {time}").replace("{time}", TimeUtils.toMCTime(hologram.getTime()));
    }

    public String getNTimesTypeInfo(NTimesHologram hologram) {
        return getNTimesTypeInfo(hologram, false);
    }

    public String getNTimesTypeInfo(NTimesHologram hologram, boolean always) {
        String msg = getMessage(always?"typeinfo.ALWAYS":"typeinfo.NTIMES", "Shown to: {players:times}");
        if (msg.contains("{players:times}")) {
            List<String> playersAndTimes = new ArrayList<>();
            for (Entry<UUID, Integer> entry : hologram.getShownTo().entrySet()) {
                OfflinePlayer player = phd.getServer().getOfflinePlayer(entry.getKey());
                String playerName = (player == null || !player.hasPlayedBefore()) ? "UNKOWNPLAYER" : player.getName();
                playersAndTimes.add(playerName + ": " + entry.getValue());
            }
            msg = msg.replace("{players:times}", String.join(", ", playersAndTimes));
        }
        return msg;
    }

    public String getMessage(String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', (getCustomConfig().getString(path, def)));
    }
    
}