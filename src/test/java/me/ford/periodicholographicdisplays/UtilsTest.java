package me.ford.periodicholographicdisplays;

import org.junit.Assert;
import org.junit.Test;

import me.ford.periodicholographicdisplays.util.TimeUtils;

public class UtilsTest {

    @Test
    public void test_parsing() {
        long curTime = 1000 * (3 * 60 * 60 + 2 * 60 + 1);
        String parsed = TimeUtils.formatDateFromDiff(curTime);
        long returned = TimeUtils.parseDateDiff(parsed);
        Assert.assertTrue(Math.abs(curTime - returned) <= 1000L);
    }
    
    @Test
    public void test_IRL_time() {
        String time = "10:15";
        long result = TimeUtils.parseHoursAndMinutesToSeconds(time);
        String returned = TimeUtils.toIRLTime(result);
        Assert.assertEquals(time, returned);
    }
    
    @Test
    public void test_MC_time() {
        String time = "10:15";
        long result = TimeUtils.parseMCTime(time);
        String returned = TimeUtils.toMCTime(result);
        Assert.assertEquals(time, returned);
    }

}