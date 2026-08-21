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
     * Creates a task with the given description and task type.
     *
     * @param description the description of the task
     * @param type the type of the task
     */
    public Task(String description, Tasktype type) {
        this.description = description;
        this.type = type;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public Tasktype getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

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
     * Converts the task into the format used when saving it to the
     * task storage file.
     *
     * <p>Subclasses override this method to provide the appropriate
     * storage format for their specific task type.</p>
     *
     * @return the task formatted as a storage record
     */
    public String parseEvent() {
        return "";
    }

}