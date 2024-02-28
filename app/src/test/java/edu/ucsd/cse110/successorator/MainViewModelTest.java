package edu.ucsd.cse110.successorator;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;

public class MainViewModelTest {

    @Test
    public void getOrderedGoals() {
    }

    @Test
    public void getNoGoalsTrue() {
        List<Goal> testGoals = List.of();
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = SimpleDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        Boolean expected = true;
        Boolean actual= mvm.getNoGoals().getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void getNoGoalsFalse(){
        List<Goal> testGoals = List.of(
                new Goal("get food",0, false, 0,0 ),
                new Goal("get kids", 1, false, 1,0)
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = SimpleDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        Boolean expected = false;
        Boolean actual= mvm.getNoGoals().getValue();
        assertEquals(expected, actual);
    }
    // we should move these to a MainViewModel testing place
    @Test
    public void completedOne() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0,0)
//             new Goal("This Massive Wall Of Text Goes On And On For All Eternity Or At Least Until It gets Off THe Screen In which Case You Will Stop Seeing It At All", 4)
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = SimpleDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(0));
        var actual = dataSource.getGoal(0).completed();
        var expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void completedMultiple() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0,0),
                new Goal("Grocery shopping", 1, false, 1,0),
                new Goal("Make dinner", 2, false, 2,0),
                new Goal("Text Maria", 3, false, 3,0)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = SimpleDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(2));
        var actual = dataSource.getGoal(2).completed();
        mvm.toggleCompleted(dataSource.getGoal(3));
        var actual2 = dataSource.getGoal(3).completed();
        var expected = true;
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, actual2);
    }


    @Test
    public void uncompletedDoubleToggle() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0,0),
                new Goal("Grocery shopping", 1, false, 1,0),
                new Goal("Make dinner", 2, false, 2,0),
                new Goal("Text Maria", 3, false, 3,0)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = SimpleDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(2));
        mvm.toggleCompleted(dataSource.getGoal(2));
        var actual = dataSource.getGoal(2).completed();
        var expected = false;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void uncompletedSingleToggle() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0,0),
                new Goal("Grocery shopping", 1, false, 1,0),
                new Goal("Make dinner", 2, false, 2,0),
                new Goal("Text Maria", 3, true, 3,0)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = SimpleDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(3));
        var actual = dataSource.getGoal(3).completed();
        var expected = false;
        Assert.assertEquals(expected, actual);
    }
}