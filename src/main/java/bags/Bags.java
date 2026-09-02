package bags;

import java.util.List;

import bags.exception.BagsException;
import bags.parser.Command;
import bags.parser.Parser;
import bags.storage.Storage;
import bags.task.Deadlines;
import bags.task.Event;
import bags.task.Task;
import bags.task.TaskList;
import bags.task.Tasktype;
import bags.task.ToDo;

/**
 * Main chatbot class for the Bags task management application.
 *
 * <p>Coordinates command parsing, task management, and storage components.</p>
 */
public class Bags {

    private static final String DEFAULT_STORAGE_PATH = "./data/Bags.txt";

    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    private boolean isAddingTask;
    private boolean isEchoMode;

    /**
     * Creates a Bags application and loads existing tasks from storage.
     */
    public Bags() {
        storage = new Storage(DEFAULT_STORAGE_PATH);
        parser = new Parser();

        try {
            //load save file and convert into tasks
            tasks = new TaskList(storage.loadTasks(parser));
        } catch (BagsException e) {
            tasks = new TaskList();
        }
    }

    /**
     * Processes a command entered by the user.
     *
     * @param input the command entered by the user
     * @return the response that should be displayed to the user
     * @throws BagsException if the command is invalid
     */
    public String processCommand(String input) throws BagsException {
        if (input == null || input.trim().isEmpty()) {
            throw new BagsException("No command was entered. Please enter a command.");
        }

        String trimmedInput = input.trim();

        if (isAddingTask) {
            return processAddingTaskMode(trimmedInput);
        }

        if (isEchoMode) {
            return processEchoMode(trimmedInput);
        }

        Command command = parser.parseCommand(trimmedInput);
        return executeCommand(command, trimmedInput);
    }

    private String processAddingTaskMode(String input) throws BagsException {
        if (input.equals("exit")) {
            isAddingTask = false;
            return "Exited editing mode.";
        }
        return addTask(input);
    }

    private String processEchoMode(String input) throws BagsException {
        if (input.equals("exit")) {
            isEchoMode = false;
            return "Exited echo mode.";
        }
        return input;
    }

    private String executeCommand(Command command, String input) throws BagsException {
        switch (command) {
            case ADD_TASK:
                isAddingTask = true;
                return """
                    Enter your task.
                    Format for each task type, follow the format closely:
                     1. todo <task name>
                     2. deadline <name> /by <year-month-day> <hour:minutes>
                     3. event <name> /from <year-month-day> <hour:minutes> <name> /to <year-month-day> <hour:minutes>
                    To exit enter exit.
                    """;
            case LIST:
                return listItems();
            case MARK:
                return markDone(input);
            case UNMARK:
                return unmarkDone(input);
            case ECHO:
                isEchoMode = true;
                return "From now on I will echo your input. To exit enter exit.";
            case DELETE:
                return deleteTask(input);
            case SEARCH:
                return searchTasks(input);
            case BYE:
                saveTasks();
                return "Bye. Hope to see you again soon!";
            default:
                throw new BagsException("The command does not exist. Please try again :(");
        }
    }

    /**
     * Adds a task to the task list.
     *
     * @param input the task command entered by the user
     * @return a message describing the added task
     * @throws BagsException if the task type or task format is invalid
     */
    private String addTask(String input) throws BagsException {
        Tasktype type = parser.parseTaskType(input);
        Task task = createTask(type, input);

        tasks.add(task);
        saveTasks();

        return "Got it, I've added the following task to the list:\n"
                + task
                + "\nNow you have "
                + tasks.getSize()
                + " tasks in your list.\n"
                + "\nEnter another task or enter exit to leave editing mode.";
    }

    private Task createTask(Tasktype type, String input) throws BagsException {
        switch (type) {
            case TODO:
                return ToDo.createTask(input);
            case DEADLINE:
                return Deadlines.createTask(input);
            case EVENT:
                return Event.createTask(input);
            default:
                throw new BagsException("Not a valid task type, only event, to do or deadline are valid.");
        }
    }

    /**
     * Displays all tasks stored in the task list.
     *
     * @return the current tasks
     * @throws BagsException if the task list is empty
     */
    private String listItems() throws BagsException {
        if (tasks.isEmpty()) {
            throw new BagsException("Your list empty. Please add some tasks!");
        }
        return tasks.toString();
    }

    /**
     * Marks the specified task as completed.
     *
     * @param input the user's mark command
     * @return a message describing the updated task
     * @throws BagsException if the task number is invalid
     */
    private String markDone(String input) throws BagsException {
        Task task = tasks.markDone(input);
        saveTasks();
        return "Ok! I've marked this task as done:\n" + task;
    }

    /**
     * Marks the specified task as incomplete.
     *
     * @param input the user's unmark command
     * @return a message describing the updated task
     * @throws BagsException if the task number is invalid
     */
    private String unmarkDone(String input) throws BagsException {
        Task task = tasks.markUndone(input);
        saveTasks();
        return "Alright! I've marked this task as undone:\n" + task;
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param input the user's delete command
     * @return a message describing the deleted task
     * @throws BagsException if the task number is invalid
     */
    private String deleteTask(String input) throws BagsException {
        Task task = tasks.delete(input);
        saveTasks();
        return "Got it! I've deleted the following task:\n"
                + task
                + "\nYou now have "
                + tasks.getSize()
                + " tasks in your task list.";
    }

    /**
     * Searches the task list for tasks containing the specified keyword.
     *
     * @param input the user's search command containing the keyword
     * @return the matching tasks
     * @throws BagsException if no search keyword is provided
     */
    private String searchTasks(String input) throws BagsException {
        return tasks.search(input);
    }

    /**
     * Saves all current tasks to the storage file.
     */
    private void saveTasks() {
        //convert all tasks into save file format
        // and store it in new file
        storage.saveRecords(tasks.saveRecords());
    }
}