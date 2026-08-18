
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.DialogPane;

public class quitAction {
    public static void handleQuit() {
        //show dialog window
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit the system?");
        alert.setContentText("Click OK to exit, or Cancel to stay.");

        //alert style
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Courier New';" + "-fx-font-size: 14px;");

        //waiting user selection
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
}
