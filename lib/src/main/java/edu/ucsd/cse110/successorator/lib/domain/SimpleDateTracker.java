package edu.ucsd.cse110.successorator.lib.domain;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
public class SimpleDateTracker implements DateTracker {
    private Calendar calendar;
    private TimeZone timeZone;
    private String currentDate;
    private Integer forwardBy;
    public SimpleDateTracker(){
        this.timeZone = TimeZone.getDefault();
        this.calendar = Calendar.getInstance(this.timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        this.currentDate = dateFormat.format(calendar.getTime());
        this.forwardBy = 0;
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

        if (forwardBy > 0) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH, forwardBy);
        }

        this.currentDate = dateFormat.format(calendar.getTime());

    }

    @Override
    public void setForwardBy(Integer forwardBy) {

        if(forwardBy > 0) {
            this.forwardBy = forwardBy;
        }

    }


}
