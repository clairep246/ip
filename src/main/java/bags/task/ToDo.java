package bags.task;

import bags.exception.BagsException;

//Todo task type

/**
 * Represents a ToDo task in the Bags application.
 *
 * <p>A ToDo task contains a description and can be marked as done
 * or undone through the functionality inherited from {@link Task}.</p>
 *
 * <p>AI was used to assist in reconfiguring the task creation method
 * into the individual task type classes. The generated code was
 * reviewed and adapted to fit the application's requirements.</p>
 */
public class ToDo extends Task {

    /**
     * Creates a ToDo task with the given description.
     *
     * @param description the description of the task
     */
    public ToDo(String description) {
        super(description, Tasktype.TODO);
    }

    /**
     * Creates a ToDo task from an add task command.
     *
     * @param output the user's add ToDo command
     * @return a new ToDo task created from the command
     * @throws BagsException if the task description is missing
     */
    public static ToDo fromCommand(String output) throws BagsException {
        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add info after the type of task");
        }

        StringBuilder name = new StringBuilder();
        for (int i = 1; i < temp.length; i++) {
            name.append(temp[i]).append(" ");
        }

        String description = name.toString().trim();
        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add info after the type of task");
        }

        return new ToDo(description);
    }

    /**
     * Returns the ToDo task in a user-readable format.
     *
     * @return the formatted ToDo task
     */
    @Override
    public String toString() {
        return "[T][" + super.getStatusIcon() + "] " + super.getDescription();
    }

    /**
     * Converts the ToDo task into the format used for saving to
     * the storage file.
     *
     * @return the ToDo task as a storage record
     */
    @Override
    public String parseEvent() {
        return "T | " + "[" + getStatusIcon() + "] | " + description;
    }

}