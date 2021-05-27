package client;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import dataclass.*;
import dbutils.DatabaseUtils;

public class Main {

    public enum Prompt {
        NAME,
        PAY,
        TIMEOFORDER,
        WEEKDAY,
        COMPLETETIME,
        MILES,
        FOODPREP,
        RATING,
        MAINMENU,
        NEXTPAGE,
        DELETEORDER,
        SEARCHMENU,
        ENTRIES,
        OPTIONS
    }

    static Scanner keyboard = new Scanner(System.in);

    static ArrayList<DoorDashOrder> orders = new ArrayList<>();
    static ArrayList<Integer> orderIds = new ArrayList<>();

    // Option Variables
    static boolean isSentenced = false;
    static int orderPagination = 2;
    static int searchOrderRows = 3;

    public static void updateOrders() throws SQLException{
        orders = DatabaseUtils.getOrdersFromDatabase();
        orderIds = DatabaseUtils.getOrderIdsFromDatabase();
    }

    public static void updateSettings() throws SQLException {
        HashMap<String, String> settings = DatabaseUtils.getSettingsFromDatabase();

        isSentenced = Boolean.parseBoolean(settings.get("isSentenced"));
        orderPagination = Integer.parseInt(settings.get("displayOrderPagination"));
        searchOrderRows = Integer.parseInt(settings.get("searchOrderRows"));
    }

    public static void main(String[] args) throws SQLException {
        DatabaseUtils.connect();
        updateOrders();
        updateSettings();

        // Main Menu Loop
        System.out.println("Welcome To DoorDash Stats Tracker!");
        mainMenu();
    }

    public static void mainMenu() throws SQLException{
        System.out.println("\nMain Menu");
        System.out.println("1 - Record Order");
        System.out.println("2 - Show Orders");
        System.out.println("3 - Search Menu");
        System.out.println("4 - Delete Order");
        System.out.println("5 - Options");
        System.out.println("6 - Quit");

        switch (inputHandler(keyboard, Prompt.MAINMENU, orders.size())) {
            case "recordOrder" -> createOrder();
            case "showOrders" -> displayAllOrders(orderPagination, isSentenced);
            case "searchOrders" -> searchMenu();
            case "deleteOrder" -> deleteOrder();
            case "options" -> optionsMenu();
            case "quit" -> {
                return;
            }
        }

        mainMenu();
    }

    public static void searchMenu() throws SQLException{
        System.out.println("\nSearch Menu");
        System.out.println("1 - Orders From Restaurant");
        System.out.println("2 - Best Dasher Ratings");
        System.out.println("3 - Money Earned By Restaurant");
        System.out.println("4 - Hours With Most Orders");
        System.out.println("5 - Fastest Average Order Complete Times By Restaurant");
        System.out.println("6 - Cancel");

        switch (inputHandler(keyboard, Prompt.SEARCHMENU, orders.size())) {
            case "mostOrders" -> displayMostOrderedRestaurants(searchOrderRows);
            case "bestRatings" -> displayAverageRestaurantRatings(searchOrderRows);
            case "mostMoney" -> displayRestaurantEarnings(searchOrderRows);
            case "bestHours" -> displayOrdersPerHour(searchOrderRows);
            case "fastestRestaurants" -> displayFastestOrdersByRestaurant(searchOrderRows);
            case "cancel" -> { return; }
        }

        searchMenu();
    }

    public static void optionsMenu() throws SQLException{
        System.out.println("\nOptions Menu");
        System.out.println("1 - Order Display Format [" + ((isSentenced) ? "Sentenced" : "Receipt") + "]");
        System.out.println("2 - Display Order Pagination [" + orderPagination + "]");
        System.out.println("3 - Search Order Rows [" + searchOrderRows + "]");
        System.out.println("4 - Accept Changes");
        System.out.println("5 - Cancel");
        switch (inputHandler(keyboard, Prompt.OPTIONS, orders.size())) {
            case "orderDisplayFormat" -> isSentenced = !isSentenced;
            case "orderPagination" -> {
                System.out.print("Enter Order Pagination Amount > ");
                orderPagination = Integer.parseInt(inputHandler(keyboard, Prompt.ENTRIES, orders.size()));
            }
            case "searchOrderRows" -> {
                System.out.print("Enter Search Order Rows > ");
                searchOrderRows = Integer.parseInt(inputHandler(keyboard, Prompt.ENTRIES, orders.size()));
            }
            case "accept" -> {
                DatabaseUtils.updateSettingsInDatabase(isSentenced, orderPagination, searchOrderRows);
                return;
            }
            case "cancel" -> { return; }
        }

        optionsMenu();
    }

