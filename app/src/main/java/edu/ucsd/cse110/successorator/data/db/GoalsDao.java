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

    @Query("SELECT * FROM goals WHERE list_num = :listNum ORDER BY sort_order")
    LiveData<List<GoalEntity>> findAllAsLiveData(int listNum);

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


    @Query("SELECT contents FROM goals WHERE recurrence_type = 1 AND " +
            "(((372 * :year)+(31 * :month)+(:day)) >= " +
            "((372 * year_starting)+(31 * month_starting)+(day_starting)))")
    List<String> getStartedDailyGoals(int day, int month, int year);

    @Query("SELECT contents FROM goals WHERE recurrence_type = 2 AND " +
            "(((372 * :year)+(31 * :month)+(:day)) >= " +
            "((372 * year_starting)+(31 * month_starting)+(day_starting)))" +
            "AND (day_of_week_to_recur == :todayOfWeek)")
    List<String> getStartedWeeklyGoalsForToday(int day, int month, int year, int todayOfWeek);

    @Query("SELECT contents FROM goals WHERE recurrence_type = 3 AND " +
            "(((372 * :year)+(31 * :month)+(:day)) >= " +
            "((372 * year_starting)+(31 * month_starting)+(day_starting)))" +
            "AND (day_of_week_to_recur == :todayOfWeek) AND week_of_month_to_recur ==:weekOfMonth")
    List<String> getStartedMonthlyGoalsForToday(int day, int month, int year, int todayOfWeek, int weekOfMonth);

    @Query("SELECT contents FROM goals WHERE recurrence_type = 3 AND overflow_flag " +
            "AND day_of_week_to_recur == :todayOfWeek")
    List<String> getFlaggedMonthlyGoalsForToday(int todayOfWeek);

    @Query("UPDATE goals SET sort_order = sort_order + 1 " +
            "WHERE completed = true")
    void shiftCompletedSortOrders();

    @Query("UPDATE goals SET overflow_flag = false WHERE recurrence_type = 3 AND overflow_flag " +
            "AND day_of_week_to_recur == :todayOfWeek")
    void resetMonthlyOverflowFlagForToday(int todayOfWeek);

    @Transaction
    default int prepend(GoalEntity goal){
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newGoal = new GoalEntity(
                goal.contents, getMinSortOrder()-1, goal.completed, goal.listNum,
                goal.recurrenceType, goal.dayStarting, goal.monthStarting, goal.yearStarting,
                goal.dayOfWeekToRecur, goal.weekOfMonthToRecur, goal.overflowFlag
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
                goal.contents, incomp, goal.completed, goal.listNum,
                goal.recurrenceType, goal.dayStarting, goal.monthStarting, goal.yearStarting,
                goal.dayOfWeekToRecur, goal.weekOfMonthToRecur, goal.overflowFlag
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
    @Query("DELETE FROM goals WHERE completed = true")
    void clearCompletedGoals();
}
