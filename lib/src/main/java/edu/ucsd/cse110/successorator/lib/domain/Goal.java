package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class Goal {
    // we do not want null strings -- that is lame
    private final @NonNull String contents;
    private final @Nullable Integer id;

    private final @NonNull Boolean completed;
    private final @Nullable int sortOrder;

    //will use this field for sorting in lists.
    //0 = today, 1 = tomorrow, 2 = pending, 3 = recurring
    private final int listNum;

    //0 = one-time; 1 = daily; 2 = weekly; 3 = monthly; 4 = yearly
    private final int recurrenceType;
    private final int dayStarting;
    private final int monthStarting;
    private final int yearStarting;
    private final int dayOfWeekToRecur;
    private final int weekOfMonthToRecur;
    //0 = Home, 1 = Work, 2 = School, 3 = Errand
    private final int context;

    public Goal(@NonNull String contents, @Nullable Integer id,
                @NonNull Boolean completed, int sortOrder, int listNum, int context) {

        this.contents = contents;
        this.id = id;
        this.completed = completed;
        this.sortOrder = sortOrder;

        this.listNum = listNum;
        this.context = context;
        //default goal is non-recurring, so recurrence data is irrelevent and can be initialized to 0
        this.recurrenceType = 0;
        this.dayStarting = 0;
        this.monthStarting = 0;
        this.yearStarting = 0;
        this.dayOfWeekToRecur = 0;
        this.weekOfMonthToRecur = 0;

    }

    //overloaded constructor for use in withRecurringData; could refactor into a factory,
    //but this is (as of now) the only other constructor the class will have
    public Goal(@NonNull String contents, @Nullable Integer id,
                @NonNull Boolean completed, int sortOrder, int listNum, int context, int recurrenceType,
                int dayStarting, int monthStarting, int yearStarting, int dayOfWeekToRecur,
                int weekOfMonthToRecur) {
        this.contents = contents;
        this.id = id;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.listNum = listNum;

        this.recurrenceType = recurrenceType;

        // do we really need starting though
        this.dayStarting = dayStarting;
        this.monthStarting = monthStarting;
        this.yearStarting = yearStarting;

        this.dayOfWeekToRecur = dayOfWeekToRecur; // Friday is 7, Saturday is 1
        this.weekOfMonthToRecur = weekOfMonthToRecur;

        this.context = context;

    }

    public @NonNull String contents(){
        return this.contents;
    }
    public @Nullable Integer id(){
        return this.id;
    }
    public @NonNull Boolean completed() { return this.completed; }
    public int sortOrder() {
        return this.sortOrder;
    }
    public int listNum() {
        return this.listNum;
    }

    public int recurrenceType() {
        return this.recurrenceType;
    }

    public int dayStarting() {
        return this.dayStarting;
    }
    public int monthStarting() { return this.monthStarting; }
    public int yearStarting() {
        return this.yearStarting;
    }

    public int dayOfWeekToRecur() {
        return this.dayOfWeekToRecur;
    }

    public int weekOfMonthToRecur() {
        return this.weekOfMonthToRecur;
    }



    public @NonNull Goal withId(@NonNull Integer id){
        return new Goal(this.contents, id, this.completed, this.sortOrder, this.listNum, this.context,
                this.recurrenceType, this.dayStarting, this.monthStarting, this.yearStarting,
                this.dayOfWeekToRecur, this.weekOfMonthToRecur
        );
    }
    public @NonNull Goal withSortOrder(int sortOrder){
        return new Goal(this.contents, this.id, this.completed, sortOrder, this.listNum,
                this.recurrenceType, this.context, this.dayStarting, this.monthStarting,
                this.yearStarting, this.dayOfWeekToRecur, this.weekOfMonthToRecur
        );
    }
    public @NonNull Goal withComplete(@NonNull Boolean completed){
        return new Goal(this.contents, this.id, completed, this.sortOrder, this.listNum, this.context,
                this.recurrenceType, this.dayStarting, this.monthStarting, this.yearStarting,
                this.dayOfWeekToRecur, this.weekOfMonthToRecur
        );
    }
    public @NonNull Goal withListNum(int listNum){
        return new Goal(this.contents, this.id, completed, this.sortOrder, listNum, this.context,
                this.recurrenceType, this.dayStarting, this.monthStarting, this.yearStarting,
                this.dayOfWeekToRecur, this.weekOfMonthToRecur
        );
    }

    //new methods
    public @NonNull Goal withRecurrenceData(int recurrenceType, int dayStarting,
                                            int monthStarting, int yearStarting, int dayOfWeekToRecur,
                                            int weekOfMonthToRecur){
        return new Goal(this.contents, this.id, this.completed, this.sortOrder, this.listNum,
                this.context, recurrenceType, dayStarting, monthStarting, yearStarting,
                dayOfWeekToRecur, weekOfMonthToRecur
                );
    }
    //returns a version of this goal with any recurrence data cleared
    public @NonNull Goal withoutRecurrence(){
        return new Goal(this.contents, null, this.completed, this.sortOrder, this.listNum, this.context);
    }

    public int context() {
        return this.context;
    }

    public @NonNull Goal withContext(int context){
        return new Goal(this.contents, this.id, this.completed, this.sortOrder,this.listNum, context,
                this.recurrenceType, this.dayStarting, this.monthStarting, this.yearStarting,
                this.dayOfWeekToRecur, this.weekOfMonthToRecur);

    }

//    public @NonNull Goal copiedGoal(){
//        return new Goal(this.contents, this.id, this.completed, this.sortOrder);
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return sortOrder == goal.sortOrder && Objects.equals(contents, goal.contents) && Objects.equals(id, goal.id) && Objects.equals(completed, goal.completed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents, id, completed, sortOrder);
    }
}