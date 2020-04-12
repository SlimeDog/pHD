package me.ford.periodicholographicdisplays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

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
import me.ford.periodicholographicdisplays.holograms.storage.HologramInfo;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;
import me.ford.periodicholographicdisplays.storage.yaml.CustomConfigHandler;
import me.ford.periodicholographicdisplays.util.PageUtils;
import me.ford.periodicholographicdisplays.util.TimeUtils;
import me.ford.periodicholographicdisplays.util.PageUtils.PageInfo;

/**
 * Messages
 */
public class Messages extends CustomConfigHandler {
    private final static String FILE_NAME = "messages.yml";
    private static final String DUMMY_FILE_NAME = "messages_dummy.yml";
    private final PeriodicHolographicDisplays phd;

    public Messages(PeriodicHolographicDisplays phd) throws InvalidConfigurationException {
        super(phd, FILE_NAME);
        saveDefaultConfig();
        this.phd = phd;
    }

    Messages(PeriodicHolographicDisplays phd, boolean dummy) throws InvalidConfigurationException {
        super(phd, DUMMY_FILE_NAME);
        this.phd = phd;
    }

    public String getAddedToCacheMessage(Player player) {
        return getMessage("added-to-cache", "Adding player {name} UUID {uuid} to cache")
            .replace("{name}", player.getName()).replace("{uuid}", player.getUniqueId().toString());
    }

    public String getOptionNotSetMessage(String option) {
        return getMessage("option-not-set", "No change, {option} was not set").replace("{option}", option);
    }

    public String getNegativeTimesMessage(String cur) {
        return getMessage("times-too-small", "Minimum times value is 1, got {times}").replace("{times}", cur);
    }

    public String getNeedTypeOrPageMessage(String cur) {
        return getMessage("need-to-specify-type-or-page",
                "Need to specify either a hologram type or a page number, got {msg}").replace("{msg}", cur);
    }

    public String getUnrecognizedCommandMessage(String input) {
        return getMessage("unrecognized-command", "Unrecognized command; try /phd help");
    }

    public String getFlashTimeTooSmallMessage(String specified) {
        return getMessage("flash-time-too-small", "Minimum flash time is 1.0, got {time}").replace("{time}", specified);
    }

    public String getFlashMustHaveBothMessage(String specified) {
        return getMessage("flash-must-have-both",
                "Need to specify both 'flashOn' and 'flashOff'; alternatively use 'flash' to set both at once");
    }

    public String getActiveStorageMessage(boolean useDatabase) {
        return getMessage("active-storage", "Active storage-type is {storage}").replace("{storage}",
                useDatabase ? "SQLITE" : "YAML");
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
        return getMessage("invalid-page", "Page needs to be between 1 and {max-page}").replace("{max-page}",
                String.valueOf(maxPage));
    }

    public String getAvailableTypesMessage(String name, Collection<PeriodicType> availableTypes) {
        String msg = getMessage("available-types", "Types available for {hologram}: {types}");
        msg = msg.replace("{hologram}", name);
        List<String> types = new ArrayList<>();
        for (PeriodicType type : availableTypes)
            types.add(type.name());
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
        return getMessage("plugin-folder-removed",
                "Plugin folder does not exist or is unreadable at reload; attempting to recreate");
    }

    public String getNoLPMessage() {
        return getMessage("no-luckperms",
                "LuckPerms not found - unable to readjust permissions on the fly or tab-complete permissions");
    }

    public String getLegacyMessage() {
        return getMessage("legacy-version-mctime",
                "MCTIME holograms can behave unpredicably because of the use of a legacy version of MC");
    }

    public String getIllegalStorageMessage(String type) {
        return getMessage("illegal-storage-type", "Illegal storage type {type} - only YAML and SQLITE are supported")
                .replace("{type}", type);
    }

    public String getSecondsTooSmallMessage(String given) {
        return getMessage("seconds-too-small", "Minimum seconds value is 1, got {seconds}").replace("{seconds}",
                given);
    }

    public String getDistanceTooSmallMessage(String given) {
        return getMessage("distance-too-small", "Minimum distance value is 1.0, got {distance}")
                .replace("{distance}", given);
    }

    public String getNothingToUnsetMessage() {
        return getMessage("nothing-to-unset", "Did not find a suitable option, nothing was unset");
    }

