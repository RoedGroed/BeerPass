package GUI.Controller.Event;

import BE.Event;
import BE.Ticket;
import BE.User;
import GUI.Controller.BaseController;
import GUI.Controller.Ticket.SpecialTicketController;
import GUI.Controller.Ticket.TicketController;
import GUI.Model.EventModel;
import Util.BarQRCodeUtil;
import Util.MailUtility;
import Util.SearchUtility;
import com.google.zxing.WriterException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;


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
    private ObservableList<User> allUsers;
    @FXML
    private Spinner spinnerSpecTickets;
    @FXML
    private MFXButton btnMailTicket;

    /**
     * Initializes the event model, toggle buttons, event preview listener, search listener, and loads all users
     */
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        eventModel = new EventModel();
        initToggleBtns();
        initListenForPreview();
        initSearchListener();
        loadAllUsers();


    }

    /**
     * Initializes the listener to update the preview when a ticket is selected from the toggle group
     */
    private void initListenForPreview() {
        ticketToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                currentSelectedTicket = (Ticket) newValue.getUserData();
                updatePreviewIfPossible();
            } else {
                currentSelectedTicket = null;
            }
        });


        lvAllUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentSelectedUser = newValue;
            updatePreviewIfPossible();
        });


    }

    /**
     * Updates the ticket preview if a ticket is selected and either it's a special ticket or an event ticket
     * Generates a new UUID for the current selected Ticket
     */
    private void updatePreviewIfPossible() {
        if (currentSelectedTicket != null && currentSelectedTicket.getTicketType().equals("Special Ticket")){
            generateNewUUIDForTicket();
            updateTicketPreview(currentSelectedTicket);
        }

        else if (currentSelectedTicket != null && currentSelectedUser != null) {
            generateNewUUIDForTicket();
            updateTicketPreview(currentSelectedTicket);
        }
    }

    private void generateNewUUIDForTicket() {
        if (currentSelectedTicket != null) {
            currentSelectedTicket.setUuid(UUID.randomUUID()); // Always set a new UUID here
            System.out.println("New UUID generated for the ticket: " + currentSelectedTicket.getUuid());
        }
    }

    /**
     * Updates the ticket preview based on the selected ticket and user
     * If the selected ticket is a "Special Ticket", loads the SpecialTicket.fxml and sets its data.
     * If the selected ticket is an "Event Ticket", loads the Ticket.fxml and sets its data
     * Generates and sets Bar & QR code images for the ticket
     * @param selectedTicket the ticket object
     */
    private void updateTicketPreview(Ticket selectedTicket) {
        User selectedUser = lvAllUsers.getSelectionModel().getSelectedItem();
        String uniqueID = currentSelectedTicket.getUuid().toString();

        try {
            if ("Special Ticket".equals(selectedTicket.getTicketType())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SpecialTicket.fxml"));
                Parent ticketPreview = loader.load();
                SpecialTicketController specialTicketController = loader.getController();
                specialTicketController.setSpecialTicketData(selectedTicket);

                // Generate Bar & QR code images for the special ticket
                Image qrCodeImage = BarQRCodeUtil.generateQRCodeImage(uniqueID);
                Image barCodeImage = BarQRCodeUtil.generateBarcodeImage(uniqueID);
                specialTicketController.setQRCodeImage(qrCodeImage);
                specialTicketController.setBarCode(barCodeImage);

                spTicketPreview.getChildren().clear();
                spTicketPreview.getChildren().add(ticketPreview);
            } else if ("Event Ticket".equals(selectedTicket.getTicketType())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ticket.fxml"));
                Parent ticketPreview = loader.load();
                TicketController ticketController = loader.getController();
                ticketController.setEventTicketData(event, selectedUser);

                // Generate Bar & QR code images for the event ticket
                Image qrCodeImage = BarQRCodeUtil.generateQRCodeImage(uniqueID);
                Image barCodeImage = BarQRCodeUtil.generateBarcodeImage(uniqueID);
                ticketController.setQRCodeImage(qrCodeImage);
                ticketController.setBarCode(barCodeImage);

                spTicketPreview.getChildren().clear();
                spTicketPreview.getChildren().add(ticketPreview);
            }
        } catch(IOException | WriterException e){
            showAlert("Error", "There was an error updating the ticket preview");
        }
    }

    /**
     * Initializes toggle buttons to update the visibility og ticket types
     * The visibility of Special Ticket and Event Ticket is updated based on selected toggle button
     */
    private void initToggleBtns() {
        tBtnSpecial.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateVisibility(newValue, false);
        });

        tBtnEvent.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateVisibility(false, newValue);
        });
    }

    /**
     * Updates the visibility of UI components based on the slected toggle buttons
     * If Special Ticket is selected, set visibility for special ticket related components
     * if Event Tickets is selected, set visibility for event ticket related components
     * @param specialSelected if special ticket is selected
     * @param eventSelected if event ticket is selected
     */

    private void updateVisibility(boolean specialSelected, boolean eventSelected) {
        lblSeleUser.setVisible(eventSelected);
        tfSearch.setVisible(eventSelected);
        lvAllUsers.setVisible(eventSelected);
        btnMailTicket.setVisible(eventSelected);
        spinnerSpecTickets.setVisible(specialSelected);
        lblSeleTicket.setVisible(specialSelected || eventSelected);
        lvRadioBtns.setVisible(specialSelected || eventSelected);

        if (eventSelected) {
                populateRadioButtonsForEventTickets(event);
        } else if (specialSelected) {
                populateRadioButtonsForSpecialTickets();
        }
    }

    /**
     * Populates radio buttons for event tickets associated with the given event
     * Retrieves the list of tickets linked to the specified event and populates radio buttons
     * @param event the event object
     */
    private void populateRadioButtonsForEventTickets(Event event) {
        try {
            List<Ticket> ticketsForEvent = model.getLinkedTickets(event.getEventID());
            populateRadioButtons(ticketsForEvent);
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while retrieving data");
        }
    }

    /**
     * Retrieves the list of special tickets and populates radio buttons
     */
    private void populateRadioButtonsForSpecialTickets() {
        try {
            List<Ticket> specialTickets = model.getSpecialTickets();
            populateRadioButtons(specialTickets);
        } catch (SQLException | IOException e) {
            showAlert("Error", "Error occurred while retrieving data");
        }
    }

    /**
     * Populates radio buttons with the provided list of tickets
     * Clears existing radio buttons and creates a new radio button for each ticket in the list
     * @param tickets list of tickets
     */
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

    /**
     * Initialize a listener for the search field
     */
    private void initSearchListener() {
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            List<User> filteredUsers = SearchUtility.filterUsers(allUsers, newValue);
            lvAllUsers.setItems(FXCollections.observableArrayList(filteredUsers));
        });
    }

    /**
     * Loads all the users from the model and populates the listview
     */
    private void loadAllUsers() {
        try {
            allUsers = FXCollections.observableArrayList(model.getAllUsers());
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while retrieving data");
        }
        lvAllUsers.setItems(allUsers);
    }

    /**
     * Handle the deletion of the current event
     * Displays confirmation box
     * Deletes event if confirmed
     */
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
                } catch (SQLException | IOException e) {
                    showAlert("Error", "An error occurred while deleting event");
                }
                loadFXML("/EventWindow.FXML", model, (Stage) lblUsername.getScene().getWindow());
            }
        });

    }

    /**
     * handle the editing of the current event, loads the edit event window
     */
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
            showAlert("Error", "An error occurred while loading the window");
        }
    }

    /**
     * handle the printing of tickets
     * Generate a UUID for the ticket
     * If Event Ticket is selected, then links the ticket to selected user nad prints the ticket preview
     * If special Ticket is selected, links multiple tickets to the event and prints.
     */
    @FXML
    private void onPrintTicket(ActionEvent actionEvent) {
        if (currentSelectedTicket != null) {
            try {
                if (tBtnEvent.isSelected()) {
                    printNode(spTicketPreview);
                    model.linkTicketToUser(currentSelectedTicket.getTicketID(), currentSelectedUser.getUserID(), event.getEventID(), currentSelectedTicket.getUuid());
                } else if (tBtnSpecial.isSelected()) {
                    int numberOfTickets = (int) spinnerSpecTickets.getValue();
                    for (int i = 0; i < numberOfTickets; i++) {
                        generateNewUUIDForTicket();
                        updateTicketPreview(currentSelectedTicket);
                        printNode(spTicketPreview);
                        model.linkSpecialTicket(currentSelectedTicket.getTicketID(), currentSelectedTicket.getUuid());
                    }

                }
            } catch (SQLException | IOException e) {
                showAlert("Error", "An error occurred while saving or printing the ticket");
            }
        } else {
            showInformationAlert("Warning", "Please select a ticket type");
        }
    }

    /**
     * Prints the specified JavaFX Node
     * Creates a WritableImage snapshot of the node
     *  Prints the node using a PrinterJob and shows a success or warning alert based on the printing result
     * @param node The JavaFX Node to be printed
     */
    private void printNode(Node node) {
            WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
            ImageView imageView = new ImageView(snapshot);

            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
                boolean success = job.printPage(imageView);
                if (success) {
                    job.endJob();
                    showInformationAlert("Success", "The ticket has been printed successfully");
                } else {
                    showInformationAlert("Warning", "An error occurred while printing the ticket");
                }
            } else {
                showInformationAlert("Warning", "No printer selected");
            }
    }

    /**
     * Sends the ticket to the selected user's email
     *  The email sending process is performed in a separate thread to avoid blocking the JavaFX application thread
     * @param actionEvent
     */
    @FXML
    private void onMailTicket(ActionEvent actionEvent) {
        if (currentSelectedTicket != null && currentSelectedUser != null){
                try {
                    String host = "smtp.simply.com";
                    String port = "587";
                    String mailFrom = "brewpass@xn--jonasdomne-k6a.dk";
                    String password = "K0de.123";
                    String toEmail = currentSelectedUser.getEmail();
                    String subject = "Your Ticket";
                    String message = "here is your Ticket!";

                    // Takes a snapshot of the ticket
                    WritableImage image = spTicketPreview.snapshot(new SnapshotParameters(), null);
                    String imagePath = "resources/Images/App/CustomerTicket"; // Opdater denne sti korrekt
                    File file = new File(imagePath);
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    // Moving the email-send thread to a different Thread
                    new Thread(() -> {
                        try {
                            MailUtility.sendEmailWithAttachment(host, port, mailFrom, password, toEmail, subject, message, imagePath);
                            model.linkTicketToUser(currentSelectedTicket.getTicketID(), currentSelectedUser.getUserID(), event.getEventID(), currentSelectedTicket.getUuid());
                            Platform.runLater(() -> {
                                showInformationAlert("Success", "the email has been sent");
                            });
                        } catch (MessagingException | IOException | SQLException e) {
                            Platform.runLater(() -> {
                                showAlert("Error", "Error when sending the email");
                            });
                        }
                    }).start();

                } catch (IOException e) {
                    showAlert("Error", "Could not save image");
                }
        } else showAlert("Error", "Please select a user and ticket to send");
    }

    /**
     * Populates the fields with information about the given event
     * @param event the event object
     */
    public void populateFields(Event event) {
        try {
            this.event = event;

            lvAllUsers.setItems(model.getAllUsers());
            lblEventName.setText(event.getName());
            lblInfo.setText(event.getTime() + " || " + event.getLocation());
            taEventNotes.setText(event.getNote());
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while retrieving data");
        }

        try {
            int soldTickets = eventModel.getSoldTicketsCount(event.getEventID());
            lblTicketCounter.setText(String.valueOf(soldTickets) + " / " + event.getTicketLimit());
        } catch (Exception e) {
            showAlert("Error", "An error occurred while retrieving data");
        }

    }

}

