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

    @ColumnInfo(name = "context")
    public String context;

    @ColumnInfo(name = "list_num")
    public int listNum;



    GoalEntity(@NonNull String contents, int sortOrder, boolean completed, int listNum, String context){
        this.contents = contents;
        this.sortOrder = sortOrder;
        this.completed = completed;
        this.listNum = listNum;
        this.context = context;
    }
    public static GoalEntity fromGoal(@NonNull Goal goal){
        var gol = new GoalEntity(goal.contents(), goal.sortOrder(), goal.completed(), goal.listNum(), goal.context());
        gol.id = goal.id();
        return gol;
    }
    public @NonNull Goal toGoal(){
        return new Goal(contents, id, completed, sortOrder, listNum, context);
    }
}
