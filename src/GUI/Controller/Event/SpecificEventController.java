package GUI.Controller.Event;

import BE.Event;
import BE.User;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SpecificEventController extends BaseController implements Initializable {


    @FXML
    private ImageView imgTicketPreview;
    @FXML
    private Label lblAdmin;
    @FXML
    private Label lblInfo;
    @FXML
    private Label lblTicketCounter;
    @FXML
    private Label lblUsername;
    @FXML
    private ListView<User> lvAllUsers;
    @FXML
    private TextArea taEventNotes;
    @FXML
    private TextField tfSearch;
    private EventModel eventModel;

    public void initialize(URL location, ResourceBundle resources){
        super.initialize(location, resources);
        eventModel = new EventModel();
    }

    @FXML
    void onDeleteEvent(ActionEvent event) {

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this event?");
        confirmationAlert.setContentText("This action cannot be undone");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
               // FIXME: eventModel.deleteEvent(selectedEvent);
                // TODO: Get the event objected passed to this controller, so that i can delete the correct object.
                // TODO: Update the events being shown, use the read method here/Remove from the list.
                loadFXML("/EventWindow.FXML",model, (Stage) lblUsername.getScene().getWindow());
            }
        });
        //Show and wait, are you sure, everything connected, tickets sold to this event
        // users assigned and event coordinators will also be removed from this event.
        // Delete Logic

    }

    @FXML
    void onEditEvent(ActionEvent event) {
        // Get info from the event loaded into the fields
        loadFXML("/EditEvent.FXML",model, (Stage) lblUsername.getScene().getWindow());
    }

    @FXML
    void onPrintTicket(ActionEvent event) {}

    @FXML
    void onMailTicket(ActionEvent event) {}


    void initData(Event event) throws SQLException, IOException {
        lvAllUsers.setItems(model.getAllUsers());
    }
}

