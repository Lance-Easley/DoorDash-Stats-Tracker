package tests;

import dataclass.DayOfWeek;
import dataclass.DoorDashOrder;
import dataclass.PerformanceRating;
import dataclass.TimeInHMS;
import dbutils.DatabaseUtils;

import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestDataBaseUtils {

    @BeforeAll
    static void clearTestTable() throws SQLException{
        DatabaseUtils.clearTestTableData();

        // Default Settings
        DatabaseUtils.updateSettingsInDatabase(false, 2, 3);
    }

    @BeforeEach
    void ensureUsingTestTable() {
        DatabaseUtils.changeToTestTables(true);
    }

    @Test
    @Order(1)
    void testChangeDatabaseTable() {
        DatabaseUtils.changeToTestTables(false);
        assertEquals("Orders", DatabaseUtils.getDbTable());

        DatabaseUtils.changeToTestTables(true);
        assertEquals("TestOrders", DatabaseUtils.getDbTable());
    }

    @Test
    @Order(1)
    void testConnection() throws SQLException {
        DatabaseUtils.connect();
        assertNotNull(DatabaseUtils.getConn());

        DatabaseUtils.closeConnection();
        assertTrue(DatabaseUtils.getConn().isClosed());
    }

    @Test
    @Order(1)
    void testClearTestTable() throws SQLException {
        // TestOrders Table is cleared before all tests

        assertEquals(new ArrayList<DoorDashOrder>(), DatabaseUtils.getOrdersFromDatabase());
    }

    @Test
    @Order(2)
    void testAddAndGetOrderFromDatabase() throws SQLException {
        DatabaseUtils.addOrderToDatabase(new DoorDashOrder(
                "TestName1",
                1,
                new TimeInHMS(0, 1, 2),
                new DayOfWeek(DayOfWeek.Weekday.MONDAY),
                new TimeInHMS(2, 1, 0),
                2,
                new PerformanceRating(PerformanceRating.Performance.EARLY),
                3
                ));

        DatabaseUtils.addOrderToDatabase(new DoorDashOrder(
                "TestName2",
                9,
                new TimeInHMS(7, 8, 9),
                new DayOfWeek(DayOfWeek.Weekday.FRIDAY),
                new TimeInHMS(9, 8, 7),
                8,
                new PerformanceRating(PerformanceRating.Performance.LATE),
                5
        ));

        ArrayList<DoorDashOrder> orders = DatabaseUtils.getOrdersFromDatabase();

        DoorDashOrder testOrder1 = orders.get(0);
        assertEquals("TestName1", testOrder1.getName());
        assertEquals(1, testOrder1.getPay());
        assertEquals(new TimeInHMS(0, 1, 2), testOrder1.getTimeOfOrder());
        assertEquals(new DayOfWeek(DayOfWeek.Weekday.MONDAY), testOrder1.getDayOfWeek());
        assertEquals(new TimeInHMS(2, 1, 0), testOrder1.getCompleteTime());
        assertEquals(2, testOrder1.getMilesTraveled());
        assertEquals(new PerformanceRating(PerformanceRating.Performance.EARLY), testOrder1.getFoodPrepPerformance());
        assertEquals(3, testOrder1.getRating());

        DoorDashOrder testOrder2 = orders.get(1);
        assertEquals("TestName2", testOrder2.getName());
        assertEquals(9, testOrder2.getPay());
        assertEquals(new TimeInHMS(7, 8, 9), testOrder2.getTimeOfOrder());
        assertEquals(new DayOfWeek(DayOfWeek.Weekday.FRIDAY), testOrder2.getDayOfWeek());
        assertEquals(new TimeInHMS(9, 8, 7), testOrder2.getCompleteTime());
        assertEquals(8, testOrder2.getMilesTraveled());
        assertEquals(new PerformanceRating(PerformanceRating.Performance.LATE), testOrder2.getFoodPrepPerformance());
        assertEquals(5, testOrder2.getRating());
    }

    @Test
    @Order(3)
    void testGetRestaurantNames() throws SQLException {
        ArrayList<String> names = DatabaseUtils.getRestaurantNames();

        assertEquals("TestName1", names.get(0));
        assertEquals("TestName2", names.get(1));
    }

    @Test
    @Order(3)
    void testNumberOfOrdersByRestaurant() throws SQLException {
        HashMap<String, Integer> restaurantData = DatabaseUtils.getNumberOfOrdersByRestaurant();
        HashMap<String, Integer> expectedData = new HashMap<>();

        expectedData.put("TestName1", 1);
        expectedData.put("TestName2", 1);

        assertEquals(expectedData, restaurantData);
    }

    @Test
    @Order(3)
    void testRatingsByRestaurant() throws SQLException {
        HashMap<String, Float> restaurantData = DatabaseUtils.getRatingsByRestaurant();
        HashMap<String, Float> expectedData = new HashMap<>();

        expectedData.put("TestName1", 3.0F);
        expectedData.put("TestName2", 5.0F);

        assertEquals(expectedData, restaurantData);
    }

    @Test
    @Order(3)
    void testEarningsByRestaurant() throws SQLException {
        HashMap<String, Float> restaurantData = DatabaseUtils.getEarningsByRestaurant();
        HashMap<String, Float> expectedData = new HashMap<>();

        expectedData.put("TestName1", 1.0F);
        expectedData.put("TestName2", 9.0F);

        assertEquals(expectedData, restaurantData);
    }

    @Test
    @Order(3)
    void testEarningsByHour() throws SQLException {
        HashMap<String, Integer> restaurantData = DatabaseUtils.getOrdersByHour();
        HashMap<String, Integer> expectedData = new HashMap<>();

        expectedData.put("00:00", 1);
        expectedData.put("07:00", 1);

        assertEquals(expectedData, restaurantData);
    }

    @Test
    @Order(3)
    void testFastestCompleteTimesByRestaurant() throws SQLException {
        HashMap<String, String> restaurantData = DatabaseUtils.getOrderCompleteTimesByRestaurant();
        HashMap<String, String> expectedData = new HashMap<>();

        expectedData.put("TestName1", "02:01:00");
        expectedData.put("TestName2", "09:08:07");

        assertEquals(expectedData, restaurantData);
    }

    @Test
    @Order(4)
    void testDeleteOrderFromDatabase() throws SQLException {
        // Deletes Order from testAddAndGetOrderFromDatabase test
        ArrayList<DoorDashOrder> orders = DatabaseUtils.getOrdersFromDatabase();
        ArrayList<Integer> orderIds = DatabaseUtils.getOrderIdsFromDatabase();

        DoorDashOrder testOrder = orders.get(0);
        int testOrderId = orderIds.get(0);

        assertEquals("TestName1", testOrder.getName());

        DatabaseUtils.deleteOrderFromDatabase(testOrderId);

        assertEquals(1, DatabaseUtils.getOrdersFromDatabase().size());
    }

    @Test
    @Order(2)
    void testUpdateAndGetOptionsFromDatabase() throws SQLException {
        DatabaseUtils.updateSettingsInDatabase(true, 5, 10);

        HashMap<String, String> testSettings = DatabaseUtils.getSettingsFromDatabase();

        assertEquals("true", testSettings.get("isSentenced"));
        assertEquals("5", testSettings.get("displayOrderPagination"));
        assertEquals("10", testSettings.get("searchOrderRows"));
    }
}
