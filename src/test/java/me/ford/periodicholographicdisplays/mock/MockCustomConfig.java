package me.ford.periodicholographicdisplays.mock;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import dev.ratas.slimedogcore.api.config.SDCConfiguration;
import dev.ratas.slimedogcore.api.config.SDCCustomConfig;
import dev.ratas.slimedogcore.api.config.exceptions.ConfigReloadException;
import dev.ratas.slimedogcore.api.config.exceptions.ConfigSaveException;
import dev.ratas.slimedogcore.impl.config.ConfigurationWrapper;

public class MockCustomConfig implements SDCCustomConfig {
    private final File file;

    public MockCustomConfig(File file) {
        this.file = file;
    }

    @Override
    public void reloadConfig() throws ConfigReloadException {
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public SDCConfiguration getConfig() {
        return new ConfigurationWrapper(YamlConfiguration.loadConfiguration(file));
    }

    @Override
    public void saveConfig() throws ConfigSaveException {
        // nothing
    }

    @Override
    public void saveDefaultConfig() throws ConfigSaveException {
        // nothing
    }

}
