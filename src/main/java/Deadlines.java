
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadlines extends Task {

    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private LocalDateTime deadline;
    private String formatted_deadline;

    public Deadlines(String description, String deadline) throws BagsException {
        super(description, Tasktype.DEADLINE);

        try {
            this.deadline = LocalDateTime.parse(deadline, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma");
            this.formatted_deadline = this.deadline.format(outputFormatter);

        } catch (DateTimeParseException e) {
            throw new BagsException("Please key in date in correct format: year-month-date hh:mm in 24h");
        }
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
        return "D | " + "[" + getStatusIcon() + "] | " + description + " | "
                + deadline.format(inputFormatter);
    }
}
