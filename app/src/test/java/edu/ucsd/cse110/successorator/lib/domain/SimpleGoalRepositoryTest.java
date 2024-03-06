package edu.ucsd.cse110.successorator.lib.domain;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.List;


import edu.ucsd.cse110.successorator.MainViewModel;
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

        Goal g1 = new Goal("1", null, false, -1,0, "Home");
        testRepository.insertUnderIncompleteGoals(g1);

        //add to empty repo
        Goal g1t = testRepository.find(0).getValue();
        assert g1t != null;
        assertEquals(g1t.contents(), "1");

        //add after incomplete goals
        Goal g2 = new Goal("2", null, false, -1,0,"Home");
        testRepository.insertUnderIncompleteGoals(g2);
        int g2order = testRepository.find(1).getValue().sortOrder();
        int g1order = testRepository.find(0).getValue().sortOrder();
        assertEquals(g1order+1, g2order);

        Goal g3 = new Goal("3", null, true, -1,0,"Home");
        //insertIncomplete doesn't know if goal is completed or not, just inserts as if it is,
        //so this test will place this incomplete goal at the end since no other completed
        // goals are present
        testRepository.insertUnderIncompleteGoals(g3);
        int g3order1 = testRepository.find(2).getValue().sortOrder();
        //insert into repo with completed goals and make sure
        //both goals have updated order
        Goal g4 = new Goal("4", null, true, -1,0,"Home");

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


        /*
         Below this point are tests for toggling incomplete -> complete
         */

        // two goals, mark the top as complete, that goal should now have the last sort order
        // (and be completed)

        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1,0,"Home");
        actual2 = new Goal("2", 2, false, 2,0,"Home");

        expected1 = new Goal("1", 1, true, 3,0,"Home");
        expected2 = new Goal("2", 2, false, 2,0,"Home");

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

        actual1 = new Goal("1", 1, false, 1,0, "Home");
        actual2 = new Goal("2", 2, false, 2,0,"Home");

        expected1 = new Goal("1", 1, false, 1,0,"Home");
        expected2 = new Goal("2", 2, true, 3,0, "Home");

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

        actual1 = new Goal("1", 1, false, 1,0,"Home");
        actual2 = new Goal("2", 2, false, 2,0,"Home");
        actual3 = new Goal("3", 3, true, 3,0,"Home");
        actual4 = new Goal("4", 4, true, 4,0,"Home");

        expected1 = new Goal("1", 1, true, 3,0,"Home");
        expected2 = new Goal("2", 2, false, 2,0,"Home");
        expected3 = new Goal("3", 3, true, 4,0,"Home");
        expected4 = new Goal("4", 4, true, 5,0,"Home");

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



        /*
         Below this point are tests for toggling complete -> incomplete (US11)
         */

        // Four goals, two incomplete, two complete, mark the last goal as incomplete
        // Should now be incomplete and have the earliest sort order of 1
        // since it replaces where the earliest goal was
        // all other goals get 1 added to their sort order (shifted down)
        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1,0,"Home");
        actual2 = new Goal("2", 2, false, 2,0,"Home");
        actual3 = new Goal("3", 3, true, 3,0,"Home");
        actual4 = new Goal("4", 4, true, 4,0,"Home");

        expected1 = new Goal("1", 1, false, 2,0,"Home");
        expected2 = new Goal("2", 2, false, 3, 0,"Home");
        expected3 = new Goal("3", 3, true, 4, 0,"Home");
        expected4 = new Goal("4", 4, false, 1, 0,"Home");

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




        // Four goals, 1 incomplete, 3 complete, mark the second goal as incomplete
        // Should now be incomplete and have the earliest sort order of 1
        // since it replaces where the earliest goal was
        // all other goals get 1 added to their sort order (shifted down)
        // (the function doesn't know to swap or keep orders neatly, but the orders are correct)
        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1,0,"Home");
        actual2 = new Goal("2", 2, true, 2, 0,"Home");
        actual3 = new Goal("3", 3, true, 3, 0,"Home");
        actual4 = new Goal("4", 4, true, 4, 0,"Home");

        expected1 = new Goal("1", 1, false, 2, 0,"Home");
        expected2 = new Goal("2", 2, false, 1, 0,"Home");
        expected3 = new Goal("3", 3, true, 4, 0,"Home");
        expected4 = new Goal("4", 4, true, 5, 0,"Home");

        testMemoryDataSource.putGoal(actual1);
        testMemoryDataSource.putGoal(actual2);
        testMemoryDataSource.putGoal(actual3);
        testMemoryDataSource.putGoal(actual4);


        // WHEN
        testRepository.toggleCompleteGoal(actual2); // second goal incomplete.
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
        assertEquals(expected4, actual4);




        // One complete goal, mark it as incomplete. Sort order should not change since
        // its already at the top.
        // GIVEN
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, true, 1, 0,"Home");

        expected1 = new Goal("1", 1, false, 1, 0,"Home");

        testMemoryDataSource.putGoal(actual1);


        // WHEN
        testRepository.toggleCompleteGoal(actual1); // second goal incomplete.
        actual1 = testMemoryDataSource.getGoal(1);


        // THEN
        assertEquals(expected1, actual1);




        /*
         Below this point is an integration test for incomplete -> complete -> incomplete
         */

        // One goal, it's incomplete. Toggle complete twice.
        // Given
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);
        actual1 = new Goal("1", 1, false, 1, 0,"Home");
        expected1 = new Goal("1", 1, true, 2, 0,"Home");
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
        expected1 = new Goal("1", 1, false, 2, 0,"Home");


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
//        SimpleGoalRepository testRepo = new SimpleGoalRepository(InMemoryDataSource.fromDefault());
//        List<Goal> goals = testRepo.findAll().getValue();
//        Goal goal = goals.get(0);
//        assertEquals(goals.size(),4);
//        testRepo.toggleCompleteGoal(goal);
//        testRepo.clearCompletedGoals();
//        List<Goal> goals2 = testRepo.findAll().getValue();
//        assertEquals(goals2.size(),3);

        // initialize
        SimpleGoalRepository testRepository;
        InMemoryDataSource testMemoryDataSource;

        Goal actual1;
        Goal actual2;
        Goal actual3;
        Goal actual4;

        Goal expected1;
        Goal expected2;
        Goal expected3;
        Goal expected4;

        // four goals, two are complete. call remove all completed goals
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        actual1 = new Goal("1", 1, false, 1, 0,"Home");
        actual2 = new Goal("2", 2, false, 2, 0,"Home");
        actual3 = new Goal("3", 3, true, 3, 0,"Home");
        actual4 = new Goal("4", 4, true, 4, 0,"Home");

        expected1 = new Goal("1", 1, false, 1, 0,"Home");
        expected2 = new Goal("2", 2, false, 2, 0,"Home");
        expected3 = null;
        expected4 = null;

        testMemoryDataSource.putGoal(actual1);
        testMemoryDataSource.putGoal(actual2);
        testMemoryDataSource.putGoal(actual3);
        testMemoryDataSource.putGoal(actual4);

        // WHEN I clear completed goals
        testRepository.clearCompletedGoals();
        actual1 = testMemoryDataSource.getGoal(1);
        actual2 = testMemoryDataSource.getGoal(2);
        actual3 = testMemoryDataSource.getGoal(3);
        actual4 = testMemoryDataSource.getGoal(4);

        // THEN expected3 and expected4 should be gone
        // Goal 1
        assertEquals(expected1, actual1);
        // Goal 2
        assertEquals(expected2, actual2);
        // Goal 3
        assertEquals(expected3, actual3);
        // Goal 4
        assertEquals(expected4, actual4);

    }


    @Test
    public void findAllViewTest() {
        // Given a repository
        InMemoryDataSource testMemoryDataSource = new InMemoryDataSource();
        SimpleGoalRepository testRepository = new SimpleGoalRepository(testMemoryDataSource);
        int listNum;

        // When goals are added to two different views
        listNum = 0; // For example, and goals for "today"
        List<Goal> expectedGoals0 = List.of(new Goal("Today Goal 1", 3, false, 3, listNum,"Home"),
                new Goal("Today Goal 2", 4, true, 4, listNum,"Home"));

        listNum = 1; // For example, goals for "tomorrow"
        List<Goal> expectedGoals1 = List.of(new Goal("Tomorrow Goal 1", 3, false, 3, listNum,"Home"),
                new Goal("Tomorrow Goal 2", 4, true, 4, listNum,"Home"));

        testRepository.save(expectedGoals0);
        testRepository.save(expectedGoals1);


        // When I get all the goals for the tomorrow view
        List<Goal> actualGoals = testRepository.findAll(listNum).getValue();


        // Then all goals in the tomorrow view should show up
        assertEquals(expectedGoals1, actualGoals);

        // And no goals from the today view
        assertNotEquals(expectedGoals0, actualGoals);

    }

    @Test
    public void testContext(){
        SimpleGoalRepository testRepository;
        InMemoryDataSource testMemoryDataSource;
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);


        Goal actual1 = new Goal("This is a goal", 1, true, 1, 0, "Home");
        Goal actual2 = new Goal("2", 2, false, 2, 0,"Work");
        Goal actual3 = new Goal("3", 3, true, 3, 0,"School");
        Goal actual4 = new Goal("4", 4, true, 4, 0,"Errand");

        testMemoryDataSource.putGoal(actual1);
        testMemoryDataSource.putGoal(actual2);
        testMemoryDataSource.putGoal(actual3);
        testMemoryDataSource.putGoal(actual4);

        Goal expected1 = new Goal("This is a goal", 1, true, 1, 0, "Home");
        Goal expected2 = new Goal("2", 2, false, 2, 0,"Work");
        Goal expected3 = new Goal("3", 3, true, 3, 0,"School");
        Goal expected4 = new Goal("4", 4, true, 4, 0,"Errand");

        actual1 = testMemoryDataSource.getGoal(1);
        actual2 = testMemoryDataSource.getGoal(2);
        actual3 = testMemoryDataSource.getGoal(3);
        actual4 = testMemoryDataSource.getGoal(4);

        assertEquals(expected1.context(), actual1.context());
        assertEquals(expected2.context(), actual2.context());
        assertEquals(expected3.context(), actual3.context());
        assertEquals(expected4.context(), actual4.context());
    }

}