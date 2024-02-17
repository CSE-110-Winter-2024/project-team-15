package edu.ucsd.cse110.successorator.lib.domain;

import static junit.framework.TestCase.assertEquals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.R;
import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentGoalListBinding;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.ui.goallist.GoalListFragment;

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
    public void insertIncompleteGoal(){
        SimpleGoalRepository testRepository = new SimpleGoalRepository(InMemoryDataSource.fromDefaultEmpty());

        Goal g1 = new Goal("1", null, false, -1);
        testRepository.insertIncompleteGoal(g1);

        //add to empty repo
        Goal g1t = testRepository.find(0).getValue();
        assert g1t != null;
        assertEquals(g1t.contents(), "1");

        //add after incomplete goals
        Goal g2 = new Goal("2", null, false, -1);
        testRepository.insertIncompleteGoal(g2);
        int g2order = testRepository.find(1).getValue().sortOrder();
        int g1order = testRepository.find(0).getValue().sortOrder();
        assertEquals(g1order+1, g2order);

        Goal g3 = new Goal("3", null, true, -1);
        //insertIncomplete doesn't know if goal is completed or not, just inserts as if it is,
        //so this test will place this incomplete goal at the end since no other completed
        // goals are present
        testRepository.insertIncompleteGoal(g3);
        int g3order1 = testRepository.find(2).getValue().sortOrder();
        //insert into repo with completed goals and make sure
        //both goals have updated order
        Goal g4 = new Goal("4", null, true, -1);

        testRepository.insertIncompleteGoal(g4);
        int g4order = testRepository.find(3).getValue().sortOrder();
        int g3order2 = testRepository.find(2).getValue().sortOrder();

        //g3order changed
        assertEquals(g3order1+1, g3order2);

        //g4 with lower order than g3
        assertEquals(g3order2-1, g4order);
    }

    @Test
    public void DisplayNoGoals() {
        List<Goal> testGoals = List.of();
        var dataSource = new InMemoryDataSource();
        for (Goal goal : testGoals) {
            dataSource.putGoal(goal);
        }
        SimpleGoalRepository testRepo = new SimpleGoalRepository(dataSource);
        MainViewModel mvm = new MainViewModel(testRepo);
//        GoalListFragment fragmentTest = new GoalListFragment().newInstance();
//        var inflater = fragmentTest.getLayoutInflater();
////        ViewGroup container = R.layout.fragment_goal_list;
//        var view = FragmentGoalListBinding.inflate(inflater, null, false);
        Boolean expected = true;
        Boolean actual= mvm.getNoGoals().getValue();
        assertEquals(expected, actual);
    }
}