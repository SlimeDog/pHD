package me.ford.periodicholographicdisplays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

public class MessagesTests extends TestHelpers {
    private MockPeriodicHolographicDisplays phd;
    private Messages messages;

    @Before
    public void setUp() {
        MockStarterUtil.startMocking();
        phd = new MockPeriodicHolographicDisplays();
        messages = phd.getMessages();
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
        Assert.assertNotNull(messages);
    }

    @Test
    public void activeStorageMessage_fills_placeholder() {
        String msg = messages.getActiveStorageMessage(true);
        assertContains(msg, "SQLITE");
        assertNoPlaceholder(msg);
    }

    @Test
    public void addedToCache_fills_placeholders() {
        MockPlayer player = new MockPlayer(getRandomName("name"));
        String msg = messages.getAddedToCacheMessage(player);
        assertContains(msg, player.getName(), player.getUniqueId().toString());
        assertNoPlaceholder(msg);
    }

    @Test
    public void alreadyHassData_fills_placeholders() {
        String type = getRandomName("type");
        String msg = messages.getAlreadyHasDataMessage(type, true);
        assertContains(msg, type, "database.db");
        assertNoPlaceholder(msg);
    }

    @Test
    public void alreadyHassData_fills_placeholders_2() {
        String type = getRandomName("type");
        String msg = messages.getAlreadyHasDataMessage(type, false);
        assertContains(msg, type, "database.yml");
        assertNoPlaceholder(msg);
    }

    @Test
    public void availableTypes_fills_placeholders_1() {
        String name = getRandomName("name");
        List<PeriodicType> types = new ArrayList<>();
        types.add(PeriodicType.ALWAYS);
        String msg = messages.getAvailableTypesMessage(name, types);
        assertContains(msg, name, PeriodicType.ALWAYS.name());
        assertNoPlaceholder(msg);
    }

    @Test
    public void availableTypes_fills_placeholders_2() {
        String name = getRandomName("name");
        List<PeriodicType> types = new ArrayList<>();
        types.add(PeriodicType.ALWAYS);
        types.add(PeriodicType.NTIMES);
        String msg = messages.getAvailableTypesMessage(name, types);
        assertContains(msg, name, PeriodicType.ALWAYS.name(), PeriodicType.NTIMES.name());
        assertNoPlaceholder(msg);
    }

    @Test
    public void cannotConvertSame_fills_placeholders() {
        String type = getRandomName("type");
        String msg = messages.getCannotConvertSameMessage(type);
        assertContains(msg, type);
        assertNoPlaceholder(msg);
    }

    @Test
    public void cannotUnsetRequired_fills_placeholders() {
        String option = getRandomName("option");
        PeriodicType type = PeriodicType.MCTIME;
        String msg = messages.getCannotUnSetRequiredMessage(option, type);
        assertContains(msg);
        assertNoPlaceholder(msg);
    }

