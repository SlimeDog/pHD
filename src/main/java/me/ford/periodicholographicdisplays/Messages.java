package me.ford.periodicholographicdisplays;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import dev.ratas.slimedogcore.api.messaging.SDCMessage;
import dev.ratas.slimedogcore.api.messaging.context.SDCQuadrupleContext;
import dev.ratas.slimedogcore.api.messaging.context.SDCTripleContext;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCDoubleContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCQuadrupleContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCSingleContextFactory;
import dev.ratas.slimedogcore.api.messaging.context.factory.SDCTripleContextFactory;
import dev.ratas.slimedogcore.api.messaging.delivery.MessageTarget;
import dev.ratas.slimedogcore.api.messaging.factory.SDCDoubleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCQuadrupleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCSingleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCTripleContextMessageFactory;
import dev.ratas.slimedogcore.api.messaging.factory.SDCVoidContextMessageFactory;
import dev.ratas.slimedogcore.impl.messaging.MessagesBase;
import dev.ratas.slimedogcore.impl.messaging.context.factory.SingleContextFactory;
import dev.ratas.slimedogcore.impl.messaging.context.factory.delegating.DelegatingDoubleContextFactory;
import dev.ratas.slimedogcore.impl.messaging.context.factory.delegating.DelegatingMultipleToOneContextFactory;
import dev.ratas.slimedogcore.impl.messaging.context.factory.delegating.DelegatingQuadrupleContextFactory;
import dev.ratas.slimedogcore.impl.messaging.context.factory.delegating.DelegatingTripleContextFactory;
import dev.ratas.slimedogcore.impl.messaging.factory.DoubleContextMessageFactory;
import dev.ratas.slimedogcore.impl.messaging.factory.MsgUtil;
import dev.ratas.slimedogcore.impl.messaging.factory.QuadrupleContextMessageFactory;
import dev.ratas.slimedogcore.impl.messaging.factory.TripleContextMessageFactory;
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
import me.ford.periodicholographicdisplays.util.PageUtils;
import me.ford.periodicholographicdisplays.util.TimeUtils;
import me.ford.periodicholographicdisplays.util.PageUtils.PageInfo;

/**
 * Messages
 */
public class Messages extends MessagesBase {
    private final static String FILE_NAME = "messages.yml";
    private final IPeriodicHolographicDisplays phd;
    private SDCSingleContextMessageFactory<Player> addedToCache;
    private SDCSingleContextMessageFactory<String> optionNotSet;
    private SDCSingleContextMessageFactory<String> negativeTimes;
    private SDCSingleContextMessageFactory<String> needTypeOrPage;
    private SDCSingleContextMessageFactory<String> flashTimeTooSmall;
    private SDCSingleContextMessageFactory<String> flashMustHaveBoth;
    private SDCSingleContextMessageFactory<Boolean> storageType;
    private SDCVoidContextMessageFactory incorrectMessages;
    private SDCVoidContextMessageFactory disabling;
    private SDCSingleContextMessageFactory<String> nextpageHint;
    private SDCSingleContextMessageFactory<Integer> invalidPage;
    private SDCDoubleContextMessageFactory<String, Collection<PeriodicType>> availableTypes;
    private SDCVoidContextMessageFactory configRecreated;
    private SDCVoidContextMessageFactory messagesRecreated;
    private SDCVoidContextMessageFactory pluginFolderRecreated;
    private SDCVoidContextMessageFactory problemRecreatingPluginFolder;
    private SDCVoidContextMessageFactory noPluginFolder;
    private SDCVoidContextMessageFactory noLuckPerms;
    private SDCVoidContextMessageFactory legacy;
    private SDCSingleContextMessageFactory<String> illegalStorage;
    private SDCSingleContextMessageFactory<String> secondsTooSmall;
    private SDCSingleContextMessageFactory<String> distanceTooSmall;
    private SDCVoidContextMessageFactory nothingToUnset;
    private SDCDoubleContextMessageFactory<String, PeriodicType> cannotUnSetRequired;
    private SDCSingleContextMessageFactory<String> storageTypeDoesNotExist;
    private SDCDoubleContextMessageFactory<String, Boolean> alreadyHasData;
    private SDCDoubleContextMessageFactory<String, String> startedConverting;
    private SDCDoubleContextMessageFactory<String, String> doneConverting;
    private SDCDoubleContextMessageFactory<String, String> unrecognizedConversion;
    private SDCSingleContextMessageFactory<String> cannotConvertSame;
    private SDCDoubleContextMessageFactory<String, PeriodicType> hologramNotTracked;
    private SDCDoubleContextMessageFactory<String, PeriodicType> hologramAlreadyManaged;
    private SDCTripleContextMessageFactory<String, PeriodicType, Map<String, String>> startedManaging;
    private SDCSingleContextMessageFactory<String> hdHologramNotFound;
    private SDCSingleContextMessageFactory<String> hologramNotManaged;
    private SDCDoubleContextMessageFactory<String, PeriodicType> hologramNotFound;
    private SDCSingleContextMessageFactory<String> typeNotRecognized;
    private SDCSingleContextMessageFactory<String> needAnInteger;
    private SDCSingleContextMessageFactory<String> needANumber;
    private SDCSingleContextMessageFactory<String> illegalTime;
    private SDCDoubleContextMessageFactory<String, PeriodicType> unmanagedHologram;
    private SDCDoubleContextMessageFactory<PeriodicType, String> noSuchOption;
    private SDCVoidContextMessageFactory needPairedOptions;
    private SDCDoubleContextMessageFactory<PeriodicType, String> optionMissing;
    private SDCSingleContextMessageFactory<String> incorrectTime;
    private SDCVoidContextMessageFactory needCountAfterPlayercount;
    private SDCSingleContextMessageFactory<String> playerNotFound;
    private SDCVoidContextMessageFactory unsetFlash;
    private SDCSingleContextMessageFactory<OfflinePlayer> unsetPlayerCount;
    private SDCSingleContextMessageFactory<List<String>> unsetOptions;
    private SDCDoubleContextMessageFactory<SettingIssue, String> problemWithConfig;
    private SDCVoidContextMessageFactory configReloaded;
    private SDCVoidContextMessageFactory sqlConnection;
    private SDCSingleContextMessageFactory<List<ReloadIssue>> reloadIssues;
    private SDCTripleContextMessageFactory<String, PeriodicType, Map<String, String>> setNewOptions;
    private SDCSingleContextMessageFactory<Long> lowFrequency;

