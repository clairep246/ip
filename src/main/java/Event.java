//Event task 

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {

    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime from;
    private String formatted_from;
    private LocalDateTime to;
    private String formatted_to;

    public Event(String description, String from, String to) throws BagsException {
        super(description, Tasktype.EVENT);
        
        try {
            this.from = LocalDateTime.parse(from, inputFormatter);
            this.to = LocalDateTime.parse(to, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
            this.formatted_from = this.from.format(outputFormatter);
            this.formatted_to = this.to.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new BagsException("Please key in date in correct format: year-month-date hh:mm in 24h");

        }
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
                + from.format(inputFormatter) + " | " + to.format(inputFormatter);
    }
}
