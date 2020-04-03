package me.ford.periodicholographicdisplays.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ford.periodicholographicdisplays.commands.subcommands.SetSub;
import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.IRLTimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.mock.MockHologram;
import me.ford.periodicholographicdisplays.mock.MockOPCommandSender;
import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.mock.MockPluginManager;
import me.ford.periodicholographicdisplays.util.TimeUtils;

public class CommandTests {
    private MockPeriodicHolographicDisplays phd;
    private PHDCommand command;
    private MockOPCommandSender sender;

    @Before
    public void setup() {
        phd = new MockPeriodicHolographicDisplays();
        command = new PHDCommand(phd, new MockPluginManager());
        sender = new MockOPCommandSender(null);
    }

    @After
    public void tearDown() {
        phd.clear();
    }

    @Test
    public void testUsagePageOne() {
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(command.getUsage(sender, 1).usage, msg);
        });
        command.onCommand(sender, null, "phd", new String[] {});
        command.onCommand(sender, null, "phd", new String[] { "1" });
        command.onCommand(sender, null, "phd", new String[] { "help", "1" });
    }

    @Test
    public void testUsagePageTwo() {
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(command.getUsage(sender, 2).usage, msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "2" });
        command.onCommand(sender, null, "phd", new String[] { "help", "2" });
    }

    @Test
    public void testSetCommand() {
        // /phd set
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(
                    new SetSub(phd.getHolograms(), null, phd.getSettings(), phd.getMessages()).getUsage(sender), msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "set" });
        // /phd set 1
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(phd.getMessages().getHologramNotManagedMessage("1"), msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "set", "1" });
    }

    private void testCommand(MockOPCommandSender sender, Command command, String label, String args[], String expectedMessage) {
        sender.setMessageConsumer((msg) -> {
            Assert.assertEquals(expectedMessage, msg);
        });
        this.command.onCommand(sender, command, label, args);
    }

    private void testSetCommonIllegals(FlashingHologram hologram, String holoName, PeriodicType wrongType) {
        // /phd set <name>
        String expected = new SetSub(phd.getHolograms(), null, phd.getSettings(), phd.getMessages()).getUsage(sender);
        testCommand(sender, null, "phd", new String[] { "set", holoName}, expected);
        // /phd set <name> 1
        expected = phd.getMessages().getTypeNotRecognizedMessage("1");
        testCommand(sender, null, "phd", new String[] { "set", holoName, "1"}, expected);
        // /phd set <name> <wrongtype>
        expected = new SetSub(phd.getHolograms(), phd.getLuckPermsHook(), phd.getSettings(),
                    phd.getMessages()).getUsage(sender);
        testCommand(sender, null, "phd", new String[] { "set", holoName, wrongType.name()}, expected);
        // /phd set <name> <type> <option> <value>
        expected = phd.getMessages().getHologramNotTrackedMessage(holoName, wrongType);
        testCommand(sender, null, "phd", new String[] { "set", holoName, wrongType.name(), "option", "value"}, expected);
        // /phd set <name> <type> <option> <value> <option2> # no value
        expected = phd.getMessages().getNeedPairedOptionsMessage();
        testCommand(sender, null, "phd", new String[] {"set", holoName, wrongType.name(), "option", "value", "option2"}, expected);
    }

    @Test
    public void testSetCommand2() {
        String holoName = "ALWAYSNAME";
        AlwaysHologram hologram = new AlwaysHologram(phd, new MockHologram(), holoName, 1.0, -1, true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        testSetCommonIllegals(hologram, holoName, PeriodicType.NTIMES);
    }

    @Test
    public void testSetIllegal() {
        String holoName = "ALWAYSNAME";
        AlwaysHologram hologram = new AlwaysHologram(phd, new MockHologram(), holoName, 1.0, -1, true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        // /phd set <name> <type> times <times>
        String expected = phd.getMessages().getNoSuchOptionMessage(PeriodicType.ALWAYS, "times");
        testCommand(sender, null, "phd", new String[] { "set", holoName, PeriodicType.ALWAYS.name(), "times", "4"}, expected);
        // /phd set <name> <type> times <times>
        expected = phd.getMessages().getNoSuchOptionMessage(PeriodicType.ALWAYS, "time");
        testCommand(sender, null, "phd", new String[] { "set", holoName, PeriodicType.ALWAYS.name(), "time", "14:14"}, expected);
    }

    @Test
    public void testSetAlwaysLegal() {

        String holoName = "ALWAYSNAME";
        PeriodicType type = PeriodicType.ALWAYS;
        AlwaysHologram hologram = new AlwaysHologram(phd, new MockHologram(), holoName, 1.0, -1, true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        testSetForHologramType(hologram, holoName, type);
    }

    private void testSetForHologramType(FlashingHologram hologram, String holoName, PeriodicType type) {
        // /phd set <name> <type> distance 5
        Map<String, String> options = new HashMap<>();
        double distance = 5;
        String dist = String.valueOf(distance);
        options.put("distance", dist);
        String expected = phd.getMessages().getSetNewOptionsMessage(holoName, type, options);
        testCommand(sender, null, "phd", new String[] { "set", holoName, type.name(), "distance", dist}, expected);
        Assert.assertEquals(distance, hologram.getActivationDistance(), 0.01);
        // /phd set <name> <type> seconds <seconds>
        int seconds = 3;
        String secs = String.valueOf(seconds);
        options.clear();
        options.put("seconds", secs);
        expected = phd.getMessages().getSetNewOptionsMessage(holoName, type, options);
        testCommand(sender, null, "phd", new String[] { "set", holoName, type.name(), "seconds", secs}, expected);
        Assert.assertEquals(seconds, hologram.getShowTime());

        // multiple
        // /phd set <name> <type> seconds <seconds> distance <distance>
        options.clear();
        distance = 15.5;
        dist = String.valueOf(distance);
        seconds = 9;
        secs = String.valueOf(seconds);

        options.put("distance", dist);
        options.put("seconds", secs);
        expected = phd.getMessages().getSetNewOptionsMessage(holoName, type, options);
        testCommand(sender, null, "phd", new String[] { "set", holoName, type.name(), "seconds", secs, "distance", dist}, expected);
        Assert.assertEquals(seconds, hologram.getShowTime());
        Assert.assertEquals(seconds, hologram.getShowTime());
    }

    @Test
    public void testSetNtimesIllegal() {
        String holoName = "Nt0imeSName";
        NTimesHologram hologram = new NTimesHologram(phd, new MockHologram(), holoName, 3.0, 10, 5, true, null, 1.2, 1.3);
        phd.getHolograms().addHologram(hologram);
        testSetCommonIllegals(hologram, holoName, PeriodicType.ALWAYS);
    }

    @Test
    public void testSetNtimesLegal() {

        String holoName = "NtimeSName";
        PeriodicType type = PeriodicType.NTIMES;
        NTimesHologram hologram = new NTimesHologram(phd, new MockHologram(), holoName, 3.0, 10, 5, true, null, 1.2, 1.3);
        phd.getHolograms().addHologram(hologram);
        testSetForHologramType(hologram, holoName, type);

        // specific
        // /phd set <name> <type> times 9
        int times = 3;
        String tms = String.valueOf(times);
        Map<String, String> options = new HashMap<>();
        options.clear();
        options.put("times", tms);
        String expected = phd.getMessages().getSetNewOptionsMessage(holoName, type, options);
        testCommand(sender, null, "phd", new String[] { "set", holoName, type.name(), "times", tms}, expected);
        Assert.assertEquals(times, hologram.getTimesToShow());
    }

    @Test
    public void testSetIRLMTimeIllegal() {
        String holoName = "iRl";
        IRLTimeHologram hologram = new IRLTimeHologram(phd, new MockHologram(), holoName, 3.0, 10, 1250, true, null, 1.2, 1.3);
        phd.getHolograms().addHologram(hologram);
        testSetCommonIllegals(hologram, holoName, PeriodicType.ALWAYS);
    }

    @Test
    public void testSetRLMTimeLegal() {

        String holoName = "inRealLife";
        PeriodicType type = PeriodicType.IRLTIME;
        IRLTimeHologram hologram = new IRLTimeHologram(phd, new MockHologram(), holoName, 3.0, 4, 2400, true, null, 1.2, 1.3);
        phd.getHolograms().addHologram(hologram);
        testSetForHologramType(hologram, holoName, type);

        // specific
        // /phd set <name> <type> time 15:40
        String time = "15:40";
        Map<String, String> options = new HashMap<>();
        options.clear();
        options.put("time", time);
        String expected = phd.getMessages().getSetNewOptionsMessage(holoName, type, options);
        testCommand(sender, null, "phd", new String[] { "set", holoName, type.name(), "time", time}, expected);
        Assert.assertEquals(TimeUtils.parseHoursAndMinutesToSeconds(time), hologram.getTime());
    }

}