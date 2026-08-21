package bags.task; 
 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bags.exception.BagsException;

/**
 * Represents a task that spans a time range.
 */
public class Event extends Task {

    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime from;
    private String formattedFrom;
    private LocalDateTime to;
    private String formattedTo;

    /**
     * Creates an event task.
     *
     * @param description text describing the task
     * @param from start date-time string in {@code yyyy-MM-dd HH:mm} format
     * @param to end date-time string in {@code yyyy-MM-dd HH:mm} format
     * @throws BagsException if either date-time format is invalid
     */
    public Event(String description, String from, String to) throws BagsException {
        super(description, Tasktype.EVENT);

        try {
            this.from = LocalDateTime.parse(from, inputFormatter);
            this.to = LocalDateTime.parse(to, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
            this.formattedFrom = this.from.format(outputFormatter);
            this.formattedTo = this.to.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new BagsException("Please key in date in correct format: year-month-date hh:mm in 24h");
        }
    }

    /**
     * Creates an event task from an add-task command string.
     *
     * @param output user input after {@code event}
     * @return the created event task
     * @throws BagsException if the description or time range is missing, or the format is invalid
     */
    public static Event fromCommand(String output) throws BagsException {
        String[] temp = output.split(" ");
        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add task info after task type");
        }

        int fromIndex = -1;
        int toIndex = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("/from")) {
                fromIndex = i;
            }
            if (temp[i].equals("/to")) {
                toIndex = i;
                break;
            }
        }

        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BagsException("Missing /from or /to! Add /from <start> /to <end> after task name");
        }

        StringBuilder name = new StringBuilder();
        for (int i = 1; i < fromIndex; i++) {
            name.append(temp[i]).append(" ");
        }
        String description = name.toString().trim();
        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add task info after task type");
        }

        StringBuilder fromInfo = new StringBuilder();
        for (int i = fromIndex + 1; i < toIndex; i++) {
            fromInfo.append(temp[i]).append(" ");
        }
        StringBuilder toInfo = new StringBuilder();
        for (int i = toIndex + 1; i < temp.length; i++) {
            toInfo.append(temp[i]).append(" ");
        }

        String fromStr = fromInfo.toString().trim();
        String toStr = toInfo.toString().trim();
        if (fromStr.isEmpty() || toStr.isEmpty()) {
            throw new BagsException("Missing timeframe after /from or /to! Maybe you forgot the dates");
        }
        return new Event(description, fromStr, toStr);
    }

    /**
     * Returns the start date-time of the event.
     *
     * @return the start date-time
     */
    public LocalDateTime getFrom() {
        return this.from;
    }

    /**
     * Returns the end date-time of the event.
     *
     * @return the end date-time
     */
    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toString() {
        return "[E][" + getStatusIcon() + "] " + description
                + " (from: " + formattedFrom + " to: " + formattedTo + ")";
    }

    @Override
    public String parseEvent() {
        return "E | " + "[" + getStatusIcon() + "] | " + description + " | "
                + from.format(inputFormatter) + " | " + to.format(inputFormatter);
    }
}
