package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalBuilder;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
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
    public void addDaylies(int day, int month, int year){
        var goalContents = goalsDao.getStartedDailyGoals(day, month, year);
        for(String title: goalContents){
            Goal toAdd = new Goal(title, null, false, -1, 1);
            this.insertUnderIncompleteGoals(toAdd);

        }
    }
    public void addWeeklies(int day, int month, int year, int dayOfWeek){
        var goalContents = goalsDao.getStartedWeeklyGoalsForToday(day, month, year, dayOfWeek);
        for(String title: goalContents){
            Goal toAdd = new Goal(title, null, false, -1, 1);
            this.insertUnderIncompleteGoals(toAdd);

        }
    }
    public void addRecurrencesToTomorrowForDate(int day, int month, int year, int dayOfWeek, int weekOfMonth){
        addDaylies(day, month, year);
        addWeeklies(day, month, year, dayOfWeek);
    }
    public void toggleCompleteGoal(Goal goal){
        goalsDao.toggleCompleteGoal(goal);
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
        var goalsLiveData = findAllRecurringTomorrow();

        // to avoid null exception
        if (goalsLiveData.getValue() == null || goalsLiveData == null) {
            return;
        }

        // the goals we want to add to tomorrow's view
        var toAdd = goalsLiveData.getValue().stream()
                .map(goal -> goal.withoutRecurrence().withListNum(1))
                .collect(Collectors.toList());

        // since we're going to add our recurring goals to tomorrow
        // the next time they should recur needs to be adjusted
        // to its next recurrence
        var recurringDateAdjusted = goalsLiveData.getValue().stream()
                .map(this::increment)
                .collect(Collectors.toList());

        // here we add the goals
        toAdd.stream()
                .sorted(SimpleDateTracker::compareGoals)
                .forEach(this::insertUnderIncompleteGoals);

        // here we update the recurring goals
        save(recurringDateAdjusted);
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
                        return SimpleDateTracker.getInstance().getValue().
                                compareGoalToTomorrow(incrGoal);
                    })
                    .collect(Collectors.toList());
        });
        return goalsLiveData;
    }

    private Goal increment(Goal goal){
        GoalBuilder builder = new GoalBuilder().withDefault(goal);
        switch(goal.recurrenceType()){
            case 1: builder.addDays(1);  break;
            case 2: builder.addWeeks(1); break;
            case 3: builder.addMonths(1); break;
            case 4: builder.addYear(1); break;
            // this shouldn't happen but if it does...
            default: throw new IllegalArgumentException();
        }
        return builder.build();
    }
//    private boolean compareDates(Goal goal1, Goal goal2){
//
//    }
}
