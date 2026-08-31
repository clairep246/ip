package bags.ui;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box containing a message and a profile picture.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        assert text != null : "Dialog text must not be null";
        assert img != null : "Dialog image must not be null";

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    DialogBox.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert dialog != null : "Dialog label must be loaded from FXML";
        assert displayPicture != null
                : "Display picture must be loaded from FXML";

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left
     * and text is on the right.
     */
    private void flip() {
        assert dialog != null : "Dialog label must be initialized";
        assert getChildren().size() >= 2
                : "Dialog box must contain text and image nodes";

        this.setAlignment(Pos.TOP_LEFT);

        ObservableList<Node> tmp =
                FXCollections.observableArrayList(this.getChildren());

        FXCollections.reverse(tmp);
        this.getChildren().setAll(tmp);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box for Bags.
     *
     * @param text the message from Bags
     * @param img the profile picture for Bags
     * @return a dialog box for Bags
     */
    public static DialogBox getBagsDialog(String text, Image img) {
        assert text != null : "Bags dialog text must not be null";
        assert img != null : "Bags profile picture must not be null";

        DialogBox db = new DialogBox(text, img);
        db.flip();

        assert db != null : "Bags dialog box must be created";
        return db;
    }

    /**
     * Creates a dialog box for the user.
     *
     * @param text the message from the user
     * @param img the user's profile picture
     * @return a dialog box for the user
     */
    public static DialogBox getUserDialog(String text, Image img) {
        assert text != null : "User dialog text must not be null";
        assert img != null : "User profile picture must not be null";

        DialogBox db = new DialogBox(text, img);

        assert db != null : "User dialog box must be created";
        return db;
    }
}