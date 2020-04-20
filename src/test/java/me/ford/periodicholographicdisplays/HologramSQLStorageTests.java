package me.ford.periodicholographicdisplays;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ford.periodicholographicdisplays.holograms.HologramStorage;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.holograms.storage.HologramInfo;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.NTimesTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.IRLTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.TypeInfo.MCTimeTypeInfo;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;
import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;

/**
 * Test1
 */
public class HologramSQLStorageTests {
    private MockPeriodicHolographicDisplays phd;

    @Before
    public void setUp() {
        phd = new MockPeriodicHolographicDisplays();
    }

    @After
    public void tearDown() {
        phd.clear();
    }

    @Test
    public void testMethods() {
        Assert.assertNotNull("phd.getHolograms() is null!", phd.getHolograms());
        Assert.assertNotNull("phd.getUserCache() is null!", phd.getUserCache());
        Assert.assertNotNull("phd.getConfig() is null!", phd.getConfig());
        Assert.assertNotNull("phd.getDataFolder() is null!", phd.getDataFolder());
        Assert.assertNotNull("phd.getLogger() is null!", phd.getLogger());
        Assert.assertNotNull("phd.getMessages() is null!", phd.getMessages());
        Assert.assertNotNull("phd.getSettings() is null!", phd.getSettings());
        Assert.assertNotNull("phd.getWorlds() is null!", phd.getWorlds());
    }

    @Test
    public void testAlways() {
        HologramStorage hs = phd.getHolograms();
        Assert.assertNotNull("Hologram storage is null!", hs);
        Storage storage = hs.getStorage();
        Assert.assertNotNull("Storage is null!", storage);
        HDHologramInfo info = new HDHologramInfo("myCustomHoloName");
        info.addInfo(new HologramInfo(info.getHoloName(), PeriodicType.ALWAYS, -1, -1, null,
                new NTimesTypeInfo(-1, new HashMap<>()), -1, -1));
        Set<HDHologramInfo> set = new HashSet<>();
        set.add(info);
        storage.saveHolograms(set, true);
        storage.loadHolograms((newInfo) -> {
            Assert.assertEquals("Expected to load the same info as was saved!", info, newInfo);
        });
    }

    @Test
    public void testNTimes() {
        HologramStorage hs = phd.getHolograms();
        Assert.assertNotNull("Hologram storage is null!", hs);
        Storage storage = hs.getStorage();
        Assert.assertNotNull("Storage is null!", storage);
        HDHologramInfo info = new HDHologramInfo("NTIMES-test");
        info.addInfo(new HologramInfo(info.getHoloName(), PeriodicType.NTIMES, 5.6, 5, "some.perms",
                new NTimesTypeInfo(4, new HashMap<>()), 1.2, 1.3));
        Set<HDHologramInfo> set = new HashSet<>();
        set.add(info);
        storage.saveHolograms(set, true);
        storage.loadHolograms((newInfo) -> {
            Assert.assertEquals("Expected to load the same info as was saved!", info, newInfo);
        });
    }

    @Test
    public void testIRLTime() {
        HologramStorage hs = phd.getHolograms();
        Assert.assertNotNull("Hologram storage is null!", hs);
        Storage storage = hs.getStorage();
        Assert.assertNotNull("Storage is null!", storage);
        HDHologramInfo info = new HDHologramInfo("IRLTime-test");
        info.addInfo(new HologramInfo(info.getHoloName(), PeriodicType.IRLTIME, 5.6, 5, "some.perms",
                new IRLTimeTypeInfo(1250), 1.2, 1.3));
        Set<HDHologramInfo> set = new HashSet<>();
        set.add(info);
        storage.saveHolograms(set, true);
        storage.loadHolograms((newInfo) -> {
            Assert.assertEquals("Expected to load the same info as was saved!", info, newInfo);
        });
    }

    @Test
    public void testMCTime() {
        HologramStorage hs = phd.getHolograms();
        Assert.assertNotNull("Hologram storage is null!", hs);
        Storage storage = hs.getStorage();
        Assert.assertNotNull("Storage is null!", storage);
        HDHologramInfo info = new HDHologramInfo("MCTime-test");
        info.addInfo(new HologramInfo(info.getHoloName(), PeriodicType.MCTIME, 6, 4, "no.perms",
                new MCTimeTypeInfo(1280), 1.2, 1.3));
        Set<HDHologramInfo> set = new HashSet<>();
        set.add(info);
        storage.saveHolograms(set, true);
        storage.loadHolograms((newInfo) -> {
            Assert.assertEquals("Expected to load the same info as was saved!", info, newInfo);
        });
    }

}