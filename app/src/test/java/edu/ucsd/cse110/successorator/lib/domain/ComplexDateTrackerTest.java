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

    public void testShouldHappenDaily() throws Exception {
        LocalDate L = LocalDate.of(2023, 12, 31);
        LocalDate T = LocalDate.of(2024, 1, 5);
        Goal firstInstanceInRange = new Goal("first in", 0, false,
                0, 0, 1, 1, 1,
                2024, 1, 1, false);
        Goal firstInstanceOutOfRange = new Goal("first out", 1, false,
                1, 0, 1, 25, 1,
                2024, 4, 4, false);
//        Goal recursInRange = new Goal("recur in", 2, false,
//                2, 0, 1, 1, 1,
//                2023, 1, 1, false);;
//        Goal recursOutOfRange = new Goal("recur out", 3, false,
//                3, 0, 1, 1, 1,
//                2024, 3, 1, false);;
        assert ComplexDateTracker.shouldHappen(firstInstanceInRange, L, T);
        assert !ComplexDateTracker.shouldHappen(firstInstanceOutOfRange, L, T);
//        assert ComplexDateTracker.shouldHappen(recursInRange, L, T);
//        assert !ComplexDateTracker.shouldHappen(recursOutOfRange, L, T);
    }
    public void testShouldHappenWeekly() throws Exception {
        LocalDate L = LocalDate.of(2023, 12, 31);
        LocalDate T = LocalDate.of(2024, 1, 11);
//        new Goal("contents", id, completed,
//                sortOrder, listNum, recurrenceType,
//                dayStarting, monthStarting, yearStarting, dayOfWeekToRecur, weekOfMonthToRecur,
//                overflowFlag);
        Goal firstInstanceInRange = new Goal("first in", 0, false,
                0, 0, 2,
                1, 1, 2024, 1, 1,
                false);
        Goal firstInstanceOutOfRange = new Goal("first out", 1, false,
                1, 0, 2,
                25, 1, 2024, 4, 4,
                false);
        Goal recursInRange = new Goal("recur in", 2, false,
                2, 0, 2,
                21, 12, 2023, 3, 1,
                false);
//        Goal recursOutOfRange = new Goal("recur out", 3, false,
//                3, 0, 2,
//                1, 1, 2024, 3, 1,
//                false);
//        System.out.println(ComplexDateTracker.whenHappen(recursInRange, L));
        assert ComplexDateTracker.shouldHappen(firstInstanceInRange, L, T);
        assert !ComplexDateTracker.shouldHappen(firstInstanceOutOfRange, L, T);
        assert ComplexDateTracker.shouldHappen(recursInRange, L, T);
//        assert !ComplexDateTracker.shouldHappen(recursOutOfRange, L, T);
    }
    public void testShouldHappenMonthly() throws Exception {
        LocalDate L = LocalDate.of(2023, 12, 31);
        LocalDate T = LocalDate.of(2024, 1, 11);
//        new Goal("contents", id, completed,
//                sortOrder, listNum, recurrenceType,
//                dayStarting, monthStarting, yearStarting, dayOfWeekToRecur, weekOfMonthToRecur,
//                overflowFlag);
        Goal firstInstanceInRange = new Goal("first in", 0, false,
                0, 0, 3,
                1, 1, 2024, 1, 1,
                false);
        Goal firstInstanceOutOfRange = new Goal("first out", 1, false,
                1, 0, 3,
                25, 1, 2024, 4, 4,
                false);
        Goal recursInRange = new Goal("recur in", 2, false,
                2, 0, 3,
                21, 12, 2023, 5, 1,
                false);
        Goal recursOutOfRange = new Goal("recur out", 3, false,
                3, 0, 3,
                1, 1, 2023, 1, 3,
                false);
        assert ComplexDateTracker.shouldHappen(firstInstanceInRange, L, T);
        assert !ComplexDateTracker.shouldHappen(firstInstanceOutOfRange, L, T);
        assert ComplexDateTracker.shouldHappen(recursInRange, L, T);
        assert !ComplexDateTracker.shouldHappen(recursOutOfRange, L, T);
    }
    public void testShouldHappenMonthlyOverflow() throws Exception {
        LocalDate L = LocalDate.of(2023, 12, 31);
        LocalDate T = LocalDate.of(2024, 1, 11);
//        new Goal("contents", id, completed,
//                sortOrder, listNum, recurrenceType,
//                dayStarting, monthStarting, yearStarting, dayOfWeekToRecur, weekOfMonthToRecur,
//                overflowFlag);
        Goal recurOverflow = new Goal("recur overflow", 2, false,
                2, 0, 3,
                29, 12, 2023, 5, 5,
                false);

        LocalDate L2 = LocalDate.of(2024, 5, 1);
        LocalDate L3 = LocalDate.of(2024, 5, 4);
        LocalDate T2 = LocalDate.of(2024, 5, 4);
        LocalDate T3 = LocalDate.of(2024, 5, 2);
        LocalDate T4 = LocalDate.of(2024, 5, 20);
        Goal recurWeird = new Goal("recur may 3", 2, false,
                2, 0, 3,
                1, 5, 2024, 5, 5,
                false);

        assert !ComplexDateTracker.shouldHappen(recurOverflow, L, T);
        assert ComplexDateTracker.shouldHappen(recurWeird, L2, T2);
        assert !ComplexDateTracker.shouldHappen(recurWeird, L2, T3);
        assert !ComplexDateTracker.shouldHappen(recurWeird, L3, T4);
    }
    public void testWhenHappen2() throws Exception {
        Goal recurWeird = new Goal("recur may 3", 2, false,
                2, 0, 3,
                1, 5, 2024, 5, 5,
                false);
        LocalDate L2 = LocalDate.of(2024, 5, 1);
        LocalDate L3 = LocalDate.of(2024, 5, 4);
        LocalDate End2 = LocalDate.of(2024, 5, 3);
        LocalDate End3 = LocalDate.of(2024, 5, 31);

        assertEquals(ComplexDateTracker.whenHappen(recurWeird, L2), End2);
        assertEquals(ComplexDateTracker.whenHappen(recurWeird, L3), End3);

    }
    public void testShouldHappenYearly() throws Exception {
        LocalDate L = LocalDate.of(2023, 12, 31);
        LocalDate T = LocalDate.of(2024, 1, 11);
//        new Goal("contents", id, completed,
//                sortOrder, listNum, recurrenceType,
//                dayStarting, monthStarting, yearStarting, dayOfWeekToRecur, weekOfMonthToRecur,
//                overflowFlag);
        Goal firstInstanceInRange = new Goal("first in", 0, false,
                0, 0, 4,
                1, 1, 2024, 1, 1,
                false);
        Goal firstInstanceOutOfRange = new Goal("first out", 1, false,
                1, 0, 4,
                25, 1, 2025, 4, 4,
                false);
        Goal recursInRange = new Goal("recur in", 2, false,
                2, 0, 4,
                1, 1, 2023, 5, 1,
                false);
        Goal recursOutOfRange = new Goal("recur out", 3, false,
                3, 0, 4,
                21, 1, 2023, 1, 3,
                false);
        assert ComplexDateTracker.shouldHappen(firstInstanceInRange, L, T);
        assert !ComplexDateTracker.shouldHappen(firstInstanceOutOfRange, L, T);
        assert ComplexDateTracker.shouldHappen(recursInRange, L, T);
        assert !ComplexDateTracker.shouldHappen(recursOutOfRange, L, T);
    }


    public void testWhenHappenDaily() {
        LocalDate L = LocalDate.of(2024, 1, 1);
//        new Goal("contents", id, completed,
//                sortOrder, listNum, recurrenceType,
//                dayStarting, monthStarting, yearStarting, dayOfWeekToRecur, weekOfMonthToRecur,
//                overflowFlag);
//        Goal daily = new Goal("daily",   0, false,
//                0, 0, 4,
//                1, 1, 2024, 1, 1,
//                false);
//        Goal weekly = new Goal("weekly", 1, false,
//                1, 0, 4,
//                1, 1, 2024, 1, 1,
//                false);
//        Goal monthly = new Goal("monthly", 2, false,
//                2, 0, 4,
//                1, 1, 2024, 1, 1,
//                false);
//        Goal yearly = new Goal("yearly", 3, false,
//                3, 0, 4,
//                1, 1, 2024, 1, 1,
//                false);
    }
}