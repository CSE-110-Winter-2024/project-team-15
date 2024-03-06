package edu.ucsd.cse110.successorator.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

@Entity(tableName = "goals")
public class GoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "contents")
    public String contents;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "list_num")
    public int listNum;

    @ColumnInfo(name = "recurrence_type")
    public int recurrenceType;

    @ColumnInfo(name = "day_starting")
    public int dayStarting;

    //breaking name cvntion to hopefully fix a bug
    @ColumnInfo(name = "month_starting")
    public int monthStarting;

    @ColumnInfo(name = "year_starting")
    public int yearStarting;

    @ColumnInfo(name = "day_of_week_to_recur")
    public int dayOfWeekToRecur;
    @ColumnInfo(name = "week_of_month_to_recur")
    public int weekOfMonthToRecur;
    @ColumnInfo(name = "overflow_flag")
    public boolean overflowFlag;


    GoalEntity(@NonNull String contents, int sortOrder, boolean completed, int listNum, int recurrenceType,
               int dayStarting, int monthStarting, int yearStarting, int dayOfWeekToRecur, int weekOfMonthToRecur,
               boolean overflowFlag){
        this.contents = contents;
        this.sortOrder = sortOrder;
        this.completed = completed;
        this.listNum = listNum;
        this.recurrenceType = recurrenceType;
        this.dayStarting = dayStarting;
        this.monthStarting = monthStarting;
        this.yearStarting = yearStarting;
        this.dayOfWeekToRecur = dayOfWeekToRecur;
        this.weekOfMonthToRecur = weekOfMonthToRecur;
        this.overflowFlag = overflowFlag;
    }
    public static GoalEntity fromGoal(@NonNull Goal goal){
        var gol = new GoalEntity(goal.contents(), goal.sortOrder(), goal.completed(), goal.listNum(),
                goal.recurrenceType(), goal.dayStarting(), goal.monthStarting(), goal.yearStarting(),
                goal.dayOfWeekToRecur(), goal.weekOfMonthToRecur(), goal.overflowFlag());
        gol.id = goal.id();
        return gol;
    }
    public @NonNull Goal toGoal(){
        return new Goal(contents, id, completed, sortOrder, listNum)
                .withRecurrenceData(recurrenceType, dayStarting, monthStarting, yearStarting,
                                    dayOfWeekToRecur, weekOfMonthToRecur, overflowFlag);
    }
}
