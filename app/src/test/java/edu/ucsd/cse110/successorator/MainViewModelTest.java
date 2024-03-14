package edu.ucsd.cse110.successorator;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.ComplexDateTracker;
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
        var dateTracker = ComplexDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        Boolean expected = true;
        Boolean actual= mvm.getNoGoals().getValue();
        assertEquals(expected, actual);
    }

    @Test
    public void getNoGoalsFalse(){
        List<Goal> testGoals = List.of(
                new Goal("get food",0, false, 0,0 , 3),
                new Goal("get kids", 1, false, 1,0, 3)
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        Boolean expected = false;
        Boolean actual= mvm.getNoGoals().getValue();
        assertEquals(expected, actual);
    }
    // we should move these to a MainViewModel testing place
    @Test
    public void completedOne() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0,0, 2)
//             new Goal("This Massive Wall Of Text Goes On And On For All Eternity Or At Least Until It gets Off THe Screen In which Case You Will Stop Seeing It At All", 4)
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(0));
        var actual = dataSource.getGoal(0).completed();
        var expected = true;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void completedMultiple() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0,0, 2),
                new Goal("Grocery shopping", 1, false, 1,0, 3),
                new Goal("Make dinner", 2, false, 2,0, 0),
                new Goal("Text Maria", 3, false, 3,0, 3)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();
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
                new Goal("Prepare for the midterm", 0, false, 0,0, 2),
                new Goal("Grocery shopping", 1, false, 1,0, 3),
                new Goal("Make dinner", 2, false, 2,0, 0),
                new Goal("Text Maria", 3, false, 3,0, 3)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(2));
        mvm.toggleCompleted(dataSource.getGoal(2));
        var actual = dataSource.getGoal(2).completed();
        var expected = false;
        Assert.assertEquals(expected, actual);
    }


    //why broken?
    //this was supposed to test uncompleting a completed goal but the goal that was supposed to be completed was written as false
    //we have fixed this and changed it back to true. The error should be fixed now.
    @Test
    public void uncompletedSingleToggle() {
        List<Goal> testGoals = List.of(
                new Goal("Prepare for the midterm", 0, false, 0,0, 0),
                new Goal("Grocery shopping", 1, false, 1,0, 3),
                new Goal("Make dinner", 2, false, 2,0, 0),
                new Goal("Text Maria", 3, true, 3,0, 3)
//
        );
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        mvm.toggleCompleted(dataSource.getGoal(3));
        var actual = dataSource.getGoal(3).completed();
        var expected = false;
        Assert.assertEquals(expected, actual);
    }



    @Test
    public void switchView(){
        /* ************************************************************************************* */
        // Checking if we can actually switch to a different view..
        // (I realized if we ever change the default view to 2, these tests might fail
        // ^ This can be fixed later, but for now we have iteration deadlines
        // and I doubt we'll change it to a different default)
        // another note: using inMemory rather than Room is because apparently we can't make an
        // instance of the DB so we're going to use inMemory for now
        // Given: A MainViewModel instance that has a default view
        var dataSource = new InMemoryDataSource();
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();

        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);
        int defaultView = mvm.getListShown();

        // When: switchView is called to set a new view (2)
        int newViewNum = 2;
        mvm.switchView(newViewNum);

        // Then: listShown should reflect the new view, it's not the default
        assertEquals(newViewNum, mvm.getListShown());
        assertNotEquals(defaultView, mvm.getListShown());
        /* ************************************************************************************* */
    }

    @Test
    public void createRecurringGoal() {
        // Given a valid input, when we create a recurring goal, then the goal is created
        // Given: A MainViewModel instance and valid input for creating a recurring goal
        var dataSource = new InMemoryDataSource();
        // i have no choice but to use the simple goal repo for now
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);

        String goalText = "daily";
        int recurrenceType = 1; // Daily
        LocalDateTime representation = LocalDateTime.of(2024, 3, 14, 0, 0);

        // When: createRecurringGoal is called with the given inputs
        mvm.createRecurringGoal(goalText, recurrenceType, representation, 0);

        // Then: A goal with the specified attributes is created
        assertFalse(mvm.getOrderedGoals().getValue().isEmpty()); // one goal needs to be made
        Goal createdGoal = mvm.getOrderedGoals().getValue().get(0);
        assertEquals(goalText, createdGoal.contents());
        assertEquals(recurrenceType, createdGoal.recurrenceType());
    }

    @Test
    public void resolveRecurrence() {
        // Given: A MainViewModel instance and radio button IDs for different recurrence types
        var dataSource = new InMemoryDataSource();
        // i have no choice but to use the simple goal repo for now
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        var dateTracker = ComplexDateTracker.getInstance();
        MainViewModel mvm = new MainViewModel(testRepo, dateTracker);

        int dailyButtonId = R.id.daily_button;
        int weeklyButtonId = R.id.weekly_button;
        int monthlyButtonId = R.id.monthly_button;
        int yearlyButtonId = R.id.yearly_button;

        // When & Then: resolveRecurrenceType is called with the given IDs, correct recurrence type is returned
        assertEquals(1, mvm.resolveRecurrenceType(dailyButtonId));
        assertEquals(2, mvm.resolveRecurrenceType(weeklyButtonId));
        assertEquals(3, mvm.resolveRecurrenceType(monthlyButtonId));
        assertEquals(4, mvm.resolveRecurrenceType(yearlyButtonId));
    }



}