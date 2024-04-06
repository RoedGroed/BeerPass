package GUI.Controller.Event;

import BE.Event;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;


public class EventWindowController extends BaseController implements Initializable {

    @FXML
    Button btnNewEvent;

    EventModel eventModel = new EventModel();

    public List<Event> handleGetAllEvents() {
        try {
            return eventModel.getAllEvents();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @FXML
    private void onNewEvent(ActionEvent actionEvent) {
        loadFXML("/NewEvent.FXML", model, (Stage) btnNewEvent.getScene().getWindow());
    }

}