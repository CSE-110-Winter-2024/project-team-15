package edu.ucsd.cse110.successorator.lib.domain;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;

public class SimpleGoalRepositoryTest {

    @Test
    public void find() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void save() {
    }

    @Test
    public void testSave() {
    }

    @Test
    public void remove() {
    }

    @Test
    public void append() {
    }

    @Test
    public void prepend() {
    }

    // should I split these into multiple functions? it would look a LOT nicer
    @Test
    public void insertUnderIncompleteGoals(){
        SimpleGoalRepository testRepository = new SimpleGoalRepository(InMemoryDataSource.fromDefaultEmpty());

        Goal g1 = new Goal("1", null, false, -1);
        testRepository.insertUnderIncompleteGoals(g1);

        //add to empty repo
        Goal g1t = testRepository.find(0).getValue();
        assert g1t != null;
        assertEquals(g1t.contents(), "1");

        //add after incomplete goals
        Goal g2 = new Goal("2", null, false, -1);
        testRepository.insertUnderIncompleteGoals(g2);
        int g2order = testRepository.find(1).getValue().sortOrder();
        int g1order = testRepository.find(0).getValue().sortOrder();
        assertEquals(g1order+1, g2order);

        Goal g3 = new Goal("3", null, true, -1);
        //insertIncomplete doesn't know if goal is completed or not, just inserts as if it is,
        //so this test will place this incomplete goal at the end since no other completed
        // goals are present
        testRepository.insertUnderIncompleteGoals(g3);
        int g3order1 = testRepository.find(2).getValue().sortOrder();
        //insert into repo with completed goals and make sure
        //both goals have updated order
        Goal g4 = new Goal("4", null, true, -1);

        testRepository.insertUnderIncompleteGoals(g4);
        int g4order = testRepository.find(3).getValue().sortOrder();
        int g3order2 = testRepository.find(2).getValue().sortOrder();

        //g3order changed
        assertEquals(g3order1+1, g3order2);

        //g4 with lower order than g3
        assertEquals(g3order2-1, g4order);
    }
    @Test
    public void toggleCompleteGoal() {
        // initialize
        SimpleGoalRepository testRepository;
        InMemoryDataSource testMemoryDataSource;

        Goal actual1;
        Goal actual2;
        Goal actual3;
        Goal actual4;

        Boolean actualCompleted;
        int actualSortOrder;

        Goal expected1;
        Goal expected2;
        Goal expected3;
        Goal expected4;




        // two goals, mark the top as complete, that goal should now have the last sort order
        // (and be completed)

        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1);
        actual2 = new Goal("2", 2, false, 2);

        expected1 = new Goal("1", 1, true, 3);
        expected2 = new Goal("2", 2, false, 2);

        testMemoryDataSource.putGoal(actual1); // 1 = sort order
        testMemoryDataSource.putGoal(actual2); // 2 = sort order


        // WHEN toggle, the first goal moves to bottom, it gets the max incomplete sort order + 1
        testRepository.toggleCompleteGoal(actual1);
        actual1 = testMemoryDataSource.getGoal(1);
        actual2 = testMemoryDataSource.getGoal(2);

        // THEN
        // Goal 1 should move and it's sort order should be the max incomplete sort order + 1
        actualCompleted = testMemoryDataSource.getGoal(1).completed();
        actualSortOrder = testMemoryDataSource.getGoal(1).sortOrder();
        assertEquals(3, actualSortOrder); // sort order
        assertEquals(Boolean.TRUE, actualCompleted); // is completed
        assertEquals(expected1, actual1);
        // Goal 2 does not move
        actualCompleted = testMemoryDataSource.getGoal(2).completed();
        actualSortOrder = testMemoryDataSource.getGoal(2).sortOrder();
        assertEquals(2, actualSortOrder); // sort order
        assertEquals(Boolean.FALSE, actualCompleted); // is completed
        assertEquals(expected2, actual2);




