package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Subject<Goal> find(int id);

    Subject<List<Goal>> findAll();

    void save(Goal goal);

    void save(List<Goal> goals);

    void remove(int id);

    void append(Goal goal);

    void insertUnderIncompleteGoals(Goal goal);

// Keeping these just in case it's necessary for me to separate toggleCompleteGoal into them
// The code for them is in a previous commit around 6pm 2/15, titled "in progress tests part 1"
// They were merged into toggleCompleteGoal
//    void moveCompleteGoal(Goal goal);
//
//    void moveIncompleteGoal(Goal goal);

    void toggleCompleteGoal(Goal goal);

    void prepend(Goal goal);

    void clearCompletedGoals();
    void setLastUpdated(String lastUpdated);
    String getLastUpdated();




}
