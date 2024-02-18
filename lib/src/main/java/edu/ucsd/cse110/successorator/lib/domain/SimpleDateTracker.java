package edu.ucsd.cse110.successorator.lib.domain;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
public class SimpleDateTracker implements DateTracker {
    private Calendar calendar;
    private TimeZone timeZone;
    private String currentDate;
    public SimpleDateTracker(){
        this.timeZone = TimeZone.getDefault();
        this.calendar = Calendar.getInstance(this.timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        this.currentDate = dateFormat.format(calendar.getTime());
    }

    @Override
    public String getDate(){
        update();
        return this.currentDate;
    }

    @Override
    public int getHour(){
        update();
        Calendar calendar =Calendar.getInstance(this.timeZone); // creates a new calendar instance
        return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
    }


    @Override
    public void update(){
        this.timeZone = TimeZone.getDefault();
        this.calendar = Calendar.getInstance(this.timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        this.currentDate = dateFormat.format(calendar.getTime());
    }

    @Override
    public void forwardUpdate() {
        // https://stackoverflow.com/questions/7691855/adding-days-with-java-util-calendar-gives-strange-results
        // https://stackoverflow.com/questions/428918/how-can-i-increment-a-date-by-one-day-in-java
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        this.currentDate = dateFormat.format(calendar.getTime());
    }

}
