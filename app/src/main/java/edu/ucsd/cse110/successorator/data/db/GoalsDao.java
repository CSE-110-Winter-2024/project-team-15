package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

@Dao
public interface GoalsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GoalEntity goal);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<GoalEntity> goals);

    @Query("SELECT * FROM goals WHERE id = :id")
    GoalEntity find(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    List<GoalEntity> findAll();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT Count(*) FROM goals")
    int count();
    @Query("SELECT Min(sort_order) FROM goals")
    int getMinSortOrder();
    @Query("SELECT Max(sort_order) FROM goals")
    int getMaxSortOrder();

    @Query("UPDATE goals SET sort_order = sort_order + :by " +
            "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Query("SELECT Max(sort_order) FROM goals WHERE completed = false")
    int getMaxIncompleteSortOrder();
    @Query("SELECT Min(sort_order) FROM goals WHERE completed = true")
    int getMinCompleteSortOrder();

    //I don't know if query update happens before or after method body, definitely need to test
    @Query("UPDATE goals SET sort_order = sort_order + 1 " +
            "WHERE completed = true")
    default int shiftCompletedSortOrders(){
        int compl = getMinCompleteSortOrder() - 1;
        int incom = getMaxIncompleteSortOrder() + 1;
        //assumes query update runs before this and that
        //finding no goals for the query causes 0 to return on above getters
        //if that is true, then minCompleterSortOder is -1 iff no completes exist,
        //issue could more easily be solved if there is a way to make getMaxIncomplete default a -1
        if(compl == -1){
            return incom;
        }else{return compl;}
    }

    @Transaction
    default int prepend(GoalEntity goal){
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newGoal = new GoalEntity(
                goal.contents, getMinSortOrder()-1, goal.completed
        );
        return Math.toIntExact(insert(newGoal));
    }
    @Transaction
    default void insertUnderIncompleteGoals(GoalEntity goal){
        GoalEntity gol = new GoalEntity(
                goal.contents, shiftCompletedSortOrders(), goal.completed
        );
        insert(gol);
    }

    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);
}
