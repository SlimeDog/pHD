package me.ford.periodicholographicdisplays;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import me.ford.periodicholographicdisplays.mock.MockPeriodicHolographicDisplays;

public class HologramYAMLStorageTests extends HologramSQLStorageTests {
    private MockPeriodicHolographicDisplays phd;

    @Override
    @Before
    public void setUp() {
        phd = new MockPeriodicHolographicDisplays();
        phd.getConfig().set("storage-type", "YAML");
        phd.getSettings().reload();
        super.setUp();
    }

    @Override
    @After
    public void tearDown() {
        File yamlFile = new File(phd.getDataFolder(), "database.yml");
        if (yamlFile.exists()) {
            yamlFile.delete();
        }
        super.tearDown();
    }

    @Override
    @Test
    public void correct_database() {
        Assert.assertFalse(phd.getSettings().useDatabase());
    }

}