package edu.ucsd.cse110.successorator.lib.domain;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

// new imports to use local time instead
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

// so what's different with this date tracker?
// it uses LocalDateTime instead..
// necessary so we won't be confused any longer
public class ComplexDateTracker implements DateTracker {
    // the singleton design pattern is used here in a special way to accomplish the following:
    // 1. maintain list of observers through all classes
    // 2. avoid issues where part of the code is on different dates
    private static MutableSubject<ComplexDateTracker> instance;
    //private Calendar calendar;
    //private TimeZone timeZone;
    private LocalDateTime dateTime;
    private String currentDate;
    private Integer forwardBy;


    private ComplexDateTracker(){
        //this.timeZone = TimeZone.getDefault();
        //this.calendar = Calendar.getInstance(this.timeZone);

        // lets get the current time using local time
        this.dateTime = LocalDateTime.now();

        // truncating day means EEEE is EEE
        // SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
        // apparently we need a different formatter?
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");


        this.currentDate = this.dateTime.format(dateFormat);
        this.forwardBy = 0;
    }

    // singleton pattern to use this one EVERYWHERE
    public static MutableSubject<ComplexDateTracker> getInstance() {
        if (instance == null) {
            var subj = new SimpleSubject<ComplexDateTracker>();
            subj.setValue(new ComplexDateTracker());
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
        //Calendar calendar =Calendar.getInstance(this.timeZone); // creates a new calendar instance
        //return calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        return this.dateTime.getHour(); // just directly get the hour in a 24h format
    }


    @Override
    public void update(){
//        this.timeZone = TimeZone.getDefault();
//        this.calendar = Calendar.getInstance(this.timeZone);
//        // truncating day means EEEE is EEE
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
//
//        if (forwardBy > 0) {
//            calendar.add(java.util.Calendar.DAY_OF_MONTH, forwardBy);
//        }
//
//        this.currentDate = dateFormat.format(calendar.getTime());

        this.dateTime = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");

        if (forwardBy > 0) {
            this.dateTime = this.dateTime.plusDays(forwardBy);
        }

        this.currentDate = this.dateTime.format(dateFormat);
    }


    public String getNextDate() {
        // no need for a copy ahahah we're thread safe here
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");
        LocalDateTime tempDateTime = this.dateTime.plusDays(1);
        return tempDateTime.format(dateFormat);
    }
    public boolean getNextDateIsLeapYear() {
        LocalDateTime tempDateTime = this.dateTime.plusDays(1);
        return tempDateTime.toLocalDate().isLeapYear();
    }

    public int getNextDateLastMonthNumDays(){
        LocalDateTime tempDateTime = this.dateTime.plusDays(1).minusMonths(1);
        return tempDateTime.toLocalDate().lengthOfMonth();
    }

    public int getNextDateDayOfMonth() {
        LocalDateTime tempDateTime = this.dateTime.plusDays(1);
        return tempDateTime.getDayOfMonth();
    }

    public int getNextDateMonthOfYear() {
        LocalDateTime tempDateTime = this.dateTime.plusDays(1);
        return tempDateTime.getMonthValue(); // ayo this is from 1 to 12
    }

    public int getNextDateYear() {
        LocalDateTime tempDateTime = this.dateTime.plusDays(1);
        return tempDateTime.getYear();
    }

    public int getNextDateDayOfWeek() {
        // originally returned calendar.day_of_month not sure why
        // getDayOfWeek returns the a day of week obj btw
        // i wanted a number so I did get value. monday is 1 sunday is 7
        LocalDateTime tempDateTime = this.dateTime.plusDays(1);
        return tempDateTime.getDayOfWeek().getValue();
    }

    @Override
    public void setForwardBy(Integer forwardBy) {
        if(forwardBy > 0) {
            this.forwardBy = forwardBy;
        }
    }


    // methods to use when you need time manipulation outside of the current time

    // remember monday is 1, and Sunday is 7
    // method to get the amount of weeks this day has occurred (like 3rd Tuesday, this returns 3)

    // we do this as seen below:
    //
    // if we go to the first day of the month
    // and then go to the first time our desired dayOfWeek appeared
    //
    // then subtracting our day of month by the first time day of week appears gives us the
    // amount of days between them
    //
    // dividing that by 7 gives us the amount of weeks between them

    // can't just divide by 7 and add 1 because of when the first day of the week can appear

    public static LocalDateTime datePickerToLocalDateTime(int year, int monthOriginal, int day) {
        int month = monthOriginal; // Date picker
        return LocalDateTime.of(year, month, day, 0, 0)
                .withSecond(0).withNano(0);
    }

    public static LocalDateTime goalToLocalDateTime(Goal goal) {
        return LocalDateTime.of(goal.yearStarting(), goal.monthStarting(), goal.dayStarting(), 0, 0)
                .withSecond(0).withNano(0);
    }


    // this is like what we were doing with calendar but with local time instead
    public static int getWeekOfMonth(LocalDateTime dateTime) {
        // The first day of the month for the given date
        LocalDateTime firstDayOfMonth = dateTime.withHour(0)
                .withMinute(0).withSecond(0).withNano(0).withDayOfMonth(1);

        // difference in days from the first day of the month
        int diff = dateTime.getDayOfWeek().getValue() - firstDayOfMonth.getDayOfWeek().getValue();

        if (diff < 0) {
            diff += 7; // the target day is in the next week
        }

        // first occurrence of the desired day of the week
        LocalDateTime firstOccurrence = firstDayOfMonth.plusDays(diff);

        // ethan math
        int weekOfMonth = (dateTime.getDayOfMonth() - firstOccurrence.getDayOfMonth()) / 7 + 1;

        return weekOfMonth;
    }

    // unlikely we'll need this, just in case we don't have a local date time ...
    public static int getWeekOfMonth(int year, int month, int dayOfMonth, int dayOfWeek) {
        // this is like what we were doing with calendar but with local time instead
        LocalDateTime date = LocalDateTime.of(year, month, dayOfMonth,0,0);
        return getWeekOfMonth(date);
    }

    // probably unnecessary idk ill see
    public static int getDayOfWeek(LocalDateTime dateTime) {
        return dateTime
                .withHour(0).withMinute(0).withSecond(0).withNano(0)
                .getDayOfWeek().getValue(); // 1 is monday 7 is sunday
    }


    public static LocalDateTime goalRepresentation(Goal goal){
        //Calendar ins = (Calendar) Calendar.getInstance().clone();
        // day starting is not day of week starting
        LocalDateTime date = LocalDateTime.of(goal.yearStarting(), goal.monthStarting(),
                goal.dayStarting(),0,0,0,0);
        return date;
    }


    // Only need this one
    // TRUE iff goal has tomorrow's date
    public boolean compareGoalToTomorrow(Goal goal){
        // Tomorrow's date .. at time 0 (not sure if the "with" stuff is necessary)
        LocalDateTime tomorrowStart = LocalDateTime.now()
                .plusDays(1).plusDays(forwardBy).withHour(0).withMinute(0).withSecond(0).withNano(0);

        // goal's LocalDateTime representation
        LocalDateTime goalDate = goalRepresentation(goal);

        // I want to add to tomorrow so I'm comparing it to tomorrow's date
        return goalDate.isEqual(tomorrowStart);
    }

    // yoav made a good case for this one .. like adding recurring goals TODAY too ...
    public boolean compareGoalToToday(Goal goal){
        // Tomorrow's date .. at time 0 (not sure if the "with" stuff is necessary)
        LocalDateTime todayStart = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        // goal's LocalDateTime representation
        LocalDateTime goalDate = goalRepresentation(goal);

        // I want to add to tomorrow so I'm comparing it to tomorrow's date
        return goalDate.isEqual(todayStart);
    }


    // a method for finding the amount of time
    public static LocalDateTime getNextOccurrence(Goal goal, LocalDateTime currentDateTime) {
        switch (goal.recurrenceType()) {

            case 0: // once
                return LocalDateTime.of(
                        goal.yearStarting(), goal.monthStarting(), goal.dayStarting(),
                        0, 0,0,0);

            case 1: // twic- i mean daily
                return currentDateTime.plusDays(1);

            case 2: // weekly
                return currentDateTime.plusWeeks(1);

            case 3: // monthly
                if (goal.dayOfWeekToRecur() > 0 && goal.weekOfMonthToRecur() > 0) {
                    // there's a much easier way to do all this using temporal adjusters
                    // fml

                    LocalDateTime firstDayOfNextMonth;

                    // if it occurs on the 5th week of a month sometimes there's bugs
                    if(goal.weekOfMonthToRecur() == 5) {
                        // basically if today is not the 5th that means the previous month
                        // didn't have a 5th.. so we stay in this month

                        // there's a much easier way to do all this using temporal adjusters
                        // fml
                        firstDayOfNextMonth = currentDateTime
                                .with(TemporalAdjusters.firstDayOfMonth());
                    } else {
                        firstDayOfNextMonth = currentDateTime.plusMonths(1)
                                .with(TemporalAdjusters.firstDayOfMonth());
                    }

                    // monday is 1, thats why ordinal is 1 (We want this date structure)
                    LocalDateTime firstOccurrence = firstDayOfNextMonth
                            .with(TemporalAdjusters.dayOfWeekInMonth(1, DayOfWeek.of(goal.dayOfWeekToRecur())));

                    // we add as many weeks as needed
                    int weeksToAdd = goal.weekOfMonthToRecur()-1;
                    return firstOccurrence.plusWeeks(weeksToAdd);

                } else {
                    // recur on the same date .. this is not offensive programming tho
                    return currentDateTime.plusMonths(1);
                }

            case 4: // yeaarly
                return currentDateTime.plusYears(1);

            default:
                return LocalDateTime.of(
                        goal.yearStarting(), goal.monthStarting(), goal.dayStarting(),
                        0, 0,0,0);

        }

    }

}
