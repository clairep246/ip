package bags;

import java.util.List;

import bags.exception.BagsException;
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
 * <p>
 * The {@code Bags} class coordinates command parsing, task management,
 * and storage components of the application.
 * </p>
 *
 * <p>
 * It supports creating, listing, completing, uncompleting, deleting,
 * searching, and saving tasks.
 * </p>
 */
public class Bags {

    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    private boolean isAddingTask;
    private boolean isEchoMode;

    /**
     * Creates a Bags application and loads existing tasks from storage.
     *
     * <p>
     * If the existing storage file cannot be loaded, an empty task list
     * is created instead.
     * </p>
     */
    public Bags() {
        storage = new Storage("./data/Bags.txt");
        parser = new Parser();
        isAddingTask = false;
        isEchoMode = false;

        try {
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
     */
    public String processCommand(String input) throws BagsException {

        if (input == null || input.trim().isEmpty()) {
            throw new BagsException(
                    "No command was entered. Please enter a command.");
        }

        input = input.trim();

        /*
         * If Bags is currently adding tasks, every input is treated
         * as a task command until the user enters exit.
         */
        if (isAddingTask) {

            if (input.equals("exit")) {
                isAddingTask = false;
                return "Exited editing mode.";
            }

            return addTask(input);
        }

        /*
         * If Bags is currently in echo mode, every input is echoed
         * until the user enters exit.
         */
        if (isEchoMode) {

            if (input.equals("exit")) {
                isEchoMode = false;
                return "Exited echo mode.";
            }

            if (input.isEmpty()) {
                throw new BagsException(
                        "I can't echo silence. Did you miss a command?");
            }

            return input;
        }

        Parser.Command command = parser.parseCommand(input);

        if (command == Parser.Command.ADD_TASK) {

            isAddingTask = true;

            return """
                    Enter your task.
                    Format for each task type, follow the format closely:
                     1. todo <task name>
                     2. deadline <name> /by <year-month-day> <hour:minutes>
                     3. event <name> /from <year-month-day> <hour:minutes> <name> /to <year-month-day> <hour:minutes>
                    To exit enter exit.
                    """;

        } else if (command == Parser.Command.LIST) {

            return listItems();

        } else if (command == Parser.Command.MARK) {

            return markDone(input);

        } else if (command == Parser.Command.UNMARK) {

            return unMarkDone(input);

        } else if (command == Parser.Command.ECHO) {

            isEchoMode = true;

            return "From now on I will echo your input. To exit enter exit.";

        } else if (command == Parser.Command.DELETE) {

            return deleteTask(input);

        } else if (command == Parser.Command.SEARCH) {

            return searchTasks(input);

        } else if (command == Parser.Command.BYE) {

            saveTasks();

            return "Bye. Hope to see you again soon!";

        } else {

            throw new BagsException(
                    "The command does not exist. Please try again :(");
        }
    }

    /**
     * Adds a task to the task list.
     *
     * <p>
     * This method replaces one iteration of the original
     * {@code while (!output.equals("exit"))} loop.
     * </p>
     *
     * @param input the task command entered by the user
     * @return a message describing the added task
     * @throws BagsException if the task type or task format is invalid
     */
    private String addTask(String input) throws BagsException {

        Tasktype type = parser.parseTaskType(input);
        Task task;

        if (type == Tasktype.TODO) {

            task = ToDo.fromCommand(input);

        } else if (type == Tasktype.DEADLINE) {

            task = Deadlines.fromCommand(input);

        } else if (type == Tasktype.EVENT) {

            task = Event.fromCommand(input);

        } else {

            throw new BagsException(
                    "Not a valid task type, only event, to do or deadline task.");
        }

        tasks.add(task);
        saveTasks();

        return "Got it, I've added the following task to the list:\n"
                + task
                + "\nNow you have "
                + tasks.size()
                + " tasks in your list.\n"
                + "\nEnter another task or enter exit to leave editing mode.";
    }

    /**
     * Displays all tasks currently stored in the task list.
     *
     * @return the current tasks
     * @throws BagsException if the task list is empty
     */
    private String listItems() throws BagsException {

        if (tasks.isEmpty()) {
            throw new BagsException(
                    "Your list empty. Please add some tasks!");
        }

        return tasks.toString();
    }

    /**
     * Marks the specified task as completed.
     *
     * @param input the user's mark command containing the task number
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
     * @param input the user's unmark command containing the task number
     * @return a message describing the updated task
     * @throws BagsException if the task number is invalid
     */
    private String unMarkDone(String input) throws BagsException {

        Task task = tasks.markUndone(input);
        saveTasks();

        return "Alright! I've marked this task as undone:\n" + task;
    }

    /**
     * Deletes the specified task from the task list.
     *
     * @param input the user's delete command containing the task number
     * @return a message describing the deleted task
     * @throws BagsException if the task number is invalid
     */
    private String deleteTask(String input) throws BagsException {

        Task task = tasks.delete(input);
        saveTasks();

        return "Got it! I've deleted the following task:\n"
                + task
                + "\nYou now have "
                + tasks.size()
                + " tasks in your task list.";
    }

    /**
     * Searches the task list for tasks containing the specified keyword.
     *
     * <p>
     * The keyword is extracted from the user's search command and passed
     * to the {@link TaskList} search method.
     *
     * @param input the user's search command containing the keyword
     * @return the matching tasks
     * @throws BagsException if no search keyword is provided
     */
    private String searchTasks(String input) throws BagsException {

        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new BagsException(
                    "Please enter a keyword to search for.");
        }

        String keyword = parts[1].trim();
        List<Task> results = tasks.search(keyword);

        if (results.isEmpty()) {
            return "No matching tasks found.";
        }

        StringBuilder output = new StringBuilder();
        output.append("Here are the matching tasks:");

        for (int i = 0; i < results.size(); i++) {
            output.append(System.lineSeparator())
                    .append(i + 1)
                    .append(".")
                    .append(results.get(i));
        }

        return output.toString();
    }

    /**
     * Saves all current tasks to the storage file.
     */
    private void saveTasks() {
        storage.save(tasks.toSaveRecords());
    }
}