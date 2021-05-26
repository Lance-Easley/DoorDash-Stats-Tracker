package dataclass;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

public @Data
@AllArgsConstructor
class DayOfWeek implements Serializable {

    public enum Weekday {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, NULL
    }

    private Weekday day;

    public static Weekday parseWeekday(String input) {
        input = input.strip().toLowerCase();

        switch (input.charAt(0)) {
            case 's' -> {
                return switch (input.substring(0, 2)) {
                    case "su" -> Weekday.SUNDAY;
                    case "sa" -> Weekday.SATURDAY;
                    default -> Weekday.NULL;
                };
            }
            case 't' -> {
                return switch (input.substring(0, 2)) {
                    case "tu" -> Weekday.TUESDAY;
                    case "th" -> Weekday.THURSDAY;
                    default -> Weekday.NULL;
                };
            }
            case '0' -> { return Weekday.SUNDAY; }
            case '1', 'm' -> { return Weekday.MONDAY; }
            case '2' -> { return Weekday.TUESDAY; }
            case '3', 'w' -> { return Weekday.WEDNESDAY; }
            case '4' -> { return Weekday.THURSDAY; }
            case '5', 'f' -> { return Weekday.FRIDAY; }
            case '6' -> { return Weekday.SATURDAY; }
        }
        return Weekday.NULL;
    }

    // Custom Getter
    public final String getFormattedDay() {
        return switch (day) {
            case SUNDAY -> "Sunday";
            case MONDAY -> "Monday";
            case TUESDAY -> "Tuesday";
            case WEDNESDAY -> "Wednesday";
            case THURSDAY -> "Thursday";
            case FRIDAY -> "Friday";
            case SATURDAY -> "Saturday";
            case NULL -> "Null";
        };
    }
}
