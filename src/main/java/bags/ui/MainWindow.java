package bags.ui;

import bags.Bags;
import bags.exception.BagsException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controls the main JavaFX window of the Bags application.
 */
public class MainWindow {

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    private Bags bags;

    /**
     * Initialises the Bags application used by the JavaFX interface.
     */
    @FXML
    public void initialize() {
        bags = new Bags();

        addMessage("Hello! I'm Bags. Nice to meet you!");
        addMessage("What can I do for you?");
    }

    /**
     * Handles commands entered through the text field or send button.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            addMessage("Please enter a command.");
            return;
        }

        addMessage("You: " + input);

        try {
            String response = bags.processCommand(input);
            addMessage(response);

            if (input.equals("bye")) {
                sendButton.setDisable(true);
                userInput.setDisable(true);
            }

        } catch (BagsException e) {
            addMessage("Error!! " + e.getMessage());
        }

        userInput.clear();
        scrollToBottom();
    }

    /**
     * Adds a message to the conversation window.
     *
     * @param message the message to display
     */
    private void addMessage(String message) {
        javafx.scene.control.Label label =
                new javafx.scene.control.Label(message);

        label.setWrapText(true);
        label.setMaxWidth(360);

        dialogContainer.getChildren().add(label);
    }

    /**
     * Scrolls the conversation window to the latest message.
     */
    private void scrollToBottom() {
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }
}