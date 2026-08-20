
import java.util.ArrayList;
import java.util.List;
//Main chatbot class 

public class Bags {

    // Used by command-specific interactions that will move to Ui next.
    private static final String divider
            = "____________________________________________________________";

    // Stores Task objects 
    public static ArrayList<Task> store = new ArrayList<>();

    private static final Storage storage = new Storage("./data/Bags.txt");
    private static final Parser parser = new Parser();

    // Stores the formatted strings that will be written to Bags.txt
    public static ArrayList<String> readingFile = new ArrayList<>();

    public static void main(String[] args) {

        Ui ui = new Ui();

        try {
            loadTasks();
        } catch (BagsException e) {
            System.out.println("Error!! " + e.getMessage());
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
     * Load task when chatbot is started store in reading file and parsed into
     * Task objects using parseTask helper *
     */
    private static void loadTasks() throws BagsException {

        List<String> savedTasks = storage.load();

        store.clear();
        readingFile.clear();

        for (String taskString : savedTasks) {

            Task task = parser.parseTask(taskString);

            if (task != null) {
                store.add(task);
                readingFile.add(task.parseEvent());
            }
        }

    }

    private static void saveTasks() {
        storage.save(readingFile);
    }

    private static void addTask(Ui ui) throws BagsException{

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
                Task task = null;

                if (type == Tasktype.TODO) {
                    task = createToDo(output);
                    store.add(task);

                } else if (type == Tasktype.DEADLINE) {
                    task = createDeadline(output);
                    store.add(task);

                } else if (type == Tasktype.EVENT) {
                    task = createEvent(output);
                    store.add(task);

                } else {
                    throw new BagsException("Not a valid task type, only event, to do or deadline task.");
                }

                // Keep the in-memory task list and its saved representation aligned.
                readingFile.set(readingFile.size() - 1, task.parseEvent());
                saveTasks();

                System.out.println("Got it, I've added the following task to the list: ");
                System.out.println(task.toString());
                System.out.println("Now you have " + store.size() + " tasks in your list");

            } catch (BagsException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(divider);

            output = ui.readCommand();
        }
        System.out.println("Exited editing mode");
    }

    //Create Todo task
    private static Task createToDo(String output) throws BagsException {

        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add info after the type of task");
        }

        StringBuilder name = new StringBuilder();

        for (int i = 1; i < temp.length; i++) {
            name.append(temp[i]).append(" ");
        }

        String description = name.toString().trim();

        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add info after the type of task");
        }

        ToDo Todo = new ToDo(description);
        String toBeStored = Todo.parseEvent();

        readingFile.add(toBeStored);

