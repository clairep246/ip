import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadlines extends Task {

    private LocalDateTime deadline;
    private String formatted_deadline;

    public Deadlines(String description, String deadline) {
        super(description, Tasktype.DEADLINE);
        
        //
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.deadline = LocalDateTime.parse(deadline, inputFormatter);
     
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
        this.formatted_deadline = this.deadline.format(outputFormatter);
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    @Override
    public String toString() {
        return "[D][" + super.getStatusIcon() + "] " + super.getDescription()
                + " (by: " + formatted_deadline + ")";
    }

    @Override
    public String parseEvent() {
        return "D | " + "[" + getStatusIcon() + "] | " + description + " | " + formatted_deadline;
    }
}
