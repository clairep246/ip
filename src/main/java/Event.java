//Event task 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Event extends Task {

    private LocalDateTime from;
    private String formatted_from;
    private LocalDateTime to;
    private String formatted_to;


    public Event(String description, String from, String to) {
        super(description, Tasktype.EVENT);

         DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.from = LocalDateTime.parse(from, inputFormatter);
        this.to = LocalDateTime.parse(to, inputFormatter);
     
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
        this.formatted_from = this.from.format(outputFormatter);
        this.formatted_to = this.to.format(outputFormatter);

    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + formatted_from + " to: " + formatted_to + ")";
    }

    @Override
    public String parseEvent() {
        return "E | " + "[" + getStatusIcon() + "] | " + description + " | " 
        + formatted_from + " | " + formatted_to;
    }
}
