package me.ford.periodicholographicdisplays.holograms.storage.storageimport;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import me.ford.periodicholographicdisplays.PeriodicHolographicDisplays;
import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;
import me.ford.periodicholographicdisplays.listeners.HologramsLoadedListener;

/**
 * StorageConverter
 */
public class StorageConverter<A extends Storage, B extends Storage> {
    private final A from;
    private final B to;
    private final Set<HDHologramInfo> infos = new HashSet<>();

    public StorageConverter(A from, B to, Runnable whenDone) {
        this.from = from;
        this.to = to;
        PeriodicHolographicDisplays phd = JavaPlugin.getPlugin(PeriodicHolographicDisplays.class);
        phd.getServer().getPluginManager().registerEvents(new HologramsLoadedListener(whenDone), phd);
    }

    public void startConvert() {
        from.loadHolograms((loaded) -> add(loaded));
    }

    private void add(HDHologramInfo info) {
        infos.clear();
        infos.add(info);
        to.saveHolograms(new HashSet<>(infos), false);
    }

    public A getFrom() {
        return from;
    }

    public B getTo() {
        return to;
    }
    
}