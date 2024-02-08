package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;

public class InMemoryDataSource {
    private final Map<Integer, Goal> goals = new HashMap<>();
    // remember to implement Subject
    private final Map<Integer, MutableSubject<Goal>> goalSubjects = new HashMap<>();
    private final MutableSubject<List<Goal>> allGoalsSubject = new MutableSubject<>();
    public InMemoryDataSource(){/*EMPTY*/}
    public List<Goal> getGoals(){
        return List.copyOf(goals.values());
    }
    public Goal getGoal(int id){
        return goals.get(id);
    }
    public void putGoal(Goal goal) {
        goals.put(goal.id(), goal);
        if (goalSubjects.containsKey(goal.id())) {
            goalSubjects.get(goal.id()).setValue(goal);
        }
        allGoalsSubject.setValue(getGoals());
    }

}