        return Todo;
    }

    //Create Deadline task
    private static Task createDeadline(String output) throws BagsException {

        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        int byIndex = -1;

        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("/by")) {
                byIndex = i;
                break;
            }
        }

        if (byIndex == -1) {
            throw new BagsException("Missing /by. Please add in /by <end date>");
        }

        StringBuilder name = new StringBuilder();

        for (int i = 1; i < byIndex; i++) {
            name.append(temp[i]).append(" ");
        }

        String description = name.toString().trim();

        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add some info after task type");
        }

        StringBuilder deadline = new StringBuilder();

        for (int i = byIndex + 1; i < temp.length; i++) {
            deadline.append(temp[i]).append(" ");
        }

        String deadlineInfo = deadline.toString().trim();

        if (deadlineInfo.isEmpty()) {
            throw new BagsException("Missing deadline after /by! Add /by <deadline> after task name");
        }

        Deadlines newDeadline
                = new Deadlines(description, deadlineInfo);

        String toBeStored = newDeadline.parseEvent();
        readingFile.add(toBeStored);

        return newDeadline;
    }

    //Create Event task 
    private static Task createEvent(String output) throws BagsException {

        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task description! Add task info after task type");
        }

        int fromIndex = -1;
        int toIndex = -1;

        for (int i = 0; i < temp.length; i++) {

            if (temp[i].equals("/from")) {
                fromIndex = i;
            }

            if (temp[i].equals("/to")) {
                toIndex = i;
                break;
            }
        }

        if (fromIndex == -1
                || toIndex == -1
                || toIndex < fromIndex) {

            throw new BagsException("Missing /from or /to! Add /from <start> /to <end> after task name");
        }

        StringBuilder name = new StringBuilder();

        for (int i = 1; i < fromIndex; i++) {
            name.append(temp[i]).append(" ");
        }

        String description = name.toString().trim();

        if (description.isEmpty()) {
            throw new BagsException("Missing task description! Add task info after task type");
        }

        StringBuilder fromInfo = new StringBuilder();

        for (int i = fromIndex + 1; i < toIndex; i++) {
            fromInfo.append(temp[i]).append(" ");
        }

        StringBuilder toInfo = new StringBuilder();

        for (int i = toIndex + 1; i < temp.length; i++) {
            toInfo.append(temp[i]).append(" ");
        }

        String fromStr = fromInfo.toString().trim();
        String toStr = toInfo.toString().trim();

        if (fromStr.isEmpty() || toStr.isEmpty()) {
            throw new BagsException("Missing timeframe after /from or /to! Maybe you forgot the dates");
        }

        Event newEvent = new Event(description, fromStr, toStr);

        String toBeStored = newEvent.parseEvent();

        readingFile.add(toBeStored);

        return newEvent;
    }

    //Displays all tasks currently stored in the task list
    private static void listItems() throws BagsException {

        if (store.isEmpty()) {
            throw new BagsException("Your list empty. Please add some tasks!");

        } else {
            System.out.println("Here are the tasks in your list:");

            for (int i = 0; i < store.size(); i++) {
                System.out.println((i + 1) + "." + store.get(i).toString());
            }
        }
    }

    //Marks the selected task as completed. 
    private static void markDone(String output) throws BagsException {
        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task number. Add a number from 1 to " + store.size());
        }

        try {
            int taskNumber = Integer.parseInt(temp[1]);
            if (taskNumber <= 0 || taskNumber > store.size()) {
                throw new BagsException("Task does not exist. Please only input number 1 to " + store.size()
                );
            }
            Task task = store.get(taskNumber - 1);
            task.markDone();

            readingFile.set(taskNumber - 1, task.parseEvent());

            saveTasks();

            System.out.println("Ok! I've marked this task as done: ");
            System.out.println(task.toString());

        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number! Please enter a valid number from 1 to " + store.size()
            );
        }
    }

    //Marks the selected task as undone. 
    private static void unMarkDone(String output)
            throws BagsException {

        String[] temp = output.split(" ");

        if (temp.length < 2) {
            throw new BagsException("Missing task number. Enter value from 1 to " + store.size()
            );
        }

        try {
            int taskNumber = Integer.parseInt(temp[1]);
            if (taskNumber <= 0 || taskNumber > store.size()) {
                throw new BagsException("Task does not exist. Enter value from 1 to " + store.size()
                );
            }

            Task task = store.get(taskNumber - 1);
            task.markUndone();

            readingFile.set(taskNumber - 1, task.parseEvent());

            saveTasks();

            System.out.println("Alright! I've marked this task as undone: ");
            System.out.println(task.toString());

        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number. Please enter a valid number from 1 to " + store.size()
            );
        }
    }

    /**
     * Deletes the selected task. Since both store and readingFile indexes the
     * task at the same index, removing both will remove the same task
     *
     */
    private static void deleteTask(String output) throws BagsException {
        String[] temp = output.split(" ");
        if (temp.length < 2) {
            throw new BagsException("Missing task number. Enter value from 1 to " + store.size());
        }

        try {
            int taskNumber = Integer.parseInt(temp[1]);
            if (taskNumber <= 0 || taskNumber > store.size()) {
                throw new BagsException("Task does not exist. Enter value from 1 to " + store.size());
            }

            Task task = store.get(taskNumber - 1);
            //Remove both from array and reading file  
            store.remove(taskNumber - 1);

            readingFile.remove(taskNumber - 1);
            saveTasks();

            System.out.println("Got it! I've deleted the following task: ");
            System.out.println(task.toString());
            System.out.println("You now have " + store.size() + " in your task list."
            );

        } catch (NumberFormatException e) {
            throw new BagsException("Invalid task number. Please enter a valid number from 1 to " + store.size()
            );
        }
    }

    //Echo the users inputs 
    private static void echoWords(Ui ui)
            throws BagsException {

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
