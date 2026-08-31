package bags.task;

import bags.exception.BagsException;

/**
 * Represents a ToDo task in the Bags application.
 */
public class ToDo extends Task {

    /**
     * Creates a to-do task.
     *
     * @param description text describing the task
     */
    public ToDo(String description) {
        assert description != null : "Description must not be null";

        super(description, Tasktype.TODO);

        assert getType() == Tasktype.TODO
                : "ToDo task must have TODO type";
    }

    /**
     * Creates a to-do task from an add-task command string.
     *
     * @param output user input after {@code todo}
     * @return the created to-do task
     * @throws BagsException if the description is missing
     */
    public static ToDo createTask(String output) throws BagsException {
        assert output != null : "Output to create todo task must not be null";

        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException(
                    "Missing task description! Add info after the type of task");
        }

        StringBuilder name = new StringBuilder();

        for (int i = 1; i < temp.length; i++) {
            name.append(temp[i]).append(" ");
        }

        String description = name.toString().trim();

        if (description.isEmpty()) {
            throw new BagsException(
                    "Missing task description! Add info after the type of task");
        }

        assert !description.isEmpty()
                : "A valid ToDo must have a description";

        return new ToDo(description);
    }

    /**
     * Returns the ToDo task in a user-readable format.
     *
     * @return the formatted ToDo task
     */
    @Override
    public String toString() {
        assert description != null : "ToDo description must not be null";

        return "[T][" + super.getStatusIcon() + "] "
                + super.getDescription();
    }

    /**
     * Converts the ToDo task into the format used for saving.
     *
     * @return the ToDo task as a storage record
     */
    @Override
    public String parseEvent() {
        assert description != null : "ToDo description must not be null";

        String record = "T | " + "[" + getStatusIcon() + "] | " + description;

        assert record.startsWith("T |")
                : "ToDo storage record must begin with its task type";

        return record;
    }
}