package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.DateTracker;
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
    private int lastUpdatedYear;

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
    public void setLastRecurrence(LocalDate last){
        goalsDao.insert(GoalEntity.fromGoal(
                new Goal("IF YOU SEE THIS, YOU ARE LOST", 9999,
                        false, 9999, 9999, 5)
                .withRecurrenceData(0, last.getDayOfMonth(), last.getMonthValue(),
                        last.getYear(),0,0)));
    }
    public LocalDate getLastRecurrence(){
        var ourEntity = goalsDao.find(9999);
        if (ourEntity == null){
            setLastRecurrence(LocalDate.now());
            ourEntity = goalsDao.find(9999);
        }
        var ourGoal = ourEntity.toGoal();
        return LocalDate.of(ourGoal.yearStarting(), ourGoal.monthStarting(),
                ourGoal.dayStarting());
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

    //method should probably be deprecated
    public void insertUnderIncompleteGoals(Goal goal){
        goalsDao.insertUnderIncompleteGoals(GoalEntity.fromGoal(goal));
    }

    //adds daily goals that should be added for month/day/year
    public void addDaylies(int day, int month, int year){
        //query daily goals that have started
        var goalContents = goalsDao.getStartedDailyGoals(day, month, year);

        //the following lines are present in all add___lies methods and would probably do well in a
        //separate method for DRY purposes
        insertAllUnderIncomplete(
                goalContents.stream()
                        .map(GoalEntity::toGoal)
                        .collect(Collectors.toList())
                        .stream()
                        .map(goal -> goal.withoutRecurrence().withListNum(1))
                        .collect(Collectors.toList())
        );
    }

    public void addWeeklies(int day, int month, int year, int dayOfWeek){
        //query weekly goals that recur on dayOfWeek and that have started
        var goalContents = goalsDao.getStartedWeeklyGoalsForToday(day, month, year, dayOfWeek);

        //add queried goals to repository
        insertAllUnderIncomplete(
                goalContents.stream()
                        .map(GoalEntity::toGoal)
                        .collect(Collectors.toList())
                        .stream()
                        .map(goal -> goal.withoutRecurrence().withListNum(1))
                        .collect(Collectors.toList())
        );
    }
    /*

     */

    public void addMonthlies(int day, int month, int year, int dayOfWeek, int weekOfMonth){
        //6 is used to mean that this is a day for both 1st and 5th week of month
        List<GoalEntity> goalContents;
        if(weekOfMonth == 6) {
            //when weekOfMonth is 6, we want to load in goals recurring on both the 1st
            //and 5th ocurrence of the dayOfWeek
            goalContents = goalsDao.getStartedMonthlyGoalsForToday(day, month, year, dayOfWeek,
                    1);
            goalContents.addAll(goalsDao.getStartedMonthlyGoalsForToday(day, month, year, dayOfWeek,
                    5));
        }
        else {
            //for normal values, just query started monthly goals for the specified weekOfMonth
            //occurrence of dayOfWeek
            goalContents = goalsDao.getStartedMonthlyGoalsForToday(day, month, year, dayOfWeek,
                    weekOfMonth);
        }
        //add queried goals to repository
        insertAllUnderIncomplete(
                goalContents.stream()
                        .map(GoalEntity::toGoal)
                        .collect(Collectors.toList())
                        .stream()
                        .map(goal -> goal.withoutRecurrence().withListNum(1))
                        .collect(Collectors.toList())
        );
    }

    public void addYearlies(int day, int month, int year, boolean isLeapYear){
        //queries yearly goals for month/day that start on or before year
        var goalContents = goalsDao.getStartedYearlyGoalsForToday(day, month, year);
        //on March 1st of non-leap year also add yearly goals for Feb29th
        if(!isLeapYear && (day == 1 && month == 3)){
            goalContents.addAll(goalsDao.getStartedYearlyGoalsForToday(29, 2, year));
        }
        //add queried goals to repository
        insertAllUnderIncomplete(
                goalContents.stream()
                        .map(GoalEntity::toGoal)
                        .collect(Collectors.toList())
                        .stream()
                        .map(goal -> goal.withoutRecurrence().withListNum(1))
                        .collect(Collectors.toList())
        );
    }

    //adds goals that should recur on the passed date to the repository
    public void addRecurrencesToTomorrowForDate(int day, int month, int year, int dayOfWeek,
                                                int weekOfMonth, boolean isLeapYear){
        addDaylies(day, month, year);
        addWeeklies(day, month, year, dayOfWeek);
        addMonthlies(day, month, year, dayOfWeek, weekOfMonth);
        addYearlies(day, month, year, isLeapYear);
        try {
            LocalDate T = LocalDate.of(year, month, day);
            addRecurrencesBeforeDate(T, 0);
            // might be possible to do something like
            // addRecurrencesInRange(T, T.plusDays(1), 1)
            // this could replace the four method calls above
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void addRecurrencesInRange(LocalDate L, LocalDate T, int view) {
        var range = goalsDao.findAllWithRecurrence();
        range.stream()
                .map(GoalEntity::toGoal)
                .filter(goal -> {
                    try {
                        return ComplexDateTracker.shouldHappen(goal, L, T);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(goal -> goal.withoutRecurrence().withListNum(view))
                // change this if necessary
                .forEach(this::insertUnderIncompleteGoals);
    }
    public void addRecurrencesBeforeDate(LocalDate T, int view) throws Exception {
        //need to add last-updated-year field to repo to make a local date out of it
//        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");
        LocalDate L = getLastRecurrence();
        //if range is too small to be adding goals to today, don't add them
        if(!T.isAfter(L.plusDays(2))){return;}
        addRecurrencesInRange(L, T, view);}

    public void insertUnderIncompleteGoalsWithContext(Goal goal){
        goalsDao.insertUnderIncompleteGoalsWithContext(GoalEntity.fromGoal(goal));
    }
    public void toggleCompleteGoal(Goal goal){
        goalsDao.toggleCompleteGoal(goal);
    }
    public void remove(int id){
        goalsDao.delete(id);
    }

    public void clearCompletedGoals() { goalsDao.clearCompletedGoals(); }

    public String getLastUpdated(){ return this.lastUpdated; }

    //may have broken tests, but haven't checked yet
    public void setLastUpdated(String lastUpdated, int lastUpdatedYear){
        this.lastUpdated = lastUpdated;
        this.lastUpdatedYear = lastUpdatedYear;
    }

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
    //no longer used, so commenting out
//    private List<Goal> stringToGoal(List<String> titles){
//        return titles.stream()
//                .map(title -> new Goal(title, null, false, -1, 1))
//                .collect(Collectors.toList());
//    }
    private void insertAllUnderIncomplete(List<Goal> all){
        all.forEach(this::insertUnderIncompleteGoals);
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
                newDayOfWeekToRecur, newWeekOfMonthToRecur);

        return newGoal;
    }
    public int getContext(int id){ return goalsDao.getContext(id); }

}
