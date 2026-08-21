package bags.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bags.exception.BagsException;

/**
 * Represents a deadline task with a specific date and time 
 * the task should be completed.

 */
public class Deadlines extends Task {

    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime deadline;
    private String formatted_deadline;

    /**
     * Creates a deadline task with the given description and deadline.
     *
     * @param description the description of the task
     * @param deadline the deadline in yyyy-MM-dd HH:mm format
     * @throws BagsException if the deadline is in an invalid format
     */
    public Deadlines(String description, String deadline) throws BagsException {
        super(description, Tasktype.DEADLINE);

        try {
            this.deadline = LocalDateTime.parse(deadline, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
            this.formatted_deadline = this.deadline.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new BagsException("Please key in date in correct format: year-month-date hh:mm in 24h");
        }
    }

    /**
     * Creates a deadline task from an add task command by splitting input and formatting each section.
     *
     * @param output the user's add deadline command
     * @return a new Deadlines task created from the command
     * @throws BagsException if the command is missing the task description,
     *                       /by keyword, or deadline
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
                + " (by: " + formatted_deadline + ")";
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