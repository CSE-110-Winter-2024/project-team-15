package edu.ucsd.cse110.successorator.lib.domain;

import java.time.LocalDateTime;

public interface DateTracker {
    LocalDateTime getDateTime();
    String getDate();
    int getHour();
    void update();
    String getNextDate();
    int getYear();
    boolean getNextDateIsLeapYear();
    int getNextDateLastMonthNumDays();
    int getNextDateDayOfMonth();
    int getNextDateMonthOfYear();
    int getNextDateYear();
    int getNextDateDayOfWeek();
    int getNextDateWeekOfMonth();
    void setForwardBy(Integer forwardBy);
    boolean compareGoalToTomorrow(Goal goal);
    boolean compareGoalToToday(Goal goal);

}
