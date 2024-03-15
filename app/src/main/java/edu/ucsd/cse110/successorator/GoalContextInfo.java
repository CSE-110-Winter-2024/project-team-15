package edu.ucsd.cse110.successorator;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.data.db.RoomGoalRepository;

public class GoalContextInfo {
    private int contextNum;
    private static MutableSubject<GoalContextInfo> globalContextShown = null;

    private GoalContextInfo(int input){
        this.contextNum = input;

    }
    public static MutableSubject<GoalContextInfo> getInst() {

        if (globalContextShown == null) {
            SimpleSubject<GoalContextInfo> temp = new SimpleSubject<>();
            temp.setValue(new GoalContextInfo(6));
            globalContextShown = temp;
        }
        return globalContextShown;
    }
    public static void setInst(int ab){
        getInst();
        globalContextShown.setValue(new GoalContextInfo(ab));
    }

    public int getContextShown(){ return contextNum; }

    public static void resetContext(){ globalContextShown = null;}
}
