package bags.ui;

import java.util.Scanner;

/**
 * Handles the chatbot's general messages and prompts shown to the user.
 */
public class Ui {

    private static final String DIVIDER
            = "____________________________________________________________";

    private static final String BANNER
            = " ____                  \n"
            + "| __ )  __ _  __ _ ___ \n"
            + "|  _ \\ / _` |/ _` / __|\n"
            + "| |_) | (_| | (_| \\__ \\\n"
            + "|____/ \\__,_|\\__, |___/\n"
            + "               |___/";

    private final Scanner scanner;

    /** Creates a user interface that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Reads one complete command entered by the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Closes the input reader when the chatbot exits. */
    public void close() {
        scanner.close();
    }

    /** Displays the chatbot greeting and its first command prompt. */
    public void showWelcome() {
        showDivider();
        System.out.println(BANNER);
        showDivider();
        System.out.println("Hello! I'm Bags. Nice to meet you!");
        showPrompt();
    }

    /** Displays the standard prompt for the next command. */
    public void showPrompt() {
        showDivider();
        System.out.println("What can I do for you?");
        showDivider();
    }

    /** Displays the chatbot's closing message. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
        showDivider();
    }

    /** Displays a divider between chatbot responses */
    public void showDivider() {
        System.out.println(DIVIDER);
    }
}
