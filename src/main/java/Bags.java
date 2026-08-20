
//Main chatbot class 

public class Bags {

    // Used by command-specific interactions that will move to Ui next.
    private static final String divider
            = "____________________________________________________________";

    private static final Storage storage = new Storage("./data/Bags.txt");
    private static final Parser parser = new Parser();
    private static TaskList tasks;

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

    private static void saveTasks() {
        storage.save(tasks.toSaveRecords());
    }

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

    //Displays all tasks currently stored in the task list
    private static void listItems() throws BagsException {

        if (tasks.isEmpty()) {
            throw new BagsException("Your list empty. Please add some tasks!");

        } else {
            System.out.println(tasks);
        }
    }

    //Marks the selected task as completed. 
    private static void markDone(String output) throws BagsException {
        Task task = tasks.markDone(output);
        saveTasks();
        System.out.println("Ok! I've marked this task as done: ");
        System.out.println(task.toString());
    }

    //Marks the selected task as undone. 
    private static void unMarkDone(String output)
            throws BagsException {

        Task task = tasks.markUndone(output);
        saveTasks();
        System.out.println("Alright! I've marked this task as undone: ");
        System.out.println(task.toString());
    }

    /**
     * Deletes the selected task. Since both store and readingFile indexes the
     * task at the same index, removing both will remove the same task
     *
     */
    private static void deleteTask(String output) throws BagsException {
        Task task = tasks.delete(output);
        saveTasks();
        System.out.println("Got it! I've deleted the following task: ");
        System.out.println(task.toString());
        System.out.println("You now have " + tasks.size() + " in your task list.");
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
