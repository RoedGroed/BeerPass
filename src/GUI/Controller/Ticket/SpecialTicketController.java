package GUI.Controller.Ticket;

import BE.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class SpecialTicketController {
    @FXML
    private Label lblEventName;
    @FXML
    private ImageView qrCode;
    @FXML
    private ImageView barCode;

    public void setSpecialTicketData(Event event) {
        lblEventName.setText(event.getName());
    }
}
