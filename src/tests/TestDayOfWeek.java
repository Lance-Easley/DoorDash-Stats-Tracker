package tests;

import dataclass.DayOfWeek;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDayOfWeek {
    @Test
    void testParsePerformance() {
        assertEquals(
                DayOfWeek.Weekday.MONDAY,
                DayOfWeek.parseWeekday("Monday")
        );
        assertEquals(
                DayOfWeek.Weekday.TUESDAY,
                DayOfWeek.parseWeekday("tuesday")
        );
        assertEquals(
                DayOfWeek.Weekday.SUNDAY,
                DayOfWeek.parseWeekday("SUNDAY")
        );
    }

    @Test
    void testFormattedDay() {
        DayOfWeek weekday1 = new DayOfWeek(DayOfWeek.Weekday.TUESDAY);
        DayOfWeek weekday2 = new DayOfWeek(DayOfWeek.Weekday.THURSDAY);
        DayOfWeek weekday3 = new DayOfWeek(DayOfWeek.Weekday.FRIDAY);

        assertEquals("Tuesday", weekday1.getFormattedDay());
        assertEquals("Thursday", weekday2.getFormattedDay());
        assertEquals("Friday", weekday3.getFormattedDay());
    }
}
