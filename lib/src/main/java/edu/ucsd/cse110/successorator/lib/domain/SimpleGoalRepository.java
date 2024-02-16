package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {
    private final InMemoryDataSource dataSource;

    public SimpleGoalRepository(InMemoryDataSource dataSource) {
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

    @Override
    public void append(Goal goal) {
        dataSource.putGoal(
                goal.withSortOrder(dataSource.getMaxSortOrder() + 1)
        );
    }

    // I renamed this but I can easily refactor back, let me know...
    @Override
    public void insertUnderIncompleteGoals(Goal goal){
        dataSource.putGoal(
                goal.withSortOrder(dataSource.shiftCompletedSortOrders())
        );
    }


    // Yoav convinced me I should combine moveCompleteGoal and moveIncompleteGoal into
    // toggleCompleteGoal ... it was a good idea
    public void toggleCompleteGoal(Goal goal) {
        var toggledGoal = goal.withComplete(!goal.completed());

        if(toggledGoal.completed()) {
            // the reason I can use insertUnderIncompleteGoals here is because
            // we want to put new completed goals under the incomplete goals
            insertUnderIncompleteGoals(toggledGoal);
        } else {
            // complete -> incomplete puts them at the top of the list
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
}
