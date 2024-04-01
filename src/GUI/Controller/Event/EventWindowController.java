package GUI.Controller.Event;

import GUI.Controller.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EventWindowController extends BaseController implements Initializable {

    @FXML
    private void onNewEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewEvent.fxml"));
            Parent root = loader.load();

            BaseController controller = loader.getController();
            controller.setModel(model);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("BrewPass");
            stage.show();
            Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load App.fxml");
            alert.showAndWait();
        }
    }

}