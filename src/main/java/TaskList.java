import java.util.ArrayList;
import java.util.List;

/** Stores tasks and provides operations that change the task collection. */
//Use AI to add tasklist related methods from Bags.java
public class TaskList {
    private final ArrayList<Task> tasks;
    private final ArrayList<String> readingFile;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
        readingFile = new ArrayList<>();
    }

    /** Creates a task list containing the supplied loaded tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
        readingFile = new ArrayList<>();
        for (Task task : tasks) {
            readingFile.add(task.parseEvent());
        }
    }

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

    /** Marks the task selected done*/
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

    /** Marks the task selected undone */
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

    /** Deletes the task selected  */
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

    /** Returns the task list in the numbered format */
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
