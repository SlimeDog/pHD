package me.ford.periodicholographicdisplays.commands;

import org.bukkit.command.Command;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import me.ford.periodicholographicdisplays.commands.subcommands.ManageSub;
import me.ford.periodicholographicdisplays.commands.subcommands.SetSub;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.mock.MockOPCommandSender;
import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.mock.MockPluginManager;

public abstract class BaseCommandTests {
    protected MockPeriodicHolographicDisplays phd;
    protected PHDCommand command;
    protected MockOPCommandSender sender;

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

    protected void testSetManageCommonIllegals(FlashingHologram hologram, String holoName, PeriodicType testType, boolean isSet) {
        String commandName = isSet ? "set" : "manage";
        // /phd set <name>
        String usageMessage;
        if (isSet) {
            usageMessage = new SetSub(phd.getHolograms(), phd.getLuckPermsHook(), phd.getSettings(), phd.getMessages()).getUsage(sender);
        } else {
            usageMessage = new ManageSub(phd).getUsage(sender);
        }
        String expected = usageMessage;
        testCommand(sender, null, "phd", new String[] {commandName, holoName}, expected);
        // /phd set <name> 1
        expected = phd.getMessages().getTypeNotRecognizedMessage("1");
        testCommand(sender, null, "phd", new String[] {commandName, holoName, "1"}, expected);
        // /phd set <name> <wrongtype>
        expected = usageMessage;
        testCommand(sender, null, "phd", new String[] {commandName, holoName, testType.name()}, expected);
        // /phd set <name> <type> <option> <value>
        if (isSet) {
            expected = phd.getMessages().getHologramNotTrackedMessage(holoName, testType);
        } else {
            expected = phd.getMessages().getHologramAlreadyManagedMessage(holoName, testType);
        }
        testCommand(sender, null, "phd", new String[] {commandName, holoName, testType.name(), "option", "value"}, expected);
        // /phd set <name> <type> <option> <value> <option2> # no value
        expected = phd.getMessages().getNeedPairedOptionsMessage();
        testCommand(sender, null, "phd", new String[] {commandName, holoName, testType.name(), "option", "value", "option2"}, expected);
    }

    protected void testCommand(MockOPCommandSender sender, Command command, String label, String args[], String expectedMessage) {
        sender.setMessageConsumer((msg) -> {
            Assert.assertEquals(expectedMessage, msg);
        });
        this.command.onCommand(sender, command, label, args);
    }

}