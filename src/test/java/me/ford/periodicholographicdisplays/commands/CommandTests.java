package me.ford.periodicholographicdisplays.commands;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ford.periodicholographicdisplays.commands.subcommands.SetSub;
import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.mock.MockHologram;
import me.ford.periodicholographicdisplays.mock.MockOPCommandSender;
import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.mock.MockPluginManager;

public class CommandTests {
    private MockPeriodicHolographicDisplays phd;
    private PHDCommand command;
    private MockOPCommandSender sender;

    @Before
    public void setup() {
        phd = new MockPeriodicHolographicDisplays();
        command = new PHDCommand(phd, new MockPluginManager());
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

    @Test
    public void testSetCommand2() {
        String holoName = "ALWAYSNAME";
        AlwaysHologram hologram = new AlwaysHologram(phd, new MockHologram(), holoName, 1.0, -1, true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        // /phd set <name>
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(
                    new SetSub(phd.getHolograms(), null, phd.getSettings(), phd.getMessages()).getUsage(sender), msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "set", holoName });
        // /phd set 1
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(phd.getMessages().getTypeNotRecognizedMessage("1"), msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "set", holoName, "1" });
        // /phd set <name> <wrongtype>
        PeriodicType type = PeriodicType.NTIMES;
        sender = new MockOPCommandSender((msg) -> {
            String expected = new SetSub(phd.getHolograms(), phd.getLuckPermsHook(), phd.getSettings(),
                    phd.getMessages()).getUsage(sender);
            Assert.assertEquals(expected, msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "set", holoName, type.name() });
        // /phd set <name> <wrongtype> <option> <value>
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(phd.getMessages().getHologramNotTrackedMessage(holoName, type), msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "set", holoName, type.name(), "option", "value" });
    }

}