package me.ford.periodicholographicdisplays;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * IPeriodicHolographicDisplays
 */
public interface IPeriodicHolographicDisplays {

    public File getDataFolder();

    public Logger getLogger();

    public InputStream getResource(String name);

}