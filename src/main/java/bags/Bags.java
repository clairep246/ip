package bags;

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
    private String startupMessage;

    private boolean isAddingTask;
    private boolean isEchoMode;
    private boolean isEditingMode;
    private String editingCommand;

    /**
     * Creates a Bags application and loads existing tasks from storage.
     */
    public Bags() {
        this(DEFAULT_STORAGE_PATH);
    }

    /**
     * Creates a Bags application that stores tasks at the supplied path.
     *
     * @param storagePath location of the task save file
     */
    Bags(String storagePath) {
        storage = new Storage(storagePath);
        parser = new Parser();

        try {
            tasks = new TaskList(storage.loadTasks(parser));
            if (storage.isSaveFileMissing()) {
                startupMessage = "No saved task file was found. Starting a new session. Please wait for a moment!";
            }
        } catch (BagsException e) {
            tasks = new TaskList();
            startupMessage = "Unable to load saved tasks. Starting a new session. Please hold on for a while!" + e.getMessage();
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
        if (input == null) {
            throw new BagsException("No command entered. Were you missing a command?"
                    + "Enter a command to get started!");
        }

        String trimmedInput = input.trim();

        if (isEchoMode) {
            return processEchoMode(trimmedInput);
        }

        if (trimmedInput.isEmpty()) {
            throw new BagsException("No command entered. Were you missing a command? "
                    + "Type a command to get started! ");
        }

        if (isAddingTask) {
            return processAddingTaskMode(trimmedInput);
        }

        if (isEditingMode) {
            return processEditingTaskMode(trimmedInput);
        }

        Command command = parser.parseCommand(trimmedInput);
        assert command != null : "Parsed command must not be null";

        String response = executeCommand(command, trimmedInput);
        assert response != null : "Command execution response should not be null";

        return response;
    }

    /**
     * Returns the startup alert for a save-file problem, if one occurred.
     */
    public String getStartupMessage() {
        return startupMessage;
    }

    private String processAddingTaskMode(String input) throws BagsException {
        assert isAddingTask : "Should only process adding task mode when flag is true";
        if (input.equals("exit")) {
            isAddingTask = false;
            return "Exited editing mode.";
        }

        Tasktype taskType = parser.parseTaskType(input);
        if (taskType == null) {
            throw new BagsException("Invalid task type! :( "
                    + "Enter a todo, deadline, or event task, or type exit to cancel.");
        }

        return addTask(input);
    }

    private String processEditingTaskMode(String input) throws BagsException {
        assert isEditingMode : "Should only process editing task mode when flag is true";
        assert editingCommand != null : "Edit command must exist while editing a task";

        if (input.equals("exit")) {
            isEditingMode = false;
            editingCommand = null;
            return "Exited editing mode without changing the task.";
        }

        Tasktype taskType = parser.parseTaskType(input);
        if (taskType == null) {
            throw new BagsException("Enter a todo, deadline, or event task, or enter exit to cancel.");
        }

        Task replacementTask = createTask(taskType, input);
        Task updatedTask = tasks.edit(editingCommand, replacementTask);

        isEditingMode = false;
        editingCommand = null;
        saveTasks();

        return "Got it, I've updated the following task:\n" + updatedTask;
    }

    private String processEchoMode(String input) throws BagsException {
        assert isEchoMode : "Should only process echo mode when flag is true";
        if (input.equals("exit")) {
            isEchoMode = false;
            return "Exited echo mode.";
        }
        if (input.isEmpty()) {
            throw new BagsException("Enter a word for me to echo! I can't echo silence :(");
        }
        return input;
    }

    private String executeCommand(Command command, String input) throws BagsException {
        assert command != null : "Command to execute cannot be null";
        assert !input.isEmpty() : "Input string to executeCommand should not be empty";

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
            case EDIT:
                return startEditingTask(input);
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
        assert input != null && !input.isEmpty() : "Task input to add must not be null or empty";

        int initialSize = tasks.getSize();
        Tasktype type = parser.parseTaskType(input);
        assert type != null : "Parsed Task type must not be null";

        Task task = createTask(type, input);

        tasks.add(task);
        assert tasks.getSize() == initialSize + 1 : "TaskList size should increase by 1 after addition";

        saveTasks();

        return "Got it, I've added the following task to the list:\n"
                + task
                + "\nNow you have "
                + tasks.getSize()
                + " tasks in your list.\n"
                + "\nEnter another task or enter exit to leave editing mode.";
    }

    private Task createTask(Tasktype type, String input) throws BagsException {
        assert type != null : "Task type cannot be null when creating task";
        assert input != null && !input.isEmpty() : "Input string cannot be empty when creating task";

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
     * Selects a task and enters editing mode for its replacement.
     *
     * @param input the user's edit command containing the task number
     * @return instructions for entering a replacement task
     * @throws BagsException if the task number is invalid
     */
    private String startEditingTask(String input) throws BagsException {
        Task task = tasks.getTask(input);

        isEditingMode = true;
        editingCommand = input;

        return """
                Editing this task:
                %s

                Enter a replacement task using the same task type:
                • todo <task name>
                • deadline <task name> /by YYYY-MM-DD HH:MM
                • event <task name> /from YYYY-MM-DD HH:MM /to YYYY-MM-DD HH:MM

                Type exit to cancel.
                """.formatted(task);
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
        assert task != null : "Task returned after markDone should not be null";
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
        assert task != null : "Task returned after markUndone should not be null";
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
        int initialSize = tasks.getSize();
        Task task = tasks.delete(input);

        assert task != null : "Deleted task object should not be null";
        assert tasks.getSize() == initialSize - 1 : "TaskList size should decrease by 1 after deletion";

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
        assert input != null : "Search input should not be null";
        return tasks.search(input);
    }

    /**
     * Saves all current tasks to the storage file.
     */
    private void saveTasks() throws BagsException {
        assert storage != null : "Storage component must exist to save tasks";
        assert tasks != null : "TaskList must exist to retrieve records for saving";
        storage.saveRecords(tasks.saveRecords());
    }
}
