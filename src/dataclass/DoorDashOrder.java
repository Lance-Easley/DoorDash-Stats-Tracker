package dataclass;

import lombok.*;

import java.io.Serializable;

public @Data
@AllArgsConstructor
class DoorDashOrder implements Serializable {

    private String name;
    private double pay;
    private TimeInHMS timeOfOrder;
    private DayOfWeek dayOfWeek;
    private TimeInHMS completeTime;
    private double milesTraveled;
    private PerformanceRating foodPrepPerformance;
    private int rating;

    public final String getSentencedOrder() {
        return "You went to " + name +
                " at " + timeOfOrder.getFormattedTime() +
                " on " + dayOfWeek.getFormattedDay() +
                ". \nIt took you " + completeTime.getFormattedTime() +
                ", driving " + milesTraveled +
                " mile" + ((milesTraveled == 1.0) ? "" : "s") + ", to complete this order. " +
                "\nThe food was ready " + foodPrepPerformance.getFormattedPerformance().toLowerCase() +
                " and you gave a rating of " + rating +
                ". \nYou were paid $" + pay + ".";
    }

    public final String getReceipt() {
        return "Restaurant: " + name +
                "\nTime: " + timeOfOrder.getFormattedTime() +
                ", " + dayOfWeek.getFormattedDay() +
                "\nFood Ready: " + foodPrepPerformance.getFormattedPerformance() +
                "\nDasher Miles: " + milesTraveled +
                "\nDash Time: " + completeTime.getFormattedTime() +
                "\nDasher Pay: $" + pay +
                "\nDasher Rating: " + rating +
                " out of 5";
    }

    // Custom Getters
    public String getTimeOfOrderString() {
        return timeOfOrder.getFormattedTime();
    }

    public String getDayOfWeekString() {
        return dayOfWeek.getFormattedDay();
    }

    public String getCompleteTimeString() {
        return completeTime.getFormattedTime();
    }

    public String getFoodPrepPerformanceString() {
        return foodPrepPerformance.getFormattedPerformance();
    }
}
