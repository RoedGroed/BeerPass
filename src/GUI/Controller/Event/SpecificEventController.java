package GUI.Controller.Event;

import BE.User;
import GUI.Controller.BaseController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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



    @FXML
    void onDeleteEvent(ActionEvent event) {
        //Show and wait, are you sure, everything connected, tickets sold to this event
        // users assigned and event coordinators will also be removed from this event.
        // Delete Logic

        loadFXML("/EventWindow.FXML",model, (Stage) lblUsername.getScene().getWindow());
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


}

