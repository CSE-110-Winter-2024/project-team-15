package edu.ucsd.cse110.successorator.lib.domain;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import edu.ucsd.cse110.successorator.MainViewModel;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;

public class GoalTest {

    @Test
    public void testGetters() {
        Goal gol = new Goal("This is a goal", 1, true, 3, 0);
        assertEquals("This is a goal", gol.contents());
        assertEquals(Integer.valueOf(1), gol.id());
        assertEquals(Boolean.TRUE, gol.completed());
        assertEquals(3, gol.sortOrder());
    }
    @Test
    public void testEquals(){
        Goal gol = new Goal("This is a goal", 1, true, 3, 0);
        Goal eql_gol = new Goal("This is a goal", 1, true, 3, 0);
        Goal bad_cont = new Goal("This is not a goal", 1, true, 3, 0);
        Goal bad_id = new Goal("This is a goal", 2, true, 3, 0);
        Goal bad_comp = new Goal("This is a goal", 1, false, 3, 0);
        Goal bad_sort = new Goal("This is a goal", 1, true, 4, 0);
        assertEquals(gol, eql_gol);
        assertNotEquals(gol, bad_cont);
        assertNotEquals(gol, bad_comp);
        assertNotEquals(gol, bad_sort);
        assertNotEquals(gol, bad_id);
    }
    @Test
    public void testEqualsWithWiths(){
        Goal gol = new Goal("This is a goal", 1, true, 3, 0);
        Goal eql_gol = gol.withId(gol.id());
        Goal bad_id = gol.withId(12);
        Goal bad_comp = gol.withComplete(false);
        Goal bad_sort = gol.withSortOrder(999);
        assertEquals(gol, eql_gol);
        assertNotEquals(gol, bad_comp);
        assertNotEquals(gol, bad_sort);
        assertNotEquals(gol, bad_id);
    }

}