package bags.task;

import java.util.ArrayList;
import java.util.List;

import bags.exception.BagsException;

/**
 * Stores tasks and provides operations that change the task collection.
 */
public class TaskList {

    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied loaded tasks.
     *
     * @param tasks the list of tasks to initialise the task list with
     */
    public TaskList(List<Task> tasks) {
        if (tasks == null) {
            this.tasks = new ArrayList<>();
        } else {
            this.tasks = new ArrayList<>(tasks);
        }
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    public int getSize() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public List<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Marks the selected task as done.
     *
     * @param command the user's mark command containing the task number
     * @return the task that was marked as done
     * @throws BagsException if the task number is missing, invalid,
     *                       or does not correspond to an existing task
     */
    public Task markDone(String command) throws BagsException {
        int index = parseTaskIndex(command);
        Task task = tasks.get(index);
        task.markDone();
        return task;
    }

    /**
     * Marks the selected task as undone.
     *
     * @param command the user's unmark command containing the task number
     * @return the task that was marked as undone
     * @throws BagsException if the task number is missing, invalid,
     *                       or does not correspond to an existing task
     */
    public Task markUndone(String command) throws BagsException {
        int index = parseTaskIndex(command);
        Task task = tasks.get(index);
        task.markUndone();
        return task;
    }

    /**
     * Deletes the task selected.
     *
     * @param command command string containing the task number
     * @return the deleted task
     * @throws BagsException if the task number is missing or invalid
     */
    public Task delete(String command) throws BagsException {
        int index = parseTaskIndex(command);
        return tasks.remove(index);
    }

    /**
     * Parses the command string to extract and validate a 0-based task index.
     *
     * @param command raw command input containing the task number
     * @return 0-based index of the selected task
     * @throws BagsException if command format or task index is invalid
     */
    private int parseTaskIndex(String command) throws BagsException {
        String[] words = command.split(" ");
        if (words.length < 2) {
            throw new BagsException(
                    "Missing task number. Please enter value from 1 to " + tasks.size());
        }

        try {
            int taskNumber = Integer.parseInt(words[1]);
            if (taskNumber <= 0 || taskNumber > tasks.size()) {
                throw new BagsException(
                        "Task does not exist. Please only input number 1 to " + tasks.size());
            }
            return taskNumber - 1;
        } catch (NumberFormatException e) {
            throw new BagsException(
                    "Invalid task number! Please enter a valid number from 1 to " + tasks.size());
        }
    }

    /**
     * Converts all tasks into formatted records suitable for Storage.
     *
     * @return list of storage-formatted task strings
     */
    public List<String> saveRecords() {
        List<String> records = new ArrayList<>();
        for (Task task : tasks) {
            records.add(task.parseEvent());
        }
        return records;
    }

    /**
     * Searches for tasks containing the keyword specified in the command string.
     *
     * @param command the user's search command containing the search keyword
     * @return formatted string of matching tasks
     * @throws BagsException if the keyword is missing or blank
     */
    public String search(String command) throws BagsException {
        if (command == null) {
            throw new BagsException("Search command cannot be null.");
        }

        String[] parts = command.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new BagsException("Please enter a keyword to search for.");
        }

        String searchKeyword = parts[1].trim().toLowerCase();

        List<Task> matchingTasks = tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(searchKeyword))
                .toList();

        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }

        StringBuilder output = new StringBuilder("Here are the matching tasks:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            output.append(System.lineSeparator())
                    .append(i + 1)
                    .append(".")
                    .append(matchingTasks.get(i));
        }

        return output.toString();
    }

    /**
     * Returns the task list in a numbered format.
     *
     * @return formatted task list string
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.append(System.lineSeparator())
                    .append(i + 1)
                    .append(".")
                    .append(tasks.get(i));
        }
        return output.toString();
    }
}