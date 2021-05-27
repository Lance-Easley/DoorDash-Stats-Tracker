package tests;

import client.Main;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestMainQOLFunctions {

    @Test
    void testUppercaseString() {
        assertEquals("Test", Main.uppercaseString("test"));
        assertEquals("Test", Main.uppercaseString("TEST"));

        assertEquals("Testing with spaces", Main.uppercaseString("testing with spaces"));
        assertEquals("Testing with spaces", Main.uppercaseString("TESTING with SPACES"));
    }

    @Test
    void testUppercaseName() {
        assertEquals("This Is A Test Name String", Main.uppercaseName("this is a test name string"));
        assertEquals("This Is A Test Name String", Main.uppercaseName("THIS is a TEST NAME STRING"));
    }

    @Test
    void testIsNumeric() {
        assertTrue(Main.isNumeric("12345"));
        assertFalse(Main.isNumeric("12T45"));

        assertTrue(Main.isNumeric("12.345"));
        assertFalse(Main.isNumeric("12.T45"));
    }

    @Test
    void testIsWholeNumber() {
        assertTrue(Main.isWholeNumber("12345"));
        assertFalse(Main.isWholeNumber("12.345"));
    }

    @Test
    void testIsArrayNotNumeric() {
        assertTrue(Main.isArrayNotNumeric(new String[]{"123", "456", "Abc", "789"}));
        assertFalse(Main.isArrayNotNumeric(new String[]{"123", "456", "111", "789"}));

        assertTrue(Main.isArrayNotNumeric(new String[]{"1.23", "456", "Abc", "78.9"}));
        assertFalse(Main.isArrayNotNumeric(new String[]{"1.23", "456", "111", "78.9"}));
    }

    @Test
    void testArrayContains() {
        assertTrue(Main.arrayContains(new String[]{"test", "array", "for", "testing"}, "test"));
        assertTrue(Main.arrayContains(new String[]{"test", "array", "for", "testing"}, "for"));
        assertFalse(Main.arrayContains(new String[]{"test", "array", "for", "testing"}, "hello"));

        assertTrue(Main.arrayContains(new String[]{"test", "array", "with", "1"}, "1"));
        assertFalse(Main.arrayContains(new String[]{"test", "array", "with", "1"}, "2"));
    }

    @Test
    void testIsInvalidArrayTimes() {
        assertThrows(NumberFormatException.class, () -> {
            Main.isInvalidArrayTimes(new String[]{"00", "HI", "30"});
        });

        // Time limits                                   hour  min   sec
        assertTrue(Main.isInvalidArrayTimes(new String[]{"24", "60", "60"}));
        assertTrue(Main.isInvalidArrayTimes(new String[]{"24", "30", "00"}));
        assertTrue(Main.isInvalidArrayTimes(new String[]{"15", "60", "15"}));
        assertTrue(Main.isInvalidArrayTimes(new String[]{"08", "59", "60"}));

        // Valid Times
        assertFalse(Main.isInvalidArrayTimes(new String[]{"23", "59", "59"}));
        assertFalse(Main.isInvalidArrayTimes(new String[]{"12", "47", "26"}));

        // Valid Single Digit Times
        assertFalse(Main.isInvalidArrayTimes(new String[]{"8", "4", "5"}));
        assertFalse(Main.isInvalidArrayTimes(new String[]{"3", "0", "0"}));

        // > 2 Digits
        assertTrue(Main.isInvalidArrayTimes(new String[]{"600", "600", "240"}));
        assertFalse(Main.isInvalidArrayTimes(new String[]{"00008", "59", "13"}));
        assertTrue(Main.isInvalidArrayTimes(new String[]{"8", "59", "1300000000"}));
    }

    @Test
    void testGetKeyWithHighestValue() {
        HashMap<String, Integer> testDictInt = new HashMap<>();
        HashMap<String, Double> testDictDouble = new HashMap<>();
        HashMap<String, Float> testDictFloat = new HashMap<>();

        testDictInt.put("test1", 10);
        testDictInt.put("test2", 5);
        testDictInt.put("test3", 12);
        testDictInt.put("test4", 8);

        testDictDouble.put("test1", 4.3);
        testDictDouble.put("test2", 1.0);
        testDictDouble.put("test3", 3.5);
        testDictDouble.put("test4", 0.6);

        testDictFloat.put("test1", 0.3F);
        testDictFloat.put("test2", 1.1F);
        testDictFloat.put("test3", 0F);
        testDictFloat.put("test4", 5.3F);

        assertEquals("test3", Main.getKeyWithHighestValue(testDictInt));
        assertEquals("test1", Main.getKeyWithHighestValue(testDictDouble));
        assertEquals("test4", Main.getKeyWithHighestValue(testDictFloat));
    }

    @Test
    void testGetKeyWithLowestTime() {
        HashMap<String, String> testDict = new HashMap<>();

        testDict.put("test1", "00:32:4");
        testDict.put("test2", "00:8:22");
        testDict.put("test3", "0:24:50");
        testDict.put("test4", "1:3:9");

        assertEquals("test2", Main.getKeyWithLowestTime(testDict));
    }
}
