package me.ford.periodicholographicdisplays.commands.subcommands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;

import me.ford.periodicholographicdisplays.commands.SubCommand;
import me.ford.periodicholographicdisplays.holograms.IRLTimeHologram;
import me.ford.periodicholographicdisplays.holograms.MCTimeHologram;
import me.ford.periodicholographicdisplays.holograms.NTimesHologram;
import me.ford.periodicholographicdisplays.holograms.PeriodicHologramBase;
import me.ford.periodicholographicdisplays.holograms.PeriodicType;
import me.ford.periodicholographicdisplays.util.TimeUtils;

/**
 * OptionPairSub
 */
public abstract class OptionPairSetSub extends SubCommand {

    protected Map<String, String> getOptionPairs(String[] args) {
        if (args.length%2 != 0) throw new IllegalArgumentException("Expected an even number of arguments!");
        Map<String, String> map = new HashMap<>();
        boolean isKey = true;
        String curKey = "";
        String curValue = "";
        for (String arg : args) {
            if (isKey) {
                curKey = arg.toLowerCase();
            } else {
                curValue = arg;
            }
            if (!isKey) {
                map.put(curKey, curValue);
            }
            isKey = !isKey;
        }
        return map;
    }

    protected void setAll(CommandSender sender, PeriodicHologramBase holo, Map<String, String> options, boolean doSpecial) throws OptionPairException {
        Set<String> invalidOptions = new HashSet<>();
        for (Entry<String, String> entry : options.entrySet()) {
            String result = entry.getValue();
            if (holo.getType() == PeriodicType.NTIMES && entry.getKey().equalsIgnoreCase("times")) {
                int times;
                try {
                    times = Integer.parseInt(result);
                } catch (NumberFormatException e) {
                    throw new OptionPairException(OptionPairExceptionType.NEED_A_NUMBER, result);
                }
                ((NTimesHologram) holo).setTimesToShow(times);
                continue;
            }
            if ((holo.getType() == PeriodicType.MCTIME || holo.getType() == PeriodicType.IRLTIME) 
                                && entry.getKey().equalsIgnoreCase("time")) {
                boolean mcTime = holo.getType() == PeriodicType.MCTIME;
                long time;
                try {
                    if (mcTime) time = TimeUtils.parseMCTime(result);
                    else time = TimeUtils.parseHoursAndMinutesToSeconds(result);
                } catch (IllegalArgumentException e) {
                    throw new OptionPairException(OptionPairExceptionType.INCORRECT_TIME, result);
                }
                if (mcTime) ((MCTimeHologram) holo).setTime(time);
                else ((IRLTimeHologram) holo).setTime(time);
                continue;
            }
            switch (entry.getKey()) {
                case "distance":
                double distance;
                try {
                    distance = Double.parseDouble(result);
                } catch (NumberFormatException e) {
                    throw new OptionPairException(OptionPairExceptionType.NEED_A_NUMBER, result);
                }
                if (distance < 0) {
                    throw new OptionPairException(OptionPairExceptionType.DISTANCE_NEGATIVE, result);
                }
                holo.setActivationDistance(distance);
                break;
                case "seconds":
                if (holo.getType() == PeriodicType.ALWAYS) {
                    throw new OptionPairException(OptionPairExceptionType.NO_SUCH_OPTION, entry.getKey());
                }
                int time;
                try {
                    time = Integer.parseInt(result);
                } catch (NumberFormatException e) {
                    throw new OptionPairException(OptionPairExceptionType.NEED_A_NUMBER, result);
                }
                if (time < 0) {
                    throw new OptionPairException(OptionPairExceptionType.SECONDS_NEGATIVE, result);
                }
                holo.setShowTime(time);
                break;
                case "permission":
                holo.setPermissions(result);
                break;
                default:
                throw new OptionPairException(OptionPairExceptionType.NO_SUCH_OPTION, entry.getKey());
            }
        }
        for (String opt : invalidOptions) options.remove(opt);
    }

    public class OptionPairException extends IllegalArgumentException {
        private final OptionPairExceptionType type;
        private final String extra;

        /**
         *
         */
        private static final long serialVersionUID = -2328560427573907651L;

        public OptionPairException(OptionPairExceptionType type, String extra) {
            super(type.name());
            this.type = type;
            this.extra = extra;
        }

        public OptionPairExceptionType getType() {
            return type;
        }

        public String getExtra() {
            return extra;
        }

    }

    public enum OptionPairExceptionType {
        NEED_A_NUMBER,
        INCORRECT_TIME,
        NO_SUCH_OPTION,
        DISTANCE_NEGATIVE,
        SECONDS_NEGATIVE
    }
    
}