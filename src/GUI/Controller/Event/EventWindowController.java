package GUI.Controller.Event;

import GUI.Controller.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class EventWindowController extends BaseController implements Initializable {

    @FXML
    Button btnNewEvent;

    @FXML
    private void onNewEvent(ActionEvent actionEvent) {
        loadFXML("/NewEvent.FXML", model, (Stage) btnNewEvent.getScene().getWindow());
    }

}