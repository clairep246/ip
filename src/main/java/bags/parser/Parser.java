package bags.parser;

import bags.exception.BagsException;
import bags.task.Deadlines;
import bags.task.Event;
import bags.task.Task;
import bags.task.Tasktype;
import bags.task.ToDo;

/**
 * Interprets user commands and converts saved task records back into tasks.
 */
public class Parser {

    public enum Command {
        ADD_TASK, LIST, MARK, UNMARK, ECHO, DELETE, SEARCH, BYE, EMPTY, UNKNOWN
    }

    /**
     * Determines the type of command represented by the user's input.
     */
    public Command parseCommand(String input) {
        if (input.trim().isEmpty()) {
            return Command.EMPTY;
        } else if (input.startsWith("add task")) {
            return Command.ADD_TASK;
        } else if (input.startsWith("list")) {
            return Command.LIST;
        } else if (input.startsWith("mark")) {
            return Command.MARK;
        } else if (input.startsWith("unmark")) {
            return Command.UNMARK;
        } else if (input.equals("echo")) {
            return Command.ECHO;
        } else if(input.startsWith("search")) {
            return Command.SEARCH;
        } else if (input.startsWith("delete")) {
            return Command.DELETE;
        } else if (input.startsWith("bye")) {
            return Command.BYE;
        }

        return Command.UNKNOWN;
    }

    /**
     * Determines the task type specified at the beginning of a task command.
     */
    public Tasktype parseTaskType(String input) {
        if (input.startsWith("todo")) {
            return Tasktype.TODO;
        } else if (input.startsWith("deadline")) {
            return Tasktype.DEADLINE;
        } else if (input.startsWith("event")) {
            return Tasktype.EVENT;
        }

        return null;
    }

    /**
     * Creates tasks objects from one record loaded from the save file.
     */
    public Task parseTask(String taskString) throws BagsException {
        String[] parts = taskString.split("\\|");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        String status = parts[1].trim();
        Task task = null;

        if (type.equals("T")) {
            task = new ToDo(parts[2].trim());

        } else if (type.equals("D") && parts.length >= 4) {
            task = new Deadlines(parts[2].trim(), parts[3].trim());

        } else if (type.equals("E") && parts.length >= 5) {
            task = new Event(parts[2].trim(), parts[3].trim(), parts[4].trim());

        }

        if (task != null && status.equals("[X]")) {
            task.markDone();
            
        }

        return task;
    }
}
