package edu.ucsd.cse110.successorator.lib.domain;

public class Goals {
    public Goal markComplete(Goal goal){
        return new Goal(goal.contents(), goal.id(), true);
    }
}
