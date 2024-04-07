package GUI.Controller.Event;

import GUI.Controller.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EditEventController extends BaseController implements Initializable {

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
        super.initialize(location, resources);
        cbEventImages.getItems().addAll("John", "Emma", "Michael", "Sophia");
    }
    @FXML
    private void onCancel(ActionEvent actionEvent) {
        loadFXML("/SpecificEvent.FXML", model, (Stage) tfEventName.getScene().getWindow());
    }

    @FXML
    private void onConfirmEvent(ActionEvent actionEvent) {
        // Show and Wait Box
        loadFXML("/SpecificEvent.FXML", model, (Stage) tfEventName.getScene().getWindow());
    }

    @FXML
    private void onAddCoordinator(ActionEvent actionEvent){}

    @FXML
    private void onRemoveCoordinator(ActionEvent actionEvent){}


}
