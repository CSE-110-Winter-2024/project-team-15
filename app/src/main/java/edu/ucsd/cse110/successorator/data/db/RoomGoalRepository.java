package edu.ucsd.cse110.successorator.data.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.util.LiveDataSubjectAdapter;

public class RoomGoalRepository implements GoalRepository {
    private final GoalsDao goalsDao;
    private String lastUpdated;

    public RoomGoalRepository(GoalsDao goalsDao){
        this.goalsDao = goalsDao;
    }
    //don't understand yet
    public Subject<Goal> find(int id){
        LiveData<GoalEntity> entityLiveData = goalsDao.findAsLiveData(id);
        LiveData<Goal> goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }

    public Subject<List<Goal>> findAll(){
        var entitiesLiveData = goalsDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities ->{
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    public Subject<List<Goal>> findAll(int listNum){
        var entitiesLiveData = goalsDao.findAllAsLiveData(listNum);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities ->{
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    public void save(Goal goal){
        goalsDao.insert(GoalEntity.fromGoal(goal));
    }
    public void save(List<Goal> goals){
        var entities = goals.stream()
                .map(GoalEntity::fromGoal)
                .collect(Collectors.toList());
        goalsDao.insert(entities);
    }
    public void prepend(Goal goal){
        goalsDao.prepend(GoalEntity.fromGoal(goal));
    }
    public void insertUnderIncompleteGoals(Goal goal){
        goalsDao.insertUnderIncompleteGoals(GoalEntity.fromGoal(goal));
    }
    public void toggleCompleteGoal(Goal goal){
        goalsDao.toggleCompleteGoal(goal);
    }
    public void remove(int id){
        goalsDao.delete(id);
    }

    public void clearCompletedGoals() { goalsDao.clearCompletedGoals(); }

    public String getLastUpdated(){ return this.lastUpdated; }

    public void setLastUpdated(String lastUpdated){ this.lastUpdated = lastUpdated; }

    public String getContext(int id){ return goalsDao.getContext(id); }

}
