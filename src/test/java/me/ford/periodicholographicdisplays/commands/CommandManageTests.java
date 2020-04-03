package me.ford.periodicholographicdisplays.commands;

import org.junit.Test;

import me.ford.periodicholographicdisplays.commands.subcommands.ManageSub;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.mock.MockNamedHologram;

public class CommandManageTests extends BaseCommandTests {
    // USAGE = "/phd manage <hologram> <type> times <integer> time <hh:mm> [<options...>]";

    @Test
    public void testManageIllegal() {
        // phd manage NonExisting
        String name = "hdHologram";
        String expectedMessage = phd.getMessages().getHDHologramNotFoundMessage(name);
        testCommand(sender, null, "phd", new String[] {"manage", name}, expectedMessage);
        // phd manage existing
        // add mock hologram
        phd.putHDHologram(name, new MockNamedHologram(name));
        String usageMessage = new ManageSub(phd).getUsage(sender);
        expectedMessage = usageMessage;
        testCommand(sender, null, "phd", new String[] {"manage", name}, expectedMessage);

        // phd manage existing <incorrectType>
        String fakeType = "FAKE";
        expectedMessage = phd.getMessages().getTypeNotRecognizedMessage(fakeType);
        testCommand(sender, null, "phd", new String[] {"manage", name, fakeType}, expectedMessage);


        // phd manage existing <type> # not ALWAYS
        PeriodicType type = PeriodicType.NTIMES;
        expectedMessage = usageMessage;
        testCommand(sender, null, "phd", new String[] {"manage", name, type.name()}, expectedMessage);

        // phd manage existing <type> <option> <value> <option_with_no_value> # not ALWAYS
        expectedMessage = phd.getMessages().getNeedPairedOptionsMessage();
        testCommand(sender, null, "phd", new String[] {"manage", name, type.name(), "opt", "val", "opt2"}, expectedMessage);

        // phd manage existing <type> <option> <value> # hologram already managed
        NTimesHologram hologram = new NTimesHologram(phd, phd.getHDHologram(name), name, 2, 4, 5, true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        expectedMessage = phd.getMessages().getHologramAlreadyManagedMessage(name, type);
        testCommand(sender, null, "phd", new String[] {"manage", name, type.name(), "opt", "val"}, expectedMessage);
    }

    @Test
    public void testManageIllegalGeneral() {
        String name = "hdHol0gram";
        MockNamedHologram mnh = new MockNamedHologram(name);
        phd.putHDHologram(name, mnh);
        NTimesHologram hologram = new NTimesHologram(phd, mnh, name, 2.2, 6, 4, true, "super.perms", 4, 2);
        phd.getHolograms().addHologram(hologram);
        this.testSetManageCommonIllegals(hologram, name, PeriodicType.NTIMES, false);
    }

}