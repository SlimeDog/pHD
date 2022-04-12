package me.ford.periodicholographicdisplays.holograms;
import me.ford.periodicholographicdisplays.IPeriodicHolographicDisplays;

public class Zombificator {
    private final IPeriodicHolographicDisplays phd;
    private final HologramStorage holograms;

    public Zombificator(IPeriodicHolographicDisplays phd) {
        this.phd = phd;
        this.holograms = phd.getHolograms();
        phd.getHologramPlatform().getHologramCommand().wrapWith(this);
    }

    public void foundRemoved(String name) {
        phd.debug("HD Hologram might have been removed - checking for zombies");
        holograms.checkForZombies();
    }

}