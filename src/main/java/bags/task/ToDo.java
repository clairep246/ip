package bags.task;

import bags.exception.BagsException;

/**
 * Represents a to-do task.
 */
public class ToDo extends Task {

    /**
     * Creates a to-do task.
     *
     * @param description text describing the task
     */
    public ToDo(String description) {
        super(description, Tasktype.TODO);
        assert description != null : "Description must not be null";
    }

    /**
     * Creates a to-do task from an add-task command string.
     *
     * @param command user input after {@code todo}
     * @return the created to-do task
     * @throws BagsException if the description is missing
     */
    public static ToDo createTask(String command) throws BagsException {
        assert command != null : "Command string must not be null";

        String[] words = command.split(" ");
        if (words.length < 2) {
            throw new BagsException(
                    "Missing task description! Add info after the type of task");
        }

        StringBuilder name = new StringBuilder();
        for (int i = 1; i < words.length; i++) {
            name.append(words[i]).append(" ");
        }

        String description = name.toString().trim();
        if (description.isEmpty()) {
            throw new BagsException(
                    "Missing task description! Add info after the type of task");
        }

        return new ToDo(description);
    }

    /**
     * Returns the task in a user-readable format.
     *
     * @return the formatted todo task
     */
    @Override
    public String toString() {
        return "[T][" + super.getStatusIcon() + "] " + super.getDescription();
    }

    /**
     * Converts the task into the format used for saving.
     *
     * @return the task as a storage record
     */
    @Override
    public String parseEvent() {
        return "T | " + "[" + getStatusIcon() + "] | " + description;
    }
}