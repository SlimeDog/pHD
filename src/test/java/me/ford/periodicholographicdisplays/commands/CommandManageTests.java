package me.ford.periodicholographicdisplays.commands;

import org.junit.Test;

import me.ford.periodicholographicdisplays.commands.subcommands.ManageSub;
import me.ford.periodicholographicdisplays.holograms.AlwaysHologram;
import me.ford.periodicholographicdisplays.holograms.IRLTimeHologram;
import me.ford.periodicholographicdisplays.holograms.MCTimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.wrap.HolographicDisplaysWrapper;
import me.ford.periodicholographicdisplays.mock.MockNamedHologram;

public class CommandManageTests extends BaseCommandTests {
    // USAGE = "/phd manage <hologram> <type> times <integer> time <hh:mm>
    // [<options...>]";

    @Test
    public void testManageIllegal() {
        // phd manage NonExisting
        String name = "hdHologram";
        String expectedMessage = phd.getMessages().getHDHologramNotFoundMessage(name);
        testCommand(sender, null, "phd", new String[] { "manage", name }, expectedMessage);
        // phd manage existing
        // add mock hologram
        phd.putHDHologram(name, new MockNamedHologram(phd.api, name));
        ManageSub ms = new ManageSub(phd);
        String usageMessage = ms.getUsage(recipient, new String[] {});
        expectedMessage = usageMessage;
        testCommand(sender, null, "phd", new String[] { "manage", name }, expectedMessage);

        // phd manage existing <incorrectType>
        String fakeType = "FAKE";
        expectedMessage = phd.getMessages().getTypeNotRecognizedMessage(fakeType);
        testCommand(sender, null, "phd", new String[] { "manage", name, fakeType }, expectedMessage);

        // phd manage existing <type> # not ALWAYS
        PeriodicType type = PeriodicType.NTIMES;
        expectedMessage = ms.getUsage(recipient, new String[] { "manage", name, type.name() });
        testCommand(sender, null, "phd", new String[] { "manage", name, type.name() }, expectedMessage);

        // phd manage existing <type> <option> <value> <option_with_no_value> # not
        // ALWAYS
        expectedMessage = phd.getMessages().getNeedPairedOptionsMessage();
        testCommand(sender, null, "phd", new String[] { "manage", name, type.name(), "opt", "val", "opt2" },
                expectedMessage);

        // phd manage existing <type> <option> <value> # hologram already managed
        NTimesHologram hologram = new NTimesHologram(phd, phd.getHologramProvider().getByName(name), name, 2, 4, 5,
                true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        expectedMessage = phd.getMessages().getHologramAlreadyManagedMessage(name, type);
        testCommand(sender, null, "phd", new String[] { "manage", name, type.name(), "opt", "val" }, expectedMessage);
    }

    @Test
    public void testManageIllegalNtimes() {
        String name = "hdHol0gram";
        MockNamedHologram mnh = new MockNamedHologram(phd.api, name);
        phd.putHDHologram(name, mnh);
        NTimesHologram hologram = new NTimesHologram(phd, new HolographicDisplaysWrapper(mnh), name, 2.2, 6, 4, true,
                "super.perms", 4, 2);
        phd.getHolograms().addHologram(hologram);
        this.testSetManageCommonIllegals(hologram, name, PeriodicType.NTIMES, false);
    }

    @Test
    public void testManageIllegalAlways() {
        String name = "ALW4YS";
        MockNamedHologram mnh = new MockNamedHologram(phd.api, name);
        phd.putHDHologram(name, mnh);
        AlwaysHologram hologram = new AlwaysHologram(phd, new HolographicDisplaysWrapper(mnh), name, 2.2, 6, true, null,
                4, 2);
        phd.getHolograms().addHologram(hologram);
        this.testSetManageCommonIllegals(hologram, name, PeriodicType.ALWAYS, false);
    }

    @Test
    public void testManageIllegalIRLTime() {
        String name = "4realTime";
        MockNamedHologram mnh = new MockNamedHologram(phd.api, name);
        phd.putHDHologram(name, mnh);
        IRLTimeHologram hologram = new IRLTimeHologram(phd, new HolographicDisplaysWrapper(mnh), name, 5.1, 3, 26440,
                true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        this.testSetManageCommonIllegals(hologram, name, PeriodicType.IRLTIME, false);
    }

    @Test
    public void testManageIllegalMCTime() {
        String name = "mcTimeWeHave";
        MockNamedHologram mnh = new MockNamedHologram(phd.api, name);
        phd.putHDHologram(name, mnh);
        MCTimeHologram hologram = new MCTimeHologram(phd, new HolographicDisplaysWrapper(mnh), name, 5.1, 3, 15000,
                true, null, -1, -1);
        phd.getHolograms().addHologram(hologram);
        this.testSetManageCommonIllegals(hologram, name, PeriodicType.MCTIME, false);
    }

}