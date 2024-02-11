package edu.ucsd.cse110.successorator.lib.data;

import static org.junit.Assert.assertEquals;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class InMemoryDataSourceTest extends TestCase {

    public void testGetGoals() {

        // get inmemory's default
        InMemoryDataSource dataSource = new InMemoryDataSource();

        // have to assume fromDefault works. this gets a list of the default goals.
        dataSource = dataSource.fromDefault();

        // this is what they should be but let's see if it gets it right
        List<Goal> actual = List.of(
                new Goal("Prepare for the midterm", 0, false, 0),
                new Goal("Grocery shopping", 1, false, 1),
                new Goal("Make dinner", 2, false, 2),
                new Goal("Text Maria", 3, false, 3)
        );
        List <Goal> expected = dataSource.getGoals();

        assertEquals(actual, expected);
    }

    public void testGetGoal() {
        InMemoryDataSource dataSource = InMemoryDataSource.fromDefault();
        Goal expected = new Goal("Prepare for the midterm", 0, false, 0);
        Goal actual = dataSource.getGoal(0);
        assertEquals(expected, actual);
    }

    public void testGetGoalSubject() {
        InMemoryDataSource dataSource = InMemoryDataSource.fromDefault();

        // expected goal at index 0 is prep for midterm
        Goal initGoal = new Goal("Prepare for the midterm", 0, false, 0);
        MutableSubject<Goal> expected = new SimpleSubject<Goal>();
        expected.setValue(initGoal);

        // actual goal at index 0 is this
        MutableSubject<Goal> actual = dataSource.getGoalSubject(0);

        // compare the values, which are goals
        assertEquals(expected.getValue(), actual.getValue());
    }

    public void testGetAllGoalsSubject() {
        InMemoryDataSource dataSource = InMemoryDataSource.fromDefault();
        MutableSubject<List<Goal>> actual = dataSource.getAllGoalsSubject();

        // we check expected size against getValue, and since getValue gets us a list, it should be 4
        assertEquals(4, actual.getValue().size());
    }

    public void testPutGoal() {

    }

    public void testPutGoals() {
        InMemoryDataSource dataSource = new InMemoryDataSource();

        Goal gol1 = new Goal("This is a goal", 1, true, 3);
        Goal gol2 = new Goal("This is a goal!", 2, true, 4);

        Map<Integer, Goal> goalz = new HashMap<>();
        goalz.put(gol1.id(), gol1);
        goalz.put(gol2.id(), gol2);

        // what it should be after putting
        List<Goal> listCopyGoal = List.copyOf(goalz.values());

        // here's us after putting
        dataSource.putGoals(listCopyGoal);

        // get Goals was written and proven to be correct, let's see if it gets
        // the copy of listCopyGoal
        assertEquals(listCopyGoal, dataSource.getGoals());
    }

    public void testRemoveGoal() {
    }

    public void testShiftSortOrders() {
    }

    public void testGetMinSortOrder() {
    }

    public void testGetMaxSortOrder() {
    }

    public void testFromDefault() {
    }

    public void testFromDefaultEmpty() {
    }
}