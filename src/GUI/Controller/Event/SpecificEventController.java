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
        String uniqueID = BarQRCodeUtil.generateUniqueID();

        try {
            if ("Special Ticket".equals(selectedTicket.getTicketType())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SpecialTicket.fxml"));
                Parent ticketPreview = loader.load();
                SpecialTicketController specialTicketController = loader.getController();
                specialTicketController.setSpecialTicketData(selectedTicket);

                // Generate Bar & QR code images for the special ticket
                Image qrCodeImage = BarQRCodeUtil.generateQRCodeImage(uniqueID);
                Image barCodeImage = BarQRCodeUtil.generateBarcodeImage(uniqueID);
                System.out.println("UUID used for barcode image: " + selectedTicket.getUuid());


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
                System.out.println("UUID used for barcode image: " + selectedTicket.getUuid());
                ticketController.setQRCodeImage(qrCodeImage);
                ticketController.setBarCode(barCodeImage);

                spTicketPreview.getChildren().clear();
                spTicketPreview.getChildren().add(ticketPreview);
            }
        } catch(IOException | WriterException e){
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
        spinnerSpecTickets.setVisible(specialSelected);
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
            e.printStackTrace();
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
        if (currentSelectedTicket != null) {
            try {
                if (tBtnEvent.isSelected()) {
                    model.linkTicketToUser(currentSelectedTicket.getTicketID(), currentSelectedUser.getUserID(), event.getEventID(), currentSelectedTicket.getUuid());
                    printNode(spTicketPreview);  // Her udskrives hele panelet
                } else if (tBtnSpecial.isSelected()) {
                    int numberOfTickets = (int) spinnerSpecTickets.getValue();
                    for (int i = 0; i < numberOfTickets; i++) {
                        updateTicketPreview(currentSelectedTicket);
                        printNode(spTicketPreview);  // Her udskrives hele panelet
                    }
                } else {
                    throw new IllegalStateException("Ingen billettype valgt");
                }
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Der skete en fejl under gemning eller udskrivning af billetten.");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Vælg venligst en gyldig billet først.");
            alert.show();
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
                System.out.println("Billetten er blevet printet succesfuldt fra snapshot.");
            } else {
                System.out.println("Der skete en fejl under udskrivningen af snapshot.");
            }
        } else {
            System.out.println("Ingen printer valgt eller fejl i printerdialogen.");
        }
    }


    @FXML
    private void onMailTicket(ActionEvent actionEvent) {
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
                String imagePath = "resources/Images/App/CustomerTicket"; // Opdater denne sti korrekt
                File file = new File(imagePath);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);

                // Her kan du flytte e-mail-sending til en anden tråd hvis nødvendigt
                new Thread(() -> {
                    try {
                        MailUtility.sendEmailWithAttachment(host, port, mailFrom, password, toEmail, subject, message, imagePath);
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "E-mailen er blevet sendt.");
                            alert.showAndWait();
                        });
                    } catch (MessagingException e) {
                        Platform.runLater(() -> {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Fejl ved sending af e-mail: " + e.getMessage());
                            alert.showAndWait();
                        });
                        e.printStackTrace();
                    }
                }).start();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Kunne ikke gemme billede: " + e.getMessage());
                alert.show();
                e.printStackTrace();
            }
        });
    }





    public void populateFields(Event event) throws SQLException, IOException {
        this.event = event;

        lvAllUsers.setItems(model.getAllUsers());
        lblEventName.setText(event.getName());
        lblInfo.setText(event.getTime() + " || " + event.getLocation());
        taEventNotes.setText(event.getNote());

        //populateTickets(event);
        try {
            int soldTickets = eventModel.getSoldTicketsCount(event.getEventID());
            lblTicketCounter.setText(String.valueOf(soldTickets) + " / " + event.getTicketLimit());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

