public class Deadlines extends Task{
    private String deadline;
    public Deadlines(String name, String deadline) {
        super(name);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D][" + super.getStatusIcon() + "] " + super.getName() + " (by: " + this.deadline + ")";
    }
}