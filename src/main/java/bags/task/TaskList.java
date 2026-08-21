package bags.task;

import java.util.ArrayList;
import java.util.List;

import bags.exception.BagsException;

/**
 * Stores tasks and provides operations that change the task collection.
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    private final ArrayList<String> readingFileRecords;

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
     * @param tasks tasks loaded from storage
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
        readingFileRecords = new ArrayList<>();
        for (Task task : tasks) {
            readingFileRecords.add(task.parseEvent());
        }
    }

    /**
     * Adds a task to the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
        readingFileRecords.add(task.parseEvent());
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the task list is empty.
     *
     * @return {@code true} if no tasks are stored
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns an unmodifiable view of the stored tasks.
     *
     * @return the tasks
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Returns the current file records for all tasks.
     *
     * @return the save-file records
     */
    public List<String> getReadingFileRecords() {
        return readingFileRecords;
    }

    /**
     * Marks the task selected done.
     *
     * @param output command string containing the task number
     * @return the marked task
     * @throws BagsException if the task number is missing or invalid
     */
    public Task markDone(String output) throws BagsException {
        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task number. Add a number from 1 to " + tasks.size());
        }

        try {
            int taskNumber = Integer.parseInt(temp[1]);
            if (taskNumber <= 0 || taskNumber > tasks.size()) {
                throw new BagsException("Task does not exist. Please only input number 1 to " + tasks.size());
            }
            Task task = tasks.get(taskNumber - 1);
            task.markDone();
            readingFileRecords.set(taskNumber - 1, task.parseEvent());
            return task;
        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number! Please enter a valid number from 1 to " + tasks.size());
        }
    }

    /**
     * Marks the task selected undone.
     *
     * @param output command string containing the task number
     * @return the unmarked task
     * @throws BagsException if the task number is missing or invalid
     */
    public Task markUndone(String output) throws BagsException {
        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task number. Enter value from 1 to " + tasks.size());
        }

        try {
            int taskNumber = Integer.parseInt(temp[1]);
            if (taskNumber <= 0 || taskNumber > tasks.size()) {
                throw new BagsException("Task does not exist. Enter value from 1 to " + tasks.size());
            }
            Task task = tasks.get(taskNumber - 1);
            task.markUndone();
            readingFileRecords.set(taskNumber - 1, task.parseEvent());
            return task;
        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number. Please enter a valid number from 1 to " + tasks.size());
        }
    }

    /**
     * Deletes the task selected.
     *
     * @param output command string containing the task number
     * @return the deleted task
     * @throws BagsException if the task number is missing or invalid
     */
    public Task delete(String output) throws BagsException {
        String[] temp = output.split(" ");
        if (temp.length < 2) {
            throw new BagsException("Missing task number. Enter value from 1 to " + tasks.size());
        }

        try {
            int taskNumber = Integer.parseInt(temp[1]);
            if (taskNumber <= 0 || taskNumber > tasks.size()) {
                throw new BagsException("Task does not exist. Enter value from 1 to " + tasks.size());
            }
            Task task = tasks.remove(taskNumber - 1);
            readingFileRecords.remove(taskNumber - 1);
            return task;
        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number. Please enter a valid number from 1 to " + tasks.size());
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
}
