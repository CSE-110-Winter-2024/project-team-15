package edu.ucsd.cse110.successorator;

import android.app.Application;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;

// default implementation from lab 4 -- uses default goals
public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private GoalRepository goalRepository;
    @Override
    public void onCreate(){
        super.onCreate();
        // we use the default goals here -- change later
        this.dataSource = InMemoryDataSource.fromDefault();
        this.goalRepository = new SimpleGoalRepository(dataSource);
        // if we eventually go custom this is the way
        //this.dataSource = InMemoryDataSource.fromDefault();
//        this.dataSource = InMemoryDataSource.fromDefaultEmpty(); // it works
//        this.goalRepository = new GoalRepository(dataSource);
    }
    public GoalRepository getGoalRepository() {
        return goalRepository;
    }
}
