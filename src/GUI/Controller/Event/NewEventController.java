package GUI.Controller.Event;

import GUI.Controller.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class NewEventController extends BaseController implements Initializable {

    @FXML
    private TextField tfEventName;
    @FXML
    private TextField tfEventLocation;
    @FXML
    private TextField tfEventTime;
    @FXML
    private TextField tfMaxAttendees;
    @FXML
    private ListView<?> lvCoordinators;
    @FXML
    private ListView<?> lvAllCoordinators;

    @FXML
    private ComboBox<String> cbEventImages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbEventImages.getItems().addAll("John", "Emma", "Michael", "Sophia");
    }
    @FXML
    private void onCancel(ActionEvent actionEvent) {
        // Add your cancel logic here
    }

    @FXML
    private void onConfirmUser(ActionEvent actionEvent) {
        // Add your confirm user logic here
    }

}
