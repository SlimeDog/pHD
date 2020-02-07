package me.ford.periodicholographicdisplays.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TimeUtils
 */
public final class TimeUtils {

    private TimeUtils() { throw new IllegalAccessError("TimeUtils should not be initialized");}

    private static final Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + 
                                                        "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + 
                                                        "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + 
                                                        "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + 
                                                        "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + 
                                                        "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + 
                                                        "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);
    
    private static final Pattern mcTimePattern = Pattern.compile("(\\d\\d):(\\d\\d)");
	    
    public static long parseDateDiff(String time) throws IllegalArgumentException {
    	Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if (m.group() == null || m.group().isEmpty()) {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++) {
                if (m.group(i) != null && !m.group(i).isEmpty()) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Could not parse Time!");
        }
        long timems = years * 365L * 24L * 60L * 60L * 1000L;
        timems += months * 30L * 24L * 60L * 60L * 1000L;
        timems += weeks * 7L * 24L * 60L * 60L * 1000L;
        timems += days * 24L * 60L * 60L * 1000L;
        timems += hours * 60L * 60L * 1000L;
        timems += minutes * 60L * 1000L;
        timems += seconds * 1000L;
    	return timems;
    }
    
    /**
     * Get a string value of time difference of
     * 
     * @param datediff  difference (in milliseconds)
     * @return          string value of the time difference
     */
    public static String formatDateFromDiff(long datediff) {
    	if (datediff < 1000L) { // less than a second
    		return "0s ";
    	}
    	long year = 365L * 24L * 60L * 60L * 1000L;
        long month = 30L * 24L * 60L * 60L * 1000L;
        long day = 24L * 60L * 60L * 1000L;
        long hour = 60L * 60L * 1000L;
        long minute = 60L * 1000L;
        long second = 1000L;
        long years = 0;
        long months = 0;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;
        String time = "";
        long rdiff = datediff;
        if (rdiff > year) {
        	years = rdiff/year;
        	time += String.valueOf(years) + " years ";
        	rdiff -= years * year;
        }
        if (rdiff > month) {
        	months = rdiff/month;
        	time += String.valueOf(months) + " months ";
        	rdiff -= months * month;
        }
        if (rdiff > day) {
        	days = rdiff/day;
        	time += String.valueOf(days) + " days ";
        	rdiff -= days * day;
        }
        if (rdiff > hour) {
        	hours = rdiff/hour;
        	time += String.valueOf(hours) + " hours ";
        	rdiff -= hours * hour;
        }
        if (rdiff > minute) {
        	minutes = rdiff/minute;
        	time += String.valueOf(minutes) + " minutes ";
        	rdiff -= minutes * minute;
        }
        if (rdiff > second) {
        	seconds = rdiff/second;
        	time += String.valueOf(seconds) + " seconds ";
        	rdiff -= seconds * second;
        }
        return time;
    }

    public static long parseMCTime(String mcTime) {
        Matcher matcher = mcTimePattern.matcher(mcTime);
        if (!matcher.matches()) throw new IllegalArgumentException("The time does not match the pattern 'hh:mm' : " + mcTime);
        int hours = Integer.parseInt(matcher.group(1)); // NumberFormatException should be avoided deu to the pattern matching
        int minutes = Integer.parseInt(matcher.group(2));
        return hours * 1000 + (minutes * 1000)/60;
    }
    
}