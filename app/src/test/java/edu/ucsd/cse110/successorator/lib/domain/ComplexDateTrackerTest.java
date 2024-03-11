package edu.ucsd.cse110.successorator.lib.domain;

import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
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

        //was throwing exception because the LocalDay needs a year but dateFormat did not have one,
        //so I parse using MonthDay which won't throw exception and using atYear with this will make
        //a LocalDay object which can be used for comparisons
        var today_parse = MonthDay.parse(today, dateFormat).atYear(2024).plusDays(1);
        var tomorrow_parse = MonthDay.parse(tomorrow, dateFormat).atYear(2024);

        assertEquals(today_parse, tomorrow_parse);
    }

    public void testGetNextDateDayOfMonth() {
        var date = ComplexDateTracker.getInstance().getValue();
        var today = date.getDate();
        var tomorrowDayOfMonth = date.getNextDateDayOfMonth();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");
        //hard-coded year should not matter here, but will in tests that consider the year, may need
        //help with those tests
        var today_parse = MonthDay.parse(today, dateFormat).atYear(2024).plusDays(1);
        var tomorrowExpectedDayOfMonth = today_parse.getDayOfMonth();

        assertEquals(tomorrowExpectedDayOfMonth, tomorrowDayOfMonth);
    }

    public void testGetNextDateMonthOfYear() {
        var date = ComplexDateTracker.getInstance().getValue();
        var today = date.getDate();
        var tomorrowMonthOfYear = date.getNextDateMonthOfYear();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");
        //hard-coded year should not matter here, but will in tests that consider the year, may need
        //help with those tests
        var today_parse = MonthDay.parse(today, dateFormat).atYear(2024).plusDays(1);
        var tomorrowExpectedMonthOfYear = today_parse.getMonthValue();

        assertEquals(tomorrowExpectedMonthOfYear, tomorrowMonthOfYear);
    }

    public void testGetNextDateYear() {
        //gonna need some help here
    }

    public void testGetNextDateDayOfWeek() {
        var date = ComplexDateTracker.getInstance().getValue();
        var today = date.getDate();
        var tomorrowDayOfWeek = date.getNextDateDayOfWeek();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, MMM dd");
        //hard-coded year may matter for this test since a date is not always the same day
        //of week depending on what year it is
        var today_parse = MonthDay.parse(today, dateFormat).atYear(2024).plusDays(1);
        var tomorrowExpectedDayOfWeek = today_parse.getDayOfWeek().getValue();

        assertEquals(tomorrowExpectedDayOfWeek, tomorrowDayOfWeek);
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