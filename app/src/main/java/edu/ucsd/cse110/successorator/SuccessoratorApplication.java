package edu.ucsd.cse110.successorator;

import android.app.Application;

import androidx.room.Room;

import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;
import edu.ucsd.cse110.successorator.data.db.SuccessoratorDatabase;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.ComplexDateTracker;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.SimpleDateTracker;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;

// default implementation from lab 4 -- uses default goals
public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private GoalRepository goalRepository;
    private MutableSubject<ComplexDateTracker> dateTracker;
    @Override
    public void onCreate(){
        super.onCreate();
        //this.dateTracker = SimpleDateTracker.getInstance();
        this.dateTracker = ComplexDateTracker.getInstance();

        // InMemoryDataSource defaults
//        this.dataSource = InMemoryDataSource.fromDefault();
//        this.dataSource = InMemoryDataSource.fromEmpty(); // use if want empty
//        this.goalRepository = new SimpleGoalRepository(dataSource);
        // NEW persistent storage
        var database = Room.databaseBuilder(
                        getApplicationContext(),
                        SuccessoratorDatabase.class,
                        "successorator-db"
                )
                .allowMainThreadQueries()
                .build();
        this.goalRepository = new RoomGoalRepository(database.goalsDao());

        // loading preferences and such
        var sharedPreferences = getSharedPreferences("successorator-db", MODE_PRIVATE);
        var isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun && database.goalsDao().count() == 0){
            goalRepository.save(InMemoryDataSource.DEFAULT_EMPTY);

            sharedPreferences.edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }
    public GoalRepository getGoalRepository() {
        return goalRepository;
    }

    public MutableSubject<ComplexDateTracker> getDateTracker() {
        return this.dateTracker;
    }

}