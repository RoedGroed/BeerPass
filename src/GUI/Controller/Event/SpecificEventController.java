package GUI.Controller.Event;

import BE.Event;
import BE.Ticket;
import BE.User;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
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
    private Label lblEventName;
    @FXML
    private ListView<User> lvAllUsers;
    @FXML
    private TextArea taEventNotes;

    @FXML
    private TextField tfSearch;
    private EventModel eventModel;
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

    public void initialize(URL location, ResourceBundle resources){
        super.initialize(location, resources);
        eventModel = new EventModel();




        tBtnSpecial.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateVisibility(newValue, false);
        });

        tBtnEvent.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateVisibility(false, newValue);
        });
    }

    private void updateVisibility(boolean specialSelected, boolean eventSelected) {
        lblSeleUser.setVisible(eventSelected);
        tfSearch.setVisible(eventSelected);
        lvAllUsers.setVisible(eventSelected);
        lblSeleTicket.setVisible(specialSelected || eventSelected);
        lvRadioBtns.setVisible(specialSelected || eventSelected);

        if (eventSelected) {
            //populateRadioButtonsForEventTickets();
        } else if (specialSelected) {
            populateRadioButtonsForSpecialTickets();
        }

    }

    private void populateRadioButtonsForEventTickets() {
        /*private void populateRadioButtonsForEventTickets(int eventId) {
            List<Ticket> ticketsForEvent = ticketModel.getTicketsForEvent(eventId);
            lvRadioBtns.getItems().clear();

            ToggleGroup ticketToggleGroup = new ToggleGroup();

            for (Ticket ticket : ticketsForEvent) {
                RadioButton radioButton = new RadioButton(ticket.getName());
                radioButton.setUserData(ticket);
                radioButton.setToggleGroup(ticketToggleGroup);

                lvRadioBtns.getItems().add(radioButton);
            }*/

        }

        private void updateTicketPreview(Ticket ticket) {

        }


    private void populateRadioButtonsForSpecialTickets() {
    }

    @FXML
    void onDeleteEvent(ActionEvent actionEvent) {

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this event?");
        confirmationAlert.setContentText("This action cannot be undone");

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK){
                try {
                    eventModel.deleteEvent(event);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
    void onEditEvent(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditEvent.fxml"));
            Parent root = loader.load();

            EditEventController editEventController = loader.getController();
            editEventController.populateFields(event); // Pass the event object to populate the fields

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
    void onPrintTicket(ActionEvent event) {}

    @FXML
    void onMailTicket(ActionEvent event) {}


    void initData(Event event) throws SQLException, IOException {
        lvAllUsers.setItems(model.getAllUsers());
    }

    public void populateFields(Event event){
        this.event = event;


        lblEventName.setText(event.getName());
        lblInfo.setText(event.getTime() + " || " + event.getLocation());
        lblTicketCounter.setText(String.valueOf(event.getTicketLimit()));
        //FIXME: Der skal laves en måde at regne de solgte billeter ud på.
        taEventNotes.setText(event.getNote());
        //populateTickets(event);

    }

    /*private void populateTickets(Event event) {
        List<Ticket> ticketsForEvent = ticketModel.getTicketsForEvent(event.getEventID());
        for (Ticket ticket : ticketsForEvent) {
            RadioButton radioButton = new RadioButton(ticket.getTicketName());
            radioButton.setToggleGroup(ticketToggleGroup);
            radioButton.setUserData(ticket);
            lvRadioBtns.getItems().add(radioButton);
        }
    }*/


}

