package edu.ucsd.cse110.successorator.data.db;

import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
public class RoomGoalRepository implements GoalRepository {
    private final GoalsDao goalsDao;

    public RoomGoalRepository(GoalsDao goalsDao){
        this.goalsDao = goalsDao;
    }
}
