public class Deadlines extends Task {
    private String deadline;

    public Deadlines(String description, String deadline) {
        super(description, Tasktype.DEADLINE);
        this.deadline = deadline;
    }

    public String getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return "[D][" + super.getStatusIcon() + "] " + super.getDescription()
                + " (by: " + this.deadline + ")";
    }
}