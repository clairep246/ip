package bags.task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import bags.exception.BagsException;

class TaskListTest {

    private TaskList createTaskList() throws BagsException {
        TaskList taskList = new TaskList();

        taskList.add(new ToDo("Read book"));
        taskList.add(new ToDo("Do homework"));

        return taskList;
    }


   //Add method
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

    //Mark done method
    @Test
    void markDone_existingTask_markCorrectTaskAsDone() throws BagsException {
        TaskList taskList = createTaskList();

        Task task = taskList.markDone("done 1");

        assertTrue(task.isDone());
    }

    @Test
    void markDone_missingTaskNumber_exceptionThrown() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done")
        );

        assertTrue(exception.getMessage().contains("Missing task number"));
    }


    @Test
    void markDone_taskZero_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done 0")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    @Test
    void markDone_taskNumberThatIsMoreThanListSize_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done 3")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }


    @Test
    void markDone_nonNumbericInput_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markDone("done abc")
        );

        assertTrue(exception.getMessage().contains("Invalid task number"));
    }

    //Mark undone
    @Test
    void markUndone_addedTask_markCorrectTaskUndone() throws BagsException {
        TaskList taskList = createTaskList();

        taskList.markDone("done 1");

        Task task = taskList.markUndone("undone 1");
        List<String> readingFile = taskList.getReadingFile();

        assertFalse(task.isDone());
        assertEquals(task.parseEvent(), readingFile.get(0));

    }

    @Test
    void markUndone_missingTaskNumber_exceptionThrown() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone")
        );

        assertTrue(exception.getMessage().contains("Missing task number"));
    }


    @Test
    void markUndone_taskZero_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone 0")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }


    @Test
    void markUndone_taskNumberMoreThanListSize_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone 3")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }


    @Test
    void markUndone_nonNumericTaskNumber_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.markUndone("undone abc")
        );

        assertTrue(exception.getMessage().contains("Invalid task number"));
    }

    //Delete task 
    @Test
    void deleteMethod_deleteOne_removeCorrectTask() throws BagsException {
        TaskList taskList = createTaskList();

        Task deleted = taskList.delete("delete 1");

        assertEquals("Read book", deleted.getDescription());
        assertEquals(1, taskList.size());
        assertEquals(
                "Do homework",
                taskList.getTasks().get(0).getDescription()
        );
    }

    @Test
    void deleteMethod_missingTaskNumber_exceptionThrown() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete")
        );

        assertTrue(exception.getMessage().contains("Missing task number"));
    }


    @Test
    void deleteMethod_taskZero_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete 0")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }

    @Test
    void deleteMethod_taskNumberGreaterThanListSize_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete 3")
        );

        assertTrue(exception.getMessage().contains("Task does not exist"));
    }


    @Test
    void deleteSMethod_nonNumericTaskInput_rejectInput() throws BagsException {
        TaskList taskList = createTaskList();

        BagsException exception = assertThrows(
                BagsException.class,
                () -> taskList.delete("delete abc")
        );

        assertTrue(exception.getMessage().contains("Invalid task number"));
    }


}