package bags.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bags.exception.BagsException;
import bags.parser.Parser;
import bags.task.Task;

/**
 * Loads task records from, and saves task records to, the application's save file.
 */
public class Storage {

    private final File saveFile;

    /**
     * Creates storage for the supplied save-file path.
     *
     * @param filePath location of the task save file
     */
    public Storage(String filePath) {
        saveFile = new File(filePath);
    }

    /**
     * Saves every supplied task record, overwriting the previous content.
     *
     * @param taskRecords records to write to the save file
     */
    public void save(List<String> taskRecords) {
        File file = getSaveFile();
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (String record : taskRecords) {
                writer.write(record + "\n");
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while saving: " + e.getMessage());
        }
    }

    /**
     * Loads and recreates all tasks saved in the file.
     *
     * @param parser converts each saved record into a task
     * @return the tasks stored in the save file
     * @throws BagsException if a saved task cannot be parsed
     */
    public List<Task> loadTasks(Parser parser) throws BagsException {
        List<Task> tasks = new ArrayList<>();

        for (String record : loadTaskRecords()) {
            Task task = parser.parseTask(record);
            if (task != null) {
                tasks.add(task);
            }
        }

        return tasks;
    }

    /**
     * Returns the configured save file.
     *
     * @return the file to use for persistence
     */
    private File getSaveFile() {
        return saveFile;
    }

    /** Reads the raw lines from the save file. */
    private List<String> loadTaskRecords() {
        List<String> taskRecords = new ArrayList<>();

        try (Scanner scanner = new Scanner(getSaveFile())) {
            while (scanner.hasNextLine()) {
                taskRecords.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous save file found. Creating a new session!");
        }

        return taskRecords;
    }
}
