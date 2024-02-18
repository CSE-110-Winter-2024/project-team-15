package edu.ucsd.cse110.successorator.lib.domain;


import org.junit.Test;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;

public class DateTrackerTest {
    @Test
    public void getDateTest(){
        TimeZone timeZone = TimeZone.getDefault();
        Calendar calendar = Calendar.getInstance(timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        String currentDate = dateFormat.format(calendar.getTime());
        DateTracker temp = new DateTracker();
        assertTrue(temp.getDate().equals(currentDate));
    }

    @Test
    public void getHourTest(){
        TimeZone timeZone = TimeZone.getDefault();
        DateTracker temp = new DateTracker();
        Calendar calendar =Calendar.getInstance(timeZone);
        assertEquals(temp.getHour(), calendar.get(Calendar.HOUR_OF_DAY));
    }

}
