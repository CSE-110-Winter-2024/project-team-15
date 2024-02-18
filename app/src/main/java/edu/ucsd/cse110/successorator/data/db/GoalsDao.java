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
    Integer getMaxIncompleteSortOrder();


    //I don't know if query update happens before or after method body, definitely need to test
    //also unsure if it matters as long as both happen now
    @Query("UPDATE goals SET sort_order = sort_order + 1 " +
            "WHERE completed = true")
    void shiftCompletedSortOrders();

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

        shiftCompletedSortOrders();

        Integer incomp = getMaxIncompleteSortOrder();
        if(incomp == null) {
            incomp = 0;
        }
        else {
            incomp = incomp+1;
        }

        GoalEntity gol = new GoalEntity(
                goal.contents, incomp, goal.completed
        );
        insert(gol);
    }

    @Transaction
    default void toggleCompleteGoal(Goal goal) {
        var toggledGoal = GoalEntity.fromGoal(goal.withComplete(!goal.completed()));
        delete(Integer.valueOf(goal.id())); // delete old goal, insert new one

        // then add into correct location with toggled complete
        if (toggledGoal.completed) {
            insertUnderIncompleteGoals(toggledGoal);
        } else {
            prepend(toggledGoal);
        }
    }
    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);
}
