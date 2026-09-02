package bags.task;

import java.util.ArrayList;
import java.util.List;

import bags.exception.BagsException;

/**
 * Stores tasks and provides operations that change the task collection.
 *
 * <p>The task list maintains both the {@code tasks} list containing the
 * task objects and the {@code readingFileRecords} list containing their
 * corresponding storage records. Both lists are kept synchronised so
 * that changes to a task are reflected in its stored record.</p>
 */
public class TaskList {

    private final List<Task> tasks;
    private final List<String> readingFileRecords;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
        readingFileRecords = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied loaded tasks.
     *
     * <p>The corresponding storage record for each task is also generated
     * and stored in {@code readingFileRecords}.</p>
     *
     * @param tasks the list of tasks to initialise the task list with
     */
    public TaskList(List<Task> tasks) {
        assert tasks != null : "Task list must not be null";

        this.tasks = new ArrayList<>(tasks);
        this.readingFileRecords = new ArrayList<>();

        for (Task task : tasks) {
            assert task != null : "Task list must not contain null tasks";
            readingFileRecords.add(task.parseEvent());
        }

        assert this.tasks.size() == readingFileRecords.size()
                : "Each task must have a corresponding storage record";
    }

    /**
     * Adds a task to the task list and creates its corresponding
     * storage record.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        assert task != null : "Cannot add a null task";

        tasks.add(task);
        readingFileRecords.add(task.parseEvent());

        assert tasks.size() == readingFileRecords.size()
                : "Task and storage-record lists must remain synchronized";
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
     * Returns the storage records corresponding to the tasks.
     *
     * @return an unmodifiable list of storage records
     */
    public List<String> getReadingFile() {
        return Collections.unmodifiableList(readingFileRecords);
    }

    /**
     * Marks the selected task as done and updates its corresponding
     * storage record.
     *
     * @param command the user's mark command containing the task number
     * @return the task that was marked as done
     * @throws BagsException if the task number is missing, invalid,
     *                       or does not correspond to an existing task
     */
    public Task markDone(String command) throws BagsException {
        assert command != null : "Command output must not be null";
        int index = parseTaskIndex(command);

        Task task = tasks.get(index);
        task.markDone();
        readingFileRecords.set(index, task.parseEvent());
        return task;
    }

    /**
     * Marks the selected task as undone and updates its corresponding
     * storage record.
     *
     * @param command the user's unmark command containing the task number
     * @return the task that was marked as undone
     * @throws BagsException if the task number is missing, invalid,
     *                       or does not correspond to an existing task
     */
    public Task markUndone(String command) throws BagsException {
        assert command != null : "Command output must not be null";
        int index = parseTaskIndex(command);

        Task task = tasks.get(index);
        task.markUndone();
        readingFileRecords.set(index, task.parseEvent());
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
        assert command != null : "Command output must not be null";
        int index = parseTaskIndex(command);

        Task task = tasks.remove(index);
        readingFileRecords.remove(index);
        return task;
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
     * Converts all tasks into records suitable for Storage.
     *
     * @return a copy of the save-file records
     */
    public List<String> toSaveRecords() {
        return new ArrayList<>(readingFileRecords);
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

    /**
     * Searches for tasks containing the given keyword in their description.
     *
     * @param keyword the keyword to search for
     * @return a list of tasks whose descriptions contain the keyword
     */
    public List<Task> search(String keyword) {
        assert keyword != null : "Search keyword must not be null";
        String searchKeyword = keyword.toLowerCase();

        return tasks.stream()
                .filter(task -> task.getDescription()
                        .toLowerCase()
                        .contains(searchKeyword))
                .toList();
    }
}