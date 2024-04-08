package GUI.Controller.Ticket;

import BE.Event;
import BE.Ticket;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class TicketWindowController extends BaseController implements Initializable {
    @FXML
    private ListView listviewEvents;
    @FXML
    private ListView listviewAvaTickets;
    @FXML
    private ListView listviewTicketTypes;
    @FXML
    private TextField txtfTicketName;
    @FXML
    private ToggleButton tglBtnEventTicket;
    @FXML
    private ToggleButton tglBtnSpecialTicket;
    private EventModel eventModel;
    private Event event;
    ToggleGroup tg = new ToggleGroup();
    public TicketWindowController() {
        eventModel = new EventModel();
    }


    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        tglBtnEventTicket.setToggleGroup(tg);
        tglBtnSpecialTicket.setToggleGroup(tg);
        initListViews();
    }

    private void initListViews() {
        try {
            ObservableList<Event> allEvents = FXCollections.observableArrayList(eventModel.getAllEvents());
            listviewEvents.setItems(allEvents);
            listviewEvents.setCellFactory(eventListView -> new ListCell<Event>() {
                @Override
                protected void updateItem(Event event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setText(null);
                    } else {
                        setText(event.getName() + " // " + event.getTime());
                    }
                }
            });

            ObservableList<Ticket> allTickets = FXCollections.observableArrayList(model.getAllTickets());
            listviewTicketTypes.setItems(allTickets);
            listviewTicketTypes.setCellFactory(ticketListView -> new ListCell<Ticket>() {
                @Override
                protected void updateItem(Ticket ticket, boolean empty) {
                    super.updateItem(ticket, empty);
                    if (empty || ticket == null) {
                        setText(null);
                    } else {
                        setText(ticket.getTicketName());
                    }
                }
            });

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void onRemoveTicket(ActionEvent actionEvent) {
    }

    public void onAddTicket(ActionEvent actionEvent) {
        //checking that the "Ticket name field" is not empty and that "togglebutton" has been selected
        if (!txtfTicketName.getText().isEmpty()) {
            ToggleButton selectedToggleButton = (ToggleButton) tg.getSelectedToggle();
            if (selectedToggleButton != null) {
                try {
                    //Gathering the information about the ticket before sending it to the database method 'addTicket'
                    String ticketName = txtfTicketName.getText();
                    String ticketType = selectedToggleButton.getText();
                    System.out.println("Ticket name " + ticketName + " TicketType " + ticketType);
                    model.addTicket(ticketName, ticketType);
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred while adding the ticket");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a ticket type to continue.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You need to enter a ticket name.");
            alert.showAndWait();
        }
    }

    public void onEventTicket(ActionEvent actionEvent) {
    }

    public void onSpecialTicket(ActionEvent actionEvent) {
    }

    public void onTriangleLeft(ActionEvent actionEvent) throws SQLException, IOException {
        if(listviewEvents.getSelectionModel().getSelectedItem() != null &&
                listviewTicketTypes.getSelectionModel().getSelectedItem() != null) {
            //Getting the event and ticket ids from the two listviews
            Event selectedEvent = (Event) listviewEvents.getSelectionModel().getSelectedItem();
            int eventID = selectedEvent.getEventID();
            Ticket selectedTicket = (Ticket) listviewTicketTypes.getSelectionModel().getSelectedItem();
            int ticketID = selectedTicket.getTicketID();
            //calling the method from the database to link the ticket to the event
            model.linkTicketToEvent(eventID, ticketID);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select both an Event and a Ticket type to perform this action.");
            alert.showAndWait();
        }
    }
    public void onTriangleRight(ActionEvent actionEvent) {
    }
}
