package bags.task;

/**
 * Represents the base template for all task types in the Bags application.
 *
 * <p>This abstract class stores the common properties and behaviours
 * shared by different task types such as ToDo, Deadline, and Event.</p>
 
 */
public abstract class Task {

    protected String description;
    protected boolean isDone;
    protected Tasktype type;

    /**
     * Creates a task with the given description and type.
     *
     * @param description text describing the task
     * @param type category of the task
     */
    public Task(String description, Tasktype type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Returns the type of this task.
     *
     * @return the task type
     */
    public Tasktype getType() {
        return this.type;
    }

    /**
     * Returns the description of this task.
     *
     * @return task description text
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns whether the task is marked done.
     *
     * @return {@code true} if the task is done
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Returns the status icon for the task.
     *
     * <p>An {@code X} represents a completed task, while a blank space
     * represents a task that has not been completed.</p>
     *
     * @return the status icon of the task
     */
    protected String getStatusIcon() {
        return this.isDone ? "X" : " ";
    }

    /**
     * Returns a basic string representation of the task containing
     * its completion status and description.
     *
     * @return the task status and description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Parses this task into its save-file record format.
     *
     * @return the formatted task record
     */
    public String parseEvent() {
        return "";
    }
}
