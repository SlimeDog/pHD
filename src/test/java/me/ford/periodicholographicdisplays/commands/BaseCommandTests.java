package me.ford.periodicholographicdisplays.commands;

import org.bukkit.command.Command;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

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

    protected void testCommand(MockOPCommandSender sender, Command command, String label, String args[], String expectedMessage) {
        sender.setMessageConsumer((msg) -> {
            Assert.assertEquals(expectedMessage, msg);
        });
        this.command.onCommand(sender, command, label, args);
    }

}