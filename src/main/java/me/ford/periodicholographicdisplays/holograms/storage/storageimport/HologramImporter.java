package me.ford.periodicholographicdisplays.holograms.storage.storageimport;

import java.util.function.Consumer;

import me.ford.periodicholographicdisplays.holograms.storage.Storage;
import me.ford.periodicholographicdisplays.holograms.storage.Storage.HDHologramInfo;

/**
 * HologramImporter
 */
public class HologramImporter<A extends Storage> {
    private final A from;
    private final Consumer<HDHologramInfo> consumer;

    public HologramImporter(A from, Consumer<HDHologramInfo> consumer) {
        this.from = from;
        this.consumer = consumer;
    }

    public void startImport() {
        from.loadHolograms((holo) -> {
            consumer.accept(holo);
        });
    }

    public A getFrom() {
        return from;
    }
    
}