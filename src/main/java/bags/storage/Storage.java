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
 * <p> AI was used to generate <code>Storage</code> class. The code was then reviewed and tweaked
 * to fit the application's standards.
 */

public class Storage {

    private static final String LEGACY_FILE_PATH = "./src/main/java/data/Bags.txt";
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
     * Uses the configured save file, or an existing save from the previous
     * source-folder working-directory setup.
     * 
     * @return the save file
     */
    private File getSaveFile() {
        File legacyFile = new File(LEGACY_FILE_PATH);

        if (!saveFile.exists() && legacyFile.exists()) {
            return legacyFile;
        }

        return saveFile;
    }

    /** Saves every supplied task record, overwriting the previous content */
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

    /** Reads the raw lines from the save file.
     *
     * @return saved file content
     */
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
