package tests;

import dataclass.TimeInHMS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTimeInHMS {
    @Test
    void testParseTime() {
        TimeInHMS expectedTime = new TimeInHMS(12, 30, 5);
        assertEquals(TimeInHMS.parseTime("12:30:05"), expectedTime);
        assertEquals(TimeInHMS.parseTime("12:30:5"), expectedTime);

        expectedTime = new TimeInHMS(6, 7, 8);
        assertEquals(TimeInHMS.parseTime("06:07:08"), expectedTime);
        assertEquals(TimeInHMS.parseTime("6:7:8"), expectedTime);
    }

    @Test
    void testFormattedTime() {
        TimeInHMS time = new TimeInHMS(6, 30, 5);
        assertEquals("06:30:05", time.getFormattedTime());
    }

    @Test
    void testNumeratedTime() {
        TimeInHMS time = new TimeInHMS(8, 45, 1);
        assertEquals(84501, time.getNumeratedTime());
    }
}
