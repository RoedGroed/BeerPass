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
    private ListView listviewEventTickets;
    @FXML
    private ListView listviewSpecialTickets;
    @FXML
    private TextField txtfTicketName;
    @FXML
    private ToggleButton tglBtnEventTicket;
    @FXML
    private ToggleButton tglBtnSpecialTicket;
    private EventModel eventModel;
    private Event event;
    private ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private ObservableList<Ticket> eventTickets = FXCollections.observableArrayList();
    private ObservableList<Ticket> specialTickets = FXCollections.observableArrayList();
    ToggleGroup tg = new ToggleGroup();
    public TicketWindowController() {
        eventModel = new EventModel();
    }

    /**
     * Sets up toggle groups and initializes list views
     */
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        tglBtnEventTicket.setToggleGroup(tg);
        tglBtnSpecialTicket.setToggleGroup(tg);
        initListViews();
    }

    /**
     * Initializes list views for events and tickets
     * Sets up cell factories for list views displaying events, event tickets, and special tickets
     */
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
            //Listview for eventticket types
            eventTickets.addAll(model.getEventTickets());
            listviewEventTickets.setItems(eventTickets);
            listviewEventTickets.setCellFactory(ticketListView -> new ListCell<Ticket>() {
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
            specialTickets.addAll(model.getSpecialTickets());
            listviewSpecialTickets.setItems(specialTickets);
            listviewSpecialTickets.setCellFactory(ticketListView -> new ListCell<Ticket>() {
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
            //listview for the event tickets linked to an event
            listviewEvents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    Event selectedEvent = (Event) newValue;
                    // Getting the linked tickets for the selected event
                    List<Ticket> linkedTickets = null;
                    try {
                        linkedTickets = model.getLinkedTickets(selectedEvent.getEventID());
                    } catch (SQLException | IOException e) {
                        showAlert("Error", "Contact support / database error");
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
            showAlert("Error", "Contact support / database error");
        }
    }

    /**
     * Removes the selected ticket from the list view and deletes it from the database
     */
    @FXML
    private void onRemoveTicket(ActionEvent actionEvent) {
        if (listviewEventTickets.getSelectionModel().getSelectedItem() != null) {
            Ticket selectedTicketName = (Ticket) listviewEventTickets.getSelectionModel().getSelectedItem();
            // Retrieving the ticketID then removing it
            try {
                model.deleteTicket(selectedTicketName.getTicketID());
                updateUI();
            } catch (SQLException | IOException e) {
                showAlert("Error", "This ticket is in use");
            }
        } else if (listviewSpecialTickets.getSelectionModel().getSelectedItem() != null) {
            Ticket selectedTicketName = (Ticket) listviewSpecialTickets.getSelectionModel().getSelectedItem();
            // Retrieving the ticketID then removing it
            try {
                model.deleteTicket(selectedTicketName.getTicketID());
                updateUI();
            } catch (SQLException | IOException e) {
                showAlert("Error", "This ticket is in use");
            }
        }
    }

    /**
     * Adds a new ticket to the database based on the input provided by the user.
     */
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
                    showAlert("Error", "An error occurred while adding this ticket. \r" +
                            "Please try again");
                } catch (SQLException e) {
                    showAlert("Error", "Contact support / database error");
                }
            } else {
                showInformationAlert("Warning", "Please select a ticket type");
            }
        } else {
            showInformationAlert("Warning", "You need to enter a ticket name");
        }
    }

    public void onEventTicket(ActionEvent actionEvent) {
    }

    public void onSpecialTicket(ActionEvent actionEvent) {
    }

    /**
     * Links the selected ticket to the selected event in the database
     */
    @FXML
    private void onTriangleLeft(ActionEvent actionEvent) {
        if(listviewEvents.getSelectionModel().getSelectedItem() != null &&
                listviewEventTickets.getSelectionModel().getSelectedItem() != null) {
            //Getting the event and ticket ids from the two listviews
            Event selectedEvent = (Event) listviewEvents.getSelectionModel().getSelectedItem();
            int eventID = selectedEvent.getEventID();
            Ticket selectedTicket = (Ticket) listviewEventTickets.getSelectionModel().getSelectedItem();
            int ticketID = selectedTicket.getTicketID();
            //calling the method from the database to link the ticket to the event
            try {
                model.linkTicketToEvent(eventID, ticketID);
            } catch (SQLException | IOException e) {
                showAlert("Error", "Contact support / database error");
            }
            updateUI();
        }
        else {
            showInformationAlert("Warning", "Please select both an event and a ticket type to perform this action");
        }
    }

    /**
     * Removes the selected ticket from the selected event in the database
     */
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
            } catch (SQLException | IOException e) {
               showAlert("Error", "Contact support / database error");
            }
        }
    }

    /**
     * Updates the UI by refreshing the events and tickets lists amd
     * refreshing the ListViews displaying them
     */
    private void updateUI() {
        try {
            // Update the events list
            allEvents.clear();
            allEvents.addAll(eventModel.getAllEvents());

            // Update the tickets list
            eventTickets.clear();
            eventTickets.addAll(model.getEventTickets());
            specialTickets.clear();
            specialTickets.addAll(model.getSpecialTickets());

            // Refresh ListViews
            listviewEvents.refresh();
            listviewEventTickets.refresh();
            listviewSpecialTickets.refresh();
        } catch (SQLException | IOException e) {
            showAlert("Error", "Could not update the UI");
        }
    }
}
