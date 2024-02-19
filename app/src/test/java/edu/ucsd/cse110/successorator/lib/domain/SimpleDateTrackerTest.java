package edu.ucsd.cse110.successorator.lib.domain;


import org.junit.Test;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SimpleDateTrackerTest {
    @Test
    public void getDateTest(){
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        String currentDate = dateFormat.format(calendar.getTime());
        var temp = SimpleDateTracker.getInstance();
        assertTrue(temp.getValue().getDate().equals(currentDate));
    }

    @Test
    public void getHourTest(){
        TimeZone timeZone = TimeZone.getDefault();
        var temp = SimpleDateTracker.getInstance();
        Calendar calendar =Calendar.getInstance(timeZone);
        // temp.forwardUpdate();
        // System.out.println(temp.getDate());
        assertEquals(temp.getValue().getHour(), calendar.get(Calendar.HOUR_OF_DAY));
    }

}
