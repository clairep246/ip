import java.util.ArrayList;
import java.util.Scanner;

public class Bags {
    private static final String divider = "____________________________________________________________";
    private static final String banner = " ____                  \n"
            + "| __ )  __ _  __ _ ___ \n"
            + "|  _ \\ / _` |/ _` / __|\n"
            + "| |_) | (_| | (_| \\__ \\\n"
            + "|____/ \\__,_|\\__, |___/\n"
            + "               |___/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> store = new ArrayList<>();

        // Printing
        System.out.println(divider);
        System.out.println(banner);
        System.out.println(divider);
        System.out.println("Hello! I'm Bags. Nice to meet you!");
        System.out.println(divider);
        System.out.println("What can I do for you?");
        System.out.println(divider);

        String output = scanner.nextLine();

        while (!output.equals("bye")) {

            // Echo
            if (output.equals("echo")) {
                System.out.println("From now on I will echo your input, please enter your input. To exit enter exit");
                System.out.println(divider);

                String echo = scanner.nextLine();

                while (!echo.equals("exit")) {
                    System.out.println(echo);
                    System.out.println(divider);

                    echo = scanner.nextLine();
                }

                System.out.println("Exited echo mode.");
                System.out.println(divider);

                // Add task
            } else if (output.equals("add task")) {
                System.out.println("Please enter your task. To exit editing mode, enter exit");
                System.out.println(divider);

                String taskName = scanner.nextLine();

                while (!taskName.equals("exit")) {
                    Task newTask = new Task(taskName);
                    store.add(newTask);

                    System.out.println("Task added!");
                    System.out.println(divider);

                    taskName = scanner.nextLine();
                }

                System.out.println("Exited editing mode.");
                System.out.println(divider);

                // List tasks
            } else if (output.equals("list")) {
                if (store.isEmpty()) {
                    System.out.println("Your task list is empty.");
                    System.out.println(divider);
                } else {
                    System.out.println("Here are your tasks:");
                    for (int i = 0; i < store.size(); i++) {
                        System.out.println("[" + store.get(i).getStatusIcon() + "] " + store.get(i).getName());
                    }
                    System.out.println(divider);
                }

                // Mark task
            } else if (output.startsWith("mark")) {

                String[] temp = output.split(" ");

                if (temp.length == 1) {
                    System.out.println("Missing task number");
                    System.out.println(divider);
                } else {
                    int taskNumber = Integer.parseInt(temp[1]);
                    if (taskNumber > store.size() || taskNumber <= 0) {
                        System.out.println("Task does not exist");
                        System.out.println(divider);
                    } else {
                        store.get(taskNumber - 1).markDone();
                        System.out.println("Marked task " + taskNumber + " as done.");
                        System.out.println(divider);
                    }
                }
                // Unmark task
            } else if (output.startsWith("unmark")) {
                String[] temp = output.split(" ");
                if (temp.length == 1) {
                    System.out.println("Missing task number");
                    System.out.println(divider);
                } else {
                    int taskNumber = Integer.parseInt(temp[1]);
                    if (taskNumber > store.size() || taskNumber <= 0) {
                        System.out.println("Task does not exist");
                        System.out.println(divider);
                    } else {
                        store.get(taskNumber - 1).markUndone();
                        System.out.println("Marked task " + taskNumber + " as undone.");
                        System.out.println(divider);
                    }
                }
                // Unknown command
            } else {
                System.out.println("The command does not exist");
                System.out.println(divider);
            }
            // New command
            System.out.println("What can I do for you?");
            System.out.println(divider);

            output = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(divider);

        scanner.close();
    }
}