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

        String[] store = new String[100];
        int taskCount = 0;

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

            if (output.equals("echo")) {
                System.out.println("From now on I will echo your input, please enter your input. To exit enter done");
                System.out.println(divider);

                String echo = scanner.nextLine();

                while (!echo.equals("done")) {
                    System.out.println(echo);
                    System.out.println(divider);

                    echo = scanner.nextLine();
                }

                System.out.println("Exited echo mode.");
                System.out.println(divider);

            } else if (output.equals("add task")) {

                System.out.println("Please enter your task. To exit editing mode, enter done");
                System.out.println(divider);

                String task = scanner.nextLine();

                while (!task.equals("done")) {

                    if (taskCount < store.length) {
                        store[taskCount] = task;
                        taskCount += 1;

                        System.out.println("Task added!");
                        System.out.println(divider);
                        task = scanner.nextLine();

                    } else {
                        System.out.println("Your task list is full!");
                        System.out.println(divider);
                        break;
                    }
                }

                System.out.println("Exited editing mode.");
                System.out.println(divider);

            } else if (output.equals("list")) {

                if (taskCount == 0) {
                    System.out.println("Your task list is empty.");
                    System.out.println(divider);

                } else {
                    System.out.println("Here are your tasks:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + ". " + store[i] + "\n");
                    }
                    System.out.println(divider);
                }
            } else {
                System.out.println("I don't know that command.");
                System.out.println(divider);
            }

            // Ask for another command
            System.out.println("What can I do for you?");
            System.out.println(divider);

            output = scanner.nextLine();
        }

        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(divider);

        scanner.close();
    }
}