package bags.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bags.exception.BagsException;

/**
 * Represents a task with a deadline.
 */
public class Deadlines extends Task {

    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");

    private final LocalDateTime deadline;
    private final String formattedDeadline;

    /**
     * Creates a deadline task.
     *
     * @param description text describing the task
     * @param deadline deadline string in {@code yyyy-MM-dd HH:mm} format
     * @throws BagsException if the deadline format is invalid
     */
    public Deadlines(String description, String deadline) throws BagsException {
        super(description, Tasktype.DEADLINE);
        assert description != null : "Task description must not be null";
        assert deadline != null : "Deadline must not be null";

        try {
            this.deadline = LocalDateTime.parse(deadline, INPUT_FORMATTER);
            this.formattedDeadline = this.deadline.format(OUTPUT_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BagsException(
                    "Please key in date in correct format: year-month-date hh:mm in 24h");
        }
    }

    /**
     * Creates a deadline task from an add-task command string.
     *
     * @param command user input after {@code deadline}
     * @return the created deadline task
     * @throws BagsException if the description or deadline is missing, or the format is invalid
     */
    public static Deadlines createTask(String command) throws BagsException {
        assert command != null : "Command string must not be null";

        String[] words = command.split(" ");
        if (words.length < 2) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        int byIndex = findByIndex(words);
        if (byIndex == -1) {
            throw new BagsException("Missing /by. Please add in /by <end date>");
        }

        String description = buildText(words, 1, byIndex);
        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        String deadlineInfo = buildText(words, byIndex + 1, words.length);
        if (deadlineInfo.isEmpty()) {
            throw new BagsException("Missing deadline after /by! Add /by <deadline> after task name");
        }

        return new Deadlines(description, deadlineInfo);
    }

    /**
     * Finds the position of the {@code /by} keyword in user command.
     *
     * @param words the command words
     * @return the index of {@code /by}, or {@code -1} if it is absent
     */
    private static int findByIndex(String[] words) {
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals("/by")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Builds a string literal from a list of words.
     *
     * @param words the referenced word list
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

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    /**
     * Returns the deadline task in a user-readable format.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return "[D][" + super.getStatusIcon() + "] " + super.getDescription()
                + " (by: " + formattedDeadline + ")";
    }

    /**
     * Converts the deadline task into the format used for saving to storage.
     *
     * @return the deadline task in its stored format
     */
    @Override
    public String parseEvent() {
        return "D | " + "[" + getStatusIcon() + "] | " + description + " | "
                + deadline.format(INPUT_FORMATTER);
    }
}
