package bags.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import bags.exception.BagsException;

/**
 * Tests the {@link Deadlines} class.
 */
class DeadlinesTest {

    @Test
    void constructor_validDeadline_parsesDateAndFormatsTask() throws BagsException {
        Deadlines deadline = new Deadlines("Submit report", "2026-09-12 23:59");

        assertEquals(LocalDateTime.of(2026, 9, 12, 23, 59), deadline.getDeadline());
        assertEquals("[D][ ] Submit report (by: 12/09/2026 11:59pm)", deadline.toString());
        assertEquals("D | [ ] | Submit report | 2026-09-12 23:59", deadline.parseEvent());
    }

    @Test
    void createTask_validCommand_createsDeadline() throws BagsException {
        Deadlines deadline = Deadlines.createTask("deadline Submit report /by 2026-09-12 23:59");

        assertEquals("Submit report", deadline.getDescription());
        assertEquals(LocalDateTime.of(2026, 9, 12, 23, 59), deadline.getDeadline());
    }

    @Test
    void createTask_missingDescription_throwsException() {
        BagsException exception = assertThrows(
                BagsException.class, () -> Deadlines.createTask("deadline /by 2026-09-12 23:59")
        );

        assertTrue(exception.getMessage().contains("Missing task description"));
    }

    @Test
    void createTask_missingByKeyword_throwsException() {
        BagsException exception = assertThrows(
                BagsException.class, () -> Deadlines.createTask("deadline Submit report")
        );

        assertTrue(exception.getMessage().contains("Missing /by"));
    }

    @Test
    void constructor_invalidDeadline_throwsException() {
        BagsException exception = assertThrows(
                BagsException.class, () -> new Deadlines("Submit report", "not-a-date")
        );

        assertTrue(exception.getMessage().contains("correct format"));
    }
}
