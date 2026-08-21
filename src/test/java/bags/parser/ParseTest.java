package bags.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import org.junit.jupiter.api.Test;

import bags.exception.BagsException;
import bags.task.Deadlines;
import bags.task.Event;
import bags.task.Task;
import bags.task.ToDo;

public class ParseTest {
    @Test
    void parseTask_validReadingFileRecords_createsCorrectTasks() throws BagsException {

        Parser parser = new Parser();

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<String> readingFileRecords = List.of(
                "T | [ ] | Read book",
                "D | [ ] | Submit assignment | 2026-08-25 23:59",
                "E | [ ] | Project meeting | 2026-08-27 14:00 | 2026-08-27 16:00"
        );

        List<Task> tasks = new ArrayList<>();

        for (String record : readingFileRecords) {
            Task task = parser.parseTask(record);
            tasks.add(task);
        }

        // ToDo task
        assertInstanceOf(ToDo.class, tasks.get(0));
        assertEquals("Read book", tasks.get(0).getDescription());
        assertFalse(tasks.get(0).isDone());

        // Deadline
        assertInstanceOf(Deadlines.class, tasks.get(1));
        assertEquals("Submit assignment", tasks.get(1).getDescription());
        assertFalse(tasks.get(1).isDone());

        String[] deadlineParts = readingFileRecords.get(1).split("\\|");
        String deadline = deadlineParts[3].trim();
        assertDoesNotThrow(() -> LocalDateTime.parse(deadline, inputFormatter));

        // Event
        assertInstanceOf(Event.class, tasks.get(2));
        assertEquals("Project meeting", tasks.get(2).getDescription());
        assertFalse(tasks.get(2).isDone());

        String[] eventParts = readingFileRecords.get(2).split("\\|");

        String from = eventParts[3].trim();
        String to = eventParts[4].trim();

        assertDoesNotThrow(() -> LocalDateTime.parse(from, inputFormatter));

        assertDoesNotThrow(() -> LocalDateTime.parse(to, inputFormatter));
    }
}
