package tests;

import dataclass.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDoorDashOrder {
    @Test
    void testSentencedOrder() {
        DoorDashOrder testOrder = new DoorDashOrder(
                "TestName",
                1,
                new TimeInHMS(0, 1, 2),
                new DayOfWeek(DayOfWeek.Weekday.MONDAY),
                new TimeInHMS(2, 1, 0),
                2,
                new PerformanceRating(PerformanceRating.Performance.EARLY),
                3
        );

        assertEquals("""
                        You went to TestName at 00:01:02 on Monday.\s
                        It took you 02:01:00, driving 2.0 miles, to complete this order.\s
                        The food was ready early and you gave a rating of 3.\s
                        You were paid $1.0.""", testOrder.getSentencedOrder()
        );
    }

    @Test
    void testOrderReceipt() {
        DoorDashOrder testOrder = new DoorDashOrder(
                "TestName",
                1,
                new TimeInHMS(0, 1, 2),
                new DayOfWeek(DayOfWeek.Weekday.MONDAY),
                new TimeInHMS(2, 1, 0),
                2,
                new PerformanceRating(PerformanceRating.Performance.EARLY),
                3
        );

        assertEquals("""
                        Restaurant: TestName
                        Time: 00:01:02, Monday
                        Food Ready: Early
                        Dasher Miles: 2.0
                        Dash Time: 02:01:00
                        Dasher Pay: $1.0
                        Dasher Rating: 3 out of 5""", testOrder.getReceipt()
        );
    }
}
