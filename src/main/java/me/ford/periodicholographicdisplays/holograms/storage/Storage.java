package me.ford.periodicholographicdisplays.holograms.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Storage
 */
public interface Storage {

    public void saveHolograms(Set<HDHologramInfo> holograms, boolean inSync);

    public void loadHolograms(Consumer<HDHologramInfo> consumer);

    public boolean hasData();

    public void clear();

    public static class HDHologramInfo {
        private final String holoName;
        private final List<HologramInfo> infos = new ArrayList<>();

        public HDHologramInfo(String holoName) {
            this.holoName = holoName;
        }

        public String getHoloName() {
            return holoName;
        }

        public void addInfo(HologramInfo info) {
            infos.add(info);
        }

        public List<HologramInfo> getInfos() {
            return infos; // I don't think I need to return a copy
        }

        @Override
        public String toString() {
            List<String> types = new ArrayList<>();
            for (HologramInfo info: infos) types.add(info.getType().name());
            return String.format("[HDHoloInfo:%s(%s)]", holoName, infos.toString());
        }

    }
    
}