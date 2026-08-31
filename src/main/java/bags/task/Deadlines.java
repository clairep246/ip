package bags.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bags.exception.BagsException;

/**
 * Represents a task with a deadline.
 */
public class Deadlines extends Task {

    private static final DateTimeFormatter inputFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime deadline;
    private String formattedDeadline;

    /**
     * Creates a deadline task.
     *
     * @param description text describing the task
     * @param deadline deadline string in {@code yyyy-MM-dd HH:mm} format
     * @throws BagsException if the deadline format is invalid
     */
    public Deadlines(String description, String deadline) throws BagsException {
        super(description, Tasktype.DEADLINE);

        try {
            this.deadline = LocalDateTime.parse(deadline, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
            this.formattedDeadline = this.deadline.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new BagsException(
                    "Please key in date in correct format: year-month-date hh:mm in 24h");
        }
    }

    /**
     * Creates a deadline task from an add-task command string.
     *
     * @param output user input after {@code deadline}
     * @return the created deadline task
     * @throws BagsException if the description or deadline is missing, or the format is invalid
     */
    public static Deadlines createTask(String output) throws BagsException {
        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        int byIndex = findByIndex(temp);
        if (byIndex == -1) {
            throw new BagsException("Missing /by. Please add in /by <end date>");
        }

        String description = buildText(temp, 1, byIndex);
        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        String deadlineInfo = buildText(temp, byIndex + 1, temp.length);
        if (deadlineInfo.isEmpty()) {
            throw new BagsException("Missing deadline after /by! Add /by <deadline> after task name");
        }

        return new Deadlines(description, deadlineInfo);
    }

    /**
     * Finds the position of the {@code /by} keyword.
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
     * Returns the deadline as a date-time object.
     *
     * @return the deadline
     */
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
     * @return the deadline task as a storage record
     */
    @Override
    public String parseEvent() {
        return "D | " + "[" + getStatusIcon() + "] | " + description + " | "
                + deadline.format(inputFormatter);
    }
}