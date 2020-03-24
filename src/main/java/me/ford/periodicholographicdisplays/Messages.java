package me.ford.periodicholographicdisplays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.DefaultReloadIssue;
import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.Settings.SettingIssue;
import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.IRLTimeHologram;
import me.ford.periodicholographicdisplays.holograms.MCTimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.util.PageUtils;
import me.ford.periodicholographicdisplays.util.TimeUtils;
import me.ford.periodicholographicdisplays.util.PageUtils.PageInfo;

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

    public String getUnrecognizedCommandMessage(String input) {
        return getMessage("unrecognized-command", "Unrecognized command; try /phd help");
    }

    public String getFlashTimeTooSmallMessage(String specified) {
        return getMessage("flash-time-too-small", "Minimum flash time is 1.0, got {time}")
                        .replace("{time}", specified);
    }

    public String getFlashMustHaveBothMessage(String specified) {
        return getMessage("flash-must-have-both", "Need to specify both 'flashOn' and 'flashOff'; alternatively use 'flash' to set both at once");
    }

    public String getActiveStorageMessage(boolean useDatabase) {
        return getMessage("active-storage", "Active storage-type is {storage}")
                        .replace("{storage}", useDatabase ? "SQLITE" : "YAML");
    }

    public String getIncorrectMessages() {
        return getMessage("incorrect-messages", "Messages.yml is incorrectly formatted");
    }

    public String getDisablingMessage() {
        return getMessage("disabling-plugin", "Disabling plugin!");
    }

    public String getNextPageHint(String command) {
        return getMessage("next-page-hint", "TIP: See the next page with {command}").replace("{command}", command);
    }

    public String getInvalidPageMessage(int maxPage) {
        return getMessage("invalid-page", "Page needs to be between 1 and {max-page}")
                        .replace("{max-page}", String.valueOf(maxPage));
    }

    public String getAvailableTypesMessage(String name, Collection<PeriodicType> availableTypes) {
        String msg = getMessage("available-types", "Types available for {hologram}: {types}");
        msg = msg.replace("{hologram}", name);
        List<String> types = new ArrayList<>();
        for (PeriodicType type : availableTypes) types.add(type.name());
        return msg.replace("{types}", String.join(", ", types));
    }

    public String getConfigRecreatedMessage() {
        return getMessage("config-recreated", "The config.yml file did not exist and was recreated");
    }

    public String getMessagesRecreatedMessage() {
        return getMessage("messages-recreated", "The messages.yml file did not exist and was recreated");
    }

    public String getPluginFolderRecreatedMessage() {
        return getMessage("plugin-folder-recreated", "Plugin folder was recreated");
    }

    public String getProblemRecreatingPluginFolder() {
        return getMessage("problem-recreated-plugin-folder", "Problem recreating plugin folder");
    }

    public String getNoPluginFolderMessage() {
        return getMessage("plugin-folder-removed", "Plugin folder does not exist or is unreadable at reload; attempting to recreate");
    }

    public String getNoLPMessage() {
        return getMessage("no-luckperms", "LuckPerms not found - unable to readjust permissions on the fly or tab-complete permissions");
    }

    public String getLegacyMessage() {
        return getMessage("legacy-version-mctime", "MCTIME holograms can behave unpredicably because of the use of a legacy version of MC");
    }

    public String getIllegalStorageMessage(String type) {
        return getMessage("illegal-storage-type", "Illegal storage type {type} - only YAML and SQLITE are supported")
                        .replace("{type}", type);
    }

    public String getNegativeSecondsMessage(String given) {
        return getMessage("seconds-cannot-be-negative", "Seconds cannot be negative: {seconds}")
                        .replace("{seconds}", given);
    }

    public String getNegativeDistanceMessage(String given) {
        return getMessage("distance-cannot-be-negative", "Distance cannot be negative: {distance}")
                        .replace("{distance}", given);
    }

    public String getNothingToUnsetMessage() {
        return getMessage("nothing-to-unset", "Did not find a suitable option, nothing was unset");
    }

    public String getCannotUnSetRequiredMessage(String option, PeriodicType type) {
        return getMessage("cannot-unset-required-option", "Cannot unset required option {option} for type {type}; use /phd unmanage instead")
                        .replace("{option}", option).replace("{type}", type.name());
    }

    public String getStorageTypeDoesNotExistMessage(String type) {
        return getMessage("source-storage-does-not-exist", "{type} source storage type does not exist; no data will be converted")
                        .replace("{type}", type);
    }

    public String getAlreadyHasDataMessage(String type) {
        return getMessage("target-storage-has-data", "The target storage type {type} already has data; use --force if you wish to override that data")
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

    public String getHologramListMessage(Map<String, String> holograms, int page) {
        List<String> lines = new ArrayList<>();
        PageInfo pageInfo = PageUtils.getPageInfo(holograms.size(), PageUtils.HOLOGRAMS_PER_PAGE, page);
        int i = 1;
        for (Entry<String, String> entry : holograms.entrySet()) {
            if (i < pageInfo.getStartNumber() || i > pageInfo.getEndNumber()) {
                i++;
                continue;
            }
            lines.add(entry.getKey() + " " + entry.getValue());
            i++;
        }
        String msg = getMessage("hologram-list", "Holograms (holograms {numbers}, page {page}/{max-pages}): \n{holograms}");
        int startNr = pageInfo.getStartNumber();
        int endNr = pageInfo.getEndNumber();
        String numbers;
        if (endNr <= startNr && startNr == 1) {
            numbers = String.valueOf(endNr);
        } else {
            numbers = String.format("%d-%d", startNr, endNr);
        }
        msg = msg.replace("{numbers}", numbers);
        msg = msg.replace("{page}", String.valueOf(page)).replace("{max-pages}", String.valueOf(pageInfo.getNumberOfPages()));
        return msg.replace("{holograms}", String.join("\n", lines));
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
        return getMessage("incorrect-set-options", "Need a set of key-value pairs to set, got an odd number of arguments");
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

    public String getUnsetFlashMessage() {
        return getMessage("unset-flash", "Unset flash");
    }

    public String getUnsetPlayerCountMessage(OfflinePlayer player) {
        return getMessage("unset-playercount", "Unset playercount of {player}; now 0")
                        .replace("{player}", player.getName());
    }

    public String getUnsetOptionsMessage(List<String> opts) {
        return getMessage("unset-options", "Unset {options}; now using default").replace("{options}", String.join(", ", opts));
    }

    public String getProblemWithConfigMessage(SettingIssue issue, String value) {
        return getMessage("problem-in-config", "Problem in config for {key}; expected {type}, got {value}")
                        .replace("{key}", issue.getPath()).replace("{type}", issue.getType().getName())
                        .replace("{value}", value);
    }

    public String getConfigReloadedMessage() {
        return getMessage("config-reloaded", "Successfully reloaded configuration, messages, and data");
    }

    public String getSqlConnectionMessage() {
        return getMessage("sqlite-connection-established", "Connection to SQLite has been established");
    }

    public String getProblemsReloadingConfigMessage(List<ReloadIssue> issues) {
        StringBuilder problems = new StringBuilder();
        for (ReloadIssue issue : issues) {
            String desc = null;
            problems.append("\n");
            if (issue instanceof DefaultReloadIssue) {
                DefaultReloadIssue dri = (DefaultReloadIssue) issue;
                switch (dri) {
                    case NO_FOLDER:
                    desc = getNoPluginFolderMessage();
                    problems.append(desc);
                    problems.append("\n");
                    desc = Boolean.valueOf(dri.getExtra()) ? getPluginFolderRecreatedMessage() : getProblemRecreatingPluginFolder();
                    break;
                    case ILLEGA_STORAGE_TYPE:
                    desc = getIllegalStorageMessage(dri.getExtra());
                    break;
                    case NO_CONFIG:
                    desc = getConfigRecreatedMessage();
                    break;
                    case NO_MESSAGES:
                    desc = getMessagesRecreatedMessage();
                    break;
                    default:
                }
            }
            if (desc == null) {
                desc = issue.getExtra() == null ? issue.getIssue() : String.format("%s: %s", issue.getIssue(), issue.getExtra());;
            }
            problems.append(desc);
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

    private String timesString = "%s %d/%d";

    public String getNtimesReportMessage(OfflinePlayer player, List<NTimesHologram> holos) {
        org.bukkit.Bukkit.getLogger().info("CUSTOM!!");
        String msg = getMessage("ntimes-report", "{player} has seen the following NTIMES holograms:\n{times}");
        msg = msg.replace("{player}", player.getName());
        StringBuilder builder = new StringBuilder();
        for (NTimesHologram hologram : holos) {
            Integer amount = hologram.getShownTo().get(player.getUniqueId());
            if (amount == null) continue; // skip ones that haven't been shown
            if (builder.length() != 0) builder.append("\n");
            builder.append(String.format(timesString, hologram.getName(), amount, hologram.getTimesToShow()));
        }
        if (builder.length() == 0) {
            builder.append("None");
        }
        return msg.replace("{times}", builder.toString());
    }

    public String getHologramInfoMessage(FlashingHologram hologram, int page) {
        String typeinfo = getTypeInfo(hologram, page);
        String typeName = (hologram.getType() == PeriodicType.NTIMES && ((NTimesHologram) hologram).getTimesToShow() < 0) ? PeriodicType.ALWAYS.name() : hologram.getType().name();
        String time = getShowTimeString(hologram);
        String distance = getDistanceString(hologram);
        String flash;
        if (hologram.flashes()) {
            flash = String.format("%3.2f/%3.2f", hologram.getFlashOn(), hologram.getFlashOff());
        } else {
            flash = "None";
        }
        return getMessage("hologram-info", "Hologram '{name}':\nWorld: {world}\nType: {type}\nShowTime: {time}\nFlash: {flash}\nActivationDistance: {distance}\nPermission: {perms}\nTypeInfo: {typeinfo}")
                        .replace("{name}", hologram.getName()).replace("{world}", hologram.getLocation().getWorld().getName())
                        .replace("{type}", typeName).replace("{time}", time)
                        .replace("{typeinfo}", typeinfo).replace("{distance}", distance)
                        .replace("{flash}", flash)
                        .replace("{perms}", hologram.hasPermissions() ? hologram.getPermissions() : "");
    }

    private String getShowTimeString(PeriodicHologramBase hologram) {
        long showTime = hologram.getShowTime();
        boolean isSpecialAlways = hologram.getType() == PeriodicType.ALWAYS;
        if (isSpecialAlways) {
            AlwaysHologram always = (AlwaysHologram) hologram;
            isSpecialAlways = always.isShownOnWorldJoin() || always.isShownWhileInArea();
        }
        String time;
        if (isSpecialAlways) { // whichever special case -> always
            time = "Always"; // add s as in seconds
        } else {
            if (showTime == PeriodicHologramBase.NO_SECONDS) {
                time = String.valueOf(phd.getSettings().getDefaultShowTime()) + " (default)";
            } else {
                time = String.valueOf(showTime);
            }
        }
        return time;
    }

    private String getDistanceString(PeriodicHologramBase hologram) {
        double dist = hologram.getActivationDistance();
        boolean isSpecialAlways = hologram.getType() == PeriodicType.ALWAYS;
        if (isSpecialAlways) {
            AlwaysHologram always = (AlwaysHologram) hologram;
            isSpecialAlways = always.isShownOnWorldJoin(); // when shown in area, show default
        }
        String distance;
        if (isSpecialAlways) {
            distance = "InWorld";
        } else {
            if (dist == PeriodicHologramBase.NO_DISTANCE) {
                distance = String.format("%3.2f (default)", phd.getSettings().getDefaultActivationDistance());
            } else {
                distance = String.format("%3.2f", dist);
            }
        }
        return distance;
    }

    public String getTypeInfo(PeriodicHologramBase hologram, int page) {
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
            typeinfo = getNTimesTypeInfo(nth, nth.getTimesToShow() < 0, page);
            break;
            case ALWAYS:
            typeinfo = getNTimesTypeInfo((NTimesHologram) hologram, true, page);
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

    public String getNTimesTypeInfo(NTimesHologram hologram, boolean always, int page) {// need to be sure not to specify the wrong page!
        String msg = getMessage(always?"typeinfo.ALWAYS":"typeinfo.NTIMES", 
                                        always ? "Always shown" : "Show times: {times}; Shown to (players {players}, page {page}/{max-pages}): {players:times}");
        if (!always) {
            msg = msg.replace("{times}", String.valueOf(hologram.getTimesToShow()));
            final int nrOfPlayers = hologram.getShownTo().size();
            PageInfo pageInfo = PageUtils.getPageInfo(nrOfPlayers, PageUtils.PLAYERS_PER_PAGE, page);
            int startNr = pageInfo.getStartNumber();
            int endNr = pageInfo.getEndNumber();
            String numbers;
            if (endNr <= startNr && startNr == 1) {
                numbers = String.valueOf(endNr);
            } else {
                numbers = String.format("%d-%d", startNr, endNr);
            }
            msg = msg.replace("{players}", numbers);
            msg = msg.replace("{page}", String.valueOf(page)).replace("{max-pages}", String.valueOf(pageInfo.getNumberOfPages()));
            List<String> playersAndTimes = new ArrayList<>();
            int i = 1;
            for (Entry<UUID, Integer> entry : hologram.getShownTo().entrySet()) {
                if (i < pageInfo.getStartNumber() || i > pageInfo.getEndNumber()) {
                    i++;
                    continue;
                }
                OfflinePlayer player = phd.getServer().getOfflinePlayer(entry.getKey());
                String playerName = (player == null || !player.hasPlayedBefore()) ? "UNKNOWNPLAYER" : player.getName();
                playersAndTimes.add(playerName + ": " + entry.getValue());
                i++;
            }
            if (playersAndTimes.isEmpty()) {
                msg = msg.replace("{players:times}", "none");
            } else {
                msg = msg.replace("{players:times}", "\n" + String.join("\n", playersAndTimes));
            }
        }
        return msg;
    }

    public String getLowSaveDelayMessage(long seconds) {
        return getMessage("save-frequency-low", "Configuration save-frequency set to {value} seconds may result in decreased performance")
                        .replace("{value}", String.valueOf(seconds));
    }

    public String getMessage(String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', (getCustomConfig().getString(path, def)));
    }
    
}