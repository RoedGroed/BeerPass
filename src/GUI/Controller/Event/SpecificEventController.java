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


    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        eventModel = new EventModel();
        initToggleBtns();
        initListenForPreview();
        initSearchListener();
        loadAllUsers();


    }
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

    private void initToggleBtns() {
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

    private void populateRadioButtonsForEventTickets(Event event) {
        try {
            List<Ticket> ticketsForEvent = model.getLinkedTickets(event.getEventID());
            populateRadioButtons(ticketsForEvent);
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while retrieving data");
        }
    }

    private void populateRadioButtonsForSpecialTickets() {
        try {
            List<Ticket> specialTickets = model.getSpecialTickets();
            populateRadioButtons(specialTickets);
        } catch (SQLException | IOException e) {
            showAlert("Error", "Error occurred while retrieving data");
        }
    }

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

    private void initSearchListener() {
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            List<User> filteredUsers = SearchUtility.filterUsers(allUsers, newValue);
            lvAllUsers.setItems(FXCollections.observableArrayList(filteredUsers));
        });
    }

    private void loadAllUsers() {
        try {
            allUsers = FXCollections.observableArrayList(model.getAllUsers());
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while retrieving data");
        }
        lvAllUsers.setItems(allUsers);
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
                } catch (SQLException | IOException e) {
                    showAlert("Error", "An error occurred while deleting event");
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
            showAlert("Error", "An error occurred while loading the window");
        }
    }


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


    @FXML
    private void onMailTicket(ActionEvent actionEvent) {
        if (currentSelectedTicket != null && currentSelectedUser != null){
            // Kør på JavaFX-tråden
            Platform.runLater(() -> {
                try {
                    String host = "smtp.simply.com";
                    String port = "587";
                    String mailFrom = "brewpass@xn--jonasdomne-k6a.dk";
                    String password = "K0de.123";
                    String toEmail = currentSelectedUser.getEmail();
                    String subject = "Din Billet";
                    String message = "Her er din billet!";

                    // Tag et snapshot af billetten
                    WritableImage image = spTicketPreview.snapshot(new SnapshotParameters(), null);
                    String imagePath = "resources/Images/App/Tickets/CustomerTicket.png"; // Opdater denne sti korrekt
                    File file = new File(imagePath);
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

                    // Her flyttes e-mail-sending til en anden tråd
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
            });
        } else showAlert("Error", "Please select a user and ticket to send");
    }

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

        //populateTickets(event);
        try {
            int soldTickets = eventModel.getSoldTicketsCount(event.getEventID());
            lblTicketCounter.setText(String.valueOf(soldTickets) + " / " + event.getTicketLimit());
        } catch (Exception e) {
            showAlert("Error", "An error occurred while retrieving data");
        }

    }

}

