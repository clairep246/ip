package bags.task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import bags.exception.BagsException;

/**
 * Tests the {@link TaskList} class for task related methods.
 *
 * <p>These tests verify that tasks are correctly added to the task list,
 * marked as done or undone, deleted, and that invalid task numbers or
 * missing task numbers are handled correctly.</p>
 *
 */
class TaskListTest {

    /**
     * Creates a task list containing two sample ToDo tasks for use
     * in the test cases.
     *
     * @return a task list containing two ToDo tasks
     * @throws BagsException if an error occurs while creating a task
     */
    private TaskList createTaskList() throws BagsException {
        TaskList taskList = new TaskList();

        taskList.add(new ToDo("Read book"));
        taskList.add(new ToDo("Do homework"));

        return taskList;
    }

    /**
     * Tests that a task can be successfully added to an empty task list
     * and that the corresponding parsed task record is added to the readingFile record.
     *
     * @throws BagsException if an error occurs while adding the task
     */
    @Test
    void addMethod_emptyList_successfullyAddTask() throws BagsException {
        TaskList taskList = new TaskList();

        assertEquals(0, taskList.size());

        ToDo todo = new ToDo("Read book");
        taskList.add(todo);

        List<Task> tasks = taskList.getTasks();
        List<String> readingFile = taskList.getReadingFile();

        assertEquals(1, taskList.size());
        assertEquals(todo, tasks.get(0));
        assertEquals(todo.parseEvent(), readingFile.get(0));
    }
    
    /**
     * Tests that an existing task can be successfully marked as done.
     *
     * @throws BagsException if an error occurs while marking the task
     */
    @Test
    void markDone_existingTask_markCorrectTaskAsDone()
            throws BagsException {
        TaskList taskList = createTaskList();

        Task task = taskList.markDone("done 1");

        assertTrue(task.isDone());
    }

    /**
     * Tests that attempting to mark a task as done without providing
     * a task number throws an appropriate exception.
     *
     * @throws BagsException if an unexpected application error occurs
     */
    @Test
    void markDone_missingTaskNumber_exceptionThrown()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done")
        );

        assertTrue(exception.getMessage().contains("Missing task number"));
    }

    /**
     * Tests that task number zero is rejected when marking a task as done.
     *
     * @throws BagsException if an unexpected application error occurs
     */
    @Test
    void markDone_taskZero_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done 0")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    /**
     * Tests that a task number greater than the task list size is rejected
     * when marking a task as done.
     *
     * @throws BagsException if an unexpected application error occurs
     */
    @Test
    void markDone_taskNumberThatIsMoreThanListSize_rejectInput()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done 3")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    /**
     * Tests that a non-numeric task number is rejected when marking
     * a task as done.
     *
     * @throws BagsException if an unexpected application error occurs
     */
    @Test
    void markDone_nonNumbericInput_rejectInput()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done abc")
        );

        assertTrue(exception.getMessage().contains("Invalid task number"));
    }

 
    /**
     * Tests that a completed task can be successfully marked as undone
     * and that the corresponding reading file record is updated.
     *
     * @throws BagsException if an error occurs while updating the task
     */
    @Test
    void markUndone_addedTask_markCorrectTaskUndone()
            throws BagsException {
        TaskList taskList = createTaskList();

        taskList.markDone("done 1");

        Task task = taskList.markUndone("undone 1");
        List<String> readingFile = taskList.getReadingFile();

        assertFalse(task.isDone());
        assertEquals(task.parseEvent(), readingFile.get(0));
    }

    /**
     * Tests that attempting to mark a task as undone without providing
     * a task number throws an appropriate exception.
     *
     * @throws BagsException if an error occurs
     */
    @Test
    void markUndone_missingTaskNumber_exceptionThrown()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone")
        );

        assertTrue(exception.getMessage().contains("Missing task number"));
    }

    /**
     * Tests that task number zero is rejected when marking a task as undone.
     *
     * @throws BagsException if an error occurs
     */
    @Test
    void markUndone_taskZero_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone 0")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    /**
     * Tests that a task number greater than the task list size is rejected
     * when marking a task as undone.
     *
     * @throws BagsException if an error occurs.
     */
    @Test
    void markUndone_taskNumberMoreThanListSize_rejectInput()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone 3")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    /**
     * Tests that a non-numeric task number is rejected when marking
     * a task as undone.
     *
     * @throws BagsException if an error occurs
     */
    @Test
    void markUndone_nonNumericTaskNumber_rejectInput()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone abc")
        );

        assertTrue(exception.getMessage().contains("Invalid task number"));
    }


    /**
     * Tests that an existing task can be deleted and that the correct
     * task is removed from the task list.
     *
     * @throws BagsException if an error 
     */
    @Test
    void deleteMethod_deleteOne_removeCorrectTask()
            throws BagsException {
        TaskList taskList = createTaskList();

        Task deleted = taskList.delete("delete 1");

        assertEquals("Read book", deleted.getDescription());
        assertEquals(1, taskList.size());
        assertEquals(
                "Do homework",
                taskList.getTasks().get(0).getDescription()
        );
    }

    /**
     * Tests that attempting to delete a task without providing
     * a task number throws an exception.
     *
     * @throws BagsException if an error occurs
     */
    @Test
    void deleteMethod_missingTaskNumber_exceptionThrown()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete")
        );

        assertTrue(exception.getMessage().contains("Missing task number"));
    }

    /**
     * Tests that task number zero is rejected when deleting a task.
     *
     * @throws BagsException if an unexpected application error occurs
     */
    @Test
    void deleteMethod_taskZero_rejectInput()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete 0")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    /**
     * Tests that a task number greater than the task list size is rejected
     * when deleting a task.
     *
     * @throws BagsException if an unexpected application error occurs
     */
    @Test
    void deleteMethod_taskNumberGreaterThanListSize_rejectInput()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete 3")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    /**
     * Tests that a non-numeric task number is rejected when deleting
     * a task.
     *
     * @throws BagsException if an error occurs
     */
    @Test
    void deleteSMethod_nonNumericTaskInput_rejectInput()
            throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete abc")
        );

        assertTrue(exception.getMessage().contains("Invalid task number"));
    }
}