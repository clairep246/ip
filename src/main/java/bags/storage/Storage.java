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
        assert filePath != null : "Save file path must not be null";

        saveFile = new File(filePath);
    }

    /**
     * Saves every supplied task record, overwriting the previous content.
     *
     * @param taskRecords records the tasks to be saved
     */
    public void saveRecords(List<String> taskRecords) {
        assert taskRecords != null : "Task records must not be null";

        File parentDir = saveFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(saveFile)) {
            for (String record : taskRecords) {
                assert record != null : "A task record must not be null";
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
        assert parser != null : "Parser must not be null";

        List<Task> tasks = new ArrayList<>();

        //loadTaskRecords is called to return the lines from save file
        for (String record : loadTaskRecords()) {
            assert record != null : "Loaded task record must not be null";

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
     * @return the file to be used for saved task records.
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * Reads the raw lines from the save file.
     *
     * @return the records read from the save file
     */
    private List<String> loadTaskRecords() {
        List<String> taskRecords = new ArrayList<>();

        try (Scanner scanner = new Scanner(saveFile)) {
            while (scanner.hasNextLine()) {
                String record = scanner.nextLine();
                assert record != null : "Task record must not be null";
                taskRecords.add(record);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous save file found. Creating a new session!");
        }

        return taskRecords;
    }
}