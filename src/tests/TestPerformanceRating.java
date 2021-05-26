package tests;

import dataclass.PerformanceRating;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPerformanceRating {
    @Test
    void testParsePerformance() {
        assertEquals(
                PerformanceRating.Performance.EARLY,
                PerformanceRating.parsePerformance("early")
        );
        assertEquals(
                PerformanceRating.Performance.LATE,
                PerformanceRating.parsePerformance("LATE")
        );
        assertEquals(
                PerformanceRating.Performance.ONPICKUP,
                PerformanceRating.parsePerformance("O")
        );
    }

    @Test
    void testFormattedPerformance() {
        PerformanceRating performance1 = new PerformanceRating(PerformanceRating.Performance.EARLY);
        PerformanceRating performance2 = new PerformanceRating(PerformanceRating.Performance.ONPICKUP);
        PerformanceRating performance3 = new PerformanceRating(PerformanceRating.Performance.LATE);

        assertEquals("Early", performance1.getFormattedPerformance());
        assertEquals("On Pickup", performance2.getFormattedPerformance());
        assertEquals("Late", performance3.getFormattedPerformance());
    }
}
