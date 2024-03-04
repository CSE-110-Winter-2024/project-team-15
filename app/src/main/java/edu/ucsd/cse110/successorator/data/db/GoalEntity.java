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

<<<<<<< HEAD
    @ColumnInfo(name = "list_num")
    public int listNum;

    GoalEntity(@NonNull String contents, int sortOrder, boolean completed, int listNum){
        this.contents = contents;
        this.sortOrder = sortOrder;
        this.completed = completed;
        this.listNum = listNum;
    }
    public static GoalEntity fromGoal(@NonNull Goal goal){
        var gol = new GoalEntity(goal.contents(), goal.sortOrder(), goal.completed(), goal.listNum());
=======
    @ColumnInfo(name = "context")
    public String context;

    GoalEntity(@NonNull String contents, int sortOrder, boolean completed, String context){
        this.contents = contents;
        this.sortOrder = sortOrder;
        this.completed = completed;
        this.context = context;
    }
    public static GoalEntity fromGoal(@NonNull Goal goal){
        var gol = new GoalEntity(goal.contents(), goal.sortOrder(), goal.completed(), goal.context());
>>>>>>> 11039fe (US5-task 2)
        gol.id = goal.id();
        return gol;
    }
    public @NonNull Goal toGoal(){
<<<<<<< HEAD
        return new Goal(contents, id, completed, sortOrder, listNum);
=======
        return new Goal(contents, id, completed, sortOrder, context);
>>>>>>> 11039fe (US5-task 2)
    }
}
