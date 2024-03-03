package edu.ucsd.cse110.successorator.lib.domain;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleDateTracker implements DateTracker {
    // the singleton design pattern is used here in a special way to accomplish the following:
    // 1. maintain list of observers through all classes
    // 2. avoid issues where part of the code is on different dates
    private static MutableSubject<SimpleDateTracker> instance;
    private Calendar calendar;
    private TimeZone timeZone;
    private String currentDate;
    private Integer forwardBy;
    private SimpleDateTracker(){
        this.timeZone = TimeZone.getDefault();
        this.calendar = Calendar.getInstance(this.timeZone);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");
        this.currentDate = dateFormat.format(calendar.getTime());
        this.forwardBy = 0;
    }

    // singleton pattern to use this one EVERYWHERE
    public static MutableSubject<SimpleDateTracker> getInstance() {
        if (instance == null) {
            var subj = new SimpleSubject<SimpleDateTracker>();
            subj.setValue(new SimpleDateTracker());
            instance = subj;
        }
        return instance;
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


    public String getNextDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd");

        // no need to alter the calendar's state, make a temp calendar
        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.add(Calendar.DATE, 1);

        //calendar.add(Calendar.DATE, (1));
        String hold = dateFormat.format(tempCalendar.getTime());
        //calendar.add(Calendar.DATE, (-1));

        return hold;
    }

    @Override
    public void setForwardBy(Integer forwardBy) {

        if(forwardBy > 0) {
            this.forwardBy = forwardBy;
        }

    }
}