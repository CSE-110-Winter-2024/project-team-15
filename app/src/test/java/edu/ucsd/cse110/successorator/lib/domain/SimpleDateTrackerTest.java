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
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
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

    @Test
    public void getNextDate() {
        /* ************************************************************************************* */
        // Checking this method doesn't alter the date..
        // Given: A simple date tracker on the current date and the current date
        SimpleDateTracker myTracker = SimpleDateTracker.getInstance().getValue();
        // current date stuff
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
        String currentDate = dateFormat.format(calendar.getTime());

        // When: getNextDate is called to get the next date
        var nextDate = myTracker.getNextDate();

        // Then: The tracker still contains the current date
        assertEquals(currentDate, myTracker.getDate());

        // Checking this method actually gets the next date..
        // And: It actually gets the next date
        calendar.add(Calendar.DATE, (1));
        String realNextDate = dateFormat.format(calendar.getTime());
        assertEquals(nextDate, realNextDate);

        /* ************************************************************************************* */
    }

}
