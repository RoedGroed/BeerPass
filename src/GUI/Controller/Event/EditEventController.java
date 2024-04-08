package GUI.Controller.Event;

import BE.Event;
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
import java.net.URL;
import java.time.LocalDate;
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
    private ListView<?> lvCoordinators;
    @FXML
    private ListView<?> lvAllCoordinators;
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
    private void onConfirmEvent(ActionEvent actionEvent) {
        // Show and Wait Box
        loadFXML("/SpecificEvent.FXML", model, (Stage) tfEventName.getScene().getWindow());
    }

    @FXML
    private void onAddCoordinator(ActionEvent actionEvent){}

    @FXML
    private void onRemoveCoordinator(ActionEvent actionEvent){}

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
                // TODO: Ændre når vi har nogle event images til den nye file path
                String imagePath = "resources/Images/App/" + selectedImage;
                Image image = new Image(new File(imagePath).toURI().toString());
                imgEventImage.setImage(image);
            }
        });
    }

    public void loadImagesIntoComboBox(){
        // TODO: Ændre når vi har nogle event images til den nye file path
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

}
