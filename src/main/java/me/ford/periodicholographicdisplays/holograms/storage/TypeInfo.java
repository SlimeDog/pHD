package me.ford.periodicholographicdisplays.holograms.storage;

import java.util.Map;
import java.util.UUID;

import me.ford.periodicholographicdisplays.holograms.PeriodicType;

/**
 * TypeInfo
 */
public interface TypeInfo {

    public PeriodicType getType();

    public class NullTypeInfo implements TypeInfo {
        private final PeriodicType type;

        public NullTypeInfo(PeriodicType type) {
            this.type = type;
        }

        @Override
        public PeriodicType getType() {
            return type;
        }

    }

    public class NTimesTypeInfo implements TypeInfo {
        private final int showTimes;
        private final Map<UUID, Integer> shownToTimes;

        public NTimesTypeInfo(int showTimes, Map<UUID, Integer> shownToTimes) {
            this.showTimes = showTimes;
            this.shownToTimes = shownToTimes;
        }

        public void addAllShownTo(Map<UUID, Integer> shownTo) {
            shownToTimes.putAll(shownTo);
        }

        public void addShownTo(UUID id, int times) {
            shownToTimes.put(id, times);
        }

        public int getShowTimes() {
            return showTimes;
        }

        public Map<UUID, Integer> getShownToTimes() {
            return shownToTimes; // shouldn't need to return a copy
        }

        @Override
        public PeriodicType getType() {
            return showTimes == -1 ? PeriodicType.ALWAYS : PeriodicType.NTIMES;
        }

        @Override
        public String toString() {
            return String.format("<%s(%d):%s>", getType().name(), showTimes, shownToTimes.toString());
        }
        
    }

    public class MCTimeTypeInfo implements TypeInfo {
        private final long atTime;

        public MCTimeTypeInfo(long atTime) {
            this.atTime = atTime;
        }

        @Override
        public PeriodicType getType() {
            return PeriodicType.MCTIME;
        }

        public long getAtTime() {
            return atTime;
        }
        
    }

    public class IRLTimeTypeInfo implements TypeInfo {
        private final long atTime;

        public IRLTimeTypeInfo(long atTime) {
            this.atTime = atTime;
        }

        @Override
        public PeriodicType getType() {
            return PeriodicType.IRLTIME;
        }

        public long getAtTime() {
            return atTime;
        }
        
    }
    
}