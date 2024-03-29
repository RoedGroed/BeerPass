package GUI.Controller.Event;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

public class EventWindowController {

    @FXML private Button btnLogout;

    @FXML
    private void handleLogoutButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        URL url = getClass().getResource("/Login.fxml");

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}