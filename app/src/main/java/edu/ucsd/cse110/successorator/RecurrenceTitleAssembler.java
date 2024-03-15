package edu.ucsd.cse110.successorator;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class RecurrenceTitleAssembler {

    private final Goal goal;

    // goal fields necessary to make titles
    private final int weekNum;
    private final int recurrenceType;
    private final int dayOfWeekToRecur;
    private final int monthToRecur;
    private final int dayDateToRecur;

    public RecurrenceTitleAssembler(Goal goal) {
        this.goal = goal;
        this.weekNum = goal.weekOfMonthToRecur();
        this.recurrenceType = goal.recurrenceType();
        this.dayOfWeekToRecur = goal.dayOfWeekToRecur();
        this.monthToRecur = goal.monthStarting();
        this.dayDateToRecur = goal.dayStarting();
    }

    public String makeTitle() {
        String dayStr = dayOfWeekToRecurStr();
        String weekStr = weekOfMonthToRecurStrSuffix();
        String monthDay = monthDayDate();

        switch (recurrenceType) {
            case 1: // daily
                return baseInfo();
            case 2: // weekly
                return baseInfo() + " on " + dayStr;
            case 3: // monthly
                return baseInfo() + " on the " + weekStr + " " + dayStr;
            case 4: // yearly
                return baseInfo() + " on " + monthDay;
            default: // one time ...
                return goal.contents();
        }
    }

    // the recur type and goal contents show in all goals
    // let's not repeat ourselves
    // this function would return something like
    // "Contents, weekly"
    private String baseInfo() {
        String recurType = recurrenceTypeStr();
        return goal.contents() + ", " + recurType;
    }

    private String recurrenceTypeStr() {
        switch (this.recurrenceType) {
            case 1: return "daily";
            case 2: return "weekly";
            case 3: return "monthly";
            case 4: return "yearly";
            default: return "";
        }
    }

    private String dayOfWeekToRecurStr() {
        switch (this.dayOfWeekToRecur) {
            case 1: return "Mon";
            case 2: return "Tue";
            case 3: return "Wed";
            case 4: return "Thu";
            case 5: return "Fri";
            case 6: return "Sat";
            case 7: return "Sun";
            default: return "";
        }
    }

    private String monthDayDate() {
        return this.monthToRecur + "/" + this.dayDateToRecur;
    }

    private String weekOfMonthToRecurStrSuffix() {
        String suffix = (weekNum == 1 || weekNum == 21 || weekNum == 31) ? "st" :
                (weekNum == 2 || weekNum == 22) ? "nd" :
                        (weekNum == 3 || weekNum == 23) ? "rd" : "th";
        return weekNum + suffix;
    }

}