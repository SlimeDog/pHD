package me.ford.periodicholographicdisplays.commands;

import org.bukkit.command.Command;
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

    private void testCommand(MockOPCommandSender sender, Command command, String label, String args[], String expectedMessage) {
        sender.setMessageConsumer((msg) -> {
            Assert.assertEquals(expectedMessage, msg);
        });
        this.command.onCommand(sender, command, label, args);
    }

    @Test
    public void testSetCommand2() {
        String holoName = "ALWAYSNAME";
        AlwaysHologram hologram = new AlwaysHologram(phd, new MockHologram(), holoName, 1.0, -1, true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        // /phd set <name>
        sender = new MockOPCommandSender(null);
        String expected = new SetSub(phd.getHolograms(), null, phd.getSettings(), phd.getMessages()).getUsage(sender);
        testCommand(sender, null, "phd", new String[] { "set", holoName}, expected);
        // /phd set <name> 1
        expected = phd.getMessages().getTypeNotRecognizedMessage("1");
        testCommand(sender, null, "phd", new String[] { "set", holoName, "1"}, expected);
        // /phd set <name> <wrongtype>
        expected = new SetSub(phd.getHolograms(), phd.getLuckPermsHook(), phd.getSettings(),
                    phd.getMessages()).getUsage(sender);
        testCommand(sender, null, "phd", new String[] { "set", holoName, PeriodicType.NTIMES.name()}, expected);
        // /phd set <name> <type> <option> <value>
        expected = phd.getMessages().getHologramNotTrackedMessage(holoName, PeriodicType.NTIMES);
        testCommand(sender, null, "phd", new String[] { "set", holoName, PeriodicType.NTIMES.name(), "option", "value"}, expected);
        // /phd set <name> <type> <option> <value> <option2> # no value
        expected = phd.getMessages().getNeedPairedOptionsMessage();
        testCommand(sender, null, "phd", new String[] {"set", holoName, PeriodicType.NTIMES.name(), "option", "value", "option2"}, expected);
    }

    @Test
    public void testSetIllegal() {
        sender = new MockOPCommandSender(null);

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

}