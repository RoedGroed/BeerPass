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
    private ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
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
            //Setting up the listview in the program (Events)
            allEvents.addAll(eventModel.getAllEvents());
            listviewEvents.setItems(allEvents);
            //Getting the relative data to insert into the listview
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
            //Listview for ticket types
            allTickets.addAll(model.getAllTickets());
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
            //listview for the tickets linked to an event
            listviewEvents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Event selectedEvent = (Event) newValue;
                    // Getting the linked tickets for the selected event
                    List<Ticket> linkedTickets = null;
                    try {
                        linkedTickets = model.getLinkedTickets(selectedEvent.getEventID());
                    } catch (SQLException | IOException e) {
                        throw new RuntimeException(e);
                    }
                    // Clear the list before adding new items
                    listviewAvaTickets.getItems().clear();
                    // Extracting ticket names
                    ObservableList<String> ticketNames = FXCollections.observableArrayList();
                    for (Ticket ticket : linkedTickets) {
                        ticketNames.add(ticket.getTicketName());
                    }
                    // Setting ticket names to the listviewAvaTickets
                    listviewAvaTickets.setItems(ticketNames);
                }
            });

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onRemoveTicket(ActionEvent actionEvent) {
        if (listviewTicketTypes.getSelectionModel().getSelectedItem() != null) {
            Ticket selectedTicketName = (Ticket) listviewTicketTypes.getSelectionModel().getSelectedItem();
            // Retrieving the ticketID then removing it
            try {
                model.deleteTicket(selectedTicketName.getTicketID());
                updateUI();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "This ticket is in use, please remove it form an event first.");
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "This ticket is in use, please remove it form an event first.");
                alert.showAndWait();
            }
        }
    }
    @FXML
    private void onAddTicket(ActionEvent actionEvent) {
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
                    updateUI();
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
    @FXML
    private void onTriangleLeft(ActionEvent actionEvent) throws SQLException, IOException, InterruptedException {
        if(listviewEvents.getSelectionModel().getSelectedItem() != null &&
                listviewTicketTypes.getSelectionModel().getSelectedItem() != null) {
            //Getting the event and ticket ids from the two listviews
            Event selectedEvent = (Event) listviewEvents.getSelectionModel().getSelectedItem();
            int eventID = selectedEvent.getEventID();
            Ticket selectedTicket = (Ticket) listviewTicketTypes.getSelectionModel().getSelectedItem();
            int ticketID = selectedTicket.getTicketID();
            //calling the method from the database to link the ticket to the event
            model.linkTicketToEvent(eventID, ticketID);
            updateUI();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select both an Event and a Ticket type to perform this action.");
            alert.showAndWait();
        }
    }
    @FXML
    private void onTriangleRight(ActionEvent actionEvent) {
        //Checking that both an event and an available ticket have been selected
        if(listviewEvents.getSelectionModel().getSelectedItem() != null &&
                listviewAvaTickets.getSelectionModel().getSelectedItem() != null) {
            //Removing a ticket from a specific event
            Event selectedEvent = (Event) listviewEvents.getSelectionModel().getSelectedItem();
            int eventID = selectedEvent.getEventID();

            //Get the selected ticket name as a string
            String selectedTicketName = (String) listviewAvaTickets.getSelectionModel().getSelectedItem();

            //Retrieving the ticket id via ticket name
            try {
                int ticketID = model.getTicketIDByName(selectedTicketName);
                model.removeTicketFromEvent(eventID, ticketID);
                updateUI();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //Method which updates the ui
    private void updateUI() {
        try {
            // Update the events list
            allEvents.clear();
            allEvents.addAll(eventModel.getAllEvents());

            // Update the tickets list
            allTickets.clear();
            allTickets.addAll(model.getAllTickets());

            // Refresh ListViews
            listviewEvents.refresh();
            listviewTicketTypes.refresh();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
