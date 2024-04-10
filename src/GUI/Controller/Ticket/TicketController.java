package GUI.Controller.Ticket;


import BE.Event;
import BE.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.*;

public class TicketController {


    @FXML
    private Label eventName;
    @FXML
    private Label eventLocation;
    @FXML
    private Label eventTime;
    @FXML
    private Label costumerName;
    @FXML
    private Label eventNotes;
    @FXML
    private ImageView qrCode;
    @FXML
    private ImageView barCode;

    public TicketController() {
    }

    public void setEventName(Label eventName) {
        this.eventName = eventName;
    }

    public void setEventLocation(Label eventLocation) {
        this.eventLocation = eventLocation;
    }

    public void setEventTime(Label eventTime) {
        this.eventTime = eventTime;
    }

    public void setCostumerName(Label costumerName) {
        this.costumerName = costumerName;
    }

    public void setEventNotes(Label eventNotes) {
        this.eventNotes = eventNotes;
    }

    public void setEventTicketData(Event event) {
        eventName.setText(event.getName());
        eventLocation.setText(event.getLocation());
        eventTime.setText(event.getTime());
        //costumerName.setText();
        eventNotes.setText(event.getNote());
    }
}



