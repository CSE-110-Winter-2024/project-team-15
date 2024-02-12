package edu.ucsd.cse110.successorator.lib.domain;


public class Goals {
   public static Goal markComplete(GoalRepository goalRepository, Goal goal){
       Goal updatedGoal = new Goal(goal.contents(), goal.id(), true, goal.sortOrder());
       goalRepository.save(updatedGoal);
       //changed from OG markcomplete -> had to update/save to repo since it wasn't updating
       return updatedGoal;
    }
}
