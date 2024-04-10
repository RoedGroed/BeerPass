package GUI.Controller.Event;

import BE.Event;
import BE.User;
import GUI.Controller.BaseController;
import GUI.Model.EventModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    @FXML
    private void onCancel(ActionEvent actionEvent) {
        loadFXML("/SpecificEvent.FXML", model, (Stage) tfEventName.getScene().getWindow());
    }

    @FXML
    private void onConfirmEvent(ActionEvent actionEvent) throws SQLException, IOException {
        getUserInput(event);
        eventModel.updateEvent(event);

        loadFXML("/EventWindow.FXML", model, (Stage) tfEventName.getScene().getWindow());
    }

    public void getUserInput(Event event){
        event.setName(tfEventName.getText());
        event.setLocation(tfEventLocation.getText());

        String timeStart= tfEventTime.getText();
        LocalDate timeDate = dpEventDate.getValue();
        String time = eventModel.formatTimeToString(timeDate,timeStart);
        event.setTime(time);

        event.setNote(taEventNotes.getText());
        event.setImagePath(cbEventImages.getValue());
        event.setTicketLimit(Integer.parseInt(tfMaxAttendees.getText()));

    }

    @FXML
    private void onAddCoordinator(ActionEvent actionEvent) {
        User selectedUser = lvAllCoordinators.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            lvCoordinators.getItems().add(selectedUser);
            lvAllCoordinators.getItems().remove(selectedUser);

            try {
                eventModel.addCoordinator(event.getEventID(), selectedUser.getUserID());
            } catch (IOException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Failed to add coordinator to the event. Please try again later.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please Select an Event Coordinator\r" +
                    "that should be added to this event");
            alert.showAndWait();
        }
    }

    @FXML
    private void onRemoveCoordinator(ActionEvent actionEvent){
        User selectedUser = lvCoordinators.getSelectionModel().getSelectedItem();
        if (selectedUser != null){
            lvAllCoordinators.getItems().add(selectedUser);
            lvCoordinators.getItems().remove(selectedUser);

            try{
                eventModel.removeCoordinator(event.getEventID(), selectedUser.getUserID());
            } catch (IOException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Failed to remove coordinator to the event. Please try again later.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please Select an Event Coordinator\r" +
                            "that should be removed from this event");
            alert.showAndWait();
        }
    }

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

    //FIXME Find a more elegant solution to load the correct image. I cant just call ComboBoxSetup.
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

    public void populateCoordinatorLists(int eventId) {
        try {

            List<User> allCoordinators = eventModel.readAllEventCoordinators();

            List<User> assignedCoordinators = eventModel.readAllAssignedEventCoordinators(eventId);

            lvAllCoordinators.getItems().addAll(allCoordinators);

            lvCoordinators.getItems().addAll(assignedCoordinators);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /* TODO: Populate the Event Coordinator lists, so that i can assign them in the database, and remove them.
        Remember to update the lists.
        Look into how its done in admin, and just filter only event coordinator through, and link them via the DAO
     */

}
