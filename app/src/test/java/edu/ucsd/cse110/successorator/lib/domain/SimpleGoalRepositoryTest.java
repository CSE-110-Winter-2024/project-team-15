package edu.ucsd.cse110.successorator.lib.domain;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;

public class SimpleGoalRepositoryTest {

    @Test
    public void find() {
        SimpleGoalRepository repo = new SimpleGoalRepository(InMemoryDataSource.fromDefault());
        var subject = new SimpleSubject<Goal>();
        Goal temp = new Goal("Prepare for the midterm", 0, false, 0);
        subject.setValue(temp);
        assertEquals(repo.find(0).getValue(),subject.getValue());
        assertFalse(repo.find(0).getValue().equals(new Goal("Prepare for the final", 0, false, 0)));
    }

    @Test
    public void findAll() {
        SimpleGoalRepository repo = new SimpleGoalRepository(InMemoryDataSource.fromDefault());
        List<Goal> lst = repo.findAll().getValue();

        assertEquals(lst.get(0), new Goal("Prepare for the midterm", 0, false, 0));
        assertEquals(lst.get(1), new Goal("Grocery shopping", 1, false, 1));
        assertEquals(lst.get(2), new Goal("Make dinner", 2, false, 2));
        assertEquals(lst.get(3), new Goal("Text Maria", 3, false, 3));

    }

    @Test
    public void save() {
        SimpleGoalRepository repo = new SimpleGoalRepository(InMemoryDataSource.fromDefaultEmpty());
        Goal temp = new Goal("Prepare for the midterm", 0, false, 0);
        repo.save(temp);
        assertEquals(repo.find(0).getValue(),new Goal("Prepare for the midterm", 0, false, 0));
        assertFalse(repo.find(0).getValue().equals(new Goal("Prepare for the final", 0, false, 0)));
    }

    @Test
    public void testSave() {
        SimpleGoalRepository repo = new SimpleGoalRepository(InMemoryDataSource.fromDefaultEmpty());
        List<Goal> lst = List.of(
                new Goal("Prepare for the midterm", 0, false, 0),
                new Goal("Grocery shopping", 1, false, 1),
                new Goal("Make dinner", 2, false, 2),
                new Goal("Text Maria", 3, false, 3)
        );

        repo.save(lst);

        assertEquals(repo.find(0).getValue(), new Goal("Prepare for the midterm", 0, false, 0));
        assertEquals(repo.find(1).getValue(), new Goal("Grocery shopping", 1, false, 1));
        assertEquals(repo.find(2).getValue(), new Goal("Make dinner", 2, false, 2));
        assertEquals(repo.find(3).getValue(), new Goal("Text Maria", 3, false, 3));
        assertFalse(repo.find(0).getValue().equals(new Goal("Prepare for the final", 0, false, 0)));

    }

    @Test
    public void remove() {
        SimpleGoalRepository repo = new SimpleGoalRepository(InMemoryDataSource.fromDefault());
        repo.remove(0);
        assertEquals(repo.find(1).getValue(), new Goal("Grocery shopping", 1, false, 0));
        repo.remove(3);
        assertEquals(repo.find(2).getValue(), new Goal("Make dinner", 2, false, 1));
    }

    @Test
    public void append() {
        SimpleGoalRepository repo = new SimpleGoalRepository(InMemoryDataSource.fromDefault());
        Goal temp = new Goal("CSE 110 Project", 100, false, 0);
        repo.append(temp);
        assertEquals(repo.find(0).getValue(), new Goal("Prepare for the midterm", 0, false, 0));
        assertEquals(repo.find(100).getValue(), new Goal("CSE 110 Project", 100, false, 4));
    }

    @Test
    public void prepend() {
        SimpleGoalRepository repo = new SimpleGoalRepository(InMemoryDataSource.fromDefault());
        Goal temp = new Goal("CSE 110 Project", 100, false, 0);
        repo.prepend(temp);
        assertEquals(repo.find(0).getValue(), new Goal("Prepare for the midterm", 0, false, 1));
        assertEquals(repo.find(100).getValue(), new Goal("CSE 110 Project", 100, false, 0));
    }
}