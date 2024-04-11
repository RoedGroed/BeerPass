package GUI.Controller.Ticket;

import BE.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class SpecialTicketController {
    @FXML
    private Label lblTicketType;
    @FXML
    private ImageView qrCode;
    @FXML
    private ImageView barCode;

    public void setSpecialTicketData(Ticket selectedTicket) {
        lblTicketType.setText(selectedTicket.getTicketName());
    }
}
