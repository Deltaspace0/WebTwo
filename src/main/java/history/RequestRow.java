package history;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RequestRow {
    private final LocalDateTime currentTime;
    private final double executionTime;
    private final int x;
    private final double y;
    private final double r;
    private final boolean hit;

    public RequestRow(LocalDateTime currentTime, double executionTime, int x, double y, double r, boolean hit) {
        this.currentTime = currentTime;
        this.executionTime = executionTime;
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
    }

    @Override
    public String toString() {
        return "<tr><td>" + DateTimeFormatter.ofPattern("hh:mm dd.MM.yyyy").format(currentTime) +
                "</td><td>" + executionTime + "</td><td>" + x + "</td><td>" + y + "</td><td>" + r + "</td><td>" +
                (hit?"Есть":"Нет") + "</td></tr>";
    }
}
