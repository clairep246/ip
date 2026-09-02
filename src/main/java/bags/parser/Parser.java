package bags.parser;

import bags.exception.BagsException;
import bags.task.Deadlines;
import bags.task.Event;
import bags.task.Task;
import bags.task.Tasktype;
import bags.task.ToDo;

/**
 * Interprets user commands and converts saved task records back into tasks.
 *
 * <p>AI was used to assist in generating and improving the parser methods.
 * The generated code was reviewed and adapted to fit the application's
 * requirements.</p>
 */
public class Parser {

    /**
     * Determines the type of command represented by the user's input.
     *
     * @param input the command entered by the user
     * @return the corresponding command type
     */
    public Command parseCommand(String input) {
        assert input != null : "Command input must not be null";

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
        } else if (input.startsWith("echo")) {
            return Command.ECHO;
        } else if (input.startsWith("search")) {
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
     *
     * @param input user command
     * @return the parsed task type, or {@code null} if unrecognized
     */
    public Tasktype parseTaskType(String input) {
        assert input != null : "Task type input must not be null";

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
     * Creates a task object from one record loaded from the save file.
     *
     * @param taskString the saved task record to parse
     * @return the corresponding task object, or null if the record is invalid
     * @throws BagsException if the task contains invalid information
     */
    public Task parseTask(String taskString) throws BagsException {
        assert taskString != null : "Saved task record must not be null";

        String[] parts = taskString.split("\\|");

        if (parts.length < 3) {
            return null;
        }

        String type = parts[0].trim();
        String status = parts[1].trim();

        if (type.equals("T")) {
            return createTask(new ToDo(parts[2].trim()), status);
        }

        if (type.equals("D") && parts.length >= 4) {
            return createTask(
                    new Deadlines(parts[2].trim(), parts[3].trim()),
                    status);
        }

        if (type.equals("E") && parts.length >= 5) {
            return createTask(
                    new Event(parts[2].trim(), parts[3].trim(), parts[4].trim()),
                    status);
        }

        return null;
    }

    /**
     * Marks a task as done when its saved status indicates completion.
     *
     * @param task the task to update
     * @param status the saved task status
     * @return the updated task
     */
    private Task createTask(Task task, String status) {
        if (status.equals("[X]")) {
            task.markDone();
            assert task.isDone() : "Task should be marked as done";
        }

        return task;
    }
}