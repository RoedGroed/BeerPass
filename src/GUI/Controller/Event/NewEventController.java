package GUI.Controller.Event;

import GUI.Controller.BaseController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        loadFXML("/EventWindow.FXML",model, (Stage) tfEventName.getScene().getWindow());
    }

    @FXML
    private void onConfirmUser(ActionEvent actionEvent) {
        // Show and Wait
        // Update the database and lists(Obserable Lists)
        loadFXML("/EventWindow.FXML",model, (Stage) tfEventName.getScene().getWindow());
    }


    /* TODO: When user gets a date, populate the time textfield with local time, so they can see the format.
            Lav en form for verification for time, så det altid er i korrekt format (Se MyTunes)
            -
            Populate(Metode) Combobox med billeder, hvor billedet så giver den rigtige imagepath.
            Skal man kunne add nye billeder igennem GUI'en?
            Gør så combobox bliver updated hvis der bliver added nye billeder i EventImage Mappen?
            ComboBox image picked -> Load Image i ImageViewer.
            -
            Format DatePicker data om til String(hvis den ikke allerede er det) og format den til ikke Amerikansk.

     */


}
