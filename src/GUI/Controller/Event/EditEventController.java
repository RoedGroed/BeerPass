package GUI.Controller.Event;

import BE.Event;
import BE.User;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
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

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EditEventController extends BaseController implements Initializable {

    @FXML
    private TextField tfEventName;
    @FXML
    private TextField tfEventLocation;
    @FXML
    private TextField tfEventTime;
    @FXML
    private TextField tfMaxAttendees;
    @FXML
    private DatePicker dpEventDate;
    @FXML
    private ImageView imgEventImage;
    @FXML
    private TextArea taEventNotes;

    @FXML
    private ListView<User> lvCoordinators;
    @FXML
    private ListView<User> lvAllCoordinators;
    private Event event;
    private EventModel eventModel;

    @FXML
    private ComboBox<String> cbEventImages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        eventModel = new EventModel();
        loadImageViewer();
        loadImagesIntoComboBox();
    }

    /**
     * Goes back to specificEvent window
     */
    @FXML
    private void onCancel(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SpecificEvent.fxml"));
            Parent root = loader.load();

            SpecificEventController controller = loader.getController();
            controller.populateFields(event);

            Stage stage = new Stage();
            stage.setTitle(event.getName());
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) tfEventTime.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            showAlert("Error", "Could not load window");
        }
    }

    /**
     * handle the conformation of event details
     */
        @FXML
    private void onConfirmEvent(ActionEvent actionEvent) {
        getUserInput(event);
        String maxAttendeesText = tfMaxAttendees.getText();
        if (!maxAttendeesText.isEmpty()) {
            try {
                int maxAttendees = Integer.parseInt(maxAttendeesText);
                if (eventModel.validateInputs(tfEventName.getText(), tfEventLocation.getText(), dpEventDate.getValue(),
                        tfEventTime.getText(), taEventNotes.getText(), maxAttendees, cbEventImages.getValue())) {
                    eventModel.updateEvent(event);
                    loadFXML("/EventWindow.FXML", model, (Stage) tfEventName.getScene().getWindow());
                }
                /// A Hack to circumvent if parsing MaxAttendees from string to an integer fails.
            } catch (NumberFormatException e) {
                showInformationAlert("Warning", "Invalid input for maximum attenders, Please enter a valid integer");
            } catch (SQLException | IOException e) {
              showAlert("Error", "Error while loading the window, try again");
            }
        } else {
            showInformationAlert("Warning", "Invalid input for maximum attenders, Field can't be empty");
        }
    }

    /**
     * Retrieves user input and sets it to the specified event object
     * @param event the event
     */
    public void getUserInput(Event event) {
        event.setName(tfEventName.getText());
        event.setLocation(tfEventLocation.getText());

        String timeStart = tfEventTime.getText();
        LocalDate timeDate = dpEventDate.getValue();
        String time = eventModel.formatTimeToString(timeDate, timeStart);
        event.setTime(time);

        event.setNote(taEventNotes.getText());
        event.setImagePath(cbEventImages.getValue());

        // Validate and parse ticket limit only if it's not empty
        String ticketLimitText = tfMaxAttendees.getText();
        if (!ticketLimitText.isEmpty()) {
            event.setTicketLimit(Integer.parseInt(ticketLimitText));
        }
    }

    /**
     * Handle the addition of an Event Coordinator to the event
     */
    @FXML
    private void onAddCoordinator(ActionEvent actionEvent) {
        User selectedUser = lvAllCoordinators.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            lvCoordinators.getItems().add(selectedUser);
            lvAllCoordinators.getItems().remove(selectedUser);

            try {
                eventModel.addCoordinator(event.getEventID(), selectedUser.getUserID());
            } catch (IOException | SQLException e) {
                showAlert("Error", "Failed to add coordinator to event");
            }
        } else {
            showInformationAlert("Warning", "Please select an Event Coordinator\r" + "that should be added to this event");
        }
    }

    /**
     * Handle the removal of an Event Coordinator from the event
     */
    @FXML
    private void onRemoveCoordinator(ActionEvent actionEvent){
        User selectedUser = lvCoordinators.getSelectionModel().getSelectedItem();
        if (selectedUser != null){
            lvAllCoordinators.getItems().add(selectedUser);
            lvCoordinators.getItems().remove(selectedUser);

            try{
                eventModel.removeCoordinator(event.getEventID(), selectedUser.getUserID());
            } catch (IOException | SQLException e) {
                showAlert("Error", "Failed to remove coordinator from the event. PLease try again");
            }
        } else {
            showInformationAlert("Warning", "Please Select an Event Coordinator\r" +
                    "that should be removed from this event");
        }
    }

    /**
     * Populates the fields with information from the provided event object
     * @param event the event object
     */
    public void populateFields(Event event){
        this.event = event;

        tfEventName.setText(event.getName());
        tfEventLocation.setText(event.getLocation());
        tfEventTime.setText(event.getTime());
        tfMaxAttendees.setText(String.valueOf(event.getTicketLimit()));

        // Gets the Date as a LocalDate.
        String[] separatedDateTime = eventModel.divorceTimeString(event.getTime());
        LocalDate date = LocalDate.parse(separatedDateTime[0]);
        // Populating the Date and Time.
        tfEventTime.setText(separatedDateTime[1]);
        dpEventDate.setValue(date);

        taEventNotes.setText(event.getNote());

        comboBoxValue();

    }

    /**
     * Sets the value of the combobox to the current image filename if it matches
     * and loads the corresponding image
     */
    private void comboBoxValue(){
        String currentImage = event.getImagePath();
        boolean currentImageFound = false;
        for (String image : cbEventImages.getItems()) {
            // Set the value of the ComboBox to the current image filename if it matches
            if (image.equals(currentImage)) {
                cbEventImages.setValue(image);
                currentImageFound = true;
                break;
            }
        }
        if (!currentImageFound) {
            cbEventImages.setPromptText("Select Image");
        }

        // Load the image corresponding to the selected value
        String selectedImage = cbEventImages.getValue();
        if (selectedImage != null) {
            String imagePath = "resources/Images/App/" + selectedImage;
            Image image = new Image(new File(imagePath).toURI().toString());
            imgEventImage.setImage(image);
        }
    }

    /**
     * Loads the selected image into the ImageViewer
     */
    public void loadImageViewer(){
        cbEventImages.setOnAction(event -> {
            String selectedImage = cbEventImages.getValue();
            if (selectedImage != null) {
                String imagePath = "resources/Images/App/" + selectedImage;
                Image image = new Image(new File(imagePath).toURI().toString());
                imgEventImage.setImage(image);
            }
        });
    }

    /**
     * Loads the images from the specified directory into the combo boxes
     */
    public void loadImagesIntoComboBox(){
        File folder = new File("resources/Images/App/");

        if(folder.exists() && folder.isDirectory()) {
            File[] imageList = folder.listFiles();
            if (imageList != null) {
                for (File file : imageList) {
                    if (file.isFile()) {
                        cbEventImages.getItems().add(file.getName());
                    }
                }
            }
        }
    }

    /**
     * Populates the lists of all coordinators and assigned coordinators for the event
     * @param eventId the event id
     */
    public void populateCoordinatorLists(int eventId) {
        try {

            List<User> allCoordinators = eventModel.readAllEventCoordinators();

            List<User> assignedCoordinators = eventModel.readAllAssignedEventCoordinators(eventId);

            lvAllCoordinators.getItems().addAll(allCoordinators);

            lvCoordinators.getItems().addAll(assignedCoordinators);
        } catch (SQLException | IOException e) {
            showAlert("Error", "An database error occurred, contact support");
        }
    }

    /* TODO:
        Make it such that there is verification on length of the all strings in the textfields.
        So it doesnt ruin the ticket preview etc.
        This is "done", but numbers need to be adjusted if important.
     */

}
