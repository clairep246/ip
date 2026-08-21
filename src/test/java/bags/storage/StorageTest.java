package bags.storage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import bags.parser.Parser;
import bags.task.Task;

/**
 * Tests the {@link Storage} class for saving and loading task records.
 *
 * <p>
 * These tests verify that task records are correctly written to files,
 * loaded from files, and handled when invalid records or
 * file-related errors are encountered.
 * </p>
 *
 * <p>
 * AI was used to assist in generating the test cases and test scenarios.
 * The generated tests were reviewed and adapted to ensure that they are
 * relevant to the expected behaviour of the {@link Storage} class.
 * </p>
 */
class StorageTest {

    @TempDir
    Path tempDir;

    /**
     * Tests that valid task records are correctly written to a file.
     *
     * @throws Exception if an unexpected file operation error occurs
     */
    @Test
    void save_validRecords_writesRecordsToFile() throws Exception {

        Path file = tempDir.resolve("Bags.txt");

        Storage storage = new Storage(file.toString());

        List<String> records = List.of(
                "T | [ ] | Read book",
                "T | [X] | Do homework"
        );

        storage.save(records);

        List<String> savedRecords = Files.readAllLines(file);

        assertEquals(records, savedRecords);
    }

    /**
     * Tests that saving to a directory instead of a file handles
     * the I/O exception correctly.
     *
     * @throws Exception if an unexpected file operation error occurs
     */
    @Test
    void save_directoryAsFile_handlesIOException() throws Exception {

        Path directory = tempDir.resolve("Bags");

        Files.createDirectory(directory);

        Storage storage = new Storage(directory.toString());

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(output));

            storage.save(List.of("T | [ ] | Read book"));

            assertTrue(
                    output.toString().contains(
                            "Something went wrong while saving:")
            );

        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * Tests that valid task records are correctly loaded and converted
     * into {@link Task} objects.
     *
     * @throws Exception if an unexpected file or parsing error occurs
     */
    @Test
    void loadTasks_validRecords_returnsCorrectTasks() throws Exception {

        Path file = tempDir.resolve("Bags.txt");

        Files.write(
                file,
                List.of(
                        "T | [ ] | Read book",
                        "T | [X] | Do homework"
                )
        );

        Storage storage = new Storage(file.toString());
        Parser parser = new Parser();

        List<Task> tasks = storage.loadTasks(parser);

        assertEquals(2, tasks.size());

        assertEquals(
                "Read book",
                tasks.get(0).getDescription()
        );

        assertFalse(tasks.get(0).isDone());

        assertEquals(
                "Do homework",
                tasks.get(1).getDescription()
        );

        assertTrue(tasks.get(1).isDone());
    }

    /**
     * Tests that invalid task records are ignored while valid
     * records are loaded correctly.
     *
     * @throws Exception if an unexpected file or parsing error occurs
     */
    @Test
    void loadTasks_invalidRecord_ignoresInvalidTask() throws Exception {

        Path file = tempDir.resolve("Bags.txt");

        Files.write(
                file,
                List.of(
                        "T | [ ] | Read book",
                        "X | [ ] | Invalid task",
                        "T | [X] | Do homework"
                )
        );

        Storage storage = new Storage(file.toString());
        Parser parser = new Parser();

        List<Task> tasks = storage.loadTasks(parser);

        assertEquals(2, tasks.size());

        assertEquals(
                "Read book",
                tasks.get(0).getDescription()
        );

        assertEquals(
                "Do homework",
                tasks.get(1).getDescription()
        );
    }

    /**
     * Tests that attempting to load tasks from a file that does not
     * exist returns an empty task list.
     *
     * @throws Exception if an unexpected application error occurs
     */
    @Test
    void loadTasks_missingFile_returnsEmptyList() throws Exception {

        Path file = tempDir.resolve("DoesNotExist.txt");

        // Deliberately do not create the file.
        Storage storage = new Storage(file.toString());
        Parser parser = new Parser();

        List<Task> tasks = storage.loadTasks(parser);

        assertTrue(tasks.isEmpty());
    }

    /**
     * Tests that task records saved to a file can be loaded back
     * as the correct task objects.
     *
     * @throws Exception if an unexpected file or parsing error occurs
     */
    @Test
    void saveThenLoad_validRecords_returnsSameTasks() throws Exception {

        Path file = tempDir.resolve("Bags.txt");

        Storage storage = new Storage(file.toString());

        List<String> records = List.of(
                "T | [ ] | Read book",
                "T | [X] | Do homework"
        );

        storage.save(records);

        Parser parser = new Parser();

        List<Task> tasks = storage.loadTasks(parser);

        assertEquals(2, tasks.size());

        assertEquals(
                "Read book",
                tasks.get(0).getDescription()
        );

        assertFalse(tasks.get(0).isDone());

        assertEquals(
                "Do homework",
                tasks.get(1).getDescription()
        );

        assertTrue(tasks.get(1).isDone());
    }
}