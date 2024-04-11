package GUI.Controller.Event;

import BE.Event;
import BE.Ticket;
import BE.User;
import GUI.Controller.BaseController;
import GUI.Controller.Ticket.SpecialTicketController;
import GUI.Controller.Ticket.TicketController;
import GUI.Model.EventModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SpecificEventController extends BaseController implements Initializable {

    private EventModel eventModel;
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
    private Label lblEventName;
    @FXML
    private ListView<User> lvAllUsers;
    @FXML
    private TextArea taEventNotes;

    @FXML
    private TextField tfSearch;

    private Event event;
    @FXML
    private ListView lvRadioBtns;

    @FXML
    private ToggleGroup grpSelectTicket;
    @FXML
    private ToggleButton tBtnSpecial;
    @FXML
    private ToggleButton tBtnEvent;
    @FXML
    private Label lblSeleTicket;
    @FXML
    private Label lblSeleUser;
    @FXML
    private StackPane spTicketPreview;
    private ToggleGroup ticketToggleGroup = new ToggleGroup();
    private Ticket currentSelectedTicket;
    private User currentSelectedUser;

    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        eventModel = new EventModel();
        initToggleBtns();
        initListenForPreview();


    }
    private void initListenForPreview() {
        ticketToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentSelectedTicket = (Ticket) newValue.getUserData();
            } else {
                currentSelectedTicket = null;
            }
            updatePreviewIfPossible();
        });


        lvAllUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentSelectedUser = newValue;
            updatePreviewIfPossible();
        });


    }

    private void updatePreviewIfPossible() {
        if (currentSelectedTicket != null && currentSelectedTicket.getTicketType().equals("Special Ticket")){
            updateTicketPreview(currentSelectedTicket);
        }

        else if (currentSelectedTicket != null && currentSelectedUser != null) {
            updateTicketPreview(currentSelectedTicket);
        }
    }

    private void updateTicketPreview(Ticket selectedTicket) {
        User selectedUser = lvAllUsers.getSelectionModel().getSelectedItem();
            try {
                if ("Special Ticket".equals(selectedTicket.getTicketType())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/SpecialTicket.fxml"));
                    Parent ticketPreview = loader.load();
                    SpecialTicketController specialTicketController = loader.getController();
                    specialTicketController.setSpecialTicketData(selectedTicket);

                    spTicketPreview.getChildren().clear();
                    spTicketPreview.getChildren().add(ticketPreview);
                } else if ("Event Ticket".equals(selectedTicket.getTicketType())) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ticket.fxml"));
                    Parent ticketPreview = loader.load();
                    TicketController ticketController = loader.getController();
                    ticketController.setEventTicketData(event, selectedUser);

                    spTicketPreview.getChildren().clear();
                    spTicketPreview.getChildren().add(ticketPreview);
                }}
            catch(IOException e){
                e.printStackTrace();
            }
    }


    private void initToggleBtns() {
        tBtnSpecial.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                updateVisibility(newValue, false);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        tBtnEvent.selectedProperty().addListener((observable, oldValue, newValue) -> {
            try {
                updateVisibility(false, newValue);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateVisibility(boolean specialSelected, boolean eventSelected) throws SQLException, IOException {
        lblSeleUser.setVisible(eventSelected);
        tfSearch.setVisible(eventSelected);
        lvAllUsers.setVisible(eventSelected);
        lblSeleTicket.setVisible(specialSelected || eventSelected);
        lvRadioBtns.setVisible(specialSelected || eventSelected);

        if (eventSelected) {
            populateRadioButtonsForEventTickets(event);
        } else if (specialSelected) {
            populateRadioButtonsForSpecialTickets();
        }
    }



    private void populateRadioButtonsForEventTickets(Event event) throws SQLException, IOException {
        List<Ticket> ticketsForEvent = model.getLinkedTickets(event.getEventID());
        populateRadioButtons(ticketsForEvent);
    }

    private void populateRadioButtonsForSpecialTickets() throws SQLException, IOException {
        List<Ticket> specialTickets = model.getSpecialTickets();
        populateRadioButtons(specialTickets);
    }
    //ToggleGroup ticketToggleGroup = new ToggleGroup();

    private void populateRadioButtons(List<Ticket> tickets) {
        lvRadioBtns.getItems().clear();
        for (Ticket ticket : tickets) {
            MFXRadioButton rb = new MFXRadioButton(ticket.getTicketType());
            rb.setUserData(ticket);
            rb.setText(ticket.getTicketName());
            rb.setToggleGroup(ticketToggleGroup);
            lvRadioBtns.getItems().add(rb);
        }
    }

    @FXML
    void onDeleteEvent(ActionEvent actionEvent) {

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this event?");
        confirmationAlert.setContentText("This action cannot be undone");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    eventModel.deleteEvent(event);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // TODO: Get the event objected passed to this controller, so that i can delete the correct object.
                // TODO: Update the events being shown, use the read method here/Remove from the list.
                loadFXML("/EventWindow.FXML", model, (Stage) lblUsername.getScene().getWindow());
            }
        });
        //Show and wait, are you sure, everything connected, tickets sold to this event
        // users assigned and event coordinators will also be removed from this event.
        // Delete Logic

    }

    @FXML
    void onEditEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditEvent.fxml"));
            Parent root = loader.load();

            EditEventController editEventController = loader.getController();
            editEventController.populateFields(event); // Pass the event object to populate the fields
            editEventController.populateCoordinatorLists(event.getEventID());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Event");
            stage.show();

            Stage currentStage = (Stage) lblInfo.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPrintTicket(ActionEvent actionEvent) {
        if (tBtnEvent.isSelected()) {
            if (lvAllUsers.getSelectionModel().getSelectedItem() != null) {
                if (ticketToggleGroup.getSelectedToggle().getUserData().toString() != null) {
                    //Find the ticket in the tickettoggleGroup and then get the ID from it
                    Ticket selected = (Ticket) ticketToggleGroup.getSelectedToggle().getUserData();
                    int ticketID = selected.getTicketID();
                    int eventID = event.getEventID();
                    int userID = lvAllUsers.getSelectionModel().getSelectedItem().getUserID();
                    try {
                        model.linkTicketToUser(userID, ticketID, eventID);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred when trying to assign the user to a ticket");
                        alert.showAndWait();
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred when trying to assign the user to a ticket");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a ticket");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a user");
                alert.showAndWait();
            }
        } else if (tBtnSpecial.isSelected()) {
            //Implement here
        }
    }


    @FXML
    void onMailTicket(ActionEvent actionEvent) {
        if (tBtnEvent.isSelected()) {
            if (lvAllUsers.getSelectionModel().getSelectedItem() != null) {
                if (ticketToggleGroup.getSelectedToggle().getUserData().toString() != null) {
                    //Find the ticket in the tickettoggleGroup and then get the ID from it
                    Ticket selected = (Ticket) ticketToggleGroup.getSelectedToggle().getUserData();
                    int ticketID = selected.getTicketID();
                    int eventID = event.getEventID();
                    int userID = lvAllUsers.getSelectionModel().getSelectedItem().getUserID();
                    try {
                        model.linkTicketToUser(userID, ticketID, eventID);
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred when trying to assign the user to a ticket");
                        alert.showAndWait();
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred when trying to assign the user to a ticket");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a ticket");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a user");
                alert.showAndWait();
            }
        } else if (tBtnSpecial.isSelected()) {
            //Implement here
        }
    }


    void initData(Event event) throws SQLException, IOException {
        lvAllUsers.setItems(model.getAllUsers());
    }

    public void populateFields(Event event) {
        this.event = event;


        lblEventName.setText(event.getName());
        lblInfo.setText(event.getTime() + " || " + event.getLocation());
        lblTicketCounter.setText(String.valueOf(event.getTicketLimit()));
        //FIXME: Der skal laves en måde at regne de solgte billeter ud på.
        taEventNotes.setText(event.getNote());

    }

}

