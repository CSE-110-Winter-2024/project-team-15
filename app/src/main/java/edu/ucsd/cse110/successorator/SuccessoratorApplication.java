package edu.ucsd.cse110.successorator;

import android.app.Application;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.DateTracker;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;

// default implementation from lab 4 -- uses default goals
public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private GoalRepository goalRepository;
    private static DateTracker dateTracker;
    @Override
    public void onCreate(){
        super.onCreate();
        // we use the default goals here -- change later
        this.dataSource = InMemoryDataSource.fromDefaultEmpty(); // it works
        this.goalRepository = new SimpleGoalRepository(dataSource);
        //this.dateTracker = new SimpleDateTracker();
        dateTracker = SimpleDateTracker.getInstance();

    }
    public GoalRepository getGoalRepository() {
        return goalRepository;
    }

    public static DateTracker getDateTracker() {
        return dateTracker;
    }

}