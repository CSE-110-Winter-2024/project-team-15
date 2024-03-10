package edu.ucsd.cse110.successorator.lib.domain;

import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ComplexDateTrackerTest extends TestCase {

    public void testSingletonInstance() {
        var real = ComplexDateTracker.getInstance();
        var secondReal = ComplexDateTracker.getInstance();
        // assert the same object is here
        assertEquals(real, secondReal);
    }

    public void testUpdate() {
    }

    public void testGetNextDate() {
        var date = ComplexDateTracker.getInstance().getValue();
        var today = date.getDate();
        var tomorrow = date.getNextDate();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");
        var today_parse = LocalDate.from(dateFormat.parse(today)).plusDays(1);
        var tomorrow_parse = LocalDate.from(dateFormat.parse(tomorrow));

        assertEquals(today_parse, tomorrow_parse);
    }

    public void testGetNextDateDayOfMonth() {
    }

    public void testGetNextDateMonthOfYear() {
    }

    public void testGetNextDateYear() {
    }

    public void testGetNextDateDayOfWeek() {
    }

    public void testSetForwardBy() {
    }

    public void testDatePickerToLocalDateTime() {
    }

    public void testGoalToLocalDateTime() {
    }

    public void testGetWeekOfMonth() {
    }

    public void testTestGetWeekOfMonth() {
    }

    public void testGetDayOfWeek() {
    }

    public void testGoalRepresentation() {
    }

    public void testCompareGoalToTomorrow() {
    }

    public void testCompareGoalToToday() {
    }

    public void testGetNextOccurrence() {
    }
}