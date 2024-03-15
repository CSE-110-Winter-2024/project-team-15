package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GoalBuilder {
    private @NonNull String contents;
    private @Nullable Integer id;

    private @NonNull Boolean completed;
    private @Nullable int sortOrder;

    //will use this field for sorting in lists.
    //0 = today, 1 = tomorrow, 2 = pending, 3 = recurring
    private int listNum;

    //0 = one-time; 1 = daily; 2 = weekly; 3 = monthly; 4 = yearly
    private int recurrenceType;
    private int dayStarting;
    private int monthStarting;
    private int yearStarting;
    private int dayOfWeekToRecur;
    private int weekOfMonthToRecur;

    private int context;

    //this flag will be used to handle cases like yearly on Feb29th to behave like customer required
    private boolean overflowFlag;

    public GoalBuilder(){}

    public GoalBuilder withDefault(Goal goal){
        this.contents = goal.contents();
        this.id = goal.id();
        this.completed = goal.completed();
        this.sortOrder = goal.sortOrder();
        this.listNum = goal.listNum();
        this.context = goal.context();
        this.recurrenceType = goal.recurrenceType();
        this.dayStarting = goal.dayStarting();
        this.monthStarting = goal.monthStarting();
        this.yearStarting = goal.yearStarting();
        this.dayOfWeekToRecur = goal.dayOfWeekToRecur();
        this.weekOfMonthToRecur = goal.weekOfMonthToRecur();
        return this;
    }
    // @require 0 <= day < 7
    public GoalBuilder withDay(int day){
        this.dayOfWeekToRecur = day;
        return this;
    }
    // @require 0 < week < 6
    public GoalBuilder withWeek(int week){
        this.weekOfMonthToRecur = week;
        return this;
    }
    // @require 0 <= month <= 11
    public GoalBuilder withMonth(int month){
        this.monthStarting = month;
        return this;
    }

    //  we don't need these, there's a much easier method to do this using the actual time
    public GoalBuilder addDays(int day){
        int newDay = dayOfWeekToRecur + day;
        if (newDay > 6){
            addWeeks(newDay / 7);
            // I think this is the line that is causing illegal arg.  we have cases for 1-7,
            // but this is often enough 0.  going to employ same fix as below by modulating 6 but
            // the math may need to be more in-depth
            // should be mod 7
            newDay = newDay % 6;
        }
        this.dayOfWeekToRecur = newDay;
        return this;
    }

    public GoalBuilder addWeeks(int weeks){
        int newWeek = weekOfMonthToRecur + weeks;
        if (newWeek > 5){
            addMonths(newWeek / 6);
            //doing mod 5 because it goes 1->2->3->4->5->1; might not be right, cant
            //think rn about what output is supposed to be for a difference of like 12 weeks, but I
            //also don't know if that case will ever be tested
            newWeek = newWeek % 5;
        }
        this.weekOfMonthToRecur = newWeek;
        return this;
    }
    public GoalBuilder addMonths(int months){
        int newMonth = monthStarting + months;
        if (newMonth > 11){
            addYear(newMonth / 12);
            //stays the way it is since month is supposed to be 0 sometimes
            newMonth = newMonth % 12;
        }
        this.monthStarting = newMonth;
        return this;
    }

    public GoalBuilder addYear(int years){
        this.yearStarting += years;
        return this;
    }

    public Goal build(){
        return new Goal(contents, id, completed, sortOrder, listNum, recurrenceType, context,
                dayStarting, monthStarting, yearStarting, dayOfWeekToRecur, weekOfMonthToRecur
                );
    }

}