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

import bags.exception.BagsException;
import bags.parser.Parser;
import bags.task.Task;

class StorageTest {

    @TempDir
    Path tempDir;

    // ============================================================
    // save()
    // ============================================================

    @Test
    void save_validRecords_writesRecordsToFile() throws Exception {

        Path file = tempDir.resolve("Bags.txt");
        Files.createFile(file);
        Storage storage = new Storage(file.toString());

        List<String> records = List.of(
                "T | [ ] | Read book",
                "T | [X] | Do homework"
        );

        storage.save(records);

        List<String> savedRecords = Files.readAllLines(file);

        assertEquals(records, savedRecords);
    }

    @Test
    void save_directoryAsFile_handlesIOException() throws Exception {

        Path directory = tempDir.resolve("Bags");

        Files.createDirectory(directory);

        Storage storage = new Storage(directory.toString());

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(output));

            storage.save(List.of(
                    "T | [ ] | Read book"
            ));

            assertTrue(
                    output.toString().contains(
                            "Something went wrong while saving:"
                    )
            );

        } finally {
            System.setOut(originalOut);
        }
    }

    // load task
    @Test
    void loadTasks_validRecords_returnsCorrectTasks()
            throws Exception {

        Path file = tempDir.resolve("Bags.txt");

        Files.write(file, List.of(
                "T | [ ] | Read book",
                "T | [X] | Do homework"
        ));

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


    @Test
    void loadTasks_invalidRecord_ignoresInvalidTask()
            throws Exception {

        Path file = tempDir.resolve("Bags.txt");

        Files.write(file, List.of(
                "T | [ ] | Read book",
                "X | [ ] | Invalid task",
                "T | [X] | Do homework"
        ));

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


    @Test
    void loadTasks_missingFile_returnsEmptyList()
            throws BagsException {

        Path file = tempDir.resolve("DoesNotExist.txt");

        Storage storage = new Storage(file.toString());
        Parser parser = new Parser();

        List<Task> tasks = storage.loadTasks(parser);

        assertTrue(tasks.isEmpty());
    }


    // Save and load task

    @Test
    void saveThenLoad_validRecords_returnsSameTasks()
            throws Exception {

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
