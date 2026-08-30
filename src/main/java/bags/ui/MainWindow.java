package bags.ui;

import bags.Bags;
import bags.exception.BagsException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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

    private Image bagsImage;
    private Image userImage;

    /**
     * Initialises the Bags application used by the JavaFX interface.
     */
    @FXML
    public void initialize() {
        bags = new Bags();
        addBagsMessage("Hello! I'm Bags. Nice to meet you!");
        addBagsMessage("What can I do for you?");
    }

    /**
     * Handles commands entered through the text field or send button.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            addBagsMessage("Please enter a command.");
            return;
        }

        addUserMessage(input);

        try {
            String response = bags.processCommand(input);
            addBagsMessage(response);

            if (input.equals("bye")) {
                sendButton.setDisable(true);
                userInput.setDisable(true);
            }

        } catch (BagsException e) {
            addBagsMessage("Error!! " + e.getMessage());
        }

        userInput.clear();
        scrollToBottom();
    }

    /**
     * Adds a message to the conversation window.
     *
     * @param message the message to display
     */
    private void addBagsMessage(String message) {
        Image bagsImage = new Image(
                MainWindow.class.getResourceAsStream("/images/Response.png"));

        DialogBox dialogBox = DialogBox.getBagsDialog(message, bagsImage);

        dialogContainer.getChildren().add(dialogBox);
    }

    private void addUserMessage(String message) {
        Image userImage = new Image(MainWindow.class.getResourceAsStream("/images/User.png"));
        DialogBox dialogBox =DialogBox.getUserDialog(message, userImage);

        dialogContainer.getChildren().add(dialogBox);
}

    /**
     * Scrolls the conversation window to the latest message.
     */
    private void scrollToBottom() {
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }
}