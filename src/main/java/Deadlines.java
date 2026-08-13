public class Deadlines {
    private String name;
    private String deadline;
    private static int deadlineCount = 0;
    private int deadlineId;
    private boolean isDone;

    public Deadlines(String name, String deadline) {
        this.name = name;
        this.deadline = deadline;
        deadlineCount++;
        this.deadlineId = deadlineCount;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public int getDeadlineId() {
        return this.deadlineId;
    }

    public String getName() {
        return this.name;
    }

    public String getDeadline() {
        return this.deadline;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    @Override
    public String toString() {
        return "[D][" + getStatusIcon() + "] " + name + " (by: " + deadline + ")";
    }
}