        // two goals, mark the bottom as complete, that goal should now have the last sort order +1
        // (and be completed)
        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1);
        actual2 = new Goal("2", 2, false, 2);

        expected1 = new Goal("1", 1, false, 1);
        expected2 = new Goal("2", 2, true, 3);

        testMemoryDataSource.putGoal(actual1); // 1 = sort order
        testMemoryDataSource.putGoal(actual2); // 2 = sort order


        // WHEN toggle, second goal stays at bottom, sortOrder = max incomplete sort order + 1
        testRepository.toggleCompleteGoal(actual2);
        actual1 = testMemoryDataSource.getGoal(1);
        actual2 = testMemoryDataSource.getGoal(2);

        // THEN
        // Goal 1
        actualCompleted = testMemoryDataSource.getGoal(1).completed();
        actualSortOrder = testMemoryDataSource.getGoal(1).sortOrder();
        assertEquals(1, actualSortOrder); // sort order
        assertEquals(Boolean.FALSE, actualCompleted); // is completed
        assertEquals(expected1, actual1);
        // Goal 2
        actualCompleted = testMemoryDataSource.getGoal(2).completed();
        actualSortOrder = testMemoryDataSource.getGoal(2).sortOrder();
        assertEquals(3, actualSortOrder); // sort order
        assertEquals(Boolean.TRUE, actualCompleted); // is completed
        assertEquals(expected2, actual2);




        // Four goals, two incomplete, two complete, mark the first goal as complete
        // Should now be complete and have a sort order of
        // max previous incomplete sort order + 1 (in our case, 3)
        // and all other complete goals' sort order goes up by 1
        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1);
        actual2 = new Goal("2", 2, false, 2);
        actual3 = new Goal("3", 3, true, 3);
        actual4 = new Goal("4", 4, true, 4);

        expected1 = new Goal("1", 1, true, 3);
        expected2 = new Goal("2", 2, false, 2);
        expected3 = new Goal("3", 3, true, 4);
        expected4 = new Goal("4", 4, true, 5);

        testMemoryDataSource.putGoal(actual1);
        testMemoryDataSource.putGoal(actual2);
        testMemoryDataSource.putGoal(actual3);
        testMemoryDataSource.putGoal(actual4);


        // WHEN we mark the first goal complete, it's sortOrder is max incomplete sort order + 1
        testRepository.toggleCompleteGoal(actual1);
        actual1 = testMemoryDataSource.getGoal(1);
        actual2 = testMemoryDataSource.getGoal(2);
        actual3 = testMemoryDataSource.getGoal(3);
        actual4 = testMemoryDataSource.getGoal(4);


        // Goal 1
        actualCompleted = testMemoryDataSource.getGoal(1).completed();
        actualSortOrder = testMemoryDataSource.getGoal(1).sortOrder();
        assertEquals(3, actualSortOrder); // sort order
        assertEquals(Boolean.TRUE, actualCompleted); // is completed
        assertEquals(expected1, actual1);
        // Goal 2
        assertEquals(expected2, actual2);
        // Goal 3
        assertEquals(expected3, actual3);
        // Goal 4
        assertEquals(expected4, actual4);




        // Four goals, two incomplete, two complete, mark the last goal as incomplete
        // Should now be incomplete and have the earliest sort order of 1
        // since it replaces where the earliest goal was
        // all other goals get 1 added to their sort order (shifted down)
        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1);
        actual2 = new Goal("2", 2, false, 2);
        actual3 = new Goal("3", 3, true, 3);
        actual4 = new Goal("4", 4, true, 4);

        expected1 = new Goal("1", 1, false, 2);
        expected2 = new Goal("2", 2, false, 3);
        expected3 = new Goal("3", 3, true, 4);
        expected4 = new Goal("4", 4, false, 1);

        testMemoryDataSource.putGoal(actual1);
        testMemoryDataSource.putGoal(actual2);
        testMemoryDataSource.putGoal(actual3);
        testMemoryDataSource.putGoal(actual4);


        // WHEN
        testRepository.toggleCompleteGoal(actual4); // last goal incomplete.
        actual1 = testMemoryDataSource.getGoal(1);
        actual2 = testMemoryDataSource.getGoal(2);
        actual3 = testMemoryDataSource.getGoal(3);
        actual4 = testMemoryDataSource.getGoal(4);


        // THEN
        // Goal 1
        assertEquals(expected1, actual1);
        // Goal 2
        assertEquals(expected2, actual2);
        // Goal 3
        assertEquals(expected3, actual3);
        // Goal 4
        actualCompleted = testMemoryDataSource.getGoal(4).completed();
        actualSortOrder = testMemoryDataSource.getGoal(4).sortOrder();
        assertEquals(1, actualSortOrder); // sort order
        assertEquals(Boolean.FALSE, actualCompleted); // is completed
        assertEquals(expected4, actual4);




        // One goal, it's incomplete. Toggle complete twice.
        // Given
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);
        actual1 = new Goal("1", 1, false, 1);
        expected1 = new Goal("1", 1, true, 2);
        testMemoryDataSource.putGoal(actual1);


        // When
        testRepository.toggleCompleteGoal(actual1);
        actual1 = testMemoryDataSource.getGoal(1);


        // Then
        actualCompleted = testMemoryDataSource.getGoal(1).completed();
        actualSortOrder = testMemoryDataSource.getGoal(1).sortOrder();
        assertEquals(2, actualSortOrder); // sort order
        assertEquals(Boolean.TRUE, actualCompleted); // is completed
        assertEquals(expected1, actual1);


        // Given
        expected1 = new Goal("1", 1, false, 2);


        // When
        testRepository.toggleCompleteGoal(testMemoryDataSource.getGoal(1));
        actual1 = testMemoryDataSource.getGoal(1);


        // Then
        actualCompleted = testMemoryDataSource.getGoal(1).completed();
        actualSortOrder = testMemoryDataSource.getGoal(1).sortOrder();
        assertEquals(2, actualSortOrder); // sort order (2 is now the earliest)
        assertEquals(Boolean.FALSE, actualCompleted); // is completed
        assertEquals(expected1, actual1);
    }

    @Test
    public void clearCompletedGoalsTest(){
        SimpleGoalRepository testRepo = new SimpleGoalRepository(InMemoryDataSource.fromDefault());
        List<Goal> goals = testRepo.findAll().getValue();
        Goal goal = goals.get(0);
        assertEquals(goals.size(),4);
        testRepo.toggleCompleteGoal(goal);
        testRepo.clearCompletedGoals();
        List<Goal> goals2 = testRepo.findAll().getValue();
        assertEquals(goals2.size(),3);
    }


}