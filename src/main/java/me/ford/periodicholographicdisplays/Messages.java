package me.ford.periodicholographicdisplays;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.holograms.IRLTimeHologram;
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

    public String getNegativeSecondsMessage(String given) {
        return getMessage("seconds-cannot-be-negative", "Seconds cannot be negative: {seconds}")
                        .replace("{seconds}", given);
    }

    public String getNegativeDistanceMessage(String given) {
        return getMessage("distance-cannot-be-negative", "Distance cannot be negative: {distance}")
                        .replace("{distance", given);
    }

    public String getNothingToUnsetMessage() {
        return getMessage("nothing-to-unset", "Did not find a suitable option, nothing was unset");
    }

    public String getCannotUnSetRequiredMessage(String option, PeriodicType type) {
        return getMessage("cannot-unset-required-option", "Cannot unset required option {option} for type {type}. Use /phd unmanage instead")
                        .replace("{option}", option).replace("{type}", type.name());
    }

    public String getAlreadyHasDataMessage(String type) {
        return getMessage("new-storage-has-data", "The new storage type {type} already has data. Use '--force' if you wish to override that data")
                        .replace("{type}", type);
    }

    public String getStartedConvertingMessage(String from, String to) {
        return getMessage("started-converting", "Started converting from {from} to {to}")
                        .replace("{from}", from).replace("{to}", to);
    }

    public String getDoneConvertingMessage(String from, String to) {
        return getMessage("done-converting", "Done converting from {from} to {to}")
                        .replace("{from}", from).replace("{to}", to);
    }

    public String getUnrecognizedStorageTypeMessage(String from, String to) {
        return getMessage("unrecognized-conversion", "Cannot convert from {from} to {to} - unrecognized storage types")
                        .replace("{from}", from).replace("{to}", to);
    }

    public String getCannotConvertSameMessage(String storageType) {
        return getMessage("cannot-convert-from-same", "Cannot convert from the same storage type ({type})")
                        .replace("{type}", storageType);
    }

    public String getHologramNotTrackedMessage(String name, PeriodicType type) {
        return getMessage("hologram-not-managed", "Hologram {name} of type {type} is not managed by pHD")
                        .replace("{name}", name).replace("{type}", type.name());
    }

    public String getHologramAlreadyManagedMessage(String name, PeriodicType type) {
        return getMessage("hologram-already-managed", "Hologram {name} of type {type} is already managed by pHD")
                        .replace("{name}", name).replace("{type}", type.name());
    }

    public String getStartedManagingMessage(String name, PeriodicType type, Map<String, String> options) {
        List<String> opts = new ArrayList<>();
        for (Entry<String, String> entry : options.entrySet()) {
            opts.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        return getMessage("started-managing-hologram", "Started managing hologram {name} of type {type}: {options}")
                        .replace("{name}", name).replace("{type}", type.name()).replace("{options}", String.join(", ", opts));
    }

    public String getHDHologramNotFoundMessage(String name) {
        return getMessage("hd-hologram-not-found", "HolographicDisplays hologram {name} was not found")
                        .replace("{name}", name);
    }

    public String getHologramNotManagedMessage(String name) {
        return getMessage("hologram-not-managed", "Hologram not managed: {name}")
                        .replace("{name}", name);
    }

    public String getHologramNotFoundMessage(String name, PeriodicType type) {
        return getMessage("hologram-not-found", "Hologram not found: {name} of type {type}")
                        .replace("{name}", name).replace("{type}", type.name());
    }

	public String getTypeNotRecognizedMessage(String type) {
		return getMessage("type-not-recognized", "Hologram type {type} is not recognized").replace("{type}", type);
	}

    public String getNeedAnIntegerMessage(String msg) {
        return getMessage("need-an-integer", "Value must be an integer, got {msg}").replace("{msg}", msg);
    }

    public String getNeedANumberMessage(String msg) {
        return getMessage("need-a-number", "Expected a number, got {msg}").replace("{msg}", msg);
    }

    public String getIllegalTimeMessage(String msg) {
        return getMessage("illega-time", "Time must be specified as e.g '1d' or '10h30m'. Available units: y, mo, d, h, m, s. Got {msg} instead")
                        .replace("{msg}", msg);
    }

    public String getHologramListMessage(Map<String, String> holograms) {
        List<String> lines = new ArrayList<>();
        for (Entry<String, String> entry : holograms.entrySet()) {
            lines.add(entry.getKey() + " " + entry.getValue());
        }
        return getMessage("hologram-list", "Holograms: \n{holograms}").replace("{holograms}", String.join("\n", lines));
    }

    public String getUnmanagedHologramMessage(String name, PeriodicType type) {
        return getMessage("unmanaged-hologram", "Unmanaged hologram {name} of type {type}")
                        .replace("{name}", name).replace("{type}", type.name());
    }

    public String getNoSuchOptionMessage(PeriodicType type, String option) {
        return getMessage("no-such-option", "{type} holograms have no {option} option")
                        .replace("{type}", type.name()).replace("{option}", option);
    }

    public String getNeedPairedOptionsMessage() {
        return getMessage("incorrect-set-options", "Need a set of key-value pairs to set (got an odd number of arguments)");
    }

    public String getOptionMissingMessage(PeriodicType type, String option) {
        return getMessage("option-missing", "Need to set {option} for a {type} hologram")
                        .replace("{type}", type.name()).replace("{option}", option);
    }

    public String getIncorrectTimeMessage(String msg) {
        return getMessage("incorrect-time", "Unable to parse time {time}").replace("{time}", msg);
    }

    public String getNeedCountAfterPlayercount() {
        return getMessage("need-player-after-playercount", "Need to specify a player after 'playercount'");
    }

    public String getPlayerNotFoundMessage(String name) {
        return getMessage("player-not-found", "Player not found: {player}").replace("{player}", name);
    }

    public String getUnsetOptionsMessage(List<String> opts) {
        return getMessage("unset-options", "Set the options to default: {options}").replace("{options}", String.join(", ", opts));
    }

    public String getConfigReloadedMessage() {
        return getMessage("config-reloaded", "Succesfully reloaded the config");
    }

    public String getProblemsReloadingConfigMessage(List<ReloadIssue> issues) {
        StringBuilder problems = new StringBuilder();
        for (ReloadIssue issue : issues) {
            String strIssue = issue.getExtra() == null ? issue.getIssue() : String.format("%s: %s", issue.getIssue(), issue.getExtra());
            problems.append(strIssue);
        }
        return getMessage("problems-reloading-config", "Problems reloading config: {problems}")
                        .replace("{problems}", problems.toString());
    }

    public String getSetNewOptionsMessage(String name, PeriodicType type, Map<String, String> options) {
        List<String> opts = new ArrayList<>();
        for (Entry<String, String> entry : options.entrySet()) {
            opts.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
        }
        return getMessage("set-new-values", "Set new values for hologram {name} of type {type}: {options}")
                        .replace("{name}", name).replace("{type}", type.name()).replace("{options}", String.join(", ", opts));
    }

    public String getHologramInfoMessage(PeriodicHologramBase hologram) {
        String typeinfo = getTypeInfo(hologram);
        String typeName = (hologram.getType() == PeriodicType.NTIMES && ((NTimesHologram) hologram).getTimesToShow() < 0) ? PeriodicType.ALWAYS.name() : hologram.getType().name();
        return getMessage("hologram-info", "Hologram '{name}':\nWorld: {world}\nType:{type}\nShowTime:{time}s\nActivationDistance:{distance}\nPermission:{perms}\nTypeInfo: {typeinfo}")
                        .replace("{name}", hologram.getName()).replace("{world}", hologram.getLocation().getWorld().getName())
                        .replace("{type}", typeName).replace("{time}", String.valueOf(hologram.getShowTimeTicks()/20))
                        .replace("{typeinfo}", typeinfo).replace("{distance}", String.format("%3.2f", hologram.getActivationDistance()))
                        .replace("{perms}", hologram.hasPermissions() ? hologram.getPermissions() : "");
    }

    public String getTypeInfo(PeriodicHologramBase hologram) {
        String typeinfo;
        switch(hologram.getType()) {
            case MCTIME:
            typeinfo = getMCTimeTypeInfo((MCTimeHologram) hologram);
            break;
            case IRLTIME:
            typeinfo = getIRLTimeTypeInfo((IRLTimeHologram) hologram);
            break;
            case NTIMES:
            NTimesHologram nth = (NTimesHologram) hologram;
            typeinfo = getNTimesTypeInfo(nth, nth.getTimesToShow() < 0);
            break;
            case ALWAYS:
            typeinfo = getNTimesTypeInfo((NTimesHologram) hologram, true);
            break;
            default:
            typeinfo = "N/A"; // this shouldn't happen!
            phd.getLogger().warning("Unable to get info for hologram of type " + hologram.getType() + " - " + hologram);
        }
        return typeinfo;
    }

    private String getIRLTimeTypeInfo(IRLTimeHologram hologram) {
        return getMessage("typeinfo.IRLTIME", "Shown at: {time}").replace("{time}", TimeUtils.toIRLTime(hologram.getTime()));
    }

    private String getMCTimeTypeInfo(MCTimeHologram hologram) {
        return getMessage("typeinfo.MCTIME", "Shown at: {time}").replace("{time}", TimeUtils.toMCTime(hologram.getTime()));
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