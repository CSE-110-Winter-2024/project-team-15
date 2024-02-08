package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class InMemoryDataSource {
    private final Map<Integer, Goal> goals = new HashMap<>();
    // we are using the SimpleSubject implementation of MutableSubject for now
    // if this causes issues, we should reimplement MutableSubject
    // maybe we should also add the ability to return immutable Subjects later
    // - Yoav blurb
    private final Map<Integer, MutableSubject<Goal>> goalSubjects = new HashMap<>();
    private final MutableSubject<List<Goal>> allGoalsSubject = new SimpleSubject<>();
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
            var subject = new SimpleSubject<Goal>();
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
    public final static List<Goal> DEFAULT_GOALS = List.of(
            new Goal("Prepare for the midterm", 0),
            new Goal("Grocery shopping", 1),
            new Goal("Make dinner", 2),
            new Goal("Text Maria", 3)
//            new Goal("This Massive Wall Of Text Goes On And On For All Eternity Or At Least Until It gets Off THe Screen In which Case You Will Stop Seeing It At All", 4)
    );
    public static InMemoryDataSource fromDefault(){
        var data = new InMemoryDataSource();
        for (Goal goal : DEFAULT_GOALS) {
            data.putGoal(goal);
        }
        return data;
    }
}
