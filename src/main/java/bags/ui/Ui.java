package bags.ui; 
 
import java.util.Scanner; 
 
/** 
 * Handles the chatbot's general messages and prompts shown to the user. 
 * 
 * <p>AI was used to assist in generating this class and moving 
 * UI-related methods from the main Bags class into this Ui class. 
 * The generated code was reviewed and adapted to fit the application's 
 * requirements.</p> 
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
 
    public Ui() { 
        scanner = new Scanner(System.in); 
    } 
 
    public String readCommand() { 
        return scanner.nextLine(); 
    } 
 
    public void close() { 
        scanner.close(); 
    } 
 
    public void showWelcome() { 
        showDivider(); 
        System.out.println(BANNER); 
        showDivider(); 
        System.out.println("Hello! I'm Bags. Nice to meet you!"); 
        showPrompt(); 
    } 
 
    public void showPrompt() { 
        showDivider(); 
        System.out.println("What can I do for you?"); 
        showDivider(); 
    } 
 
    public void showGoodbye() { 
        System.out.println("Bye. Hope to see you again soon!"); 
        showDivider(); 
    } 
 
    public void showDivider() { 
        System.out.println(DIVIDER); 
    } 
}
