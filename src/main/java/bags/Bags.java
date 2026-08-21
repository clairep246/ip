package bags; 
 
import bags.exception.BagsException;
import bags.parser.Parser;
import bags.storage.Storage;
import bags.task.Deadlines;
import bags.task.Event;
import bags.task.Task;
import bags.task.TaskList;
import bags.task.Tasktype;
import bags.task.ToDo; 
import bags.ui.Ui; 
 
/** 
 * Main chatbot class for the Bags task management application. 
 * 
 * <p>This class coordinates the user interface, command parsing, 
 * task management, and storage components.</p> 
 
 */ 
public class Bags { 
 
    private static final String divider 
            = "____________________________________________________________"; 
 
    private static final Storage storage = new Storage("./data/Bags.txt"); 
    private static final Parser parser = new Parser(); 
    private static TaskList tasks; 
 
    /** 
     * Starts the Bags application and handles the main command loop. 
     * 
     * @param args command-line arguments provided when starting the program 
     */ 
    public static void main(String[] args) { 
 
        Ui ui = new Ui(); 
 
        try { 
            tasks = new TaskList(storage.loadTasks(parser)); 
        } catch (BagsException e) { 
            System.out.println("Error!! " + e.getMessage()); 
            tasks = new TaskList(); 
        } 
 
        ui.showWelcome(); 
 
        String output = ui.readCommand(); 
        ui.showDivider(); 
 
        Parser.Command command = parser.parseCommand(output); 
        while (command != Parser.Command.BYE) { 
 
            try { 
                if (command == Parser.Command.EMPTY) { 
                    throw new BagsException("No command was entered. Please enter a command."); 
                } 
 
                if (command == Parser.Command.ADD_TASK) { 
                    addTask(ui); 
 
                } else if (command == Parser.Command.LIST) { 
                    listItems(); 
 
                } else if (command == Parser.Command.MARK) { 
                    markDone(output); 
 
                } else if (command == Parser.Command.UNMARK) { 
                    unMarkDone(output); 
 
                } else if (command == Parser.Command.ECHO) { 
                    echoWords(ui); 
 
                } else if (command == Parser.Command.DELETE) { 
                    deleteTask(output); 
 
                } else { 
                    throw new BagsException("The command does not exist. Please try again :("); 
                } 
 
            } catch (BagsException e) { 
                System.out.println("Error!! " + e.getMessage()); 
            } 
 
            ui.showPrompt(); 
 
            output = ui.readCommand(); 
            command = parser.parseCommand(output); 
        } 
 
        saveTasks(); 
 
        ui.showGoodbye(); 
 
        ui.close(); 
    } 
 
    /** 
     * Saves the current tasks to the storage file. 
     */ 
    private static void saveTasks() { 
        storage.save(tasks.toSaveRecords()); 
    } 
 
     /**
     * Enters task creation mode and allows the user to add multiple tasks.
     *
     * <p>The user can create ToDo, Deadline, and Event tasks by entering
     * commands in the specified format. The user can enter {@code exit}
     * to leave task creation mode.</p>
     *
     * @param ui the user interface used to receive commands from the user
     */
    private static void addTask(Ui ui) { 
        System.out.println(""" 
                           Enter your task.  
                           Format for each task type, follow the format closely:  
                            1. todo <task name> 
                            2. deadline <name> /by <year-month-day> <hour:minutes> 
                            3. event <name> /from <year-month-day> <hour:minutes> <name> /to <year-month-day> <hour:minutes> 
                           To exit enter exit."""); 
        System.out.println(divider); 
 
        String output = ui.readCommand(); 
        while (!output.equals("exit")) { 
            try { 
                Tasktype type = parser.parseTaskType(output); 
                Task task; 
                if (type == Tasktype.TODO) { 
                    task = ToDo.fromCommand(output); 
                } else if (type == Tasktype.DEADLINE) { 
                    task = Deadlines.fromCommand(output); 
                } else if (type == Tasktype.EVENT) { 
                    task = Event.fromCommand(output); 
                } else { 
                    throw new BagsException("Not a valid task type, only event, to do or deadline task."); 
                } 
                tasks.add(task); 
                saveTasks(); 
                System.out.println("Got it, I've added the following task to the list: "); 
                System.out.println(task); 
                System.out.println("Now you have " + tasks.size() + " tasks in your list"); 
            } catch (BagsException e) { 
                System.out.println(e.getMessage()); 
            } 
            System.out.println(divider); 
            output = ui.readCommand(); 
        } 
        System.out.println("Exited editing mode"); 
    } 
 
    /** 
     * Displays all tasks currently stored in the task list. 
     * 
     * @throws BagsException if the task list is empty 
     */ 
    private static void listItems() throws BagsException { 
 
        if (tasks.isEmpty()) { 
            throw new BagsException("Your list empty. Please add some tasks!"); 
 
        } else { 
            System.out.println(tasks); 
        } 
    } 
 
    /** 
     * Marks the selected task as completed and saves the updated task list. 
     * 
     * @param output the user's mark command containing the task number 
     * @throws BagsException if the task number is invalid or does not exist 
     */ 
    private static void markDone(String output) throws BagsException { 
        Task task = tasks.markDone(output); 
        saveTasks(); 
        System.out.println("Ok! I've marked this task as done: "); 
        System.out.println(task.toString()); 
    } 
 
    /** 
     * Marks the selected task as undone and saves the updated task list. 
     * 
     * @param output the user's unmark command containing the task number 
     * @throws BagsException if the task number is invalid or does not exist 
     */ 
    private static void unMarkDone(String output) 
            throws BagsException { 
 
        Task task = tasks.markUndone(output); 
        saveTasks(); 
        System.out.println("Alright! I've marked this task as undone: "); 
        System.out.println(task.toString()); 
    } 
 
    /** 
     * Deletes the selected task and saves the updated task list. Since both 
     * store and readingFile indexes the task at the same index, removing both 
     * will remove the same task. 
     * 
     * @param output the user's delete command containing the task number 
     * @throws BagsException if the task number is invalid or does not exist 
     */ 
    private static void deleteTask(String output) throws BagsException { 
        Task task = tasks.delete(output); 
        saveTasks(); 
        System.out.println("Got it! I've deleted the following task: "); 
        System.out.println(task.toString()); 
        System.out.println("You now have " + tasks.size() + " in your task list."); 
    } 
 
    /** 
     * Echoes the user's input until the user enters exit. 
     * 
     * @param ui the user interface used to read commands from the user 
     * @throws BagsException if the user enters an empty input 
     */ 
    private static void echoWords(Ui ui) throws BagsException { 
 
        System.out.println("From now on I will echo your input. To exit enter exit."); 
 
        System.out.println(divider); 
 
        String echo = ui.readCommand(); 
 
        while (!echo.equals("exit")) { 
 
            if (echo.isEmpty()) { 
                throw new BagsException("I can't echo silence. Did you miss a command?"); 
            } 
 
            System.out.println(echo); 
            System.out.println(divider); 
 
            echo = ui.readCommand(); 
        } 
 
        System.out.println("Exited echo mode."); 
    } 
}
