package me.ford.periodicholographicdisplays.holograms.wrap.platform;

public abstract class AbstractHologramPlatform implements HologramPlatform {
    private final String name;

    public AbstractHologramPlatform(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

}
