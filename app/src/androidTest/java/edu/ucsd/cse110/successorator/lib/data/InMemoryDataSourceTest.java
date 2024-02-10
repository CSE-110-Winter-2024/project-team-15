package edu.ucsd.cse110.successorator.lib.data;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class InMemoryDataSourceTest {

    @Test
    public void putGoal() {
        InMemoryDataSource tst = new InMemoryDataSource();
        var dummy_goal = new Goal("thisdf", 3, true, 2);
        tst.putGoal(new Goal("thisdf", 3, true, 2));
        assertEquals(tst.getGoal(3), dummy_goal);
    }

    @Test
    public void putGoals() {
        InMemoryDataSource tst = new InMemoryDataSource();
        var dummy_goals = List.of(new Goal("thisdf", 3, true, 2),
                new Goal("thisdf", 3, true, 2),
                new Goal("thisdf", 3, true, 2),
                new Goal("thisdf", 3, true, 2),
                new Goal("thisdf", 3, true, 2),
                new Goal("thisdf", 3, true, 2));
        
    }

    @Test
    public void getGoals() {
    }

    @Test
    public void getGoal() {
    }

    @Test
    public void getGoalSubject() {
    }

    @Test
    public void getAllGoalsSubject() {
    }

    @Test
    public void removeGoal() {
    }

    @Test
    public void shiftSortOrders() {
    }

    @Test
    public void getMinSortOrder() {
    }

    @Test
    public void getMaxSortOrder() {
    }

    @Test
    public void fromDefault() {
    }

    @Test
    public void fromDefaultEmpty() {
    }
}