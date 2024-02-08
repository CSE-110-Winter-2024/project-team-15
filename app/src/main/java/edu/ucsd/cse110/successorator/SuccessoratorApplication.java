package edu.ucsd.cse110.successorator;

import android.app.Application;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

// default implementation from lab 4 -- uses default goals
public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private GoalRepository goalRepository;
    @Override
    public void onCreate(){
        super.onCreate();
        // we use the default goals here -- change later
        this.dataSource = InMemoryDataSource.fromDefault();
        this.goalRepository = new GoalRepository(dataSource);
    }
    public GoalRepository getGoalRepository() {
        return goalRepository;
    }
}