    public static void createOrder() throws SQLException{
        String input;

        int sizeOfOrders = orders.size();

        System.out.println("Fill in the prompts (Type '...' to exit at any time)\n");

        System.out.print("Enter the restaurant name > ");
        input = inputHandler(keyboard, Prompt.NAME, sizeOfOrders);
        if (input.equals("...")) return;
        String name = input;

        System.out.print("Enter the amount you were paid > $");
        input = inputHandler(keyboard, Prompt.PAY, sizeOfOrders);
        if (input.equals("...")) return;
        double pay = Double.parseDouble(input);

        System.out.print("Enter the time that the order was placed (H:M/H:M:S) > ");
        input = inputHandler(keyboard, Prompt.TIMEOFORDER, sizeOfOrders);
        if (input.equals("...")) return;
        String[] times = input.split(":", 0);
        TimeInHMS timeOfOrder = new TimeInHMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));

        System.out.print("Enter the day of the week for this order > ");
        input = inputHandler(keyboard, Prompt.WEEKDAY, sizeOfOrders);
        if (input.equals("...")) return;
        DayOfWeek dayOfWeek = new DayOfWeek(DayOfWeek.parseWeekday(input));

        System.out.print("Enter the time it took to complete the order (M/H:M/H:M:S) > ");
        input = inputHandler(keyboard, Prompt.COMPLETETIME, sizeOfOrders);
        if (input.equals("...")) return;
        times = input.split(":", 0);
        TimeInHMS completeTime = new TimeInHMS(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));

        System.out.print("Enter the miles driven for this order > ");
        input = inputHandler(keyboard, Prompt.MILES, sizeOfOrders);
        if (input.equals("...")) return;
        double milesTraveled = Double.parseDouble(input);

        System.out.print("Enter the restaurant's food prep performance (Early, OnPickup, Late) > ");
        input = inputHandler(keyboard, Prompt.FOODPREP, sizeOfOrders);
        if (input.equals("...")) return;
        PerformanceRating foodPrepPerformance = new PerformanceRating(PerformanceRating.parsePerformance(input));

        System.out.print("Enter your rating for this order (1-5) > ");
        input = inputHandler(keyboard, Prompt.RATING, sizeOfOrders);
        if (input.equals("...")) return;
        int rating = Integer.parseInt(input);

        DoorDashOrder order = new DoorDashOrder(name, pay, timeOfOrder, dayOfWeek, completeTime, milesTraveled, foodPrepPerformance, rating);

        DatabaseUtils.addOrderToDatabase(order);

        updateOrders();
    }

    public static void displayAllOrders(int paginationThreshold, boolean isSentenced) {
        if (orders.size() == 0) {
            System.out.println("You have no recorded orders");
            return;
        }

        if (orders.size() <= paginationThreshold) {
            System.out.println("--Showing " + orders.size() + " Order" + ((orders.size() == 1) ? "" : "s") + "--");
            for (DoorDashOrder order : orders) {
                if (isSentenced) System.out.println(order.getSentencedOrder());
                else System.out.println(order.getReceipt());
                System.out.println("---");
            }
            return;
        }

        // !"I absolutely positively without a doubt in my mind love pagination";
        for (int page = 0; page < orders.size(); page += paginationThreshold) {
            int entriesToShow = Math.min(orders.size() - page, paginationThreshold);

            System.out.println(
                "\n--Showing " + (entriesToShow) +
                " Order" + ((entriesToShow == 1) ? "" : "s") +
                " (Page " + (page / paginationThreshold + 1) +
                " / " + (int) Math.ceil(orders.size() / (double) paginationThreshold) + ")--");

            for (int entry = page; entry < page + entriesToShow; entry++) {
                if (isSentenced) System.out.println(orders.get(entry).getSentencedOrder());
                else System.out.println(orders.get(entry).getReceipt());

                if (entry < page + paginationThreshold - 1 || entry == orders.size() - 1) System.out.println("---");
            }
            if (page + paginationThreshold < orders.size()) {
                System.out.println("--Press Enter for next page (q to quit)--");
                if (inputHandler(keyboard, Prompt.NEXTPAGE, orders.size()).equals("quit")) { return; }
            }
        }
    }


    public static void deleteOrder() throws SQLException{
        if (orders.size() == 0) {
            System.out.println("You have no recorded orders");
            return;
        }

        System.out.println("Here are all orders on record:");
        for (int i = 0; i < orders.size(); i++) {
            System.out.println("---(" + (i + 1) + ")---");
            System.out.println(orders.get(i).getReceipt());
        }

        System.out.print("Which order would you like to delete? (Type '...' to cancel) > ");
        String input = inputHandler(keyboard, Prompt.DELETEORDER, orders.size());
        if (input.equals("...")) return;

        DatabaseUtils.deleteOrderFromDatabase(orderIds.get(Integer.parseInt(input) - 1));

        updateOrders();
    }

    public static void displayMostOrderedRestaurants(int entries) throws SQLException{
        HashMap<String, Integer> restaurantData = DatabaseUtils.getNumberOfOrdersByRestaurant();

        int size = restaurantData.size() + 1;

        System.out.println("\n--Restaurants With The Most Orders--");

        for (int i = 1; i < Math.min(entries + 1, size); i++) {
            String key = getKeyWithHighestValue(restaurantData);

            System.out.println(i + ": " + key + " - " + restaurantData.get(key));

            restaurantData.remove(key);
        }
    }

    public static void displayAverageRestaurantRatings(int entries) throws SQLException{
        HashMap<String, Float> restaurantData = DatabaseUtils.getRatingsByRestaurant();

        int size = restaurantData.size() + 1;

        System.out.println("\n--Restaurants With The Highest Average Dasher Rating--");

        for (int i = 1; i < Math.min(entries + 1, size); i++) {
            String key = getKeyWithHighestValue(restaurantData);

            System.out.println(i + ": " + key + " - " + restaurantData.get(key));

            restaurantData.remove(key);
        }
    }

    public static void displayRestaurantEarnings(int entries) throws SQLException{
        HashMap<String, Float> restaurantData = DatabaseUtils.getEarningsByRestaurant();

        int size = restaurantData.size() + 1;

        System.out.println("\n--Restaurants With The Highest Earnings--");

        for (int i = 1; i < Math.min(entries + 1, size); i++) {
            String key = getKeyWithHighestValue(restaurantData);

            System.out.println(i + ": " + key + " - $" + restaurantData.get(key));

            restaurantData.remove(key);
        }
    }

    public static void displayOrdersPerHour(int entries) throws SQLException{
        HashMap<String, Integer> earningsData = DatabaseUtils.getOrdersByHour();

        int size = earningsData.size() + 1;

        System.out.println("\n--Hours With The Most Orders--");

        for (int i = 1; i < Math.min(entries + 1, size); i++) {
            String key = getKeyWithHighestValue(earningsData);

            System.out.println(i + ": " + key + " - " + earningsData.get(key));

            earningsData.remove(key);
        }
    }

    public static void displayFastestOrdersByRestaurant(int entries) throws SQLException{
        HashMap<String, String> restaurantData = DatabaseUtils.getOrderCompleteTimesByRestaurant();

        int size = restaurantData.size() + 1;

        System.out.println("\n--Restaurants With The Fastest Order Times--");

        for (int i = 1; i < Math.min(entries + 1, size); i++) {
            String key = getKeyWithLowestTime(restaurantData);

            System.out.println(i + ": " + key + " - " + restaurantData.get(key));

            restaurantData.remove(key);
        }
    }

    public static String inputHandler(Scanner keyboard, Prompt prompt, int sizeOfOrdersArray) {
        String userInput = keyboard.nextLine().strip().toLowerCase();

        if (prompt == Prompt.NEXTPAGE && userInput.length() == 0) {
            return "nextPage";
        } else if (userInput.length() < 1) {
            if (prompt == Prompt.PAY) {
                System.out.print("Invalid Input, Try Again > $");
                return inputHandler(keyboard, prompt, sizeOfOrdersArray);
            }

            System.out.print("Invalid Input, Try Again > ");

            return inputHandler(keyboard, prompt, sizeOfOrdersArray);
        }

        if (userInput.equals("...")) {
            return "...";
        }

        switch (prompt) {
            case NAME -> { return uppercaseName(userInput); }
            case PAY, MILES -> {
                if (isNumeric(userInput) && Double.parseDouble(userInput) >= 0) return userInput;
            }
            case RATING -> {
                if (isNumeric(userInput)) {
                    if (!isWholeNumber(userInput)) break;
                    if (Integer.parseInt(userInput) < 6 && Integer.parseInt(userInput) > 0) return userInput;
                }
            }
            case ENTRIES -> {
                if (isNumeric(userInput)) {
                    if (isWholeNumber(userInput) && Integer.parseInt(userInput) > 0) return userInput;
                }
            }
            case TIMEOFORDER -> {
                StringBuilder result = new StringBuilder();
                String[] times = userInput.split(":", 10);

                if (arrayContains(times, "") || isArrayNotNumeric(times) || isInvalidArrayTimes(times)) break;

                switch (times.length) {
                    case 2 -> {
                        for (int i = 0; i < 2; i++) {
                            if (times[i].length() == 2) result.append(times[i]).append(":");
                            else if (times[i].length() == 1) result.append("0").append(times[i]).append(":");
                        }

                        result.append("00");

                        return result.toString();
                    }
                    case 3 -> {
                        for (int i = 0; i < 3; i++) {
                            if (times[i].length() == 2) result.append(times[i]).append(":");
                            else if (times[i].length() == 1) result.append("0").append(times[i]).append(":");
                        }

                        return result.toString();
                    }
                }
            }
            case COMPLETETIME -> {
                StringBuilder result = new StringBuilder();
                String[] times = userInput.split(":", 0);

                if (arrayContains(times, "") || isArrayNotNumeric(times) || isInvalidArrayTimes(times)) break;

                switch (times.length) {
                    case 1 -> {
                        result.append("00:");
                        if (times[0].length() == 2) result.append(times[0]).append(":00");
                        else if (times[0].length() == 1) result.append("0").append(times[0]).append(":00");
                    }
                    case 2 -> {
                        for (int i = 0; i < 2; i++) {
                            if (times[i].length() == 2) result.append(times[i]).append(":");
                            else if (times[i].length() == 1) result.append("0").append(times[i]).append(":");
                        }

                        result.append("00");
                    }
                    case 3 -> {
                        for (int i = 0; i < 3; i++) {
                            if (times[i].length() == 2) result.append(times[i]).append(":");
                            else if (times[i].length() == 1) result.append("0").append(times[i]).append(":");
                        }
                    }
                }

                return result.toString();
            }
            case WEEKDAY -> {
                if (isNumeric(userInput) && !isWholeNumber(userInput)) break;

                switch (userInput.charAt(0)) {
                    case 's' -> {
                        if (userInput.length() != 2) break;

                        switch (userInput.substring(0, 2)) {
                            case "su" -> { return "Sunday"; }
                            case "sa" -> { return "Saturday"; }
                        }
                    }
                    case 't' -> {
                        if (userInput.length() != 2) break;

                        switch (userInput.substring(0, 2)) {
                            case "tu" -> { return "Tuesday"; }
                            case "th" -> { return "Thursday"; }
                        }
                    }
                    case '0' -> { return "Sunday"; }
                    case '1', 'm' -> { return "Monday"; }
                    case '2' -> { return "Tuesday"; }
                    case '3', 'w' -> { return "Wednesday"; }
                    case '4' -> { return "Thursday"; }
                    case '5', 'f' -> { return "Friday"; }
                    case '6' -> { return "Saturday"; }
                }
            }
            case FOODPREP -> {
                switch (userInput.charAt(0)){
                    case 'e' -> { return "Early"; }
                    case 'o' -> { return "OnPickup"; }
                    case 'l' -> { return "Late"; }
                }
            }
            case MAINMENU -> {
                switch (userInput.charAt(0)) {
                    case 's' -> {
                        if (userInput.length() > 1) {
                            if (userInput.startsWith("sh")) return "showOrders";
                            if (userInput.startsWith("se")) return "searchOrders";
                        }
                    }
                    case 'r', '1' -> { return "recordOrder"; }
                    case '2' -> { return "showOrders"; }
                    case '3' -> { return "searchOrders"; }
                    case 'd', '4' -> { return "deleteOrder"; }
                    case 'o', '5' -> { return "options"; }
                    case 'q', '6' -> { return "quit"; }
                }
            }
            case NEXTPAGE -> {
                if (userInput.charAt(0) == 'q') return "quit";
            }
            case DELETEORDER -> {
                if (isNumeric(userInput) && isWholeNumber(userInput)) {
                    if (Integer.parseInt(userInput) <= sizeOfOrdersArray && Integer.parseInt(userInput) > 0) {
                        return userInput;
                    }
                }
            }
            case SEARCHMENU -> {
                switch (userInput.charAt(0)) {
                    case 'o', '1' -> { return "mostOrders"; }
                    case 'b', '2' -> { return "bestRatings"; }
                    case 'm', '3' -> { return "mostMoney"; }
                    case 'h', '4' -> { return "bestHours"; }
                    case 'f', '5' -> { return "fastestRestaurants"; }
                    case 'c', '6' -> { return "cancel"; }
                }
            }
            case OPTIONS -> {
                switch (userInput.charAt(0)) {
                    case 'o', '1' -> { return "orderDisplayFormat"; }
                    case 'd', '2' -> { return "orderPagination"; }
                    case 's', '3' -> { return "searchOrderRows"; }
                    case 'a', '4' -> { return "accept"; }
                    case 'c', '5' -> { return "cancel"; }
                }
            }
        }

        if (prompt == Prompt.PAY) {
            System.out.print("Invalid Input, Try Again > $");
            return inputHandler(keyboard, prompt, sizeOfOrdersArray);
        }

        System.out.print("Invalid Input, Try Again > ");
        return inputHandler(keyboard, prompt, sizeOfOrdersArray);
    }



    // Smaller QOL Functions
    public static String uppercaseString(String input) {
        return input.substring(0,1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String uppercaseName(String input) {
        String[] words = input.split(" ", 0);
        StringBuilder result = new StringBuilder();

        for (String s : words) result.append(uppercaseString(s)).append(" ");

        return result.toString().strip();
    }

    public static boolean isNumeric(String str) {
        if (str == null) return false;

        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean isWholeNumber(String str) {
        return Double.parseDouble(str) % 1 == 0;
    }

    public static boolean isArrayNotNumeric(String[] array) {
        for (String s : array) {
            if (!isNumeric(s)) return true;
        }

        return false;
    }

    public static boolean arrayContains(String[] array, String target) {
        for (String s : array) {
            if (s.equals(target)) return true;
        }

        return false;
    }

    public static boolean isInvalidArrayTimes(String[] array) {
        // Assuming that the array contains number strings
        if (array.length == 1) {
            return !isWholeNumber(array[0]) || (Integer.parseInt(array[0]) < 0 || Integer.parseInt(array[0]) >= 60);
        }

        for (int i = 0; i < array.length; i++) {
            if (!isWholeNumber(array[i])) return true;
            if (i == 0) {
                if (Integer.parseInt(array[i]) < 0 || Integer.parseInt(array[i]) > 23) {
                    return true;
                }
            }
            if (Integer.parseInt(array[i]) < 0 || Integer.parseInt(array[i]) > 59) return true;
        }

        return false;
    }

    public static String getKeyWithHighestValue(HashMap<String, ? extends Number> dict) {
        String maxKey = "";
        double maxVal = 0.0;

        for (String key : dict.keySet()) {
            if (dict.get(key).doubleValue() > maxVal) {
                maxVal = dict.get(key).doubleValue();
                maxKey = key;
            }
        }

        return maxKey;
    }

    public static String getKeyWithLowestTime(HashMap<String, String> dict) {
        String minKey = "";
        int minVal = 999999999;

        for (String key : dict.keySet()) {
            int time = TimeInHMS.parseTime(dict.get(key)).getNumeratedTime();
            if (time < minVal) {
                minVal = time;
                minKey = key;
            }
        }

        return minKey;
    }
}
