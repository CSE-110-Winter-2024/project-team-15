package edu.ucsd.cse110.successorator.lib.domain;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;

public class GoalTest {

    @Test
    public void testGetters() {
        Goal gol = new Goal("This is a goal", 1, true, 3);
        assertEquals("This is a goal", gol.contents());
        assertEquals(Integer.valueOf(1), gol.id());
        assertEquals(Boolean.TRUE, gol.completed());
        assertEquals(3, gol.sortOrder());
    }
    @Test
    public void testEquals(){
        Goal gol = new Goal("This is a goal", 1, true, 3);
        Goal eql_gol = new Goal("This is a goal", 1, true, 3);
        Goal bad_cont = new Goal("This is not a goal", 1, true, 3);
        Goal bad_id = new Goal("This is a goal", 2, true, 3);
        Goal bad_comp = new Goal("This is a goal", 1, false, 3);
        Goal bad_sort = new Goal("This is a goal", 1, true, 4);
        assertEquals(gol, eql_gol);
        assertNotEquals(gol, bad_cont);
        assertNotEquals(gol, bad_comp);
        assertNotEquals(gol, bad_sort);
        assertNotEquals(gol, bad_id);
    }
    @Test
    public void testEqualsWithWiths(){
        Goal gol = new Goal("This is a goal", 1, true, 3);
        Goal eql_gol = gol.withId(gol.id());
        Goal bad_id = gol.withId(12);
        Goal bad_comp = gol.withComplete(false);
        Goal bad_sort = gol.withSortOrder(999);
        assertEquals(gol, eql_gol);
        assertNotEquals(gol, bad_comp);
        assertNotEquals(gol, bad_sort);
        assertNotEquals(gol, bad_id);
    }

    // we should move these to a MainViewModel testing place
    // still need to do this
    @Test
    public void completedOne() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0)
//             new Goal("This Massive Wall Of Text Goes On And On For All Eternity Or At Least Until It gets Off THe Screen In which Case You Will Stop Seeing It At All", 4)
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        SimpleDateTracker dateTracker = new SimpleDateTracker();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(0));
        var actual = dataSource.getGoal(0).completed();
        var expected = true;
        assertEquals(expected, actual);
    }

    @Test
    public void completedMultiple() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0),
                new Goal("Grocery shopping", 1, false, 1),
                new Goal("Make dinner", 2, false, 2),
                new Goal("Text Maria", 3, false, 3)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        SimpleDateTracker dateTracker = new SimpleDateTracker();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(2));
        var actual = dataSource.getGoal(2).completed();
        mvm.toggleCompleted(dataSource.getGoal(3));
        var actual2 = dataSource.getGoal(3).completed();
        var expected = true;
        assertEquals(expected, actual);
        assertEquals(expected, actual2);
    }


    @Test
    public void uncompletedDoubleToggle() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0),
                new Goal("Grocery shopping", 1, false, 1),
                new Goal("Make dinner", 2, false, 2),
                new Goal("Text Maria", 3, false, 3)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        SimpleDateTracker dateTracker = new SimpleDateTracker();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(2));
        mvm.toggleCompleted(dataSource.getGoal(2));
        var actual = dataSource.getGoal(2).completed();
        var expected = false;
        assertEquals(expected, actual);
    }

    @Test
    public void uncompletedSingleToggle() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0),
                new Goal("Grocery shopping", 1, false, 1),
                new Goal("Make dinner", 2, false, 2),
                new Goal("Text Maria", 3, true, 3)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        SimpleDateTracker dateTracker = new SimpleDateTracker();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(3));
        var actual = dataSource.getGoal(3).completed();
        var expected = false;
        assertEquals(expected, actual);
    }
}