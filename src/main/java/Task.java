public class Task {
    private String name;
    private static int taskCount = 0;
    private boolean isDone;
    private int taskId;

    public Task(String name) {
        this.name = name;
        taskCount ++;
        this.taskId = taskCount;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public int getTaskId(){
        return this.taskId;
    }

    public String getName() {
        return this.name;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }
}