    public String getCannotUnSetRequiredMessage(String option, PeriodicType type) {
        return getMessage("cannot-unset-required-option",
                "Cannot unset required option {option} for type {type}; use /phd unmanage instead")
                        .replace("{option}", option).replace("{type}", type.name());
    }

    public String getStorageTypeDoesNotExistMessage(String type) {
        return getMessage("source-storage-does-not-exist",
                "{type} source storage type does not exist; no data will be converted").replace("{type}", type);
    }

    public String getAlreadyHasDataMessage(String type, boolean isDatabase) {
        String fileName = "database." + (isDatabase ? "db" : "yml");
        return getMessage("target-storage-has-data",
                "The target storage type {type} already has data\nMove or remove the file ({file}) before proceeding")
                        .replace("{type}", type).replace("{file}", fileName);
    }

    public String getStartedConvertingMessage(String from, String to) {
        return getMessage("started-converting", "Started converting from {from} to {to}").replace("{from}", from)
                .replace("{to}", to);
    }

    public String getDoneConvertingMessage(String from, String to) {
        return getMessage("done-converting", "Done converting from {from} to {to}").replace("{from}", from)
                .replace("{to}", to);
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
        return getMessage("hologram-not-tracked", "Hologram not managed: {name}").replace("{name}", name);
    }

    public String getHologramNotFoundMessage(String name, PeriodicType type) {
        return getMessage("hologram-not-found", "Hologram not found: {name} of type {type}").replace("{name}", name)
                .replace("{type}", type.name());
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
        return getMessage("illega-time",
                "Time must be specified as e.g '1d' or '10h30m'. Available units: y, mo, d, h, m, s. Got {msg} instead")
                        .replace("{msg}", msg);
    }

    public String getZombieListMessage(Set<HDHologramInfo> zombies, int page, boolean doPages) {
        String msg = getListRaw();
        PageInfo pageInfo = PageUtils.getPageInfo(zombies.size(), PageUtils.HOLOGRAMS_PER_PAGE, page, doPages);
        int i = 0;
        int startNr = pageInfo.getStartNumber();
        int endNr = pageInfo.getEndNumber();
        List<String> lines = new ArrayList<>();
        for (HDHologramInfo zombie : zombies) {
            i++;
            if (i < startNr || i > endNr) continue;
            List<String> types = new ArrayList<>();
            for (HologramInfo info : zombie.getInfos()) {
                types.add(info.getType().name());
            }
            lines.add(String.format("%s %s", zombie.getHoloName(), String.join(", ", types)));
        }
        String numbers;
        if (endNr <= startNr && startNr == 1) {
            numbers = String.valueOf(endNr);
        } else {
            numbers = String.format("%d-%d", startNr, endNr);
        }
        msg = msg.replace("{numbers}", numbers);
        msg = msg.replace("{page}", String.valueOf(page)).replace("{max-pages}",
                String.valueOf(pageInfo.getNumberOfPages()));
        if (!doPages) {
            msg = msg.replace(", page 1/1", "");
        }
        msg = msg.replace("{holograms}", String.join("\n", lines));
        return msg;
    }

    private String getListRaw() {
        return getMessage("hologram-list", "Holograms (holograms {numbers}, page {page}/{max-pages}): \n{holograms}");
    }

    public String getHologramListMessage(Map<String, String> holograms, int page, boolean doPages) {
        List<String> lines = new ArrayList<>();
        PageInfo pageInfo = PageUtils.getPageInfo(holograms.size(), PageUtils.HOLOGRAMS_PER_PAGE, page, doPages);
        int i = 1;
        for (Entry<String, String> entry : holograms.entrySet()) {
            if (i < pageInfo.getStartNumber() || i > pageInfo.getEndNumber()) {
                i++;
                continue;
            }
            lines.add(entry.getKey() + " " + entry.getValue());
            i++;
        }
        String msg = getListRaw();
        int startNr = pageInfo.getStartNumber();
        int endNr = pageInfo.getEndNumber();
        String numbers;
        if (endNr <= startNr && startNr == 1) {
            numbers = String.valueOf(endNr);
        } else {
            numbers = String.format("%d-%d", startNr, endNr);
        }
        msg = msg.replace("{numbers}", numbers);
        msg = msg.replace("{page}", String.valueOf(page)).replace("{max-pages}",
                String.valueOf(pageInfo.getNumberOfPages()));
        if (!doPages) {
            msg = msg.replace(", page 1/1", "");
        }
        return msg.replace("{holograms}", String.join("\n", lines));
    }

