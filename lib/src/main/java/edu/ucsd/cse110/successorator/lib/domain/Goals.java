package edu.ucsd.cse110.successorator.lib.domain;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Goals {
   public static Goal markComplete(GoalRepository goalRepository, Goal goal){
       Goal updatedGoal = new Goal(goal.contents(), goal.id(), true, goal.sortOrder());
       goalRepository.save(updatedGoal);
       //changed from OG markcomplete -> had to update/save to repo since it wasn't updating
       return updatedGoal;
    }

    @NonNull
    public static List<Goal> rotate(List<Goal> goals, int k) {
        var newGoals = new ArrayList<Goal>();
        for (int i = 0; i < goals.size(); i++) {
            var thisGoal = goals.get(i);
            var thatGoal = goals.get(Math.floorMod(i + k, goals.size()));
            newGoals.add(thisGoal.withSortOrder(thatGoal.sortOrder()));
        }
        return newGoals;
    }
}
