package bags.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bags.exception.BagsException;

/**
 * Represents a task with a deadline.
 */
public class Deadlines extends Task {

    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
            throw new BagsException("Please key in date in correct format: year-month-date hh:mm in 24h");
        }
    }

    /**
     * Creates a deadline task from an add-task command string.
     *
     * @param output user input after {@code deadline}
     * @return the created deadline task
     * @throws BagsException if the description or deadline is missing, or the format is invalid
     */
    public static Deadlines fromCommand(String output) throws BagsException {
        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        int byIndex = -1;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("/by")) {
                byIndex = i;
                break;
            }
        }

        if (byIndex == -1) {
            throw new BagsException("Missing /by. Please add in /by <end date>");
        }

        StringBuilder name = new StringBuilder();
        for (int i = 1; i < byIndex; i++) {
            name.append(temp[i]).append(" ");
        }
        String description = name.toString().trim();
        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        StringBuilder deadline = new StringBuilder();
        for (int i = byIndex + 1; i < temp.length; i++) {
            deadline.append(temp[i]).append(" ");
        }
        String deadlineInfo = deadline.toString().trim();
        if (deadlineInfo.isEmpty()) {
            throw new BagsException("Missing deadline after /by! Add /by <deadline> after task name");
        }
        return new Deadlines(description, deadlineInfo);
    }

    /**
     * Returns the deadline as a date-time object.
     *
     * @return the deadline
     */
    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return "[D][" + super.getStatusIcon() + "] " + super.getDescription()
                + " (by: " + formattedDeadline + ")";
    }

    @Override
    public String parseEvent() {
        return "D | " + "[" + getStatusIcon() + "] | " + description + " | "
                + deadline.format(inputFormatter);
    }
}
