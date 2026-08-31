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
 */
public class Bags {

    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;

    private boolean isAddingTask;
    private boolean isEchoMode;

    /**
     * Creates a Bags application and loads existing tasks from storage.
     */
    public Bags() {
        storage = new Storage("./data/Bags.txt");
        parser = new Parser();
        isAddingTask = false;
        isEchoMode = false;

        assert storage != null : "Storage must be initialized";
        assert parser != null : "Parser must be initialized";

        try {
            tasks = new TaskList(storage.loadTasks(parser));
        } catch (BagsException e) {
            tasks = new TaskList();
        }

        assert tasks != null : "Task list must be initialized";
        assert !isAddingTask : "Adding-task mode should initially be disabled";
        assert !isEchoMode : "Echo mode should initially be disabled";
    }

    /**
     * Processes a command entered by the user.
     *
     * @param input the command entered by the user
     * @return the response that should be displayed to the user
     * @throws BagsException if the command is invalid
     */
    public String processCommand(String input) throws BagsException {
        assert parser != null : "Parser must be initialized";
        assert tasks != null : "Task list must be initialized";
        assert storage != null : "Storage must be initialized";

        if (input == null || input.trim().isEmpty()) {
            throw new BagsException(
                    "No command was entered. Please enter a command.");
        }

        input = input.trim();

        if (isAddingTask) {
            if (input.equals("exit")) {
                isAddingTask = false;
                return "Exited editing mode.";
            }

            return addTask(input);
        }

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

        assert command != null : "Parser must return a command";

        if (command == Parser.Command.ADD_TASK) {
            isAddingTask = true;

            assert isAddingTask : "Adding-task mode must be enabled";

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

            assert isEchoMode : "Echo mode must be enabled";

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
     * @param input the task command entered by the user
     * @return a message describing the added task
     * @throws BagsException if the task type or task format is invalid
     */
    private String addTask(String input) throws BagsException {
        assert input != null : "Task input must not be null";
        assert parser != null : "Parser must be initialized";
        assert tasks != null : "Task list must be initialized";

        Tasktype type = parser.parseTaskType(input);
        Task task;

        if (type == Tasktype.TODO) {
            task = ToDo.createTask(input);
        } else if (type == Tasktype.DEADLINE) {
            task = Deadlines.createTask(input);
        } else if (type == Tasktype.EVENT) {
            task = Event.createTask(input);
        } else {
            throw new BagsException(
                    "Not a valid task type, only event, to do or deadline task.");
        }

        assert task != null : "A valid task must be created";

        tasks.add(task);
        saveTasks();

        assert tasks.getSize() > 0
                : "Task list must contain the new task";

        return "Got it, I've added the following task to the list:\n"
                + task
                + "\nNow you have "
                + tasks.getSize()
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
        assert tasks != null : "Task list must be initialized";

        if (tasks.isEmpty()) {
            throw new BagsException(
                    "Your list empty. Please add some tasks!");
        }

        String result = tasks.toString();

        assert result != null : "Task list output must not be null";

        return result;
    }

    /**
     * Marks the specified task as completed.
     *
     * @param input the user's mark command
     * @return a message describing the updated task
     * @throws BagsException if the task number is invalid
     */
    private String markDone(String input) throws BagsException {
        assert input != null : "Mark command must not be null";
        assert tasks != null : "Task list must be initialized";

        Task task = tasks.markDone(input);

        assert task != null : "Marked task must be returned";
        assert task.isDone() : "Marked task must be completed";

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
    private String unMarkDone(String input) throws BagsException {
        assert input != null : "Unmark command must not be null";
        assert tasks != null : "Task list must be initialized";

        Task task = tasks.markUndone(input);

        assert task != null : "Unmarked task must be returned";
        assert !task.isDone() : "Unmarked task must be incomplete";

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
        assert input != null : "Delete command must not be null";
        assert tasks != null : "Task list must be initialized";

        Task task = tasks.delete(input);

        assert task != null : "Deleted task must be returned";

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
     * @param input the user's search command
     * @return the matching tasks
     * @throws BagsException if no search keyword is provided
     */
    private String searchTasks(String input) throws BagsException {
        assert input != null : "Search command must not be null";
        assert tasks != null : "Task list must be initialized";

        String[] parts = input.split(" ", 2);

        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new BagsException(
                    "Please enter a keyword to search for.");
        }

        String keyword = parts[1].trim();

        assert !keyword.isEmpty() : "Search keyword must not be empty";

        List<Task> results = tasks.search(keyword);

        assert results != null : "Search results must not be null";

        if (results.isEmpty()) {
            return "No matching tasks found.";
        }

        StringBuilder output = new StringBuilder();
        output.append("Here are the matching tasks:");

        for (int i = 0; i < results.size(); i++) {
            assert results.get(i) != null
                    : "Search results must not contain null tasks";

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
        assert storage != null : "Storage must be initialized";
        assert tasks != null : "Task list must be initialized";

        storage.saveRecords(tasks.toSaveRecords());
    }
}