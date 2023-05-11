package me.ford.periodicholographicdisplays.commands;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;

import org.bukkit.command.Command;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import dev.ratas.slimedogcore.api.messaging.recipient.SDCRecipient;
import dev.ratas.slimedogcore.impl.messaging.recipient.MessageRecipient;
import dev.ratas.slimedogcore.impl.messaging.recipient.MessageRecipient;
import dev.ratas.slimedogcore.impl.wrappers.BukkitAdapter;
import me.ford.periodicholographicdisplays.commands.subcommands.ManageSub;
import me.ford.periodicholographicdisplays.commands.subcommands.SetSub;
import me.ford.periodicholographicdisplays.holograms.FlashingHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.mock.MockOPCommandSender;
import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.mock.MockPluginManager;
import me.ford.periodicholographicdisplays.mock.MockStarterUtil;

public abstract class BaseCommandTests {
    protected MockPeriodicHolographicDisplays phd;
    protected PHDCommand command;
    protected MockOPCommandSender sender;
    protected SDCRecipient recipient;

    static {
        try {
            Field field = BukkitAdapter.class.getDeclaredField("ALLOW_MINI_MESSAGES");
            field.setAccessible(true);
            field.set(null, false);
        } catch (NoSuchFieldException | IllegalAccessException | SecurityException | InaccessibleObjectException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setup() {
        MockStarterUtil.startMocking();
        phd = new MockPeriodicHolographicDisplays();
        command = new PHDCommand(phd, new MockPluginManager());
        sender = new MockOPCommandSender(null);
        // allowing mini messages at test time breaks the testss
        recipient = new MessageRecipient(sender, false);
    }

    @After
    public void tearDown() {
        phd.clear();
        MockStarterUtil.stopMocking();
    }

    protected void testSetManageCommonIllegals(FlashingHologram hologram, String holoName, PeriodicType testType,
            boolean isSet) {
        String commandName = isSet ? "set" : "manage";
        // /phd set <name>
        String usageMessage;
        PHDSubCommand cmd;
        if (isSet) {
            cmd = new SetSub(null, phd.getHolograms(), phd.getLuckPermsHook(), phd.getSettings(), phd.getMessages());
        } else {
            cmd = new ManageSub(phd);
        }
        usageMessage = cmd.getUsage(recipient, new String[] { commandName, holoName });
        String expected = usageMessage;
        testCommand(sender, null, "phd", new String[] { commandName, holoName }, expected);
        // /phd set <name> 1
        expected = phd.getMessages().getTypeNotRecognizedMessage("1");
        testCommand(sender, null, "phd", new String[] { commandName, holoName, "1" }, expected);
        // /phd set <name> <wrongtype>
        expected = cmd.getUsage(recipient, new String[] { commandName, holoName, testType.name() });
        if (!isSet && testType == PeriodicType.ALWAYS) {
            expected = phd.getMessages().getHologramAlreadyManagedMessage(holoName, testType);
        }
        testCommand(sender, null, "phd", new String[] { commandName, holoName, testType.name() }, expected);
        // /phd set <name> <type> <option> <value>
        if (isSet) {
            expected = phd.getMessages().getHologramNotTrackedMessage(holoName, testType);
        } else {
            expected = phd.getMessages().getHologramAlreadyManagedMessage(holoName, testType);
        }
        testCommand(sender, null, "phd", new String[] { commandName, holoName, testType.name(), "option", "value" },
                expected);
        // /phd set <name> <type> <option> <value> <option2> # no value
        expected = phd.getMessages().getNeedPairedOptionsMessage();
        testCommand(sender, null, "phd",
                new String[] { commandName, holoName, testType.name(), "option", "value", "option2" }, expected);
    }

    protected void testCommand(MockOPCommandSender sender, Command command, String label, String args[],
            String expectedMessage) {
        sender.setMessageConsumer((msg) -> {
            Assert.assertEquals(expectedMessage, msg);
        });
        this.command.onCommand(sender, command, label, args);
    }

}