    public Messages(IPeriodicHolographicDisplays phd) throws InvalidConfigurationException {
        super(phd.getCustomConfigManager().getConfig(FILE_NAME));
        getConfig().saveDefaultConfig();
        this.phd = phd;
        loadMessages();
    }

    Messages(IPeriodicHolographicDisplays phd, String name) throws InvalidConfigurationException {
        super(phd.getCustomConfigManager().getConfig(name));
        this.phd = phd;
    }

    private void loadMessages() {
        addedToCache = MsgUtil.twoToOneContextDelegator("{name}", player -> player.getName(), "{uuid}",
                player -> player.getUniqueId().toString(),
                getRawMessage("added-to-cache", "Adding player {name} UUID {uuid} to cache"));
        optionNotSet = MsgUtil.singleContext("{option}", option -> option,
                getRawMessage("option-not-set", "No change, {option} was not set"));
        negativeTimes = MsgUtil.singleContext("{times}", cur -> cur,
                getRawMessage("times-too-small", "Minimum times value is 1, got {times}"));
        needTypeOrPage = MsgUtil.singleContext("{msg}", cur -> cur,
                getRawMessage("need-to-specify-type-or-page",
                        "Need to specify either a hologram type or a page number, got {msg}"));
        flashTimeTooSmall = MsgUtil.singleContext("{time}", time -> time,
                getRawMessage("flash-time-too-small", "Minimum flash time is 1.0, got {time}"));
        flashMustHaveBoth = MsgUtil.singleContext("#unused#", specified -> specified,
                getRawMessage("flash-must-have-both",
                        "Need to specify both 'flashOn' and 'flashOff'; alternatively use 'flash' to set both at once"));
        storageType = MsgUtil.singleContext("{storage}", useDatabase -> useDatabase ? "SQLITE" : "YAML",
                getRawMessage("active-storage", "Active storage-type is {storage}"));
        incorrectMessages = MsgUtil
                .voidContext(getRawMessage("incorrect-messages", "Messages.yml is incorrectly formatted"));
        disabling = MsgUtil.voidContext(getRawMessage("disabling-plugin", "Disabling plugin!"));
        nextpageHint = MsgUtil.singleContext("{command}", cmd -> cmd,
                getRawMessage("next-page-hint", "TIP: See the next page with &n{command}&r"));
        invalidPage = MsgUtil.singleContext("{max-page}", maxPage -> String.valueOf(maxPage),
                getRawMessage("invalid-page", "Page needs to be between 1 and {max-page}"));
        availableTypes = MsgUtil.doubleContext("{hologram}", name -> name, "{types}", availableTypes -> {
            List<String> types = new ArrayList<>();
            for (PeriodicType type : availableTypes) {
                types.add(type.name());
            }
            return String.join(", ", types);
        },
                getRawMessage("available-types", "Types available for {hologram}: {types}"));
        configRecreated = MsgUtil
                .voidContext(getRawMessage("config-recreated", "The config.yml file did not exist and was recreated"));
        messagesRecreated = MsgUtil.voidContext(
                getRawMessage("messages-recreated", "The messages.yml file did not exist and was recreated"));
        pluginFolderRecreated = MsgUtil
                .voidContext(getRawMessage("plugin-folder-recreated", "Plugin folder was recreated"));
        problemRecreatingPluginFolder = MsgUtil
                .voidContext(getRawMessage("problem-recreated-plugin-folder", "Problem recreating plugin folder"));
        noPluginFolder = MsgUtil.voidContext(getRawMessage("plugin-folder-removed",
                "Plugin folder does not exist or is unreadable at reload; attempting to recreate"));
        noLuckPerms = MsgUtil.voidContext(getRawMessage("no-luckperms",
                "LuckPerms not found - unable to readjust permissions on the fly or tab-complete permissions"));
        legacy = MsgUtil.voidContext(getRawMessage("legacy-version-mctime",
                "MCTIME holograms can behave unpredictably because of the use of a legacy version of MC"));
        illegalStorage = MsgUtil.singleContext("{type}", type -> type, getRawMessage("illegal-storage-type",
                "Illegal storage type {type} - only YAML and SQLITE are supported"));
        secondsTooSmall = MsgUtil.singleContext("{seconds}", seconds -> seconds,
                getRawMessage("seconds-too-small", "Minimum seconds value is 1, got {seconds}"));
        distanceTooSmall = MsgUtil.singleContext("{distance}", dist -> dist,
                getRawMessage("distance-too-small", "Minimum distance value is 1.0, got {distance}"));
        nothingToUnset = MsgUtil
                .voidContext(getRawMessage("nothing-to-unset", "Did not find a suitable option, nothing was unset"));
        cannotUnSetRequired = MsgUtil.doubleContext("{option}", option -> option, "{type}", type -> type.name(),
                getRawMessage("cannot-unset-required-option",
                        "Cannot unset required option {option} for type {type}; use /phd unmanage instead"));
        storageTypeDoesNotExist = MsgUtil.singleContext("{tyoe}", type -> type,
                getRawMessage("source-storage-does-not-exist",
                        "{type} source storage type does not exist; no data will be converted"));
        alreadyHasData = MsgUtil.doubleContext("{type}", type -> type, "{file}",
                isDatabase -> "database." + (isDatabase ? "db" : "yml"), getRawMessage("target-storage-has-data",
                        "The target storage type {type} already has data\nMove or remove the file ({file}) before proceeding"));
        startedConverting = MsgUtil.doubleContext("{from}", from -> from, "{to}", to -> to,
                getRawMessage("started-converting", "Started converting from {from} to {to}"));
        doneConverting = MsgUtil.doubleContext("{from}", from -> from, "{to}", to -> to,
                getRawMessage("done-converting", "Done converting from {from} to {to}"));
        unrecognizedConversion = MsgUtil.doubleContext("{from}", from -> from, "{to}", to -> to, getRawMessage(
                "unrecognized-conversion", "Cannot convert from {from} to {to} - unrecognized storage types"));
        cannotConvertSame = MsgUtil.singleContext("{type}", type -> type,
                getRawMessage("cannot-convert-from-same", "Cannot convert from the same storage type ({type})"));
        hologramNotTracked = MsgUtil.doubleContext("{name}", name -> name, "{type}", type -> type.name(),
                getRawMessage("hologram-not-managed", "Hologram {name} of type {type} is not managed by pHD"));
        hologramAlreadyManaged = MsgUtil.doubleContext("{name}", name -> name, "{type}", type -> type.name(),
                getRawMessage("hologram-already-managed", "Hologram {name} of type {type} is already managed by pHD"));
        startedManaging = MsgUtil.tripleContext("{name}", name -> name, "{type}", type -> type.name(), "{options}",
                options -> {
                    List<String> opts = new ArrayList<>();
                    for (Entry<String, String> entry : options.entrySet()) {
                        opts.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
                    }
                    return String.join(", ", opts);
                }, getRawMessage("started-managing-hologram",
                        "Started managing hologram {name} of type {type}: {options}"));
        hdHologramNotFound = MsgUtil.singleContext("{name}", name -> name,
                getRawMessage("hd-hologram-not-found", "HolographicDisplays hologram {name} was not found"));
        hologramNotManaged = MsgUtil.singleContext("{name}", name -> name,
                getRawMessage("hologram-not-tracked", "Hologram not managed: {name}"));
        hologramNotManaged = MsgUtil.singleContext("{name}", name -> name,
                getRawMessage("hologram-not-tracked", "Hologram not managed: {name}"));
        hologramNotManaged = MsgUtil.singleContext("{name}", name -> name,
                getRawMessage("hologram-not-tracked", "Hologram not managed: {name}"));
        hologramNotManaged = MsgUtil.singleContext("{name}", name -> name,
                getRawMessage("hologram-not-tracked", "Hologram not managed: {name}"));
        hologramNotFound = MsgUtil.doubleContext("{name}", name -> name, "{type}", type -> type.name(),
                getRawMessage("hologram-not-found", "Hologram not found: {name} of type {type}"));
        typeNotRecognized = MsgUtil.singleContext("{type}", type -> type,
                getRawMessage("type-not-recognized", "Hologram type {type} is not recognized"));
        needAnInteger = MsgUtil.singleContext("{msg}", msg -> msg,
                getRawMessage("need-an-integer", "Value must be an integer, got {msg}"));
        needANumber = MsgUtil.singleContext("{msg}", msg -> msg,
                getRawMessage("need-a-number", "Value must be a number, got {msg}"));
        illegalTime = MsgUtil.singleContext("{msg}", msg -> msg, getRawMessage("illega-time",
                "Time must be specified as e.g '1d' or '10h30m'. Available units: y, mo, d, h, m, s. Got {msg} instead"));
        unmanagedHologram = MsgUtil.doubleContext("{name}", name -> name, "{type}", type -> type.name(),
                getRawMessage("unmanaged-hologram", "Unmanaged hologram {name} of type {type}"));
        noSuchOption = MsgUtil.doubleContext("{type}", type -> type.name(), "{option}", option -> option,
                getRawMessage("no-such-option", "{type} holograms have no {option} option"));
        needPairedOptions = MsgUtil.voidContext(getRawMessage("incorrect-set-options",
                "Need a set of key-value pairs to set, got an odd number of arguments"));
        optionMissing = MsgUtil.doubleContext("{type}", type -> type.name(), "{option}", option -> option,
                getRawMessage("option-missing", "Need to set {option} for a {type} hologram"));
        incorrectTime = MsgUtil.singleContext("{time}", time -> time,
                getRawMessage("incorrect-time", "Time format is hh:mm (24-hour), got {time}"));
        needCountAfterPlayercount = MsgUtil.voidContext(
                getRawMessage("need-player-after-playercount", "Need to specify a player after 'playercount'"));
        playerNotFound = MsgUtil.singleContext("{player}", name -> name,
                getRawMessage("player-not-found", "Player not found: {player}"));
        unsetFlash = MsgUtil.voidContext(getRawMessage("unset-flash", "Unset flash"));
        unsetPlayerCount = MsgUtil.singleContext("{player}", player -> player.getName(),
                getRawMessage("unset-playercount", "Unset playercount of {player}; now 0"));
        unsetOptions = MsgUtil.singleContext("{options}", options -> String.join(", ", options),
                getRawMessage("unset-options", "Unset {options}; now using default"));
        SDCDoubleContextFactory<SettingIssue, String> problemWithConfigContextFactory = new DelegatingDoubleContextFactory<>(
                new DelegatingMultipleToOneContextFactory<SettingIssue>(
                        new SingleContextFactory<>("{key}", issue -> issue.getPath()),
                        new SingleContextFactory<>("{type}", issue -> issue.getType().getName())),
                new SingleContextFactory<>("{value}", value -> value));
        problemWithConfig = new DoubleContextMessageFactory<>(problemWithConfigContextFactory,
                getRawMessage("problem-in-config", "Problem in config for {key}; expected {type}, got {value}"),
                MessageTarget.TEXT);
        configReloaded = MsgUtil.voidContext(
                getRawMessage("config-reloaded", "Successfully reloaded configuration, messages, and data"));
        sqlConnection = MsgUtil.voidContext(
                getRawMessage("sqlite-connection-established", "Connection to SQLite has been established"));
        reloadIssues = MsgUtil.singleContext("{problems}", issues -> {

            StringBuilder problems = new StringBuilder();
            for (ReloadIssue issue : issues) {
                String desc = null;
                problems.append("\n");
                if (issue instanceof DefaultReloadIssue) {
                    DefaultReloadIssue dri = (DefaultReloadIssue) issue;
                    switch (dri) {
                        case NO_FOLDER:
                            desc = getNoPluginFolderMessage().getMessage().getFilled();
                            problems.append(desc);
                            problems.append("\n");
                            desc = Boolean.valueOf(dri.getExtra())
                                    ? getPluginFolderRecreatedMessage().getMessage().getFilled()
                                    : getProblemRecreatingPluginFolder().getMessage().getFilled();
                            break;
                        case ILLEGA_STORAGE_TYPE:
                            desc = getIllegalStorageMessage().createWith(dri.getExtra()).getFilled();
                            break;
                        case NO_CONFIG:
                            desc = getConfigRecreatedMessage().getMessage().getFilled();
                            break;
                        case NO_MESSAGES:
                            desc = getMessagesRecreatedMessage().getMessage().getFilled();
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
            return problems.toString();
        }, getRawMessage("problems-reloading-config", "Problems reloading config: {problems}"));
        setNewOptions = MsgUtil.tripleContext("{name}", name -> name, "{type}", type -> type.name(), "{options}",
                options -> {
                    List<String> opts = new ArrayList<>();
                    for (Entry<String, String> entry : options.entrySet()) {
                        opts.add(String.format("%s=%s", entry.getKey(), entry.getValue()));
                    }
                    return String.join(", ", opts);
                },
                getRawMessage("set-new-values", "Set new values for hologram {name} of type {type}: {options}"));
        lowFrequency = MsgUtil.singleContext("{value}", seconds -> String.valueOf(seconds),
                getRawMessage("save-frequency-low",
                        "Configuration save-frequency set to {value} seconds may result in decreased performance"));
    }

    public SDCSingleContextMessageFactory<Player> getAddedToCacheMessage() {
        return addedToCache;
    }

    public SDCSingleContextMessageFactory<String> getOptionNotSetMessage() {
        return optionNotSet;
    }

    public SDCSingleContextMessageFactory<String> getNegativeTimesMessage() {
        return negativeTimes;
    }

    public SDCSingleContextMessageFactory<String> getNeedTypeOrPageMessage() {
        return needTypeOrPage;
    }

    public SDCSingleContextMessageFactory<String> getFlashTimeTooSmallMessage() {
        return flashTimeTooSmall;
    }

    public SDCSingleContextMessageFactory<String> getFlashMustHaveBothMessage() {
        return flashMustHaveBoth;
    }

    public SDCSingleContextMessageFactory<Boolean> getActiveStorageMessage() {
        return storageType;
    }

    public SDCVoidContextMessageFactory getIncorrectMessages() {
        return incorrectMessages;
    }

    public SDCVoidContextMessageFactory getDisablingMessage() {
        return disabling;
    }

    public SDCSingleContextMessageFactory<String> getNextPageHint() {
        return nextpageHint;
    }

    public SDCSingleContextMessageFactory<Integer> getInvalidPageMessage() {
        return invalidPage;
    }

    public SDCDoubleContextMessageFactory<String, Collection<PeriodicType>> getAvailableTypesMessage() {
        return availableTypes;
    }

    public SDCVoidContextMessageFactory getConfigRecreatedMessage() {
        return configRecreated;
    }

    public SDCVoidContextMessageFactory getMessagesRecreatedMessage() {
        return messagesRecreated;
    }

    public SDCVoidContextMessageFactory getPluginFolderRecreatedMessage() {
        return pluginFolderRecreated;
    }

    public SDCVoidContextMessageFactory getProblemRecreatingPluginFolder() {
        return problemRecreatingPluginFolder;
    }

    public SDCVoidContextMessageFactory getNoPluginFolderMessage() {
        return noPluginFolder;
    }

    public SDCVoidContextMessageFactory getNoLPMessage() {
        return noLuckPerms;
    }

    public SDCVoidContextMessageFactory getLegacyMessage() {
        return legacy;
    }

    public SDCSingleContextMessageFactory<String> getIllegalStorageMessage() {
        return illegalStorage;
    }

    public SDCSingleContextMessageFactory<String> getSecondsTooSmallMessage() {
        return secondsTooSmall;
    }

    public SDCSingleContextMessageFactory<String> getDistanceTooSmallMessage() {
        return distanceTooSmall;
    }

    public SDCVoidContextMessageFactory getNothingToUnsetMessage() {
        return nothingToUnset;
    }

    public SDCDoubleContextMessageFactory<String, PeriodicType> getCannotUnSetRequiredMessage() {
        return cannotUnSetRequired;
    }

    public SDCSingleContextMessageFactory<String> getStorageTypeDoesNotExistMessage() {
        return storageTypeDoesNotExist;
    }

    public SDCDoubleContextMessageFactory<String, Boolean> getAlreadyHasDataMessage() {
        return alreadyHasData;
    }

    public SDCDoubleContextMessageFactory<String, String> getStartedConvertingMessage() {
        return startedConverting;
    }

    public SDCDoubleContextMessageFactory<String, String> getDoneConvertingMessage() {
        return doneConverting;
    }

    public SDCDoubleContextMessageFactory<String, String> getUnrecognizedStorageTypeMessage() {
        return unrecognizedConversion;
    }

    public SDCSingleContextMessageFactory<String> getCannotConvertSameMessage() {
        return cannotConvertSame;
    }

    public SDCDoubleContextMessageFactory<String, PeriodicType> getHologramNotTrackedMessage() {
        return hologramNotTracked;
    }

    public SDCDoubleContextMessageFactory<String, PeriodicType> getHologramAlreadyManagedMessage() {
        return hologramAlreadyManaged;
    }

    public SDCTripleContextMessageFactory<String, PeriodicType, Map<String, String>> getStartedManagingMessage() {
        return startedManaging;
    }

    public SDCSingleContextMessageFactory<String> getHDHologramNotFoundMessage() {
        return hdHologramNotFound;
    }

    public SDCSingleContextMessageFactory<String> getHologramNotManagedMessage() {
        return hologramNotManaged;
    }

    public SDCDoubleContextMessageFactory<String, PeriodicType> getHologramNotFoundMessage() {
        return hologramNotFound;
    }

    public SDCSingleContextMessageFactory<String> getTypeNotRecognizedMessage() {
        return typeNotRecognized;
    }

    public SDCSingleContextMessageFactory<String> getNeedAnIntegerMessage() {
        return needAnInteger;
    }

    public SDCSingleContextMessageFactory<String> getNeedANumberMessage() {
        return needANumber;
    }

    public SDCSingleContextMessageFactory<String> getIllegalTimeMessage() {
        return illegalTime;
    }

    private static final class ZombiesHelper {
        private final Set<HDHologramInfo> holograms;
        private final int page;
        private final boolean doPages;
        private String hologramsRepl;
        private String numbersRepl;
        private String pageRepl;
        private String maxPageRepl;
        private String page1Repl;

        private ZombiesHelper(Set<HDHologramInfo> holograms, int page, boolean doPages) {
            this.holograms = holograms;
            this.page = page;
            this.doPages = doPages;
            calculate();
        }

        private void calculate() {

            PageInfo pageInfo = PageUtils.getPageInfo(holograms.size(), PageUtils.HOLOGRAMS_PER_PAGE, page, doPages);
            int i = 0;
            int startNr = pageInfo.getStartNumber();
            int endNr = pageInfo.getEndNumber();
            List<String> lines = new ArrayList<>();
            for (HDHologramInfo zombie : holograms) {
                i++;
                if (i < startNr || i > endNr)
                    continue;
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
            this.numbersRepl = numbers;
            this.pageRepl = String.valueOf(page);
            this.maxPageRepl = String.valueOf(pageInfo.getNumberOfPages());
            if (!doPages) {
                this.page1Repl = "";
            } else {
                this.page1Repl = ", page 1/1";
            }
            this.hologramsRepl = String.join("\n", lines);
        }

    }

    public SDCMessage<SDCTripleContext<Set<HDHologramInfo>, Integer, Boolean>> getZombieListMessage(
            Set<HDHologramInfo> holograms, int page, boolean doPages) {
        final ZombiesHelper tuple = new ZombiesHelper(holograms, page, doPages);
        SingleContextFactory<Set<HDHologramInfo>> delegate01 = new SingleContextFactory<>("{max-pages}",
                fake -> tuple.maxPageRepl);
        SingleContextFactory<Set<HDHologramInfo>> delegate02 = new SingleContextFactory<>("{holograms}",
                fake -> tuple.hologramsRepl);
        SingleContextFactory<Set<HDHologramInfo>> delegate03 = new SingleContextFactory<>("{page}",
                fake -> tuple.pageRepl);
        SDCSingleContextFactory<Set<HDHologramInfo>> delegate1 = new DelegatingMultipleToOneContextFactory<>(delegate01,
                delegate02, delegate03);
        SDCSingleContextFactory<Integer> delegate2 = new SingleContextFactory<>("{numbers}",
                fake -> tuple.numbersRepl);
        SDCSingleContextFactory<Boolean> delegate3 = new SingleContextFactory<>(", page 1/1",
                fake -> tuple.page1Repl);
        SDCTripleContextFactory<Set<HDHologramInfo>, Integer, Boolean> contextFactory = new DelegatingTripleContextFactory<>(
                delegate1, delegate2, delegate3);
        return new TripleContextMessageFactory<>(contextFactory,
                getRawMessage("hologram-list",
                        "Holograms (holograms {numbers}, page {page}/{max-pages}): \n{holograms}"),
                MessageTarget.TEXT).createWith(holograms, page, doPages);
    }

    private static final class HologramsHelper {
        private final Map<String, String> holograms;
        private final int page;
        private final boolean doPages;
        private String hologramsRepl;
        private String numbersRepl;
        private String pageRepl;
        private String maxPageRepl;
        private String page1Repl;

        private HologramsHelper(Map<String, String> holograms, int page, boolean doPages) {
            this.holograms = holograms;
            this.page = page;
            this.doPages = doPages;
            calculate();
        }

        private void calculate() {

            PageInfo pageInfo = PageUtils.getPageInfo(holograms.size(), PageUtils.HOLOGRAMS_PER_PAGE, page, doPages);
            int i = 1;
            int startNr = pageInfo.getStartNumber();
            int endNr = pageInfo.getEndNumber();
            List<String> lines = new ArrayList<>();
            for (Entry<String, String> entry : holograms.entrySet()) {
                if (i < pageInfo.getStartNumber() || i > pageInfo.getEndNumber()) {
                    i++;
                    continue;
                }
                lines.add(entry.getKey() + " " + entry.getValue());
                i++;
            }
            String numbers;
            if (endNr <= startNr && startNr == 1) {
                numbers = String.valueOf(endNr);
            } else {
                numbers = String.format("%d-%d", startNr, endNr);
            }
            this.numbersRepl = numbers;
            this.pageRepl = String.valueOf(page);
            this.maxPageRepl = String.valueOf(pageInfo.getNumberOfPages());
            if (!doPages) {
                this.page1Repl = "";
            } else {
                this.page1Repl = ", page 1/1";
            }
            this.hologramsRepl = String.join("\n", lines);
        }

    }

    public SDCMessage<SDCTripleContext<Map<String, String>, Integer, Boolean>> getHologramListMessage(
            Map<String, String> holograms, int page, boolean doPages) {
        final HologramsHelper tuple = new HologramsHelper(holograms, page, doPages);
        SingleContextFactory<Map<String, String>> delegate01 = new SingleContextFactory<>("{max-pages}",
                fake -> tuple.maxPageRepl);
        SingleContextFactory<Map<String, String>> delegate02 = new SingleContextFactory<>("{holograms}",
                fake -> tuple.hologramsRepl);
        SingleContextFactory<Map<String, String>> delegate03 = new SingleContextFactory<>("{page}",
                fake -> tuple.pageRepl);
        SDCSingleContextFactory<Map<String, String>> delegate1 = new DelegatingMultipleToOneContextFactory<>(delegate01,
                delegate02, delegate03);
        SDCSingleContextFactory<Integer> delegate2 = new SingleContextFactory<>("{numbers}",
                fake -> tuple.numbersRepl);
        SDCSingleContextFactory<Boolean> delegate3 = new SingleContextFactory<>(", page 1/1",
                fake -> tuple.page1Repl);
        SDCTripleContextFactory<Map<String, String>, Integer, Boolean> contextFactory = new DelegatingTripleContextFactory<>(
                delegate1, delegate2, delegate3);
        return new TripleContextMessageFactory<>(contextFactory,
                getRawMessage("hologram-list",
                        "Holograms (holograms {numbers}, page {page}/{max-pages}): \n{holograms}"),
                MessageTarget.TEXT).createWith(holograms, page, doPages);

    }

    public SDCDoubleContextMessageFactory<String, PeriodicType> getUnmanagedHologramMessage() {
        return unmanagedHologram;
    }

    public SDCDoubleContextMessageFactory<PeriodicType, String> getNoSuchOptionMessage() {
        return noSuchOption;
    }

    public SDCVoidContextMessageFactory getNeedPairedOptionsMessage() {
        return needPairedOptions;
    }

    public SDCDoubleContextMessageFactory<PeriodicType, String> getOptionMissingMessage() {
        return optionMissing;
    }

    public SDCSingleContextMessageFactory<String> getIncorrectTimeMessage() {
        return incorrectTime;
    }

    public SDCVoidContextMessageFactory getNeedCountAfterPlayercount() {
        return needCountAfterPlayercount;
    }

    public SDCSingleContextMessageFactory<String> getPlayerNotFoundMessage() {
        return playerNotFound;
    }

    public SDCVoidContextMessageFactory getUnsetFlashMessage() {
        return unsetFlash;
    }

    public SDCSingleContextMessageFactory<OfflinePlayer> getUnsetPlayerCountMessage() {
        return unsetPlayerCount;
    }

    public SDCSingleContextMessageFactory<List<String>> getUnsetOptionsMessage() {
        return unsetOptions;
    }

    public SDCDoubleContextMessageFactory<SettingIssue, String> getProblemWithConfigMessage() {
        return problemWithConfig;
    }

    public SDCVoidContextMessageFactory getConfigReloadedMessage() {
        return configReloaded;
    }

    public SDCVoidContextMessageFactory getSqlConnectionMessage() {
        return sqlConnection;
    }

    public SDCSingleContextMessageFactory<List<ReloadIssue>> getProblemsReloadingConfigMessage() {
        return reloadIssues;
    }

    public SDCTripleContextMessageFactory<String, PeriodicType, Map<String, String>> getSetNewOptionsMessage() {
        return setNewOptions;
    }

    private static String timesString = "%s %d/%d";

    private static final class NTimesReportHelper {
        private final OfflinePlayer player;
        private final List<NTimesHologram> holograms;
        private final int page;
        private final boolean doPages;
        private String playerRepl;
        private String hologramsRepl;
        private String pageRepl;
        private String maxPageRepl;
        private String page1Repl;
        private String timesRepl;

        public NTimesReportHelper(OfflinePlayer player, List<NTimesHologram> holograms, int page, boolean doPages) {
            this.player = player;
            this.holograms = holograms;
            this.page = page;
            this.doPages = doPages;
            calculate();
        }

        private void calculate() {
            playerRepl = player.getName();

            PageInfo pageInfo = PageUtils.getPageInfo(holograms.size(), PageUtils.HOLOGRAMS_PER_PAGE, page, doPages);
            int startNr = pageInfo.getStartNumber();
            int endNr = pageInfo.getEndNumber();
            String numbers;
            if (endNr <= startNr && startNr == 1) {
                numbers = String.valueOf(endNr);
            } else {
                numbers = String.format("%d-%d", startNr, endNr);
            }
            hologramsRepl = numbers;
            // msg = msg.replace("{holograms}", numbers);
            pageRepl = String.valueOf(page);
            maxPageRepl = String.valueOf(pageInfo.getNumberOfPages());

            StringBuilder builder = new StringBuilder();
            int i = 0;
            for (NTimesHologram hologram : holograms) {
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
                page1Repl = "";
            } else {
                page1Repl = ", page 1/1";
            }
            timesRepl = builder.toString();
        }
    }

    public SDCMessage<SDCQuadrupleContext<OfflinePlayer, List<NTimesHologram>, Integer, Boolean>> getNtimesReportMessage(
            OfflinePlayer player, List<NTimesHologram> holograms, int page, boolean doPages) {
        NTimesReportHelper helper = new NTimesReportHelper(player, holograms, page, doPages);
        SDCSingleContextFactory<OfflinePlayer> delegate1 = new SingleContextFactory<>("{player}",
                fake -> helper.playerRepl);
        SDCSingleContextFactory<List<NTimesHologram>> delegate2 = new SingleContextFactory<>("{holograms}",
                fake -> helper.hologramsRepl);
        SDCSingleContextFactory<Integer> delegate3 = new DelegatingMultipleToOneContextFactory<>(
                new SingleContextFactory<>("{page}", fake -> helper.pageRepl),
                new SingleContextFactory<>("{max-pages}", fake -> helper.maxPageRepl),
                new SingleContextFactory<>(", page 1/1", fake -> helper.page1Repl));
        SDCSingleContextFactory<Boolean> delegate4 = new SingleContextFactory<>("{times}", fake -> helper.timesRepl);
        SDCQuadrupleContextFactory<OfflinePlayer, List<NTimesHologram>, Integer, Boolean> contextFactory = new DelegatingQuadrupleContextFactory<>(
                delegate1, delegate2, delegate3, delegate4);
        SDCQuadrupleContextMessageFactory<OfflinePlayer, List<NTimesHologram>, Integer, Boolean> factory = new QuadrupleContextMessageFactory<>(
                contextFactory, getRawMessage("ntimes-report",
                        "{player} has seen the following NTIMES holograms (holograms {holograms}, page {page}/{max-pages}):\n{times}"),
                MessageTarget.TEXT);
        return factory.createWith(player, holograms, page, doPages);
    }

    private final class HologramInfoHelper {
        private final FlashingHologram hologram;
        private final int page;
        private final boolean doPages;
        private String nameRepl;
        private String worldRepl;
        private String typeRepl;
        private String timeRepl;
        private String typeInfoRepl;
        private String distanceRepl;
        private String flashRepl;
        private String locationRepl;
        private String permsRepl;

        private HologramInfoHelper(FlashingHologram hologram, int page, boolean doPages) {
            this.hologram = hologram;
            this.page = page;
            this.doPages = doPages;
            calculate();
        }

        public void calculate() {
            nameRepl = hologram.getName();
            worldRepl = hologram.getLocation().getWorld().getName();
            typeRepl = (hologram.getType() == PeriodicType.NTIMES
                    && ((NTimesHologram) hologram).getTimesToShow() < 0) ? PeriodicType.ALWAYS.name()
                            : hologram.getType().name();
            timeRepl = getShowTimeString(hologram);
            typeInfoRepl = getTypeInfo(hologram, page, doPages);
            distanceRepl = getDistanceString(hologram);
            flashRepl = (hologram.flashes())
                    ? String.format("%3.2f/%3.2f", hologram.getFlashOn(), hologram.getFlashOff())
                    : "None";
            Location loc = hologram.getLocation();
            locationRepl = String.format("%.1f %.1f %.1f", loc.getX(), loc.getY(), loc.getZ());
            permsRepl = hologram.hasPermissions() ? hologram.getPermissions() : "";
        }

    }

    public SDCMessage<SDCTripleContext<FlashingHologram, Integer, Boolean>> getHologramInfoMessage(
            FlashingHologram hologram,
            int page, boolean doPages) {
        HologramInfoHelper helper = new HologramInfoHelper(hologram, page, doPages);
        SDCSingleContextFactory<FlashingHologram> delegate1 = new DelegatingMultipleToOneContextFactory<>(
                new SingleContextFactory<>("{name}", fake -> helper.nameRepl),
                new SingleContextFactory<>("{world}", fake -> helper.worldRepl),
                new SingleContextFactory<>("{type}", fake -> helper.typeRepl),
                new SingleContextFactory<>("{typeinfo}", fake -> helper.typeInfoRepl),
                new SingleContextFactory<>("{time}", fake -> helper.timeRepl),
                new SingleContextFactory<>("{location}", fake -> helper.locationRepl),
                new SingleContextFactory<>("{perms}", fake -> helper.permsRepl));
        SDCSingleContextFactory<Integer> delegate2 = new SingleContextFactory<>("{distance}",
                fake -> helper.distanceRepl);
        SDCSingleContextFactory<Boolean> delegate3 = new SingleContextFactory<>("{flash}", fake -> helper.flashRepl);
        SDCTripleContextFactory<FlashingHologram, Integer, Boolean> contextFactory = new DelegatingTripleContextFactory<>(
                delegate1, delegate2, delegate3);
        return new TripleContextMessageFactory<>(contextFactory, getRawMessage("hologram-info",
                "Hologram {name}:\nWorld: {world}\nLocation: {location}\nType: {type}\nShowTime: {time}\nFlash: {flash}\nActivationDistance: {distance}\nPermission: {perms}\nTypeInfo: {typeinfo}"),
                MessageTarget.TEXT).createWith(hologram, page, doPages);
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
        return getRawMessage("typeinfo.IRLTIME", "Shown at: {time}").replace("{time}",
                TimeUtils.toIRLTime(hologram.getTime()));
    }

    private String getMCTimeTypeInfo(MCTimeHologram hologram) {
        return getRawMessage("typeinfo.MCTIME", "Shown at: {time}").replace("{time}",
                TimeUtils.toMCTime(hologram.getTime()));
    }

    public String getNTimesTypeInfo(NTimesHologram hologram, boolean always, int page, boolean doPages) {
        String msg = getRawMessage(always ? "typeinfo.ALWAYS" : "typeinfo.NTIMES", always ? "Always shown"
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
                OfflinePlayer player = phd.getOfflinePlayer(entry.getKey());
                String playerName = (player == null || (!player.hasPlayedBefore() && !player.isOnline()))
                        ? "UNKNOWNPLAYER"
                        : player.getName();
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

    public SDCSingleContextMessageFactory<Long> getLowSaveDelayMessage() {
        return lowFrequency;
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        loadMessages();
    }

    public File getFile() {
        return getConfig().getFile();
    }

    public void saveDefaultConfig() {
        getConfig().saveDefaultConfig();
    }

}