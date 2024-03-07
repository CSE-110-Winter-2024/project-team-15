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
        String recurType = recurrenceTypeStr();
        String dayStr = dayOfWeekToRecurStr();
        String weekStr = weekOfMonthToRecurStrSuffix();
        String monthDay = monthDayDate();

        switch (recurrenceType) {
            case 1: // daily
                return goal.contents() + recurType;
            case 2: // weekly
                return goal.contents() + recurType + " on " + dayStr;
            case 3: // monthly
                return goal.contents() + recurType + " on the " + weekStr + " " + dayStr;
            case 4: // yearly
                return goal.contents() + ", yearly on " + monthDay;
            default: // one time ...
                return goal.contents();
        }
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
            case 1: return "Sat";
            case 2: return "Sun";
            case 3: return "Mon";
            case 4: return "Tue";
            case 5: return "Wed";
            case 6: return "Thu";
            case 7: return "Fri";
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