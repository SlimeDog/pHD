package me.ford.periodicholographicdisplays.mock;

import java.io.File;

import dev.ratas.slimedogcore.api.config.SDCCustomConfig;
import dev.ratas.slimedogcore.api.config.SDCCustomConfigManager;
import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;

public class MockCustomConfigManager implements SDCCustomConfigManager {
    private final IPeriodicHolographicDisplays phd;

    public MockCustomConfigManager(IPeriodicHolographicDisplays phd) {
        this.phd = phd;
    }

    @Override
    public SDCCustomConfig getConfig(String fileName) {
        return getConfig(new File(phd.getDataFolder(), fileName));
    }

    @Override
    public SDCCustomConfig getConfig(File file) {
        return new MockCustomConfig(file);
    }

    @Override
    public SDCCustomConfig getDefaultConfig() {
        return getConfig("config.yml");
    }

}