    @Test
    public void configRecreated_no_placeholders() {
        String msg = messages.getConfigRecreatedMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void configReloaded_no_placeholders() {
        String msg = messages.getConfigReloadedMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void disable_no_placeholders() {
        String msg = messages.getDisablingMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void distanceTooSmall_fills_placeholders() {
        String dist = getRandomName("dist");
        String msg = messages.getDistanceTooSmallMessage(dist);
        assertContains(msg, dist);
        assertNoPlaceholder(msg);
    }

    @Test
    public void doneConverting_fills_placeholders() {
        String from = getRandomName("from");
        String to = getRandomName("to");
        String msg = messages.getDoneConvertingMessage(from, to);
        assertContains(msg, from, to);
        assertNoPlaceholder(msg);
    }

    @Test
    public void flashMustHaveBoth_fills_placeholders() {
        String msg = messages.getFlashMustHaveBothMessage(null); // irrelevant argument
        assertNoPlaceholder(msg);
    }

    @Test
    public void flashTimeTooSmall_fills_placeholders() {
        String specified = getRandomName("spec");
        String msg = messages.getFlashTimeTooSmallMessage(specified);
        assertContains(msg, specified);
        assertNoPlaceholder(msg);
    }

    @Test
    public void hdHologramNotFound_fills_placeholders() {
        String name = getRandomName("name");
        String msg = messages.getHDHologramNotFoundMessage(name);
        assertContains(msg, name);
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramAlreadyManaged_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.IRLTIME;
        String msg = messages.getHologramAlreadyManagedMessage(name, type);
        assertContains(msg, name, type.name());
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramInfo_fills_placeholders_no_pages_no_perms() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                null, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg = messages.getHologramInfoMessage(hologram, 1, false);
        assertContains(msg, name, hologram.getActivationDistance(), hologram.getShowTime(),
                holo.getPosition().toLocation());
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramInfo_fills_placeholders_no_pages_with_perms() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                perms, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg = messages.getHologramInfoMessage(hologram, 1, false);
        assertContains(msg, name, perms, hologram.getActivationDistance(), hologram.getShowTime(),
                holo.getPosition().toLocation());
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_no_perms() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                null, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg = messages.getHologramInfoMessage(hologram, 1, false);
        assertContains(msg, name, hologram.getActivationDistance(), hologram.getShowTime(),
                holo.getPosition().toLocation());
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramInfo_fills_placeholders_with_pages_with_perms() {
        String name = getRandomName("name");
        String perms = getRandomName();
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(holo), name, 5.55, 4, false,
                perms, FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg = messages.getHologramInfoMessage(hologram, 1, false);
        assertContains(msg, name, perms, hologram.getActivationDistance(), hologram.getShowTime(),
                holo.getPosition().toLocation());
        assertNoPlaceholder(msg);
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
        String msg = messages.getHologramInfoMessage(hologram, 1, false);
        assertContains(msg, name, perms, hologram.getActivationDistance(), hologram.getShowTime(),
                holo.getPosition().toLocation(),
                flashOn, flashOff);
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramList_fills_placeholders() {
        String name = getRandomName("name");
        String types = getRandomName("types");
        Map<String, String> holograms = new HashMap<>();
        holograms.put(name, types);
        int page = 1;
        String msg = messages.getHologramListMessage(holograms, page, false);
        assertContains(msg, name, types, page);
        assertNoPlaceholder(msg);
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
        String msg = messages.getHologramListMessage(holograms, page, false);
        assertContains(msg, name1, types1, name2, types2, page);
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramNotFound_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.IRLTIME;
        String msg = messages.getHologramNotFoundMessage(name, type);
        assertContains(msg, name, type.name());
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramNotManaged_fills_placeholders() {
        String name = getRandomName("name");
        String msg = messages.getHologramNotManagedMessage(name);
        assertContains(msg, name);
        assertNoPlaceholder(msg);
    }

    @Test
    public void hologramNotTracked_fills_placeholders() {
        String name = getRandomName("name");
        PeriodicType type = PeriodicType.MCTIME;
        String msg = messages.getHologramNotTrackedMessage(name, type);
        assertContains(msg, name, type.name());
        assertNoPlaceholder(msg);
    }

    @Test
    public void illgealStorage_fills_placeholders() {
        String sType = getRandomName("type");
        String msg = messages.getIllegalStorageMessage(sType);
        assertContains(msg, sType);
        assertNoPlaceholder(msg);
    }

    @Test
    public void illegalTime_fills_placeholders() {
        String time = getRandomName("time");
        String msg = messages.getIllegalTimeMessage(time);
        assertContains(msg, time);
        assertNoPlaceholder(msg);
    }

    @Test
    public void incorrectMessages_no_placeholders() {
        String msg = messages.getIncorrectMessages();
        assertNoPlaceholder(msg);
    }

    @Test
    public void incorrectTime_fills_placeholders() {
        String timeMsg = getRandomName("msg");
        String msg = messages.getIncorrectTimeMessage(timeMsg);
        assertContains(msg, timeMsg);
        assertNoPlaceholder(msg);
    }

    @Test
    public void invalidPage_fills_placeholders() {
        int maxPage = 10;
        String msg = messages.getInvalidPageMessage(maxPage);
        assertContains(msg, maxPage);
        assertNoPlaceholder(msg);
    }

    @Test
    public void legacy_no_placeholders() {
        String msg = messages.getLegacyMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void lowSaveDelay_fills_placeholders() {
        int seconds = 18;
        String msg = messages.getLowSaveDelayMessage(seconds);
        assertContains(msg, seconds);
        assertNoPlaceholder(msg);
    }

    @Test
    public void messagesRecreated_no_placeholders() {
        String msg = messages.getMessagesRecreatedMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_1() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 7.6, 9, 3, false,
                "s.perms", 4.4, 3.3);
        String msg = messages.getNTimesTypeInfo(hologram, false, 1, false);
        assertNoPlaceholder(msg);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_2() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 6.7, 8, 3, false,
                "s.perms",
                FlashingHologram.NO_FLASH, FlashingHologram.NO_FLASH);
        String msg = messages.getNTimesTypeInfo(hologram, false, 1, false);
        assertNoPlaceholder(msg);
    }

    @Test
    public void ntimesTypeInfo_no_placeholders_3() {
        String name = getRandomName("name");
        MockNamedHologram holo = new MockNamedHologram(phd.api, name);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(holo), name, 1.2, 5, 3, false,
                "s.perms", 4.4, 3.3);
        String msg = messages.getNTimesTypeInfo(hologram, false, 1, false);
        assertNoPlaceholder(msg);
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

        String msg = messages.getNTimesTypeInfo(hologram, false, 1, false);
        assertContains(msg, times);
        assertNoPlaceholder(msg);
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

        String msg = messages.getNTimesTypeInfo(hologram, false, 1, false);
        assertContains(msg, times1, times2);
        assertNoPlaceholder(msg);
    }

    @Test
    public void needANumber_fills_placeholders() {
        String notNumber = getRandomName("msg");
        String msg = messages.getNeedANumberMessage(notNumber);
        assertContains(msg, notNumber);
        assertNoPlaceholder(msg);
    }

    @Test
    public void needAnInteger_fills_placeholders() {
        String notInt = getRandomName("msg");
        String msg = messages.getNeedAnIntegerMessage(notInt);
        assertContains(msg, notInt);
        assertNoPlaceholder(msg);
    }

    @Test
    public void needCountAfterPlayercound_no_placeholders() {
        String msg = messages.getNeedCountAfterPlayercount();
        assertNoPlaceholder(msg);
    }

    @Test
    public void needPairedOptions_no_placeholders() {
        String msg = messages.getNeedPairedOptionsMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void needTypeOrPage_fills_placeholders() {
        String spec = getRandomName("msg");
        String msg = messages.getNeedTypeOrPageMessage(spec);
        assertContains(msg, spec);
        assertNoPlaceholder(msg);
    }

    @Test
    public void negativeTimes_fills_placeholders() {
        String times = getRandomName("msg");
        String msg = messages.getNegativeTimesMessage(times);
        assertContains(msg, times);
        assertNoPlaceholder(msg);
    }

    @Test
    public void nextPageHint_fills_placeholders() {
        String command = getRandomName("msg");
        String msg = messages.getNextPageHint(command);
        assertContains(msg, command);
        assertNoPlaceholder(msg);
    }

    @Test
    public void noLP_no_placeholders() {
        String msg = messages.getNoLPMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void noPluginFolder_no_placeholders() {
        String msg = messages.getNoPluginFolderMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void nosuchOption_fills_placeholders() {
        PeriodicType type = PeriodicType.ALWAYS;
        String notInt = getRandomName("msg");
        String msg = messages.getNoSuchOptionMessage(type, notInt);
        assertContains(msg, notInt, type.name());
        assertNoPlaceholder(msg);
    }

    @Test
    public void nothingToUnset_no_placeholders() {
        String msg = messages.getNothingToUnsetMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void ntimesReport_fills_placeholders_no_pages() {
        String name = getRandomName("name");
        MockPlayer player = new MockPlayer(name);
        List<NTimesHologram> holos = new ArrayList<>();
        holos.add((NTimesHologram) getRandomHolgram(phd, PeriodicType.NTIMES));
        int page = 1;
        int maxPage = 1;
        int seen = 0;
        String msg = messages.getNtimesReportMessage(player, holos, page, false);
        assertContains(msg, name, holos.size(), page, maxPage, holos.get(0).getName(), seen,
                holos.get(0).getTimesToShow());
        assertNoPlaceholder(msg);
    }

    @Test
    public void ntimesReport_fills_placeholders_no_pages_2() {
        String name = getRandomName("name");
        MockPlayer player = new MockPlayer(name);
        List<NTimesHologram> holos = new ArrayList<>();
        holos.add((NTimesHologram) getRandomHolgram(phd, PeriodicType.NTIMES));
        holos.add((NTimesHologram) getRandomHolgram(phd, PeriodicType.NTIMES));
        int page = 1;
        int maxPage = 1;
        int seen = 0;
        String msg = messages.getNtimesReportMessage(player, holos, page, false);
        assertContains(msg, name, holos.size(), page, maxPage, holos.get(0).getName(), holos.get(1).getName(), seen,
                holos.get(0).getTimesToShow(), holos.get(1).getTimesToShow());
        assertNoPlaceholder(msg);
    }

    @Test
    public void optionMissing_fills_placeholders() {
        PeriodicType type = PeriodicType.MCTIME;
        String option = getRandomName("option");
        String msg = messages.getOptionMissingMessage(type, option);
        assertContains(msg, option, type.name());
        assertNoPlaceholder(msg);
    }

    @Test
    public void optionNotSet_fills_placeholders() {
        String option = getRandomName("option");
        String msg = messages.getOptionNotSetMessage(option);
        assertContains(msg, option);
        assertNoPlaceholder(msg);
    }

    @Test
    public void playerNotFound_fills_placeholders() {
        String player = getRandomName("player");
        String msg = messages.getPlayerNotFoundMessage(player);
        assertContains(msg, player);
        assertNoPlaceholder(msg);
    }

    @Test
    public void pluginFoldereRecreated_no_placeholders() {
        String msg = messages.getPluginFolderRecreatedMessage();
        assertNoPlaceholder(msg);
    }

    @Test
    public void problemRecreatingPluginFolder_no_placeholders() {
        String msg = messages.getProblemRecreatingPluginFolder();
        assertNoPlaceholder(msg);
    }

    @Test
    public void problemWithConfig_fills_placeholders() {
        String value = getRandomName("msg");
        SettingIssue issue = SettingIssue.ACTIVATION_DISTANCE;
        String msg = messages.getProblemWithConfigMessage(issue, value);
        assertContains(msg, value, issue.getPath(), issue.getType().getName());
        assertNoPlaceholder(msg);
    }

    @Test // TODO - this is rather primitive now
    public void problemsReloadingConfig_fills_placeholders() {
        List<ReloadIssue> issues = new ArrayList<>();
        String msg = messages.getProblemsReloadingConfigMessage(issues);
        assertContains(msg, issues);
        assertNoPlaceholder(msg);
    }

    @Test
    public void secondsTooSmall_fills_placeholders() {
        String times = getRandomName("msg");
        String msg = messages.getSecondsTooSmallMessage(times);
        assertContains(msg, times);
        assertNoPlaceholder(msg);
    }

}