    public String getUnmanagedHologramMessage(String name, PeriodicType type) {
        return getMessage("unmanaged-hologram", "Unmanaged hologram {name} of type {type}").replace("{name}", name)
                .replace("{type}", type.name());
    }

    public String getNoSuchOptionMessage(PeriodicType type, String option) {
        return getMessage("no-such-option", "{type} holograms have no {option} option").replace("{type}", type.name())
                .replace("{option}", option);
    }

    public String getNeedPairedOptionsMessage() {
        return getMessage("incorrect-set-options",
                "Need a set of key-value pairs to set, got an odd number of arguments");
    }

    public String getOptionMissingMessage(PeriodicType type, String option) {
        return getMessage("option-missing", "Need to set {option} for a {type} hologram").replace("{type}", type.name())
                .replace("{option}", option);
    }

    public String getIncorrectTimeMessage(String msg) {
        return getMessage("incorrect-time", "Time format is hh:mm (24-hour), got {time}").replace("{time}", msg);
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
        return getMessage("unset-playercount", "Unset playercount of {player}; now 0").replace("{player}",
                player.getName());
    }

    public String getUnsetOptionsMessage(List<String> opts) {
        return getMessage("unset-options", "Unset {options}; now using default").replace("{options}",
                String.join(", ", opts));
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
                        desc = Boolean.valueOf(dri.getExtra()) ? getPluginFolderRecreatedMessage()
                                : getProblemRecreatingPluginFolder();
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
                desc = issue.getExtra() == null ? issue.getIssue()
                        : String.format("%s: %s", issue.getIssue(), issue.getExtra());
                ;
            }
            problems.append(desc);
        }
        return getMessage("problems-reloading-config", "Problems reloading config: {problems}").replace("{problems}",
                problems.toString());
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

    public String getNtimesReportMessage(OfflinePlayer player, List<NTimesHologram> holos, int page, boolean doPages) {
        String msg = getMessage("ntimes-report",
                "{player} has seen the following NTIMES holograms (holograms {holograms}, page {page}/{max-pages}):\n{times}");
        msg = msg.replace("{player}", player.getName());

        PageInfo pageInfo = PageUtils.getPageInfo(holos.size(), PageUtils.HOLOGRAMS_PER_PAGE, page, doPages);
        int startNr = pageInfo.getStartNumber();
        int endNr = pageInfo.getEndNumber();
        String numbers;
        if (endNr <= startNr && startNr == 1) {
            numbers = String.valueOf(endNr);
        } else {
            numbers = String.format("%d-%d", startNr, endNr);
        }
        msg = msg.replace("{holograms}", numbers);
        msg = msg.replace("{page}", String.valueOf(page)).replace("{max-pages}",
                String.valueOf(pageInfo.getNumberOfPages()));

        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (NTimesHologram hologram : holos) {
            i++;
            if (i < pageInfo.getStartNumber() || i > pageInfo.getEndNumber()) {
                continue;
            }
            Integer amount = hologram.getShownTo().get(player.getUniqueId());
            if (amount == null)
                amount = 0;
            if (builder.length() != 0)
                builder.append("\n");
            builder.append(String.format(timesString, hologram.getName(), amount, hologram.getTimesToShow()));
        }
        if (builder.length() == 0) {
            builder.append("None");
        }
        if (!doPages) {
            msg = msg.replace(", page 1/1", "");
        }
        return msg.replace("{times}", builder.toString());
    }

    public String getHologramInfoMessage(FlashingHologram hologram, int page, boolean doPages) {
        String typeinfo = getTypeInfo(hologram, page, doPages);
        String typeName = (hologram.getType() == PeriodicType.NTIMES
                && ((NTimesHologram) hologram).getTimesToShow() < 0) ? PeriodicType.ALWAYS.name()
                        : hologram.getType().name();
        String time = getShowTimeString(hologram);
        String distance = getDistanceString(hologram);
        String flash;
        if (hologram.flashes()) {
            flash = String.format("%3.2f/%3.2f", hologram.getFlashOn(), hologram.getFlashOff());
        } else {
            flash = "None";
        }
        Location loc = hologram.getLocation();
        return getMessage("hologram-info",
                "Hologram {name}:\nWorld: {world}\nLocation: {location}\nType: {type}\nShowTime: {time}\nFlash: {flash}\nActivationDistance: {distance}\nPermission: {perms}\nTypeInfo: {typeinfo}")
                        .replace("{name}", hologram.getName())
                        .replace("{world}", hologram.getLocation().getWorld().getName()).replace("{type}", typeName)
                        .replace("{time}", time).replace("{typeinfo}", typeinfo).replace("{distance}", distance)
                        .replace("{flash}", flash)
                        .replace("{location}", String.format("%.1f %.1f %.1f", loc.getX(), loc.getY(), loc.getZ()))
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

    public String getTypeInfo(PeriodicHologramBase hologram, int page, boolean doPages) {
        String typeinfo;
        switch (hologram.getType()) {
            case MCTIME:
                typeinfo = getMCTimeTypeInfo((MCTimeHologram) hologram);
                break;
            case IRLTIME:
                typeinfo = getIRLTimeTypeInfo((IRLTimeHologram) hologram);
                break;
            case NTIMES:
                NTimesHologram nth = (NTimesHologram) hologram;
                typeinfo = getNTimesTypeInfo(nth, nth.getTimesToShow() < 0, page, doPages);
                break;
            case ALWAYS:
                typeinfo = getNTimesTypeInfo((NTimesHologram) hologram, true, page, doPages);
                break;
            default:
                typeinfo = "N/A"; // this shouldn't happen!
                phd.getLogger()
                        .warning("Unable to get info for hologram of type " + hologram.getType() + " - " + hologram);
        }
        return typeinfo;
    }

    private String getIRLTimeTypeInfo(IRLTimeHologram hologram) {
        return getMessage("typeinfo.IRLTIME", "Shown at: {time}").replace("{time}",
                TimeUtils.toIRLTime(hologram.getTime()));
    }

    private String getMCTimeTypeInfo(MCTimeHologram hologram) {
        return getMessage("typeinfo.MCTIME", "Shown at: {time}").replace("{time}",
                TimeUtils.toMCTime(hologram.getTime()));
    }

    public String getNTimesTypeInfo(NTimesHologram hologram, boolean always, int page, boolean doPages) {
        String msg = getMessage(always ? "typeinfo.ALWAYS" : "typeinfo.NTIMES", always ? "Always shown"
                : "Show times: {times}; Shown to (players {players}, page {page}/{max-pages}): {players:times}");
        if (!always) {
            msg = msg.replace("{times}", String.valueOf(hologram.getTimesToShow()));
            final int nrOfPlayers = hologram.getShownTo().size();
            PageInfo pageInfo = PageUtils.getPageInfo(nrOfPlayers, PageUtils.PLAYERS_PER_PAGE, page, doPages);
            int startNr = pageInfo.getStartNumber();
            int endNr = pageInfo.getEndNumber();
            String numbers;
            if (endNr <= startNr && startNr == 1) {
                numbers = String.valueOf(endNr);
            } else {
                numbers = String.format("%d-%d", startNr, endNr);
            }
            msg = msg.replace("{players}", numbers);
            msg = msg.replace("{page}", String.valueOf(page)).replace("{max-pages}",
                    String.valueOf(pageInfo.getNumberOfPages()));
            List<String> playersAndTimes = new ArrayList<>();
            int i = 1;
            for (Entry<UUID, Integer> entry : hologram.getShownTo().entrySet()) {
                if (i < pageInfo.getStartNumber() || i > pageInfo.getEndNumber()) {
                    i++;
                    continue;
                }
                OfflinePlayer player = phd.getServer().getOfflinePlayer(entry.getKey());
                String playerName = (player == null || (!player.hasPlayedBefore() && !player.isOnline())) ? "UNKNOWNPLAYER" : player.getName();
                playersAndTimes.add(playerName + ": " + entry.getValue());
                i++;
            }
            if (playersAndTimes.isEmpty()) {
                msg = msg.replace("{players:times}", "none");
            } else {
                msg = msg.replace("{players:times}", "\n" + String.join("\n", playersAndTimes));
            }
        }
        if (!doPages) {
            msg = msg.replace(", page 1/1", "");
        }
        return msg;
    }

    public String getLowSaveDelayMessage(long seconds) {
        return getMessage("save-frequency-low",
                "Configuration save-frequency set to {value} seconds may result in decreased performance")
                        .replace("{value}", String.valueOf(seconds));
    }

    public String getMessage(String path, String def) {
        return ChatColor.translateAlternateColorCodes('&', (getConfig().getString(path, def)));
    }

}