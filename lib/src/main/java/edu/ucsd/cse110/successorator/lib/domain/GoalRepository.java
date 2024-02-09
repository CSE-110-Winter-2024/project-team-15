package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;

public class GoalRepository {
    //need to add dummy goal repository
    private final InMemoryDataSource dataSource;
    public GoalRepository(InMemoryDataSource dataSource){
        this.dataSource = dataSource;
    }
    public Integer count() {
        return dataSource.getGoals().size();
    }

    public MutableSubject<Goal> find(int id) {
        return dataSource.getGoalSubject(id);
    }

    public MutableSubject<List<Goal>> findAll() {
        return dataSource.getAllGoalsSubject();
    }

    public void save(Goal goal) {
        dataSource.putGoal(goal);
    }
}
