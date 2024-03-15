package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.Subject;

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

    // livedata since the used findall is livedata ... basically a subject right
    @Query("SELECT * FROM goals WHERE recurrence_type > 0")
    LiveData<List<GoalEntity>> findAllWithRecurrenceLiveData();
    @Query("SELECT * FROM goals WHERE recurrence_type > 0 AND list_num = 3")
    List<GoalEntity> findAllWithRecurrence();

    @Query("SELECT Count(*) FROM goals")
    int count();
    @Query("SELECT Min(sort_order) FROM goals")
    int getMinSortOrder();

    @Query("SELECT Min(sort_order) FROM goals WHERE (context >= :context AND context <= 4) AND completed == false")
    Integer getMinIncompleteSortOrderForContext(int context);

    @Query("SELECT Max(sort_order) FROM goals")
    int getMaxSortOrder();

    @Query("UPDATE goals SET sort_order = sort_order + :by " +
            "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Query("SELECT Max(sort_order) FROM goals WHERE completed = false AND context <= 4")
    Integer getMaxIncompleteSortOrder();


    //queries daily goals (recurrence_type = 1) that have a starting date at or before the date of
    //the argument
    @Query("SELECT * FROM goals WHERE recurrence_type = 1 AND " +
            "(((372 * :year)+(31 * :month)+(:day)) >= " +
            "((372 * year_starting)+(31 * month_starting)+(day_starting)))")
    List<GoalEntity> getStartedDailyGoals(int day, int month, int year);

    //queries yearly goals (recurrence_type = 4) that have the specified day and month and are set
    //to start at or before the specified year
    @Query("SELECT * FROM goals WHERE recurrence_type = 4 AND year_starting <= :year " +
            "AND month_starting = :month AND day_starting = :day")
    List<GoalEntity> getStartedYearlyGoalsForToday(int day, int month, int year);

    //queries weekly goals (recurrence_type = 2) that have a starting date at or before the date of
    //the argument and are set to recur on the passed dayOfWeek
    @Query("SELECT * FROM goals WHERE recurrence_type = 2 AND " +
            "(((372 * :year)+(31 * :month)+(:day)) >= " +
            "((372 * year_starting)+(31 * month_starting)+(day_starting)))" +
            "AND (day_of_week_to_recur == :todayOfWeek)")
    List<GoalEntity> getStartedWeeklyGoalsForToday(int day, int month, int year, int todayOfWeek);

    //queries monthly goals (recurrence_type = 3) that have a starting date at or before the date of
    //the argument and are set to recur on the passed dayOfWeek as well as the passed weekOfMonth
    @Query("SELECT * FROM goals WHERE recurrence_type = 3 AND " +
            "(((372 * :year)+(31 * :month)+(:day)) >= " +
            "((372 * year_starting)+(31 * month_starting)+(day_starting)))" +
            "AND (day_of_week_to_recur == :todayOfWeek) " +
            "AND (week_of_month_to_recur == :weekOfMonth)")
    List<GoalEntity> getStartedMonthlyGoalsForToday(int day, int month, int year, int todayOfWeek, int weekOfMonth);

    @Query("SELECT Max(sort_order) FROM goals WHERE completed = false AND (context <= :context AND context <=4)")
    Integer getMaxIncompleteSortOrderWithContext(int context);



    @Query("UPDATE goals SET sort_order = sort_order + 1 " +
            "WHERE completed = true")
    void shiftCompletedSortOrders();

    @Query("UPDATE goals SET sort_order = sort_order + 1 " +
            "WHERE completed = true OR context > :context")
    void shiftSortOrdersAfterContext(int context);

    @Transaction
    default int prepend(GoalEntity goal){
        shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
        var newGoal = new GoalEntity(
                goal.contents, getMinSortOrder()-1, goal.completed, goal.listNum,
                goal.context, goal.recurrenceType, goal.dayStarting, goal.monthStarting,
                goal.yearStarting, goal.dayOfWeekToRecur, goal.weekOfMonthToRecur

        );
        return Math.toIntExact(insert(newGoal));
    }

    @Transaction
    default int prependWithContext(GoalEntity goal){

        int context = goal.context;
        Integer minContextOrder = getMinIncompleteSortOrderForContext(context);
        //need to make sure that if there are no goals with this context or one of lesser priority
        //the goal is not prepended before goals with higher context priority
        if(minContextOrder == null){
            if(context > 0){
                minContextOrder = getMaxIncompleteSortOrderWithContext(context-1);
                //need to move to 1 after the highest sort order among the previous context or
                //start of the list if there are no other incomplete goals
                if(minContextOrder == null){minContextOrder = 0;} else {minContextOrder++;}
            } else {minContextOrder = 0;}
        }

        shiftSortOrders(minContextOrder, getMaxSortOrder(), 1);
        var newGoal = new GoalEntity(

                goal.contents, minContextOrder, goal.completed, goal.listNum, goal.context,
                goal.recurrenceType, goal.dayStarting, goal.monthStarting,
                goal.yearStarting, goal.dayOfWeekToRecur, goal.weekOfMonthToRecur
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
                goal.contents, incomp, goal.completed, goal.listNum, goal.context,
                goal.recurrenceType, goal.dayStarting, goal.monthStarting, goal.yearStarting,
                goal.dayOfWeekToRecur, goal.weekOfMonthToRecur
        );
        insert(gol);
    }
    @Transaction
    default void insertUnderIncompleteGoalsWithContext(GoalEntity goal){

        int context = goal.context;
        shiftSortOrdersAfterContext(context);

        Integer incomp = getMaxIncompleteSortOrderWithContext(context);
        if(incomp == null) {
            incomp = 0;
        }
        else {
            incomp = incomp+1;
        }

        GoalEntity gol = new GoalEntity(
                goal.contents, incomp, goal.completed, goal.listNum, goal.context,
                goal.recurrenceType, goal.dayStarting, goal.monthStarting, goal.yearStarting,
                goal.dayOfWeekToRecur, goal.weekOfMonthToRecur
        );
        insert(gol);
    }

    @Transaction
    default void toggleCompleteGoal(Goal goal) {
        var toggledGoal = GoalEntity.fromGoal(goal.withComplete(!goal.completed()));
        delete(Integer.valueOf(goal.id())); // delete old goal, insert new one

        // then add into correct location with toggled complete
        if (toggledGoal.completed) {
            //mark complete is not supposed to care about context, so this still has use
            insertUnderIncompleteGoals(toggledGoal);
        } else {
            //use with context now as this is new desired behaviour of un-toggle
            prependWithContext(toggledGoal);
        }
    }
    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);
    @Query("DELETE FROM goals WHERE completed = true")
    void clearCompletedGoals();

    @Query("SELECT context FROM goals WHERE id = :id")
    int getContext(int id);
}
