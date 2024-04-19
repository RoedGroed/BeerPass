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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewEventController extends BaseController implements Initializable {

    @FXML
    private TextField tfEventName;
    @FXML
    private TextField tfEventLocation;
    @FXML
    private TextField tfEventTime;
    @FXML
    private TextArea taEventNotes;
    @FXML
    private TextField tfMaxAttendees;
    @FXML
    private Label lblSelectedImage;
    @FXML
    private ImageView imgEventImage;
    @FXML
    private ListView<User> lvCoordinators;
    @FXML
    private ListView<User> lvAllCoordinators;
    @FXML
    private DatePicker dpEventDate;
    private EventModel eventModel;
    private List<User> selectedCoordinators = new ArrayList<>();


    @FXML
    private ComboBox<String> cbEventImages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        eventModel = new EventModel();
        loadImageViewer();
        loadImagesIntoComboBox();
        populateList();

    }
    @FXML
    private void onCancel(ActionEvent actionEvent) {
        loadFXML("/EventWindow.FXML",model, (Stage) tfEventName.getScene().getWindow());
    }

    @FXML
    private void onConfirmUser(ActionEvent actionEvent) {
        Event event = getUserInput();

        if (event != null){

            try {
                eventModel.createEvent(event);
                loadFXML("/EventWindow.FXML",model, (Stage) tfEventName.getScene().getWindow());
            } catch (SQLException | IOException e) {
                showAlert("Error", "Error occurred while creating a user, try again");
            }
        }

    }


    private Event getUserInput(){
        String name = tfEventName.getText();
        String location = tfEventLocation.getText();
        LocalDate timeDate = dpEventDate.getValue();
        String timeStart = tfEventTime.getText();
        String note = taEventNotes.getText(); // set a limit to 255 NVARS
        int ticketLimit;
        try {
            ticketLimit = Integer.parseInt(tfMaxAttendees.getText());
        } catch (NumberFormatException e) {
            showInformationAlert("Warning", "Invalid input for max attenders, please inter a valid integer");
            return null;
        }
        String imagePath = cbEventImages.getValue();

        if (!eventModel.validateInputs(name, location, timeDate, timeStart, note, ticketLimit,imagePath)) {
            return null;
        }

        String time = eventModel.formatTimeToString(timeDate, timeStart);

        return new Event(-1, name, location, time, note, ticketLimit, imagePath);
    }

    private void loadImageViewer(){
        cbEventImages.setOnAction(event -> {
            String selectedImage = cbEventImages.getValue();
            if (selectedImage != null) {

                String imagePath = "resources/Images/App/" + selectedImage;
                Image image = new Image(new File(imagePath).toURI().toString());
                imgEventImage.setImage(image);
            }
        });
    }

    private void loadImagesIntoComboBox(){
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

    public void populateList() {
        try {
            List<User> coordinators = eventModel.readAllEventCoordinators();
            lvAllCoordinators.getItems().addAll(coordinators);

            lvAllCoordinators.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            lvAllCoordinators.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    selectedCoordinators.add(newValue);
                }
            });
        } catch (SQLException | IOException e) {
            showAlert("Error", "An error occurred while fetching event coordinators.");
        }
    }


    @FXML
    private void onAddCoordinator(ActionEvent actionEvent) {
        User selectedUser = lvAllCoordinators.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            lvAllCoordinators.getItems().remove(selectedUser);
            lvCoordinators.getItems().add(selectedUser);
        } else {
            showInformationAlert("Warning", "Please select an Event Coordinator\r" +
                    "to add to this event");
        }
    }

    @FXML
    private void onRemoveCoordinator(ActionEvent actionEvent) {
        User selectedUser = lvCoordinators.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            lvCoordinators.getItems().remove(selectedUser);
            lvAllCoordinators.getItems().add(selectedUser);
        } else {
            showInformationAlert("Warning", "Please select and Event Coordinator to remove");
        }
    }


}
