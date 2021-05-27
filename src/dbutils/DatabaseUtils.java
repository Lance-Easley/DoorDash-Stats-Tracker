package dbutils;

import dataclass.DayOfWeek;
import dataclass.DoorDashOrder;
import dataclass.PerformanceRating;
import dataclass.TimeInHMS;

import java.sql.*;
import java.util.ArrayList;

import java.util.HashMap;

public class DatabaseUtils {

    static String dbUrl = "jdbc:sqlite:src/dbutils/doordash_stats.db"; // file path to .db file
    static Connection conn;
    static String dbTable = "Orders";
    static String dbOptionsTable = "Options";

    public static void connect() throws SQLException{
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(dbUrl);
        }
    }

    public static void changeToTestTables(boolean choice){
        if (choice) {
            dbTable = "TestOrders";
            dbOptionsTable = "TestOptions";
        }
        else {
            dbTable = "Orders";
            dbOptionsTable = "Options";
        }
    }

    public static void closeConnection() throws SQLException {
        if (conn != null) {
            if (!conn.isClosed()) {
                conn.close();
            }
        }
    }

    public static Connection getConn() {
        return conn;
    }

    public static String getDbTable() {
        return dbTable;
    }

    public static void clearTestTableData() throws SQLException {
        connect();

        String resetString = "DELETE FROM TestOrders";
        PreparedStatement resetOrder = conn.prepareStatement(resetString);

        resetOrder.executeUpdate();
    }

    public static void addOrderToDatabase(DoorDashOrder order) throws SQLException {
        connect();

        String insertString = "INSERT INTO " + dbTable + " " +
                "(RestaurantName, Pay, TimeOfOrder, DayOfWeek, CompleteTime, MilesTraveled, FoodPrepPerformance, Rating) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement insertOrder = conn.prepareStatement(insertString);

        insertOrder.setString(1, order.getName());
        insertOrder.setDouble(2, order.getPay());
        insertOrder.setString(3, order.getTimeOfOrderString());
        insertOrder.setString(4, order.getDayOfWeekString());
        insertOrder.setString(5, order.getCompleteTimeString());
        insertOrder.setDouble(6, order.getMilesTraveled());
        insertOrder.setString(7, order.getFoodPrepPerformanceString());
        insertOrder.setInt(8, order.getRating());

        insertOrder.executeUpdate();
    }

    public static ArrayList<DoorDashOrder> getOrdersFromDatabase() throws SQLException {
        connect();

        String selectString = "SELECT * FROM " + dbTable;
        PreparedStatement selectOrders = conn.prepareStatement(selectString);

        ResultSet data = selectOrders.executeQuery();

        ArrayList<DoorDashOrder> result = new ArrayList<>();
        while (data.next()) {
            result.add(new DoorDashOrder(
                    data.getString("RestaurantName"),
                    data.getDouble("Pay"),
                    TimeInHMS.parseTime(data.getString("TimeOfOrder")),
                    new DayOfWeek(DayOfWeek.parseWeekday(data.getString("DayOfWeek"))),
                    TimeInHMS.parseTime(data.getString("CompleteTime")),
                    data.getDouble("MilesTraveled"),
                    new PerformanceRating(PerformanceRating.parsePerformance(data.getString("FoodPrepPerformance"))),
                    data.getInt("Rating")
            ));
        }
        return result;
    }

    public static ArrayList<Integer> getOrderIdsFromDatabase() throws SQLException {
        connect();

        String selectString = "SELECT id FROM " + dbTable;
        PreparedStatement selectOrders = conn.prepareStatement(selectString);

        ResultSet data = selectOrders.executeQuery();

        ArrayList<Integer> result = new ArrayList<>();



        while (data.next()) {
            result.add(data.getInt("id"));
        }
        return result;
    }

    public static void deleteOrderFromDatabase(int orderId) throws SQLException {
        connect();

        String deleteString = "DELETE FROM " + dbTable + " WHERE id=?";
        PreparedStatement deleteOrder = conn.prepareStatement(deleteString);

        deleteOrder.setInt(1, orderId);

        deleteOrder.executeUpdate();
    }

    public static ArrayList<String> getRestaurantNames() throws SQLException {
        connect();

        ArrayList<String> result = new ArrayList<>();

        String getRestaurantsString = "SELECT RestaurantName FROM " + dbTable;
        PreparedStatement getRestaurants = conn.prepareStatement(getRestaurantsString);

        ResultSet data = getRestaurants.executeQuery();

        while (data.next()) {
            result.add(data.getString("RestaurantName"));
        }

        return result;
    }

    // Search Functions
    public static HashMap<String, Integer> getNumberOfOrdersByRestaurant() throws SQLException {
        ArrayList<String> restaurants = getRestaurantNames();
        HashMap<String, Integer> result = new HashMap<>();

        for (String restaurant : restaurants) {
            String countString = "SELECT COUNT(*) AS total FROM " + dbTable + " WHERE RestaurantName = ?";
            PreparedStatement countOrders = conn.prepareStatement(countString);

            countOrders.setString(1, restaurant);

            ResultSet data = countOrders.executeQuery();

            result.put(restaurant, data.getInt("total"));
        }

        return result;
    }

    public static HashMap<String, Float> getRatingsByRestaurant() throws SQLException {
        ArrayList<String> restaurants = getRestaurantNames();
        HashMap<String, Float> result = new HashMap<>();

        for (String restaurant : restaurants) {
            String countString = "SELECT AVG(Rating) AS average FROM " + dbTable + " WHERE RestaurantName = ?";
            PreparedStatement countOrders = conn.prepareStatement(countString);

            countOrders.setString(1, restaurant);

            ResultSet data = countOrders.executeQuery();

            result.put(restaurant, data.getFloat("average"));
        }

        return result;
    }

    public static HashMap<String, Float> getEarningsByRestaurant() throws SQLException {
        ArrayList<String> restaurants = getRestaurantNames();
        HashMap<String, Float> result = new HashMap<>();


        for (String restaurant : restaurants) {
            String countString = "SELECT SUM(Pay) AS total FROM " + dbTable + " WHERE RestaurantName = ?";
            PreparedStatement countOrders = conn.prepareStatement(countString);

            countOrders.setString(1, restaurant);

            ResultSet data = countOrders.executeQuery();

            result.put(restaurant, data.getFloat("total"));
        }

        return result;
    }

    public static HashMap<String, Integer> getOrdersByHour() throws SQLException {
        connect();
        HashMap<String, Integer> result = new HashMap<>();

        for (int i = 0; i < 24; i++) {
            String countString = "SELECT Count(*) AS count, TimeOfOrder AS time FROM " + dbTable + " GROUP BY TimeOfOrder";
            PreparedStatement countOrders = conn.prepareStatement(countString);

            ResultSet data = countOrders.executeQuery();

            while (data.next()) {
                int time = TimeInHMS.parseTime(data.getString("time")).getNumeratedTime();

                if (time >= i * 10000 && time < (i + 1) * 10000) {
                    result.put(((i < 10) ? "0" + i : i) + ":00", data.getInt("count"));
                }
            }
        }

        return result;
    }

    public static HashMap<String, String> getOrderCompleteTimesByRestaurant() throws SQLException {
        ArrayList<String> restaurants = getRestaurantNames();
        HashMap<String, String> result = new HashMap<>();

        for (String restaurant : restaurants) {
            String countString = "SELECT CompleteTime FROM " + dbTable + " WHERE RestaurantName = ?";
            PreparedStatement countOrders = conn.prepareStatement(countString);

            countOrders.setString(1, restaurant);

            ResultSet data = countOrders.executeQuery();

            result.put(restaurant, data.getString("CompleteTime"));
        }

        return result;
    }

    public static HashMap<String, String> getSettingsFromDatabase() throws SQLException {
        HashMap<String, String> settings = new HashMap<>();

        String settingsString = "SELECT * FROM " + dbOptionsTable;
        PreparedStatement settingsStatement = conn.prepareStatement(settingsString);

        ResultSet data = settingsStatement.executeQuery();

        settings.put("isSentenced", (data.getString("isSentenced").equals("1")) ? "true" : "false");
        settings.put("displayOrderPagination", data.getString("displayOrderPagination"));
        settings.put("searchOrderRows", data.getString("searchOrderRows"));

        return settings;
    }

    public static void updateSettingsInDatabase(boolean isSentenced, int displayOrderPagination, int searchOrderRows) throws SQLException {
        String settingsString = "UPDATE " + dbOptionsTable + " SET isSentenced = ?, displayOrderPagination = ?, searchOrderRows = ?";
        PreparedStatement settingsStatement = conn.prepareStatement(settingsString);

        settingsStatement.setBoolean(1, isSentenced);
        settingsStatement.setInt(2, displayOrderPagination);
        settingsStatement.setInt(3, searchOrderRows);

        settingsStatement.executeUpdate();
    }
}
