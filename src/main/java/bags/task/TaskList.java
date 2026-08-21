package bags.task;

import java.util.ArrayList;
import java.util.List;

import bags.exception.BagsException;

/**
 * Stores tasks and provides operations that change the task collection.
 *
 * <p>The task list maintains both the {@code tasks} list containing the
 * task objects and the {@code readingFile} list containing their
 * corresponding storage records. Both lists are kept synchronised so
 * that changes to a task are reflected in its stored record.</p>
 *
 * <p>AI was used to assist in adding task-list-related methods from the
 * Bags class into this TaskList class. The generated code was reviewed
 * and adapted to fit the application's requirements.</p>
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    private final ArrayList<String> readingFile;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
        readingFile = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied loaded tasks.
     *
     * <p>The corresponding storage record for each task is also generated
     * and stored in {@code readingFile}.</p>
     *
     * @param tasks the list of tasks to initialise the task list with
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
        readingFile = new ArrayList<>();
        for (Task task : tasks) {
            readingFile.add(task.parseEvent());
        }
    }

    /**
     * Adds a task to the task list and creates its corresponding
     * storage record.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
        readingFile.add(task.parseEvent());
    }


    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }


    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Returns the storage records corresponding to the tasks.
     *
     * @return the list of storage records
     */
    public List<String> getReadingFile() {
        return readingFile;
    }

    /**
     * Marks the selected task as done and updates its corresponding
     * storage record.
     *
     * @param output the user's mark command containing the task number
     * @return the task that was marked as done
     * @throws BagsException if the task number is missing, invalid,
     *                       or does not correspond to an existing task
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
            readingFile.set(taskNumber - 1, task.parseEvent());
            return task;
        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number! Please enter a valid number from 1 to " + tasks.size());
        }
    }

    /**
     * Marks the selected task as undone and updates its corresponding
     * storage record.
     *
     * @param output the user's unmark command containing the task number
     * @return the task that was marked as undone
     * @throws BagsException if the task number is missing, invalid,
     *                       or does not correspond to an existing task
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
            readingFile.set(taskNumber - 1, task.parseEvent());
            return task;
        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number. Please enter a valid number from 1 to " + tasks.size());
        }
    }

    /**
     * Deletes the selected task and its corresponding storage record.
     *
     * <p>The task and storage record use the same index, so removing
     * the same index from both lists keeps them synchronised.</p>
     *
     * @param output the user's delete command containing the task number
     * @return the task that was deleted
     * @throws BagsException if the task number is missing, invalid,
     *                       or does not correspond to an existing task
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
            readingFile.remove(taskNumber - 1);
            return task;
        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number. Please enter a valid number from 1 to " + tasks.size());
        }
    }

    /** Converts all tasks into records suitable for Storage. */
    public List<String> toSaveRecords() {
        return new ArrayList<>(readingFile);
    }

    /**
     * Returns the task list in the numbered format used when displaying
     * tasks to the user.
     *
     * @return the numbered task list as a string
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