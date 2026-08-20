//Task class that is used a template to be overriden for other task types
public abstract class Task {

    protected String description;
    protected boolean isDone;
    protected Tasktype type;

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

    protected String getStatusIcon() {
        return this.isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    //Parse task to format it before appending to task file 
    public String parseEvent() {
        return "";
    }

}
