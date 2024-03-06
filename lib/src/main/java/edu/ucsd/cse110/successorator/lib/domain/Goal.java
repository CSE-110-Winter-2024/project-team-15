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
    private final String context;

    public Goal(@NonNull String contents, @Nullable Integer id,
                @NonNull Boolean completed, int sortOrder, int listNum, String context) {

        this.contents = contents;
        this.id = id;
        this.completed = completed;
        this.sortOrder = sortOrder;

        this.listNum = listNum;
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
    public String context() {
        return this.context;
    }
    public @NonNull Goal withId(@NonNull Integer id){
        return new Goal(this.contents, id, this.completed, this.sortOrder, this.listNum, this.context);
    }
    public @NonNull Goal withSortOrder(int sortOrder){
        return new Goal(this.contents, this.id, this.completed, sortOrder, this.listNum, this.context);
    }
    public @NonNull Goal withComplete(@NonNull Boolean completed){
        return new Goal(this.contents, this.id, completed, this.sortOrder, this.listNum, this.context);
    }
    public @NonNull Goal withListNum(int listNum){
        return new Goal(this.contents, this.id, completed, this.sortOrder, listNum, this.context);
    }

    public @NonNull Goal withContext(String context){
        return new Goal(this.contents, this.id, this.completed, this.sortOrder,this.listNum, context);

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