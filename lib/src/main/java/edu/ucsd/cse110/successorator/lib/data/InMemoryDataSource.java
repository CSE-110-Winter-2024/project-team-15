package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class InMemoryDataSource {
    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;
    private int minSortOrderCompleted = minSortOrder;
    private int nextId = 0;
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
        // we don't set observers for these subjects... yet
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
        var fixedCard = preInsert(goal);

        goals.put(fixedCard.id(), fixedCard);
        postInsert();
        assertSortOrderConstraints();

        if (goalSubjects.containsKey(fixedCard.id())) {
            goalSubjects.get(fixedCard.id()).setValue(fixedCard);
        }
        allGoalsSubject.setValue(getGoals());
    }
    public void putGoals(List<Goal> goalsList) {
        var fixedGoals = goalsList.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedGoals.forEach(goal -> goals.put(goal.id(), goal));
        postInsert();
        assertSortOrderConstraints();

        fixedGoals.forEach(goal -> {
            if (goalSubjects.containsKey(goal.id())) {
                goalSubjects.get(goal.id()).setValue(goal);
            }
        });
        allGoalsSubject.setValue(getGoals());
    }
    public void removeGoal(int id) {
        var card = goals.get(id);
        var sortOrder = card.sortOrder();

        goals.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (goalSubjects.containsKey(id)) {
            goalSubjects.get(id).setValue(null);
        }
        allGoalsSubject.setValue(getGoals());
    }

    public void setComplete(Goal goal) {
        var toggledGoal = goal.withComplete(!goal.completed());
        // goalRepository.save(toggledGoal);

        // just move it to the bottom if there have been no completed goals
        // and set minsortordercompleted
        if(minSortOrderCompleted == minSortOrder) {
            minSortOrderCompleted = maxSortOrder;
        } else {
            // BRAINSTORMING
        }

    }


    public void shiftSortOrders(int from, int to, int by) {
        var cards = goals.values().stream()
                .filter(card -> card.sortOrder() >= from && card.sortOrder() <= to)
                .map(card -> card.withSortOrder(card.sortOrder() + by))
                .collect(Collectors.toList());

        putGoals(cards);
    }


    public int getMinSortOrder() {
        return this.minSortOrder;
    }
    public int getMaxSortOrder() {
        return this.maxSortOrder;
    }

    public int getMinSortOrderCompleted() { return this.minSortOrderCompleted; }


    /**
     * Private utility method to maintain state of the fake DB: ensures that new
     * cards inserted have an id, and updates the nextId if necessary.
     * Directly from Lab 5. Thank you Dylan!
     */
    private Goal preInsert(Goal goal) {
        var id = goal.id();
        if (id == null) {
            // If the goal has no id, give it one.
            goal = goal.withId(nextId++);
        }
        else if (id > nextId) {
            // If the goal has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load goals like in fromDefault().
            nextId = id + 1;
        }

        return goal;
    }
    /**
     * Private utility method to maintain state of the fake DB: ensures that the
     * min and max sort orders are up to date after an insert.
     * Directly from Lab 5. Thank you Dylan!
     */
    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = goals.values().stream()
                .map(Goal::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = goals.values().stream()
                .map(Goal::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }
    /**
     * Safety checks to ensure the sort order constraints are maintained.
     * If any of the constraints are violated, it will throw an AssertionError.
     * We would like to avoid this becoming a problem.
     * Written by Dylan. Thanks Dylan!
     */
    private void assertSortOrderConstraints() {
        // Get all the sort orders...
        var sortOrders = goals.values().stream()
                .map(Goal::sortOrder)
                .collect(Collectors.toList());

        // Non-negative...
        assert sortOrders.stream().allMatch(i -> i >= 0);

        // Unique...
        assert sortOrders.size() == sortOrders.stream().distinct().count();

        // Between min and max...
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }
    public final static List<Goal> DEFAULT_GOALS = List.of(
            new Goal("Prepare for the midterm", 0, false, 0),
            new Goal("Grocery shopping", 1, false, 1),
            new Goal("Make dinner", 2, false, 2),
            new Goal("Text Maria", 3, false, 3)
//             new Goal("This Massive Wall Of Text Goes On And On For All Eternity Or At Least Until It gets Off THe Screen In which Case You Will Stop Seeing It At All", 4)
    );


    public static InMemoryDataSource fromDefault(){
        var data = new InMemoryDataSource();
        for (Goal goal : DEFAULT_GOALS) {
            data.putGoal(goal);
        }
        return data;
    }

    // most likely we don't need this
    public final static List<Goal> DEFAULT_EMPTY = List.of(
    );
    public static InMemoryDataSource fromDefaultEmpty(){
        var data = new InMemoryDataSource();

        // completely unnecessary
        for (Goal goal : DEFAULT_EMPTY) {
            data.putGoal(goal);
        }

        return data;
    }

}
