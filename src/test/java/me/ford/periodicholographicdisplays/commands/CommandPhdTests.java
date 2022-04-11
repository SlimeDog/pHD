package me.ford.periodicholographicdisplays.commands;

import org.junit.Assert;
import org.junit.Test;

import dev.ratas.slimedogcore.impl.messaging.recipient.PlayerRecipient;
import me.ford.periodicholographicdisplays.mock.MockOPCommandSender;
import me.ford.periodicholographicdisplays.mock.MockPlayer;

public class CommandPhdTests extends BaseCommandTests {

    @Test
    public void testUsagePageOne() {
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(command.getUsage(recipient), msg);
        });
        command.onCommand(sender, null, "phd", new String[] {});
        command.onCommand(sender, null, "phd", new String[] { "1" });
        command.onCommand(sender, null, "phd", new String[] { "help", "1" });
    }

    @Test
    public void testUsagePageTwo() {
        sender = new MockOPCommandSender((msg) -> {
            Assert.assertEquals(command.getUsage(recipient), msg);
        });
        command.onCommand(sender, null, "phd", new String[] { "2" });
        command.onCommand(sender, null, "phd", new String[] { "help", "2" });
    }

    @Test
    public void testUsagePageOne_player() {
        MockPlayer player = new MockPlayer("mocKpL4yer");
        player.setMessageConsumer((msg) -> {
            Assert.assertEquals(command.getUsage(new PlayerRecipient(player)), msg);
        });
        command.onCommand(player, null, "phd", new String[] {});
        command.onCommand(player, null, "phd", new String[] { "1" });
        command.onCommand(player, null, "phd", new String[] { "help", "1" });
    }

    @Test
    public void testUsagePageTwo_player() {
        MockPlayer player = new MockPlayer("mocKpL4yer");
        player.setMessageConsumer((msg) -> {
            Assert.assertEquals(command.getUsage(new PlayerRecipient(player)), msg);
        });
        command.onCommand(player, null, "phd", new String[] { "2" });
        command.onCommand(player, null, "phd", new String[] { "help", "2" });
    }

}