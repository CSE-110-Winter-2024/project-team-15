package edu.ucsd.cse110.successorator.lib.domain;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
public class DateTracker {
    private Calendar calendar;
    private TimeZone timeZone;
    private String currentDate;
    public DateTracker(){
        this.timeZone = TimeZone.getDefault();
        this.calendar = Calendar.getInstance(this.timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        this.currentDate = dateFormat.format(calendar.getTime());
    }

    public String getDate(){
        update();
        return this.currentDate;
    }

    public int getHour(){
        update();
        Calendar calendar =Calendar.getInstance(this.timeZone); // creates a new calendar instance
        return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
    }


    public void update(){
        this.timeZone = TimeZone.getDefault();
        this.calendar = Calendar.getInstance(this.timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        this.currentDate = dateFormat.format(calendar.getTime());
    }
}