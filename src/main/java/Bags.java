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

        // Printing
        System.out.println(divider);
        System.out.println(banner);
        System.out.println("Hello! I'm Bags. Nice to meet you!");
        System.out.println("What can I do for you?");

        String output = scanner.nextLine();

        if (output.equals("echo")) {
            System.out.println("From now on I will echo your input, please enter your input. To exit enter bye");

            String echo = scanner.nextLine();

            while (!echo.equals("bye")) {
                System.out.println(echo);
                echo = scanner.nextLine();
            }
            System.out.println(divider);
            System.out.println("Bye. Hope to see you again soon!");

        } else if (output.equals("bye")) {
            System.out.println(divider);
            System.out.println("Bye. Hope to see you again soon!");

        } else {
            System.out.println("I don't know that command.");
            System.out.println(divider);
            System.out.println("Bye. Hope to see you again soon!");
        }

        scanner.close();
    }
}
