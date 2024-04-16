package GUI.Controller.Event;

import BE.Event;
import BE.Ticket;
import BE.User;
import GUI.Controller.BaseController;
import GUI.Controller.Ticket.SpecialTicketController;
import GUI.Controller.Ticket.TicketController;
import GUI.Model.EventModel;
import Util.BarQRCodeUtil;
import Util.PrintAndMailUtility;
import Util.QRCodeGenerator;
import com.google.zxing.WriterException;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import jakarta.mail.MessagingException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.Printer;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
    @FXML
    private Spinner spinnerSpecTickets;

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

                // Generate QR code image for the special ticket
                Image qrCodeImage = QRCodeGenerator.generateQRCodeImage(selectedTicket.getTicketData(), selectedTicket.getUuid(), 400, 400);
                Image barCodeImage = QRCodeGenerator.generateBarcodeImage(selectedTicket.getTicketData(), selectedTicket.getUuid(), 400, 400);
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

                // Generate QR code image for the event ticket
                Image qrCodeImage = QRCodeGenerator.generateQRCodeImage(selectedTicket.getTicketData(), selectedTicket.getUuid(), 400, 400);
                Image barCodeImage = QRCodeGenerator.generateBarcodeImage(selectedTicket.getTicketData(), selectedTicket.getUuid(), 400, 400);
                System.out.println("UUID used for barcode image: " + selectedTicket.getUuid());
                ticketController.setQRCodeImage(qrCodeImage);
                ticketController.setBarCode(barCodeImage);

                spTicketPreview.getChildren().clear();
                spTicketPreview.getChildren().add(ticketPreview);
            }
        } catch(IOException e){
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

    private void printTicket() {
        Printer printer = Printer.getDefaultPrinter();
        PrinterJob printerJob = PrinterJob.createPrinterJob();

        if(printerJob != null) {
            PageLayout pageLayout = printerJob.getPrinter().getDefaultPageLayout();
            printerJob.getJobSettings().setPageLayout(pageLayout);

            // Take a snapshot of the StackPane contents
            WritableImage snapshot = spTicketPreview.snapshot(new SnapshotParameters(), null);
            ImageView imageView = new ImageView(snapshot);

            // Check to ensure that the ImageView node has been created and the printerJob has been created
            if(imageView != null) {
                if(printerJob.showPrintDialog(spTicketPreview.getScene().getWindow())) {
                    boolean printed = printerJob.printPage(imageView);
                    if(printed){
                        printerJob.endJob();
                    }
                }
            } else {
                System.err.println("Error creating image view.");
            }
        } else {
            System.err.println("Error creating print job.");
        }
    }

    @FXML
    private void onPrintTicket(ActionEvent actionEvent) {
        if (tBtnEvent.isSelected()) {
            printEventTicket();
        } else if (tBtnSpecial.isSelected()) {
            generateAndPrintSpecialTickets();
        }
    }

    private void printEventTicket() {
        if (lvAllUsers.getSelectionModel().getSelectedItem() != null) {
            if (ticketToggleGroup.getSelectedToggle().getUserData().toString() != null) {
                // Find the ticket in the tickettoggleGroup and then get the ID from it
                Ticket selected = (Ticket) ticketToggleGroup.getSelectedToggle().getUserData();
                int ticketID = selected.getTicketID();
                int eventID = event.getEventID();
                int userID = lvAllUsers.getSelectionModel().getSelectedItem().getUserID();
                try {
                    if (eventModel.getSoldTicketsCount(eventID) <= event.getTicketLimit()) {
                        model.linkTicketToUser(userID, ticketID, eventID);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "There are no more tickets available to this event");
                        alert.showAndWait();
                    }
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

        // Print the event ticket
        printTicket();
    }

    private void generateAndPrintSpecialTickets() {
        // Popup dialog to input the number of special tickets to generate
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Generate Special Tickets");
        dialog.setHeaderText("Enter the number of special tickets to generate:");
        dialog.setContentText("Number of tickets:");

        // Show the dialog and wait for the user's input
        Optional<String> result = dialog.showAndWait();

        // Process the user's input
        result.ifPresent(number -> {
            try {
                int numTickets = Integer.parseInt(number);
                generateSpecialTickets(numTickets);
                // Print the special tickets after generating
                printTicket();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number.");
                alert.showAndWait();
            }
        });
    }

    private void generateSpecialTickets(int numTickets) {
        try {
            RadioButton selectedRadioButton = (RadioButton) ticketToggleGroup.getSelectedToggle();
            String selectedTicketName = selectedRadioButton.getText();

            for (int i = 0; i < numTickets - 1; i++) {
                Ticket specialTicket = new Ticket();
                // Set the ticket type and other details as needed
                // For example:
                specialTicket.setTicketType("Special Ticket");
                specialTicket.setTicketName(selectedTicketName);
                // Generate a unique UUID for the ticket
                String uuid = UUID.randomUUID().toString();
                specialTicket.setUuid(UUID.fromString(uuid));

                // Update the ticket preview with the special ticket
                updateTicketPreview(specialTicket);

                // Print each special ticket after generating
                printTicket();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error generating special tickets.");
            alert.showAndWait();
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
                        if (eventModel.getSoldTicketsCount(eventID) <= event.getTicketLimit()) {
                            model.linkTicketToUser(userID, ticketID, eventID);
                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "There are no more tickets available to this event");
                            alert.showAndWait();
                        }
                    } catch (SQLException | IOException e) {
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

    /*@FXML
    private void onPrintTicket(ActionEvent actionEvent) {
        if (tBtnSpecial.isSelected()) {
            int numberOfTickets = (int) spinnerSpecTickets.getValue();
            for (int i = 0; i < numberOfTickets; i++) {
                String uniqueCode = BarQRCodeUtil.generateUUID();
                try {
                    Image barcodeImage = BarQRCodeUtil.generateBarcode(uniqueCode, 300, 100);
                    Image qrCodeImage = BarQRCodeUtil.generateQRCode(uniqueCode, 300, 300);
                    Node ticketNode = createTicketNode(barcodeImage, qrCodeImage);
                    PrintAndMailUtility.printTicket(ticketNode);
                    //model.saveSpecialTicket(uniqueCode); // Gemmer hver billet med sin unikke kode
                } catch (WriterException | IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (tBtnEvent.isSelected() && currentSelectedUser != null) {
            Node ticketNode = spTicketPreview; // Antag at denne node repræsenterer den aktuelle billet
            PrintAndMailUtility.printTicket(ticketNode);
        }
    }*/

    /*@FXML
    private void onMail(ActionEvent event) {
        if (tBtnEvent.isSelected() && currentSelectedUser != null) {
            try {
                String uniqueCode = BarQRCodeUtil.generateUUID();
                Image qrCodeImage = BarQRCodeUtil.generateQRCode(uniqueCode, 300, 300);
                PrintAndMailUtility.sendTicketByEmail(currentSelectedUser.getEmail(), "Din Event Billet", "Her er din QR-kode billet.", qrCodeImage);
            } catch (WriterException | MessagingException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*private Node createTicketNode(Image barcodeImage, Image qrCodeImage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/TicketLayout.fxml")); // Antag at du har en FXML fil for billet layout
        Parent layout = loader.load();

        // Sæt billeder
        ((ImageView) layout.lookup("#barCode")).setImage(barcodeImage);
        ((ImageView) layout.lookup("#qrCode")).setImage(qrCodeImage);

        // Du kan også tilpasse andre elementer baseret på billetdata, hvis nødvendigt
        return layout;
    }*/



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

