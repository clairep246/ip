public class ToDo {
    private String name;
    private static int todoCount = 0;
    private int todoId;
    private boolean isDone;

    public ToDo(String name) {
        this.name = name;
        todoCount++;
        this.todoId = todoCount;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public int getTodoId() {
        return this.todoId;
    }

    public String getName() {
        return this.name;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + name;
    }
}