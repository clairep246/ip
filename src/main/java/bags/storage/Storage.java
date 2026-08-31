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
 *
 * <p>AI was used to generate the {@code Storage} class. The code was then
 * reviewed to fit the application's standards.</p>
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

        assert saveFile != null : "Save file must be created";
    }

    /**
     * Saves every supplied task record, overwriting the previous content.
     *
     * @param taskRecords records to write to the save file
     */
    public void saveRecords(List<String> taskRecords) {
        assert taskRecords != null : "Task records must not be null";
        assert saveFile != null : "Save file must be available";

        File file = getSaveFile();
        File parentDir = file.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileWriter writer = new FileWriter(file)) {
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
        assert saveFile != null : "Save file must be available";

        List<Task> tasks = new ArrayList<>();

        for (String record : loadTaskRecords()) {
            assert record != null : "Loaded task record must not be null";

            Task task = parser.parseTask(record);

            if (task != null) {
                tasks.add(task);
            }
        }

        assert tasks != null : "Task list must be created";

        return tasks;
    }

    /**
     * Returns the configured save file.
     *
     * @return the file to use for persistence
     */
    private File getSaveFile() {
        assert saveFile != null : "Save file must not be null";

        return saveFile;
    }

    /**
     * Reads the raw lines from the save file.
     *
     * @return the records read from the save file
     */
    private List<String> loadTaskRecords() {
        assert saveFile != null : "Save file must be available";

        List<String> taskRecords = new ArrayList<>();

        try (Scanner scanner = new Scanner(getSaveFile())) {
            while (scanner.hasNextLine()) {
                String record = scanner.nextLine();

                assert record != null : "task record must not be null";

                taskRecords.add(record);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous save file found. Creating a new session!");
        }

        assert taskRecords != null : "Task records list must be created";

        return taskRecords;
    }
}