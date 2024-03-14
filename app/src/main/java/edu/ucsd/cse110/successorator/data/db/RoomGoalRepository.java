package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalBuilder;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.ComplexDateTracker;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomGoalRepository implements GoalRepository {
    private final GoalsDao goalsDao;
    private String lastUpdated;

    public RoomGoalRepository(GoalsDao goalsDao){
        this.goalsDao = goalsDao;
    }
    //don't understand yet
    public Subject<Goal> find(int id){
        LiveData<GoalEntity> entityLiveData = goalsDao.findAsLiveData(id);
        LiveData<Goal> goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }

    public Subject<List<Goal>> findAll(){
        var entitiesLiveData = goalsDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities ->{
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    public Subject<List<Goal>> findAll(int listNum){
        var entitiesLiveData = goalsDao.findAllAsLiveData(listNum);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities ->{
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }


    public void save(Goal goal){
        goalsDao.insert(GoalEntity.fromGoal(goal));
    }
    public void save(List<Goal> goals){
        var entities = goals.stream()
                .map(GoalEntity::fromGoal)
                .collect(Collectors.toList());
        goalsDao.insert(entities);
    }
    public void prepend(Goal goal){
        goalsDao.prepend(GoalEntity.fromGoal(goal));
    }
    public void insertUnderIncompleteGoals(Goal goal){
        goalsDao.insertUnderIncompleteGoals(GoalEntity.fromGoal(goal));
    }

    //adds daily goals that should be added for month/day/year
    public void addDaylies(int day, int month, int year){
        //query daily goals that have started
        var goalContents = goalsDao.getStartedDailyGoals(day, month, year);

        //the following lines are present in all add___lies methods and would probably do well in a
        //separate method for DRY purposes
        for(String title: goalContents){
            //query returns only the contents string from the goal, so for each goal, create a new
            //one without recurrence for Tomorrow list and add it (if we had time I would change
            //the queries to return a list of goals instead and run
            //title.withoutRecurrence().withListNum(1) instead which would be much better)
            Goal toAdd = new Goal(title, null, false, -1, 1);
            this.insertUnderIncompleteGoals(toAdd);

        }
    }

    public void addWeeklies(int day, int month, int year, int dayOfWeek){
        //query weekly goals that recur on dayOfWeek and that have started
        var goalContents = goalsDao.getStartedWeeklyGoalsForToday(day, month, year, dayOfWeek);

        //add queried goals to repository
        for(String title: goalContents){
            Goal toAdd = new Goal(title, null, false, -1, 1);
            this.insertUnderIncompleteGoals(toAdd);

        }
    }

    public void addMonthlies(int day, int month, int year, int dayOfWeek, int weekOfMonth){
        //6 is used to mean that this is a day for both 1st and 5th week of month
        List<String> goalContents;
        if(weekOfMonth == 6) {
            //when weekOfMonth is 6, we want to load in goals recurring on both the 1st
            //and 5th ocurrence of the dayOfWeek
            goalContents = goalsDao.getStartedMonthlyGoalsForToday(day, month, year, dayOfWeek, 1);
            goalContents.addAll(goalsDao.getStartedMonthlyGoalsForToday(day, month, year, dayOfWeek, 5));
        }
        else {
            //for normal values, just query started monthly goals for the specified weekOfMonth
            //occurrence of dayOfWeek
            goalContents = goalsDao.getStartedMonthlyGoalsForToday(day, month, year, dayOfWeek,
                    weekOfMonth);
        }
        //add queried goals to repository
        for(String title: goalContents){
            Goal toAdd = new Goal(title, null, false, -1, 1);
            this.insertUnderIncompleteGoals(toAdd);
        }
    }

    public void addYearlies(int day, int month, int year, boolean isLeapYear){
        //queries yearly goals for month/day that start on or before year
        var goalContents = goalsDao.getStartedYearlyGoalsForToday(day, month, year);
        //on March 1st of non-leap year also add yearly goals for Feb29th
        if(!isLeapYear && (day == 1 && month == 3)){
            goalContents.addAll(goalsDao.getStartedYearlyGoalsForToday(29, 2, year));
        }
        //add queried goals to repository
        for(String title: goalContents){
            Goal toAdd = new Goal(title, null, false, -1, 1);
            this.insertUnderIncompleteGoals(toAdd);
        }
    }

    //adds goals that should recur on the passed date to the repository
    public void addRecurrencesToTomorrowForDate(int day, int month, int year, int dayOfWeek,
                                                int weekOfMonth, boolean isLeapYear){
        addDaylies(day, month, year);
        addWeeklies(day, month, year, dayOfWeek);
        addMonthlies(day, month, year, dayOfWeek, weekOfMonth);
        addYearlies(day, month, year, isLeapYear);
    }
    public void toggleCompleteGoal(Goal goal){
        goalsDao.toggleCompleteGoal(goal);
    }

    public void toggleCompleteGoal(int id){
        goalsDao.toggleCompleteGoal(id);
    }
    public void remove(int id){
        goalsDao.delete(id);
    }

    public void clearCompletedGoals() { goalsDao.clearCompletedGoals(); }

    public String getLastUpdated(){ return this.lastUpdated; }

    public void setLastUpdated(String lastUpdated){ this.lastUpdated = lastUpdated; }

    // need method to move goals from tomorrow to today



    // let's get recurring goals (from the recurring goals view of course)(see goalsdao)
    // transformation stuff stolen from findall of course
    public Subject<List<Goal>> findAllWithRecurrence() {
        var entitiesLiveData = goalsDao.findAllWithRecurrenceLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    // get list of recurring goals
    // filter out all the ones that aren't supposed to be refreshed
    // add copies of the remaining recurring goals to the tomorrow view (without recurrence)
    // update the remaining recurring goals with the next date of recurrence
    public void refreshRecurrence(){
        var goals = getAllRecurringTomorrow();

        // to avoid null exception
        if (goals == null) return;

        // the goals we want to add to tomorrow's view
        var toAdd = goals.stream()
                .map(goal -> goal.withoutRecurrence().withListNum(1))
                .collect(Collectors.toList());

        //toAdd.add(new Goal("hi", 1, false, 1, 1));

        // since we're going to add our recurring goals to tomorrow
        // the next time they should recur needs to be adjusted
        // to its next recurrence
        var recurringDateAdjusted = goals.stream()
                .map(this::increment)
                .collect(Collectors.toList());

        // here we add the goals
        toAdd.stream()
                //you are calling compare on added goals here which have no recurrence data.
                //I'm guessing sorting by sortOrder would do all you need here,
//                .sorted(SimpleDateTracker::compareGoals)
                .forEach(this::insertUnderIncompleteGoals);

        // here we update the recurring goals
        save(recurringDateAdjusted);
    }


    private List<Goal> getAllRecurringTomorrow(){
        return goalsDao.findAllWithRecurrence().stream()
                .map(GoalEntity::toGoal)
                .filter(goal -> {
                    //compare returns true if the goal comes after tomorrow, so you actually don't
                    //want it to recur if this is true, but you do if equal, so I will switch the sign in
                    //dateTracker and update method behaviour comment
                    return ComplexDateTracker.getInstance().getValue().
                            compareGoalToTomorrow(goal);
                })
                .collect(Collectors.toList());
    }

    private List<Goal> getAllRecurringToday(){
        return goalsDao.findAllWithRecurrence().stream()
                .map(GoalEntity::toGoal)
                .filter(goal -> {
                    //compare returns true if the goal comes after tomorrow, so you actually don't
                    //want it to recur if this is true, but you do if equal, so I will switch the sign in
                    //dateTracker and update method behaviour comment
                    return ComplexDateTracker.getInstance().getValue().
                            compareGoalToToday(goal);
                })
                .collect(Collectors.toList());
    }


    private LiveData<List<Goal>> findAllRecurringTomorrow() {
        var entitiesLiveData = goalsDao.findAllWithRecurrenceLiveData();
        // to avoid null exception
        if (entitiesLiveData == null) return null;

        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .filter(goal -> {
                        Goal incrGoal = increment(goal);
                        return ComplexDateTracker.getInstance().getValue().
                                compareGoalToTomorrow(incrGoal);
                    })
                    .collect(Collectors.toList());
        });
        return goalsLiveData;
    }

    // this method needs to increment the "starting" fields
    // since we're making copies...

    //after hours of toil, abandoning this idea.  it was beautiful, but requires more knowledge
    //than we have time to acrew
    // uwhahahah thank you
    private Goal increment(Goal goal){
        // our tracker handles the new date finding
        ComplexDateTracker myTracker = ComplexDateTracker.getInstance().getValue();
        LocalDateTime goalAsTime = myTracker.goalToLocalDateTime(goal);

        LocalDateTime nextOccurrence = myTracker.getNextOccurrence(goal, goalAsTime);
        int newDayStarting = nextOccurrence.getDayOfMonth();
        int newMonthStarting = nextOccurrence.getMonthValue();
        int newYearStarting = nextOccurrence.getYear();
        int newDayOfWeekToRecur = nextOccurrence.getDayOfWeek().getValue();
        int newWeekOfMonthToRecur = myTracker.getWeekOfMonth(nextOccurrence);

        // if anyone wants to make this use builder instead FEEL FREE
        Goal newGoal = goal.withRecurrenceData(
                goal.recurrenceType(), newDayStarting, newMonthStarting, newYearStarting,
                newDayOfWeekToRecur, newWeekOfMonthToRecur, false);

        return newGoal;
    }

    public void moveTomorrowToToday() {
        //Executors.newSingleThreadExecutor().execute(() -> {
        // Fetch goals set for tomorrow (listNum = 1)
        List<GoalEntity> tomorrowGoals = goalsDao.findAllWithListNum(1);

        // Modify each goal's listNum to 0 (today) and re-insert
        tomorrowGoals.stream()
                .map(GoalEntity::toGoal)
                .forEach(goal -> {
                    Goal newGoal = goal.withListNum(0);
                    // // This re-inserts the goal with the new listNum, replacing the existing row thanks to OnConflictStrategy.REPLACE
                    // alight, apparently this just makes a copy, eugh .. that shouldn't happen
                    // since I don't want to ruin anything.. i'll just delete
                    goalsDao.delete(newGoal.id());
                    goalsDao.insertUnderIncompleteGoals(GoalEntity.fromGoal(newGoal));
                });
        //});
    }
    @Override
    public void changePendingGoalStatus(int id, int listNum) {
        goalsDao.updatesGoalStatus(id,listNum);
    }



}
