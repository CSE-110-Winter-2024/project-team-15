package edu.ucsd.cse110.successorator.data.db;

import androidx.room.RoomDatabase;
import androidx.room.Database;

@Database(entities = {GoalEntity.class}, version = 1)
public abstract class SuccessoratorDatabase extends RoomDatabase {
    public abstract GoalsDao goalsDao();
}
