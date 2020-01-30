package me.ford.periodicholographicdisplays;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import me.ford.periodicholographicdisplays.holograms.EverytimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.OnJoinHologram;
import me.ford.periodicholographicdisplays.holograms.OnceHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * Settings
 */
public class Settings {
    private final PeriodicHolographicDisplays pl;

    public Settings(PeriodicHolographicDisplays plugin) {
        this.pl = plugin;
    }

    public String getHologramNotFoundMessage(String name) {
        return getMessage("hologram-not-found", "Hologram not found: {name}").replace("{name}", name);
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

    public String getAdoptedPeriodicMessage(String name, long showTimeTicks, long showDelaySeconds) {
        return getMessage("adopted-periodic", "Adopted the periodic hologram '{name}'. It'll be displayed for {time}s no more often than {delay}")
                        .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0))
                        .replace("{delay}", TimeUtils.formatDateFromDiff(showDelaySeconds * 1000L));
    }

    public String getAdoptedOnceMessage(String name, long showTimeTicks) {
        return getMessage("adopted-once", "Adopted the once activated hologram '{name}'. It'll be displayed for {time}s on first encounter")
                        .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getAdoptedEveryTimeMessage(String name, long showTimeTicks) {
        return getMessage("adopted-everytime", "Adopted the hologram '{name}'. It'll be displayed for {time}s on every encounter")
                        .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getAdoptedNTimesMessage(String name, long showTimeTicks, int timesToShow) {
        return getMessage("adopted-ntimes", "Adopted the hologram '{name}'. It'll be displayed {times} to each player for {time}s")
                        .replace("{name}", name).replace("{times}", String.valueOf(timesToShow))
                        .replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getAdoptedOnJoinMessage(String name, long showTimeTicks) {
        return getMessage("adopted-onjoin", "Adopted the hologram '{name}'. It'll be displayed when a player joins and gets into range for {time}s")
                        .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getNeedAPlayerMessage() {
        return getMessage("need-a-player", "Only a player can use this command!");
    }

    public String getCreatedPeriodicMessage(String name, long showTimeTicks, long showDelaySeconds) {
        return getMessage("created-periodic", "Created the periodic hologram '{name}'. It'll be displayed for {time}s no more often than {delay}")
                    .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0))
                    .replace("{delay}", TimeUtils.formatDateFromDiff(showDelaySeconds * 1000L));
    }

    public String getCreatedOnceMessage(String name, long showTimeTicks) {
        return getMessage("created-once", "Created the once activated hologram '{name}'. It'll be displayed for {time}s on first encounter")
                        .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getCreatedEveryTimeMessage(String name, long showTimeTicks) {
        return getMessage("created-everytime", "Created the hologram '{name}'. It'll be displayed for {time}s on every encounter")
                        .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getCreatedNTimes(String name, long showTimeTicks, int timesToShow) {
        return getMessage("created-ntimes", "Created the hologram '{name}'. It'll be displayed {times} to each player for {time}s")
                        .replace("{name}", name).replace("{times}", String.valueOf(timesToShow))
                        .replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getCreatedOnJoinMessage(String name, long showTimeTicks) {
        return getMessage("created-onjoin", "Created the hologram '{name}'. It'll be displayed when a player joins and gets into range for {time}s")
                        .replace("{name}", name).replace("{time}", String.format("%4.2f", showTimeTicks/20.0));
    }

    public String getWorldNotFoundMessage(String name) {
        return getMessage("world-not-found", "World not found: {name}").replace("{name}", name);
    }

    public String getHologramListMessage(World world, List<String> names) {
        return getMessage("hologram-list", "Holograms in {world}: {holograms}")
                        .replace("{world}", world.getName()).replace("{holograms}", String.join(", ", names));
    }

    public String getHologramInfoMessage(PeriodicHologramBase hologram) {
        String typeinfo;
        switch(hologram.getType()) {
            case PERIODIC:
            typeinfo = getPeriodicTypeInfo((PeriodicHologram) hologram);
            break;
            case ONCE:
            typeinfo = getOnceTypeInfo((OnceHologram) hologram);
            break;
            case NTIMES:
            typeinfo = getNTimesTypeInfo((NTimesHologram) hologram);
            break;
            case ONJOIN:
            typeinfo = getOnJoinTypeInfo((OnJoinHologram) hologram);
            break;
            case EVERYTIME:
            typeinfo = getEverytimeTypeInfo((EverytimeHologram) hologram);
            break;
            default:
            typeinfo = "N/A"; // this shouldn't happen!
        }
        return getMessage("hologram-info", "A hologram named '{name}':\nWorld: {world}\nType:{type}\nShowTime:{time}s\nTypeInfo:{typeinfo}")
                        .replace("{name}", hologram.getName()).replace("{world}", hologram.getLocation().getWorld().getName())
                        .replace("{type}", hologram.getType().name()).replace("{time}", String.valueOf(hologram.getShowTimeTicks()/20))
                        .replace("{typeinfo}", typeinfo);
    }

    public String getOnJoinTypeInfo(OnJoinHologram hologram) {
        String msg = getMessage("typeinfo.ONJOIN", "Shown to: {players}");
        if (msg.contains("{players}")) {
            List<String> players = new ArrayList<>();
            for (UUID id : hologram.getShownTo()) {
                OfflinePlayer player = pl.getServer().getOfflinePlayer(id);
                if (player == null || !player.hasPlayedBefore()) {
                    players.add("UNKNOWNPLAYER");
                } else {
                    players.add(player.getName());
                }
            }
            msg = msg.replace("{players}", String.join(", ", players));
        }
        return msg;
    }

    public String getNTimesTypeInfo(NTimesHologram hologram) {
        String msg = getMessage("typeinfo.NTIMES", "Shown to: {players:times}");
        if (msg.contains("{players:times}")) {
            List<String> playersAndTimes = new ArrayList<>();
            for (Entry<UUID, Integer> entry : hologram.getShownTo().entrySet()) {
                OfflinePlayer player = pl.getServer().getOfflinePlayer(entry.getKey());
                String playerName = (player == null || !player.hasPlayedBefore()) ? "UNKOWNPLAYER" : player.getName();
                playersAndTimes.add(playerName + ": " + entry.getValue());
            }
            msg = msg.replace("{players:times}", String.join(", ", playersAndTimes));
        }
        return msg;
    }
    
    public String getPeriodicTypeInfo(PeriodicHologram hologram) {
        String msg = getMessage("typeinfo.PERIODIC", "{delay} delay, on cooldown: {players}");
        msg = msg.replace("{delay}", TimeUtils.formatDateFromDiff(((PeriodicHologram) hologram).getShowDelay()));
        if (msg.contains("{players}")) {
            List<String> players = new ArrayList<>();
            for (Entry<UUID, Long> entry : hologram.getLastShown().entrySet()) {
                if (entry.getValue() + hologram.getShowDelay() > System.currentTimeMillis()) {
                    OfflinePlayer player = pl.getServer().getOfflinePlayer(entry.getKey());
                    if (player == null || !player.hasPlayedBefore()) {
                        players.add("UNKNOWNPLAYER");
                    } else {
                        players.add(player.getName());
                    }
                }
            }
            msg = msg.replace("{players}", String.join(", ", players));
        }
        return msg;
    }
    public String getOnceTypeInfo(OnceHologram hologram) {
        String msg = getMessage("typeinfo.ONCE", "Shown to: {players}");
        if (msg.contains("{players}")) {
            List<String> players = new ArrayList<>();
            for (UUID id : hologram.getShownTo()) {
                OfflinePlayer player = pl.getServer().getOfflinePlayer(id);
                if (player == null || !player.hasPlayedBefore()) {
                    players.add("UNKNOWNPLAYER");
                } else {
                    players.add(player.getName());
                }
            }
            msg = msg.replace("{players}", String.join(", ", players));
        }
        return msg;
    }

    public String getEverytimeTypeInfo(EverytimeHologram hologram) {
        return getMessage("typeinfo.EVERYTIME", "N/A");
    }

    public String getMessage(String path, String def) {
        return color(pl.getConfig().getString("messages." + path, def));
    }

    public String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    
}