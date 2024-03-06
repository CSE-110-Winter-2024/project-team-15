package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {
    private final InMemoryDataSource dataSource;
    private String lastUpdated;

    public SimpleGoalRepository(InMemoryDataSource dataSource) {
        this.lastUpdated ="";
        this.dataSource = dataSource;
    }

    @Override
    public Subject<Goal> find(int id) {
        return dataSource.getGoalSubject(id);
    }

    @Override
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalsSubject();
    }

    public Subject<List<Goal>> findAll(int listNum) {
        return dataSource.getViewGoalsSubject(listNum);
    }


    @Override
    public void save(Goal goal) {
        dataSource.putGoal(goal);
    }

    @Override
    public void save(List<Goal> goals) {
        dataSource.putGoals(goals);
    }

    @Override
    public void remove(int id) {
        dataSource.removeGoal(id);
    }

//    @Override
//    public void append(Goal goal) {
//        dataSource.putGoal(
//                goal.withSortOrder(dataSource.getMaxSortOrder() + 1)
//        );
//    }

    // I renamed this but I can easily refactor back, let me know...
    @Override
    public void insertUnderIncompleteGoals(Goal goal){
        dataSource.putGoal(
                goal.withSortOrder(dataSource.shiftCompletedSortOrders())
        );
    }


    // US11 and US8 cover this method
    // NOTE: Newly completed goals will have a sortOrder of incomplete max sort order +1.
    // This can lead to sort orders like 1,2,3 turning into 1,3,4, and it can build up, but
    // it still functions correctly.
    // Additionally, newly incompleted goals will have a sortOrder of whatever is currently
    // at the top. If there's only one goal that gets toggled consistently this
    // number will just keep going up
    public void toggleCompleteGoal(Goal goal) {
        var toggledGoal = goal.withComplete(!goal.completed());

        if(toggledGoal.completed()) {
            insertUnderIncompleteGoals(toggledGoal);
        } else {
            prepend(toggledGoal);
        }

    }

    @Override
    public void prepend(Goal goal) {
        dataSource.shiftSortOrders(0, dataSource.getMaxSortOrder(), 1);
        dataSource.putGoal(
                goal.withSortOrder(dataSource.getMinSortOrder() - 1)
        );
    }
    @Override
    public String getLastUpdated(){ return this.lastUpdated; }
    @Override
    public void setLastUpdated(String lastUpdated){ this.lastUpdated = lastUpdated; }

    @Override
    public void clearCompletedGoals(){
        List<Goal> goals = dataSource.getGoals();
        goals.forEach(
                (goal) -> {
                    if(goal.completed()){
                        dataSource.removeGoal(goal.id());
                    }
                });
    }


    //don't know if these should even be implemented
    public void addDaylies(int day, int month, int year){

    }
    public void addRecurrencesToTomorrowForDate(int day, int month, int year, int dayOfWeek, int weekOfMonth){

    }
    public void addWeeklies(int day, int month, int year, int dayOfWeek){

    }

    /*
    commenting out until I'm sure I either do or dont need this
    @Override
    public void switchViewType(int listNum){

    }
    */


}
