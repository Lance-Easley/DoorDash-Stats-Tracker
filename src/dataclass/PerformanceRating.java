package dataclass;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

public @Data
@AllArgsConstructor
class PerformanceRating implements Serializable {

    public enum Performance {
        EARLY, ONPICKUP, LATE, NULL
    }

    private Performance rating;

    public static Performance parsePerformance(String input) {
        input = input.strip().toLowerCase();
        return switch (input.charAt(0)){
            case 'e' -> Performance.EARLY;
            case 'o' -> Performance.ONPICKUP;
            case 'l' -> Performance.LATE;
            default -> Performance.NULL;
        };
    }

    // Custom Getter
    public final String getFormattedPerformance() {
        return switch (rating) {
            case EARLY -> "Early";
            case ONPICKUP -> "On Pickup";
            case LATE -> "Late";
            case NULL -> "Null";
        };
    }
}
