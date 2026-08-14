public class ToDo extends Task {

    public ToDo(String description) {
        super(description, Tasktype.TODO);
    }

    @Override
    public String toString() {
        return "[T][" + super.getStatusIcon() + "] " + super.getDescription();
    }
}