package edu.ucsd.cse110.successorator.lib.domain;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

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
        // by the way, I do know this style of testing is wasteful, but it helps me understand...
        // it's java it's fine
        SimpleGoalRepository testRepository;
        InMemoryDataSource testMemoryDataSource;
        Goal g1;
        Goal g2;
        Goal g3;
        Goal g4;


        // two goals, mark the top as complete, that goal should now have the last sort order
        // (and be completed)
        testMemoryDataSource = new InMemoryDataSource();
        testRepository = new SimpleGoalRepository(testMemoryDataSource);

        g1 = new Goal("1", null, false, 1);
        g2 = new Goal("2", null, false, 2);
        testMemoryDataSource.putGoal(g1); // 0 = sort order
        testMemoryDataSource.putGoal(g2); // 1 = sort order

        testRepository.toggleCompleteGoal(g1);

        assertEquals(1, testMemoryDataSource.getGoal(0).sortOrder()); // sort order
        assertEquals(Boolean.TRUE, testMemoryDataSource.getGoal(0).completed()); // is completed

    }


}