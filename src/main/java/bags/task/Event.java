package bags.task; 
 
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import bags.exception.BagsException; 
 
/** 
 * Represents an event task with a start and end date and time. 
 * 
 * <p>The input formatter uses the format {@code yyyy-MM-dd HH:mm} 
 * to parse dates entered by the user and to store the event in the 
 * required file format. A separate output formatter uses the format 
 * {@code dd/MM/yyyy h:mma} to display the event dates in a more 
 * readable format to the user.</p> 
 * 
 * <p>AI was used to assist in reconfiguring the task creation method 
 * into the individual task type classes. The generated code was 
 * reviewed and adapted to fit the application's requirements.</p> 
 */ 
public class Event extends Task { 
 
    private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
    private LocalDateTime from; 
    private String formatted_from; 
    private LocalDateTime to; 
    private String formatted_to; 
 
    /** 
     * Creates an event task with the given description, start time, and end time. 
     * 
     * <p>The {@code inputFormatter} is used to convert the provided date 
     * and time strings into {@link LocalDateTime} objects. If the input 
     * does not follow the {@code yyyy-MM-dd HH:mm} format, a 
     * {@link BagsException} is thrown.</p> 
     * 
     * @param description the description of the event 
     * @param from the starting date and time in yyyy-MM-dd HH:mm format 
     * @param to the ending date and time in yyyy-MM-dd HH:mm format 
     * @throws BagsException if either date or time is in an invalid format 
     */ 
    public Event(String description, String from, String to) throws BagsException { 
        super(description, Tasktype.EVENT); 
 
        try { 
            this.from = LocalDateTime.parse(from, inputFormatter); 
            this.to = LocalDateTime.parse(to, inputFormatter); 
 
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy h:mma"); 
            this.formatted_from = this.from.format(outputFormatter); 
            this.formatted_to = this.to.format(outputFormatter); 
 
        } catch (DateTimeParseException e) { 
            throw new BagsException("Please key in date in correct format: year-month-date hh:mm in 24h"); 
 
        } 
    } 
 
    /** 
     * Creates an event task from add task command. 
     * 
     * <p>The command is expected to contain a task description, 
     * a {@code /from} keyword with a start date and time, and a 
     * {@code /to} keyword with an end date and time.</p> 
     * 
     * @param output the user's add event command 
     * @return a new Event task created from the command 
     * @throws BagsException if the command is missing the task description, 
     *                       /from, /to, or the required date and time 
     */ 
    public static Event fromCommand(String output) throws BagsException { 
        String[] temp = output.split(" "); 
        if (temp.length < 2) { 
            throw new BagsException("Missing task description! Add task info after task type"); 
        } 
 
        int fromIndex = -1; 
        int toIndex = -1; 
        for (int i = 0; i < temp.length; i++) { 
            if (temp[i].equals("/from")) { 
                fromIndex = i; 
            } 
            if (temp[i].equals("/to")) { 
                toIndex = i; 
                break; 
            } 
        } 
 
        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) { 
            throw new BagsException("Missing /from or /to! Add /from <start> /to <end> after task name"); 
        } 
 
        StringBuilder name = new StringBuilder(); 
        for (int i = 1; i < fromIndex; i++) { 
            name.append(temp[i]).append(" "); 
        } 
        String description = name.toString().trim(); 
        if (description.isEmpty()) { 
            throw new BagsException("Missing task description! Add task info after task type"); 
        } 
 
        StringBuilder fromInfo = new StringBuilder(); 
        for (int i = fromIndex + 1; i < toIndex; i++) { 
            fromInfo.append(temp[i]).append(" "); 
        } 
        StringBuilder toInfo = new StringBuilder(); 
        for (int i = toIndex + 1; i < temp.length; i++) { 
            toInfo.append(temp[i]).append(" "); 
        } 
 
        String fromStr = fromInfo.toString().trim(); 
        String toStr = toInfo.toString().trim(); 
        if (fromStr.isEmpty() || toStr.isEmpty()) { 
            throw new BagsException("Missing timeframe after /from or /to! Maybe you forgot the dates"); 
        } 
        return new Event(description, fromStr, toStr); 
    } 
 
    public LocalDateTime getFrom() { 
        return this.from; 
    } 

    public LocalDateTime getTo() { 
        return this.to; 
    } 
 
    /** 
     * Returns the event in a user-readable format. 
     * 
     * <p>The output formatter displays the date using 
     * {@code dd/MM/yyyy h:mma}, for example {@code 27/08/2026 2:00PM}.</p> 
     * 
     * @return the formatted event description 
     */ 
    @Override 
    public String toString() { 
        return "[E][" + getStatusIcon() + "] " + description 
                + " (from: " + formatted_from + " to: " + formatted_to + ")"; 
    } 
 
    /** 
     * Converts the event into the format used for saving to the storage file. 
     * 
     * <p>The input formatter is used here so that the date is saved in the 
     * same {@code yyyy-MM-dd HH:mm} format that the parser expects when 
     * loading the task again.</p> 
     * 
     * @return the event as a storage record 
     */ 
    @Override 
    public String parseEvent() { 
        return "E | " + "[" + getStatusIcon() + "] | " + description + " | " 
                + from.format(inputFormatter) + " | " + to.format(inputFormatter); 
    } 
}