package dataclass;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


public @Data
@AllArgsConstructor
class TimeInHMS implements Serializable {

    private int hour;
    private int minute;
    private int second;

    public static TimeInHMS parseTime(String input) {
        String[] times = input.split(":", 0);

        int hour;
        int minute;
        int second;

        switch (times.length) {
            case 1 -> {
                hour = 0;
                minute = Integer.parseInt(times[0]);
                second = 0;
            }
            case 2 -> {
                hour = Integer.parseInt(times[0]);
                minute = Integer.parseInt(times[1]);
                second = 0;
            }
            case 3 -> {
                hour = Integer.parseInt(times[0]);
                minute = Integer.parseInt(times[1]);
                second = Integer.parseInt(times[2]);
            }
            default -> {
                hour = 0;
                minute = 0;
                second = 0;
            }
        }
        return new TimeInHMS(hour, minute, second);
    }

    // Custom Getters
    public String getFormattedTime() {
        String displayHour = Integer.toString(hour);
        String displayMinute = Integer.toString(minute);
        String displaySecond = Integer.toString(second);
        if (hour < 10) displayHour = "0" + hour;
        if (minute < 10) displayMinute = "0" + minute;
        if (second < 10) displaySecond = "0" + second;
        return displayHour + ":" + displayMinute + ":" + displaySecond;
    }

    public int getNumeratedTime() {
        return (hour * 10000) + (minute * 100) + (second);
    }
}
