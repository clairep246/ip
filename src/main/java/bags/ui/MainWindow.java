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
        assert userInput != null : "User input field must be initialized";
        assert sendButton != null : "Send button must be initialized";
        assert scrollPane != null : "Scroll pane must be initialized";
        assert dialogContainer != null
                : "Dialog container must be initialized";

        bags = new Bags();

        assert bags != null : "Bags application must be created";

        addBagsMessage("Hello! I'm Bags. Nice to meet you!");
        addBagsMessage("What can I do for you?");
    }

    /**
     * Handles commands entered through the text field or send button.
     */
    @FXML
    private void handleUserInput() {
        assert userInput != null : "User input field must be initialized";
        assert bags != null : "Bags application must be initialized";

        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            addBagsMessage("Please enter a command.");
            return;
        }

        addUserMessage(input);

        try {
            String response = bags.processCommand(input);

            assert response != null : "Bags must return a response";

            addBagsMessage(response);

            if (input.equals("bye")) {
                assert sendButton != null
                        : "Send button must be initialized";
                sendButton.setDisable(true);
                userInput.setDisable(true);
            }

        } catch (BagsException e) {
            assert e != null : "Caught exception must not be null";
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
        assert message != null : "Bags message must not be null";
        assert dialogContainer != null
                : "Dialog container must be initialized";

        bagsImage = new Image(
                MainWindow.class.getResourceAsStream(
                        "/images/Response.png"));

        assert bagsImage != null : "Bags image must be loaded";
        assert !bagsImage.isError() : "Bags image must load without errors";

        DialogBox dialogBox =
                DialogBox.getBagsDialog(message, bagsImage);

        assert dialogBox != null : "Bags dialog box must be created";

        dialogContainer.getChildren().add(dialogBox);
    }

    /**
     * Adds a user message to the conversation window.
     *
     * @param message the message to display
     */
    private void addUserMessage(String message) {
        assert message != null : "User message must not be null";
        assert dialogContainer != null
                : "Dialog container must be initialized";

        userImage = new Image(
                MainWindow.class.getResourceAsStream(
                        "/images/User.png"));

        assert userImage != null : "User image must be loaded";
        assert !userImage.isError() : "User image must load without errors";

        DialogBox dialogBox =
                DialogBox.getUserDialog(message, userImage);

        assert dialogBox != null : "User dialog box must be created";

        dialogContainer.getChildren().add(dialogBox);
    }

    /**
     * Scrolls the conversation window to the latest message.
     */
    private void scrollToBottom() {
        assert scrollPane != null : "Scroll pane must be initialized";

        scrollPane.layout();
        scrollPane.setVvalue(1.0);

        assert scrollPane.getVvalue() == 1.0
                : "Scroll pane should be positioned at the bottom";
    }
}