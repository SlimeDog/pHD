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
import me.ford.periodicholographicdisplays.holograms.wrap.HolographicDisplaysWrapper;
import me.ford.periodicholographicdisplays.mock.MockNamedHologram;
import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.mock.MockPlayer;
import me.ford.periodicholographicdisplays.mock.MockStarterUtil;

public class DefaultVsCodeMessagesTests extends TestHelpers {
    private MockPeriodicHolographicDisplays phd;
    private Messages defaults;
    private Messages noDefaults;

    @Before
    public void setUp() {
        MockStarterUtil.startMocking();
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
        MockStarterUtil.stopMocking();
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
        String msg1 = defaults.getActiveStorageMessage().createWith(true).getFilled();
        String msg2 = noDefaults.getActiveStorageMessage().createWith(true).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void addedToCache_fills_placeholders() {
        MockPlayer player = new MockPlayer(getRandomName("name"));
        String msg1 = defaults.getAddedToCacheMessage().createWith(player).getFilled();
        String msg2 = noDefaults.getAddedToCacheMessage().createWith(player).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void alreadyHassData_fills_placeholders() {
        String type = getRandomName("type");
        String msg1 = defaults.getAlreadyHasDataMessage().createWith(type, true).getFilled();
        String msg2 = noDefaults.getAlreadyHasDataMessage().createWith(type, true).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void alreadyHassData_fills_placeholders_2() {
        String type = getRandomName("type");
        String msg1 = defaults.getAlreadyHasDataMessage().createWith(type, false).getFilled();
        String msg2 = noDefaults.getAlreadyHasDataMessage().createWith(type, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void availableTypes_fills_placeholders_1() {
        String name = getRandomName("name");
        List<PeriodicType> types = new ArrayList<>();
        types.add(PeriodicType.ALWAYS);
        String msg1 = defaults.getAvailableTypesMessage().createWith(name, types).getFilled();
        String msg2 = noDefaults.getAvailableTypesMessage().createWith(name, types).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void availableTypes_fills_placeholders_2() {
        String name = getRandomName("name");
        List<PeriodicType> types = new ArrayList<>();
        types.add(PeriodicType.ALWAYS);
        types.add(PeriodicType.NTIMES);
        String msg1 = defaults.getAvailableTypesMessage().createWith(name, types).getFilled();
        String msg2 = noDefaults.getAvailableTypesMessage().createWith(name, types).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void cannotConvertSame_fills_placeholders() {
        String type = getRandomName("type");
        String msg1 = defaults.getCannotConvertSameMessage().createWith(type).getFilled();
        String msg2 = noDefaults.getCannotConvertSameMessage().createWith(type).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void cannotUnsetRequired_fills_placeholders() {
        String option = getRandomName("option");
        PeriodicType type = PeriodicType.MCTIME;
        String msg1 = defaults.getCannotUnSetRequiredMessage().createWith(option, type).getFilled();
        String msg2 = noDefaults.getCannotUnSetRequiredMessage().createWith(option, type).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void configRecreated_no_placeholders() {
        String msg1 = defaults.getConfigRecreatedMessage().getMessage().getFilled();
        String msg2 = noDefaults.getConfigRecreatedMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void configReloaded_no_placeholders() {
        String msg1 = defaults.getConfigReloadedMessage().getMessage().getFilled();
        String msg2 = noDefaults.getConfigReloadedMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void disable_no_placeholders() {
        String msg1 = defaults.getDisablingMessage().getMessage().getFilled();
        String msg2 = noDefaults.getDisablingMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void distanceTooSmall_fills_placeholders() {
        String dist = getRandomName("dist");
        String msg1 = defaults.getDistanceTooSmallMessage().createWith(dist).getFilled();
        String msg2 = noDefaults.getDistanceTooSmallMessage().createWith(dist).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void doneConverting_fills_placeholders() {
        String from = getRandomName("from");
        String to = getRandomName("to");
        String msg1 = defaults.getDoneConvertingMessage().createWith(from, to).getFilled();
        String msg2 = noDefaults.getDoneConvertingMessage().createWith(from, to).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void flashMustHaveBoth_fills_placeholders() {
        String msg1 = defaults.getFlashMustHaveBothMessage().createWith(null).getFilled(); // irrelevant argument
        String msg2 = noDefaults.getFlashMustHaveBothMessage().createWith(null).getFilled(); // irrelevant argument
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void flashTimeTooSmall_fills_placeholders() {
        String specified = getRandomName("spec");
        String msg1 = defaults.getFlashTimeTooSmallMessage().createWith(specified).getFilled();
        String msg2 = noDefaults.getFlashTimeTooSmallMessage().createWith(specified).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hdHologramNotFound_fills_placeholders() {
        String name = getRandomName("name");
        String msg1 = defaults.getHDHologramNotFoundMessage().createWith(name).getFilled();
        String msg2 = noDefaults.getHDHologramNotFoundMessage().createWith(name).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramAlreadyManaged_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.IRLTIME;
        String msg1 = defaults.getHologramAlreadyManagedMessage().createWith(name, type).getFilled();
        String msg2 = noDefaults.getHologramAlreadyManagedMessage().createWith(name, type).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_no_pages_no_perms() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                null, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_no_pages_with_perms() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                perms, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_no_perms() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                null, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_with_perms() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                perms, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_with_perms_with_flash() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        double flashOn = 2.1;
        double flashOff = 2.2;
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                perms, flashOn, flashOff);
        String msg1 = defaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        String msg2 = noDefaults.getHologramInfoMessage(hologram, 1, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramList_fills_placeholders() {
        String name = getRandomName("name");
        String types = getRandomName("types");
        Map<String, String> holograms = new HashMap<>();
        holograms.put(name, types);
        int page = 1;
        String msg1 = defaults.getHologramListMessage(holograms, page, false).getFilled();
        String msg2 = noDefaults.getHologramListMessage(holograms, page, false).getFilled();
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
        String msg1 = defaults.getHologramListMessage(holograms, page, false).getFilled();
        String msg2 = noDefaults.getHologramListMessage(holograms, page, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramNotFound_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.IRLTIME;
        String msg1 = defaults.getHologramNotFoundMessage().createWith(name, type).getFilled();
        String msg2 = noDefaults.getHologramNotFoundMessage().createWith(name, type).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramNotManaged_fills_placeholders() {
        String name = getRandomName("name");
        String msg1 = defaults.getHologramNotManagedMessage().createWith(name).getFilled();
        String msg2 = noDefaults.getHologramNotManagedMessage().createWith(name).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void hologramNotTracked_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.MCTIME;
        String msg1 = defaults.getHologramNotTrackedMessage().createWith(name, type).getFilled();
        String msg2 = noDefaults.getHologramNotTrackedMessage().createWith(name, type).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void illgealStorage_fills_placeholders() {
        String sType = getRandomName("type");
        String msg1 = defaults.getIllegalStorageMessage().createWith(sType).getFilled();
        String msg2 = noDefaults.getIllegalStorageMessage().createWith(sType).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void illegalTime_fills_placeholders() {
        String time = getRandomName("time");
        String msg1 = defaults.getIllegalTimeMessage().createWith(time).getFilled();
        String msg2 = noDefaults.getIllegalTimeMessage().createWith(time).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void incorrectMessages_no_placeholders() {
        String msg1 = defaults.getIncorrectMessages().getMessage().getFilled();
        String msg2 = noDefaults.getIncorrectMessages().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void incorrectTime_fills_placeholders() {
        String timeMsg = getRandomName("msg");
        String msg1 = defaults.getIncorrectTimeMessage().createWith(timeMsg).getFilled();
        String msg2 = noDefaults.getIncorrectTimeMessage().createWith(timeMsg).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void invalidPage_fills_placeholders() {
        int maxPage = 10;
        String msg1 = defaults.getInvalidPageMessage().createWith(maxPage).getFilled();
        String msg2 = noDefaults.getInvalidPageMessage().createWith(maxPage).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void legacy_no_placeholders() {
        String msg1 = defaults.getLegacyMessage().getMessage().getFilled();
        String msg2 = noDefaults.getLegacyMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void lowSaveDelay_fills_placeholders() {
        int seconds = 18;
        String msg1 = defaults.getLowSaveDelayMessage().createWith((long) seconds).getFilled();
        String msg2 = noDefaults.getLowSaveDelayMessage().createWith((long) seconds).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void messagesRecreated_no_placeholders() {
        String msg1 = defaults.getMessagesRecreatedMessage().getMessage().getFilled();
        String msg2 = noDefaults.getMessagesRecreatedMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_1() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 7.6, 9, 3, false,
                "s.perms", 4.4, 3.3);
        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_2() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 6.7, 8, 3, false,
                "s.perms", FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_3() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 1.2, 5, 3, false,
                "s.perms", 4.4, 3.3);
        String msg1 = defaults.getNTimesTypeInfo(hologram, false, 1, false);
        String msg2 = noDefaults.getNTimesTypeInfo(hologram, false, 1, false);
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesTypeInfo_fills_placeholders_with_info() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 0.1, 2, 5, false,
                null, 4.4, 3.3);
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
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 0.1, 2, 5, false,
                null, 4.4, 3.3);
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
        String msg1 = defaults.getNeedANumberMessage().createWith(notNumber).getFilled();
        String msg2 = noDefaults.getNeedANumberMessage().createWith(notNumber).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needAnInteger_fills_placeholders() {
        String notInt = getRandomName("msg");
        String msg1 = defaults.getNeedAnIntegerMessage().createWith(notInt).getFilled();
        String msg2 = noDefaults.getNeedAnIntegerMessage().createWith(notInt).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needCountAfterPlayercound_no_placeholders() {
        String msg1 = defaults.getNeedCountAfterPlayercount().getMessage().getFilled();
        String msg2 = noDefaults.getNeedCountAfterPlayercount().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needPairedOptions_no_placeholders() {
        String msg1 = defaults.getNeedPairedOptionsMessage().getMessage().getFilled();
        String msg2 = noDefaults.getNeedPairedOptionsMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void needTypeOrPage_fills_placeholders() {
        String spec = getRandomName("msg");
        String msg1 = defaults.getNeedTypeOrPageMessage().createWith(spec).getFilled();
        String msg2 = noDefaults.getNeedTypeOrPageMessage().createWith(spec).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void negativeTimes_fills_placeholders() {
        String times = getRandomName("msg");
        String msg1 = defaults.getNegativeTimesMessage().createWith(times).getFilled();
        String msg2 = noDefaults.getNegativeTimesMessage().createWith(times).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void nextPageHint_fills_placeholders() {
        String command = getRandomName("msg");
        String msg1 = defaults.getNextPageHint().createWith(command).getFilled();
        String msg2 = noDefaults.getNextPageHint().createWith(command).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void noLP_no_placeholders() {
        String msg1 = defaults.getNoLPMessage().getMessage().getFilled();
        String msg2 = noDefaults.getNoLPMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void noPluginFolder_no_placeholders() {
        String msg1 = defaults.getNoPluginFolderMessage().getMessage().getFilled();
        String msg2 = noDefaults.getNoPluginFolderMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void nosuchOption_fills_placeholders() {
        PeriodicType type = PeriodicType.ALWAYS;
        String notInt = getRandomName("msg");
        String msg1 = defaults.getNoSuchOptionMessage().createWith(type, notInt).getFilled();
        String msg2 = noDefaults.getNoSuchOptionMessage().createWith(type, notInt).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void nothingToUnset_no_placeholders() {
        String msg1 = defaults.getNothingToUnsetMessage().getMessage().getFilled();
        String msg2 = noDefaults.getNothingToUnsetMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesReport_fills_placeholders_no_pages() {
        String name = getRandomName("name");
        MockPlayer player = new MockPlayer(name);
        List<NTimesHologram> holos = new ArrayList<>();
        holos.add((NTimesHologram) getRandomHolgram(phd, PeriodicType.NTIMES));
        int page = 1;
        String msg1 = defaults.getNtimesReportMessage(player, holos, page, false).getFilled();
        String msg2 = noDefaults.getNtimesReportMessage(player, holos, page, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void ntimesReport_fills_placeholders_no_pages_2() {
        String name = getRandomName("name");
        MockPlayer player = new MockPlayer(name);
        List<NTimesHologram> holos = new ArrayList<>();
        holos.add((NTimesHologram) getRandomHolgram(phd, PeriodicType.NTIMES));
        holos.add((NTimesHologram) getRandomHolgram(phd, PeriodicType.NTIMES));
        int page = 1;
        String msg1 = defaults.getNtimesReportMessage(player, holos, page, false).getFilled();
        String msg2 = noDefaults.getNtimesReportMessage(player, holos, page, false).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void optionMissing_fills_placeholders() {
        PeriodicType type = PeriodicType.MCTIME;
        String option = getRandomName("option");
        String msg1 = defaults.getOptionMissingMessage().createWith(type, option).getFilled();
        String msg2 = noDefaults.getOptionMissingMessage().createWith(type, option).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void optionNotSet_fills_placeholders() {
        String option = getRandomName("option");
        String msg1 = defaults.getOptionNotSetMessage().createWith(option).getFilled();
        String msg2 = noDefaults.getOptionNotSetMessage().createWith(option).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void playerNotFound_fills_placeholders() {
        String player = getRandomName("player");
        String msg1 = defaults.getPlayerNotFoundMessage().createWith(player).getFilled();
        String msg2 = noDefaults.getPlayerNotFoundMessage().createWith(player).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void pluginFoldereRecreated_no_placeholders() {
        String msg1 = defaults.getPluginFolderRecreatedMessage().getMessage().getFilled();
        String msg2 = noDefaults.getPluginFolderRecreatedMessage().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void problemRecreatingPluginFolder_no_placeholders() {
        String msg1 = defaults.getProblemRecreatingPluginFolder().getMessage().getFilled();
        String msg2 = noDefaults.getProblemRecreatingPluginFolder().getMessage().getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void problemWithConfig_fills_placeholders() {
        String value = getRandomName("msg");
        SettingIssue issue = SettingIssue.ACTIVATION_DISTANCE;
        String msg1 = defaults.getProblemWithConfigMessage().createWith(issue, value).getFilled();
        String msg2 = noDefaults.getProblemWithConfigMessage().createWith(issue, value).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test // TODO - this is rather primitive now
    public void problemsReloadingConfig_fills_placeholders() {
        List<ReloadIssue> issues = new ArrayList<>();
        String msg1 = defaults.getProblemsReloadingConfigMessage().createWith(issues).getFilled();
        String msg2 = noDefaults.getProblemsReloadingConfigMessage().createWith(issues).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    @Test
    public void secondsTooSmall_fills_placeholders() {
        String times = getRandomName("msg");
        String msg1 = defaults.getSecondsTooSmallMessage().createWith(times).getFilled();
        String msg2 = noDefaults.getSecondsTooSmallMessage().createWith(times).getFilled();
        Assert.assertEquals(msg1, msg2);
    }

    private class DefMessages extends Messages {
        private static final String FILE_NAME = "../../../src/main/resources/messages.yml";

        protected DefMessages(MockPeriodicHolographicDisplays phd) throws InvalidConfigurationException {
            super(phd, FILE_NAME);
        }

    }

}