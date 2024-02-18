package edu.ucsd.cse110.successorator.lib.domain;

public interface DateTracker {
    String getDate();

    int getHour();

    void update();

    void forwardUpdate();
}
