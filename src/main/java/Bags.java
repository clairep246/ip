import java.util.ArrayList;
import java.util.Scanner;

public class Bags {
    private static final String divider =
            "____________________________________________________________";

    private static final String banner =
            " ____                  \n"
                    + "| __ )  __ _  __ _ ___ \n"
                    + "|  _ \\ / _` |/ _` / __|\n"
                    + "| |_) | (_| | (_| \\__ \\\n"
                    + "|____/ \\__,_|\\__, |___/\n"
                    + "               |___/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> store = new ArrayList<>();

        System.out.println(divider);
        System.out.println(banner);
        System.out.println(divider);
        System.out.println("Hello! I'm Bags. Nice to meet you!");
        System.out.println(divider);
        System.out.println("What can I do for you?");
        System.out.println(divider);

        String output = scanner.nextLine();
        System.out.println(divider);

        while (!output.equals("bye")) {
            try {
                if (output.trim().isEmpty()) {
                    throw new BagsException("Oops no command was entered. Please enter a command.");
                }

                if (output.equals("add task")) {
                    System.out.println("Enter your task. Specify type of task in front, add /by for deadline task, add /from and /to for event task. To exit enter exit.");
                    System.out.println(divider);

                    output = scanner.nextLine();

                    while (!output.equals("exit")) {
                        try {
                            String[] temp = output.split(" ");

                            // TODO
                            if (output.startsWith("todo")) {
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

                                ToDo newTodo = new ToDo(description);
                                store.add(newTodo);

                                System.out.println("Got it. I've added this task:");
                                System.out.println(newTodo.toString());
                                System.out.println("Now you have " + store.size() + " tasks in the list.");

                                // DEADLINE
                            } else if (output.startsWith("deadline")) {
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

                                StringBuilder deadline = new StringBuilder();
                                for (int i = byIndex + 1; i < temp.length; i++) {
                                    deadline.append(temp[i]).append(" ");
                                }

                                String description = name.toString().trim();
                                String deadlineInfo = deadline.toString().trim();

                                if (description.isEmpty()) {
                                    throw new BagsException("Missing task description! Add some info after task type");
                                }
                                if (deadlineInfo.isEmpty()) {
                                    throw new BagsException("Missing deadline after /by! Add /by <deadline> after task name");
                                }

                                Deadlines newDeadline = new Deadlines(description, deadlineInfo);
                                store.add(newDeadline);

                                System.out.println("Got it. I've added this task:");
                                System.out.println(newDeadline.toString());
                                System.out.println("Now you have " + store.size() + " tasks in the list.");

                                // EVENT
                            } else if (output.startsWith("event")) {
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

                                if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                                    throw new BagsException("Missing /from or /to! Add /from <start> /to <end> after task name");
                                }

                                StringBuilder name = new StringBuilder();
                                for (int i = 1; i < fromIndex; i++) {
                                    name.append(temp[i]).append(" ");
                                }

                                StringBuilder fromInfo = new StringBuilder();
                                for (int i = fromIndex + 1; i < toIndex; i++) {
                                    fromInfo.append(temp[i]).append(" ");
                                }

                                StringBuilder toInfo = new StringBuilder();
                                for (int i = toIndex + 1; i < temp.length; i++) {
                                    toInfo.append(temp[i]).append(" ");
                                }

                                String description = name.toString().trim();
                                String fromStr = fromInfo.toString().trim();
                                String toStr = toInfo.toString().trim();

                                if (description.isEmpty()) {
                                    throw new BagsException("Missing task description! Add task info after task type");
                                }
                                if (fromStr.isEmpty() || toStr.isEmpty()) {
                                    throw new BagsException("Missing timeframe after /from or /to! Maybe you forgot the dates");
                                }

                                Event newEvent = new Event(description, fromStr, toStr);
                                store.add(newEvent);

                                System.out.println("Got it. I've added this task:");
                                System.out.println(newEvent.toString());
                                System.out.println("Now you have " + store.size() + " tasks in the list.");

                            } else {
                                throw new BagsException("Not a valid task type, only event, to do or deadline task.");
                            }

                        } catch (BagsException e) {
                            System.out.println(e.getMessage());
                        }

                        System.out.println(divider);
                        output = scanner.nextLine();
                    }
                    System.out.println("Exited editing mode");

                } else if (output.equals("list")) {
                    if (store.isEmpty()) {
                        throw new BagsException("Your list empty. Please add some tasks!");
                    } else {
                        System.out.println("Here are the tasks in your list:");
                        for (int i = 0; i < store.size(); i++) {
                            System.out.println((i + 1) + "." + store.get(i).toString());
                        }
                    }

                    // MARK
                } else if (output.startsWith("mark")) {
                    String[] temp = output.split(" ");

                    if (temp.length < 2) {
                        throw new BagsException("Missing task number. Add a number from 1 to " + store.size());
                    }

                    try {
                        int taskNumber = Integer.parseInt(temp[1]);
                        if (taskNumber <= 0 || taskNumber > store.size()) {
                            throw new BagsException("Task does not exist. Please only input number 1 to " + store.size());
                        }

                        Task task = store.get(taskNumber - 1);
                        task.markDone();
                        System.out.println("Ok! I've marked this task as done: ");
                        System.out.println(task.toString());
                    } catch (NumberFormatException e) {
                        throw new BagsException("Invalid task number! Please enter a valid number from 1 to " + store.size());
                    }

                    // UNMARK
                } else if (output.startsWith("unmark")) {
                    String[] temp = output.split(" ");

                    if (temp.length < 2) {
                        throw new BagsException("Missing task number. Enter value from 1 to " + store.size());
                    }

                    try {
                        int taskNumber = Integer.parseInt(temp[1]);
                        if (taskNumber <= 0 || taskNumber > store.size()) {
                            throw new BagsException("Task does not exist.Enter value from 1 to " + store.size());
                        }

                        Task task = store.get(taskNumber - 1);
                        task.markUndone();
                        System.out.println("Alright! I've marked this task as undone: ");
                        System.out.println(task.toString());
                    } catch (NumberFormatException e) {
                        throw new BagsException("Invalid task number. Please enter a valid number from 1 to " + store.size());
                    }

                    // ECHO
                } else if (output.equals("echo")) {
                    System.out.println("From now on I will echo your input. To exit enter exit.");
                    System.out.println(divider);

                    String echo = scanner.nextLine();

                    while (!echo.equals("exit")) {
                        if (echo.isEmpty()) {
                            throw new BagsException("I can't echo silence :( Did you miss a command?");
                        }
                        System.out.println(echo);
                        System.out.println(divider);

                        echo = scanner.nextLine();
                    }

                    System.out.println("Exited echo mode.");

                    // UNKNOWN COMMAND
                }
                else {
                    throw new BagsException("The command does not exist. Please try again :(");
                }

            } catch (BagsException e) {
                System.out.println("Error!! " + e.getMessage());
            }

            System.out.println(divider);
            System.out.println("What can I do for you?");
            System.out.println(divider);

            output = scanner.nextLine();
        }

        // End of loop goodbye
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(divider);

        scanner.close();
    }
}