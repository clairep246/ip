
/**
 * Starts the Bags chatbot and displays its initial greeting.
 */
public class Bags {
    /** Separates sections of the chatbot's output. */
    private static final String divider = "____________________________________________________________";

    /** The banner displayed when the chatbot starts. */
    private static final String banner = " ____                  \n"
            + "| __ )  __ _  __ _ ___ \n"
            + "|  _ \\ / _` |/ _` / __|\n"
            + "| |_) | (_| | (_| \\__ \\\n"
            + "|____/ \\__,_|\\__, |___/\n"
            + "               |___/";

    public static void main(String[] args) {
        System.out.println(divider);
        System.out.println(banner);
        System.out.println("Hello! I'm Bags. Nice to meet you!");
        System.out.println("What can I do for you?");
        System.out.println(divider);
        System.out.println("Bye. Hope to see you again soon!");
    }
}
