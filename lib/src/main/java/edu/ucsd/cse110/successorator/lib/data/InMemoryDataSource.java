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
    public InMemoryDataSource(){
        /*EMPTY*/
    }
    public List<Goal> getGoals(){
        return List.copyOf(goals.values());
    }
    public Goal getGoal(int id){
        return goals.get(id);
    }

    // this is how they did it in lab
    // however may be good idea to change it to potentially return null
    // this is because this getter method also SETS. NOT SO SRP

    // but put goal should make a goal subject? so it's a bit
    // strange to make a new subject for any id...
    // alas this is lab work.
    // maybe ill ask in OH or Tutor hours
    // - Keren blurb
    public MutableSubject<Goal> getGoalSubject(int id) {
        if (!goalSubjects.containsKey(id)) {
            var subject = new MutableSubject<Goal>();
            subject.setValue(getGoal(id));
            goalSubjects.put(id, subject);
        }
        return goalSubjects.get(id);
    }

    public MutableSubject<List<Goal>> getAllGoalsSubject() {
        return allGoalsSubject;
    }

    public void putGoal(Goal goal) {
        goals.put(goal.id(), goal);
        if (goalSubjects.containsKey(goal.id())) {
            goalSubjects.get(goal.id()).setValue(goal);
        }
        allGoalsSubject.setValue(getGoals());
    }

}
