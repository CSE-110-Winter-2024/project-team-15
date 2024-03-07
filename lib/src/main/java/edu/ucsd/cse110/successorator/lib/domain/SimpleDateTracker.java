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
        // truncating day means EEEE is EEE
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
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
        // truncating day means EEEE is EEE
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");

        if (forwardBy > 0) {
            calendar.add(java.util.Calendar.DAY_OF_MONTH, forwardBy);
        }

        this.currentDate = dateFormat.format(calendar.getTime());

    }


    public String getNextDate() {
        // truncating day means EEEE is EEE
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");

        // no need to alter the calendar's state, make a temp calendar
        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.add(Calendar.DATE, 1);

        //calendar.add(Calendar.DATE, (1));
        String hold = dateFormat.format(tempCalendar.getTime());
        //calendar.add(Calendar.DATE, (-1));

        return hold;
    }

    public int getNextDateDayOfMonth() {

        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.add(Calendar.DATE, 1);

        //return tempCalendar.getTime().getDate();
        int date = tempCalendar.get(Calendar.DATE);
        return date;
    }

    public int getNextDateMonthOfYear() {

        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.add(Calendar.DATE, 1);

        //return tempCalendar.getTime().getMonth();
        int month = tempCalendar.get(Calendar.MONTH);
        // months start at 0
        return month;
    }
    public int getNextDateYear() {

        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.add(Calendar.DATE, 1);

        //return tempCalendar.getTime().getYear();
        int year = tempCalendar.get(Calendar.YEAR);
        return year;
    }
    public int getNextDateDayOfWeek() {

        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.add(Calendar.DATE, 1);

        //return tempCalendar.getTime().getDay();
        int day = tempCalendar.get(Calendar.DAY_OF_WEEK);
        return day;
    }

    @Override
    public void setForwardBy(Integer forwardBy) {

        if(forwardBy > 0) {
            this.forwardBy = forwardBy;
        }

    }
}