package me.ford.periodicholographicdisplays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays.ReloadIssue;
import me.ford.periodicholographicdisplays.Settings.SettingIssue;
import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.mock.MockNamedHologram;
import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.mock.MockPlayer;

public class DefaultVsCodeMessagesTests extends TestHelpers {
    private MockPeriodicHolographicDisplays phd;
    private Messages defaults;
    private Messages noDefaults;

    @Before
    public void setUp() {
        phd = new MockPeriodicHolographicDisplays();
        defaults = phd.getMessages(); // in code defaults
        try {
            noDefaults = new DefMessages(phd);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        phd.clear();
    }

    @Override
    public IPeriodicHolographicDisplays getPhd() {
        return phd;
    }

    @Test
    public void messages_not_null() {
        Assert.assertNotNull(defaults);
    }

    @Test
    public void activeStorageMessage_fills_placeholder() {
        String msg1 = defaults.getActiveStorageMessage(true);
        String msg2 = noDefaults.getActiveStorageMessage(true);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void addedToCache_fills_placeholders() {
        MockPlayer player = new MockPlayer(getRandomName("name"));
        String msg1 = defaults.getAddedToCacheMessage(player);
        String msg2 = noDefaults.getAddedToCacheMessage(player);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void alreadyHassData_fills_placeholders() {
        String type = getRandomName("type");
        String msg1 = defaults.getAlreadyHasDataMessage(type, true);
        String msg2 = noDefaults.getAlreadyHasDataMessage(type, true);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void alreadyHassData_fills_placeholders_2() {
        String type = getRandomName("type");
        String msg1 = defaults.getAlreadyHasDataMessage(type, false);
        String msg2 = noDefaults.getAlreadyHasDataMessage(type, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void availableTypes_fills_placeholders_1() {
        String name = getRandomName("name");
        List<PeriodicType> types = new ArrayList<>();
        types.add(PeriodicType.ALWAYS);
        String msg1 = defaults.getAvailableTypesMessage(name, types);
        String msg2 = noDefaults.getAvailableTypesMessage(name, types);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void availableTypes_fills_placeholders_2() {
        String name = getRandomName("name");
        List<PeriodicType> types = new ArrayList<>();
        types.add(PeriodicType.ALWAYS);
        types.add(PeriodicType.NTIMES);
        String msg1 = defaults.getAvailableTypesMessage(name, types);
        String msg2 = noDefaults.getAvailableTypesMessage(name, types);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void cannotConvertSame_fills_placeholders() {
        String type = getRandomName("type");
        String msg1 = defaults.getCannotConvertSameMessage(type);
        String msg2 = noDefaults.getCannotConvertSameMessage(type);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void cannotUnsetRequired_fills_placeholders() {
        String option = getRandomName("option");
        PeriodicType type = PeriodicType.MCTIME;
        String msg1 = defaults.getCannotUnSetRequiredMessage(option, type);
        String msg2 = noDefaults.getCannotUnSetRequiredMessage(option, type);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void configRecreated_no_placeholders() {
        String msg1 = defaults.getConfigRecreatedMessage();
        String msg2 = noDefaults.getConfigRecreatedMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void configReloaded_no_placeholders() {
        String msg1 = defaults.getConfigReloadedMessage();
        String msg2 = noDefaults.getConfigReloadedMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void disable_no_placeholders() {
        String msg1 = defaults.getDisablingMessage();
        String msg2 = noDefaults.getDisablingMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void distanceTooSmall_fills_placeholders() {
        String dist = getRandomName("dist");
        String msg1 = defaults.getDistanceTooSmallMessage(dist);
        String msg2 = noDefaults.getDistanceTooSmallMessage(dist);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void doneConverting_fills_placeholders() {
        String from = getRandomName("from");
        String to = getRandomName("to");
        String msg1 = defaults.getDoneConvertingMessage(from, to);
        String msg2 = noDefaults.getDoneConvertingMessage(from, to);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void flashMustHaveBoth_fills_placeholders() {
        String msg1 = defaults.getFlashMustHaveBothMessage(null); // irrelevant argument
        String msg2 = noDefaults.getFlashMustHaveBothMessage(null); // irrelevant argument
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void flashTimeTooSmall_fills_placeholders() {
        String specified = getRandomName("spec");
        String msg1 = defaults.getFlashTimeTooSmallMessage(specified);
        String msg2 = noDefaults.getFlashTimeTooSmallMessage(specified);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hdHologramNotFound_fills_placeholders() {
        String name = getRandomName("name");
        String msg1 = defaults.getHDHologramNotFoundMessage(name);
        String msg2 = noDefaults.getHDHologramNotFoundMessage(name);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramAlreadyManaged_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.IRLTIME;
        String msg1 = defaults.getHologramAlreadyManagedMessage(name, type);
        String msg2 = noDefaults.getHologramAlreadyManagedMessage(name, type);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_no_pages_no_perms() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name);
        AlwaysHologram hologram = new AlwaysHologram(phd, holo, name, 5.55, 4, false, null, FlashingHologram.NO_FLASH,
                FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false);
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_no_pages_with_perms() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(name);
        AlwaysHologram hologram = new AlwaysHologram(phd, holo, name, 5.55, 4, false, perms, FlashingHologram.NO_FLASH,
                FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false);
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_no_perms() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name);
        AlwaysHologram hologram = new AlwaysHologram(phd, holo, name, 5.55, 4, false, null, FlashingHologram.NO_FLASH,
                FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false);
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_with_perms() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(name);
        AlwaysHologram hologram = new AlwaysHologram(phd, holo, name, 5.55, 4, false, perms, FlashingHologram.NO_FLASH,
                FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false);
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_with_perms_with_flash() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(name);
        double flashOn = 2.1;
        double flashOff = 2.2;
        AlwaysHologram hologram = new AlwaysHologram(phd, holo, name, 5.55, 4, false, perms, flashOn, flashOff);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false);
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramList_fills_placeholders() {
        String name = getRandomName("name");
        String types = getRandomName("types");
        Map<String, String> holograms = new HashMap<>();
        holograms.put(name, types);
        int page = 1;
        String msg1 = defaults.getHologramListMessage(holograms, page, false);
        String msg2 = noDefaults.getHologramListMessage(holograms, page, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramList_fills_placeholders_multiple() {
        Map<String, String> holograms = new HashMap<>();
        String name1 = getRandomName("name");
        String types1 = getRandomName("types");
        String name2 = getRandomName("name");
        String types2 = getRandomName("types");
        holograms.put(name1, types1);
        holograms.put(name2, types2);
        int page = 1;
        String msg1 = defaults.getHologramListMessage(holograms, page, false);
        String msg2 = noDefaults.getHologramListMessage(holograms, page, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramNotFound_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.IRLTIME;
        String msg1 = defaults.getHologramNotFoundMessage(name, type);
        String msg2 = noDefaults.getHologramNotFoundMessage(name, type);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramNotManaged_fills_placeholders() {
        String name = getRandomName("name");
        String msg1 = defaults.getHologramNotManagedMessage(name);
        String msg2 = noDefaults.getHologramNotManagedMessage(name);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramNotTracked_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.MCTIME;
        String msg1 = defaults.getHologramNotTrackedMessage(name, type);
        String msg2 = noDefaults.getHologramNotTrackedMessage(name, type);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void illgealStorage_fills_placeholders() {
        String sType = getRandomName("type");
        String msg1 = defaults.getIllegalStorageMessage(sType);
        String msg2 = noDefaults.getIllegalStorageMessage(sType);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void illegalTime_fills_placeholders() {
        String time = getRandomName("time");
        String msg1 = defaults.getIllegalTimeMessage(time);
        String msg2 = noDefaults.getIllegalTimeMessage(time);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void incorrectMessages_no_placeholders() {
        String msg1 = defaults.getIncorrectMessages();
        String msg2 = noDefaults.getIncorrectMessages();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void incorrectTime_fills_placeholders() {
        String timeMsg = getRandomName("msg");
        String msg1 = defaults.getIncorrectTimeMessage(timeMsg);
        String msg2 = noDefaults.getIncorrectTimeMessage(timeMsg);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void invalidPage_fills_placeholders() {
        int maxPage = 10;
        String msg1 = defaults.getInvalidPageMessage(maxPage);
        String msg2 = noDefaults.getInvalidPageMessage(maxPage);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void legacy_no_placeholders() {
        String msg1 = defaults.getLegacyMessage();
        String msg2 = noDefaults.getLegacyMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void lowSaveDelay_fills_placeholders() {
        int seconds = 18;
        String msg1 = defaults.getLowSaveDelayMessage(seconds);
        String msg2 = noDefaults.getLowSaveDelayMessage(seconds);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void messagesRecreated_no_placeholders() {
        String msg1 = defaults.getMessagesRecreatedMessage();
        String msg2 = noDefaults.getMessagesRecreatedMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_1() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name);
        NTimesHologram hologram = new NTimesHologram(phd, holo, name, 7.6, 9, 3, false, "s.perms", 4.4, 3.3);
        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_2() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name);
        NTimesHologram hologram = new NTimesHologram(phd, holo, name, 6.7, 8, 3, false, "s.perms",
                FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_3() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name);
        NTimesHologram hologram = new NTimesHologram(phd, holo, name, 1.2, 5, 3, false, "s.perms", 4.4, 3.3);
        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_fills_placeholders_with_info() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name);
        NTimesHologram hologram = new NTimesHologram(phd, holo, name, 0.1, 2, 5, false, null, 4.4, 3.3);
        UUID id = UUID.randomUUID(); // UNKOWNPLAYER
        int times = 4;
        hologram.addShownTo(id, times);

        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_fills_placeholders_with_multiple_info() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(name);
        NTimesHologram hologram = new NTimesHologram(phd, holo, name, 0.1, 2, 5, false, null, 4.4, 3.3);
        UUID id1 = UUID.randomUUID(); // UNKOWNPLAYER
        int times1 = 4;
        UUID id2 = UUID.randomUUID();
        int times2 = 8;
        hologram.addShownTo(id1, times1);
        hologram.addShownTo(id2, times2);

        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needANumber_fills_placeholders() {
        String notNumber = getRandomName("msg");
        String msg1 = defaults.getNeedANumberMessage(notNumber);
        String msg2 = noDefaults.getNeedANumberMessage(notNumber);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needAnInteger_fills_placeholders() {
        String notInt = getRandomName("msg");
        String msg1 = defaults.getNeedAnIntegerMessage(notInt);
        String msg2 = noDefaults.getNeedAnIntegerMessage(notInt);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needCountAfterPlayercound_no_placeholders() {
        String msg1 = defaults.getNeedCountAfterPlayercount();
        String msg2 = noDefaults.getNeedCountAfterPlayercount();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needPairedOptions_no_placeholders() {
        String msg1 = defaults.getNeedPairedOptionsMessage();
        String msg2 = noDefaults.getNeedPairedOptionsMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needTypeOrPage_fills_placeholders() {
        String spec = getRandomName("msg");
        String msg1 = defaults.getNeedTypeOrPageMessage(spec);
        String msg2 = noDefaults.getNeedTypeOrPageMessage(spec);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void negativeTimes_fills_placeholders() {
        String times = getRandomName("msg");
        String msg1 = defaults.getNegativeTimesMessage(times);
        String msg2 = noDefaults.getNegativeTimesMessage(times);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void nextPageHint_fills_placeholders() {
        String command = getRandomName("msg");
        String msg1 = defaults.getNextPageHint(command);
        String msg2 = noDefaults.getNextPageHint(command);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void noLP_no_placeholders() {
        String msg1 = defaults.getNoLPMessage();
        String msg2 = noDefaults.getNoLPMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void noPluginFolder_no_placeholders() {
        String msg1 = defaults.getNoPluginFolderMessage();
        String msg2 = noDefaults.getNoPluginFolderMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void nosuchOption_fills_placeholders() {
        PeriodicType type = PeriodicType.ALWAYS;
        String notInt = getRandomName("msg");
        String msg1 = defaults.getNoSuchOptionMessage(type, notInt);
        String msg2 = noDefaults.getNoSuchOptionMessage(type, notInt);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void nothingToUnset_no_placeholders() {
        String msg1 = defaults.getNothingToUnsetMessage();
        String msg2 = noDefaults.getNothingToUnsetMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesReport_fills_placeholders_no_pages() {
        String name = getRandomName("name");
        MockPlayer player = new MockPlayer(name);
        List<NTimesHologram> holos = new ArrayList<>();
        holos.add((NTimesHologram) getRandomHolgram(PeriodicType.NTIMES));
        int page = 1;
        String msg1 = defaults.getNtimesReportMessage(player, holos, page, false);
        String msg2 = noDefaults.getNtimesReportMessage(player, holos, page, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesReport_fills_placeholders_no_pages_2() {
        String name = getRandomName("name");
        MockPlayer player = new MockPlayer(name);
        List<NTimesHologram> holos = new ArrayList<>();
        holos.add((NTimesHologram) getRandomHolgram(PeriodicType.NTIMES));
        holos.add((NTimesHologram) getRandomHolgram(PeriodicType.NTIMES));
        int page = 1;
        String msg1 = defaults.getNtimesReportMessage(player, holos, page, false);
        String msg2 = noDefaults.getNtimesReportMessage(player, holos, page, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void optionMissing_fills_placeholders() {
        PeriodicType type = PeriodicType.MCTIME;
        String option = getRandomName("option");
        String msg1 = defaults.getOptionMissingMessage(type, option);
        String msg2 = noDefaults.getOptionMissingMessage(type, option);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void optionNotSet_fills_placeholders() {
        String option = getRandomName("option");
        String msg1 = defaults.getOptionNotSetMessage(option);
        String msg2 = noDefaults.getOptionNotSetMessage(option);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void playerNotFound_fills_placeholders() {
        String player = getRandomName("player");
        String msg1 = defaults.getPlayerNotFoundMessage(player);
        String msg2 = noDefaults.getPlayerNotFoundMessage(player);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void pluginFoldereRecreated_no_placeholders() {
        String msg1 = defaults.getPluginFolderRecreatedMessage();
        String msg2 = noDefaults.getPluginFolderRecreatedMessage();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void problemRecreatingPluginFolder_no_placeholders() {
        String msg1 = defaults.getProblemRecreatingPluginFolder();
        String msg2 = noDefaults.getProblemRecreatingPluginFolder();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void problemWithConfig_fills_placeholders() {
        String value = getRandomName("msg");
        SettingIssue issue = SettingIssue.ACTIVATION_DISTANCE;
        String msg1 = defaults.getProblemWithConfigMessage(issue, value);
        String msg2 = noDefaults.getProblemWithConfigMessage(issue, value);
        Assert.assertEquals(msg1, msg2);
    }

    @Test // TODO - this is rather primitive now
    public void problemsReloadingConfig_fills_placeholders() {
        List<ReloadIssue> issues = new ArrayList<>();
        String msg1 = defaults.getProblemsReloadingConfigMessage(issues);
        String msg2 = noDefaults.getProblemsReloadingConfigMessage(issues);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void secondsTooSmall_fills_placeholders() {
        String times = getRandomName("msg");
        String msg1 = defaults.getSecondsTooSmallMessage(times);
        String msg2 = noDefaults.getSecondsTooSmallMessage(times);
        Assert.assertEquals(msg1, msg2);
    }

    private class DefMessages extends Messages {
        private static final String FILE_NAME = "../../../src/main/resources/messages.yml";

        protected DefMessages(MockPeriodicHolographicDisplays phd) throws InvalidConfigurationException {
            super(phd, FILE_NAME);
        }

    }

}