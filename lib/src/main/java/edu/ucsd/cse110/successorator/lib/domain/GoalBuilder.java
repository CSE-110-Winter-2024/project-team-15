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

    //this flag will be used to handle cases like yearly on Feb29th to behave like customer required
    private boolean overflowFlag;

    public GoalBuilder(){}

    public GoalBuilder withDefault(Goal goal){
        this.contents = goal.contents();
        this.id = goal.id();
        this.completed = goal.completed();
        this.sortOrder = goal.sortOrder();
        this.listNum = goal.listNum();
        this.recurrenceType = goal.recurrenceType();
        this.dayStarting = goal.dayStarting();
        this.monthStarting = goal.monthStarting();
        this.yearStarting = goal.yearStarting();
        this.dayOfWeekToRecur = goal.dayOfWeekToRecur();
        this.weekOfMonthToRecur = goal.weekOfMonthToRecur();
        this.overflowFlag = goal.overflowFlag();
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
    public GoalBuilder addDays(int day){
        int newDay = dayOfWeekToRecur + day;
        if (newDay > 6){
            addWeeks(newDay / 7);
            newDay = newDay % 7;
        }
        this.dayOfWeekToRecur = newDay;
        return this;
    }
    public GoalBuilder addWeeks(int weeks){
        int newWeek = weekOfMonthToRecur + weeks;
        if (newWeek > 5){
            addMonths(newWeek / 6);
            newWeek = newWeek % 6;
        }
        this.weekOfMonthToRecur = newWeek;
        return this;
    }
    public GoalBuilder addMonths(int months){
        int newMonth = monthStarting + months;
        if (newMonth > 11){
            addYear(newMonth / 12);
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
        return new Goal(contents, id, completed, sortOrder, listNum, recurrenceType,
                dayStarting, monthStarting, yearStarting, dayOfWeekToRecur, weekOfMonthToRecur,
                overflowFlag);
    }
}