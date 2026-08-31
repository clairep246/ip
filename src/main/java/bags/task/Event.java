package bags.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bags.exception.BagsException;

/**
 * Represents a task that spans a time range.
 */
public class Event extends Task {

    private static final DateTimeFormatter inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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

            DateTimeFormatter outputFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
            this.formattedFrom = this.from.format(outputFormatter);
            this.formattedTo = this.to.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new BagsException(
                    "Please key in date in correct format: year-month-date hh:mm in 24h");
        }
    }

    /**
     * Creates an event task from an add-task command string.
     *
     * @param output user input after {@code event}
     * @return the created event task
     * @throws BagsException if the description or time range is missing, or the format is invalid
     */
    public static Event createTask(String output) throws BagsException {
        String[] words = output.split(" ");

        if (words.length < 2) {
            throw new BagsException("Missing task description! Add task info after task type");
        }

        int fromIndex = findIndex(words, "/from");
        int toIndex = findIndex(words, "/to");

        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new BagsException(
                    "Missing /from or /to! Add /from <start> /to <end> after task name");
        }

        String description = buildText(words, 1, fromIndex);
        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add task info after task type");
        }

        String fromString = buildText(words, fromIndex + 1, toIndex);
        String toString = buildText(words, toIndex + 1, words.length);

        if (fromString.isEmpty() || toString.isEmpty()) {
            throw new BagsException(
                    "Missing timeframe after /from or /to! Maybe you forgot the dates");
        }

        return new Event(description, fromString, toString);
    }

    /**
     * Finds the position of a keyword in the command.
     *
     * @param words the command words
     * @param keyword the keyword to find
     * @return the keyword index, or {@code -1} if it is absent
     */
    private static int findIndex(String[] words, String keyword) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(keyword)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Builds a trimmed string from a range of command words.
     *
     * @param words the command words
     * @param startIndex the inclusive starting index
     * @param endIndex the exclusive ending index
     * @return the combined text
     */
    private static String buildText(String[] words, int startIndex, int endIndex) {
        StringBuilder text = new StringBuilder();

        for (int i = startIndex; i < endIndex; i++) {
            text.append(words[i]).append(" ");
        }

        return text.toString().trim();
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